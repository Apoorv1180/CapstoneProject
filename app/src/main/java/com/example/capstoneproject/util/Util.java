package com.example.capstoneproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Util {

    private static final String KEY_ROLE="Admin";



    public static String getRole(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(KEY_ROLE, "");
    }

    public static void setRole(Context context, String role) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    public static boolean checkUsername(String userName) {
        if (TextUtils.isEmpty(userName))
            return false;
        else
            return true;
    }
    public static boolean checkPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber))
            return false;
        else
            return true;
    }

    public static boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        } else
            return true;

    }

    public static String makePath() {
        return "ARTICLE_IMAGES/"+ UUID.randomUUID().toString();
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
