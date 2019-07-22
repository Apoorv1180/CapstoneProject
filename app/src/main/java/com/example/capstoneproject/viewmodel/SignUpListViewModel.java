package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;
import com.google.firebase.auth.FirebaseUser;


public class SignUpListViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String mUserName, mPassword;

    public SignUpListViewModel(Application mApplication, String mUserName, String mPassword) {
        super(mApplication);
        try {
            this.mUserName = mUserName;
            this.mPassword = mPassword;
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<FirebaseUser> isRegisteredStatus() {
        return dataRepository.registerUser(mUserName, mPassword);
    }
}
