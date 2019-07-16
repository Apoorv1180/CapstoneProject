package com.example.capstoneproject;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

//created by ekta 16/07/19.

public class CapstoneApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
