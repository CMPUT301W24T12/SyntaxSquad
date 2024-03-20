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
import com.example.eventease2.Organizer.OrganizerEventFrame;
import com.example.eventease2.Organizer.OrganizerSignUpFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Custom ListArray Adapter for the Event List page
 * uses the event_list.xml to display the events in the Event Page.
 * Recieves information fromEventListFragment
 * @author Adeel Khan
 * @param Context context, ArrayList<String> eventNames, ArrayList<String> eventDescription, String organizerID, ArrayList<String> eventIDs
 * @return view
 */
public class EventListArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private String organizerID;
    private ArrayList<String> eventIDs;
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

        Button eventInfo = view.findViewById(R.id.event_details);
        eventInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrganizerEventFrame.class);
            intent.putExtra("ID", eventID);
            intent.putExtra("OrganizerID", organizerID);
            context.startActivity(intent);
        });

        Button viewAttendees = view.findViewById(R.id.view_attendees);
        viewAttendees.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrganizerSignUpFragment.class);
            intent.putExtra("ID", eventID);
            intent.putExtra("OrganizerID", organizerID);
            context.startActivity(intent);
        });

        return view;
    }
}
