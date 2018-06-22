package com.example.yasar.multirow.dummy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yasar on 6/10/16.
 */
public class SliderModel implements Parcelable {
    private String firstimage, secondimage, thirdimage, fourthimage, fifthimage, thumpimage, sliderImage;


    private String businessName, productDescription, startDate, endDate, shopName, Address, place, state, pincode, city, contactNumber, newitem, websitelink;

    public SliderModel(String firstimage, String secondimage, String thirdimage, String fourthimage, String fifthimage, String businessName, String productDescription, String startDate, String endDate, String shopName, String address, String place, String state, String pincode, String city, String contactNumber, String newitem, String thumpimage, String sliderImage, String websitelink) {
        this.firstimage = firstimage;
        this.secondimage = secondimage;
        this.thirdimage = thirdimage;
        this.fourthimage = fourthimage;
        this.fifthimage = fifthimage;
        this.businessName = businessName;
        this.productDescription = productDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shopName = shopName;
        Address = address;
        this.place = place;
        this.state = state;
        this.pincode = pincode;
        this.city = city;
        this.contactNumber = contactNumber;
        this.newitem = newitem;
        this.thumpimage = thumpimage;
        this.sliderImage = sliderImage;
        this.websitelink = websitelink;
    }

    public SliderModel() {
    }

    public String getWebsitelink() {
        return websitelink;
    }

    public void setWebsitelink(String websitelink) {
        this.websitelink = websitelink;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public String getThumpimage() {
        return thumpimage;
    }

    public void setThumpimage(String thumpimage) {
        this.thumpimage = thumpimage;
    }

    public String getNewitem() {
        return newitem;
    }

    public void setNewitem(String newitem) {
        this.newitem = newitem;
    }

    public SliderModel(String firstimage, String secondimage, String thirdimage, String fourthimage, String fifthimage, String newitem, String thumpimage, String sliderImage, String websitelink) {
        this.firstimage = firstimage;
        this.secondimage = secondimage;
        this.thirdimage = thirdimage;
        this.fourthimage = fourthimage;
        this.fifthimage = fifthimage;
        this.newitem = newitem;
        this.thumpimage = thumpimage;
        this.sliderImage = sliderImage;
        this.websitelink = websitelink;
    }

    protected SliderModel(Parcel in) {
        firstimage = in.readString();
        secondimage = in.readString();
        thirdimage = in.readString();
        fourthimage = in.readString();
        fifthimage = in.readString();
        businessName = in.readString();
        productDescription = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        shopName = in.readString();
        Address = in.readString();
        place = in.readString();
        state = in.readString();
        pincode = in.readString();
        city = in.readString();
        contactNumber = in.readString();
        newitem = in.readString();
        thumpimage = in.readString();
        sliderImage = in.readString();
        websitelink = in.readString();
    }

    public static final Creator<SliderModel> CREATOR = new Creator<SliderModel>() {
        @Override
        public SliderModel createFromParcel(Parcel in) {
            return new SliderModel(in);
        }

        @Override
        public SliderModel[] newArray(int size) {
            return new SliderModel[size];
        }
    };

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getSecondimage() {
        return secondimage;
    }

    public void setSecondimage(String secondimage) {
        this.secondimage = secondimage;
    }

    public String getThirdimage() {
        return thirdimage;
    }

    public void setThirdimage(String thirdimage) {
        this.thirdimage = thirdimage;
    }

    public String getFourthimage() {
        return fourthimage;
    }

    public void setFourthimage(String fourthimage) {
        this.fourthimage = fourthimage;
    }

    public String getFifthimage() {
        return fifthimage;
    }

    public void setFifthimage(String fifthimage) {
        this.fifthimage = fifthimage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstimage);
        dest.writeString(secondimage);
        dest.writeString(thirdimage);
        dest.writeString(fourthimage);
        dest.writeString(fifthimage);
        dest.writeString(businessName);
        dest.writeString(productDescription);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(shopName);
        dest.writeString(Address);
        dest.writeString(place);
        dest.writeString(state);
        dest.writeString(pincode);
        dest.writeString(city);
        dest.writeString(contactNumber);
        dest.writeString(newitem);
        dest.writeString(thumpimage);
        dest.writeString(sliderImage);
        dest.writeString(websitelink);
    }
}
