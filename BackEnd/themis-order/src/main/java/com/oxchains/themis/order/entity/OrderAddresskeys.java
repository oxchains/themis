package com.oxchains.themis.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/25.
 */
@Entity
@Table(name = "order_address_key")
public class OrderAddresskeys {
    @Id
    private Long id;
    private String orderId;
    private String buyerPubAuth;
    private String buyerPriAuth;
    private String sellerPubAuth;
    private String sellerPriAuth;
    private String userPubAuth;
    private String userPriAuth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyerPubAuth() {
        return buyerPubAuth;
    }

    public void setBuyerPubAuth(String buyerPubAuth) {
        this.buyerPubAuth = buyerPubAuth;
    }

    public String getBuyerPriAuth() {
        return buyerPriAuth;
    }

    public void setBuyerPriAuth(String buyerPriAuth) {
        this.buyerPriAuth = buyerPriAuth;
    }

    public String getSellerPubAuth() {
        return sellerPubAuth;
    }

    public void setSellerPubAuth(String sellerPubAuth) {
        this.sellerPubAuth = sellerPubAuth;
    }

    public String getSellerPriAuth() {
        return sellerPriAuth;
    }

    public void setSellerPriAuth(String sellerPriAuth) {
        this.sellerPriAuth = sellerPriAuth;
    }

    public String getUserPubAuth() {
        return userPubAuth;
    }

    public void setUserPubAuth(String userPubAuth) {
        this.userPubAuth = userPubAuth;
    }

    public String getUserPriAuth() {
        return userPriAuth;
    }

    public void setUserPriAuth(String userPriAuth) {
        this.userPriAuth = userPriAuth;
    }

    @Override
    public String toString() {
        return "OrderAddresskeys{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", buyerPubAuth='" + buyerPubAuth + '\'' +
                ", buyerPriAuth='" + buyerPriAuth + '\'' +
                ", sellerPubAuth='" + sellerPubAuth + '\'' +
                ", sellerPriAuth='" + sellerPriAuth + '\'' +
                ", userPubAuth='" + userPubAuth + '\'' +
                ", userPriAuth='" + userPriAuth + '\'' +
                '}';
    }
}
