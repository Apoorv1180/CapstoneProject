package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;
import com.google.firebase.auth.FirebaseUser;


public class UploadImageViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    byte[] mFilePath;
    FirebaseUser mUser;
    String mChildPath;

    public UploadImageViewModel(Application mApplication, byte[] mFilePath, FirebaseUser mUser, String childPath) {
        super(mApplication);
        try {
            this.mFilePath = mFilePath;
            this.mUser = mUser;
            this.mChildPath = childPath;
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isImageUploaded() {
        return dataRepository.saveImage(mFilePath, mUser, mChildPath);
    }
}
