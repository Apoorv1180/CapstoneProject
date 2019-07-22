package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;
import com.google.firebase.auth.FirebaseUser;


public class LogoutViewModel extends AndroidViewModel {

    DataRepository dataRepository;

    public LogoutViewModel(Application mApplication) {
        super(mApplication);
        try {
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isLoggedOutStatus() {
        return (LiveData<Boolean>) dataRepository.logout();
    }
}
