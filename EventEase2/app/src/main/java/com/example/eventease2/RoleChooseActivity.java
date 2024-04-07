package com.example.eventease2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.eventease2.Attendee.AttendeeStartActivity;

import com.example.eventease2.Administrator.AppEventsActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Opening page of app, User selects their role
 * gets instance of Firebase and imei
 * Each role has an icon with a click listener sending them to their respective acitivty after clicking confirm
 */
import java.util.List;

/**
 * RoleChooseActivity is the opening activity of the application where users can select their role.
 * Depending on the selected role (organizer, administrator, or attendee), the user will be directed
 * to the respective activity after confirmation.
 *
 * This activity initializes Firebase Firestore instance, retrieves IMEI (device number) using DeviceInfoUtils,
 * and handles click events for icons representing different roles. It also fetches data from Firestore
 * based on the selected role.
 *
 * The class contains methods to initialize UI elements, set onClickListeners for icons, handle icon click events,
 * and fetch data from Firestore for attendees and administrators.
 *
 */

public class RoleChooseActivity extends AppCompatActivity {

    ImageButton organizerIcon;
    ImageButton admIcon;
    ImageButton attendeeIcon;
    Button confirmButton;
    FirebaseFirestore appDb;
    public String imei;
    private DocumentReference organizerRef;
    private CollectionReference collectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choose);

        // Initialize Firebase Firestore instance
        initializeFirebaseFirestore();

        // Get IMEI device number
        getIMEINumber();

        // Initialize UI elements
        initializeUI();

        // Set onClickListeners for icons
        setIconClickListeners();
    }

    /**
     * Initializes the Firebase Firestore instance.
     */
    private void initializeFirebaseFirestore() {
        appDb = FirebaseFirestore.getInstance();
    }

    /* Retrieves IMEI (device number) using DeviceInfoUtils.*/
    private void getIMEINumber() {
        imei = DeviceInfoUtils.getIMEI(getApplicationContext());
        Log.d("IMEI", imei);
    }

    /**
     * Initializes UI elements such as icons and buttons.
     */
    private void initializeUI() {
        organizerIcon = findViewById(R.id.orgIcon);
        admIcon = findViewById(R.id.admIcon);
        attendeeIcon = findViewById(R.id.attendIcon);
        confirmButton = findViewById(R.id.confirmButton);
    }

    /**
     *  Sets onClickListeners for icons.
     */
    private void setIconClickListeners() {
        attendeeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAttendeeIconClick();
            }
        });

        organizerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOrganizerIconClick();
            }
        });

        admIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAdmIconClick();
            }
        });
    }

    /**
     *  Handles click on the attendee icon.
     */
    private void handleAttendeeIconClick() {
        confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoleChooseActivity.this, AttendeeStartActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Handles click on the organizer icon.
     */
    private void handleOrganizerIconClick() {
        confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoleChooseActivity.this, EventListFragment.class);
                intent.putExtra("OrganizerID", imei);
                startActivity(intent);
            }
        });
    }

    /**
     *  Handles click on the administrator icon.
     */
    private void handleAdmIconClick() {
        confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AppEventsActivity.class);
                startActivity(intent);
            }
        });
    }
}