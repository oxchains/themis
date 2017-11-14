package com.oxchains.themis.arbitrate.entity.vo;
import com.oxchains.themis.arbitrate.entity.Orders;
import com.oxchains.themis.arbitrate.entity.Payment;
import com.oxchains.themis.repo.entity.Notice;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/11/2.
 *
 * @author huohuo
 * @Date:Created in 9:25 2017/11/2
 */
@Data
public class OrdersInfo {
    private String id;         //订单编号
    private BigDecimal money;  //订单金额
    private String createTime;  //下单时间
    private String finishTime;//完成时间
    private BigDecimal amount; //交易数量
     private Long paymentId; //支付方式编号  1 现金 2 转账 3 支付宝 4 微信 5 Apple Pay
    private Long vcurrencyId; //数字货币币种 1 比特币
    private Long currencyId;  //纸币币种    1  人民币 2  美元
    private Long buyerId;     // 买家id
    private Long sellerId;    //卖家id
    private Long orderStatus; // 订单状态    1  待确认 2 代付款  3 待收货 4  待评价 5 完成 6  已取消 7等待卖家退款 8 仲裁中
    private Long noticeId;
    private String p2shAddress;  //协商地址
    private String orderStatusName; //订单状态名称
    private Notice notice;  //相关联的公告信息
    private Payment payment; //相关联的 支付方式信息
    private int arbitrate;   //是否在仲裁中 默认 0： 不在仲裁中 1： 在仲裁中 2:仲裁结束
    private String orderType;  //  交易类型     购买  或 出售
    private String friendUsername; //交易伙伴名称
    private String buyerUsername; //买家名称
    private String sellerUsername; //卖家名称
    private String uri;
    private Orders orders;
    private Integer pageCount;
    private Integer status;

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

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getP2shAddress() {
        return p2shAddress;
    }

    public void setP2shAddress(String p2shAddress) {
        this.p2shAddress = p2shAddress;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
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

    public int getArbitrate() {
        return arbitrate;
    }

    public void setArbitrate(int arbitrate) {
        this.arbitrate = arbitrate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public OrdersInfo(Orders orders) {
        if(orders != null){
            this.id = orders.getId();
            this.money = orders.getMoney();
            this.amount = orders.getAmount();
            this.arbitrate = orders.getArbitrate();
            this.buyerId=orders.getBuyerId();
            this.sellerId=orders.getSellerId();
            this.createTime=orders.getCreateTime();
            this.currencyId=orders.getCurrencyId();
            this.vcurrencyId=orders.getVcurrencyId();
            this.finishTime=orders.getFinishTime();
            this.noticeId=orders.getNoticeId();
            this.paymentId=orders.getPaymentId();
            this.orderStatus=orders.getOrderStatus();
        }
    }
    @Override
    public String toString() {
        return "OrdersInfo{" +
                "id='" + id + '\'' +
                ", money=" + money +
                ", createTime='" + createTime + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", amount=" + amount +
                ", paymentId=" + paymentId +
                ", vcurrencyId=" + vcurrencyId +
                ", currencyId=" + currencyId +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", orderStatus=" + orderStatus +
                ", noticeId=" + noticeId +
                ", p2shAddress='" + p2shAddress + '\'' +
                ", orderStatusName='" + orderStatusName + '\'' +
                ", arbitrate=" + arbitrate +
                ", orderType='" + orderType + '\'' +
                ", friendUsername='" + friendUsername + '\'' +
                ", buyerUsername='" + buyerUsername + '\'' +
                ", sellerUsername='" + sellerUsername + '\'' +
                ", uri='" + uri + '\'' +
                ", pageCount=" + pageCount +
                ", status=" + status +
                '}';
    }
}
