package com.oxchains.themis.order.service;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.AddressKeys;
import com.oxchains.themis.common.model.OrdersKeyAmount;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.order.common.*;
import com.oxchains.themis.order.entity.*;
import com.oxchains.themis.order.entity.ValidaPojo.AddOrderPojo;
import com.oxchains.themis.order.entity.ValidaPojo.SaveAddresskeyPojo;
import com.oxchains.themis.order.entity.ValidaPojo.UploadTxIdPojo;
import com.oxchains.themis.order.entity.vo.OrdersInfo;
import com.oxchains.themis.order.entity.vo.UserTxDetails;
import com.oxchains.themis.order.repo.OrderCommentRepo;
import com.oxchains.themis.repo.dao.OrderAddresskeyRepo;
import com.oxchains.themis.repo.dao.OrderRepo;
import com.oxchains.themis.repo.dao.PaymentRepo;
import com.oxchains.themis.repo.dao.UserTxDetailDao;
import com.oxchains.themis.repo.entity.*;
import jnr.x86asm.OP;
import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

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
    private RemoteCallService callService;
    @Resource
    private OrderRepo orderRepo;
    @Resource
    private OrderAddresskeyRepo orderAddresskeyRepo;
    @Resource
    private UserTxDetailDao userTxDetailDao;
    @Resource
    private OrderCommentRepo orderCommentRepo;
    @Resource
    private PaymentRepo paymentRepo;
    @Resource
    private MessageService messageService;
    @Resource
    private RestTemplate restTemplate;
    private String defaultImageName = "123";
    private static final String remoteError = "服务器繁忙,请稍后重试!";
    /*
    * 查询所有订单  用来测试
    * */
    public List<Orders> findOrders(){
        return IteratorUtils.toList(orderRepo.findAll().iterator());
    }
    /*
    * 对发布的公告 下一个订单 生成订单信息
    * @param userId 当前用户的id
    * @param noticeId 当前公告的id
    * @param amount 交易数量
    * @param money 总价
    * */
    public RestResp addOrders(AddOrderPojo pojo){
        Orders orders = null;
        try {
            Notice notice = callService.findNoticeById(pojo.getNoticeId());
            AddressKeys addressKeys = callService.getAddressKeys();
            List<User> userList = callService.getArbitrateUser();
            if(userList == null || addressKeys == null || notice == null || userList.size() < ShamirUtil.N){
                return RestResp.fail(remoteError);
            }
            //生成一条订单信息 订单状态为1 是否仲裁为0
            orders = new Orders(DateUtil.getOrderId(),
                    pojo.getMoney(),
                    DateUtil.getPresentDate(),
                    pojo.getAmount(),
                    notice.getPayType(), ParamType.VcurrencyStatus.BTC.getId(),
                    notice.getCurrency(),
                    notice.getNoticeType().longValue()== ParamType.NoticeStatus.BUY.getStatus()?notice.getUserId():pojo.getUserId(),
                    notice.getNoticeType().longValue()==ParamType.NoticeStatus.BUY.getStatus()?pojo.getUserId():notice.getUserId(),
                    ParamType.OrderStatus.WAIT_CONFIRM.getStatus(),
                    notice.getId(),
                    ParamType.ArbitrateStatus.NOARBITRATE.getStatus());
            orders = orderRepo.save(orders);
            Optional<Orders> ordersOptional = Optional.of(orders);
            ordersOptional.ifPresent((orders1) -> {
                //发给发布公告的人和拍订单的人的站内信
                messageService.postAddOrderMessage(orders1,pojo.getUserId(),notice.getUserId());
                OrderAddresskeys orderAddresskeys = new OrderAddresskeys(orders1.getId(),addressKeys.getPublicKey(),addressKeys.getPrivateKey(),addressKeys.getPublicKey(),addressKeys.getPrivateKey());
                //生成买家用户的公私匙 存到 order_address_key table 每个订单对应一条
                orderAddresskeys = orderAddresskeyRepo.save(orderAddresskeys);
                Optional<OrderAddresskeys> addresskeysOptional = Optional.of(orderAddresskeys);
                //将仲裁者用户的私匙 分为三个密匙碎片分别分给三个人 存储在 订单仲裁表里面 每个订单对应三条信息
                String[] strArr = ShamirUtil.splitAuth(addresskeysOptional.get().getUserPriAuth());
                //生成仲裁者用户的公私匙 存到 订单买家卖家仲裁者表里 order_address_key 每个订单对应一条
                List<OrderArbitrate> arbitrateList = new ArrayList<>(ShamirUtil.N);
                OrderArbitrate orderArbitrate = null;
                for(int i = 0;i<strArr.length;i++){
                    orderArbitrate = new OrderArbitrate(orders1.getId(),userList.get(i).getId(),ParamType.ArbitrateStatus.NOARBITRATE.getStatus(),strArr[i]);
                    arbitrateList.add(orderArbitrate);
                }
                Integer saveStatus = callService.saveOrderAbritrate(arbitrateList);
                if(saveStatus == -1){
                    throw new RuntimeException();
                }
                //处理双方的交易信息
                this.handleUserTxDetail(pojo.getUserId(),notice.getUserId(),ParamType.UserTxDetailHandle.FIRST_BUY_TIME.getStatus(),null);
            });
        }catch (Exception e){
            LOG.error("add orders faild : {}",e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail(remoteError);
        }
        return RestResp.success(new OrdersInfo(orders));
    }
           private void handleUserTxDetail(Long userId,Long ortherUserId,Integer status,Pojo pojo){
               try {
                   if(status.intValue() == ParamType.UserTxDetailHandle.FIRST_BUY_TIME.getStatus()){
                       UserTxDetail byUserId = userTxDetailDao.findByUserId(userId);
                       UserTxDetail noticeTx = userTxDetailDao.findByUserId(ortherUserId);
                       if(byUserId.getFirstBuyTime() == null){
                           byUserId.setFirstBuyTime(DateUtil.getPresentDate());
                       }
                       if(noticeTx.getFirstBuyTime() == null){
                           noticeTx.setFirstBuyTime(DateUtil.getPresentDate());
                       }
                       userTxDetailDao.save(byUserId);
                       userTxDetailDao.save(noticeTx);
                   }
                   if(status.intValue() == ParamType.UserTxDetailHandle.DESC.getStatus()){
                       UserTxDetail byUserId = userTxDetailDao.findByUserId(userId);
                       if(pojo.getStatus() == ParamType.CommentStatus.GOOD.getStatus().intValue()){
                           byUserId.setGoodDesc(byUserId.getGoodDesc()+1);
                       }
                       else{
                           byUserId.setBadDesc(byUserId.getBadDesc()+1);
                       }
                       userTxDetailDao.save(byUserId);
                   }
                   if(status.intValue() == ParamType.UserTxDetailHandle.TX_NUM_AMOUNT.getStatus()){
                       BigDecimal amount = orderRepo.findOne(pojo.getId()).getAmount();

                       UserTxDetail userTxDetails = userTxDetailDao.findByUserId(userId);
                       userTxDetails.setTxNum(userTxDetails.getTxNum()+1);
                       userTxDetails.setSuccessCount(userTxDetails.getSuccessCount()+amount.doubleValue());
                       userTxDetailDao.save(userTxDetails);

                       UserTxDetail noticeTx = userTxDetailDao.findByUserId(ortherUserId);
                       noticeTx.setTxNum(noticeTx.getTxNum()+1);
                       noticeTx.setSuccessCount(noticeTx.getSuccessCount()+amount.doubleValue());
                       userTxDetailDao.save(noticeTx);
                   }
               } catch (Exception e) {
                   LOG.error("user trasaction detail handle faild :{}",e);
                   throw  e;
               }
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
            ordersInfo.setNotice(callService.findNoticeById(o.getNoticeId()));
            this.setOrderStatusName(ordersInfo);
            if(pojo.getUserId()==null){
             return ordersInfo;
            }
            if(ordersInfo.getBuyerId().longValue() == pojo.getUserId()){
                ordersInfo.setOrderType("购买");
                ordersInfo.setFriendUsername(this.getLoginNameByUserId(o.getSellerId()));
                ordersInfo.setPartnerUserId(o.getSellerId());
            }
            else{
                ordersInfo.setOrderType("出售");
                ordersInfo.setFriendUsername(this.getLoginNameByUserId(o.getBuyerId()));
                ordersInfo.setPartnerUserId(o.getBuyerId());
            }
            ordersInfo.setPayment(paymentRepo.findOne(ordersInfo.getPaymentId()));
        } catch (Exception e) {
            LOG.error("get order details faild : {}",e);
            return null;
        }
        return ordersInfo;
    }
    /*
   * 根据id查询自己已完成的的订单
   * */
    public RestRespPage findCompletedOrdersById(Pojo pojo){
        Pageable pageable = new PageRequest(pojo.getPageNum()-1,pojo.getPageSize(),new Sort(Sort.Direction.DESC,"createTime"));
        List<OrdersInfo> ordersInfoList = null;
        Page<Orders> ordersPage = null;
        OrdersInfo ordersInfo = null;
        try {
            List<Long> longList = new ArrayList<>();
            longList.add(ParamType.OrderStatus.FINISH.getStatus());
            longList.add(ParamType.OrderStatus.CANCEL.getStatus());
            ordersPage = orderRepo.findOrdersByBuyerIdOrSellerIdAndOrderStatus(pojo.getUserId(),pojo.getUserId(),longList,pageable);
            ordersInfoList = new ArrayList<>();
            for (Orders o:ordersPage.getContent()) {
                ordersInfo = new OrdersInfo(o);
                ordersInfo.setNotice(callService.findNoticeById(o.getNoticeId()));
                if(o.getBuyerId().longValue() == pojo.getUserId()){
                    ordersInfo.setOrderType("购买");
                    ordersInfo.setFriendUsername(this.getLoginNameByUserId(o.getSellerId()));
                    ordersInfo.setPartnerUserId(o.getSellerId());
                }
                else{
                    ordersInfo.setOrderType("出售");
                    ordersInfo.setFriendUsername(this.getLoginNameByUserId(o.getBuyerId()));
                    ordersInfo.setPartnerUserId(o.getBuyerId());
                }
                this.setOrderStatusName(ordersInfo);
                ordersInfoList.add(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("query complete order faild : {}",e.getMessage(),e);
            return RestRespPage.fail(remoteError);
        }
        return RestRespPage.success(ordersInfoList,ordersPage.getTotalElements());
    }
    /*
   * 根据id查询自己未完成的的订单
   * */
    public RestRespPage findNoCompletedOrdersById(Pojo pojo){
        Pageable pageable = new PageRequest(pojo.getPageNum()-1,pojo.getPageSize(),new Sort(Sort.Direction.DESC,"createTime"));
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
                ordersInfo.setNotice(callService.findNoticeById(o.getNoticeId()));
                if(o.getBuyerId().longValue() == pojo.getUserId()){
                    ordersInfo.setOrderType("购买");
                    ordersInfo.setFriendUsername(this.getLoginNameByUserId(o.getSellerId()));
                    ordersInfo.setPartnerUserId(o.getSellerId());

                }
                else{
                    ordersInfo.setOrderType("出售");
                    ordersInfo.setFriendUsername(this.getLoginNameByUserId(o.getBuyerId()));
                    ordersInfo.setPartnerUserId(o.getBuyerId());
                }
                this.setOrderStatusName(ordersInfo);
                ordersInfoList.add(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("query noComplete order faild : {}",e.getMessage(),e);
            return RestRespPage.fail(remoteError);
        }
        return RestRespPage.success(ordersInfoList,ordersPage.getTotalElements());
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
                messageService.postCancelOrder(orders,userId);
            }
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_PAY.getStatus()){
                User user = callService.getUserById(orders.getSellerId());
                if(user == null){
                    return RestResp.fail("Network error : get user info faild");
                }
                //买家的私匙给卖家
                OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(id);
                orderAddresskeys.setSellerBuyerPriAuth(orderAddresskeys.getBuyerPriAuth());
                //将卖家的BTC从协商地址转回到 买家账户
                String s = orderAddresskeys.getBuyerPriAuth()+","+orderAddresskeys.getSellerPriAuth();
                OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),s,orders.getAmount().doubleValue(),user.getFirstAddress());
                HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), callService.getHttpHeader());
                JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.MOVE_BTC,formEntity,JSONObject.class);
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    orders.setOrderStatus(ParamType.OrderStatus.CANCEL.getStatus());
                    orders.setFinishTime(DateUtil.getPresentDate());
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
            return RestResp.fail(remoteError);
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
                //查询BTC有没有到协商地址如果到了地址
                JSONObject restResp = restTemplate.getForObject(ThemisUserAddress.CHECK_BTC+pojo.getId(), JSONObject.class);
                if(restResp != null){
                    Integer status  = (Integer) restResp.get("status");
                    if(status==1){
                        o.setOrderStatus(ParamType.OrderStatus.WAIT_PAY.getStatus());
                        o = orderRepo.save(o);
                        ordersInfo = new OrdersInfo(o);
                        this.setOrderStatusName(ordersInfo);
                        messageService.postConfirmOrder(o);
                        return RestResp.success(ordersInfo);
                    }
                    return RestResp.fail("你的支付还未到账,请稍后重试");
                }
        } catch (Exception e) {
            LOG.error("confirm order faild : {}",e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail(remoteError);
        }
        return RestResp.fail(remoteError);
    }
    /*
    * 买家确认收到退款   将买家的私匙给卖家 订单状态改为6
    * */
    public RestResp confirmReceiveRefund(Pojo pojo){
        OrdersInfo ordersInfo = null;
        try {
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orders.getBuyerId().longValue() == pojo.getUserId() && orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_REFUND.getStatus()){
                User user = callService.getUserById(orders.getSellerId());
                Optional.of(user);
                OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orders.getId());
                orderAddresskeys.setSellerBuyerPriAuth(orderAddresskeys.getBuyerPriAuth());
                orderAddresskeyRepo.save(orderAddresskeys);
                //将卖家的BTC从写上地址转回到 买家账户
                String s = orderAddresskeys.getBuyerPriAuth()+","+orderAddresskeys.getSellerPriAuth();
                OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),s,orders.getAmount().doubleValue(),user.getFirstAddress());
                JSONObject jsonObject = callService.moveBTC(ordersKeyAmount);
                if(jsonObject != null){
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    orders.setOrderStatus(ParamType.OrderStatus.CANCEL.getStatus());
                    orders.setFinishTime(DateUtil.getPresentDate());
                    orders = orderRepo.save(orders);
                    messageService.postRefoundMoney(orders,pojo.getUserId());
                }
                ordersInfo = new OrdersInfo(orders);
                this.setOrderStatusName(ordersInfo);
                }
                return orders!=null?RestResp.success(ordersInfo):RestResp.fail(remoteError);
            }
        } catch (Exception e) {
            LOG.error("confirm receive refund faild : {}",e.getMessage(),e);
            return RestResp.fail(remoteError);
        }
        return RestResp.fail(remoteError);
    }
    /*
    * 卖家上传公私钥
    * @Param orderId  订单id
    * @Param sellerPubAuth 卖家公钥
    * @Param sellerPriAuth 卖家私钥
    * */
    public RestResp saveAddresskey(SaveAddresskeyPojo orderAddresskeys){
        try {
            //将卖家的公私匙上传到公私匙表里
            OrderAddresskeys addressKeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(orderAddresskeys.getOrderId());
                //调用用户中心接口生成协商地址
            Optional<Orders> orders = Optional.of(orderRepo.findOne(addressKeys.getOrderId()));
            StringBuilder sb = new StringBuilder();
            sb.append(addressKeys.getBuyerPubAuth());
            sb.append(",");
            sb.append(addressKeys.getSellerPubAuth());
            sb.append(",");
            sb.append(addressKeys.getUserPubAuth());
            OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(addressKeys.getOrderId(),sb.toString(),orders.get().getAmount().doubleValue());
            JSONObject jsonObject = callService.createCenterAddress(ordersKeyAmount);
            if(jsonObject != null){
                Integer status = (Integer) jsonObject.get("status");
                if(status == 1){
                    addressKeys.setSellerPubAuth(orderAddresskeys.getSellerPubAuth());
                    addressKeys.setSellerPriAuth(orderAddresskeys.getSellerPriAuth());
                    orderAddresskeyRepo.save(addressKeys);
                    OrdersInfo ordersInfo = new OrdersInfo(orders.get());
                    LinkedHashMap data = (LinkedHashMap) jsonObject.get("data");
                    ordersInfo.setP2shAddress((String) data.get("address"));
                    ordersInfo.setUri((String)data.get("URI"));
                    orders.get().setUri((String)data.get("URI"));
                    orderRepo.save(orders.get());
                    messageService.postAddAddressKey(orders.get());
                    return RestResp.success(ordersInfo);
                }else{
                    return RestResp.fail("公钥验证失败,请输入正确的公私钥");
                }
            };
        } catch (Exception e) {
            LOG.error("save address key faild : {}",e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail(remoteError);
        }
        return RestResp.fail(remoteError);
    }
    /*
    * 这是一个工具类方法  为了给要返回到前台的orders 附上订单状态值
    * */
    public void setOrderStatusName(OrdersInfo o){
        if(o != null){
            if(o.getOrderStatus() != null){
                String orderStatusName = ParamType.OrderStatus.getName(o.getOrderStatus());
                o.setOrderStatusName(orderStatusName);
            }
        }
    }
    public UserTxDetails findUserTxDetailsAndNotice(Pojo pojo){
        UserTxDetails userTxDetails = null;
        try {
            Notice notice = callService.findNoticeById(pojo.getNoticeId());
            pojo.setUserId(notice!=null?notice.getUserId():null);
            userTxDetails = this.findUserTxDetails(pojo);
            if(userTxDetails!=null){
                userTxDetails.setNotice(notice);
                User user = callService.getUserById(notice.getUserId());
                if(user !=null){
                    userTxDetails.setLoginname(user.getLoginname());
                    userTxDetails.setImageName(this.defaultImageName);
                    if(user.getImage() != null){
                        userTxDetails.setImageName(user.getImage());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("find user transaction and notice  faild : {}",e);
            return null;
        }
        return userTxDetails;
    }
    public UserTxDetails findUserTxDetails(Pojo pojo){
        if(pojo.getUserId() == null){
            return null;
        }
        UserTxDetails userTxDetails = null;
        try {
            UserTxDetail userTxDetail = userTxDetailDao.findByUserId(pojo.getUserId());
            DecimalFormat df   = new DecimalFormat("######0.00");
            String goodDegree = "";
            if(userTxDetail.getGoodDesc()+userTxDetail.getBadDesc() == 0){
                goodDegree = "0.00%";
            }
            else{
                goodDegree = df.format(((userTxDetail.getGoodDesc().doubleValue() / (userTxDetail.getGoodDesc().doubleValue()+userTxDetail.getBadDesc().doubleValue()))*100))+"%";
            }
            User user = callService.getUserById(pojo.getUserId());
            userTxDetails = new UserTxDetails(userTxDetail);
            userTxDetails.setEmailVerify("未验证");
            userTxDetails.setUsernameVerify("未验证");
            userTxDetails.setMobilePhoneVerify("未验证");
            userTxDetails.setCreateTime(user!=null?user.getCreateTime():null);
            userTxDetails.setGoodDegree(goodDegree);
            userTxDetails.setLoginname(user!=null?user.getLoginname():null);
            if(user.getEmail()!=null){
                userTxDetails.setEmailVerify("已验证");
            }
            if(user.getUsername()!=null){
                userTxDetails.setUsernameVerify("已验证");
            }
            if(user.getMobilephone()!=null){
                userTxDetails.setMobilePhoneVerify("已验证");
            }
        } catch (Exception e) {
            LOG.error("find user transaction faild : {}",e);
            return null;
        }
        return userTxDetails;
    }
    public RestResp getPartnerTxDetails(Pojo pojo){
        Long userId = null;
        try {
            Orders one = orderRepo.findOne(pojo.getId());
            userId = one.getSellerId().longValue() == pojo.getUserId() ? one.getBuyerId():one.getSellerId();
            pojo.setUserId(userId);
            UserTxDetails userTxDetails = this.findUserTxDetails(pojo);
            return userTxDetails != null ? RestResp.success(userTxDetails):RestResp.fail();
        }
        catch (Exception e){
            LOG.error("get Partner Tx Details faild ",e);
            return RestResp.fail();
        }
    }

    /*
    * 卖家上传交易凭据 txid
    * @Param id 订单id
    * @Param txid 交易id
    * */
    public RestResp uploadTxId(UploadTxIdPojo pojo){
        try {
            OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount();
            ordersKeyAmount.setTxId(pojo.getTxId());
            Optional<JSONObject> jsonObjectOptional = Optional.of(callService.uploadTxId(ordersKeyAmount, pojo.getId()));
            Integer status = (Integer) jsonObjectOptional.get().get("status");
            if(status == 1){
                messageService.postUploadTxId(orderRepo.findOne(pojo.getId()));
                if(pojo.getUploadType() == 2){
                    callService.uploadTxInform(pojo);
                }
                return RestResp.success();
            }
        } catch (Exception e) {
            LOG.error("faild upload tx id : {}",e.getMessage(),e);
            return RestResp.fail(remoteError);
        }
        return RestResp.fail(remoteError);
    }
    /*
    * 买家确认付款
    * */
    public RestResp confirmSendMoney(Pojo pojo){
        OrdersInfo ordersInfo = null;
        try {
            Optional<Orders> orders = Optional.of(orderRepo.findOne(pojo.getId()));
            if(orders.get().getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_PAY.getStatus()){
                orders.get().setOrderStatus(ParamType.OrderStatus.WAIT_SEND.getStatus());
                Orders orders1 = orderRepo.save(orders.get());
                Optional<Orders> ordersOptional = Optional.of(orders1);
                ordersInfo = new OrdersInfo(ordersOptional.get());
                messageService.postConfirmSendMoney(ordersOptional.get());
                return RestResp.success(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("confirm send money faild : {}",e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail(remoteError);
        }
        return RestResp.fail("操作异常");
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
                Transaction transaction = callService.getTransactionById(orders.getId());
                ordersInfo = new OrdersInfo(orders);
                ordersInfo.setP2shAddress(transaction.getP2shAddress());
                ordersInfo.setTxId(transaction.getUtxoTxid());
                return orders!=null?RestResp.success(ordersInfo):RestResp.fail("未上传");
            }
        } catch (Exception e) {
            LOG.error("judge seller public private auth faild : {}",e);
        }
        return RestResp.fail("未上传");
    };
    public RestResp releaseBTC(Pojo pojo){
        OrderAddresskeys save = null;
        try {
            OrderAddresskeys orderAddresskeys = orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId());
            Orders orders = orderRepo.findOne(pojo.getId());
            User user = callService.getUserById(orders.getBuyerId());
            Optional.of(user);
            //只有卖家可以释放BTC
            if(orders.getSellerId().longValue() == pojo.getUserId()){
                //将卖家的私匙给买家
                orderAddresskeys.setBuyerSellerPriAuth(orderAddresskeys.getSellerPriAuth());
                save = orderAddresskeyRepo.save(orderAddresskeys);
                //将卖家的BTC从协商地址转回到 买家账户
                String s = save.getBuyerPriAuth()+","+save.getBuyerSellerPriAuth();
                OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),s,orders.getAmount().doubleValue(),user.getFirstAddress());
                JSONObject jsonObject = callService.moveBTC(ordersKeyAmount);
                if(jsonObject != null){
                    Integer status = (Integer) jsonObject.get("status");
                    if(status == 1){
                        orders.setOrderStatus(ParamType.OrderStatus.WAIT_RECIVE.getStatus());
                        orders = orderRepo.save(orders);
                        messageService.postReleaseBtc(orders);
                        return RestResp.success(save);
                    }
                }
                return RestResp.fail("释放失败,请稍后再试");
            }
        } catch (Exception e) {
            LOG.error("release BTC faild : {}",e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail(remoteError);
        }
        return  RestResp.fail("操作异常");
    };
    public boolean sellerReleaseBTCIsOrNot(Pojo pojo){
        return orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId()).getBuyerSellerPriAuth()!=null?true:false;
    };
    //确认收到BTC
    public RestResp confirmReciveBTC(Pojo pojo){
        OrdersInfo ordersInfo = null;
        try {
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orders.getBuyerId().longValue() == pojo.getUserId() && orders.getOrderStatus().longValue() ==ParamType.OrderStatus.WAIT_RECIVE.getStatus()){
                orders.setOrderStatus(ParamType.OrderStatus.WAIT_COMMENT.getStatus());
                orders = orderRepo.save(orders);
                messageService.postConfirmReceive(orders);
                ordersInfo = new OrdersInfo(orders);
                return RestResp.success(ordersInfo);
            }
        } catch (RestClientException e) {
            LOG.error("confirm recive BTC faild : {} ",e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail(remoteError);
        }
        return RestResp.fail("操作异常");
    }
    //上传评价
    public RestResp saveComment(Pojo pojo){
        OrderComment orderComment1 = null;
        try {
            this.handleUserTxDetail(pojo.getUserId(),null,ParamType.UserTxDetailHandle.DESC.getStatus(),pojo);
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
                    o.setFinishTime(DateUtil.getPresentDate());
                    o = orderRepo.save(o);
                    messageService.postFinishOrders(o);
                    this.handleUserTxDetail(pojo.getUserId(),o.getSellerId(),ParamType.UserTxDetailHandle.TX_NUM_AMOUNT.getStatus(),pojo);
                }
            }
            if(o.getSellerId().longValue() == pojo.getUserId()){
                orderComment1.setSellerContent(pojo.getContent());
                orderComment1.setSellerStatus(pojo.getStatus());
                orderComment1 = orderCommentRepo.save(orderComment1);
                if(orderComment1.getBuyerContent()!=null){
                    o.setOrderStatus(ParamType.OrderStatus.FINISH.getStatus());
                    o.setFinishTime(DateUtil.getPresentDate());
                    o = orderRepo.save(o);
                    messageService.postFinishOrders(o);
                    this.handleUserTxDetail(pojo.getUserId(),o.getBuyerId(),ParamType.UserTxDetailHandle.TX_NUM_AMOUNT.getStatus(),pojo);

                }
            }
        } catch (Exception e) {
            LOG.error("faild save comment : {}",e.getMessage(),e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return RestResp.fail(remoteError);
        }
        return orderComment1!=null?RestResp.success(orderComment1):RestResp.fail(remoteError);
    }
    private String getLoginNameByUserId(Long userId){
        User userById = callService.getUserById(userId);
        return userById != null?userById.getLoginname():null;
    }
}
