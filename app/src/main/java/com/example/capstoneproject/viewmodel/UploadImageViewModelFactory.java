package com.example.capstoneproject.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class UploadImageViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private Uri mFilePath;
    private FirebaseUser mUser;



    public UploadImageViewModelFactory(Application application, Uri filePath, FirebaseUser result) {
        mApplication = application;
        mFilePath = filePath;
        mUser=result;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UploadImageViewModel(mApplication,mFilePath,mUser);
    }
}