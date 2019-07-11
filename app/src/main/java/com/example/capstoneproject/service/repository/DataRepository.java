package com.example.capstoneproject.service.repository;


import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DataRepository {

    private static DataRepository dataRepository;
    private static Context context;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    public DataRepository(Application application) {
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public synchronized static DataRepository getInstance(Application application) {
        if (dataRepository == null) {
                dataRepository = new DataRepository(application);
                context = application.getApplicationContext();
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

    public LiveData<Boolean> saveUser(String userId, String mUserName, String mPhoneNumber) {
        final MutableLiveData<Boolean> status  = new MutableLiveData<>();
        String userIdChild = userId;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child(userIdChild);
        Map newUser = new HashMap();
        newUser.put("name",mUserName);
        newUser.put("phone",mPhoneNumber);
        mDatabase.setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError!=null){
                    status.setValue(Boolean.FALSE);
                }else
                    status.setValue(Boolean.TRUE);
            }
        });
    return  status;
    }
}
