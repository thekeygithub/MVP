package com.xczg.blockchain.yibaodapp.bean;

public class LoginUser {
    private String account;
    private String password;

    public LoginUser() {
    }

    public LoginUser(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
