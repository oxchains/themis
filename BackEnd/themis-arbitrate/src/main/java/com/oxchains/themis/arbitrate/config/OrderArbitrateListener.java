package com.oxchains.themis.arbitrate.config;

import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.arbitrate.common.ParamType;
import com.oxchains.themis.arbitrate.common.ShamirUtil;
import com.oxchains.themis.arbitrate.common.ThemisUserAddress;
import com.oxchains.themis.arbitrate.entity.Notice;
import com.oxchains.themis.arbitrate.entity.OrderAddresskeys;
import com.oxchains.themis.arbitrate.entity.OrderArbitrate;
import com.oxchains.themis.arbitrate.entity.Orders;
import com.oxchains.themis.arbitrate.repo.*;
import com.oxchains.themis.arbitrate.service.MessageService;
import com.oxchains.themis.common.model.OrdersKeyAmount;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by xuqi on 2017/11/9.
 * @author huohuo
 */
@Component
public class OrderArbitrateListener {
    @Resource
    private OrderRepo orderRepo;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private OrderArbitrateRepo arbitrateRepo;
    @Resource
    private OrderAddresskeyRepo addresskeyRepo;
    @Resource
    private UserRepo userRepo;
    @Resource
    private NoticeRepo noticeRepo;
    @Resource
    private MessageService messageService;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Scheduled(cron = "*/4 * * * * ?")
    public void orderArbitrateMonitor(){
        List<Orders> ordersList = orderRepo.findByArbitrate(ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
        for (Orders orders: ordersList) {
            List<OrderArbitrate> arbitrateList = arbitrateRepo.findByOrderId(orders.getId());
            List<String> buyerList = new ArrayList<>(ShamirUtil.N);
            List<String> sellerList = new ArrayList<>(ShamirUtil.N);
            for (OrderArbitrate o:arbitrateList) {
                if(o.getBuyerAuth()!=null) {
                    buyerList.add(o.getBuyerAuth());
                }
                if(o.getSellerAuth()!=null) {
                    sellerList.add(o.getSellerAuth());
                }
            }
            if(sellerList.size()>=ShamirUtil.K || buyerList.size()>=ShamirUtil.K){
                OrderAddresskeys odk = addresskeyRepo.findOrderAddresskeysByOrderId(orders.getId());

                String auth =buyerList.size()>=ShamirUtil.K?ShamirUtil.getAuth(buyerList.toArray(new String[buyerList.size()]))+","+odk.getBuyerPriAuth():ShamirUtil.getAuth(sellerList.toArray(new String[sellerList.size()]))+","+odk.getSellerPriAuth();
                String address = buyerList.size()>=ShamirUtil.K?userRepo.findOne(orders.getBuyerId()).getFirstAddress():userRepo.findOne(orders.getSellerId()).getFirstAddress();
                orders.setOrderStatus(buyerList.size()>=ShamirUtil.K?ParamType.OrderStatus.FINISH.getStatus():ParamType.OrderStatus.CANCEL.getStatus());

                OrdersKeyAmount ordersKeyAmount = new OrdersKeyAmount(orders.getId(),auth,orders.getAmount().doubleValue(),address);
                HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(ordersKeyAmount), this.getHttpHeader());
                JSONObject jsonObject = restTemplate.postForObject(ThemisUserAddress.MOVE_BTC,formEntity,JSONObject.class);
                Integer status = (Integer) jsonObject.get("status");
                if(status==1){
                    Notice notice = noticeRepo.findOne(orders.getNoticeId());
                    notice.setTxStatus(buyerList.size()>=ShamirUtil.K?ParamType.NoticeTxStatus.TXEND.getStatus():ParamType.NoticeTxStatus.NOTX.getStatus());
                    noticeRepo.save(notice);

                    orders.setArbitrate(ParamType.ArbitrateStatus.ARBITRATEEND.getStatus());
                    orders.setFinishTime(DateUtil.getPresentDate());
                    Orders save = orderRepo.save(orders);
                    messageService.postArbitrateFinish(orders);

                    List<OrderArbitrate> arbitrates = arbitrateRepo.findByOrOrderIdAndStatus(orders.getId(), ParamType.ArbitrateStatus.ARBITRATEING.getStatus());
                    for(OrderArbitrate oa :arbitrates){
                        oa.setStatus(ParamType.ArbitrateStatus.ARBITRATEEND.getStatus());
                        arbitrateRepo.save(oa);
                    }

                }
            }

        }



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
}