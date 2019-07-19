package com.example.capstoneproject.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class UserDetail implements Serializable,Parcelable {

    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    protected String phone;

    public UserDetail() {
    }

    public UserDetail(String userName, String phno) {
        name = userName;
        phone = phno;
    }

    protected UserDetail(Parcel in) {
        name=in.readString();
        phone=in.readString();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
    }
}
