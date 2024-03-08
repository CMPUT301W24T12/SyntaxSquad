package com.example.eventease2.Attendee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease2.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class AttendeeQRFragment extends Fragment{
    private AttendeeItemViewModel viewModel;
    private boolean flag = false;
    private FirebaseFirestore appDb;
    private CollectionReference attendeeCollect;
    private String event ="";
    private String organizer="";
    public AttendeeQRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_q_r, container, false);
        super.onViewCreated(view,savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(AttendeeItemViewModel.class);
        Button btnScanQR = view.findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(v -> startQRScanner());
        return view;
    }

    private void startQRScanner() {
        // Initialize QR code scanner
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setPrompt("Scan QR Code");

        //Todo:Delete this when QR scanning is fully used all the time
        String scannedData = "d2ea4e72-5d4c-47a6-8c4b-49a442a08a41#ffffffff-8a86-b983-0000-0000380c0fa3";
        sendDataToModel(scannedData);
        event = viewModel.getEvent();
        organizer = viewModel.getOrganizer();
        firebase();
        addAttendeeData();
        //Todo: delete above

        //Can delete when full implementation is done
        integrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();
                displayScanResult(scannedData);
                sendDataToModel(scannedData);
                event = viewModel.getEvent();
                organizer = viewModel.getOrganizer();
                //firebase();
                addAttendeeData();
            } else {
                // Handle if no QR code is found
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void displayScanResult(String scannedData) {
        // Display a message using a Toast
        Toast.makeText(getContext(), "Scan Successful: " + scannedData, Toast.LENGTH_SHORT).show();
    }
    private void sendDataToModel(String scannedData){
        String eventIDAppend ="";
        String organizerIDAppend ="";
        for(int i = 0; i < scannedData.length();i++){
            if(!flag){
                if(scannedData.charAt(i) != '#'){
                    eventIDAppend+=scannedData.charAt(i);
                }else{
                    flag = true;
                }
            }else{
                organizerIDAppend+=scannedData.charAt(i);
            }
        }
        viewModel.setEvent(eventIDAppend);
        viewModel.setOrganizer(organizerIDAppend);
        flag =false;
    }
    public void addAttendeeData(){
        HashMap<String,String> data = new HashMap<>();
        data.put("Name", viewModel.getProfileName());
        data.put("Email", viewModel.getProfileEmail());
        data.put("Phone", viewModel.getProfilePhone());
        attendeeCollect.document(viewModel.getAttendeeID()).set(data);
    }
    public void firebase(){
        if(!Objects.equals(event, "")){
            appDb = FirebaseFirestore.getInstance();

            attendeeCollect = appDb.collection("Organizer").document(organizer)
                    .collection("Events")
                    .document(event)
                    .collection("Attendees");
        }
    }

}
