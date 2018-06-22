package com.pappaya.prms.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasar on 18/12/16.
 */
public class CreateOrUpdateModel implements Parcelable {

    private Projects projects;

    private AddDates addDates;

    public CreateOrUpdateModel(Projects projects, AddDates addDates) {
        this.projects = projects;
        this.addDates = addDates;
    }

    protected CreateOrUpdateModel(Parcel in) {
        projects = in.readParcelable(Projects.class.getClassLoader());
        addDates = in.readParcelable(AddDates.class.getClassLoader());
    }

    public static final Creator<CreateOrUpdateModel> CREATOR = new Creator<CreateOrUpdateModel>() {
        @Override
        public CreateOrUpdateModel createFromParcel(Parcel in) {
            return new CreateOrUpdateModel(in);
        }

        @Override
        public CreateOrUpdateModel[] newArray(int size) {
            return new CreateOrUpdateModel[size];
        }
    };

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public AddDates getAddDates() {
        return addDates;
    }

    public void setAddDates(AddDates addDates) {
        this.addDates = addDates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(projects, i);
        parcel.writeParcelable(addDates, i);
    }
}
