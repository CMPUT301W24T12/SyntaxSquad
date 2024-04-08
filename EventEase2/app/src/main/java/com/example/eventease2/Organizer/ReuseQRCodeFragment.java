package com.example.eventease2.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.eventease2.DeviceInfoUtils;
import com.example.eventease2.EventListArrayAdapter;
import com.example.eventease2.EventListFragment;
import com.example.eventease2.R;
import com.example.eventease2.RoleChooseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This fragment displays a list of events that an organizer can choose from to reuse a QR code.
 * It retrieves the list of events associated with the organizer from Firestore and populates the list view.
 * If the organizer has no events, it displays an empty list.
 * The {@link #onCreate(Bundle)} method initializes the fragment by retrieving the list of events associated
 * with the organizer from Firestore and populating the list view accordingly.
 * The {@link #onCreate(Bundle)} method also sets up a click listener for the back button to navigate back to the
 * previous activity when clicked.
 */

public class ReuseQRCodeFragment extends EventListFragment {
    ListView eventList;
    ArrayList<String> eventDataList;
    EventListArrayAdapterReuseQRCode eventListArrayAdapter;
    ArrayList<String> eventID;
    ArrayList<String> eventIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

        eventList = findViewById(R.id.event_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String organizerID = DeviceInfoUtils.getIMEI(getApplicationContext()); // device number
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
                                            eventListArrayAdapter = new EventListArrayAdapterReuseQRCode(ReuseQRCodeFragment.this, eventNameList, eventInfoList, organizerID, eventIDs);
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
                            eventListArrayAdapter = new EventListArrayAdapterReuseQRCode(ReuseQRCodeFragment.this, emptyList, emptyList, organizerID, emptyList);
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

        Button back = findViewById(R.id.button_second);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
