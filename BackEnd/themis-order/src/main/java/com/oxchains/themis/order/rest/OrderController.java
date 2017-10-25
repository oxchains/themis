package com.oxchains.themis.order.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.order.entity.Orders;
import com.oxchains.themis.order.entity.Pojo;
import com.oxchains.themis.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/**
 * Created by xuqi on 2017/10/23.
 */
@RestController
public class OrderController {
    private OrderService orderService;
    public OrderController(@Autowired OrderService orderService) {
        this.orderService = orderService;
    }
    /*
    * 获取所有的订单
    * */
    @RequestMapping("/order/findOrder")
    public RestResp findOrders(){
        List<Orders> list = orderService.findOrders();
        return RestResp.success(list);
    }
    /*
    * 根据订单id获取订单详情
    * */
    @RequestMapping("/order/findOrdersDetails")
    public RestResp findOrdersDetails(@RequestBody Pojo pojo){
        System.out.println(pojo.getId());
     Orders o = orderService.findOrdersDetails(pojo.getId());
     return o!=null?RestResp.success(o): RestResp.fail();
    }
    /*
    * 下个订单 订单信息和仲裁人列表
    * */
    @RequestMapping("/order/addOrder")
    public RestResp addOrder(@RequestBody  Orders orders){
        Orders orders1 = orderService.addOrders(orders);
        return orders1 !=null?RestResp.success(orders1):RestResp.fail();
    }
    /*
 * 根据id查询自己已完成的的订单
 * */
    @RequestMapping("/order/findCompletedOrders")
    public RestResp findCompletedOrders(@RequestBody Pojo pojo){
        List<Orders>  list= orderService.findCompletedOrdersById(pojo.getUserId());
        return list!=null?RestResp.success(list):RestResp.fail();
    }
    /*
   * 根据id查询自己未完成的的订单
   * */
    @RequestMapping("/order/findNoCompletedOrders")
    public RestResp findNoCompletedOrders(@RequestBody Pojo pojo){
        List<Orders>  list= orderService.findNoCompletedOrdersById(pojo.getUserId());
        return list!=null?RestResp.success(list):RestResp.fail();
    }
    /*
    * 取消订单
    * */
    @RequestMapping("/order/cancelOrders")
    public RestResp cancelOrders(@RequestBody Pojo pojo){
       Orders orders = orderService.cancelOrders(pojo.getId(),pojo.getUserId());
       return orders!=null?RestResp.success(orders):RestResp.fail();
    }
    /*
    * 发布公告的人确认订单
    * */
    @RequestMapping("/order/confirmOrders")
    public RestResp confirmOrders(@RequestBody Pojo pojo){
        Orders o = orderService.confirmOrders(pojo.getId());
        return o!=null?RestResp.success(o):RestResp.fail();
    }
    /*
    * 查询自己发布的订单锁生成的需要自己确认的订单
    * */
    @RequestMapping("/order/findNotConfirmOrders")
    public RestResp findNotConfirmOrders(@RequestBody Pojo pojo){
        List<Orders> list = orderService.findNotConfirmOrders(pojo.getUserId());
        return RestResp.success(list);
    }
    /*
    * 买家确认收到退款
    * */
    public RestResp confirmReceiveRefund(@RequestBody Pojo pojo){
     Orders orders = orderService.confirmReceiveRefund(pojo.getId());
     return orders!=null?RestResp.success(orders):RestResp.fail();
    }
    /*
    * 申请仲裁订单
    * */
    public RestResp arbitrateOrder(@RequestBody Pojo pojo){
        Orders orders = orderService.arbitrateOrder(pojo.getId());
        return orders!=null?RestResp.success(orders):RestResp.fail();
    }
 /*
    * 根据仲裁者id查找哪些订单可以被自己仲裁的订单列表
    * */
    public RestResp findArbitrareOrderById(@RequestBody Pojo pojo){
        List<Orders> list = orderService.findArbitrareOrderById(pojo.getUserId());
        return RestResp.success(list);
    }

}



