package com.example.caminoalba.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean checkDigits(String text) {
        boolean containsOnlyDigits = false;
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) { // in case that a char is NOT a digit, enter to the if code block
                containsOnlyDigits = true;
                break; // break the loop, since we found that this char is not a digit
            }
        }
        return containsOnlyDigits;
    }


    public static Date validateDate(String strDate) {
        Date javaDate;
        if (strDate.trim().equals("")) {
            return null;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            try {
                javaDate = simpleDateFormat.parse(strDate);
                System.out.println(strDate + " is valid date format");
            } catch (ParseException e) {
                System.out.println(strDate + " is Invalid Date format");
                return null;
            }
            /* Return true if date format is valid */
            return javaDate;
        }
    }

}
