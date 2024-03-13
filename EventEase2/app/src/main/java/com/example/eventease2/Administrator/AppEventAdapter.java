package com.example.eventease2.Administrator;

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

import com.example.eventease2.R;

import java.util.ArrayList;

public class AppEventAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private ArrayList<String> organizerID;
    private  ArrayList<String> eventIDs;
    private ArrayList<String> participantCountList;
    private Context context;

    Button eventDetailButton;
    Button viewAttendeeButton;


    public AppEventAdapter(Context context, ArrayList<String> eventNames, ArrayList<String> eventDescription, ArrayList<String> organizerID, ArrayList<String> eventIDs, ArrayList<String> participantCountList) {
        super(context, 0, eventNames);
        this.eventNames = eventNames;
        this.eventDescription = eventDescription;
        this.organizerID = organizerID;
        this.eventIDs = eventIDs;
        this.context = context;
        this.participantCountList = participantCountList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_list, parent, false);
        }

        TextView eventNameView = view.findViewById(R.id.event_title);
        TextView eventDetailsView = view.findViewById(R.id.event_description);
        TextView eventCountView = view.findViewById(R.id.participant_count);


        eventNameView.setText(eventNames.get(position));
        eventDetailsView.setText(eventDescription.get(position));
        eventCountView.setText(participantCountList.get(position));


        eventDetailButton = view.findViewById(R.id.event_details);
        eventDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
//                Intent intent = new Intent(context, OrganizerEventFrame.class);
//                intent.putExtra("ID",eventIDs.get(position));
//                intent.putExtra("OrganizerID",organizerID.get(position));
//                // Start the new activity
//                context.startActivity(intent);
            }
        });

        return view;
    }
}

