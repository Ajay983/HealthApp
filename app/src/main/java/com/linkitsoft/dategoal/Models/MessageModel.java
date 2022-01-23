package com.linkitsoft.dategoal.Models;

public class MessageModel {


    public String createdAt;
    public String updatedAt;
    public String userID;
    public String message;
    public String _id;
    public String isViewed;

    public String getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(String isViewed) {
        this.isViewed = isViewed;
    }

    public MessageModel(){

    }

    public String getUserID() {
        return userID;
    }

    public void getUserID(String _id) {
        this.userID = _id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
