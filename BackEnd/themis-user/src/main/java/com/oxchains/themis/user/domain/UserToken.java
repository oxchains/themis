package com.oxchains.themis.user.domain;

/**
 * @Author ccl
 * @Time 2017-10-20 10:38
 * @Name UserToken
 * @Desc:
 */
public class UserToken {

    public UserToken(){}
    public UserToken(String username,String token){
        this.username=username;
        this.token=token;
    }

    String username;
    String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
