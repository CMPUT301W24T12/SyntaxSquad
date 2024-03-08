package com.example.eventease2.Organizer;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.eventease2.EventListArrayAdapter;
import com.example.eventease2.EventListFragment;
import com.example.eventease2.Organizer.OrganizerAttendeeListArrayAdapter;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrganizerAttendeeListFragment extends AppCompatActivity {

    ListView attendeeList;
    ArrayList<String> attendeeNameList;
    OrganizerAttendeeListArrayAdapter attendeeArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_attendee_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventID = getIntent().getStringExtra("ID");
        String organizerID = getIntent().getStringExtra("OrganizerID");

        attendeeList = findViewById(R.id.organizer_attendee_list);

        CollectionReference attendeeRef = db.collection("Organizer").document("ffffffff-8a86-b983-0000-0000380c0fa3").collection("Events").document("d2ea4e72-5d4c-47a6-8c4b-49a442a08a41").collection("Attendees");
        ArrayList<String> attendeeIDs = new ArrayList<>();                                                   //replace with organizer ID                                                                        //replace with event ID
        ArrayList<String> attendeeNames = new ArrayList<>();
        attendeeRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Access each document here
                            Log.d("NewTag", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            //                         attendee id's                            attendee info
                            attendeeIDs.add(documentSnapshot.getId());
                            attendeeNames.add(documentSnapshot.getString("Name"));
                        }
                        Log.d("Attendee IDs", attendeeIDs.get(7));
                        Log.d("Attendee Names", attendeeNames.get(7));
                        attendeeArrayAdapter = new OrganizerAttendeeListArrayAdapter(OrganizerAttendeeListFragment.this, attendeeIDs, attendeeNames);
                        attendeeList.setAdapter(attendeeArrayAdapter);
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
