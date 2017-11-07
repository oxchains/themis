package com.oxchains.themis.order.config;

import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.order.entity.Orders;
import com.oxchains.themis.order.repo.OrderRepo;
import com.oxchains.themis.order.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
    @Scheduled(cron = "*/4 * * * * ?")
    public void orderAddressLis(){
        List<Orders> ordersByOrderStatus = orderRepo.findOrdersByOrderStatus(1L);
        for (Orders o: ordersByOrderStatus) {
            JSONObject restResp = restTemplate.getForObject("http://themis-user/account/"+o.getId(), JSONObject.class);
            Integer status  = (Integer) restResp.get("status");
            System.out.println(status);
            if(status==1){
                o.setOrderStatus(2L);
                o = orderRepo.save(o);
                messageService.postOrderMessage(o);
            }
        }
    }

}
