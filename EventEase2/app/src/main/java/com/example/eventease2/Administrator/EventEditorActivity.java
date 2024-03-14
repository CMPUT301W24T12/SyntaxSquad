package com.example.eventease2.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventease2.EventListFragment;
import com.example.eventease2.Organizer.AddEventFragment;
import com.example.eventease2.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventEditorActivity extends AppCompatActivity {
    FirebaseFirestore appDb = FirebaseFirestore.getInstance();

    TextView eventDesciption;
    Button deleteEvent;
    String eventID;
    String organizerID;
    DocumentReference eventInfoDoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_editor);

        eventDesciption = findViewById(R.id.event_detail);
        deleteEvent = findViewById(R.id.event_remove_profile_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve data from intent extras
        eventID = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");

        eventInfoDoc = appDb.collection("Organizer").document(organizerID).collection("Events").document(eventID);

        deleteEvent.setOnClickListener(v -> {
            // Delete the event document
            eventInfoDoc.delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EventEditorActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EventEditorActivity.this, AppEventsActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Log.w("TAG", "Error deleting document", e));
        });
    }
}