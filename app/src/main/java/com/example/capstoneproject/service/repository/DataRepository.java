package com.example.capstoneproject.service.repository;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DataRepository {

    private static DataRepository dataRepository;
    private static Context context;
    private FirebaseAuth auth;
    private boolean status;

    public DataRepository(Application application) {
        auth = FirebaseAuth.getInstance();
    }

    public synchronized static DataRepository getInstance(Application application) {
        if (dataRepository == null) {
            if (dataRepository == null) {
                dataRepository = new DataRepository(application);
                context = application.getApplicationContext();
            }
        }
        return dataRepository;
    }

    public boolean registerUser(String mUserName, String mPassword) {
        auth.createUserWithEmailAndPassword(mUserName, mPassword)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            setStatusValueTrue();
                        } else {
                            setStatusValueFalse();
                        }
                    }
                });
        return status;
    }

    private void setStatusValueFalse() {
        status = false;
    }

    private void setStatusValueTrue() {
        status = true;
    }
}
