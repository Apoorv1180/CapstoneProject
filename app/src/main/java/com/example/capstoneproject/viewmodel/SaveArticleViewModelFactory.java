package com.example.capstoneproject.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SaveArticleViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String mImageUrl;
    private String mArticleDesc;


    public SaveArticleViewModelFactory(Application application, String mImageUrl, String mArticleDesc) {
        mApplication = application;
        this.mImageUrl = mImageUrl;
        this.mArticleDesc = mArticleDesc;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SaveArticleViewModel(mApplication, mImageUrl, mArticleDesc);
    }
}