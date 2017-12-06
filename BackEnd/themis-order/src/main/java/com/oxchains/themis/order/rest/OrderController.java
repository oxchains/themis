package com.oxchains.themis.order.rest;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.order.common.Pojo;
import com.oxchains.themis.order.common.RestRespPage;
import com.oxchains.themis.order.entity.ValidaPojo.AddOrderPojo;
import com.oxchains.themis.order.entity.ValidaPojo.SaveAddresskeyPojo;
import com.oxchains.themis.order.entity.ValidaPojo.UploadTxIdPojo;
import com.oxchains.themis.order.entity.vo.OrdersInfo;
import com.oxchains.themis.order.entity.vo.UserTxDetails;
import com.oxchains.themis.order.service.OrderService;
import com.oxchains.themis.repo.entity.OrderAddresskeys;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.validation.Valid;
/**
 * Created by huohuo on 2017/10/23.
 * @author huohuo
 */
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;
    /*
   * 一 ：添加订单
   * */
    @RequestMapping("/order/addOrder")
    public RestResp addOrder(@Valid @RequestBody AddOrderPojo pojo,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return RestResp.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return orderService.addOrders(pojo);
    }
    /*
    * 二 ：卖家上传公私钥
    * */
    @RequestMapping("/order/saveAddresskey")
    public RestResp saveAddresskey(@Valid @RequestBody SaveAddresskeyPojo saveAddresskeyPojo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return RestResp.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return orderService.saveAddresskey(saveAddresskeyPojo);
    }
    /*
    * 三 ：卖家上传交易凭据
    * */
    @RequestMapping("/order/uploadTxId")
    public RestResp uploadTxId(@Valid @RequestBody UploadTxIdPojo pojo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return RestResp.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return orderService.uploadTxId(pojo);
    }
    //移动端
    /*
    * 移动端 卖家扫完付款 上传交易凭据
    * */
    @RequestMapping("/order/uploadTxIdMove")
    public RestResp uploadTxIdMove(@Valid @RequestBody UploadTxIdPojo pojo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return RestResp.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return orderService.uploadTxId(pojo);
    }
    /*
    * 四 ：发布公告的人确认订单
    * */
    @RequestMapping("/order/confirmOrders")
    public RestResp confirmOrders(@RequestBody Pojo pojo){
        return orderService.confirmOrders(pojo);
    }

    /*
    * 五 ：买家确认付款
    * */
    @RequestMapping("/order/confirmSendMoney")
    public RestResp confirmSendMoney(@RequestBody Pojo pojo){
        return orderService.confirmSendMoney(pojo);
    }
    /*
    * 六 ：卖家释放BTC
    * */
    @RequestMapping("/order/releaseBTC")
    public RestResp releaseBTC(@RequestBody Pojo pojo){
        return orderService.releaseBTC(pojo);
    }
    /*
    * 七 ：买家确认收货
    * */
    @RequestMapping("/order/confirmReciveBTC")
    public RestResp confirmReciveBTC(@RequestBody Pojo pojo){
        return orderService.confirmReciveBTC(pojo);
    }
    /*
    * 八 ：取消订单
    * */
    @RequestMapping("/order/cancelOrders")
    public RestResp cancelOrders(@RequestBody Pojo pojo){
        return orderService.cancelOrders(pojo.getId(),pojo.getUserId());
    }
    /*
   * 九 ：买家确认收到退款
   * */
    @RequestMapping("/order/confirmReceiveRefund")
    public RestResp confirmReceiveRefund(@RequestBody Pojo pojo){
        return orderService.confirmReceiveRefund(pojo);
    }
    /*
    * 十四 ：提交评论
    * */
    @RequestMapping("/order/saveComment")
    public RestResp saveComment(@Valid @RequestBody Pojo pojo,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return RestResp.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return orderService.saveComment(pojo);
    }

    /*
    * 根据订单id获取订单详情
    * */
    @RequestMapping("/order/findOrdersDetails")
    public RestResp findOrdersDetails(@RequestBody Pojo pojo){
        OrdersInfo o = orderService.findOrdersDetails(pojo);
     return o!=null?RestResp.success(o): RestResp.fail("服务器繁忙,请稍后再试!");
    }
    /*
    * 根据id查询自己已完成的的订单
    * */
    @RequestMapping("/order/findCompletedOrders")
    public RestRespPage findCompletedOrders(@RequestBody Pojo pojo){
        this.checkPage(pojo);
        return orderService.findCompletedOrdersById(pojo);
    }
    /*
   * 根据id查询自己未完成的的订单
   * */
    @RequestMapping("/order/findNoCompletedOrders")
    public RestRespPage findNoCompletedOrders(@RequestBody Pojo pojo){
        this.checkPage(pojo);
        return orderService.findNoCompletedOrdersById(pojo);
    }

    /*
    * 获取卖家历史交易资料有 好评率 交易次数 第一次购买时间 用户创建时间 交易量 电子邮箱验证否 电话号码验证否 实名认证否 信任量
    * */
    @RequestMapping("/order/findUserTxDetail")
    public RestResp findUserTxDetails(@RequestBody Pojo pojo){
        UserTxDetails UserTxDetails = orderService.findUserTxDetails(pojo);
        return UserTxDetails!=null?RestResp.success(UserTxDetails):RestResp.fail("未知错误");
    }
    /*
    * 购买出售详情页面需要查的用户的历史交易信息 和公告的信息
    * */
    @RequestMapping("/order/findUserTxDetailAndNotice")
    public RestResp findUserTxDetailsAndNotice(@RequestBody Pojo pojo){
        UserTxDetails UserTxDetailsAndNotice = orderService.findUserTxDetailsAndNotice(pojo);
        return RestResp.success(UserTxDetailsAndNotice);
    }
    /*
    * 判断卖家有没有上传公私匙
    * */
    @RequestMapping("/order/judgeSellerPubPriAuth")
    public RestResp judgeSellerPubPriAuth(@RequestBody Pojo pojo){
        return orderService.judgeSellerPubPriAuth(pojo);
    }
    /*
    * 判断卖家有没有释放BTC   success 已经释放   fail  未释放
    * */
    @RequestMapping("/order/sellerReleaseBTCIsOrNot")
    public RestResp sellerReleaseBTCIsOrNot(@RequestBody Pojo pojo){
        boolean b = orderService.sellerReleaseBTCIsOrNot(pojo);
        return b?RestResp.success():RestResp.fail();
    }
    /*
    * 购买出售详情页面需要查的用户的历史交易信息 和公告的信息
    * */
    @RequestMapping("/order/findUserDetail")
    public RestResp findUserDetail(@RequestBody Pojo pojo){
        UserTxDetails UserTxDetailsAndNotice = orderService.findUserTxDetailsAndNotice(pojo);
        return UserTxDetailsAndNotice==null?RestResp.fail():RestResp.success(UserTxDetailsAndNotice);
    }

    private void checkPage(Pojo pojo){
        if(pojo.getPageSize() == null){
            pojo.setPageSize(8);
        }
        if(pojo.getPageNum() == null){
            pojo.setPageNum(1);
        }
    }
}



