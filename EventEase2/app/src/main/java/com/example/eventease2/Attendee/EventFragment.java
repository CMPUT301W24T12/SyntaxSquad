package com.example.eventease2.Attendee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eventease2.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    private String eventID;

    private EditText eventTitle;
    private EditText eventDescription;
    private EditText eventBody;


    public String getEventID() {
        return eventID;
    }

    EventFragment(String event){
        this.eventID = event;
    }
    EventFragment(){
        this.eventID = "default";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(!Objects.equals(eventID, "default")){

            return inflater.inflate(R.layout.fragment_event, container, false);
        }else{
            return inflater.inflate(R.layout.fragment_event, container, false);
        }
    }
}