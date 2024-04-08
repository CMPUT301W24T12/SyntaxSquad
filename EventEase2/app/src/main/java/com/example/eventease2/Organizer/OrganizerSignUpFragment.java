package com.example.eventease2.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.EventListFragment;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Fragment for the Signed Up page, gets the necessary data for the page from firebase
 * and displays it using OrganizerAttendeeListArrayAdapter. Will only display the attendees name and
 * profile picture if they have signed up for the event. Has a button to take user to the Check In page
 * Can click on attendee list view that direct the user to the attendees profile
 * @author Adeel Khan
 * This class represents the fragment used by organizers to view and manage attendee sign-ups
 * for a specific event. It retrieves attendee information from Firebase Firestore and displays
 * it in a list view. Organizers can view attendee details and navigate to individual attendee
 * profiles.
 * The {@link #onCreate(Bundle)} method initializes the fragment, retrieves attendee data from
 * Firestore, and populates the list view with attendee information. It sets click listeners
 * for each attendee item to navigate to the attendee profile fragment.
 */
public class OrganizerSignUpFragment extends AppCompatActivity {

    ListView attendeeList;
    OrganizerAttendeeListArrayAdapter attendeeArrayAdapter;
    private String organizerID;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_signup_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        eventID = getIntent().getStringExtra("EventID");
        organizerID = getIntent().getStringExtra("OrganizerID");

        attendeeList = findViewById(R.id.organizer_attendee_list);

        // Change to sign-Up list in Firebase
        CollectionReference attendeeRef = db.collection("Organizer").document(organizerID).collection("Events").document(eventID).collection("Attendees");
        ArrayList<String> attendeeIDs = new ArrayList<>();
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
                        attendeeArrayAdapter = new OrganizerAttendeeListArrayAdapter(OrganizerSignUpFragment.this, attendeeIDs, attendeeNames, organizerID, eventID);
                        attendeeList.setAdapter(attendeeArrayAdapter);

                        attendeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(OrganizerSignUpFragment.this, OrganizerAttendeeProfileFragment.class);
                                // Include anything attendee profile may need
                                // intent.putExtra("Name", name)
                                intent.putExtra("OrganizerID", organizerID);
                                intent.putExtra("EventID", eventID);
                                intent.putExtra("ID", attendeeIDs.get(position));
                                intent.putExtra("Name", attendeeNames.get(position));
                                startActivity(intent);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NewTag", "Error getting documents.", e);
                    }
                });

        Button checkedIn = findViewById(R.id.checked_in_button);
        checkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerSignUpFragment.this, OrganizerAttendeeListFragment.class);
                intent.putExtra("OrganizerID", organizerID);
                intent.putExtra("EventID", eventID);
                // Start the new activity
                startActivity(intent);
            }
        });


        TextView back = findViewById(R.id.back_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(OrganizerSignUpFragment.this, EventListFragment.class);
                intent.putExtra("OrganizerID", organizerID);
                intent.putExtra("EventID", eventID);
                // Start the new activity
                startActivity(intent);
                finish();
            }
        });
    }
}
