package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignUpListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String mUserName, mPassword;

    public SignUpListViewModelFactory(Application application, String userName, String password) {
        mApplication = application;
        mUserName = userName;
        mPassword = password;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SignUpListViewModel(mApplication, mUserName, mPassword);
    }
}