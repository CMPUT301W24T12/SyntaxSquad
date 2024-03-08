package com.example.eventease2.Attendee;

import androidx.lifecycle.ViewModel;

public class AttendeeItemViewModel extends ViewModel {
    private String event = "";


    private String organizer = "";

    public String getOrganizer() {return organizer;}
    public String getEvent() {
        return event;
    }
    public void setEvent(String string){
        this.event = string;
    }
    public void setOrganizer(String organizer) {this.organizer = organizer;}


}
