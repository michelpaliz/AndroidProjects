package com.example.caminoalba.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {

    private String user_id;
    private String email;
    private String password;
    private String type;
    private String verificationCode;
    private Boolean enabled;
    private AccountStatus accountStatus;


    public User() {
    }


    public User(String user_id, String email, String password, String type, String verificationCode, Boolean enabled, AccountStatus accountStatus) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.type = type;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
        this.accountStatus = accountStatus;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", enabled=" + enabled +
                ", accountStatus=" + accountStatus +
                '}';
    }
}
