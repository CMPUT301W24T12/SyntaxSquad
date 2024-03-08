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
import android.widget.Toast;

import com.example.eventease2.Administrator.AdminEventView;
import com.example.eventease2.Organizer.AddEventFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RoleChooseActivity extends AppCompatActivity {

    ImageButton organizerIcon;
    ImageButton admIcon;
    ImageButton attendeeIcon;
    Button confirmButton;
    FirebaseFirestore appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choose);

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
                        Intent intent = new Intent(getApplicationContext(), AddEventFragment.class);
                        startActivity(intent);
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
                        Intent intent = new Intent(getApplicationContext(), AdminEventView.class);
                        startActivity(intent);
                    }
                });
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = db.collection("Organizer").document("ffffffff-8a86-b983-0000-0000380c0fa3").collection("Events");
                collectionRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    // Access each document here
                                    Log.d("NewTag", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("NewTag", "Error getting documents.", e);
                            }
                        });
            }
        });
    }
}
