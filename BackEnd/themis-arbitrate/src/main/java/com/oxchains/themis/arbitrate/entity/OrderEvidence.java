package com.oxchains.themis.arbitrate.entity;

import javax.persistence.*;

/**
 * Created by huohuo on 2017/10/31.
 * @author huohuo
 */
@Entity
@Table(name = "order_arbitrate_upload_evidence")
public class OrderEvidence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private String buyerContent;
    private String buyerFiles;
    private String sellerContent;
    private String sellerFiles;

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

    public String getBuyerContent() {
        return buyerContent;
    }

    public void setBuyerContent(String buyerContent) {
        this.buyerContent = buyerContent;
    }

    public String getBuyerFiles() {
        return buyerFiles;
    }

    public void setBuyerFiles(String buyerFiles) {
        this.buyerFiles = buyerFiles;
    }

    public String getSellerContent() {
        return sellerContent;
    }

    public void setSellerContent(String sellerContent) {
        this.sellerContent = sellerContent;
    }

    public String getSellerFiles() {
        return sellerFiles;
    }

    public void setSellerFiles(String sellerFiles) {
        this.sellerFiles = sellerFiles;
    }
}
