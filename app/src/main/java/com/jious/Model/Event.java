package com.jious.Model;

public class Event {
    private String eID, eName, eDes,eLocation,sDate, eDate, sTime, eTime, eCreatorID, viewAccess, imageURL;

    public Event(){

    }

    public Event(String eID, String eName, String eDes, String eLocation, String sDate, String eDate, String sTime,  String eTime, String eCreatorID,String viewAccess){
        this.eID = eID;
        this.eName = eName;
        this.eDes = eDes;
        this.eLocation = eLocation;
        this.sDate = sDate;
        this.eDate = eDate;
        this.sTime = sTime;
        this.eTime = eTime;
        this.eCreatorID = eCreatorID;
        this.viewAccess = viewAccess;
    }
    public Event(String eID, String eName, String eDes, String eLocation, String sDate, String eDate, String sTime,  String eTime, String eCreatorID,String viewAccess, String imageURL){
        this.eID = eID;
        this.eName = eName;
        this.eDes = eDes;
        this.eLocation = eLocation;
        this.sDate = sDate;
        this.eDate = eDate;
        this.sTime = sTime;
        this.eTime = eTime;
        this.eCreatorID = eCreatorID;
        this.viewAccess = viewAccess;
        this.imageURL = imageURL;
    }

    public String geteID() {
        return eID;
    }

    public String geteName() {
        return eName;
    }

    public String geteDes() {
        return eDes;
    }

    public String geteLocation() {
        return eLocation;
    }

    public String getsDate() {
        return sDate;
    }

    public String geteDate() {
        return eDate;
    }

    public String getsTime() {
        return sTime;
    }

    public String geteTime() {
        return eTime;
    }
    public String geteCreatorID(){
        return eCreatorID;
    }
    public String getviewAccess(){ return viewAccess;}
    public String getImageURL(){return imageURL;};
    public void setImageURL(String imageURL){
        this.imageURL=imageURL;
    }

    public void seteID(String eID) {
        this.eID = eID;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public void seteDes(String eDes) {
        this.eDes = eDes;
    }

    public void seteLocation(String eLocation) {
        this.eLocation = eLocation;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public void seteCreatorID(String eCreatorID) {
        this.eCreatorID = eCreatorID;
    }

    public void setViewAccess(String viewAccess) {
        this.viewAccess = viewAccess;
    }
}
