package com.example.eventease2.Administrator;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.example.eventease2.RoleChooseActivity;
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

/**
 * AppEventsActivity displays a list of events for the administrator.
 * It retrieves event data from Firebase Firestore and populates the list accordingly.
 *
 * This activity initializes various UI elements, sets listeners for buttons,
 * refreshes event data from Firestore, fetches organizers and their events,
 * processes event and attendee data, and updates the event list accordingly.
 */
public class AppEventsActivity extends AppCompatActivity {

    // Instance variables
    ListView eventList;
    AppEventAdapter adminListArrayAdapter;
    public ArrayList<String> organizerList;
    public ArrayList<String> eventNameList;
    public ArrayList<String> eventInfoList;
    public ArrayList<String> eventIDs;
    public ArrayList<String> participantCountList;
    TextView backInstruct;
    public static AppData appData;
    Button showMoreButton;
    private int initiallyDisplayedCount = 10;
    public FirebaseFirestore appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_event_page);
        initializeViews();
        setListeners();
        refreshEventData();
    }

    /**
     * Initializes all the required views from the layout file.
     */
    private void initializeViews() {
        eventList = findViewById(R.id.event_list);
        backInstruct = findViewById(R.id.events_back_button);
        showMoreButton = findViewById(R.id.see_more_button);
        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        participantCountList = new ArrayList<>();
    }

    /**
     * Sets click listeners for buttons.
     */
    private void setListeners() {
        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiallyDisplayedCount += 10;
                notifyDataAdapter();
            }
        });

        backInstruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppEventsActivity.this, RoleChooseActivity.class));
            }
        });
    }

    /**
     * Refreshes event data by clearing existing lists and fetching data from Firestore.
     */
    public void refreshEventData() {
        clearEventData();
        appDb = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = appDb.collection("Organizer");
        fetchOrganizers(collectionRef, appDb);
    }

    /**
     * Clears all event data lists.
     */
    private void clearEventData() {
        organizerList.clear();
        eventNameList.clear();
        eventInfoList.clear();
        eventIDs.clear();
        participantCountList.clear();
    }

    /**
     * Fetches organizers from the Firestore database.
     * @param collectionRef Reference to the collection of organizers in Firestore
     * @param appDb Reference to the Firestore database
     */
    public void fetchOrganizers(CollectionReference collectionRef, FirebaseFirestore appDb) {
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

    /**
     * Fetches events for a particular organizer from Firestore.
     * @param eventsCollectionRef Reference to the collection of events for an organizer in Firestore
     */
    private void fetchEventsForOrganizer(CollectionReference eventsCollectionRef) {
        eventsCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot eventQueryDocumentSnapshots) {
                for (QueryDocumentSnapshot eventSnapshot : eventQueryDocumentSnapshots) {
                    processEvent(eventSnapshot, eventsCollectionRef);
                }
            }
        });
    }

    /**
     * Processes an event document snapshot from Firestore.
     * @param eventSnapshot Snapshot of an event document
     * @param eventsCollectionRef Reference to the collection of events for an organizer in Firestore
     */
    private void processEvent(QueryDocumentSnapshot eventSnapshot, CollectionReference eventsCollectionRef) {
        String eventId = eventSnapshot.getId();
        List<String> attendeeList = (List<String>) eventSnapshot.get("AttendeeList");
        int attendeeListLength = attendeeList != null ? attendeeList.size() : 0;
        String description = eventSnapshot.getString("Description");
        String name = eventSnapshot.getString("Name");
        ArrayList<String> attendeeIDs = new ArrayList<>();
        fetchAttendeesForEvent(eventsCollectionRef, eventSnapshot, attendeeIDs, eventId, name, description);
    }

    /**
     * Fetches attendees for a particular event from Firestore.
     * @param eventsCollectionRef Reference to the collection of events for an organizer in Firestore
     * @param eventSnapshot Snapshot of an event document
     * @param attendeeIDs List to store attendee IDs
     * @param eventId ID of the event
     * @param name Name of the event
     * @param description Description of the event
     */
    private void fetchAttendeesForEvent(CollectionReference eventsCollectionRef, QueryDocumentSnapshot eventSnapshot, ArrayList<String> attendeeIDs, String eventId, String name, String description) {
        CollectionReference attendeeRef = eventsCollectionRef.document(eventId).collection("Attendees");
        attendeeRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    processAttendee(documentSnapshot, attendeeIDs);
                }
                updateEventDataLists(eventSnapshot, eventId, name, description, attendeeIDs);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DEBUG here", "Failed to fetch events for organizer");
            }
        });
    }

    /**
     * Processes an attendee document snapshot from Firestore.
     * @param documentSnapshot Snapshot of an attendee document
     * @param attendeeIDs List to store attendee IDs
     */
    private void processAttendee(QueryDocumentSnapshot documentSnapshot, ArrayList<String> attendeeIDs) {
        String checkIns = documentSnapshot.getString("Number of Check ins:");
        if (checkIns != null && Integer.parseInt(checkIns) > 0) {
            attendeeIDs.add(documentSnapshot.getId());
        }
    }

    /**
     * Updates event data lists with information from Firestore.
     * @param eventSnapshot Snapshot of an event document
     * @param eventId ID of the event
     * @param name Name of the event
     * @param description Description of the event
     * @param attendeeIDs List of attendee IDs for the event
     */
    private void updateEventDataLists(QueryDocumentSnapshot eventSnapshot, String eventId, String name, String description, ArrayList<String> attendeeIDs) {
        organizerList.add(eventSnapshot.getReference().getParent().getParent().getId());
        eventNameList.add(name);
        eventInfoList.add(description);
        eventIDs.add(eventId);
        participantCountList.add(String.valueOf(attendeeIDs.size()));
        notifyDataAdapter();
    }

    /**
     * Notifies the data adapter about changes in the data set.
     */
    private void notifyDataAdapter() {
        if (adminListArrayAdapter == null) {
            adminListArrayAdapter = new AppEventAdapter(AppEventsActivity.this, eventNameList, eventInfoList, organizerList, eventIDs, participantCountList, initiallyDisplayedCount);
            eventList.setAdapter(adminListArrayAdapter);
        } else {
            ArrayList<String> newEventNames = new ArrayList<>(adminListArrayAdapter.getEventNames());
            ArrayList<String> newEventDescriptions = new ArrayList<>(adminListArrayAdapter.getEventDescription());
            ArrayList<String> newOrganizerIDs = new ArrayList<>(adminListArrayAdapter.getOrganizerID());
            ArrayList<String> newEventIDs = new ArrayList<>(adminListArrayAdapter.getEventIDs());
            ArrayList<String> newParticipantCountList = new ArrayList<>(adminListArrayAdapter.getParticipantCountList());

            newEventNames.addAll(eventNameList.subList(adminListArrayAdapter.getCount(), eventNameList.size()));
            newEventDescriptions.addAll(eventInfoList.subList(adminListArrayAdapter.getCount(), eventInfoList.size()));
            newOrganizerIDs.addAll(organizerList.subList(adminListArrayAdapter.getCount(), organizerList.size()));
            newEventIDs.addAll(eventIDs.subList(adminListArrayAdapter.getCount(), eventIDs.size()));
            newParticipantCountList.addAll(participantCountList.subList(adminListArrayAdapter.getCount(), participantCountList.size()));

            adminListArrayAdapter.updateData(newEventNames, newEventDescriptions, newOrganizerIDs, newEventIDs, newParticipantCountList);
            adminListArrayAdapter.setInitiallyDisplayedCount(initiallyDisplayedCount);
        }

        if (initiallyDisplayedCount < eventNameList.size()) {
            showMoreButton.setVisibility(View.VISIBLE);
        } else {
            showMoreButton.setVisibility(View.GONE);
        }
    }
}
