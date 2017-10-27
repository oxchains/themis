package com.oxchains.themis.arbitrate.entity;

import javax.persistence.*;

/**
 * @Author ccl
 * @Time 2017-10-12 17:13
 * @Name User
 * @Desc:
 */

@Entity
@Table(name = "tbl_sys_user")
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


    private Integer loginStatus;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }
}
