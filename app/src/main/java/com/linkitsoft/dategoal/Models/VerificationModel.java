package com.linkitsoft.dategoal.Models;

public class VerificationModel {
    String userID;
    String Image1;
    String Image2;
    int Status;

    public VerificationModel(){

    }


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public VerificationModel(String userID, String Image1, String Image2){
        this.userID = userID;
        this.Image1 = Image1;
        this.Image2 = Image2;
    }
}
