package com.example.eventease2.Attendee;

public class Attendee {
    private String name;
    private String phone;
    private String email;

    Attendee(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    String getName(){return this.name;}
    String getPhone(){return this.phone;}
    String getEmail(){return this.email;}
}
