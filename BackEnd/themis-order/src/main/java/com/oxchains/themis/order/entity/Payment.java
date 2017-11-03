package com.oxchains.themis.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/23.
 * @author huohuo
 */
@Entity
@Table(name = "tbl_biz_payment")
public class Payment {
    @Id
    private Long id;
    private String paymentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
