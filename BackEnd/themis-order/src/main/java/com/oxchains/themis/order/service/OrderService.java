package com.oxchains.themis.order.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.OrdersKeyAmount;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.order.common.*;
import com.oxchains.themis.order.entity.*;
import com.oxchains.themis.order.entity.vo.OrdersInfo;
import com.oxchains.themis.order.repo.*;
import com.oxchains.themis.repo.entity.*;
import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
/**
 * Created by huohuo on 2017/10/23.
 * @author huohuo
 */
@Transactional(rollbackFor=Exception.class)
@Service
public class OrderService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private OrderRepo orderRepo;
    @Resource
    private OrderAddresskeyRepo orderAddresskeyRepo;
    @Resource
    private UserTxDetailRepo userTxDetailRepo;
    @Resource
    private OrderCommentRepo orderCommentRepo;
    @Resource
    private PaymentRepo paymentRepo;
    @Resource
    private MessageService messageService;
    /**
    * 工具类方法 用来在用户系统获取一对随机的公私匙
    * */
   public AddressKeys getAddressKeys(){
       AddressKeys ak = null;
       try {
           String   r = restTemplate.getForObject(ThemisUserAddress.GET_ADDRESS_KEYS,String.class);
           JSONObject result= JSON.parseObject(r);
           int status= (int) result.get("status");
           if(status==1){
               Object o=result.get("data");
               ak = (AddressKeys) JsonUtil.fromJson(JsonUtil.toJson(o),AddressKeys.class);
           }
       } catch (RestClientException e) {
           LOG.error("get address key faild : {}",e.getMessage(),e);
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
    public RestResp addOrders(Pojo pojo){
        Orders orders = null;
        try {
            Notice notice = this.findNoticeById(pojo.getNoticeId());
            //生成一条订单信息 订单状态为1 是否仲裁为0
            orders = new Orders(DateUtil.getOrderId(),
                    pojo.getMoney(),
                    DateUtil.getPresentDate(),
                    new BigDecimal(pojo.getAmount()),
                    notice.getPayType(), ParamType.VcurrencyStatus.BTC.getId(),
                    notice.getCurrency(),
                    notice.getNoticeType().longValue()== ParamType.NoticeStatus.BUY.getStatus()?notice.getUserId():pojo.getUserId(),
                    notice.getNoticeType().longValue()==ParamType.NoticeStatus.BUY.getStatus()?pojo.getUserId():notice.getUserId(),
                    ParamType.OrderStatus.WAIT_CONFIRM.getStatus(),
                    notice.getId(),
                    ParamType.ArbitrateStatus.NOARBITRATE.getStatus());
            orders =orderRepo.save(orders);
            //发给发布公告的人和拍订单的人的站内信
            messageService.postAddOrderMessage(orders,pojo.getUserId(),notice.getId());
            //生成买家用户的公私匙 存到 order_address_key table 每个订单对应一条
            AddressKeys addressKeys = this.getAddressKeys();
            //生成仲裁者用户的公私匙 存到 订单买家卖家仲裁者表里 order_address_key 每个订单对应一条
            AddressKeys addressKeys1 = this.getAddressKeys();
            OrderAddresskeys orderAddresskeys = new OrderAddresskeys(orders.getId(),addressKeys.getPublicKey(),addressKeys.getPrivateKey(),addressKeys1.getPublicKey(),addressKeys1.getPrivateKey());
            orderAddresskeys = orderAddresskeyRepo.save(orderAddresskeys);

            //将仲裁者用户的私匙 分为三个密匙碎片分别分给三个人 存储在 订单仲裁表里面 每个订单对应三条信息
            String[] strArr = ShamirUtil.splitAuth(orderAddresskeys.getUserPriAuth());
            List<User> userList = this.getArbitrateUser();
            for(int i = 0;i<strArr.length;i++){
                OrderArbitrate orderArbitrate = new OrderArbitrate(orders.getId(),userList.get(i).getId(),ParamType.ArbitrateStatus.NOARBITRATE.getStatus(),strArr[i]);
                this.saveOrderAbritrate(orderArbitrate);
            }
            this.saveNotice(pojo.getNoticeId(),ParamType.NoticeTxStatus.TXING.getStatus());
        }catch (Exception e){
            LOG.error("add orders faild : {}",e.getMessage(),e);
            return RestResp.fail("请正确填写订单信息");
        }
        return orders!=null?RestResp.success(new OrdersInfo(orders)):RestResp.fail("请正确填写订单信息");
    }
    /*
    * 根据订单编号查询订单的详细信息
    * */
    public OrdersInfo findOrdersDetails(Pojo pojo){
        Orders o = null;
        OrdersInfo ordersInfo = null;
        try {
            o = orderRepo.findOne(pojo.getId());
            ordersInfo = new OrdersInfo(o);
            ordersInfo.setNotice(this.findNoticeById(o.getNoticeId()));
            this.setOrderStatusName(ordersInfo);
            if(pojo.getUserId()==null){
             return ordersInfo;
            }
            if(ordersInfo.getBuyerId().longValue() == pojo.getUserId()){
                ordersInfo.setOrderType("购买");
                ordersInfo.setFriendUsername(this.getUserById(o.getSellerId()).getLoginname());
            }
            else{
                ordersInfo.setOrderType("出售");
                ordersInfo.setFriendUsername(this.getUserById(o.getBuyerId()).getLoginname());
            }
            ordersInfo.setPayment(paymentRepo.findOne(ordersInfo.getPaymentId()));
        } catch (Exception e) {
            LOG.error("get order details faild : {}",e.getMessage(),e);
        }
        return ordersInfo;
    }
    /*
   * 根据id查询自己已完成的的订单
   * */
    public RestResp findCompletedOrdersById(Pojo pojo){
        Pageable pageable = new PageRequest(pojo.getPageNum()-1,pojo.getPageSize(),new Sort(Sort.Direction.DESC,"id"));
        List<OrdersInfo> ordersInfoList = null;
        OrdersInfo ordersInfo = null;
        Page<Orders> ordersPage = null;
        try {
            List<Long> longList = new ArrayList<>();
            longList.add(ParamType.OrderStatus.FINISH.getStatus());
            longList.add(ParamType.OrderStatus.CANCEL.getStatus());
            ordersPage = orderRepo.findOrdersByBuyerIdOrSellerIdAndOrderStatus(pojo.getUserId(),pojo.getUserId(),longList,pageable);
            ordersInfoList = new ArrayList<>();
            for (Orders o:ordersPage.getContent()) {
                ordersInfo = new OrdersInfo(o);
                ordersInfo.setNotice(this.findNoticeById(o.getNoticeId()));
                if(o.getBuyerId().longValue() == pojo.getUserId()){
                    ordersInfo.setOrderType("购买");
                    ordersInfo.setPageCount(ordersPage.getTotalPages());
                    ordersInfo.setFriendUsername(this.getUserById(o.getSellerId()).getLoginname());
                }
                else{
                    ordersInfo.setOrderType("出售");
                    ordersInfo.setPageCount(ordersPage.getTotalPages());
                    ordersInfo.setFriendUsername(this.getUserById(o.getBuyerId()).getLoginname());
                }
                this.setOrderStatusName(ordersInfo);
                ordersInfoList.add(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("query complete order faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.success(ordersInfoList);
    }
    /*
   * 根据id查询自己未完成的的订单
   * */
    public RestResp findNoCompletedOrdersById(Pojo pojo){
        Pageable pageable = new PageRequest(pojo.getPageNum()-1,pojo.getPageSize(),new Sort(Sort.Direction.DESC,"id"));
        List<OrdersInfo> ordersInfoList = null;
        OrdersInfo ordersInfo = null;
        Page<Orders> ordersPage = null;
        try {
            List<Long> longList = new ArrayList<>();
            longList.add(ParamType.OrderStatus.FINISH.getStatus());
            longList.add(ParamType.OrderStatus.CANCEL.getStatus());
            ordersPage = orderRepo.findOrdersByBuyerIdOrSellerIdAndOrderStatusIsNotIn(pojo.getUserId(),pojo.getUserId(),longList,pageable);
            ordersInfoList = new ArrayList<>();
            for (Orders o:ordersPage.getContent()) {
                ordersInfo = new OrdersInfo(o);
                ordersInfo.setNotice(this.findNoticeById(o.getNoticeId()));
                if(o.getBuyerId().longValue() == pojo.getUserId()){
                    ordersInfo.setOrderType("购买");
                    ordersInfo.setPageCount(ordersPage.getTotalPages());
                    ordersInfo.setFriendUsername(this.getUserById(o.getSellerId()).getLoginname());

                }
                else{
                    ordersInfo.setOrderType("出售");
                    ordersInfo.setPageCount(ordersPage.getTotalPages());
                    ordersInfo.setFriendUsername(this.getUserById(o.getBuyerId()).getLoginname());
                }
                this.setOrderStatusName(ordersInfo);
                ordersInfoList.add(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("query noComplete order faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.success(ordersInfoList);
    }
    /*
    * 取消订单
    * */
    public RestResp cancelOrders(String id,Long userId){
        Orders orders1 = null;
        OrdersInfo ordersInfo = null;
        try {
            Orders orders = orderRepo.findOne(id);
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_CONFIRM.getStatus()){
                //当订单状态为1 时 买家已拍下 但商家还未确认 可以直接取消订单 不采取任何操作
                orders.setOrderStatus(ParamType.OrderStatus.CANCEL.getStatus());
                orders.setFinishTime(DateUtil.getPresentDate());
                this.saveNotice(orders.getNoticeId(),ParamType.NoticeTxStatus.NOTX.getStatus());
                messageService.postCancelOrder(orders,userId);
            }
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_PAY.getStatus()){
                //买家的私匙给卖家
                OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(id);
                orderAddresskeys.setSellerBuyerPriAuth(orderAddresskeys.getBuyerPriAuth());
                //将卖家的BTC从协商地址转回到 买家账户
                String s = orderAddresskeys.getBuyerPriAuth()+","+orderAddresskeys.getSellerPriAuth();
                OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),s,orders.getAmount().doubleValue(),this.getUserById(orders.getSellerId()).getFirstAddress());
                HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
                JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.MOVE_BTC,formEntity,JSONObject.class);
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    orders.setOrderStatus(ParamType.OrderStatus.CANCEL.getStatus());
                    orders.setFinishTime(DateUtil.getPresentDate());
                    this.saveNotice(orders.getNoticeId(),ParamType.NoticeTxStatus.NOTX.getStatus());
                    messageService.postCancelOrder(orders,userId);
                }
            }
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_SEND.getStatus()){
                orders.setOrderStatus(ParamType.OrderStatus.WAIT_REFUND.getStatus());
                messageService.postRefund(orders,userId);
            }
            orders1 = orderRepo.save(orders);
            ordersInfo = new OrdersInfo(orders1);
            this.setOrderStatusName(ordersInfo);
        } catch (Exception e) {
            LOG.error("cancel orders faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return orders1!=null?RestResp.success(ordersInfo):RestResp.fail();
    }
    /*
    * 发布公告人确认订单
    * description     发布公告的人确认交易
    * */
    public RestResp confirmOrders(Pojo pojo){
        OrdersInfo ordersInfo = null;
        try {
            Orders o = orderRepo.findOne(pojo.getId());
            Notice notice = this.findNoticeById(o.getNoticeId());
            //只有发布公告的人才可以确认订单
            if(notice.getUserId().longValue() == pojo.getUserId() && o.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_CONFIRM.getStatus()){
                //查询BTC有没有到协商地址如果到了地址
                JSONObject restResp = restTemplate.getForObject(ThemisUserAddress.CHECK_BTC+pojo.getId(), JSONObject.class);
                Integer status  = (Integer) restResp.get("status");
                        if(status==1){
                            o.setOrderStatus(ParamType.OrderStatus.WAIT_PAY.getStatus());
                            o = orderRepo.save(o);
                            ordersInfo = new OrdersInfo(o);
                            this.setOrderStatusName(ordersInfo);
                            messageService.postConfirmOrder(o);
                            return RestResp.success(ordersInfo);
                        }
            }
        } catch (Exception e) {
            LOG.error("confirm order faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.fail();
    }
    /*
    * 买家确认收到退款   将买家的私匙给卖家 订单状态改为6
    * */
    public RestResp confirmReceiveRefund(Pojo pojo){
        OrdersInfo ordersInfo = null;
        try {
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orders.getBuyerId().longValue() == pojo.getUserId() && orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_REFUND.getStatus()){
                OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orders.getId());
                orderAddresskeys.setSellerBuyerPriAuth(orderAddresskeys.getBuyerPriAuth());
                orderAddresskeyRepo.save(orderAddresskeys);
                //将卖家的BTC从写上地址转回到 买家账户
                String s = orderAddresskeys.getBuyerPriAuth()+","+orderAddresskeys.getSellerPriAuth();
                OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),s,orders.getAmount().doubleValue(),this.getUserById(orders.getSellerId()).getFirstAddress());
                HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
                JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.MOVE_BTC,formEntity,JSONObject.class);
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    this.saveNotice(orders.getNoticeId(),ParamType.NoticeTxStatus.NOTX.getStatus());
                    orders.setOrderStatus(ParamType.OrderStatus.CANCEL.getStatus());
                    orders.setFinishTime(DateUtil.getPresentDate());
                    orders = orderRepo.save(orders);
                    messageService.postRefoundMoney(orders,pojo.getUserId());
                }
                ordersInfo = new OrdersInfo(orders);
                this.setOrderStatusName(ordersInfo);
                return orders!=null?RestResp.success(ordersInfo):RestResp.fail();
            }
        } catch (Exception e) {
            LOG.error("confirm receive refund faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.fail();
    }
    //卖家上传公私钥
    public RestResp saveAddresskey(OrderAddresskeys orderAddresskeys){
        OrderAddresskeys orderAddresskeys1 = null;
        OrdersInfo ordersInfo = null;
        try {
            //将卖家的公私匙上传到公私匙表里
            orderAddresskeys1 = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orderAddresskeys.getOrderId());
            orderAddresskeys1.setSellerPubAuth(orderAddresskeys.getSellerPubAuth());
            orderAddresskeys1.setSellerPriAuth(orderAddresskeys.getSellerPriAuth());
            orderAddresskeys1 = orderAddresskeyRepo.save(orderAddresskeys1);

            //调用用户中心接口生成协商地址
            Orders orders = orderRepo.findOne(orderAddresskeys1.getOrderId());
            StringBuilder sb = new StringBuilder();
            sb.append(orderAddresskeys1.getBuyerPubAuth());
            sb.append(",");
            sb.append(orderAddresskeys1.getSellerPubAuth());
            sb.append(",");
            sb.append(orderAddresskeys1.getUserPubAuth());

            OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orderAddresskeys1.getOrderId(),sb.toString(),orders.getAmount().doubleValue());
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
            JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.CREATE_CENTET_ADDRESS,formEntity,JSONObject.class);
            Integer status =  (Integer) jsonObject.get("status");
            if(status == 1){
                ordersInfo = new OrdersInfo(orders);
                LinkedHashMap data = (LinkedHashMap) jsonObject.get("data");
                ordersInfo.setP2shAddress((String) data.get("address"));
                ordersInfo.setUri((String)data.get("URI"));
                messageService.postAddAddressKey(orders);
                return ordersInfo!=null?RestResp.success(ordersInfo):RestResp.fail("请输入正确的公私匙");
            }
        } catch (Exception e) {
            LOG.error("save address key faild : {}",e.getMessage(),e);
            return RestResp.fail("请输入正确的公私匙");
        }
        return  RestResp.fail("请输入正确的公私匙");

    }
    /*
    * 这是一个工具类方法  为了给要返回到前台的orders 附上订单状态值
    * */
    public void setOrderStatusName(OrdersInfo o){
        String orderStatusName = ParamType.OrderStatus.getName(o.getOrderStatus());
        o.setOrderStatusName(orderStatusName);
        /*try {
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_CONFIRM.getStatus()){
                o.setOrderStatusName("待确认");
            }
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_PAY.getStatus()){
                o.setOrderStatusName("待付款");
            }
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_SEND.getStatus()){
                o.setOrderStatusName("待收货");
            }
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_RECIVE.getStatus()){
                o.setOrderStatusName("待收货");
            }
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_COMMENT.getStatus()){
                o.setOrderStatusName("待评价");
            }
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.FINISH.getStatus()){
                o.setOrderStatusName("已完成");
            }
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.CANCEL.getStatus()){
                o.setOrderStatusName("已取消");
            }
            if(o.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_REFUND.getStatus()){
                o.setOrderStatusName("退款中");
            }
        } catch (Exception e) {
            LOG.error("set order status value faild : {}",e.getMessage(),e);
        }*/
    }
    public UserTxDetails findUserTxDetailsAndNotice(Pojo pojo){
        UserTxDetails UserTxDetails = null;
        try {
            Notice notice = this.findNoticeById(pojo.getNoticeId());
            pojo.setUserId(notice.getUserId());
            UserTxDetails = this.findUserTxDetails(pojo);
            UserTxDetails.setNotice(notice);
            UserTxDetails.setLoginname(this.getUserById(notice.getUserId()).getLoginname());
            UserTxDetails.setSuccessCount(orderRepo.countByBuyerIdAndOrderStatus(pojo.getUserId(),ParamType.OrderStatus.FINISH.getStatus())+orderRepo.countBySellerIdAndOrderStatus(pojo.getUserId(),ParamType.OrderStatus.FINISH.getStatus()));
        } catch (Exception e) {
            LOG.error("find user transaction and notice  faild : {}",e.getMessage(),e);
        }
        return UserTxDetails;
    }
    public UserTxDetails findUserTxDetails(Pojo pojo){
        UserTxDetails UserTxDetails = null;
        try {
            UserTxDetails = userTxDetailRepo.findByUserId(pojo.getUserId());
            DecimalFormat df   = new DecimalFormat("######0.00");
            String goodDegree = "";
            if(UserTxDetails.getGoodDesc()+UserTxDetails.getBadDesc() == 0){
                goodDegree = "0.00%";
            }
            else{
                goodDegree = df.format(((UserTxDetails.getGoodDesc().doubleValue() / (UserTxDetails.getGoodDesc().doubleValue()+UserTxDetails.getBadDesc().doubleValue()))*100))+"%";
            }
            User user = this.getUserById(pojo.getUserId());
            UserTxDetails.setEmailVerify("未验证");
            UserTxDetails.setUsernameVerify("未验证");
            UserTxDetails.setMobilePhoneVerify("未验证");
            UserTxDetails.setCreateTime(user.getCreateTime().toString());
            UserTxDetails.setGoodDegree(goodDegree);
            UserTxDetails.setLoginname(this.getUserById(pojo.getUserId()).getLoginname());
            if(user.getEmail()!=null){
                UserTxDetails.setEmailVerify("已验证");
            }
            if(user.getUsername()!=null){
                UserTxDetails.setUsernameVerify("已验证");
            }
            if(user.getMobilephone()!=null){
                UserTxDetails.setMobilePhoneVerify("已验证");
            }
        } catch (Exception e) {
            LOG.error("find user transaction faild : {}",e.getMessage(),e);
        }
        return UserTxDetails;
    }

    private HttpHeaders getHttpHeader(){
        HttpHeaders headers = null;
        try {
            headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        } catch (Exception e) {
            LOG.error("get http header faild : {}",e.getMessage(),e);
        }
        return  headers;
    }
    /*
    * 卖家上传交易凭据 txid
    * */
    public RestResp uploadTxId(Pojo pojo){
        try {
            OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount();
            ordersKeyAmount.setTxId(pojo.getTxId());
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
            JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.CHECK_BTC + pojo.getId(), formEntity, JSONObject.class);
            Integer status = (Integer) jsonObject.get("status");
            if(status == 1){
                messageService.postUploadTxId(orderRepo.findOne(pojo.getId()));
                return RestResp.success();
            }
        } catch (Exception e) {
            LOG.error("faild upload tx id : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.fail("请输入正确的交易id");
    }
    /*
    * 买家确认付款
    * */
    public RestResp confirmSendMoney(Pojo pojo){
        OrdersInfo ordersInfo = null;
        try {
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_PAY.getStatus()){
                orders.setOrderStatus(ParamType.OrderStatus.WAIT_SEND.getStatus());
                orders = orderRepo.save(orders);
                ordersInfo = new OrdersInfo(orders);
                messageService.postConfirmSendMoney(orders);
                return ordersInfo!=null?RestResp.success(ordersInfo):RestResp.fail();
            }
        } catch (Exception e) {
            LOG.error("confirm send money faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.fail("未知错误");
    }
    /*
    *判断卖家有没有上传公私钥并且生成协商地址
    * */
    public RestResp judgeSellerPubPriAuth(Pojo pojo){
        OrdersInfo ordersInfo = null;
        try {
            OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId());
            if(orderAddresskeys.getSellerPubAuth()!=null && orderAddresskeys.getSellerPriAuth()!=null){
                Orders orders = orderRepo.findOne(pojo.getId());
                String address = this.getP2shAddressByOrderId(orders.getId());
                ordersInfo = new OrdersInfo(orders);
                ordersInfo.setP2shAddress(address);
                String uri = "bitcoin:"+address+"?amount="+orders.getAmount();
                ordersInfo.setUri(uri);
                return orders!=null?RestResp.success(ordersInfo):RestResp.fail();
            }
        } catch (Exception e) {
            LOG.error("judge seller public private auth faild : {}",e.getMessage(),e);
        }
        return RestResp.fail();
    };
    public RestResp releaseBTC(Pojo pojo){
        OrderAddresskeys save = null;
        try {
            OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId());
            Orders orders = orderRepo.findOne(pojo.getId());
            //只有卖家可以释放BTC
            if(orders.getSellerId().longValue() == pojo.getUserId()){
                //将卖家的私匙给买家
                orderAddresskeys.setBuyerSellerPriAuth(orderAddresskeys.getSellerPriAuth());
                save = orderAddresskeyRepo.save(orderAddresskeys);
                //将卖家的BTC从协商地址转回到 买家账户
                String s = save.getBuyerPriAuth()+","+save.getBuyerSellerPriAuth();
                OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),s,orders.getAmount().doubleValue(),this.getUserById(orders.getBuyerId()).getFirstAddress());
                HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
                JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.MOVE_BTC,formEntity,JSONObject.class);
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    orders.setOrderStatus(ParamType.OrderStatus.WAIT_RECIVE.getStatus());
                    orders = orderRepo.save(orders);
                    messageService.postReleaseBtc(orders);
                    return save!=null?RestResp.success(save):RestResp.fail("哎呦，网络有点差，请稍后重试");
                }
                else{
                    return RestResp.fail("哎呦，网络有点差，请稍后重试");
                }
            }
        } catch (Exception e) {
            LOG.error("release BTC faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return  RestResp.fail("未知错误");
    };
    public boolean sellerReleaseBTCIsOrNot(Pojo pojo){
        return orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId()).getBuyerSellerPriAuth()!=null?true:false;
    };
    public RestResp confirmReciveBTC(Pojo pojo){
        OrdersInfo ordersInfo = null;
        Orders orders = null;
        try {
            orders = orderRepo.findOne(pojo.getId());
            if(orders.getBuyerId().longValue() == pojo.getUserId() && orders.getOrderStatus().longValue() ==ParamType.OrderStatus.WAIT_RECIVE.getStatus()){
                orders.setOrderStatus(ParamType.OrderStatus.WAIT_COMMENT.getStatus());
                orders = orderRepo.save(orders);
                messageService.postConfirmReceive(orders);
            }
        } catch (RestClientException e) {
            LOG.error("confirm recive BTC faild : {} ",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        ordersInfo = new OrdersInfo(orders);
        return orders!=null?RestResp.success(ordersInfo):RestResp.fail("未知错误");
    }

    public RestResp saveComment(Pojo pojo){
        OrderComment orderComment1 = null;
        try {
            orderComment1 = orderCommentRepo.findOrderCommentByOrderId(pojo.getId());
            if(orderComment1==null){
                orderComment1 = new OrderComment();
                orderComment1.setOrderId(pojo.getId());
                orderComment1 = orderCommentRepo.save(orderComment1);
            }
            Orders o = orderRepo.findOne(pojo.getId());
            messageService.postCommentMessage(o,pojo.getUserId());
            if(o.getBuyerId().longValue() == pojo.getUserId()){
                orderComment1.setBuyerContent(pojo.getContent());
                orderComment1.setBuyerStatus(pojo.getStatus());
                orderComment1 =  orderCommentRepo.save(orderComment1);
                if(orderComment1.getSellerContent()!=null){
                    o.setOrderStatus(ParamType.OrderStatus.FINISH.getStatus());
                    o = orderRepo.save(o);
                    this.saveNotice(o.getNoticeId(),ParamType.NoticeTxStatus.TXEND.getStatus());
                    messageService.postFinishOrders(o);
                }
            }
            if(o.getSellerId().longValue() == pojo.getUserId()){
                orderComment1.setSellerContent(pojo.getContent());
                orderComment1.setSellerStatus(pojo.getStatus());
                orderComment1 = orderCommentRepo.save(orderComment1);
                if(orderComment1.getBuyerContent()!=null){
                    o.setOrderStatus(ParamType.OrderStatus.FINISH.getStatus());
                    o = orderRepo.save(o);
                    this.saveNotice(o.getNoticeId(),ParamType.NoticeTxStatus.TXEND.getStatus());
                    messageService.postFinishOrders(o);
                }
            }
        } catch (Exception e) {
            LOG.error("faild save comment : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return orderComment1!=null?RestResp.success(orderComment1):RestResp.fail();
    }
    public Orders updateOrderStatus(String orderId,Long status){
        Orders o = null;
        try {
            o = orderRepo.findOne(orderId);
            o.setOrderStatus(status);
            o = orderRepo.save(o);

        } catch (Exception e) {
            LOG.error("update order status faild : {}",e.getMessage(),e);
        }
        return o;
    }

    public User getUserById(Long userId){
        User user = null;
        try {
            JSONObject str = restTemplate.getForObject(ThemisUserAddress.GET_USER+userId, JSONObject.class);
            if(null != str){
                Integer status = (Integer) str.get("status");
                if(status == 1){
                    user = JsonUtil.jsonToEntity(JsonUtil.toJson(str.get("data")), User.class);
                }
            }
            return user;
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}",e.getMessage(),e);
        }
        return null;
    }
    public List<User> getArbitrateUser(){
        List<User> list = null;
        try {
            JSONObject str = restTemplate.getForObject(ThemisUserAddress.GET_ARBITRATE_USER, JSONObject.class);
            if(null != str){
                Integer status = (Integer) str.get("status");
                if(status == 1){
                    list = JsonUtil.jsonToList(JsonUtil.toJson(str.get("data")), User.class);
                }
                return list;
            }
        } catch (RestClientException e) {
            LOG.error("get arbitrate user from themis-user faild : {}",e.getMessage(),e);
        }
        return null;
    }
    private String getP2shAddressByOrderId(String id){
        try {
            JSONObject jsonObject = restTemplate.getForObject(ThemisUserAddress.GET_PTSHADDRESS, JSONObject.class);
            if(jsonObject != null){
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    Transaction transaction = JsonUtil.jsonToEntity(JsonUtil.toJson(jsonObject.get("data")), Transaction.class);
                    return transaction.getP2shAddress();
                }
            }
        } catch (Exception e) {
            LOG.error("get transaction from themis-user faild : {}",e.getMessage(),e);
        }
        return null;
    }
    private void saveOrderAbritrate(OrderArbitrate orderArbitrate){
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(orderArbitrate), this.getHttpHeader());
            JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.SAVE_ARBITRATE, formEntity, JSONObject.class);
        } catch (RestClientException e) {
            LOG.error("save order arbitrate faild : {}",e.getMessage(),e);
        }
    }
    private Notice saveNotice(Long id,Integer sta){
        try {
            JSONObject forObject = restTemplate.getForObject(ThemisUserAddress.SAVE_NOTICE + id + "/" + sta, JSONObject.class);
            Integer status = (Integer) forObject.get("status");
            if(status == 1){
                Notice notice = JsonUtil.jsonToEntity(JsonUtil.toJson(forObject.get("data")),Notice.class);
                return notice;
            }
        } catch (RestClientException e) {
            LOG.error("update notice status faild:{}",e.getMessage(),e);
        }
        return null;

    }
    private Notice findNoticeById(Long id){
        try {
            JSONObject forObject = restTemplate.getForObject(ThemisUserAddress.GET_NOTICE + id, JSONObject.class);
            Integer status = (Integer) forObject.get("status");
            if(status == 1){
                Notice notice = JsonUtil.jsonToEntity(JsonUtil.toJson(forObject.get("data")), Notice.class);
                return notice;
            }
        } catch (RestClientException e) {
            LOG.error("get notice faild : {}",e.getMessage(),e);
        }
        return null;
    }
}
