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
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendeeEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendeeEventFragment extends Fragment {
    private String eventID;
    private String  organizerID;
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventBody;
    private ImageView eventImage;
    private FirebaseFirestore appDb;
    private DocumentReference orgainzerRef;
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
        eventTitle.setText("");

        eventDescription = (TextView)view.findViewById(R.id.eventDescription);
        eventDescription.setText("");

        eventBody = (TextView)view.findViewById(R.id.eventBody);
        eventBody.setText("");
        // Inflate the layout for this fragment
        appDb = FirebaseFirestore.getInstance();
        //eventID = "f24e4939-4cbb-4af7-944d-51fcfdb98855";
        //organizerID = "29bc643d-3a87-4d5d-8716-2b7b6a224d69";
        if(!Objects.equals(eventID, "default")) {

            orgainzerRef = appDb.collection("EventEase").document("Organizer");

            //CollectionReference collectionReference = orgainzerRef.collection("29bc643d-3a87-4d5d-8716-2b7b6a224d69");
            CollectionReference collectionReference = orgainzerRef.collection(organizerID);

            //eventRef = collectionReference.document("f24e4939-4cbb-4af7-944d-51fcfdb98855");
            eventRef = collectionReference.document(eventID);


            eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    eventTitle = (TextView) view.findViewById(R.id.eventTitle);
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String eventName = String.valueOf(document.getData().get("Name"));
                            String eventDescript = String.valueOf(document.getData().get("Description"));
                            //TODO: add implementation for eventBody when Firebase changes. Also image.
                            //String eventMainBody = String.valueOf(document.getData().get("Description"));
                            eventTitle.setText(eventName);
                            eventDescription.setText(eventDescript);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        if(!Objects.equals(eventID, "default")){

            return view;
        }else{
            return view;
        }
    }
}