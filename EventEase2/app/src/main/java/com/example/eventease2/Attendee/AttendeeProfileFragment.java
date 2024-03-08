package com.example.eventease2.Attendee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
import java.util.Objects;

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
        event = "";
        organizer = "";
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
        View view = inflater.inflate(R.layout.fragment_attendee_profile, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AttendeeItemViewModel.class);
        Attendee user = new Attendee();

//        attendeeList = new ArrayList<>();
//
//        attendeeAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_attendee_profile, attendeeList);
        // Find the ImageButton by its ID
        if(!Objects.equals(event, "")){
            appDb = FirebaseFirestore.getInstance();

            orgainzerRef = appDb.collection("EventEase").document("Organizer");
            CollectionReference collectionReference = orgainzerRef.collection(organizer);
            attendeeRef = collectionReference.document(event);
        }
        getEditTextBoxes(view);
        //Todo:Add a set text for bio and image and geolocation
        attendeeNameText.setText(viewModel.getProfileName());
        attendeePhoneText.setText(viewModel.getProfilePhone());
        attendeeEmailText.setText(viewModel.getProfileEmail());
        attendeeSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(attendeeNameText.getText().toString());
                user.setPhone(attendeePhoneText.getText().toString());
                user.setEmail(attendeeEmailText.getText().toString());

                viewModel.setProfileName(attendeeNameText.getText().toString());
                viewModel.setProfileEmail(attendeeEmailText.getText().toString());
                viewModel.setProfilePhone(attendeePhoneText.getText().toString());
                if(!flag && !Objects.equals(event, "")){
                   addAttendeeData(user);
                   flag =true;
                }else if (!Objects.equals(event, "")){
                    remAttendeeData(user);
                    addAttendeeData(user);
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
    public void addAttendeeData(Attendee user){
        attendeeRef.update("EmailList", FieldValue.arrayUnion(user.getEmail()));
        attendeeRef.update("NameList", FieldValue.arrayUnion(user.getName()));
        attendeeRef.update("PhoneList", FieldValue.arrayUnion(user.getPhone()));
    }
    public void remAttendeeData(Attendee user){
        attendeeRef.update("EmailList", FieldValue.arrayRemove(user.getEmail()));
        attendeeRef.update("NameList", FieldValue.arrayRemove(user.getName()));
        attendeeRef.update("PhoneList", FieldValue.arrayRemove(user.getPhone()));
    }
    public void getEditTextBoxes(View view){
        attendeeSaveChanges = view.findViewById(R.id.AttendeeAddChanges);
        attendeeNameText = view.findViewById(R.id.editProfileName);
        attendeePhoneText = view.findViewById(R.id.editTextPhone2);
        attendeeEmailText = view.findViewById(R.id.editProfileEmail);
    }
    public void getProfileTexts(ViewModel viewModel){

    }

}