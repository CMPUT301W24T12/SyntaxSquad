package com.example.eventease2.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventease2.Administrator.AdminArrayAdapter;
import com.example.eventease2.Event;
import com.example.eventease2.EventListArrayAdapter;
import com.example.eventease2.EventListFragment;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class AdminEventView extends AppCompatActivity {


    ListView eventList;
    AdminArrayAdapter adminListArrayAdapter;
    ArrayList<Event> eventDataList;
    AdminArrayAdapter adminArrayAdapter;
    private TextView eventNameText;
    private EditText eventTitle;
    private Button eventDetailsButton;
    private Button eventAttendeeButton;
    private Button backButton;
    private ListView eventListView;
    private CollectionReference eventIdRefrence;
    private ImageView imageEvent;

    ArrayList<String> organizerList;
    ArrayList<String> eventNameList;
    ArrayList<String> eventInfoList;
    ArrayList<String> eventIDs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

        eventList = findViewById(R.id.event_list);

        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();

        FirebaseFirestore appDb = FirebaseFirestore.getInstance();
        ArrayList<Event> eventsIDs = new ArrayList<>();
        CollectionReference collectionRef = appDb.collection("Organizer");

        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

                                // Get the name of the event
                                String name = eventSnapshot.getString("Name");

                                organizerList.add(organizerId);
                                eventNameList.add(name);
                                eventInfoList.add(description);
                                eventIDs.add(eventId);
                            }
                            iterateThroughLists();
                            adminListArrayAdapter = new AdminArrayAdapter(AdminEventView.this, eventNameList, eventInfoList, organizerList, eventIDs);
                            eventList.setAdapter(adminListArrayAdapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failures
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG here", "Hello there i am here");
            }
        });
    }
    public void iterateThroughLists() {
        // Check if all lists have the same size
        if (organizerList.size() == eventNameList.size() &&
                eventNameList.size() == eventInfoList.size() &&
                eventInfoList.size() == eventIDs.size()) {

            for (int i = 0; i < organizerList.size(); i++) {
                String organizer = organizerList.get(i);
                String eventName = eventNameList.get(i);
                String eventInfo = eventInfoList.get(i);
                String eventId = eventIDs.get(i);

                // Perform some action with the elements from each list
                Log.d("Event Info", "Organizer: " + organizer + ", Name: " + eventName + ", Info: " + eventInfo + ", ID: " + eventId);
            }
        } else {
            Log.e("iterateThroughLists", "ArrayLists have different sizes");
        }
    }


}
