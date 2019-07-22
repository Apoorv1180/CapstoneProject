package com.example.capstoneproject.viewmodel;

import android.app.Application;

import com.example.capstoneproject.service.model.PlanDetail;
import com.example.capstoneproject.service.repository.DataRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

// Created by ekta on 22/7/19.
public class GetUserPlanViewModel extends AndroidViewModel {


    DataRepository dataRepository;

    public GetUserPlanViewModel(@NonNull Application application) {
        super(application);
        dataRepository = DataRepository.getInstance(application);
    }

    public LiveData<List<PlanDetail>> getUserPlanData()
    {
        return dataRepository.getPlanDetail();
    }
}
