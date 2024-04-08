package com.example.eventease2.Attendee;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.example.eventease2.RoleChooseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Shows the user a default event if the QR scanner hasn't been scanned and shows the scanned event
 * if the scan was successful
 * @author Sean
 */
public class AttendeeEventFragment extends Fragment {
    private String eventID;
    private String  organizerID,eventEntries;
    ArrayList<String> organizerList;
    ArrayList<String> eventNameList;
    ArrayList<String> eventInfoList;
    ArrayList<String> eventIDs;
    ArrayList<String> maxAttendeeList,entriesAttendeeList;
    ListView eventList;
    AttendeeEventAdapter attendeeListArrayAdapter;
    public static AttendeeAppData attendeeAppData;
    private AttendeeItemViewModel viewModel;

    public String getEventID() {
        return eventID;
    }

    public AttendeeEventFragment(String event, String organizer){
        this.eventID = event;
        this.organizerID = organizer;
    }
    AttendeeEventFragment(){
        this.eventID = "default";
        this.organizerID = "default";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.attendee_event_page, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AttendeeItemViewModel.class);
        eventList = view.findViewById(R.id.attendee_event_list);

        TextView back = view.findViewById(R.id.header);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the AppEvent activity
                Intent intent = new Intent(getActivity(), RoleChooseActivity.class);
                startActivity(intent);
            }
        });

        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        maxAttendeeList = new ArrayList<>();
        entriesAttendeeList = new ArrayList<>();
        refreshEventData();


        return view;
    }
    public void refreshEventData() {
        clearEventData();
        FirebaseFirestore appDb = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = appDb.collection("Organizer");
        collectionRef.orderBy("OrganizerID");
        fetchOrganizers(collectionRef, appDb);
    }
    private void clearEventData() {
        organizerList.clear();
        eventNameList.clear();
        eventInfoList.clear();
        eventIDs.clear();
        maxAttendeeList.clear();
        entriesAttendeeList.clear();
    }
    private void fetchOrganizers(CollectionReference collectionRef, FirebaseFirestore appDb) {
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot organizerSnapshot : queryDocumentSnapshots) {
                    String organizerId = organizerSnapshot.getId();
                    CollectionReference eventsCollectionRef = appDb.collection("Organizer").document(organizerId).collection("Events");
                    eventsCollectionRef.orderBy("Name");
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
                    String description = eventSnapshot.getString("Description");
                    String name = eventSnapshot.getString("Name");
                    String maxAttendees;

                    if(eventSnapshot.get("Max") != null) {
                        maxAttendees= eventSnapshot.get("Max").toString();
                    }else {
                        maxAttendees ="0";
                    }
                    organizerList.add(eventSnapshot.getReference().getParent().getParent().getId());
                    eventNameList.add(name);
                    eventInfoList.add(description);
                    eventIDs.add(eventId);
                    maxAttendeeList.add(maxAttendees);
                }
                attendeeAppData = new AttendeeAppData();
                attendeeAppData.setOrganizerList(organizerList);
                attendeeAppData.setEventNameList(eventNameList);
                attendeeAppData.setEventInfoList(eventInfoList);
                attendeeAppData.setEventIDs(eventIDs);
                attendeeAppData.setMaxAttendeeList(maxAttendeeList);
                //attendeeAppData.setEntriesAttendeeList(entriesAttendeeList);
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
        attendeeListArrayAdapter = new AttendeeEventAdapter(getActivity().getApplicationContext(),
                attendeeAppData, viewModel.getAttendeeID(), viewModel.getProfileName(), viewModel.getProfilePhone(),
                viewModel.getProfileEmail());
        eventList.setAdapter(attendeeListArrayAdapter);
    }
}
