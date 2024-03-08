package com.example.eventease2.Attendee;

public class Attendee {
    private String name;



    private String phone;
    private String email;
    private String eventID;

    Attendee(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    Attendee(String event){
        this.eventID = event;
    }

    public Attendee() {
        this.name = "name";
        this.phone = "phone";
        this.email = "email";
    }

    String getName(){return this.name;}
    String getPhone(){return this.phone;}
    String getEmail(){return this.email;}
    public void setName(String name) {this.name = name;}

    public void setPhone(String phone) {this.phone = phone;}

    public void setEmail(String email) {this.email = email;}
}
