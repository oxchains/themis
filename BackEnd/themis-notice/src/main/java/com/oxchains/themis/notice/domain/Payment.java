package com.oxchains.themis.notice.domain;

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
    private Integer id;
    private String payment_name;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
