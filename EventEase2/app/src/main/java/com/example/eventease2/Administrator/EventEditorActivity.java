package com.example.eventease2.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventease2.EventListFragment;
import com.example.eventease2.Organizer.AddEventFragment;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventEditorActivity extends AppCompatActivity {
    FirebaseFirestore appDb = FirebaseFirestore.getInstance();

    TextView eventDesciption;
    TextView eventTitle;
    Button deleteEvent;
    TextView backInstruct;
    String eventID;
    String organizerID;
    String posOfEvent;
    ImageView eventPic;
    DocumentReference eventInfoDoc;
    TextView getBackInstruct;

    public static AppData appData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_editor);

        eventDesciption = findViewById(R.id.event_detail);
        eventTitle = findViewById(R.id.event_title);
        deleteEvent = findViewById(R.id.event_remove_profile_button);
        backInstruct = findViewById(R.id.events_back_button);
        eventPic = findViewById(R.id.event_editable_photo);
        getBackInstruct = findViewById(R.id.events_back_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve data from intent extras
        eventID = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");
        posOfEvent = getIntent().getStringExtra("posOfEvent");

        eventInfoDoc = appDb.collection("Organizer").document(organizerID).collection("Events").document(eventID);

        deleteEvent.setOnClickListener(v -> {
            // Delete the event document
            eventInfoDoc.delete()
                    .addOnSuccessListener(aVoid -> {
                        appData.deleteEventID(Integer.parseInt(posOfEvent));
                        appData.deleteEventInfo(Integer.parseInt(posOfEvent));
                        appData.deleteOrganizer(Integer.parseInt(posOfEvent));
                        appData.deleteEventName(Integer.parseInt(posOfEvent));
                        appData.deleteParticipantCount(Integer.parseInt(posOfEvent));
                        finish();

                    })
                    .addOnFailureListener(e -> Log.w("TAG", "Error deleting document", e));
        });

        getBackInstruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backInstruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Retrieve the document
        eventInfoDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Document found, extract the fields
                        String description = document.getString("Description");
                        String eventBody = document.getString("EventBody");
                        String name = document.getString("Name");

                        // Do something with the extracted information
                        if (description != null && eventBody != null && name != null) {
                            eventDesciption.setText(eventBody);
                            eventTitle.setText(name);

                        } else {
                            // Handle the case if any of the fields are null
                            Log.d("Description", "Description, EventBody, or Name is null");
                        }
                    } else {
                        // Document does not exist
                        Log.d("Description", "Document does not exist");
                    }
                } else {
                    // Error occurred while retrieving document
                    Log.d("Description", "Error getting document: " + task.getException());
                }
            }
        });

    }


}