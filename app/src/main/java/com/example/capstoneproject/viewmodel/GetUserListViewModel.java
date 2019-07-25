package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.model.UserDetail;
import com.example.capstoneproject.service.repository.DataRepository;

import java.util.List;


public class GetUserListViewModel extends AndroidViewModel {


    DataRepository dataRepository;

    public GetUserListViewModel(@NonNull Application application) {
        super(application);
        dataRepository = DataRepository.getInstance(application);
    }

    public LiveData<List<UserDetail>> getUserData() {
        return dataRepository.getUserRecord();
    }

}
