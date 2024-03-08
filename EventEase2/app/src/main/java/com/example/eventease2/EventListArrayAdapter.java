package com.example.eventease2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventease2.Organizer.OrganizerAttendeeListFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventListArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private String organizerID;
    private  ArrayList<String> eventIDs;
    private Context context;

    private String name;
    private String description;
    private String eventID;

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
        Log.d("Adapter", "hello");
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list, parent,false);
        }


        if (!eventIDs.isEmpty()) {
            name = eventNames.get(position);
            description = eventDescription.get(position);
            eventID = eventIDs.get(position);
        }
        Log.d("Adapter", "hello agains");
        TextView eventName = view.findViewById(R.id.event_title);
        if (eventName == null) {
            Log.e("Event List1", "ListView with ID event_list not found in layout");
        } else {
            Log.e("Event List2", "good");
        }
        TextView eventDetails = view.findViewById(R.id.event_description);
        if (eventName == null) {
            Log.e("Event List1", "ListView with ID event_list not found in layout");
        } else {
            Log.e("Event List2", "good");
        }
        eventName.setText(name);
        eventDetails.setText(description);

        Button eventInfo = view.findViewById(R.id.event_details);
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
//                Intent intent = new Intent(context, EventFragment.class);
//                intent.putExtra("ID",eventID);
//                intent.putExtra("OrganizerID",organizerID);
//                // Start the new activity
//                context.startActivity(intent);
            }
        });

        Button viewAttendees = view.findViewById(R.id.view_attendees);
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
