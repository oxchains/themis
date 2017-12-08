package com.oxchains.themis.order.entity.vo;

import com.oxchains.themis.repo.entity.Notice;
import com.oxchains.themis.repo.entity.Orders;
import com.oxchains.themis.repo.entity.Payment;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/11/2.
 *
 * @author huohuo
 * @Date:Created in 9:25 2017/11/2
 */
@Data
public class OrdersInfo implements Serializable{
    private String p2shAddress;  //协商地址
    private String orderStatusName; //订单状态名称
    private Notice notice;  //相关联的公告信息
    private Payment payment; //相关联的 支付方式信息
    private String orderType;  //  交易类型     购买  或 出售
    private String friendUsername; //交易伙伴名称
    private String buyerUsername; //买家名称
    private String sellerUsername; //卖家名称
    private Integer status;
    private String amount;
    private Long partnerUserId;
    private String txId;
    public OrdersInfo(Orders orders) {
        if(orders != null){
            this.setId(orders.getId());
            this.setMoney(orders.getMoney());
            this.amount = orders.getAmount().toPlainString();
            this.setArbitrate(orders.getArbitrate());
            this.setBuyerId(orders.getBuyerId());
            this.setSellerId(orders.getSellerId());
            this.setCreateTime(orders.getCreateTime());
            this.setCurrencyId(orders.getCurrencyId());
            this.setVcurrencyId(orders.getVcurrencyId());
            this.setFinishTime(orders.getFinishTime());
            this.setNoticeId(orders.getNoticeId());
            this.setPaymentId(orders.getPaymentId());
            this.setOrderStatus(orders.getOrderStatus());
            this.setUri(orders.getUri());

        }
    }




    private String id;         //订单编号
    private BigDecimal money;  //订单金额
    private String createTime;  //下单时间
    private String finishTime;//完成时间
    private Long paymentId; //支付方式编号  1 现金 2 转账 3 支付宝 4 微信 5 Apple Pay
    private Long vcurrencyId; //数字货币币种 1 比特币
    private Long currencyId;  //纸币币种    1  人民币 2  美元
    private Long buyerId;     // 买家id
    private Long sellerId;    //卖家id
    private Long orderStatus; // 订单状态    1  待确认 2 代付款  3 待收货 4  待评价 5 完成 6  已取消 7等待卖家退款 8 仲裁中
    private Long noticeId;
    private int arbitrate;   //是否在仲裁中 默认 0： 不在仲裁中 1： 在仲裁中 2:仲裁结束
    private String uri;

}
