package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;


public class SaveUserProgressViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String mWeight,selectedDate;

    public SaveUserProgressViewModel(Application mApplication,  String mWeight,String selectedDate) {
        super(mApplication);
        try {
            this.mWeight=mWeight;
            this.selectedDate=selectedDate;
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isSavedProgressStatus() {
        return dataRepository.saveUserProgress(mWeight,selectedDate);
    }
}
