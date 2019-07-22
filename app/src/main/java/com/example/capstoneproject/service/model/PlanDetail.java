package com.example.capstoneproject.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PlanDetail implements Serializable,Parcelable{
    String uname;
    String planname;
    String renewdate;
    String joiningdate;
    String fees;

    public PlanDetail(String uname, String planname, String renewdate, String joiningdate, String fees) {
        this.uname = uname;
        this.planname = planname;
        this.renewdate = renewdate;
        this.joiningdate = joiningdate;
        this.fees = fees;
    }

    public PlanDetail() {
    }

    protected PlanDetail(Parcel in) {
        uname = in.readString();
        planname = in.readString();
        renewdate = in.readString();
        joiningdate = in.readString();
        fees = in.readString();
    }

    public static final Creator<PlanDetail> CREATOR = new Creator<PlanDetail>() {
        @Override
        public PlanDetail createFromParcel(Parcel in) {
            return new PlanDetail(in);
        }

        @Override
        public PlanDetail[] newArray(int size) {
            return new PlanDetail[size];
        }
    };

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getRenewdate() {
        return renewdate;
    }

    public void setRenewdate(String renewdate) {
        this.renewdate = renewdate;
    }

    public String getJoiningdate() {
        return joiningdate;
    }

    public void setJoiningdate(String joiningdate) {
        this.joiningdate = joiningdate;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uname);
        parcel.writeString(planname);
        parcel.writeString(renewdate);
        parcel.writeString(joiningdate);
        parcel.writeString(fees);
    }
}
