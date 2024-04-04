package com.example.eventease2.Attendee;

import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Objects;

/**
 * QR fragment is the responsible for showing the button that allows users
 * to scan a QR code after pressing a button.
 * @author Sean
 */
public class AttendeeQRFragment extends Fragment{
    private AttendeeItemViewModel viewModel;
    private boolean flag = false;
    private FirebaseFirestore appDb;
    private CollectionReference attendeeCollect;
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
    /**
     * This function opens the camera app on your phone and detects a QR code.
     */
    private void startQRScanner() {
        // Initialize QR code scanner
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setPrompt("Scan QR Code");

        //Todo:Delete this when QR scanning is fully used all the time
//        String scannedData = "d2ea4e72-5d4c-47a6-8c4b-49a442a08a41#ffffffff-8a86-b983-0000-0000380c0fa3";
//        sendDataToModel(scannedData);
//        event = viewModel.getEvent();
//        organizer = viewModel.getOrganizer();
//        firebase();
//        addAttendeeData();
//        //Todo: delete above

        //Can delete when full implementation is done
        integrator.initiateScan();

    }
    /**
     * When the user scans a QR code, we get the information needed to access the firebase.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();

                //displayScanResult(scannedData);
                sendDataToModel(scannedData);

                //Todo:Uncomment this line below of code in order for normal functionality to work

                //checkIn();
            } else {
                // Handle if no QR code is found
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /**
     * Displays a success toast if updated correctly
     * @param scannedData
     * This shows the scanned data to the screen, shows the organizerID and EventID
     */
    private void displayScanResult(String scannedData) {
        // Display a message using a Toast
        //Toast.makeText(getContext(), "Scan Successful: " + scannedData, Toast.LENGTH_SHORT).show();
    }
    /**
     * Sends data to the view model of the organizer ID and the event ID for firebase use.
     * @param scannedData
     * Sends the viewModel so the Attendee fragments can receive information for the firebase
     */
    private void sendDataToModel(String scannedData){
        String eventIDAppend ="";
        String organizerIDAppend ="";
        //NEed to update information so it sends checks in user at certain event.
        if (scannedData.charAt(0) == '*') {
            for (int i = 1; i < scannedData.length(); i++) {
                if (scannedData.charAt(i) != '#' && !flag) {
                       eventIDAppend += scannedData.charAt(i);
                } else if(scannedData.charAt(i) == '#') {
                    flag = true;
                }else{
                    organizerIDAppend += scannedData.charAt(i);
                }
            }
            viewModel.setEvent(eventIDAppend);
            viewModel.setOrganizer(organizerIDAppend);
            flag = false;
            firebase();
            checkIn();
        }else{
            for (int i = 0; i < scannedData.length(); i++) {
                if (scannedData.charAt(i) != '#' && !flag) {
                    eventIDAppend += scannedData.charAt(i);
                } else if(scannedData.charAt(i) == '#') {
                    flag = true;
                }else{
                    organizerIDAppend += scannedData.charAt(i);
                }
            }
            viewModel.setEvent(eventIDAppend);
            viewModel.setOrganizer(organizerIDAppend);
            flag = false;
            firebase();
            sendToPromotion();
            //send user to the
            // promotional part in the page
        }

    }

    private void sendToPromotion() {
        Intent intent = new Intent(AttendeeQRFragment.this.getActivity(), AttendeeEventDetailsActivity.class);
        intent.putExtra("ID", viewModel.getEvent());

        intent.putExtra("OrganizerID", viewModel.getOrganizer());;

        intent.putExtra("AttendeeID",viewModel.getAttendeeID());

        intent.putExtra("AttendeeName",viewModel.getProfileName());

        intent.putExtra("AttendeePhone",viewModel.getProfilePhone());
        intent.putExtra("AttendeeEmail", viewModel.getProfileEmail());
        intent.putExtra("AttendeeCheckInTimes",viewModel.getCheckIN());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        AttendeeQRFragment.this.getActivity().startActivity(intent);
    }

    /**
     * When initial scan is complete, add the current profile to the firebase
     */
    public void checkIn(){
        HashMap<String,String> data = new HashMap<>();
        data.put("Name", viewModel.getProfileName());
        data.put("Email", viewModel.getProfileEmail());
        data.put("Phone", viewModel.getProfilePhone());
        viewModel.setCheckIN(viewModel.getCheckIN()+1);
        Toast.makeText(this.getActivity(), "viewmodel Organizer: "+viewModel.getOrganizer(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this.getActivity(), "viewmodel Organizer: "+viewModel.getEvent(), Toast.LENGTH_SHORT).show();
        data.put("Number of Check ins:",String.valueOf(viewModel.getCheckIN()));
        attendeeCollect.document(viewModel.getAttendeeID()).set(data);
    }
    /**
     * Firebase connecting function.
     */
    public void firebase(){
        if(!Objects.equals(viewModel.getEvent(), "")){
            appDb = FirebaseFirestore.getInstance();

            attendeeCollect = appDb.collection("Organizer").document(viewModel.getOrganizer())
                    .collection("Events")
                    .document(viewModel.getEvent())
                    .collection("Attendees");
        }
    }


}
