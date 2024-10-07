package com.example.profile_api.dao;

import com.example.profile_api.model.User;

public class LoginToken {
    private String accessToken;
    private String refreshToken;
    private  User userInfo;


    public LoginToken() {
    }

    public LoginToken(String accessToken, String refreshToken, User userInfo) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userInfo = userInfo;
    }

    public LoginToken(String accessToken, User userInfo) {
        this.accessToken = accessToken;
        this.userInfo = userInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}
