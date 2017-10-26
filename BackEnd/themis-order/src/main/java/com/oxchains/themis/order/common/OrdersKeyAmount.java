package com.oxchains.themis.order.common;

import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/26.
 */
public class OrdersKeyAmount {
    private String orderId;
    private String[] pubKeys;
    private BigDecimal amount;

    public OrdersKeyAmount(String orderId, String[] pubKeys, BigDecimal amount) {
        this.orderId = orderId;
        this.pubKeys = pubKeys;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String[] getPubKeys() {
        return pubKeys;
    }

    public void setPubKeys(String[] pubKeys) {
        this.pubKeys = pubKeys;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
