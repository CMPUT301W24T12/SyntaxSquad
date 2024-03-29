package com.example.eventease2.Attendee;

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

import com.example.eventease2.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AttendeeEventAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private ArrayList<String> organizerID;
    private  ArrayList<String> eventIDs;
    private ArrayList<String> maxAttendee;
    private Context context;
    private String attendeeID, attendeeName, attendeePhone, attendeeEmail,stringEventPhoto;


    Button eventDetailButton;
    AttendeeAppData attendeeAppData;

    public AttendeeEventAdapter(Context context, AttendeeAppData appData,String attendeeID,
                                String attendeeName,String attendeePhone, String attendeeEmail) {
        super(context, 0, appData.getEventNameList());
        this.attendeeAppData = appData;
        this.eventNames = appData.getEventNameList();
        this.eventDescription = appData.getEventInfoList();
        this.organizerID = appData.getOrganizerList();
        this.eventIDs = appData.getEventIDs();
        this.maxAttendee = appData.getMaxAttendeeList();
        this.context = context;
        this.attendeeID = attendeeID;
        this.attendeeName = attendeeName;
        this.attendeePhone = attendeePhone;
        this.attendeeEmail = attendeeEmail;
        //this.participantCountList = appData.getParticipantCountList();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.attendee_event_list, parent, false);
        }
        StorageReference storageRef = storage.getReference();


        TextView eventNameView = view.findViewById(R.id.event_title);
        TextView eventDetailsView = view.findViewById(R.id.event_description);
        //TextView eventCountView = view.findViewById(R.id.entriesSignedUp);
        TextView maxAttendeeCount = view.findViewById(R.id.maxEntriesNum);
        Button eventDetailButton = view.findViewById(R.id.event_details);


        eventNameView.setText(eventNames.get(position));
        eventDetailsView.setText(eventDescription.get(position));
        //eventCountView.setText(participantCountList.get(position));
        maxAttendeeCount.setText(maxAttendee.get(position));

        eventDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                Intent intent = new Intent(context, AttendeeEventDetailsActivity.class);
                intent.putExtra("ID", eventIDs.get(position));
                intent.putExtra("OrganizerID", organizerID.get(position));
                intent.putExtra("posOfEvent", position);
                intent.putExtra("AttendeeID",attendeeID);
                intent.putExtra("AttendeeName",attendeeName);
                intent.putExtra("AttendeePhone",attendeePhone);
                intent.putExtra("AttendeeEmail",attendeeEmail);
                intent.putExtra("AttendeePhoto",stringEventPhoto);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Log.d("BACK", "I am back");
                notifyDataSetChanged();
            }
        });


        return view;
    }

}

