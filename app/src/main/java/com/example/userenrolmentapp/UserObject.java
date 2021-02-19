package com.example.userenrolmentapp;


public class UserObject {
    private String phone,fname,lname,dob,gender,country,state,hometown,timestamp;

    public  UserObject(String phone,String fname,String lname,String dob,String gender,String country,String state,String hometown,String timestamp) {
        this.phone=phone;
        this.fname=fname;
        this.lname=lname;
        this.dob=dob;
        this.gender=gender;
        this.country=country;
        this.state=state;
        this.timestamp=timestamp;
        this.hometown=hometown;
    }

    public String getFname(){return fname;}
    public String getPhone(){return phone;}
    public String getLname(){return lname;}
    public String getDob(){return dob;}
    public String getGender(){return gender;}
    public String getCountry(){return country;}
    public String getState(){return state;}
    public String getTimestamp(){return timestamp;}
    public String getHometown(){return hometown;}
}

