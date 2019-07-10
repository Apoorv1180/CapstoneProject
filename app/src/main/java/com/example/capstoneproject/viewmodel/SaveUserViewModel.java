package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;
import com.google.firebase.auth.FirebaseUser;


public class SaveUserViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String mUserId, mUserName,mPhoneNumber;

    public SaveUserViewModel(Application mApplication, String mUserId,String mUserName, String mPhoneNumber) {
        super(mApplication);
        try {
            this.mUserId=mUserId;
            this.mUserName = mUserName;
            this.mPhoneNumber =mPhoneNumber;
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isSavedStatus() {
        return dataRepository.saveUser(mUserId,mUserName,mPhoneNumber);
    }
}
