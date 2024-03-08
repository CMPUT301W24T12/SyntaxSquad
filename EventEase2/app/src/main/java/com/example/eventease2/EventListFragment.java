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

public class EventListFragment extends AppCompatActivity {
    ListView eventList;
    ArrayList<String> eventDataList;
    EventListArrayAdapter eventListArrayAdapter;
    ArrayList<String> eventID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String organizerID = getIntent().getStringExtra("OrganizerID");

        /*
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the value of a field named "fieldName" as a String
                    String eventName = (String) documentSnapshot.get("Name");

                    // Create a new list and copy the contents of nameList to it
                    Log.d("Event Name: ", eventName);
                } else {
                    Log.d("TAG", "No such document");
                }
            }
        });
        */

        ArrayList<String> eventIDs = new ArrayList<>();

        CollectionReference collectionRef = db.collection("Organizer").document(organizerID).collection("Events");
        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Access each document here
                            Log.d("NewTag", documentSnapshot.getId() + " => " + documentSnapshot.getData());
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

        //start loop

        eventListArrayAdapter = new EventListArrayAdapter(EventListFragment.this, eventNameList, eventInfoList, organizerID, eventID);
        eventList.setAdapter(eventListArrayAdapter);

        ImageButton add = findViewById(R.id.imageButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(EventListFragment.this, AddEventFragment.class);
                intent.putExtra("OrganizerID",organizerID);
                // Start the new activity
                startActivity(intent);
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

        ImageButton add = findViewById(R.id.imageButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(EventListFragment.this, AddEventFragment.class);
                intent.putExtra("OrganizerID",organizerID);
                // Start the new activity
                startActivity(intent);
            }
        });
    }
    */
}