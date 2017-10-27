package com.oxchains.themis.order.rest;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.order.common.Pojo;
import com.oxchains.themis.order.entity.*;
import com.oxchains.themis.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/**
 * Created by huohuo on 2017/10/23.
 */
@RestController
public class OrderController {
    private OrderService orderService;
    public OrderController(@Autowired OrderService orderService) {
        this.orderService = orderService;
    }
    /*
   * 一 ：添加订单
   * */
    @RequestMapping("/order/addOrder")
    public RestResp addOrder(@RequestBody  Orders orders){
        Orders orders1 = orderService.addOrders(orders);
        return orders1 !=null?RestResp.success(orders1):RestResp.fail();
    }
    /*
    * 二 ：卖家上传公私钥
    * */
    @RequestMapping("/order/saveAddresskey")
    public RestResp saveAddresskey(@RequestBody OrderAddresskeys orderAddresskeys){
        System.out.println(orderAddresskeys);
        Orders orders = orderService.saveAddresskey(orderAddresskeys);
        return orderAddresskeys==null?RestResp.fail():RestResp.success(orders);
    }
    /*
    * 三 ：卖家上传交易凭据
    * */
    @RequestMapping("/order/uploadTxId")
    public RestResp uploadTxId(@RequestBody Orders orders){
        Orders orders1 = orderService.uploadTxId(orders);
        return orders1!=null?RestResp.success():RestResp.fail();
    }
    /*
    * 四 ：发布公告的人确认订单
    * */
    @RequestMapping("/order/confirmOrders")
    public RestResp confirmOrders(@RequestBody Pojo pojo){
        Orders o = orderService.confirmOrders(pojo);
        return o!=null?RestResp.success(o):RestResp.fail();
    }

    /*
    * 五 ：买家确认付款
    * */
    @RequestMapping("/order/confirmSendMoney")
    public RestResp confirmSendMoney(@RequestBody Pojo pojo){
        Orders orders = orderService.confirmSendMoney(pojo);
        return orders!=null?RestResp.success():RestResp.fail();
    }
    /*
    * 六 ：取消订单
    * */
    @RequestMapping("/order/cancelOrders")
    public RestResp cancelOrders(@RequestBody Pojo pojo){
        Orders orders = orderService.cancelOrders(pojo.getId(),pojo.getUserId());
        return orders!=null?RestResp.success(orders):RestResp.fail();
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
        Orders o = orderService.findOrdersDetails(pojo);
     return o!=null?RestResp.success(o): RestResp.fail();
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
    * 查询自己发布的公告所生成的需要自己确认的订单
    * */
    @RequestMapping("/order/findNotConfirmOrders")
    public RestResp findNotConfirmOrders(@RequestBody Pojo pojo){
        List<Orders> list = orderService.findNotConfirmOrders(pojo.getUserId());
        return RestResp.success(list);
    }
    /*
    * 买家确认收到退款
    * */
    @RequestMapping("/order/confirmReceiveRefund")
    public RestResp confirmReceiveRefund(@RequestBody Pojo pojo){
     Orders orders = orderService.confirmReceiveRefund(pojo);
     return orders!=null?RestResp.success(orders):RestResp.fail();
    }
    /*
    * 申请仲裁订单
    * */
    @RequestMapping("/order/arbitrateOrder")
    public RestResp arbitrateOrder(@RequestBody Pojo pojo){
        Orders orders = orderService.arbitrateOrder(pojo.getId());
        return orders!=null?RestResp.success(orders):RestResp.fail();
    }
 /*
    * 根据仲裁者id查找哪些订单可以被自己仲裁的订单列表
    * */
    @RequestMapping("/order/findArbitrareOrderById")
    public RestResp findArbitrareOrderById(@RequestBody Pojo pojo){
        List<Orders> list = orderService.findArbitrareOrderById(pojo.getUserId());
        return RestResp.success(list);
    }


    /*
    * 仲裁者仲裁将密匙碎片给胜利者
    * */
    @RequestMapping("/order/arbitrateOrderToUser")
    public RestResp arbitrateOrderToUser(@RequestBody Pojo pojo){
       OrderArbitrate orderArbitrate = orderService.arbitrateOrderToUser(pojo);
       return orderArbitrate!=null?RestResp.success():RestResp.fail();
    }
    /*
    * 获取卖家历史交易资料有 好评率 交易次数 第一次购买时间 用户创建时间 交易量 电子邮箱验证否 电话号码验证否 实名认证否 信任量
    * */
    @RequestMapping("/order/findUserTxDetail")
    public RestResp findUserTxDetail(@RequestBody Pojo pojo){
        System.out.println(pojo);
        UserTxDetail userTxDetail = orderService.findUserTxDetail(pojo);
        System.out.println(userTxDetail);
        return userTxDetail!=null?RestResp.success(userTxDetail):RestResp.fail();
    }
    /*
    * 张晓晶 调试状态用
    * */
    @RequestMapping("/order/{orderid}/{status}")
    public RestResp updateOrderStatus(@PathVariable("orderid") String orderId,@PathVariable("status") Long  status){
        Orders o = orderService.updateOrderStatus(orderId,status);
     return o==null?RestResp.fail():RestResp.success(o);
    }
    /*
    * 购买出售详情页面需要查的用户的历史交易信息 和公告的信息
    * */
    @RequestMapping("/order/findUserTxDetailAndNotice")
    public RestResp findUserTxDetailAndNotice(@RequestBody Pojo pojo){
        System.out.println(pojo);
        UserTxDetail userTxDetailAndNotice = orderService.findUserTxDetailAndNotice(pojo);
        return userTxDetailAndNotice==null?RestResp.fail():RestResp.success(userTxDetailAndNotice);
    }
    /*
    * 判断卖家有没有上传公私匙
    * */
    public RestResp judgeSellerPubPriAuth(@RequestBody Pojo pojo){
        Boolean b = orderService.judgeSellerPubPriAuth(pojo);
        return b?RestResp.success():RestResp.fail();
    }

}



