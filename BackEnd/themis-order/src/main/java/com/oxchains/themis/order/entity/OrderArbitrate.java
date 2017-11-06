package com.oxchains.themis.order.entity;
import javax.persistence.*;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Entity
@Table(name = "order_arbitrate")
public class OrderArbitrate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private Long userId;
    private  String buyerAuth;
    private String sellerAuth;
    private Integer status;
    private String userAuth;

    public OrderArbitrate() {
    }

    public OrderArbitrate(String orderId, Long userId, Integer status, String userAuth) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.userAuth = userAuth;
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
