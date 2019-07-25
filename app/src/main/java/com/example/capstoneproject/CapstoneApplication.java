package com.example.capstoneproject;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;


public class CapstoneApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
