package com.oxchains.themis.order.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.order.common.OrdersKeyAmount;
import com.oxchains.themis.order.common.Pojo;
import com.oxchains.themis.order.common.ShamirUtil;
import com.oxchains.themis.order.entity.*;
import com.oxchains.themis.order.repo.*;
import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Created by huohuo on 2017/10/23.
 */
@Service
public class OrderService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate;
    private OrderRepo orderRepo;
    private NoticeRepo noticeRepo;
    private UserRepo userRepo;
    private OrderArbitrateRepo orderArbitrateRepo;
    @Autowired
    private OrderAddresskeyRepo orderAddresskeyRepo;
    @Autowired
    private UserTxDetailRepo userTxDetailRepo;
    @Autowired
    private OrderTransactionRepo transactionRepo;

    public OrderService(@Autowired OrderRepo orderRepo,@Autowired NoticeRepo noticeRepo,@Autowired UserRepo userRepo,@Autowired OrderArbitrateRepo orderArbitrateRepo,@Autowired RestTemplate restTemplate) {
        this.orderRepo = orderRepo;
        this.noticeRepo = noticeRepo;
        this.userRepo = userRepo;
        this.orderArbitrateRepo = orderArbitrateRepo;
        this.restTemplate = restTemplate;
    }
    /*
    * 工具类方法 用来在用户系统获取一对随机的公私匙
    * */
   public AddressKeys getAddressKeys(){
       AddressKeys ak = null;
       try {
           String   r = restTemplate.getForObject("http://themis-user/account/keys",String.class);
           JSONObject result= JSON.parseObject(r);
           int status= (int) result.get("status");
           if(status==1){
               Object o=result.get("data");
               ak = (AddressKeys) JsonUtil.fromJson(JsonUtil.toJson(o),AddressKeys.class);
           }
       } catch (RestClientException e) {
           LOG.info("get address key faild : ",e.getMessage());
           e.printStackTrace();
       }
       return  ak;
    };
    /*
    * 查询所有订单  用来测试
    * */
    public List<Orders> findOrders(){
        return IteratorUtils.toList(orderRepo.findAll().iterator());
    }
    /*
    * 对发布的公告 下一个订单 生成订单信息
    * */
    public Orders addOrders(Orders orders){
        Orders orders1 =null;
        try {
            //生成一条订单信息 订单状态为1 是否仲裁为0
            orders.setCreateTime(DateUtil.getPresentDate());
            orders.setId(DateUtil.getOrderId());
            //Notice notice = noticeRepo.findOne(orders.getNoticeId());
            orders.setOrderStatus(1L);
            orders.setArbitrate(0);
            orders1 =orderRepo.save(orders);

            //生成买家用户的公私匙 存到 订单买家卖家仲裁者表里 order_address_key 每个订单对应一条
            AddressKeys addressKeys = this.getAddressKeys();
            OrderAddresskeys orderAddresskeys = new OrderAddresskeys();
            orderAddresskeys.setOrderId(orders1.getId());
            orderAddresskeys.setBuyerPubAuth(addressKeys.getPublicKey());
            orderAddresskeys.setBuyerPriAuth(addressKeys.getPrivateKey());

            //生成仲裁者用户的公私匙 存到 订单买家卖家仲裁者表里 order_address_key 每个订单对应一条
            AddressKeys addressKeys1 = this.getAddressKeys();
            orderAddresskeys.setUserPubAuth(addressKeys1.getPublicKey());
            orderAddresskeys.setUserPriAuth(addressKeys1.getPrivateKey());
            orderAddresskeyRepo.save(orderAddresskeys);

            //将仲裁者用户的私匙 分为三个密匙碎片分别分给三个人 存储在 订单仲裁表里面 每个订单对应三条信息
            String[] strArr = ShamirUtil.splitAuth(addressKeys1.getPrivateKey());
            List<User> userList = userRepo.findUserByRoleId(3L);
            for(int i = 0;i<strArr.length;i++){
                OrderArbitrate orderArbitrate = new OrderArbitrate();
                orderArbitrate.setUserId(userList.get(i).getId());
                orderArbitrate.setOrderId(orders1.getId());
                orderArbitrate.setStatus(0);
                orderArbitrate.setUserAuth(strArr[i]);
                orderArbitrateRepo.save(orderArbitrate);
            }
        }catch (Exception e){
            LOG.info("add orders faild :",e.getMessage());
            e.printStackTrace();
        }
        this.setOrderStatusName(orders1);
        return orders1;
    }
    /*
    * 根据订单编号查询订单的详细信息
    * */
    public Orders findOrdersDetails(Pojo pojo){
        Orders o = orderRepo.findOne(pojo.getId());
        this.setOrderStatusName(o);
        if(pojo.getUserId()==null){
         return o;
        }
        if(o.getBuyerId()==pojo.getUserId()){
            o.setOrderType("购买");
            o.setFriendUsername(userRepo.findOne(o.getSellerId()).getLoginname());
        }
        else{
            o.setOrderType("出售");
            o.setFriendUsername(userRepo.findOne(o.getSellerId()).getLoginname());
        }
        return o;
    }

    /*
   * 根据id查询自己已完成的的订单
   * */
    public List<Orders> findCompletedOrdersById(Long id){
        List<Orders>  list= null;
        try {
            list = orderRepo.findOrdersByBuyerIdOrSellerIdAndOrderStatus(id,id,5L);
            for (Orders o:list) {
                if(o.getBuyerId()==id){
                    o.setOrderType("购买");
                    o.setFriendUsername(userRepo.findOne(o.getSellerId()).getLoginname());
                }
                else{
                    o.setOrderType("出售");
                    o.setFriendUsername(userRepo.findOne(o.getBuyerId()).getLoginname());
                }
                this.setOrderStatusName(o);
            }
        } catch (Exception e) {
            LOG.info("query complete order faild :",e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    /*
   * 根据id查询自己未完成的的订单
   * */
    public List<Orders> findNoCompletedOrdersById(Long id){
        List<Orders>  list= null;
        try {
            list = orderRepo.findOrdersByBuyerIdOrSellerIdAndOrderStatusIsNot(id,id,5L,6L);
            for (Orders o:list) {
                if(o.getBuyerId()==id){
                    o.setOrderType("购买");
                    o.setFriendUsername(userRepo.findOne(o.getSellerId()).getLoginname());

                }
                else{
                    o.setOrderType("出售");
                    o.setFriendUsername(userRepo.findOne(o.getBuyerId()).getLoginname());
                }
                this.setOrderStatusName(o);
            }
        } catch (Exception e) {
            LOG.info("query noComplete order faild :",e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    /*
    * 取消订单
    * */
    public Orders cancelOrders(String id,Long userId){

        Orders orders1 = null;
        try {
            Orders orders = orderRepo.findOne(id);
            if(orders.getOrderStatus()==1){
                //当订单状态为1 时 买家已拍下 但商家还未确认 可以直接取消订单 不采取任何操作
                orders.setOrderStatus(6L);
            }
            if(orders.getOrderStatus()==2){
                //买家的私匙给卖家
                OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(id);
                orderAddresskeys.setSellerBuyerPriAuth(orderAddresskeys.getBuyerPriAuth());
                orders.setOrderStatus(6L);
            }
            if(orders.getOrderStatus()==3){
                if(userId==orders.getBuyerId()){
                    //买家取消订单 证明收到退款 调用接口让BTC回到卖家地址 状态改为6
                    OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orders.getId());
                    orderAddresskeys.setSellerBuyerPriAuth(orderAddresskeys.getBuyerPriAuth());
                    orderAddresskeyRepo.save(orderAddresskeys);
                    orders.setOrderStatus(6L);
                }
                if(userId==orders.getSellerId()){
                    //卖家取消订单 状态改为7 等待买家收到退款
                    orders.setOrderStatus(7L);
                }
            }
            orders1 = orderRepo.save(orders);
            this.setOrderStatusName(orders1);
        } catch (Exception e) {
            LOG.info("cancel orders faild :",e.getMessage());
            e.printStackTrace();
        }
        return orders1;
    }
    /*
    * 发布公告人确认订单
    * description     发布公告的人确认交易
    * */
    public Orders confirmOrders(Pojo pojo){
        Orders o = null;
        try {
            o = orderRepo.findOne(pojo.getId());
            Notice notice = o.getNotice();
            if(notice.getUserId()==pojo.getUserId() && o.getOrderStatus()==1 && o.getTxId() != null){
                        //查询BTC有没有到协商地址如果到了地址
                        if(true){
                            o.setOrderStatus(2L);
                            o = orderRepo.save(o);
                            this.setOrderStatusName(o);
                            return o;
                        }
            }
        } catch (Exception e) {
            LOG.info("confirm order faild :",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /*
    * 查询发布公告的人的所有待确认的订单
    * */
    public List<Orders> findNotConfirmOrders(Long id){
        List<Orders> list = null;
        try {
            //出售公告
            Notice seller = noticeRepo.findNoticeByUserIdAndTxStatusIsNotAndNoticeType(id,2,1L);
            //购买公告
            Notice buyer = noticeRepo.findNoticeByUserIdAndTxStatusIsNotAndNoticeType(id,2,2L);

            list = new ArrayList<>();
            if(seller != null){
                List<Orders> list1 = orderRepo.findOrdersByNoticeIdAndOrderStatus(seller.getId(),1L);
                for (Orders o:
                     list1) {
                    o.setOrderType("出售");
                }
                list.addAll(list1);
            }
            if(buyer != null){
                List<Orders> list1 = orderRepo.findOrdersByNoticeIdAndOrderStatus(buyer.getId(),1L);
                for (Orders o:
                        list1) {
                    o.setOrderType("购买");
                 this.setOrderStatusName(o);
                }
                list.addAll(list1);
            }
        } catch (Exception e) {
            LOG.info("query no confirm orders faild :",e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    /*
    * 买家确认收到退款   将买家的私匙给卖家 订单状态改为6
    * */
    public Orders confirmReceiveRefund(Pojo pojo){
        try {
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orders.getBuyerId()==pojo.getUserId() && orders.getOrderStatus()==7L){
                OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orders.getId());
                orderAddresskeys.setSellerBuyerPriAuth(orderAddresskeys.getBuyerPriAuth());
                orderAddresskeyRepo.save(orderAddresskeys);
                orders.setOrderStatus(6L);
                orders = orderRepo.save(orders);
                this.setOrderStatusName(orders);
                return orders;
            }
        } catch (Exception e) {
            LOG.info("confirm receive refund faild :",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /*
    * 对当前订单发起仲裁
    * */
    public Orders arbitrateOrder(String id){
        Orders orders1 = null;
        try {
            Orders orders = orderRepo.findOne(id);
            //判断订单状态为3或7时可以仲裁
            if(orders.getOrderStatus()==3||orders.getOrderStatus()==7){
                //仲裁状态（arbitrate）改为1 仲裁中
                orders.setArbitrate(1);
                //订单仲裁表中的 对应订单的三条仲裁状态改为1 表示 仲裁者仲裁中
                List<OrderArbitrate> orderArbitrateList = orderArbitrateRepo.findOrderArbitrateByOrderId(orders.getId());
                for (OrderArbitrate o:orderArbitrateList) {
                    o.setStatus(1);
                    orderArbitrateRepo.save(o);
                }
                orders1 = orderRepo.save(orders);
                this.setOrderStatusName(orders1);
                return  orders1;
            }
        } catch (Exception e) {
            LOG.info("apply for arbitrate order faild :",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /*
   * 根据仲裁者id查找哪些订单可以被自己仲裁的订单列表
   * */
    public List<Orders> findArbitrareOrderById(Long id){
        /*
        * */
        List<OrderArbitrate> orderArbitrateList = orderArbitrateRepo.findOrderArbitrateByUserIdAndAndStatus(id,1);
        List<Orders> ordersList = new ArrayList<>();

        for (OrderArbitrate o: orderArbitrateList) {
            Pojo pojo = new Pojo();
            pojo.setId(o.getOrderId());
            Orders orders = this.findOrdersDetails(pojo);
            orders.setBuyerUsername(userRepo.findOne(orders.getBuyerId()).getLoginname());
            orders.setSellerUsername(userRepo.findOne(orders.getSellerId()).getLoginname());
            this.setOrderStatusName(orders);
            ordersList.add(orders);
        }
        return ordersList;
    }
    //卖家上传公私钥
    public Orders saveAddresskey(OrderAddresskeys orderAddresskeys){
        OrderAddresskeys orderAddresskeys1 = null;
        Orders orders = null;
        try {
            //将卖家的公私匙上传到公私匙表里
            orderAddresskeys1 = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orderAddresskeys.getOrderId());
            orderAddresskeys1.setSellerPubAuth(orderAddresskeys.getSellerPubAuth());
            orderAddresskeys1.setSellerPriAuth(orderAddresskeys.getSellerPriAuth());
            orderAddresskeys1 = orderAddresskeyRepo.save(orderAddresskeys1);

            orders = orderRepo.findOne(orderAddresskeys1.getOrderId());
            //调用用户中心接口生成协商地址
            String s = orderAddresskeys1.getBuyerPubAuth()+","+orderAddresskeys1.getSellerPubAuth()+","+orderAddresskeys1.getUserPubAuth();
            OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orderAddresskeys1.getOrderId(),s,orders.getAmount().doubleValue());
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
            JSONObject jsonObject = restTemplate.postForObject("http://themis-user/account/p2sh",formEntity,JSONObject.class);
            Integer status =  (Integer) jsonObject.get("status");
            if(status==1){
                LinkedHashMap data = (LinkedHashMap) jsonObject.get("data");
                orders.setP2shAddress((String) data.get("address"));
                orders.setUri((String)data.get("uri"));
                return orders;
            }
        } catch (Exception e) {
            LOG.info("save address key faild :",e.getMessage());
            e.printStackTrace();
        }
        return  null;

    }
    /*
   * 仲裁者仲裁将密匙碎片给胜利者
   * */
    public OrderArbitrate arbitrateOrderToUser(Pojo pojo){
        OrderArbitrate orderArbitrate = null;
        try {
            orderArbitrate = orderArbitrateRepo.findOrderArbitrateByUserIdAndOrderId(pojo.getUserId(),pojo.getId());
            if(orderArbitrate.getStatus()==1){
                Orders orders = orderRepo.findOne(pojo.getId());
                if(orders.getBuyerId()==pojo.getSuccessId()){
                    orderArbitrate.setBuyerAuth(orderArbitrate.getUserAuth());
                }
                if(orders.getSellerId()==pojo.getSuccessId()){
                    orderArbitrate.setSellerAuth(orderArbitrate.getUserAuth());
                }
                orderArbitrate.setStatus(2);
                OrderArbitrate orderArbitrate1 = orderArbitrateRepo.save(orderArbitrate);
            }
        } catch (Exception e) {
            LOG.info("arbitrate orders to user faild ",e.getMessage());
            e.printStackTrace();
        }
        return orderArbitrate;
    }
    /*
    * 这是一个工具类方法  为了给要饭回到前台的orders 附上订单状态值
    * */
    public void setOrderStatusName(Orders o){
        try {
            if(o.getOrderStatus()==1){
                o.setOrderStatusName("待确认");
            }
            if(o.getOrderStatus()==2){
                o.setOrderStatusName("待付款");
            }
            if(o.getOrderStatus()==3){
                o.setOrderStatusName("待收货");
            }
            if(o.getOrderStatus()==4){
                o.setOrderStatusName("待评价");
            }
            if(o.getOrderStatus()==5){
                o.setOrderStatusName("完成");
            }
            if(o.getOrderStatus()==6){
                o.setOrderStatusName("已取消");
            }
            if(o.getOrderStatus()==7){
                o.setOrderStatusName("退款中");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("set order status value faild",e.getMessage());
        }
    }

    public UserTxDetail findUserTxDetailAndNotice(Pojo pojo){
        Notice notice = noticeRepo.findOne(pojo.getNoticeId());
        pojo.setUserId(notice.getUserId());
        UserTxDetail userTxDetail = this.findUserTxDetail(pojo);
        userTxDetail.setNotice(notice);
        userTxDetail.setUsername(userRepo.findOne(notice.getUserId()).getLoginname());
        return userTxDetail;
    }

    public UserTxDetail findUserTxDetail(Pojo pojo){
     UserTxDetail userTxDetail = userTxDetailRepo.findOne(pojo.getUserId());
     String goodDegree = ((userTxDetail.getGoodDesc() / (userTxDetail.getGoodDesc()+userTxDetail.getBadDesc()))*100)+"%";
     User user = userRepo.findOne(pojo.getUserId());
     userTxDetail.setEmailVerify("未验证");
     userTxDetail.setUsernameVerify("未验证");
     userTxDetail.setMobilePhoneVerify("未验证");
     if(user.getEmail()!=null){
         userTxDetail.setEmailVerify("已验证");
     }
     if(user.getUsername()!=null){
         userTxDetail.setUsernameVerify("已验证");
     }
     if(user.getMobilephone()!=null){
         userTxDetail.setMobilePhoneVerify("已验证");
     }
     userTxDetail.setGoodDegree(goodDegree);
     return userTxDetail;
    }
    public Orders updateOrderStatus(String orderId,Long status){
        Orders o = orderRepo.findOne(orderId);
        o.setOrderStatus(status);
        o = orderRepo.save(o);
        if(status==1){

            OrderTransaction orderTransaction = transactionRepo.findByOrderId(orderId);
            if(orderTransaction!=null){
                transactionRepo.delete(orderTransaction);
                OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orderId);
                orderAddresskeys.setSellerPriAuth(null);
                orderAddresskeys.setSellerPubAuth(null);
                orderAddresskeyRepo.save(orderAddresskeys);
            }
        }
        return o;
    }

    private HttpHeaders getHttpHeader(){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return  headers;
    }
    /*
    * 卖家上传交易凭据 txid
    * */
    public Orders uploadTxId(Pojo pojo){
        Orders orders1 = null;
        try {
            orders1 = orderRepo.findOne(pojo.getId());
            orders1.setTxId(pojo.getTxId());
        } catch (Exception e) {
            LOG.info("faild upload tx id :",e.getMessage());
            e.printStackTrace();
        }
        return orderRepo.save(orders1);
    }
    /*
    * 买家确认付款
    * */
    public Orders confirmSendMoney(Pojo pojo){
        try {
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orders.getOrderStatus()==2){
                orders.setOrderStatus(3L);
                return orderRepo.save(orders);
            }
            if(orders.getOrderStatus()==3){
                return orders;
            }
        } catch (Exception e) {
            LOG.info("confirm send money faild : ",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /*
    *判断卖家有没有上传公私钥并且生成协商地址
    * */
    public Orders judgeSellerPubPriAuth(Pojo pojo){
        try {
            OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId());
            if(orderAddresskeys.getSellerPubAuth()!=null && orderAddresskeys.getSellerPriAuth()!=null){
                Orders orders = orderRepo.findOne(pojo.getId());
                OrderTransaction transaction = transactionRepo.findByOrderId(pojo.getId());
                orders.setP2shAddress(transaction.getP2shAddress());
                String URI = "bitcoin:"+transaction.getP2shAddress()+"?amount="+orders.getAmount();
                orders.setUri(URI);
                return orders;
            }
        } catch (Exception e) {
            LOG.info("judge seller public private auth faild :",e.getMessage());
            e.printStackTrace();
        }
        return null;
    };
    public OrderAddresskeys findOrderAddressKeys(Pojo pojo){
        OrderAddresskeys orderAddresskeys = new OrderAddresskeys();
        OrderAddresskeys orderAddresskeys1 = orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId());
        Orders orders = orderRepo.findOne(pojo.getId());
        orderAddresskeys.setOrderId(pojo.getId());
        //如果订单没有经过仲裁 获取卖家买家两个人的私匙
        if(orders.getArbitrate()==0){
            if(orders.getBuyerId() == pojo.getUserId()){
                orderAddresskeys.setBuyerPriAuth(orderAddresskeys1.getBuyerPriAuth());
                orderAddresskeys.setSellerPriAuth(orderAddresskeys1.getBuyerSellerPriAuth());
            }
            else{
                orderAddresskeys.setSellerPriAuth(orderAddresskeys1.getSellerPriAuth());
                orderAddresskeys.setBuyerPriAuth(orderAddresskeys1.getSellerBuyerPriAuth());
            }
            //经过仲裁 获取自己的私匙和仲裁者的私匙
        }else{
            List<OrderArbitrate> orderArbitrateList = orderArbitrateRepo.findOrderArbitrateByOrderId(pojo.getId());
            //买家
            if(orders.getBuyerId() == pojo.getUserId()){
                String[] s = new String[2];
                List<String> stringList = new ArrayList<>();
                for (OrderArbitrate o:orderArbitrateList) {
                    if(o.getBuyerAuth()!=null){
                        stringList.add(o.getBuyerAuth());
                    }
                }
                if(stringList.size()>=2){
                   String secure =  ShamirUtil.getAuth((String[]) stringList.toArray());
                   orderAddresskeys.setUserPriAuth(secure);
                }
                orderAddresskeys.setBuyerPriAuth(orderAddresskeys1.getBuyerPriAuth());
            }
            else{
                //卖家
                String[] s = new String[2];
                List<String> stringList = new ArrayList<>();
                for (OrderArbitrate o:orderArbitrateList) {
                    if(o.getSellerAuth()!=null){
                        stringList.add(o.getSellerAuth());
                    }
                }
                if(stringList.size()>=2){
                    String secure =  ShamirUtil.getAuth((String[]) stringList.toArray());
                    orderAddresskeys.setUserPriAuth(secure);
                }
                orderAddresskeys.setSellerPriAuth(orderAddresskeys1.getSellerPriAuth());
            }
        }
        return orderAddresskeys;
    }
}
