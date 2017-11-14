package com.oxchains.themis.arbitrate.service;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.arbitrate.common.*;
import com.oxchains.themis.arbitrate.entity.OrderEvidence;
import com.oxchains.themis.arbitrate.entity.Orders;
import com.oxchains.themis.arbitrate.entity.vo.OrdersInfo;
import com.oxchains.themis.arbitrate.repo.*;
import com.oxchains.themis.common.constant.*;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.repo.entity.Notice;
import com.oxchains.themis.repo.entity.OrderArbitrate;
import com.oxchains.themis.repo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    private PaymentRepo paymentRepo;
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
        try {
            OrdersInfo ordersInfo = null;
            Page<OrderArbitrate> orderArbitratePage = null;
            orderArbitratePage = orderArbitrateRepo.findByUserIdAndAndStatusIsNot(pojos.getUserId(),ParamType.ArbitrateStatus.NOARBITRATE.getStatus(),pageable);
            ordersInfoList = new ArrayList<>();
            for (OrderArbitrate o: orderArbitratePage.getContent()) {
                ordersInfo = this.findOrdersDetails(o.getOrderId());
                ordersInfo.setBuyerUsername(this.getUserById(ordersInfo.getBuyerId()).getLoginname());
                ordersInfo.setSellerUsername(this.getUserById(ordersInfo.getSellerId()).getLoginname());
                this.setOrderStatusName(ordersInfo);
                ordersInfo.setPageCount(orderArbitratePage.getTotalPages());
                ordersInfo.setStatus(o.getStatus());
                ordersInfoList.add(ordersInfo);
            }
        } catch (Exception e) {
            LOG.error("find arbitrate order faild : {}",e.getMessage(),e);
            return RestResp.fail("未知错误");
        }
        return RestResp.success(ordersInfoList);
    }
    /*
    * 根据订单编号查询订单的详细信息
    * */
    public OrdersInfo findOrdersDetails(String orderId){
        Orders o = null;
        OrdersInfo ordersInfo = null;
        try {
            o = orderRepo.findOne(orderId);
            ordersInfo = new OrdersInfo(o);
            ordersInfo.setNotice(this.findNoticeById(o.getNoticeId()));
            this.setOrderStatusName(ordersInfo);
            ordersInfo.setPayment(paymentRepo.findOne(ordersInfo.getPaymentId()));
            return ordersInfo;
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
            StringBuilder imageName = new StringBuilder();
            List<MultipartFile> multipartFileList = Arrays.asList(pojo.getFiles());
            for(MultipartFile mf:multipartFileList){
                String filename = mf.getOriginalFilename();
                String suffix = filename.substring(filename.lastIndexOf("."));
                UUID uuid = UUID.randomUUID();
                String newFileName = uuid.toString() + suffix;
                mf.transferTo(new File(imageUrl+newFileName));
                imageName.append(",");
                imageName.append(newFileName);
            }
            orderEvidence = orderEvidenceRepo.findByOrderId(pojo.getId());
            Orders orders = orderRepo.findOne(pojo.getId());
            if(orderEvidence == null){
                orderEvidence = new OrderEvidence();
                orderEvidence.setOrderId(pojo.getId());
                orderEvidence = orderEvidenceRepo.save(orderEvidence);
                orders.setArbitrate(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                orders = orderRepo.save(orders);
                //订单仲裁表中的 对应订单的三条仲裁状态改为1 表示 仲裁者仲裁中
                List<OrderArbitrate> orderArbitrateList = orderArbitrateRepo.findByOrderId(orders.getId());
                for (OrderArbitrate o:orderArbitrateList) {
                    o.setStatus(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                    orderArbitrateRepo.save(o);
                }
                messageService.postEvidenceMessage(orders,pojo.getUserId());
            }
            if(orders.getBuyerId() == pojo.getUserId().longValue()){
                orderEvidence.setBuyerContent(pojo.getContent());
                orderEvidence.setBuyerFiles(imageName.toString().substring(1));
            }
            if(orders.getSellerId() == pojo.getUserId().longValue()){
                orderEvidence.setSellerFiles(imageName.toString().substring(1));
                orderEvidence.setSellerContent(pojo.getContent());
            }
            orderEvidence = orderEvidenceRepo.save(orderEvidence);
            messageService.postUploadEvidence(orders,pojo.getUserId());
        } catch (Exception e) {
            LOG.error("upload evidence faild : {}",e.getMessage(),e);
            return RestResp.fail("申请仲裁失败");
        }
        return  orderEvidence!=null? RestResp.success():RestResp.fail();
    }

    /*
   * 仲裁者仲裁将密匙碎片给胜利者 并且判断一下是谁胜利了
   * */
    public RestResp arbitrateOrderToUser(Pojo pojo){
        OrderArbitrate orderArbitrate = null;
        try {
            orderArbitrate = orderArbitrateRepo.findByUserIdAndOrderId(pojo.getUserId(),pojo.getId());
            if(orderArbitrate.getStatus().longValue() == ParamType.ArbitrateStatus.ARBITRATEING.getStatus()){
                Orders orders = orderRepo.findOne(pojo.getId());
                if(pojo.getSuccessId().longValue() == BUYER_SUCCESS){
                    orderArbitrate.setBuyerAuth(orderArbitrate.getUserAuth());
                }
                if(pojo.getSuccessId().longValue() == SELLER_SUCCESS){
                    orderArbitrate.setSellerAuth(orderArbitrate.getUserAuth());
                }
                orderArbitrate.setStatus(ParamType.ArbitrateStatus.ARBITRATEEND.getStatus());
                orderArbitrate = orderArbitrateRepo.save(orderArbitrate);
                //仲裁完成后将系统通知发送到卖家买家两方
                messageService.postArbitrateMessage(orders,pojo.getUserId(),pojo.getSuccessId());
            }
        } catch (Exception e) {
            LOG.error("arbitrate orders to user faild : {}",e.getMessage(),e);
            return RestResp.fail("仲裁失败请稍后重试");
        }
        return orderArbitrate!=null?RestResp.success(orderArbitrate):RestResp.fail();
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
        return RestResp.success(orderEvidenceRepo.findByOrderId(pojo.getId()));
    }
    public RestResp saveOrderAbritrate(OrderArbitrate orderArbitrate){
        OrderArbitrate save = orderArbitrateRepo.save(orderArbitrate);
        return save != null?RestResp.success():RestResp.fail();
    }
    public User getUserById(Long userId){
        User user = null;
        try {
            JSONObject str = restTemplate.getForObject(com.oxchains.themis.common.constant.ThemisUserAddress.GET_USER+userId, JSONObject.class);
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
    public Notice saveNotice(Long id, Integer sta){
        try {
            JSONObject forObject = restTemplate.getForObject(com.oxchains.themis.common.constant.ThemisUserAddress.SAVE_NOTICE + id + "/" + sta, JSONObject.class);
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
    public Notice findNoticeById(Long id){
        try {
            JSONObject forObject = restTemplate.getForObject(com.oxchains.themis.common.constant.ThemisUserAddress.GET_NOTICE + id, JSONObject.class);
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
