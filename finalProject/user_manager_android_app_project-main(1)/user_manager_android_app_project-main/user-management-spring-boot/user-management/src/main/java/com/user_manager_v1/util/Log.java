package com.user_manager_v1.util;


import com.user_manager_v1.UserManagementApplication;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    public static void e(String tag, String msg) {
        Logger.getLogger(tag).log(Level.SEVERE, "["+tag+"] " + msg);
    }

    public static void i(String tag, String msg) {
        Logger.getLogger(tag).log(Level.INFO, "["+tag+"] " + msg);
    }

    public static void d(String tag, String msg) {
        if(UserManagementApplication.DEBUG) {
            Logger.getLogger(tag).log(Level.INFO, "DEBUG ["+tag+"] " + msg);
        }
    }

    public static void w(String tag, String msg) {
        Logger.getLogger(tag).log(Level.WARNING, "["+tag+"] " + msg);
    }
}

