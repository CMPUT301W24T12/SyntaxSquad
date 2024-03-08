package com.example.eventease2.Attendee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.eventease2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.WriteResult;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Button attendeeSaveChanges;

    private ArrayAdapter<Attendee> attendeeAdapter;
    private FirebaseFirestore appDb;

    private DocumentReference orgainzerRef;
    private DocumentReference attendeeRef;
    private ArrayList<Attendee> attendeeList;
    private EditText attendeeNameText;
    private EditText attendeePhoneText;
    private EditText attendeeEmailText;
    private String event;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public ProfileFragment(String eventID) {
        this.event = eventID;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDb = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = appDb.collection("Attendee");
        attendeeList = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        appDb = FirebaseFirestore.getInstance();


        orgainzerRef = appDb.collection("EventEase").document("Organizer");
        CollectionReference collectionReference = orgainzerRef.collection("29bc643d-3a87-4d5d-8716-2b7b6a224d69");
        attendeeRef = collectionReference.document("f24e4939-4cbb-4af7-944d-51fcfdb98855");
        attendeeList = new ArrayList<>();

        attendeeAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_profile, attendeeList);
        // Find the ImageButton by its ID
        attendeeSaveChanges = view.findViewById(R.id.AttendeeAddChanges);
        attendeeNameText = view.findViewById(R.id.editAttendeeName);
        attendeePhoneText = view.findViewById(R.id.editTextPhone2);
        attendeeEmailText = view.findViewById(R.id.editEmailAddress);
        attendeeSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Objects.equals(event, "default")){
                    final String attendeeName = attendeeNameText.getText().toString();
                    final String attendeePhone = attendeePhoneText.getText().toString();
                    final String attendeeEmail = attendeeEmailText.getText().toString();

                    attendeeRef.update("EmailList", FieldValue.arrayUnion(attendeeEmail));
                    attendeeRef.update("NameList", FieldValue.arrayUnion(attendeeName));
                    attendeeRef.update("PhoneList", FieldValue.arrayUnion(attendeePhone));
                }

            }
        });

        return view;

    }

    private void addNewAttendee(Attendee attendee) {
        CollectionReference collectionReference = appDb.collection("Attendee");

        attendeeList.add(attendee);
        attendeeAdapter.notifyDataSetChanged();

        HashMap<String, Attendee> data = new HashMap<>();
        data.put(attendee.getName(), attendee);
        collectionReference.document(attendee.getName()).set(data);
    }

}