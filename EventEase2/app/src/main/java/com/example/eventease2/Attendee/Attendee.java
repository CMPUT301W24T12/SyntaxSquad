package com.example.eventease2.Attendee;

import android.net.Uri;
import android.widget.ImageButton;

public class Attendee {
    private String name;
    private String phone;
    private String email;
    private String bio;
    private Uri profileImage;
    private String eventID;
//
//    Attendee(String name, String phone, String email){
//        this.name = name;
//        this.phone = phone;
//        this.email = email;
//    }
//    Attendee(String event){this.eventID = event;}

    public Attendee() {
        this.name = "";
        this.phone = "";
        this.email = "";
        this.bio = "";

    }

    public String getName(){return this.name;}
    public String getPhone(){return this.phone;}
    public String getEmail(){return this.email;}
    public void setName(String name) {this.name = name;}

    public void setPhone(String phone) {this.phone = phone;}

    public void setEmail(String email) {this.email = email;}

    public String getBio() {return bio;}

    public void setBio(String bio) {this.bio = bio;}

    public Uri getProfileImage() {return profileImage;}

    public void setProfileImage(Uri profileImage) {this.profileImage = profileImage;}
}
