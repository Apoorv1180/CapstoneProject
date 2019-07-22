package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SaveUserProgressViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String mWeight, selectedDate;

    public SaveUserProgressViewModelFactory(Application application, String mWeight, String selectedDate) {
        this.mApplication = application;
        this.mWeight = mWeight;
        this.selectedDate = selectedDate;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SaveUserProgressViewModel(mApplication, mWeight, selectedDate);
    }
}