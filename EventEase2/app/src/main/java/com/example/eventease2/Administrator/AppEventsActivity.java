package com.example.eventease2.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppEventsActivity extends AppCompatActivity {

    ListView eventList;
    AppEventAdapter adminListArrayAdapter;

    ArrayList<String> organizerList;
    ArrayList<String> eventNameList;
    ArrayList<String> eventInfoList;
    ArrayList<String> eventIDs;
    ArrayList<String> participantCountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

        eventList = findViewById(R.id.event_list);

        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        participantCountList = new ArrayList<>();

        refreshEventData();
    }

    public void refreshEventData() {
        clearEventData();
        FirebaseFirestore appDb = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = appDb.collection("Organizer");
        fetchOrganizers(collectionRef, appDb);
    }

    private void clearEventData() {
        organizerList.clear();
        eventNameList.clear();
        eventInfoList.clear();
        eventIDs.clear();
        participantCountList.clear();
    }

    private void fetchOrganizers(CollectionReference collectionRef, FirebaseFirestore appDb) {
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot organizerSnapshot : queryDocumentSnapshots) {
                    String organizerId = organizerSnapshot.getId();
                    CollectionReference eventsCollectionRef = appDb.collection("Organizer").document(organizerId).collection("Events");
                    fetchEventsForOrganizer(eventsCollectionRef);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG here", "Failed to fetch organizers");
            }
        });
    }

    private void fetchEventsForOrganizer(CollectionReference eventsCollectionRef) {
        eventsCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot eventQueryDocumentSnapshots) {
                for (QueryDocumentSnapshot eventSnapshot : eventQueryDocumentSnapshots) {
                    String eventId = eventSnapshot.getId();
                    List<String> attendeeList = (List<String>) eventSnapshot.get("AttendeeList");
                    int attendeeListLength = attendeeList != null ? attendeeList.size() : 0;
                    String description = eventSnapshot.getString("Description");
                    String name = eventSnapshot.getString("Name");

                    organizerList.add(eventSnapshot.getReference().getParent().getParent().getId());
                    eventNameList.add(name);
                    eventInfoList.add(description);
                    eventIDs.add(eventId);
                    participantCountList.add(String.valueOf(attendeeListLength));
                }
                notifyDataAdapter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG here", "Failed to fetch events for organizer");
            }
        });
    }

    private void notifyDataAdapter() {
        adminListArrayAdapter = new AppEventAdapter(AppEventsActivity.this, eventNameList, eventInfoList, organizerList, eventIDs, participantCountList);
        eventList.setAdapter(adminListArrayAdapter);
    }
}