package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.capstoneproject.service.repository.DataRepository;


public class SignUpListViewModel extends AndroidViewModel {

    private boolean isRegisteredStatus;
    DataRepository dataRepository;

    public SignUpListViewModel(Application mApplication, String mUserName, String mPassword) {
        super(mApplication);
        dataRepository = DataRepository.getInstance(mApplication);
        try {
            isRegisteredStatus = DataRepository.getInstance(mApplication).registerUser(mUserName,mPassword);
            setRegisteredStatus(isRegisteredStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRegisteredStatus() {
        return isRegisteredStatus;
    }

    public void setRegisteredStatus(boolean registeredStatus) {
        isRegisteredStatus = registeredStatus;
    }
}
