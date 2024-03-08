package com.example.eventease2.Organizer;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.eventease2.Organizer.OrganizerAttendeeListArrayAdapter;
import com.example.eventease2.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
        String id = getIntent().getStringExtra("ID");
        String organizerID = getIntent().getStringExtra("OrganizerID");

        DocumentReference docRef = db.collection("EventEase")
                .document("Organizer")
                .collection(organizerID)
                .document(id);

        /*
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the value of a field named "fieldName" as a String
                    attendeeNameList = (ArrayList<String>) documentSnapshot.get("NameList");

                    // Create a new list and copy the contents of nameList to it
                    Log.d("List", attendeeNameList.get(0));
                } else {
                    Log.d("TAG", "No such document");
                }
            }
        });
        */
        attendeeList = findViewById(R.id.organizer_attendee_list);
        attendeeArrayAdapter = new OrganizerAttendeeListArrayAdapter(this, attendeeNameList);
        attendeeList.setAdapter(attendeeArrayAdapter);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    // Get the value of a field named "fieldName" as a String
                    attendeeNameList = (ArrayList<String>) value.get("NameList");

                    // Create a new list and copy the contents of nameList to it
                    Log.d("List", attendeeNameList.get(0));
                } else {
                    Log.d("TAG", "No such document");
                }
            }
        });
    }

}
