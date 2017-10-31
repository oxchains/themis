package com.oxchains.themis.user.domain;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2017-10-20 17:33
 * @name OrderType
 * @desc:
 */
@Entity
@Table(name = "tbl_biz_orderstatus")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer statusValue;

    @Column(length = 32)
    private String statusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
