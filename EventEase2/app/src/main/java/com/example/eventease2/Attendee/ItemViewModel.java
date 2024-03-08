package com.example.eventease2.Attendee;

import android.view.View;

import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private String string = "";
    public String getString() {
        return string;
    }
    public void setString(String string){
        this.string = string;
    }


}
