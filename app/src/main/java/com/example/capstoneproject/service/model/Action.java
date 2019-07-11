package com.example.capstoneproject.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

// Created by apoorv on 11/7/19.
public class Action implements Parcelable, Serializable {

    private String actionName;
    private String actionDescription;
    private int actionPhotoId;

    public Action(String actionName, String actionDescription, int actionPhotoId) {
        this.actionName = actionName;
        this.actionDescription = actionDescription;
        this.actionPhotoId = actionPhotoId;
    }

    protected Action(Parcel in) {
        actionName = in.readString();
        actionDescription = in.readString();
        actionPhotoId = in.readInt();
    }

    public static final Creator<Action> CREATOR = new Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public int getActionPhotoId() {
        return actionPhotoId;
    }

    public void setActionPhotoId(int actionPhotoId) {
        this.actionPhotoId = actionPhotoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(actionName);
        parcel.writeString(actionDescription);
        parcel.writeInt(actionPhotoId);
    }
}
