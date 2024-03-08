package com.example.eventease2.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

public class AdminEditEvent extends AppCompatActivity {

    ImageView editableImage;
    Button eventDeletePhoto;
    Button eventDeleteProfile;
    TextView eventTitle;
    TextView attendeeEventDetails;

    String eventId;
    String eventPhoto; // Assuming you have eventPhoto declared somewhere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_event);

        editableImage = findViewById(R.id.event_editable_photo);
        eventDeletePhoto = findViewById(R.id.event_remove_photo_button);
        eventDeleteProfile = findViewById(R.id.event_remove_profile_button);
        eventTitle = findViewById(R.id.event_title);
        attendeeEventDetails = findViewById(R.id.event_detail);

        // Receive the intent
        Intent intent = getIntent();

        // Check if the intent has extras
        if (intent != null && intent.hasExtra("eventId") && intent.hasExtra("eventName") && intent.hasExtra("eventDetails")) {
            // Retrieve the data from intent extras
            eventId = intent.getStringExtra("eventId");
            String eventName = intent.getStringExtra("eventName");
            String eventDetails = intent.getStringExtra("eventDetails");


            // Now you can use eventId, eventName, and eventDetails as needed
            // For example, set them to TextViews
            eventTitle.setText(eventName);
            attendeeEventDetails.setText(eventDetails);

            eventPhoto = "images/" + eventId;

            eventDeletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(eventPhoto);
                    // Delete the file
                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminEditEvent.this, "Photo deleted successfully", Toast.LENGTH_SHORT).show();
                            int drawableResourceId = R.drawable._920px_the_event_2010_intertitle_svg;
                            Uri imageURI = Uri.parse("android.resource://" + getPackageName() + "/" + drawableResourceId);
                            storageRef.putFile(imageURI);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(AdminEditEvent.this, "Failed to delete photo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            eventDeleteProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Access Firestore instance
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Get a reference to the "Events" collection
                    CollectionReference eventsCollectionRef = db.collection("Events");
                    // Get a reference to the specific event document
                    DocumentReference eventDocRef = eventsCollectionRef.document(eventId);
                    // Delete the event document
                    eventDocRef.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Event deleted successfully
                                    Toast.makeText(AdminEditEvent.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to delete event
                                    Toast.makeText(AdminEditEvent.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else {
            // Handle case when intent extras are missing
            // For example, show a message or navigate back
            finish(); // Finish this activity if data is not available
        }
    }
}
