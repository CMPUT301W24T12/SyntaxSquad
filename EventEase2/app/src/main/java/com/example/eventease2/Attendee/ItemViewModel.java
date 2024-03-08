package com.example.eventease2.Attendee;

import android.view.View;

import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private String event = "";

    public String getOrganizer() {return organizer;}
    private String organizer = "";
    public String getEvent() {
        return event;
    }
    public void setString(String string){
        this.event = string;
    }


}
