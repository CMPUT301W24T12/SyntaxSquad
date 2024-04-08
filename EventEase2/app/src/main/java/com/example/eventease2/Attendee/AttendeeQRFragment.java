package com.example.eventease2.Attendee;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease2.R;
import com.example.eventease2.RoleChooseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Objects;

/**
 * The AttendeeQRFragment class is responsible for managing the QR code scanning functionality
 * for attendees. It allows users to scan a QR code to check-in to an event and provides
 * relevant event details upon successful scanning.
 * <p>
 * The class handles the initiation of the QR code scanner, processing scanned data,
 * and sending relevant information to the ViewModel for Firebase interaction. It also
 * manages Firebase connections for retrieving event details and attendee check-in operations.
 * <p>
 * Additionally, it subscribes the attendee to event-specific topics for receiving notifications
 * related to the subscribed event.
 *
 * @author Sean
 */
public class AttendeeQRFragment extends Fragment {
    private AttendeeItemViewModel viewModel;
    private boolean flag = false;
    private boolean noLimit = false;
    private boolean checkedIn;
    private FirebaseFirestore appDb;
    private CollectionReference attendeeCollect;
    private DocumentReference event;
    private int maxAttendees;
    private long currentAttendees;

    public AttendeeQRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_q_r, container, false);
        super.onViewCreated(view, savedInstanceState);
        checkedIn = false;
        viewModel = new ViewModelProvider(requireActivity()).get(AttendeeItemViewModel.class);
        Button btnScanQR = view.findViewById(R.id.btnScanQR);
        TextView back = view.findViewById(R.id.home_qr);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the AppEvent activity
                Intent intent = new Intent(getActivity(), RoleChooseActivity.class);
                startActivity(intent);
            }
        });
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
     *
     * @param scannedData This shows the scanned data to the screen, shows the organizerID and EventID
     */
    private void displayScanResult(String scannedData) {
        // Display a message using a Toast
        //Toast.makeText(getContext(), "Scan Successful: " + scannedData, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sends data to the view model of the organizer ID and the event ID for firebase use.
     *
     * @param scannedData Sends the viewModel so the Attendee fragments can receive information for the firebase
     */
    private void sendDataToModel(String scannedData) {
        String eventIDAppend = "";
        String organizerIDAppend = "";
        //NEed to update information so it sends checks in user at certain event.
        if (scannedData.charAt(0) == '*') {
            for (int i = 1; i < scannedData.length(); i++) {
                if (scannedData.charAt(i) != '#' && !flag) {
                    eventIDAppend += scannedData.charAt(i);
                } else if (scannedData.charAt(i) == '#') {
                    flag = true;
                } else {
                    organizerIDAppend += scannedData.charAt(i);
                }
            }
            viewModel.setEvent(eventIDAppend);
            viewModel.setOrganizer(organizerIDAppend);
            flag = false;
            firebase();
            checkIn();
        } else {
            for (int i = 0; i < scannedData.length(); i++) {
                if (scannedData.charAt(i) != '#' && !flag) {
                    eventIDAppend += scannedData.charAt(i);
                } else if (scannedData.charAt(i) == '#') {
                    flag = true;
                } else {
                    organizerIDAppend += scannedData.charAt(i);
                }
            }
            viewModel.setEvent(eventIDAppend);
            viewModel.setOrganizer(organizerIDAppend);
            flag = false;
            firebase();
            sendToPromotion();
        }

    }

    /**
     * Navigates the user to the AttendeeEventDetailsActivity to view event details and promotions.
     */
    private void sendToPromotion() {
        Intent intent = new Intent(AttendeeQRFragment.this.getActivity(), AttendeeEventDetailsActivity.class);
        intent.putExtra("ID", viewModel.getEvent());

        intent.putExtra("OrganizerID", viewModel.getOrganizer());

        intent.putExtra("AttendeeID", viewModel.getAttendeeID());

        intent.putExtra("AttendeeName", viewModel.getProfileName());

        intent.putExtra("AttendeePhone", viewModel.getProfilePhone());

        intent.putExtra("AttendeeEmail", viewModel.getProfileEmail());

        intent.putExtra("AttendeeCheckInTimes", viewModel.getCheckIN());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        AttendeeQRFragment.this.getActivity().startActivity(intent);
    }

    /**
     * When initial scan is complete, add the current profile to the firebase
     */
    public void checkIn() {
        event = appDb.collection("Organizer").document(viewModel.getOrganizer())
                .collection("Events")
                .document(viewModel.getEvent());
        event.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (document.get("Max") != null) {
                            maxAttendees = Integer.parseInt(document.get("Max").toString());
                        }
                    } else {
                        maxAttendees = -10;
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        Query query = attendeeCollect;
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    currentAttendees = snapshot.getCount();
                    if (noLimit) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("Name", viewModel.getProfileName());
                        data.put("Email", viewModel.getProfileEmail());
                        data.put("Phone", viewModel.getProfilePhone());
                        viewModel.setCheckIN(viewModel.getCheckIN() + 1);
                        data.put("Number of Check ins:", String.valueOf(viewModel.getCheckIN()));
                        attendeeCollect.document(viewModel.getAttendeeID()).set(data);
                        Toast.makeText(getContext(), "Checked In!", Toast.LENGTH_SHORT).show();
                        firebaseMessaging();
                        noLimit = false;
                    } else if (currentAttendees < maxAttendees) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("Name", viewModel.getProfileName());
                        data.put("Email", viewModel.getProfileEmail());
                        data.put("Phone", viewModel.getProfilePhone());
                        viewModel.setCheckIN(viewModel.getCheckIN() + 1);
                        data.put("Number of Check ins:", String.valueOf(viewModel.getCheckIN()));
                        attendeeCollect.document(viewModel.getAttendeeID()).set(data);
                        firebaseMessaging();
                        Toast.makeText(getContext(), "Checked In!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "All Spots Taken!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Check in Failed", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }

        });
        Toast.makeText(getContext(), "Checked In = "+checkedIn, Toast.LENGTH_SHORT).show();
    }

    /**
     * Establishes Firebase connections and retrieves event details for the attendee's current event.
     */
    public void firebase() {
        if (!Objects.equals(viewModel.getEvent(), "")) {
            appDb = FirebaseFirestore.getInstance();

            attendeeCollect = appDb.collection("Organizer").document(viewModel.getOrganizer())
                    .collection("Events")
                    .document(viewModel.getEvent())
                    .collection("Attendees");

            event = appDb.collection("Organizer").document(viewModel.getOrganizer())
                    .collection("Events")
                    .document(viewModel.getEvent());

            event.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Toast.makeText(getContext(), "Going to Task", Toast.LENGTH_SHORT).show();
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if (document.get("Max") != null) {
                                maxAttendees = Integer.parseInt(String.valueOf(document.get("Max")));
                                //Toast.makeText(getContext(), maxAttendees, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "No Limit", Toast.LENGTH_SHORT).show();
                                noLimit = true;
                            }
                        } else
                            Log.d(TAG, "No such document");
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    public void firebaseMessaging(){
        Log.d("Notification Event", String.valueOf(event));
        Toast.makeText(getContext(), "Event: "+ event, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Check in Failed", Toast.LENGTH_SHORT).show();
        FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(event))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Subscribed to topic successfully");
                        Toast.makeText(getContext(), "Subscription Passed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Failed to subscribe to topic: " + task.getException().getMessage());
                        Toast.makeText(getContext(), "Subscription Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
