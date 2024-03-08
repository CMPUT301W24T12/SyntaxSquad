package com.example.eventease2.Attendee;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageButton;

import androidx.lifecycle.ViewModel;

public class AttendeeItemViewModel extends ViewModel {
    private String event = "";
    private String organizer = "";
    private String profileName = "";
    private String profilePhone = "";
    private String profileEmail = "";
    private String profileBio = "";
    private String attendeeID = "";
    private Uri profileImage= null;

    public String getProfileName() {return profileName;}

    public void setProfileName(String profileName) {this.profileName = profileName;}

    public String getProfilePhone() {return profilePhone;}

    public void setProfilePhone(String profilePhone) {this.profilePhone = profilePhone;}

    public String getProfileEmail() {return profileEmail;}

    public void setProfileEmail(String profileEmail) {this.profileEmail = profileEmail;}

    public String getOrganizer() {return organizer;}

    public String getEvent() {return event;}

    public void setEvent(String string){this.event = string;}

    public void setOrganizer(String organizer) {this.organizer = organizer;}

    public String getProfileBio() {return profileBio;}

    public void setProfileBio(String profileBio) {this.profileBio = profileBio;}

    public Uri getProfileImage() {return profileImage;}

    public void setProfileImage(Uri profileImage) {this.profileImage = profileImage;}

    public String getAttendeeID() {return attendeeID;}

    public void setAttendeeID(String attendeeID) {this.attendeeID = attendeeID;}
}
