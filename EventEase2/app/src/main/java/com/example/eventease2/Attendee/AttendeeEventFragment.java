package com.example.eventease2.Attendee;

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

import com.example.eventease2.Administrator.AppData;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows the user a default event if the QR scanner hasn't been scanned and shows the scanned event
 * if the scan was successful
 * @author Sean
 */
public class AttendeeEventFragment extends Fragment {
    private String eventID;
    private String  organizerID;
    ArrayList<String> organizerList;
    ArrayList<String> eventNameList;
    ArrayList<String> eventInfoList;
    ArrayList<String> eventIDs;
    ArrayList<String> participantCountList;
    ListView eventList;
    AttendeeEventAdapter attendeeListArrayAdapter;
    public static AppData appData;
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
        eventList = view.findViewById(R.id.event_list);

        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        participantCountList = new ArrayList<>();
        refreshEventData();


        return view;
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
                    //List<String> attendeeList = (List<String>) eventSnapshot.get("AttendeeList");
                    //int attendeeListLength = attendeeList != null ? attendeeList.size() : 0;
                    String description = eventSnapshot.getString("Description");
                    String name = eventSnapshot.getString("Name");

                    organizerList.add(eventSnapshot.getReference().getParent().getParent().getId());
                    eventNameList.add(name);
                    eventInfoList.add(description);
                    eventIDs.add(eventId);
                    //participantCountList.add(String.valueOf(attendeeListLength));
                }

                appData = new AppData();
                appData.setOrganizerList(organizerList);
                appData.setEventNameList(eventNameList);
                appData.setEventInfoList(eventInfoList);
                appData.setEventIDs(eventIDs);
                //appData.setParticipantCountList(participantCountList);
//                logAppDataInfo(appData);
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
                appData, viewModel.getAttendeeID(), viewModel.getProfileName(), viewModel.getProfilePhone(),
                viewModel.getProfileEmail());
        eventList.setAdapter(attendeeListArrayAdapter);
    }
}
