package com.oxchains.themis.order.rest;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.order.common.Pojo;
import com.oxchains.themis.order.entity.*;
import com.oxchains.themis.order.service.OrderService;
import org.hibernate.criterion.Order;
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
    public RestResp addOrder(@RequestBody Pojo pojo){
        Orders orders1 = orderService.addOrders(pojo);
        return orders1 !=null?RestResp.success(orders1):RestResp.fail();
    }
    /*
    * 二 ：卖家上传公私钥
    * */
    @RequestMapping("/order/saveAddresskey")
    public RestResp saveAddresskey(@RequestBody  OrderAddresskeys orderAddresskeys){
        Orders orders = orderService.saveAddresskey(orderAddresskeys);
        return orders==null?RestResp.fail():RestResp.success(orders);
    }
    /*
    * 三 ：卖家上传交易凭据
    * */
    @RequestMapping("/order/uploadTxId")
    public RestResp uploadTxId(@RequestBody Pojo pojo){
        Orders orders1 = orderService.uploadTxId(pojo);
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
    * 六 ：卖家释放BTC
    * */
    @RequestMapping("/order/releaseBTC")
    public RestResp releaseBTC(@RequestBody Pojo pojo){
        OrderAddresskeys orderAddresskeys = orderService.releaseBTC(pojo);
        return orderAddresskeys!=null?RestResp.success():RestResp.fail();
    }
    /*
    * 七 ：买家确认收货
    * */
    @RequestMapping("/order/confirmReciveBTC")
    public RestResp confirmReciveBTC(@RequestBody Pojo pojo){
        Orders orders = orderService.confirmReciveBTC(pojo);
        return orders!=null?RestResp.success():RestResp.fail();
    }
    /*
    * 八 ：取消订单
    * */
    @RequestMapping("/order/cancelOrders")
    public RestResp cancelOrders(@RequestBody Pojo pojo){
        Orders orders = orderService.cancelOrders(pojo.getId(),pojo.getUserId());
        return orders!=null?RestResp.success(orders):RestResp.fail();
    }
    /*
   * 九 ：买家确认收到退款
   * */
    @RequestMapping("/order/confirmReceiveRefund")
    public RestResp confirmReceiveRefund(@RequestBody Pojo pojo){
        Orders orders = orderService.confirmReceiveRefund(pojo);
        return orders!=null?RestResp.success(orders):RestResp.fail();
    }
    /*
    * 十 ：申请仲裁订单
    * */
    @RequestMapping("/order/arbitrateOrder")
    public RestResp arbitrateOrder(@RequestBody Pojo pojo){
        Orders orders = orderService.arbitrateOrder(pojo.getId());
        return orders!=null?RestResp.success(orders):RestResp.fail();
    }
    /*
    * 十一 仲裁者查看自己可以仲裁的订单
    * */
    @RequestMapping("/order/findArbitrareOrderById")
    public RestResp findArbitrareOrderById(@RequestBody Pojo pojo){
        List<Orders> list = orderService.findArbitrareOrderById(pojo.getUserId());
        return RestResp.success(list);
    }
    /*
    * 十二 仲裁者对订单进行仲裁 仲裁者仲裁将密匙碎片给胜利者
    * */
    @RequestMapping("/order/arbitrateOrderToUser")
    public RestResp arbitrateOrderToUser(@RequestBody Pojo pojo){
        OrderArbitrate orderArbitrate = orderService.arbitrateOrderToUser(pojo);
        return orderArbitrate!=null?RestResp.success():RestResp.fail();
    }
    /*
    * 十三 ： 用户获取订单的 协商地址 自己的 公匙 私匙 卖家的公匙私匙 仲裁者的公匙私匙  交易的量
    * */
    @RequestMapping("/order/findOrderAddressKeys")
    public RestResp findOrderAddressKeys(@RequestBody Pojo pojo){
        OrderAddresskeys orderAddresskeys  = orderService.findOrderAddressKeys(pojo);
        return orderAddresskeys!=null?RestResp.success(orderAddresskeys):RestResp.fail();
    }
    /*
    * 十四 ：提交评论
    * */
    @RequestMapping("/order/saveComment")
    public RestResp saveComment(@RequestBody Pojo pojo){
        OrderComment orderComments = orderService.saveComment(pojo);
        return orderComments!=null?RestResp.success():RestResp.fail();
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
    * 获取卖家历史交易资料有 好评率 交易次数 第一次购买时间 用户创建时间 交易量 电子邮箱验证否 电话号码验证否 实名认证否 信任量
    * */
    @RequestMapping("/order/findUserTxDetail")
    public RestResp findUserTxDetail(@RequestBody Pojo pojo){
        UserTxDetail userTxDetail = orderService.findUserTxDetail(pojo);
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
        UserTxDetail userTxDetailAndNotice = orderService.findUserTxDetailAndNotice(pojo);
        return userTxDetailAndNotice==null?RestResp.fail():RestResp.success(userTxDetailAndNotice);
    }
    /*
    * 判断卖家有没有上传公私匙
    * */
    @RequestMapping("/order/judgeSellerPubPriAuth")
    public RestResp judgeSellerPubPriAuth(@RequestBody Pojo pojo){
        Orders b = orderService.judgeSellerPubPriAuth(pojo);
        return b!=null?RestResp.success(b):RestResp.fail();
    }
    /*
    * 判断卖家有没有释放BTC   success 已经释放   fail  未释放
    * */
    @RequestMapping("/order/sellerReleaseBTCIsOrNot")
    public RestResp sellerReleaseBTCIsOrNot(@RequestBody Pojo pojo){
        boolean b = orderService.sellerReleaseBTCIsOrNot(pojo);
        return b?RestResp.success():RestResp.fail();
    }
}



