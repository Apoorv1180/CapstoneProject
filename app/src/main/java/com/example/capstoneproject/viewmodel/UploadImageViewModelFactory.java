package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class UploadImageViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private byte[] mFilePath;
    private FirebaseUser mUser;
    private String mChildPath;


    public UploadImageViewModelFactory(Application application, byte[] filePath, FirebaseUser result, String childPath) {
        mApplication = application;
        mFilePath = filePath;
        mUser = result;
        mChildPath = childPath;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UploadImageViewModel(mApplication, mFilePath, mUser, mChildPath);
    }
}