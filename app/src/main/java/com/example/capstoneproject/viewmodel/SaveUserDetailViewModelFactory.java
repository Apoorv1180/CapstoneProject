package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SaveUserDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private String uname, joiningdate, renewdate, fees, planname, uid;


    public SaveUserDetailViewModelFactory(Application application, String uname, String joiningdate, String renewdate, String fees, String planname, String uid) {
        mApplication = application;
        this.uname = uname;
        this.joiningdate = joiningdate;
        this.renewdate = renewdate;
        this.fees = fees;
        this.planname = planname;
        this.uid = uid;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SaveUserDetailViewModel(mApplication, uname, joiningdate, renewdate, fees, planname, uid);
    }
}
