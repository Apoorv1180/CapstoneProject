package com.example.capstoneproject.service.repository;


import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DataRepository {

    private static DataRepository dataRepository;
    private static Context context;
    private FirebaseAuth auth;

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

    public LiveData<FirebaseUser> registerUser(String mUserName, String mPassword) {
        final MutableLiveData<FirebaseUser> userValues  = new MutableLiveData<>();
        auth.createUserWithEmailAndPassword(mUserName, mPassword)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            userValues.setValue(null);
                        } else {
                            if(auth.getCurrentUser()!=null)
                            {
                                userValues.setValue(auth.getCurrentUser());
                            }
                            else
                                userValues.setValue(null);
                        }
                    }
                });
        return userValues;
    }

    public LiveData<FirebaseUser> signInUser(String mUserName, String mPassword) {
        final MutableLiveData<FirebaseUser> userValuesSignIn  = new MutableLiveData<>();
        auth.signInWithEmailAndPassword(mUserName, mPassword)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            userValuesSignIn.setValue(null);
                        } else {
                            if(auth.getCurrentUser()!=null)
                            {
                                userValuesSignIn.setValue(auth.getCurrentUser());
                            }
                            else
                                userValuesSignIn.setValue(null);
                        }
                    }
                });
        return userValuesSignIn;
    }

    public LiveData<FirebaseUser> checkIfUserIsLoggedIn(){
        final MutableLiveData<FirebaseUser> userValues  = new MutableLiveData<>();

        if(auth.getCurrentUser()!=null)
        {
            userValues.setValue(auth.getCurrentUser());
        }
        else
            userValues.setValue(null);

        return userValues;
    }
}
