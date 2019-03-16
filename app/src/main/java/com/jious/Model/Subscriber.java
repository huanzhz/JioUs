package com.jious.Model;

public class Subscriber {

    private String subID;
    private String eCreatorID;
    private String userID;

    public Subscriber(){

    };

    public Subscriber(String subID, String eCreatorID, String userID){
        this.subID = subID;
        this.eCreatorID = eCreatorID;
        this.userID = userID;

    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String geteCreatorID() {
        return eCreatorID;
    }

    public void seteCreatorID(String eCreatorID) {
        this.eCreatorID = eCreatorID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
