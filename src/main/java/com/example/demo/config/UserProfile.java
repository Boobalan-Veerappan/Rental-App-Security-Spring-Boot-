package com.example.demo.config;

public class UserProfile {
    private String userId;
    private String userName;
    private String passwd;
    private String role;

    public UserProfile(String userId, String userName, String passwd, String role) {
        this.userId = userId;
        this.userName = userName;
        this.passwd = passwd;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
