package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;


public class SaveUserProgressViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String mWeight,selectedDate;
    private LiveData<Boolean> status;

    public SaveUserProgressViewModel(Application mApplication,  String mWeight,String selectedDate) {
        super(mApplication);
        try {
            this.mWeight=mWeight;
            this.selectedDate=selectedDate;
            this.dataRepository = DataRepository.getInstance(mApplication);
        //    this.status=dataRepository.saveUserProgress(mWeight,selectedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   // public void isSavedProgressStatus() {
   //     return status;

}
