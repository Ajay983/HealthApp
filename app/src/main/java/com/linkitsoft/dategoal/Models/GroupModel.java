package com.linkitsoft.dategoal.Models;

public class GroupModel {

    String _id;
    String title;
    String metatitle;
    String slug;
    String summary;
    String profilePic;
    String content;
    String userID;
    int members;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    String adminId;

    public GroupModel() {
    }

    public GroupModel( String title, String metatitle, String slug, String summary, String profilePic, String content, String userID, String adminId) {
        this.title = title;
        this.metatitle = metatitle;
        this.slug = slug;
        this.summary = summary;
        this.profilePic = profilePic;
        this.content = content;
        this.userID = userID;
        this.adminId = adminId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetatitle() {
        return metatitle;
    }

    public void setMetatitle(String metatitle) {
        this.metatitle = metatitle;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }
}
