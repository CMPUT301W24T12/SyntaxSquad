package com.example.eventease2.Attendee;

// Imported libraries

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * This class is a custom ArrayAdapter used to populate a ListView with event information for attendees.
 * It binds the event name, description, and other details to the respective TextViews and Buttons in the list item layout.
 * <p>
 * The constructor initializes the adapter with event data and attendee information.
 * </p>
 * <p>
 * The {@link #getView(int, View, ViewGroup)} method is responsible for creating the View for each item in the ListView,
 * updating UI elements based on attendee's participation status in events, and setting click listeners for event details buttons.
 * </p>
 * @author Sean
 */
public class AttendeeEventAdapter extends ArrayAdapter<String> {

    // Member variables
    private final ArrayList<String> eventNames;
    private final ArrayList<String> eventDescription;
    private final ArrayList<String> organizerID;
    private final ArrayList<String> eventIDs;
    private final Context context;
    private final String attendeeID;
    private final String attendeeName;
    private final String attendeePhone;
    private final String attendeeEmail;
    private final FirebaseFirestore appDb = FirebaseFirestore.getInstance();
    private Button eventDetailButton;
    private final AttendeeAppData attendeeAppData;

    /**
     * Constructor for the AttendeeEventAdapter.
     *
     * @param context       The context of the application.
     * @param appData       An instance of AttendeeAppData containing event data.
     * @param attendeeID    The ID of the attendee.
     * @param attendeeName  The name of the attendee.
     * @param attendeePhone The phone number of the attendee.
     * @param attendeeEmail The email of the attendee.
     */
    public AttendeeEventAdapter(Context context, AttendeeAppData appData, String attendeeID,
                                String attendeeName, String attendeePhone, String attendeeEmail) {
        super(context, 0, appData.getEventNameList());
        this.attendeeAppData = appData;
        this.eventNames = appData.getEventNameList();
        this.eventDescription = appData.getEventInfoList();
        this.organizerID = appData.getOrganizerList();
        this.eventIDs = appData.getEventIDs();
        this.context = context;
        this.attendeeID = attendeeID;
        this.attendeeName = attendeeName;
        this.attendeePhone = attendeePhone;
        this.attendeeEmail = attendeeEmail;
    }

    /**
     * Method to get the View for each item in the ListView. And updating which events that the
     * current attendee is a part of.
     *
     * @param position    The position of the item in the ListView.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup.
     * @return The View for the item at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.attendee_event_list, parent, false);
        }
        StorageReference storageRef = storage.getReference();

        // Find TextViews and Button in the layout
        TextView eventNameView = view.findViewById(R.id.event_title);
        TextView eventDetailsView = view.findViewById(R.id.event_description);
        TextView enteredInEvent = view.findViewById(R.id.signedUp);
        Button eventDetailButton = view.findViewById(R.id.event_details);

        // Check if attendee is signed up for the event and update UI accordingly
        CollectionReference attendeeCollectionRef = appDb.collection("Organizer")
                .document(organizerID.get(position)).collection("Events")
                .document(eventIDs.get(position)).collection("Attendees");
        attendeeCollectionRef.document(attendeeID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        enteredInEvent.setText("Yes");
                        enteredInEvent.setTextSize(40);
                        enteredInEvent.setTextColor(Color.rgb(0, 181, 169));
                    } else {
                        Log.d(TAG, "No such document");
                        enteredInEvent.setText("No");
                        enteredInEvent.setTextSize(24);
                        enteredInEvent.setTextColor(Color.rgb(0, 0, 0));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // Set event name and description
        eventNameView.setText(eventNames.get(position));
        eventDetailsView.setText(eventDescription.get(position));

        // Set click listener for event details button
        eventDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here
                Intent intent = new Intent(context, AttendeeEventDetailsActivity.class);
                intent.putExtra("ID", eventIDs.get(position));
                intent.putExtra("OrganizerID", organizerID.get(position));
                intent.putExtra("posOfEvent", position);
                intent.putExtra("AttendeeID", attendeeID);
                intent.putExtra("AttendeeName", attendeeName);
                intent.putExtra("AttendeePhone", attendeePhone);
                intent.putExtra("AttendeeEmail", attendeeEmail);
                // Start activity and pass necessary data
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return view;
    }

}
