package com.oxchains.themis.order.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author luoxuri
 * @create 2017-10-24 10:43
 **/
@Entity
@Table(name = "notice_type")
@Data
public class NoticeType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "NoticeType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
