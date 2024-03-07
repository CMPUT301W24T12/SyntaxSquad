package com.example.eventease2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.eventease2.Attendee.AttendeeStartActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoleChooseActivity extends AppCompatActivity {

    ImageButton organizerIcon;
    ImageButton admIcon;
    ImageButton attendeeIcon;
    Button confirmButton;
    FirebaseFirestore appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDb = FirebaseFirestore.getInstance();
        // Firebase contain info of all events on app
        CollectionReference collectionReference = appDb.collection("Events");

        organizerIcon = findViewById(R.id.orgIcon);
        admIcon = findViewById(R.id.admIcon);
        attendeeIcon = findViewById(R.id.attendIcon);
        confirmButton = findViewById(R.id.confirmButton);

        attendeeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RoleChooseActivity.this, "You clicked the Attendee Button", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RoleChooseActivity.this, AttendeeStartActivity.class);
                        startActivity(i);

                    }
                });
            }
        });

        organizerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RoleChooseActivity.this, "You clicked the Organizer Button", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        admIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButton.setTextColor(Color.parseColor("#FFFFFF"));
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(RoleChooseActivity.this, "You clicked the admin Button", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this, city + " was clicked", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}