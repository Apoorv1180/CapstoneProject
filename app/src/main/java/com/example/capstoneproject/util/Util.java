package com.example.capstoneproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

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
}
