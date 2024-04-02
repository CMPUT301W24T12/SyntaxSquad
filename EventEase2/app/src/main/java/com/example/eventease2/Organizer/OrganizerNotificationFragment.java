package com.example.eventease2.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;


public class OrganizerNotificationFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_notification_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventID = getIntent().getStringExtra("EventID");
        String organizerID = getIntent().getStringExtra("OrganizerID");

        Button send = findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout title_header = findViewById(R.id.textInputLayout2);
                EditText title_input = title_header.getEditText();
                String title = null;
                if (title_input != null) {
                    title = title_input.toString();
                }

                TextInputLayout body_header = findViewById(R.id.textInputLayout);
                EditText body_input = body_header.getEditText();
                String body = null;
                if (body_input != null) {
                    body = body_input.toString();
                }

            }
        });

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(OrganizerNotificationFragment.this, OrganizerAttendeeListFragment.class);
                intent.putExtra("OrganizerID", organizerID);
                intent.putExtra("EventID", eventID);
                // Start the new activity
                //startActivity(intent);
                finish();
            }
        });
    }
}
