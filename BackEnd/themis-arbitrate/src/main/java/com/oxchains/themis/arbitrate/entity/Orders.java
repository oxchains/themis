package com.oxchains.themis.arbitrate.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/23.
 */
@Entity
@Table(name = "tbl_biz_orders")
public class Orders {
    @Id
    private String id;         //订单编号
    private BigDecimal money;  //订单金额
    private String createTime;  //下单时间
    private String finishTime;//完成时间
    private BigDecimal amount; //交易数量
   // private Long paymentId; //支付方式编号  1 现金 2 转账 3 支付宝 4 微信 5 Apple Pay
    private Long vcurrencyId; //数字货币币种 1 比特币
    private Long currencyId;  //纸币币种    1  人民币 2  美元
    private Long buyerId;     // 买家id
    private Long sellerId;    //卖家id
    private Long orderStatus; // 订单状态    1  待确认 2 代付款  3 待收货 4  待评价 5 完成 6  已取消 7等待卖家退款 8 仲裁中
   // private Long noticeId;
   //公告id
    @Transient
    private String orderStatusName;
    @ManyToOne
    private Notice notice;
    @ManyToOne
    private Payment payment;

    private int arbitrate;   //是否在仲裁中 默认 0： 不在仲裁中 1： 在仲裁中 2:仲裁结束
    @Transient
    private String orderType;  //  交易类型     购买  或 出售
    @Transient
    private String friendUsername;
    @Transient
    private String buyerUsername;
    @Transient
    private String sellerUsername;

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public int getArbitrate() {
        return arbitrate;
    }

    public void setArbitrate(int arbitrate) {
        this.arbitrate = arbitrate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Long getVcurrencyId() {
        return vcurrencyId;
    }

    public void setVcurrencyId(Long vcurrencyId) {
        this.vcurrencyId = vcurrencyId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Long orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
