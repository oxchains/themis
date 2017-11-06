package com.oxchains.themis.arbitrate.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oxchains.themis.arbitrate.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * create by huohuo
 * @author huohuo
 */
@Entity
public class UserToken {
    @Id
    private Long id;
    @Column(length = 512)
    private String token;
    @JsonFormat(pattern = "yyy-MM-dd hh:mm:ss") private Date createtime = new Date();
    @Transient
    private User user;
    @JsonIgnore
    private String username;
    public UserToken() {
    }

    public UserToken(User user, String token) {
        this.user = user;
        this.id = user.getId().longValue();
        this.token = token;
        this.username = user.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
