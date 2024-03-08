package com.example.eventease2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.Organizer.AddEventFragment;
import com.example.eventease2.Organizer.OrganizerAttendeeListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment for the Events List page, gets the necessary data for the page from firebase
 * and displays it using EventListArrayAdapter. Also has a add/plus button that will send users to the
 * create event activity
 * @author Adeel Khan
 */
public class EventListFragment extends AppCompatActivity {
    ListView eventList;
    ArrayList<String> eventDataList;
    EventListArrayAdapter eventListArrayAdapter;
    ArrayList<String> eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

        eventList = findViewById(R.id.event_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String organizerID = getIntent().getStringExtra("OrganizerID");
        Log.d("Event List", organizerID);

        ArrayList<String> organizerIDs = new ArrayList<>();
        CollectionReference organizerRef = db.collection("Organizer");
        organizerRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Access each document here
                            Log.d("Event List", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            //                         organizer id's                            organizer info

                            organizerIDs.add(documentSnapshot.getId());
                            Log.d("Event List", organizerIDs.get(0));
                        }
                        Log.d("Event List", organizerIDs.get(0));
                        Log.d("Event List", organizerID);
                        if (organizerIDs.contains("ffffffff-8a86-b983-0000-0000380c0fa3")) {
                                                     //replace with organizerID
                            Log.d("Nested", organizerIDs.get(0));
                            ArrayList<String> eventNameList = new ArrayList<>();
                            ArrayList<String> eventInfoList = new ArrayList<>();
                            ArrayList<String> eventIDs = new ArrayList<>();
                            CollectionReference eventRef = db.collection("Organizer").document("ffffffff-8a86-b983-0000-0000380c0fa3").collection("Events");
                                                                                                                         // replace with organizerID
                            eventRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                // Access each document here
                                                Log.d("NewTag", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                                //                         event id's                            event info
                                                String name = documentSnapshot.getString("Name");
                                                String description = documentSnapshot.getString("Description");

                                                eventNameList.add(name);
                                                eventInfoList.add(description);
                                                eventIDs.add(documentSnapshot.getId());
                                            }
                                            Log.d("Event IDs", eventIDs.get(0));
                                            Log.d("Name List", eventNameList.get(0));
                                            Log.d("Event Info", eventInfoList.get(0));
                                            eventListArrayAdapter = new EventListArrayAdapter(EventListFragment.this, eventNameList, eventInfoList, organizerID, eventIDs);
                                            eventList.setAdapter(eventListArrayAdapter);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("NewTag", "Error getting documents.", e);
                                        }
                                    });
                        } else {
                            Log.d("Not If", "new Organizer ID");
                            Log.d("Event List", organizerIDs.get(0));
                            ArrayList<String> emptyList = new ArrayList<String>();
                            eventListArrayAdapter = new EventListArrayAdapter(EventListFragment.this, emptyList, emptyList, organizerID, emptyList);
                            eventList.setAdapter(eventListArrayAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NewTag", "Error getting documents.", e);
                    }
                });

        ImageButton add = findViewById(R.id.attendeeProfileImage);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(EventListFragment.this, AddEventFragment.class);
                intent.putExtra("OrganizerID", organizerID);
                // Start the new activity
                startActivity(intent);
            }
        });
    }
}

        /*
        if (organizerIDs.contains(organizerID)) {
            ArrayList<String> eventNameList = new ArrayList<>();
            ArrayList<String> eventInfoList = new ArrayList<>();
            ArrayList<String> eventIDs = new ArrayList<>();
            CollectionReference eventRef = db.collection("Organizer").document("ffffffff-8a86-b983-0000-0000380c0fa3").collection("Events");
                                                                                                         // replace with organizerID
            eventRef.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Access each document here
                                Log.d("NewTag", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                //                         event id's                            event info
                                String name = documentSnapshot.getString("Name");
                                String description = documentSnapshot.getString("Description");

                                eventNameList.add(name);
                                eventInfoList.add(description);
                                eventIDs.add(documentSnapshot.getId());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("NewTag", "Error getting documents.", e);
                        }
                    });
            eventListArrayAdapter = new EventListArrayAdapter(EventListFragment.this, eventNameList, eventInfoList, organizerID, eventIDs);
            eventList.setAdapter(eventListArrayAdapter);
        } else {
            ArrayList<String> emptyList = new ArrayList<String>();
            eventListArrayAdapter = new EventListArrayAdapter(EventListFragment.this, emptyList, emptyList, organizerID, emptyList);
            eventList.setAdapter(eventListArrayAdapter);
        }
        */

//start loop

//eventListArrayAdapter = new EventListArrayAdapter(EventListFragment.this, eventNameList, eventInfoList, organizerID, eventIDs);
//eventList.setAdapter(eventListArrayAdapter);

        /*
        eventList = findViewById(R.id.event_list);
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> eventNameList = new ArrayList<>();
                    ArrayList<String> eventInfoList = new ArrayList<>();
                    ArrayList<String> eventIDsList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        String name = document.getString("Name");
                        String description = document.getString("Description");
                        String eventID = document.getString("ID");

                        eventNameList.add(name);
                        eventInfoList.add(description);
                        eventIDsList.add(eventID);
                    }

                    // Do something with the event information list, such as displaying it in a RecyclerView
                    // or updating UI elements
                    // For example:
                    // ArrayAdapter<String> adapter = new ArrayAdapter<>(EventActivity.this, android.R.layout.simple_list_item_1, eventInfoList);
                    // listView.setAdapter(adapter);
                    eventListArrayAdapter = new EventListArrayAdapter(EventListFragment.this, eventNameList, eventInfoList, organizerID, eventID);
                    eventList.setAdapter(eventListArrayAdapter);
                } else {
                    Log.d("Event List", "Error getting documents: ", task.getException());
                }
            }
        });

    */