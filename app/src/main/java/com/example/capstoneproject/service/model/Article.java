package com.example.capstoneproject.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

// Created by apoorv on 12/7/19.
public class Article implements Serializable,Parcelable{

    int imageId;
    String imageUrl;
    String articleDescription;

    public Article() {
    }

    public Article(String imageUrl, String articleDescription) {
        this.imageUrl = imageUrl;
        this.articleDescription = articleDescription;
    }

    public Article(int imageId, String articleDescription) {
        this.imageId = imageId;
        this.articleDescription = articleDescription;
    }

    protected Article(Parcel in) {
        imageId = in.readInt();
        imageUrl = in.readString();
        articleDescription = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageId);
        parcel.writeString(imageUrl);
        parcel.writeString(articleDescription);
    }
}
