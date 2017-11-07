package com.oxchains.themis.order.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author huohuo
 */

@Entity
@Table(name = "tbl_sys_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 32)
    private String username;

    @Column(length = 32)
    private String loginname;

    @Column(length = 32)
    private String email;

    @Column(length = 11)
    private String mobilephone;

    //@JsonIgnore
    @Column(length = 64)
    private String password;

    @Column(length = 35)
    private String firstAddress;

    private Long roleId;

    private String createTime;
}
