package com.example.eventease2.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

    public static AppData appData;
    Button showMoreButton;

    // Keep track of the number of events initially displayed
    private int initiallyDisplayedCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_page);

        eventList = findViewById(R.id.event_list);
        showMoreButton = findViewById(R.id.see_more_button);

        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        participantCountList = new ArrayList<>();

        refreshEventData();

        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increase the count to show more events
                initiallyDisplayedCount += 10;
                notifyDataAdapter();
            }
        });
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

                appData = new AppData();
                appData.setOrganizerList(organizerList);
                appData.setEventNameList(eventNameList);
                appData.setEventInfoList(eventInfoList);
                appData.setEventIDs(eventIDs);
                appData.setParticipantCountList(participantCountList);
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
        if (adminListArrayAdapter == null) {
            adminListArrayAdapter = new AppEventAdapter(AppEventsActivity.this, eventNameList, eventInfoList, organizerList, eventIDs, participantCountList, initiallyDisplayedCount);
            eventList.setAdapter(adminListArrayAdapter);
        } else {
            // Update the data in the adapter with the new event lists
            ArrayList<String> newEventNames = new ArrayList<>(adminListArrayAdapter.getEventNames());
            ArrayList<String> newEventDescriptions = new ArrayList<>(adminListArrayAdapter.getEventDescription());
            ArrayList<String> newOrganizerIDs = new ArrayList<>(adminListArrayAdapter.getOrganizerID());
            ArrayList<String> newEventIDs = new ArrayList<>(adminListArrayAdapter.getEventIDs());
            ArrayList<String> newParticipantCountList = new ArrayList<>(adminListArrayAdapter.getParticipantCountList());

            // Add the newly fetched event data to the existing data
            newEventNames.addAll(eventNameList.subList(adminListArrayAdapter.getCount(), eventNameList.size()));
            newEventDescriptions.addAll(eventInfoList.subList(adminListArrayAdapter.getCount(), eventInfoList.size()));
            newOrganizerIDs.addAll(organizerList.subList(adminListArrayAdapter.getCount(), organizerList.size()));
            newEventIDs.addAll(eventIDs.subList(adminListArrayAdapter.getCount(), eventIDs.size()));
            newParticipantCountList.addAll(participantCountList.subList(adminListArrayAdapter.getCount(), participantCountList.size()));

            // Update the adapter with the combined data
            adminListArrayAdapter.updateData(newEventNames, newEventDescriptions, newOrganizerIDs, newEventIDs, newParticipantCountList);
            adminListArrayAdapter.setInitiallyDisplayedCount(initiallyDisplayedCount); // Add this line
        }

        // Show or hide the "Show More" button based on the remaining events
        if (initiallyDisplayedCount < eventNameList.size()) {
            showMoreButton.setVisibility(View.VISIBLE);
        } else {
            showMoreButton.setVisibility(View.GONE);
        }
    }



    private void logAppDataInfo(AppData appData) {
        ArrayList<String> organizerList = appData.getOrganizerList();
        ArrayList<String> eventNameList = appData.getEventNameList();
        ArrayList<String> eventInfoList = appData.getEventInfoList();
        ArrayList<String> eventIDs = appData.getEventIDs();
        ArrayList<String> participantCountList = appData.getParticipantCountList();

        // Log information for each event
        for (int i = 0; i < eventNameList.size(); i++) {
            Log.d("AppData Info", "Event " + (i + 1) + ":");
            Log.d("AppData Info", "Organizer: " + organizerList.get(i));
            Log.d("AppData Info", "Event Name: " + eventNameList.get(i));
            Log.d("AppData Info", "Event Info: " + eventInfoList.get(i));
            Log.d("AppData Info", "Event ID: " + eventIDs.get(i));
            Log.d("AppData Info", "Participant Count: " + participantCountList.get(i));
            Log.d("AppData Info", "---------------------------");
        }
    }
}

