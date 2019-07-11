package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SaveUserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String mUserName,mPassword,mUserId;

    public SaveUserViewModelFactory(Application application,String mUserid, String userName, String password) {
        mApplication = application;
        mUserName = userName;
        mUserId = mUserid;
        mPassword = password;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SaveUserViewModel(mApplication,mUserId, mUserName,mPassword);
    }
}