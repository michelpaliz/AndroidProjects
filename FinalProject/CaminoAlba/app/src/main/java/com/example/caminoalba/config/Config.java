package com.example.caminoalba.config;

public class Config {
    public final static String PORT_NUMBER  = ":9080/";
    public final static String CLIENT_API  = "http://192.168.9.127"+PORT_NUMBER;
    public final static String REGISTER_API  = CLIENT_API + "user/register/";
    public final static String LOGIN_API  =  CLIENT_API + "api/v1/user/login";
    public static final String CHECK_ACCOUNT_API = CLIENT_API+ "api/v1/{userId}/";
    public static boolean USER_SAVED = false;


}
