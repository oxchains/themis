package com.oxchains.themis.arbitrate.service;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.arbitrate.common.*;
import com.oxchains.themis.arbitrate.entity.*;
import com.oxchains.themis.arbitrate.entity.vo.OrdersInfo;
import com.oxchains.themis.arbitrate.repo.*;
import com.oxchains.themis.common.model.OrdersKeyAmount;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Transactional(rollbackFor=Exception.class)
@Service
public class ArbitrateService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private OrderRepo orderRepo;
    @Resource
    private OrderArbitrateRepo orderArbitrateRepo;
    @Resource
    private NoticeRepo noticeRepo;
    @Resource
    private PaymentRepo paymentRepo;
    @Resource
    private UserRepo userRepo;
    @Resource
    private OrderEvidenceRepo orderEvidenceRepo;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private OrderAddresskeyRepo orderAddresskeyRepo;
    @Resource
    private MessageService messageService;

    public static final Integer BUYER_SUCCESS = 1;
    public static final Integer SELLER_SUCCESS = 2;
    /*
   * 根据仲裁者id查找哪些订单可以被自己仲裁的订单列表
   * */
    public RestResp findArbitrareOrderById(Pojo pojos){
        Pageable pageable = new PageRequest(pojos.getPageNum()-1,pojos.getPageSize(),new Sort(Sort.Direction.DESC,"id"));
        List<OrdersInfo> ordersInfoList = null;
        OrdersInfo ordersInfo = null;
        Page<OrderArbitrate> orderArbitratePage = null;
        try {
            orderArbitratePage = orderArbitrateRepo.findOrderArbitrateByUserIdAndAndStatusIsNot(pojos.getUserId(),ParamType.ArbitrateStatus.NOARBITRATE.getStatus(),pageable);
            ordersInfoList = new ArrayList<>();
            for (OrderArbitrate o: orderArbitratePage.getContent()) {
                Pojo pojo = new Pojo();
                pojo.setId(o.getOrderId());
                ordersInfo = this.findOrdersDetails(pojo);
                ordersInfo.setBuyerUsername(userRepo.findOne(ordersInfo.getBuyerId()).getLoginname());
                ordersInfo.setSellerUsername(userRepo.findOne(ordersInfo.getSellerId()).getLoginname());
                this.setOrderStatusName(ordersInfo);
                ordersInfo.setPageCount(orderArbitratePage.getTotalPages());
                ordersInfo.setStatus(o.getStatus());
                ordersInfoList.add(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("find arbitrate order faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return ordersInfoList!=null?RestResp.success(ordersInfoList):RestResp.fail();
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
            ordersInfo.setNotice(noticeRepo.findOne(o.getNoticeId()));
            this.setOrderStatusName(ordersInfo);
            if(pojo.getUserId()==null){
                return ordersInfo;
            }
            if(ordersInfo.getBuyerId().longValue() == pojo.getUserId()){
                ordersInfo.setOrderType("购买");
                ordersInfo.setFriendUsername(userRepo.findOne(o.getSellerId()).getLoginname());
            }
            else{
                ordersInfo.setOrderType("出售");
                ordersInfo.setFriendUsername(userRepo.findOne(o.getBuyerId()).getLoginname());
            }
            ordersInfo.setPayment(paymentRepo.findOne(ordersInfo.getPaymentId()));
        } catch (Exception e) {
            LOG.error("get order details faild : {}",e.getMessage(),e);
        }
        return ordersInfo;
    }
    /*
   * 这是一个工具类方法  为了给要返回到前台的orders 附上订单状态值
   * */
    public void setOrderStatusName(OrdersInfo o){
        try {
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
        }
    }
    public RestResp uploadEvidence(RegisterRequest pojo,String imageUrl){

        OrderEvidence orderEvidence = null;
        try {
            MultipartFile multipartFile = pojo.getMultipartFile();
            if(multipartFile !=null ){
                String filename = multipartFile.getOriginalFilename();
                String suffix = filename.substring(filename.lastIndexOf("."));
                UUID uuid = UUID.randomUUID();
                String newFileName = uuid.toString() + suffix;
                multipartFile.transferTo(new File(imageUrl+newFileName));
                pojo.setFileName(newFileName);
            }

            orderEvidence = orderEvidenceRepo.findByOrderId(pojo.getId());
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orderEvidence == null){
                orderEvidence = new OrderEvidence();
                orderEvidence.setOrderId(pojo.getId());
                orderEvidence = orderEvidenceRepo.save(orderEvidence);
                messageService.postEvidenceMessage(orders,pojo.getUserId());
            }
            if(orders.getBuyerId() == pojo.getUserId().longValue()){
                orderEvidence.setBuyerContent(pojo.getContent());
                orderEvidence.setBuyerFiles(pojo.getFileName());
            }
            if(orders.getSellerId() == pojo.getUserId().longValue()){
                orderEvidence.setSellerFiles(pojo.getFileName());
                orderEvidence.setSellerContent(pojo.getContent());
            }
            if(orders.getArbitrate() == ParamType.ArbitrateStatus.NOARBITRATE.getStatus()){
                orders.setArbitrate(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                //订单仲裁表中的 对应订单的三条仲裁状态改为1 表示 仲裁者仲裁中
                List<OrderArbitrate> orderArbitrateList = orderArbitrateRepo.findOrderArbitrateByOrderId(orders.getId());
                for (OrderArbitrate o:orderArbitrateList) {
                    o.setStatus(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                    orderArbitrateRepo.save(o);
                }
                orderRepo.save(orders);
            }
            orderEvidence = orderEvidenceRepo.save(orderEvidence);
        } catch (Exception e) {
            LOG.error("upload evidence faild : {}",e.getMessage(),e);
        }
        return  orderEvidence!=null? RestResp.success():RestResp.fail();
    }
    /*
 * 对当前订单发起仲裁
 * */
    public RestResp arbitrateOrder(String id){
        OrdersInfo ordersInfo = null;
        Orders orders1 = null;
        try {
            Orders orders = orderRepo.findOne(id);
            //判断订单状态为3或7时可以仲裁
            if(orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_SEND.getStatus() || orders.getOrderStatus().longValue() == ParamType.OrderStatus.WAIT_REFUND.getStatus()){
                //仲裁状态（arbitrate）改为1 仲裁中
                orders.setArbitrate(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                //订单仲裁表中的 对应订单的三条仲裁状态改为1 表示 仲裁者仲裁中
                List<OrderArbitrate> orderArbitrateList = orderArbitrateRepo.findOrderArbitrateByOrderId(orders.getId());
                for (OrderArbitrate o:orderArbitrateList) {
                    o.setStatus(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                    orderArbitrateRepo.save(o);
                }
                orders1 = orderRepo.save(orders);
                ordersInfo = new OrdersInfo(orders1);
                this.setOrderStatusName(ordersInfo);
                return  orders1!=null?RestResp.success(ordersInfo):RestResp.fail();
            }
        } catch (Exception e) {
            LOG.error("apply for arbitrate order faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.fail();
    }
    /*
   * 仲裁者仲裁将密匙碎片给胜利者
   * */
    public RestResp arbitrateOrderToUser(Pojo pojo){
        OrderArbitrate orderArbitrate = null;
        try {
            orderArbitrate = orderArbitrateRepo.findOrderArbitrateByUserIdAndOrderId(pojo.getUserId(),pojo.getId());
            if(orderArbitrate.getStatus().longValue() == ParamType.ArbitrateStatus.ARBITRATEING.getStatus()){
                Orders orders = orderRepo.findOne(pojo.getId());
                if(pojo.getSuccessId().longValue() == BUYER_SUCCESS){
                    orderArbitrate.setBuyerAuth(orderArbitrate.getUserAuth());
                }
                if(pojo.getSuccessId().longValue() == SELLER_SUCCESS){
                    orderArbitrate.setSellerAuth(orderArbitrate.getUserAuth());
                }
                orderArbitrate.setStatus(ParamType.ArbitrateStatus.ARBITRATEEND.getStatus());
                OrderArbitrate orderArbitrate1 = orderArbitrateRepo.save(orderArbitrate);
                //仲裁完成后将系统通知发送到卖家买家两方
                messageService.postArbitrateMessage(orders,pojo.getUserId(),pojo.getSuccessId());

                //判断谁胜利了
                List<OrderArbitrate> list = orderArbitrateRepo.findOrderArbitrateByOrderId(pojo.getId());
                List<String> buyerList = new ArrayList<>(3);
                List<String> sellerList = new ArrayList<>(3);
                for (OrderArbitrate o:list) {
                    if(o.getBuyerAuth()!=null) {
                        buyerList.add(o.getBuyerAuth());
                    }
                    if(o.getSellerAuth()!=null) {
                        sellerList.add(o.getSellerAuth());
                    }
                }
                //判断如果有人胜利将BTC 转回到 胜利方的账户 订单仲裁状态改为2 仲裁结束 将仲裁表的三个仲裁人的信息全部改为2
                if(sellerList.size()>=ShamirUtil.K || buyerList.size()>=ShamirUtil.K){
                    OrderAddresskeys odk = orderAddresskeyRepo.findOrderAddresskeysByOrderId(pojo.getId());
                    String auth = "";
                    String address = "";
                    if(buyerList.size()>=ShamirUtil.K){
                        //将卖家的BTC从写上地址转回到 买家账户
                        auth = ShamirUtil.getAuth(buyerList.toArray(new String[buyerList.size()]))+","+odk.getBuyerPriAuth();
                        address = userRepo.findOne(orders.getBuyerId()).getFirstAddress();
                        orders.setOrderStatus(6L);
                    }
                    if(sellerList.size()>=ShamirUtil.K){
                        //将卖家的BTC从协商地址转回到 卖家账户
                        auth = ShamirUtil.getAuth(sellerList.toArray(new String[sellerList.size()]))+","+odk.getSellerPriAuth();
                        address = userRepo.findOne(orders.getSellerId()).getFirstAddress();
                        orders.setOrderStatus(7L);
                    }
                    OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),auth,orders.getAmount().doubleValue(),address);
                    HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
                    JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.MOVE_BTC,formEntity,JSONObject.class);
                    Integer status = (Integer) jsonObject.get("status");
                    if(status==1){
                        Notice notice = noticeRepo.findOne(orders.getNoticeId());
                        notice.setTxStatus(ParamType.NoticeTxStatus.TXEND.getStatus());
                        noticeRepo.save(notice);
                        orders.setArbitrate(ParamType.ArbitrateStatus.ARBITRATEEND.getStatus());
                        orders.setOrderStatus(ParamType.OrderStatus.CANCEL.getStatus());
                        orders.setFinishTime(DateUtil.getPresentDate());
                        Orders save = orderRepo.save(orders);
                        messageService.postArbitrateFinish(orders);

                        OrderArbitrate orderArbitrate2 = orderArbitrateRepo.findByOrOrderIdAndStatus(orders.getId(), ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                        orderArbitrateRepo.save(orderArbitrate2);

                        return RestResp.success(orderArbitrate);
                    }
                    else{
                        return RestResp.fail();
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("arbitrate orders to user faild : {}",e.getMessage(),e);
            return RestResp.fail("仲裁失败请稍后重试");
        }
        return RestResp.success(orderArbitrate);
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
    public RestResp getEvidence(Pojo pojo){
        OrderEvidence byOrderId = orderEvidenceRepo.findByOrderId(pojo.getId());
        return byOrderId!=null?RestResp.success(byOrderId):RestResp.fail();
    }
}
