package com.oxchains.themis.order.entity;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Created by huohuo on 2017/10/25.
 */
@Entity
@Table(name = "order_arbitrate")
public class OrderArbitrate {
    @Id
    private Long id;
    private String orderId;
    private Long userId;
    private  String buyerAuth;
    private String sellerAuth;
    private Integer status;
    private String userAuth;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBuyerAuth() {
        return buyerAuth;
    }

    public void setBuyerAuth(String buyerAuth) {
        this.buyerAuth = buyerAuth;
    }

    public String getSellerAuth() {
        return sellerAuth;
    }

    public void setSellerAuth(String sellerAuth) {
        this.sellerAuth = sellerAuth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }
}
