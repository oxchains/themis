package com.oxchains.chat.common;

/**
 * Created by xuqi on 2017/10/12.
 */
public class User {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String mobilephone;
    private String token;
    public User(Integer id,String username){
        this.id = id;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public static void main(String[] args) {
        String s = "123456789_9";
        System.out.println(s.substring(s.lastIndexOf("_")));
        System.out.println(s);
    }

}
