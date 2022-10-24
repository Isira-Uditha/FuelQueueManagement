package com.example.fuelquemanagement_client.database;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The InputValidatorHelper class facilitates validations of input fields
 */
public class InputValidatorHelper {

    //Validates a email
    public boolean isValidGuestEmail(String string){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    //Validates empty text fields
    public boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }

    //Validates a length of a number
    public boolean isCheckContact(String string){
        if(string.length() != 10){
            return true;
        }
        return false;
    }

    //Validates a length of a string
    public boolean isCheckText(String string){
        if(string.length()<5){
            return true;
        }
        return false;
    }

    public boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }
}
