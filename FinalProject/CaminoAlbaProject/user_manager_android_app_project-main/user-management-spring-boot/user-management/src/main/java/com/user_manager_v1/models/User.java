package com.user_manager_v1.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "type")
    private String type;

    @Column(name = "verification_code")
    private String verificationCode;
    private boolean enabled;

    private AccountStatus accountStatus;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private Profile profile;

    public User(String uid, String first_name, String last_name, String email, String type, String verificationCode, boolean enabled, AccountStatus accountStatus) {
        this.user_id = Integer.parseInt(String.valueOf(uid));
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.type = type;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
        this.accountStatus = accountStatus;
    }


    public User(int user_id, String first_name, String last_name, String email, String password, String type, String verificationCode, boolean enabled, AccountStatus accountStatus, Profile profile) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
        this.accountStatus = accountStatus;
        this.profile = profile;
    }

    public User() {

    }



    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String isType() {
        return type;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Profile getPerson() {
        return profile;
    }

    public void setPerson(Profile profile) {
        this.profile = profile;
    }
}
