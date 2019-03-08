package com.jious.Model;

public class Event {
    private String eID, eName, eDes,eLocation,sDate, eDate, sTime, eTime, EUser_ID;

    public Event(){

    }

    public Event(String eID, String eName, String eDes, String eLocation, String sDate, String eDate, String sTime,  String eTime, String EUser_ID){
        this.eID = eID;
        this.eName = eName;
        this.eDes = eDes;
        this.eLocation = eLocation;
        this.sDate = sDate;
        this.eDate = eDate;
        this.sTime = sTime;
        this.eTime = eTime;
        this.EUser_ID = EUser_ID;
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
    public String getEUser_ID(){
        return EUser_ID;
    }
}
