package com.example.capstoneproject.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;


public class SaveImageUrlViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String mChildPath;
    Uri mFilePath;

    public SaveImageUrlViewModel(Application mApplication, String mChildPath, Uri mFilePath) {
        super(mApplication);
        try {
            this.mChildPath = mChildPath;
            dataRepository = DataRepository.getInstance(mApplication);
            this.mFilePath = mFilePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Uri> isImageUrlSaved() {
        return dataRepository.saveImageUrl(mChildPath, mFilePath);
    }
}
