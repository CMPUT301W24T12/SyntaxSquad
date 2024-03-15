package com.example.eventease2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.eventease2.Attendee.AttendeeStartActivity;

import com.example.eventease2.Administrator.AppEventsActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Opening page of app, User selects their role
 * gets instance of Firebase and imei
 * Each role has an icon with a click listener sending them to their respective acitivty after clicking confirm
 */
import java.util.List;

public class RoleChooseActivity extends AppCompatActivity {

    ImageButton organizerIcon;
    ImageButton admIcon;
    ImageButton attendeeIcon;
    Button confirmButton;
    FirebaseFirestore appDb;
    public String imei;
    private DocumentReference organizerRef;
    private  CollectionReference collectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choose);

        appDb = FirebaseFirestore.getInstance();
        // Firebase contains info of all events on the app
        CollectionReference collectionReference = appDb.collection("Events");

        // Copyright 2020 M. Fadli Zein
        imei = DeviceInfoUtils.getIMEI(getApplicationContext()); // device number
        Log.d("IMEI", imei);
        CollectionReference collectionRef = appDb.collection("Organizer");

        organizerIcon = findViewById(R.id.orgIcon);
        admIcon = findViewById(R.id.admIcon);
        attendeeIcon = findViewById(R.id.attendIcon);
        confirmButton = findViewById(R.id.confirmButton);

        attendeeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RoleChooseActivity.this, AttendeeStartActivity.class);
                        startActivity(intent);
                    }
                });
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

                                                String name = eventSnapshot.getString("Name");

                                                Log.d("AttendeeList length for event " + eventId + " is ", String.valueOf(attendeeListLength));
                                                Log.d("Description for event " + eventId + " is ", description);
                                                Log.d("The name for the event is", name);
                                            }
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
            }
        });

        organizerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RoleChooseActivity.this, EventListFragment.class);
                        intent.putExtra("OrganizerID",imei);
                        startActivity(intent);
                    }
                });
            }
        });

        admIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), AppEventsActivity.class);
                        startActivity(intent);
//                        Toast.makeText(RoleChooseActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                    }
                });


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = db.collection("Organizer").document("ffffffff-8a86-b983-0000-0000380c0fa3").collection("Events");
                collectionRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    // Access each document here
                                    Log.d("NewTag", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                    //                         event id's                            event info
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("NewTag", "Error getting documents.", e);
                            }
                        });
            }
        });
    }}
