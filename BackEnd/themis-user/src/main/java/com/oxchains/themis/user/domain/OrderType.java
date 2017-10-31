package com.oxchains.themis.user.domain;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2017-10-20 17:33
 * @name OrderType
 * @desc:
 */
@Entity
@Table(name = "tbl_biz_ordertype")
public class OrderType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer typeValue;

    @Column(length = 32)
    private String typeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(Integer typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
