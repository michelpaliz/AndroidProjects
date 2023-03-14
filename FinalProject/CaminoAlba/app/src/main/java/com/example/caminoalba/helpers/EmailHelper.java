package com.example.caminoalba.helpers;

import android.util.Patterns;

public class EmailHelper {

    public static boolean isValidEmail(CharSequence target) {
//        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
        return ( Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}
