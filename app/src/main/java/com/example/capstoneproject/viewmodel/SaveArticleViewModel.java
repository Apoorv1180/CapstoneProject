package com.example.capstoneproject.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.capstoneproject.service.repository.DataRepository;


public class SaveArticleViewModel extends AndroidViewModel {

    DataRepository dataRepository;
    String mImageUrl, mArticleDescripton;

    public SaveArticleViewModel(Application mApplication, String mImageUrl, String mArticleDescripton) {
        super(mApplication);
        try {
            this.mImageUrl=mImageUrl;
            this.mArticleDescripton = mArticleDescripton;
            dataRepository = DataRepository.getInstance(mApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<Boolean> isArticleSaved() {
        return dataRepository.saveArticle(mImageUrl,mArticleDescripton);
    }
}
