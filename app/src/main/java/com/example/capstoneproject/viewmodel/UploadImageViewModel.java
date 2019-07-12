package com.example.capstoneproject.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;
import com.google.firebase.auth.FirebaseUser;


public class UploadImageViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    Uri mFilePath;
    FirebaseUser mUser;

    public UploadImageViewModel(Application mApplication, Uri mFilePath, FirebaseUser mUser) {
        super(mApplication);
        try {
            this.mFilePath = mFilePath;
            this.mUser = mUser;
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isImageUploaded() {
        return dataRepository.saveImage(mFilePath,mUser);
    }
}
