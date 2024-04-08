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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventease2.Organizer.OrganizerAttendeeListFragment;

import com.example.eventease2.Attendee.AttendeeEventFragment;
import com.example.eventease2.Organizer.OrganizerEventFrame;
import com.example.eventease2.R;

import java.util.ArrayList;

/**
 * AppEventAdapter is a custom ArrayAdapter used to populate a ListView with event data.
 * It handles displaying event names, descriptions, participant counts, and provides
 * functionality for event detail and attendee list button clicks.
 */

public class AppEventAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private ArrayList<String> organizerID;
    private ArrayList<String> eventIDs;
    private ArrayList<String> participantCountList;
    private Context context;
    private int initiallyDisplayedCount; // Change this to the number of events initially displayed

    /**
     * Constructor for the adapter.
     *
     * @param context                The context in which the adapter is being used.
     * @param eventNames             List of event names.
     * @param eventDescription       List of event descriptions.
     * @param organizerID            List of organizer IDs.
     * @param eventIDs               List of event IDs.
     * @param participantCountList   List of participant counts for each event.
     * @param initiallyDisplayedCount The initial count of events to be displayed.
     */
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

    /**
     * Getter method for event names.
     *
     * @return List of event names.
     */
    public ArrayList<String> getEventNames() {
        return eventNames;
    }

    /**
     * Getter method for event descriptions.
     *
     * @return List of event descriptions.
     */
    public ArrayList<String> getEventDescription() {
        return eventDescription;
    }

    /**
     * Getter method for organizer IDs.
     *
     * @return List of organizer IDs.
     */
    public ArrayList<String> getOrganizerID() {
        return organizerID;
    }

    /**
     * Getter method for event IDs.
     *
     * @return List of event IDs.
     */
    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }

    /**
     * Getter method for participant counts.
     *
     * @return List of participant counts for each event.
     */
    public ArrayList<String> getParticipantCountList() {
        return participantCountList;
    }

    /**
     * Setter method for initiallyDisplayedCount.
     *
     * @param initiallyDisplayedCount The initial count of events to be displayed.
     */
    public void setInitiallyDisplayedCount(int initiallyDisplayedCount) {
        this.initiallyDisplayedCount = initiallyDisplayedCount;
    }

    /**
     * Method to update adapter data.
     *
     * @param eventNameList      Updated list of event names.
     * @param eventInfoList      Updated list of event descriptions.
     * @param organizerList      Updated list of organizer IDs.
     * @param eventIDs           Updated list of event IDs.
     * @param participantCountList Updated list of participant counts for each event.
     */
    public void updateData(ArrayList<String> eventNameList, ArrayList<String> eventInfoList, ArrayList<String> organizerList, ArrayList<String> eventIDs, ArrayList<String> participantCountList) {
        this.eventNames = eventNameList;
        this.eventDescription = eventInfoList;
        this.organizerID = organizerList;
        this.eventIDs = eventIDs;
        this.participantCountList = participantCountList;
        notifyDataSetChanged();
    }

    /**
     * Method to handle event detail button click.
     *
     * @param position The position of the clicked event in the list.
     */
    private void handleEventDetailButtonClick(int position) {
        Intent intent = new Intent(context, EventEditorActivity.class);
        intent.putExtra("ID", eventIDs.get(position));
        intent.putExtra("OrganizerID", organizerID.get(position));
        intent.putExtra("posOfEvent", String.valueOf(position));
        context.startActivity(intent);
        Log.d("BACKKKK", "I am back");
    }

    /**
     * Method to handle attendee list button click.
     *
     * @param position The position of the clicked event in the list.
     */
    private void handleAttendeeListButtonClick(int position) {
        Intent intent = new Intent(context, AdminAttendeeView.class);
        intent.putExtra("ID", eventIDs.get(position));
        intent.putExtra("OrganizerID", organizerID.get(position));
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * Method to determine the number of items in the adapter.
     *
     * @return The number of items in the adapter, capped at initiallyDisplayedCount.
     */
    @Override
    public int getCount() {
        return Math.min(initiallyDisplayedCount, eventNames.size());
    }

    /**
     * Method to create and populate views for the ListView.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return The view corresponding to the data at the specified position.
     */
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
                handleEventDetailButtonClick(position);
            }
        });

        attendeeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAttendeeListButtonClick(position);
            }
        });

        return view;
    }
}