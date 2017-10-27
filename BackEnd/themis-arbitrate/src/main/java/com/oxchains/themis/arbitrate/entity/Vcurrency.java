package com.oxchains.themis.arbitrate.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huohuo on 2017/10/23.
 */
@Entity
@Table(name = "tbl_biz_vcurrency")
public class Vcurrency {
    @Id
    private Long id;
    private String vcurrency_name;
    private String vcurrency_short;

    public String getVcurrency_name() {
        return vcurrency_name;
    }

    public void setVcurrency_name(String vcurrency_name) {
        this.vcurrency_name = vcurrency_name;
    }

    public String getVcurrency_short() {
        return vcurrency_short;
    }

    public void setVcurrency_short(String vcurrency_short) {
        this.vcurrency_short = vcurrency_short;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Vcurrency{" +
                "id=" + id +
                ", vcurrency_name='" + vcurrency_name + '\'' +
                ", vcurrency_short='" + vcurrency_short + '\'' +
                '}';
    }
}
