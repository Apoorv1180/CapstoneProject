package com.example.capstoneproject.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class SaveImageUrlViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String mChildPath;
    private Uri mFilePath;


    public SaveImageUrlViewModelFactory(Application application, String childPath, Uri filePath) {
        mApplication = application;
        mChildPath = childPath;
        mFilePath = filePath;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SaveImageUrlViewModel(mApplication, mChildPath, mFilePath);
    }
}