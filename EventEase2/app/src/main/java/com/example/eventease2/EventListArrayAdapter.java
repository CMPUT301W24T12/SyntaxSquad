package com.example.eventease2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventListArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private String organizerID;
    private  ArrayList<String> eventIDs;
    private Context context;

    public EventListArrayAdapter(Context context, ArrayList<String> eventNames, ArrayList<String> eventDescription, String organizerID, ArrayList<String> eventIDs) {
        super(context, 0, eventNames);
        this.eventNames = eventNames;
        this.eventDescription = eventDescription;
        this.organizerID = organizerID;
        this.eventIDs = eventIDs;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_list, parent, false);
        }

        String name = eventNames.get(position);
        String description = eventDescription.get(position);
        String eventID = eventIDs.get(position);

        TextView eventName = view.findViewById(R.id.event_title);
        TextView eventDetails = view.findViewById(R.id.event_description);

        eventName.setText(name);
        eventDetails.setText(description);

        Button eventInfo = convertView.findViewById(R.id.event_details);
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(context, EventFragment.class);
                intent.putExtra("ID",eventID);
                intent.putExtra("OrganizerID",organizerID);
                // Start the new activity
                context.startActivity(intent);
            }
        });

        Button viewAttendees = convertView.findViewById(R.id.view_attendees);
        viewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(context, OrganizerAttendeeListFragment.class);
                intent.putExtra("ID",eventID);
                intent.putExtra("OrganizerID",organizerID);
                // Start the new activity
                context.startActivity(intent);
            }
        });

        return view;
    }
}
