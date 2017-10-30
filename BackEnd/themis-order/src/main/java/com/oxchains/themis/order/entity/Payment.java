package com.oxchains.themis.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/23.
 */
@Entity
@Table(name = "tbl_biz_payment")
public class Payment {
    @Id
    private Long id;
    private String payment_name;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPayment_name() {
        return payment_name;
    }
    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }
    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", payment_name='" + payment_name + '\'' +
                '}';
    }
}
