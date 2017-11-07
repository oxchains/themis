package com.oxchains.themis.order.entity;
import javax.persistence.*;

/**
 * Created by huohuo on 2017/10/28.
 * @author huohuo
 */
@Entity
@Table(name = "order_comment")
public class OrderComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private Integer sellerStatus;
    private Integer buyerStatus;
    private String buyerContent;
    private String sellerContent;
    @Transient
    private Long userId;
    @Transient
    private String content;
    @Transient
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderComment{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", sellerStatus=" + sellerStatus +
                ", buyerStatus=" + buyerStatus +
                ", buyerContent='" + buyerContent + '\'' +
                ", sellerContent='" + sellerContent + '\'' +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getSellerStatus() {
        return sellerStatus;
    }

    public void setSellerStatus(Integer sellerStatus) {
        this.sellerStatus = sellerStatus;
    }

    public Integer getBuyerStatus() {
        return buyerStatus;
    }

    public void setBuyerStatus(Integer buyerStatus) {
        this.buyerStatus = buyerStatus;
    }

    public String getBuyerContent() {
        return buyerContent;
    }

    public void setBuyerContent(String buyerContent) {
        this.buyerContent = buyerContent;
    }

    public String getSellerContent() {
        return sellerContent;
    }

    public void setSellerContent(String sellerContent) {
        this.sellerContent = sellerContent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
