package com.example.eventease2.Attendee;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * Shows the user a default event if the QR scanner hasn't been scanned and shows the scanned event
 * if the scan was successful
 * @author Sean
 */
public class AttendeeEventFragment extends Fragment {
    private String eventID;
    private String  organizerID;
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventBody;
    private FirebaseFirestore appDb;
    private DocumentReference eventRef;

    public String getEventID() {
        return eventID;
    }

    AttendeeEventFragment(String event, String organizer){
        this.eventID = event;
        this.organizerID = organizer;
    }
    AttendeeEventFragment(){
        this.eventID = "default";
        this.organizerID = "default";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_attendee_event, container, false);

        eventTitle = (TextView)view.findViewById(R.id.eventTitle);
        eventTitle.setText("Event Title");

        eventDescription = (TextView)view.findViewById(R.id.eventDescription);
        eventDescription.setText("Event Description");

        eventBody = (TextView)view.findViewById(R.id.eventBody);
        eventBody.setText("Event Body Example");

        appDb = FirebaseFirestore.getInstance();

        if(!Objects.equals(eventID, "default")) {

            eventRef = appDb.collection("Organizer").document(organizerID)
                    .collection("Events").document(eventID);

            eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    eventTitle = (TextView) view.findViewById(R.id.eventTitle);
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String eventName = String.valueOf(document.getData().get("Name"));
                            String eventDescript = String.valueOf(document.getData().get("Description"));
                            String eventMainBody = String.valueOf(document.getData().get("EventBody"));
                            //TODO: add implementation for image.
                            eventTitle.setText(eventName);
                            eventDescription.setText(eventDescript);
                            eventBody.setText(eventMainBody);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        return view;
    }
}
