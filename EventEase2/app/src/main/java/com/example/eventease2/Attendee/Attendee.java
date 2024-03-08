package com.example.eventease2.Attendee;

import android.net.Uri;
import android.widget.ImageButton;
/**
 * This is a class that defines a Attendee. This is made for further implmentation at the final
 * checkpoint.
 * @author Sean
 */
public class Attendee {
    private String name;
    private String phone;
    private String email;
    private String bio;
    private Uri profileImage;
    private String eventID;
    public Attendee() {
        this.name = "";
        this.phone = "";
        this.email = "";
        this.bio = "";

    }

    String getName(){return this.name;}
    String getPhone(){return this.phone;}
    String getEmail(){return this.email;}
    public void setName(String name) {this.name = name;}

    public void setPhone(String phone) {this.phone = phone;}

    public void setEmail(String email) {this.email = email;}

    public String getBio() {return bio;}

    public void setBio(String bio) {this.bio = bio;}

    public Uri getProfileImage() {return profileImage;}

    public void setProfileImage(Uri profileImage) {this.profileImage = profileImage;}
}
