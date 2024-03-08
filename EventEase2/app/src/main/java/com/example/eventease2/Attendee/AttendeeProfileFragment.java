package com.example.eventease2.Attendee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.eventease2.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendeeProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendeeProfileFragment extends Fragment {

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
    private String organizer;
    private AttendeeItemViewModel viewModel;
    private boolean flag = false;
    public AttendeeProfileFragment() {
        // Required empty public constructor
    }

    public AttendeeProfileFragment(String eventID, String organizerID) {
        this.event = eventID;
        this.organizer = organizerID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendee_profile, container, false);
        appDb = FirebaseFirestore.getInstance();
        //event = viewModel.getString();
        Attendee user = new Attendee();
        orgainzerRef = appDb.collection("EventEase").document("Organizer");

        //CollectionReference collectionReference = orgainzerRef.collection("29bc643d-3a87-4d5d-8716-2b7b6a224d69");
        CollectionReference collectionReference = orgainzerRef.collection(organizer);

        //attendeeRef = collectionReference.document("f24e4939-4cbb-4af7-944d-51fcfdb98855");
        attendeeRef = collectionReference.document(event);

        attendeeList = new ArrayList<>();

        attendeeAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_attendee_profile, attendeeList);
        // Find the ImageButton by its ID
        attendeeSaveChanges = view.findViewById(R.id.AttendeeAddChanges);
        attendeeNameText = view.findViewById(R.id.editProfileName);
        attendeePhoneText = view.findViewById(R.id.editTextPhone2);
        attendeeEmailText = view.findViewById(R.id.editProfileEmail);
        attendeeSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag){
                    user.setName(attendeeNameText.getText().toString());
                    user.setPhone(attendeePhoneText.getText().toString());
                    user.setEmail(attendeeEmailText.getText().toString());
                    attendeeRef.update("EmailList", FieldValue.arrayUnion(user.getEmail()));
                    attendeeRef.update("NameList", FieldValue.arrayUnion(user.getName()));
                    attendeeRef.update("PhoneList", FieldValue.arrayUnion(user.getPhone()));
                    flag =true;
                }else{
                    attendeeRef.update("EmailList", FieldValue.arrayRemove(user.getEmail()));
                    attendeeRef.update("NameList", FieldValue.arrayRemove(user.getName()));
                    attendeeRef.update("PhoneList", FieldValue.arrayRemove(user.getPhone()));

                    user.setName(attendeeNameText.getText().toString());
                    user.setPhone(attendeePhoneText.getText().toString());
                    user.setEmail(attendeeEmailText.getText().toString());

                    attendeeRef.update("EmailList", FieldValue.arrayUnion(user.getEmail()));
                    attendeeRef.update("NameList", FieldValue.arrayUnion(user.getName()));
                    attendeeRef.update("PhoneList", FieldValue.arrayUnion(user.getPhone()));
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