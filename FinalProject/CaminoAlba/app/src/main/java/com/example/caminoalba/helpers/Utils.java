package com.example.caminoalba.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


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

    public static String generateVerificationCode() {
        int count = 6; // number of characters in the verification code
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    public static LocalDate validateDate(String date) {
        Date javaDate = null;
        LocalDate localDate = null;
        if (date.trim().equals("")) {
            return null;
        } else {
//            ZonedDateTime dateTime = ZonedDateTime.parse("yyyy-MM-dd");
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            inputFormat.setLenient(false);
            try {
                javaDate = inputFormat.parse(date);
                assert javaDate != null;
                localDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(javaDate));

                System.out.println(javaDate + " is valid date format");
            } catch (ParseException e) {
                System.out.println(date + " is Invalid Date format");
            }
            /* Return true if date format is valid */
            return localDate;

        }
    }

    public static java.sql.Date convertDateToSQLDATE(Date date) {
        long milliseconds = date.getTime();
        return new java.sql.Date(milliseconds);
    }


}
