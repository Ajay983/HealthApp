package com.linkitsoft.dategoal.Models;

import java.util.Date;

public class FriendsModel {

    public String _id;
    public int onlineStatus;
    public String name;
    public String Photo1;
    public String Photo2;
    public String Photo3;
    public String Photo4;
    public String Photo5;
    public String Photo6;
    public String city;
    public Date DOB;
    public int isRequested;
//    @SerializedName("geometry")
//    @Expose
    private Geometry geometry;


//    public double lat;
//    public double lng;


    public FriendsModel() {
    }

    public int getIsRequested() {
        return isRequested;
    }

    public void setIsRequested(int isRequested) {
        this.isRequested = isRequested;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public String getPhoto2() {
        return Photo2;
    }

    public void setPhoto2(String photo2) {
        Photo2 = photo2;
    }

    public String getPhoto3() {
        return Photo3;
    }

    public void setPhoto3(String photo3) {
        Photo3 = photo3;
    }

    public String getPhoto4() {
        return Photo4;
    }

    public void setPhoto4(String photo4) {
        Photo4 = photo4;
    }

    public String getPhoto5() {
        return Photo5;
    }

    public void setPhoto5(String photo5) {
        Photo5 = photo5;
    }

    public String getPhoto6() {
        return Photo6;
    }

    public void setPhoto6(String photo6) {
        Photo6 = photo6;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    //    public double getLat() {
//        return lat;
//    }
//
//    public void setLat(double lat) {
//        this.lat = lat;
//    }
//
//    public double getLng() {
//        return lng;
//    }
//
//    public void setLng(double lng) {
//        this.lng = lng;
//    }
}
