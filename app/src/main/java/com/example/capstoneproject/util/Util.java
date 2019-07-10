package com.example.capstoneproject.util;

import android.text.TextUtils;

public class Util {

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
