package com.oxchains.themis.user.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author ccl
 * @Time 2017-10-17 11:11
 * @Name Order
 * @Desc:
 */
@Entity
@Table(name = "tbl_biz_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 35)
    private String fromAddress;

    @Column(length = 35)
    private String recvAddress;

    @Column(length = 35)
    private String p2shAddress;

    @Column(length = 1024)
    private String p2shRedeemScript;

    @Column(length = 1024)
    private String signTx;

    private Integer orderStatus;

    private Integer orderType;

    private String orderNo;

    private Date createTime;

    private Date finishTime;

    private Double amount;

    private Double money;

    private Integer currency;

    private Integer vcurrency;

    private Integer payment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress;
    }

    public String getP2shAddress() {
        return p2shAddress;
    }

    public void setP2shAddress(String p2shAddress) {
        this.p2shAddress = p2shAddress;
    }

    public String getP2shRedeemScript() {
        return p2shRedeemScript;
    }

    public void setP2shRedeemScript(String p2shRedeemScript) {
        this.p2shRedeemScript = p2shRedeemScript;
    }

    public String getSignTx() {
        return signTx;
    }

    public void setSignTx(String signTx) {
        this.signTx = signTx;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Integer getVcurrency() {
        return vcurrency;
    }

    public void setVcurrency(Integer vcurrency) {
        this.vcurrency = vcurrency;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }
}
