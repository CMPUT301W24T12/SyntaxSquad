package com.example.eventease2.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.eventease2.EventListArrayAdapter;
import com.example.eventease2.EventListFragment;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReuseQRCodeFragment extends EventListFragment {
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
                        if (organizerIDs.contains(organizerID)) {
                            //replace with organizerID
                            Log.d("Nested", organizerIDs.get(0));
                            ArrayList<String> eventNameList = new ArrayList<>();
                            ArrayList<String> eventInfoList = new ArrayList<>();
                            ArrayList<String> eventIDs = new ArrayList<>();
                            CollectionReference eventRef = db.collection("Organizer").document(organizerID).collection("Events");

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
                                            eventListArrayAdapter = new EventListArrayAdapter(ReuseQRCodeFragment.this, eventNameList, eventInfoList, organizerID, eventIDs);
                                            eventList.setAdapter(eventListArrayAdapter);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("NewTag", "Error getting documents.", e);
                                        }
                                    });
                            eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String eventID = eventIDs.get(position);
//                                    Intent intent = new Intent(EventListFragment.this, AddEventFragment.class);
//                                    intent.putExtra("OrganizerID", organizerID);
//                                    startActivity(intent);
                                    Toast.makeText(ReuseQRCodeFragment.this,eventID,Toast.LENGTH_LONG).show();

                                }
                            });

                        } else {
                            Log.d("Not If", "new Organizer ID");
                            Log.d("Event List", organizerIDs.get(0));
                            ArrayList<String> emptyList = new ArrayList<String>();
                            eventListArrayAdapter = new EventListArrayAdapter(ReuseQRCodeFragment.this, emptyList, emptyList, organizerID, emptyList);
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
    }
}
