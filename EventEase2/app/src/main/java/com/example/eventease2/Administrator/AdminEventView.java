package com.example.eventease2.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventease2.Administrator.AdminArrayAdapter;
import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminEventView extends AppCompatActivity {


    ListView eventList;
    ArrayList<Event> eventDataList;
    AdminArrayAdapter adminArrayAdapter;
    private TextView eventNameText;
    private EditText eventTitle;
    private Button eventDetailsButton;
    private Button eventAttendeeButton;
    private Button backButton;
    private ListView eventListView;
    private FirebaseFirestore appDb;
    private CollectionReference eventIdRefrence;
    private ImageView imageEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_view2);
        appDb = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = appDb.collection("Organizer");

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot organizerSnapshot : queryDocumentSnapshots) {
                            // Access each organizer document here
                            String organizerId = organizerSnapshot.getId();
                            // Get a reference to the "Events" collection for this organizer
                            CollectionReference eventsCollectionRef = appDb.collection("Organizer").document(organizerId).collection("Events");
                            ArrayList<Event> eventsIDs = new ArrayList<>();
                            eventsCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot eventQueryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot eventSnapshot : eventQueryDocumentSnapshots) {
                                        // Access each event document here
                                        String eventId = eventSnapshot.getId();
                                        Log.d("Event ID is ", eventId);

                                        // Get the AttendeeList field from the event document
                                        List<String> attendeeList = (List<String>) eventSnapshot.get("AttendeeList");
                                        // Get the length of the AttendeeList
                                        int attendeeListLength = attendeeList != null ? attendeeList.size() : 0;

                                        // Get the Description field from the event document
                                        String description = eventSnapshot.getString("Description");

                                        // Get the name of the event
                                        String name = eventSnapshot.getString("Name");
                                        Event event = new Event(null,name,description,null,false,null,null);
                                        eventsIDs.add(event);
                                        Log.d("AttendeeList length for event " + eventId + " is ", String.valueOf(attendeeListLength));
                                        Log.d("Description for event " + eventId + " is ", description);
                                    }
                                    adminArrayAdapter = new AdminArrayAdapter(AdminEventView.this, eventsIDs);
                                    eventListView.setAdapter(adminArrayAdapter);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failures
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failures
                    }
                });

        //        List attendeeList = new ArrayList<>();
        //        eventDetailsButton = findViewById(R.id.event_details);
        //        eventAttendeeButton = findViewById(R.id.view_attendees);
        //        adminArrayAdapter = new AdminArrayAdapter(this, eventDataList);
        //        eventListView.setAdapter(adminArrayAdapter);
        //        eventDetailsButton.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //
        //            }
        //        });
        //
        //        eventDetailsButton.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //
        //            }
        //        });
    }
}




