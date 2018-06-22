package com.example.yasar.multirow.dummy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CityEvent implements Parcelable{

    public static final int SLIDER_TYPE = 0;
    public static final int RECTANGLE_TYPE = 1;

    public static final int BANNER_TYPE = 2;

//    public static final int EVENT_TYPESLIDE = 2;

    private String bannerimage;

    private String rectangleimage1, rectangleimage2;

    private int mType;

    private ArrayList<SliderModel> sliderimage;

    public CityEvent(){}

    public CityEvent(ArrayList<SliderModel> sliderimage, int type) {
        this.sliderimage = sliderimage;
        this.mType = type;
    }

    public CityEvent(String bannerimage, int type) {
        this.bannerimage = bannerimage;
        this.mType = type;
    }

    public CityEvent(String rectangleimage1, String rectangleimage2, int type) {
        this.rectangleimage1 = rectangleimage1;
        this.rectangleimage2 = rectangleimage2;
        this.mType = type;
    }

    protected CityEvent(Parcel in) {
        bannerimage = in.readString();
        rectangleimage1 = in.readString();
        rectangleimage2 = in.readString();
        mType = in.readInt();
    }

    public static final Creator<CityEvent> CREATOR = new Creator<CityEvent>() {
        @Override
        public CityEvent createFromParcel(Parcel in) {
            return new CityEvent(in);
        }

        @Override
        public CityEvent[] newArray(int size) {
            return new CityEvent[size];
        }
    };

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getBannerimage() {
        return bannerimage;
    }

    public void setBannerimage(String bannerimage) {
        this.bannerimage = bannerimage;
    }

    public String getRectangleimage1() {
        return rectangleimage1;
    }

    public void setRectangleimage1(String rectangleimage1) {
        this.rectangleimage1 = rectangleimage1;
    }

    public String getRectangleimage2() {
        return rectangleimage2;
    }

    public void setRectangleimage2(String rectangleimage2) {
        this.rectangleimage2 = rectangleimage2;
    }

    public ArrayList<SliderModel> getSliderimage() {
        return sliderimage;
    }

    public void setSliderimage(ArrayList<SliderModel> sliderimage) {
        this.sliderimage = sliderimage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bannerimage);
        dest.writeString(rectangleimage1);
        dest.writeString(rectangleimage2);
        dest.writeInt(mType);
    }
}
