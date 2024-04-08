package com.example.eventease2.Organizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

/**
 * Fragment for the Attendee List page
 * recieves event and organizer ID's from intent
 * gets attendee ID and Names from Firebase and sends it to OrganizerAttendeeArrayAdapter to be displayed
 * has click listener for back button sending it to the Event List page
 * Contains Send Notification button to send notification to all attendees checked in
 * and has a set milestone button that alerts the user if they have reached a certain number of
 * attendees signed up. Can click on attendee list view that direct the user to the attendees profile
 * @Author Adeel Khan
 */
public class OrganizerAttendeeListFragment extends AppCompatActivity {

    ListView attendeeList;
    ArrayList<String> attendeeNameList;
    OrganizerAttendeeListArrayAdapter attendeeArrayAdapter;
    private String organizerID;
    private String eventID;
    private int count = 0; // initialize milestone
    private Boolean milestone = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_attendee_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        eventID = getIntent().getStringExtra("EventID");
        organizerID = getIntent().getStringExtra("OrganizerID");

        attendeeList = findViewById(R.id.organizer_attendee_list);

        CollectionReference attendeeRef = db.collection("Organizer").document(organizerID).collection("Events").document(eventID).collection("Attendees");
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
                            if (documentSnapshot.getString("Number of Check ins:") != null && !documentSnapshot.getString("Number of Check ins:").equals("0")) {
                                attendeeIDs.add(documentSnapshot.getId());
                                attendeeNames.add(documentSnapshot.getString("Name"));
                            }
                        }
                        attendeeArrayAdapter = new OrganizerAttendeeListArrayAdapter(OrganizerAttendeeListFragment.this, attendeeIDs, attendeeNames, organizerID, eventID);
                        attendeeList.setAdapter(attendeeArrayAdapter);

                        if (attendeeIDs.size() != 0 && attendeeIDs.size() >= count && milestone == Boolean.TRUE) {
                            Toast.makeText(OrganizerAttendeeListFragment.this, "Milestone reached!", Toast.LENGTH_SHORT).show();
                        }
                        // Click listener on List to send to Attendee profile

                        attendeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(OrganizerAttendeeListFragment.this, OrganizerAttendeeProfileFragment.class);
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

        Button milestone = findViewById(R.id.button5);
        milestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMilestoneDialog();
            }
        });

        Button notifications = findViewById(R.id.button4);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(OrganizerAttendeeListFragment.this, OrganizerNotificationFragment.class);
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
                Intent intent = new Intent(OrganizerAttendeeListFragment.this, OrganizerSignUpFragment.class);
                intent.putExtra("OrganizerID", organizerID);
                intent.putExtra("EventID", eventID);
                // Start the new activity
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Method to create dialog builder to get milestone information from user
     * Gives option to cancel builder or set the milestone
     * If the milestone is reached a toast message will appear for the user
     * The milestone is reached if the number of signed up attendees is equal or greater than the milestone
     */
    private void setMilestoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrganizerAttendeeListFragment.this);
        builder.setTitle("Set Milestone");
        builder.setMessage("Enter the milestone number:");

        // Set up the input field
        final EditText input = new EditText(OrganizerAttendeeListFragment.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String milestoneStr = input.getText().toString();
                if (!milestoneStr.isEmpty()) {
                    count = Integer.parseInt(milestoneStr);
                    milestone = Boolean.TRUE;
                    Toast.makeText(OrganizerAttendeeListFragment.this, "Milestone set to " + count, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
