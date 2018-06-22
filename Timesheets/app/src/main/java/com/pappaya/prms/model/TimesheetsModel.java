package com.pappaya.prms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasar on 25/11/16.
 */
public class TimesheetsModel implements Parcelable {

    private String name, startDate, endDate, hours, status, project;

    public TimesheetsModel(String name, String startDate, String endDate, String hours, String status, String project) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hours = hours;
        this.status = status;
        this.project = project;
    }

    public TimesheetsModel(String startDate, String endDate, String hours, String status, String project) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hours = hours;
        this.status = status;
        this.project = project;
    }

    protected TimesheetsModel(Parcel in) {
        name = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        hours = in.readString();
        status = in.readString();
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public static final Creator<TimesheetsModel> CREATOR = new Creator<TimesheetsModel>() {
        @Override
        public TimesheetsModel createFromParcel(Parcel in) {
            return new TimesheetsModel(in);
        }

        @Override
        public TimesheetsModel[] newArray(int size) {
            return new TimesheetsModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(hours);
        parcel.writeString(status);
    }
}
