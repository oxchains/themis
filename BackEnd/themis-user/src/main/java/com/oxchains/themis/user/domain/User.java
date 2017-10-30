package com.oxchains.themis.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author ccl
 * @Time 2017-10-12 17:13
 * @Name User
 * @Desc:
 */

@Entity
@Table(name = "tbl_sys_user")
public class User {

    public User(){}

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

    private Integer loginStatus;

    private Date createTime;

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

    //@JsonIgnore
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<String> authorities = new HashSet<>();
//
//    public Set<String> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(Set<String> authorities) {
//        this.authorities = authorities;
//    }

    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Transient
    private String token;

    @Transient
    private Role role;

    @Transient
    private UserTxDetail userTxDetail;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UserTxDetail getUserTxDetail() {
        return userTxDetail;
    }

    public void setUserTxDetail(UserTxDetail userTxDetail) {
        this.userTxDetail = userTxDetail;
    }

    public User(User user){
        //setAuthorities(user.getAuthorities());
        setRole(user.getRole());
        setRoleId(user.getRoleId());
        setEmail(user.getEmail());
        setLoginname(user.getLoginname());
        setUsername(user.getUsername());
        setFirstAddress(user.getFirstAddress());
        setId(user.getId());
        setMobilephone(user.getMobilephone());
        setLoginStatus(user.getLoginStatus());
        setCreateTime(user.getCreateTime());

        setUserTxDetail(user.getUserTxDetail());
    }
}
