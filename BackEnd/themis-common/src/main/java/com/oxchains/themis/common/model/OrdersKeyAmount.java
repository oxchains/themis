package com.oxchains.themis.common.model;

/**
 * Created by huohuo on 2017/10/26.
 */
public class OrdersKeyAmount {
    private String orderId;
    private String pubKeys;
    private Double amount;

    public OrdersKeyAmount() {
    }

    public OrdersKeyAmount(String orderId, String pubKeys, Double amount) {
        this.orderId = orderId;
        this.pubKeys = pubKeys;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrdersKeyAmount{" +
                "orderId='" + orderId + '\'' +
                ", pubKeys='" + pubKeys + '\'' +
                ", amount=" + amount +
                '}';
    }

    public String getPubKeys() {
        return pubKeys;
    }

    public void setPubKeys(String pubKeys) {
        this.pubKeys = pubKeys;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}