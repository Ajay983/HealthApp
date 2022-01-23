package com.linkitsoft.dategoal.Models;

import java.util.Date;

public class UserModel {

    public String _id;
    public String email;
    public String password;
    public String name;
    public Date DOB;
    public String gender;
    public String ethinicity;
    public String sexuality;
    public String herefor;
    public String phone;
    public String coordinates;
    public String passions;
    public String city;
    public String relegious;
    public String state;
    public String country;
    public String Photo1;
    public String Photo2;
    public String Photo3;
    public String Photo4;
    public String Photo5;
    public String Photo6;
    public String relegion;
    public String tatto;
    public String dreamDate;
    public String relationShipGoals;
    public String matching;
    public String friends;
    public String profilePic;
    public String googleId;

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String facebookId;
    public String loginType;


    public int verfiedStatus;
    public int onlineStatus;
    public int connectedAccounts;

    public String userID;

    public UserModel() {
    }

    public UserModel(String email, String password, String name, Date DOB, String gender, String ethinicity, String sexuality, String herefor, String phone, String coordinates, String passions, String city, String relegious, String state, String country, String photo1, String photo2, String photo3, String photo4, String photo5, String photo6, String relegion, String tatto,
                     String dreamDate, String relationShipGoals, int verfiedStatus, int onlineStatus, int connectedAccounts, String googleId, String loginType,String facebookId) {
        this.email = email;
        this.password = password;
        this.googleId = googleId;
        this.facebookId = facebookId;
        this.loginType = loginType;
        this.name = name;
        this.DOB = DOB;
        this.gender = gender;
        this.ethinicity = ethinicity;
        this.sexuality = sexuality;
        this.herefor = herefor;
        this.phone = phone;
        this.coordinates = coordinates;
        this.passions = passions;
        this.city = city;
        this.relegious = relegious;
        this.state = state;
        this.country = country;
        Photo1 = photo1;
        Photo2 = photo2;
        Photo3 = photo3;
        Photo4 = photo4;
        Photo5 = photo5;
        Photo6 = photo6;
        this.relegion = relegion;
        this.tatto = tatto;
        this.dreamDate = dreamDate;
        this.relationShipGoals = relationShipGoals;
        this.verfiedStatus = verfiedStatus;
        this.onlineStatus = onlineStatus;
        this.connectedAccounts = connectedAccounts;
    }


    public UserModel(String email, String password, String name, Date DOB, String gender, String ethinicity, String sexuality, String herefor, String phone,
                     String coordinates, String passions, String city, String relegious, String state,
                     String country, String photo1, String photo2, String photo3, String photo4, String photo5, String photo6, String relegion,
                     String tatto, String dreamDate, String relationShipGoals, String googleId, String loginType, String facebookId) {

        this.email = email;
        this.password = password;
        this.googleId = googleId;
        this.facebookId = facebookId;
        this.loginType = loginType;
        this.name = name;
        this.DOB = DOB;
        this.gender = gender;
        this.ethinicity = ethinicity;
        this.sexuality = sexuality;
        this.herefor = herefor;
        this.phone = phone;
        this.coordinates = coordinates;
        this.passions = passions;
        this.city = city;
        this.relegious = relegious;
        this.state = state;
        this.country = country;
        Photo1 = photo1;
        Photo2 = photo2;
        Photo3 = photo3;
        Photo4 = photo4;
        Photo5 = photo5;
        Photo6 = photo6;
        this.relegion = relegion;
        this.tatto = tatto;
        this.dreamDate = dreamDate;
        this.relationShipGoals = relationShipGoals;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthinicity() {
        return ethinicity;
    }

    public void setEthinicity(String ethinicity) {
        this.ethinicity = ethinicity;
    }

    public String getSexuality() {
        return sexuality;
    }

    public void setSexuality(String sexuality) {
        this.sexuality = sexuality;
    }

    public String getHerefor() {
        return herefor;
    }

    public void setHerefor(String herefor) {
        this.herefor = herefor;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getPassions() {
        return passions;
    }

    public void setPassions(String passions) {
        this.passions = passions;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRelegious() {
        return relegious;
    }

    public void setRelegious(String relegious) {
        this.relegious = relegious;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getRelegion() {
        return relegion;
    }

    public void setRelegion(String relegion) {
        this.relegion = relegion;
    }

    public String getTatto() {
        return tatto;
    }

    public void setTatto(String tatto) {
        this.tatto = tatto;
    }

    public String getDreamDate() {
        return dreamDate;
    }

    public void setDreamDate(String dreamDate) {
        this.dreamDate = dreamDate;
    }

    public String getRelationShipGoals() {
        return relationShipGoals;
    }

    public void setRelationShipGoals(String relationShipGoals) {
        this.relationShipGoals = relationShipGoals;
    }

    public int getVerfiedStatus() {
        return verfiedStatus;
    }

    public void setVerfiedStatus(int verfiedStatus) {
        this.verfiedStatus = verfiedStatus;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getConnectedAccounts() {
        return connectedAccounts;
    }

    public void setConnectedAccounts(int connectedAccounts) {
        this.connectedAccounts = connectedAccounts;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMatching() {
        return matching;
    }

    public void setMatching(String matching) {
        this.matching = matching;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }
}
