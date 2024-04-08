package com.example.eventease2.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.eventease2.EventListArrayAdapter;
import com.example.eventease2.EventListFragment;
import com.example.eventease2.Organizer.OrganizerAttendeeListArrayAdapter;
import com.example.eventease2.Organizer.OrganizerAttendeeListFragment;
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

/**
 * Fragment for the Attendee List page
 * recieves event and organizer ID's from intent
 * gets attendee ID and Names from Firebase and sends it to attendeeArrayAdapter to be displayed
 * has click listener for back button sending it to the Event List page
 * @Author Ashlyn
 */
public class AdminAttendeeView extends AppCompatActivity {

    ListView attendeeList;
    ArrayList<String> attendeeNameList;

    AdminAttendeeListArrayAdapter attendeeArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_attendee_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventID = getIntent().getStringExtra("ID");
        String organizerID = getIntent().getStringExtra("OrganizerID");
        String profile_pic = getIntent().getStringExtra("ProfilePicture");
        String email = getIntent().getStringExtra("Email");
        String phone = getIntent().getStringExtra("Phone");

        attendeeList = findViewById(R.id.organizer_attendee_list);

        CollectionReference attendeeRef = db.collection("Organizer").document(organizerID).collection("Events").document(eventID).collection("Attendees");
        ArrayList<String> attendeeIDs = new ArrayList<>();                                                                                                                           //replace with event ID
        ArrayList<String> attendeeNames = new ArrayList<>();
        attendeeRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Access each document here
                            Log.d("NewTag", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            String checkInsStr = documentSnapshot.getString("Number of Check ins:");
                            if (checkInsStr != null) {
                                int checkIns = Integer.parseInt(checkInsStr);
                                if (checkIns >= 1) {
                                    // Only include attendees who have checked in at least once
                                    attendeeIDs.add(documentSnapshot.getId());
                                    attendeeNames.add(documentSnapshot.getString("Name"));
                                }
                            }
                        }
                        attendeeArrayAdapter = new AdminAttendeeListArrayAdapter(AdminAttendeeView.this, attendeeIDs, attendeeNames, eventID, organizerID,profile_pic, email, phone );
                        attendeeList.setAdapter(attendeeArrayAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NewTag", "Error getting documents.", e);
                    }
                });

        TextView back = findViewById(R.id.back_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the AppEvent activity
                Intent intent = new Intent(AdminAttendeeView.this, AppEventsActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
//        attendeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedAttendeeID = attendeeIDs.get(position);
//                String selectedAttendeeName = attendeeNames.get(position);
//                Intent intent = new Intent(AdminAttendeeView.this, EditProfileActivity.class);
//                intent.putExtra("ID", selectedAttendeeID);
//                intent.putExtra("Name", selectedAttendeeName);
//                startActivity(intent);
//            }
//        });

    }


}