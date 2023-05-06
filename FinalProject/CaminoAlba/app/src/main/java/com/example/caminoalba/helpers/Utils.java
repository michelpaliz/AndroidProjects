package com.example.caminoalba.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        LocalDate localDate = null;
        if (date.trim().equals("")) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
            try {
                localDate = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                System.out.println(date + " is an Invalid Date format. Please use format dd-MM-yyyy.");
            }
            return localDate;
        }
    }



    public static java.sql.Date convertDateToSQLDATE(Date date) {
        long milliseconds = date.getTime();
        return new java.sql.Date(milliseconds);
    }


}
