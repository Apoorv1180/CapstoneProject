package com.example.capstoneproject.viewmodel;

import android.app.Application;

import com.example.capstoneproject.service.repository.DataRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SaveUserDetailViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String uname, joiningdate,renewdate,fees,planname,uid;

    public SaveUserDetailViewModel(Application mApplication, String uname, String joiningdate,String renewdate,String fees,String planname,String uid) {
        super(mApplication);
        try {
            this.uname=uname;
            this.joiningdate = joiningdate;
            this.renewdate = renewdate;
            this.fees = fees;
            this.planname = planname;
            this.uid=uid;
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isUserRecordSave() {
        return dataRepository.saveUserDetails(uname,joiningdate,renewdate,fees,planname,uid);
    }
}
