package com.example.capstoneproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final String KEY_ROLE = "Admin";
    private static final String KEY_CREDENTIAL = "UserName";


    public static String getRole(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(KEY_ROLE, "");
    }

    public static void setRole(Context context, String role) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    public static String getCredential(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(KEY_CREDENTIAL, "");
    }

    public static void setCredential(Context context, String credential) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(KEY_CREDENTIAL, credential);
        editor.commit();
    }

    public static boolean isValidUsername(String userName) {
        return (!isEmptyText(userName) && Patterns.EMAIL_ADDRESS.matcher(userName).matches());
    }

    public static boolean isEmptyText(String userName) {
        if (TextUtils.isEmpty(userName))
            return true;
        else
            return false;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 10);
    }

    public static boolean isValidPassword(String password) {
        Matcher matcher = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,15})").matcher(password);
        return (!isEmptyText(password) && matcher.matches());
    }

    public static String getTodayDateInString() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    public static String makePath() {
        return "ARTICLE_IMAGES/" + UUID.randomUUID().toString();
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

    public static void displaySnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
