package com.example.eventease2.Attendee;

import androidx.lifecycle.ViewModel;

public class AttendeeItemViewModel extends ViewModel {
    private String event = "";
    private String organizer = "";
    private String profileName = "";
    private String profilePhone = "";
    private String profileEmail = "";
    public String getProfileName() {return profileName;}

    public void setProfileName(String profileName) {this.profileName = profileName;}

    public String getProfilePhone() {return profilePhone;}

    public void setProfilePhone(String profilePhone) {this.profilePhone = profilePhone;}

    public String getProfileEmail() {return profileEmail;}

    public void setProfileEmail(String profileEmail) {this.profileEmail = profileEmail;}

    public String getOrganizer() {return organizer;}
    public String getEvent() {
        return event;
    }
    public void setEvent(String string){
        this.event = string;
    }
    public void setOrganizer(String organizer) {this.organizer = organizer;}


}
