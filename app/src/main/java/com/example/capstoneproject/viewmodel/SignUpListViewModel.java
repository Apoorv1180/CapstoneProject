package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;


public class SignUpListViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String mUserName,mPassword;

    public SignUpListViewModel(Application mApplication, String mUserName, String mPassword) {
        super(mApplication);
        dataRepository = DataRepository.getInstance(mApplication);
        try {
            this.mUserName = mUserName;
            this.mPassword =mPassword;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isRegisteredStatus() {
        return (LiveData<Boolean>)dataRepository.registerUser(mUserName,mPassword);
    }
}
