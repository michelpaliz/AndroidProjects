package com.example.caminoalba.Config;

public class Config {
    public final static String PORT_NUMBER  = ":9080/";
    public final static String CLIENT_API  = "http://192.168.9.127"+PORT_NUMBER;
    public final static String REGISTER_API  = CLIENT_API + "api/v1/user/register";
    public final static String LOGIN_API  =  CLIENT_API + "api/v1/user/login";


}
