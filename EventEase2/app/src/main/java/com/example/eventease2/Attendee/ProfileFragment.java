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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button attendeeSaveChanges;

    private ArrayAdapter<Attendee> attendeeAdapter;
    private FirebaseFirestore appDb;

    private DocumentReference orgainzerRef;
    private DocumentReference attendeeRef;
    private ArrayList<Attendee> attendeeList;
    private EditText attendeeNameText;
    private EditText attendeePhoneText;
    private EditText attendeeEmailText;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDb = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        CollectionReference collectionReference = orgainzerRef.collection("NewEvent");
        attendeeRef = collectionReference.document("TestEvent");
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

                final String attendeeName = attendeeNameText.getText().toString();
                final String attendeePhone = attendeePhoneText.getText().toString();
                final String attendeeEmail = attendeeEmailText.getText().toString();

                attendeeRef.update("EmailList",FieldValue.arrayUnion(attendeeEmail));
                attendeeRef.update("NameList",FieldValue.arrayUnion(attendeeName));
                attendeeRef.update("PhoneList",FieldValue.arrayUnion(attendeePhone));
//
//                WriteBatch batch = appDb.batch();
//                HashMap<String, String> userInfo = new HashMap<>();
//                Attendee attendee = new Attendee(attendeeName,attendeePhone,attendeeEmail);
//                userInfo.put("Phone",attendeePhone);
//                userInfo.put("Email",attendeeEmail);
//
////                collectionReference.document(attendee.getName())
////                        .set(userInfo)
////                        .addOnSuccessListener(new OnSuccessListener<Void>() {
////                            @Override
////                            public void onSuccess(Void unused) {
////                                Log.d("Firestore", "DocumentSnapshot successfully written!");
////                            }
////                        });

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