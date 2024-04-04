package com.example.eventease2.Administrator;

import android.app.Activity;
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

import com.example.eventease2.Attendee.AttendeeEventFragment;
import com.example.eventease2.Organizer.OrganizerEventFrame;
import com.example.eventease2.R;

import java.util.ArrayList;

public class AppEventAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private ArrayList<String> organizerID;
    private ArrayList<String> eventIDs;
    private ArrayList<String> participantCountList;
    private Context context;
    private int initiallyDisplayedCount; // Change this to the number of events initially displayed

    // Constructor
    public ArrayList<String> getEventNames() {
        return eventNames;
    }

    public ArrayList<String> getEventDescription() {
        return eventDescription;
    }

    public ArrayList<String> getOrganizerID() {
        return organizerID;
    }

    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }

    public ArrayList<String> getParticipantCountList() {
        return participantCountList;
    }
    public void setInitiallyDisplayedCount(int initiallyDisplayedCount) {
        this.initiallyDisplayedCount = initiallyDisplayedCount;
    }

    public AppEventAdapter(Context context, ArrayList<String> eventNames, ArrayList<String> eventDescription, ArrayList<String> organizerID, ArrayList<String> eventIDs, ArrayList<String> participantCountList, int initiallyDisplayedCount) {
        super(context, 0, eventNames);
        this.eventNames = eventNames;
        this.eventDescription = eventDescription;
        this.organizerID = organizerID;
        this.eventIDs = eventIDs;
        this.participantCountList = participantCountList;
        this.context = context;
        this.initiallyDisplayedCount = initiallyDisplayedCount;
    }
    public void updateData(ArrayList<String> eventNameList, ArrayList<String> eventInfoList, ArrayList<String> organizerList, ArrayList<String> eventIDs, ArrayList<String> participantCountList) {
        this.eventNames = eventNameList;
        this.eventDescription = eventInfoList;
        this.organizerID = organizerList;
        this.eventIDs = eventIDs;
        this.participantCountList = participantCountList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return Math.min(initiallyDisplayedCount, eventNames.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.indiv_event_frag, parent, false);
        }

        TextView eventNameView = view.findViewById(R.id.event_title);
        TextView eventDetailsView = view.findViewById(R.id.event_description);
        TextView eventCountView = view.findViewById(R.id.participant_count);
        Button eventDetailButton = view.findViewById(R.id.event_details);
        Button attendeeListButton = view.findViewById(R.id.view_attendees);
        String eventID = eventIDs.get(position);
        eventNameView.setText(eventNames.get(position));
        eventDetailsView.setText(eventDescription.get(position));
        eventCountView.setText(participantCountList.get(position));


        eventDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                Intent intent = new Intent(context, EventEditorActivity.class);
                intent.putExtra("ID", eventIDs.get(position));
                intent.putExtra("OrganizerID", organizerID.get(position));
                intent.putExtra("posOfEvent", String.valueOf(position));
                context.startActivity(intent);
                Log.d("BACK", "I am back");
            }
        });
        attendeeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminAttendeeView.class);
                intent.putExtra("ID", eventID);
                intent.putExtra("OrganizerID", organizerID.get(position));
                context.startActivity(intent);
                ((Activity) context).finish();

            }
        });






        return view;
    }
}