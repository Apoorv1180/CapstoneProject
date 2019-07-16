package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.model.Article;
import com.example.capstoneproject.service.repository.DataRepository;

import java.util.List;

// Created by akshata on 15/7/19.
public class GetArticleListViewModel extends AndroidViewModel {

    DataRepository dataRepository;

    public GetArticleListViewModel(@NonNull Application application) {
        super(application);
        dataRepository = DataRepository.getInstance(application);
    }

    public LiveData<List<Article>> getArticleList()
    {
        return dataRepository.getArticles();
    }
}
