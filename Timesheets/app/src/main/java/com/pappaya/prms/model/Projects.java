package com.pappaya.prms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasar on 25/11/16.
 */
public class Projects implements Parcelable {
    private String projectName, billablenonbillable, projectid, sheetid;

    public Projects(String projectName, String billablenonbillable) {
        this.projectName = projectName;
        this.billablenonbillable = billablenonbillable;
//        this.projectid = projectid;
//        this.sheetid = sheetid;
    }

    public Projects(String projectName, String billablenonbillable, String projectid, String sheetid) {
        this.projectName = projectName;
        this.billablenonbillable = billablenonbillable;
        this.projectid = projectid;
        this.sheetid = sheetid;
    }

    public String getSheetid() {
        return sheetid;
    }

    public void setSheetid(String sheetid) {
        this.sheetid = sheetid;
    }

    protected Projects(Parcel in) {
        projectName = in.readString();
        billablenonbillable = in.readString();
        projectid = in.readString();
        sheetid = in.readString();
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public static final Creator<Projects> CREATOR = new Creator<Projects>() {
        @Override
        public Projects createFromParcel(Parcel in) {
            return new Projects(in);
        }

        @Override
        public Projects[] newArray(int size) {
            return new Projects[size];
        }
    };

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return billablenonbillable;
    }

    public void setDescription(String billablenonbillable) {
        this.billablenonbillable = billablenonbillable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(projectName);
        parcel.writeString(billablenonbillable);
        parcel.writeString(projectid);
        parcel.writeString(sheetid);
    }

    @Override
    public String toString() {
//        return "{" +
//                "projectName='" + projectName + '\'' +
//                ", billablenonbillable='" + billablenonbillable + '\'' +
//                ", projectid='" + projectid + '\'' +
//                ", sheetid='" + sheetid + '\'' +
//                '}';

        return "";
    }
}
