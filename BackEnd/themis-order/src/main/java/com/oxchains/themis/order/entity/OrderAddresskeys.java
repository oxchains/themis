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
    private Long id;        //公私匙表唯一id
    private String orderId;  //相关联的订单id
    private String buyerPubAuth; //买家公匙
    private String buyerPriAuth;//买家私匙
    private String buyerSellerPriAuth; //买家拥有的买家的私匙
    private String sellerPubAuth; //卖家公匙
    private String sellerPriAuth; //卖家私匙
    private String sellerBuyerPriAuth; //卖家拥有的买家的私匙
    private String userPubAuth; //仲裁者公匙
    private String userPriAuth; //装菜社私匙

    public String getBuyerSellerPriAuth() {
        return buyerSellerPriAuth;
    }

    public void setBuyerSellerPriAuth(String buyerSellerPriAuth) {
        this.buyerSellerPriAuth = buyerSellerPriAuth;
    }

    public String getSellerBuyerPriAuth() {
        return sellerBuyerPriAuth;
    }

    public void setSellerBuyerPriAuth(String sellerBuyerPriAuth) {
        this.sellerBuyerPriAuth = sellerBuyerPriAuth;
    }

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
                ", buyerSellerPriAuth='" + buyerSellerPriAuth + '\'' +
                ", sellerPubAuth='" + sellerPubAuth + '\'' +
                ", sellerPriAuth='" + sellerPriAuth + '\'' +
                ", sellerBuyerPriAuth='" + sellerBuyerPriAuth + '\'' +
                ", userPubAuth='" + userPubAuth + '\'' +
                ", userPriAuth='" + userPriAuth + '\'' +
                '}';
    }
}
