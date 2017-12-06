package com.oxchains.themis.order.config;

import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.order.service.MessageService;
import com.oxchains.themis.repo.dao.OrderRepo;
import com.oxchains.themis.repo.entity.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huohuo on 2017/10/28.
 * @author huohuo
 */
@Component
public class OrderListener {
    @Resource
    private OrderRepo orderRepo;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private MessageService messageService;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Scheduled(cron = "*/4 * * * * ?")
    public void orderAddressLis(){
        try {
            List<Orders> ordersByOrderStatus = orderRepo.findOrdersByOrderStatus(1L);
            for (Orders o: ordersByOrderStatus) {
                JSONObject restResp = restTemplate.getForObject("http://themis-user/account/"+o.getId(), JSONObject.class);
                Integer status  = (Integer) restResp.get("status");
                if(status==1){
                    o.setOrderStatus(2L);
                    o = orderRepo.save(o);
                    messageService.postConfirmOrder(o);
                }
            }
        } catch (RestClientException e) {
            LOG.error("check orders is or not  faild : {}",e);
        }
    }

}
