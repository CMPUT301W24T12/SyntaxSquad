package com.example.eventease2.Administrator;
import com.bumptech.glide.request.RequestOptions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
/**
 * This activity allows an administrator to edit attendee profile information,
 * including name, bio, email, and phone number. The administrator can also
 * remove the attendee's profile picture and profile entirely.
 */
public class EditProfileActivity extends AppCompatActivity {
    private FirebaseFirestore appDb = FirebaseFirestore.getInstance();
    private TextView attendeeName, attendeeBio, email, phone;
    private Button removePic, removeProfile;
    private ImageView profile_pic;
    private int originalProfilePicResId = R.drawable.ellipse_9;
    private String attendeeID, eventID, organizerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeViews();

        retrieveIntentData();

        if (eventID == null || organizerID == null) {
            handleNullEventOrOrganizer();
            return;
        }

        setupEventInfoDocument();

        loadProfilePicture();

        removePic.setOnClickListener(this::onRemovePicClicked);
        removeProfile.setOnClickListener(this::onRemoveProfileClicked);

        setBackButtonListener();
    }

    /**
     * Initializes all the views used in the activity.
     */
    private void initializeViews() {
        attendeeName = findViewById(R.id.textView2);
        attendeeBio = findViewById(R.id.textView3);
        email = findViewById(R.id.editTextText2);
        phone = findViewById(R.id.editTextText3);
        removePic = findViewById(R.id.remove_pic);
        removeProfile = findViewById(R.id.remove_profile);
        profile_pic = findViewById(R.id.imageView2);
    }

    /**
     * Retrieves intent data passed to the activity.
     */
    private void retrieveIntentData() {
        attendeeName.setText(getIntent().getStringExtra("Name"));
        attendeeID = getIntent().getStringExtra("ID");
        eventID = getIntent().getStringExtra("EventID");
        organizerID = getIntent().getStringExtra("OrganizerID");
    }

    /**
     * Handles the case when eventID or organizerID is null.
     */
    private void handleNullEventOrOrganizer() {
        Log.e("EditProfileActivity", "EventID or OrganizerID is null");
        finish();
    }

    /**
     * Sets up the document reference for the attendee's data.
     */
    private void setupEventInfoDocument() {
        DocumentReference eventInfoDoc = appDb.collection("Organizer")
                .document(organizerID)
                .collection("Events")
                .document(eventID)
                .collection("Attendees")
                .document(attendeeID);

        fetchAttendeeData(eventInfoDoc);
    }

    /**
     * Fetches attendee data from Firestore.
     *
     * @param eventInfoDoc The document reference for the attendee's data.
     */
    private void fetchAttendeeData(DocumentReference eventInfoDoc) {
        eventInfoDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    updateUIWithAttendeeData(document);
                } else {
                    Log.d("EditProfileActivity", "No such document");
                }
            } else {
                Log.d("EditProfileActivity", "get failed with ", task.getException());
            }
        });
    }

    /**
     * Updates the UI with the fetched attendee data.
     *
     * @param document The document snapshot containing attendee data.
     */
    private void updateUIWithAttendeeData(DocumentSnapshot document) {
        String emailStr = document.getString("Email");
        String phoneStr = document.getString("Phone");
        String bioStr = document.getString("Bio");
        email.setText(emailStr);
        phone.setText(phoneStr);
        attendeeBio.setText(bioStr);
    }

    /**
     * Loads the profile picture of the attendee from Firebase Storage.
     */
    private void loadProfilePicture() {
        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilepics");
        profilePicRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference itemRef : listResult.getItems()) {
                if (itemRef.getName().startsWith(attendeeID)) {
                    loadImageIntoImageView(itemRef);
                    break;
                }
            }
        }).addOnFailureListener(exception -> {
            Log.d("EditProfileActivity", "Error: " + exception.getMessage());
        });
    }

    /**
     * Loads the image into the image view using Glide.
     *
     * @param itemRef The storage reference of the profile picture.
     */
    private void loadImageIntoImageView(StorageReference itemRef) {
        itemRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profile_pic);
        }).addOnFailureListener(e -> {
            Log.e("EditProfileActivity", "Failed to download profile picture: " + e.getMessage());
            profile_pic.setImageResource(originalProfilePicResId);
        });
    }

    /**
     * Handles the event when the remove picture button is clicked.
     *
     * @param v The view that was clicked.
     */
    private void onRemovePicClicked(View v) {
        profile_pic.setImageResource(originalProfilePicResId);

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilepics/" + attendeeID + ".jpg");
        profilePicRef.delete().addOnSuccessListener(aVoid -> {
            Log.d("EditProfileActivity", "onSuccess: deleted file");
            profile_pic.setImageResource(originalProfilePicResId);
        }).addOnFailureListener(exception -> {
            Log.d("EditProfileActivity", "onFailure: did not delete file");
        });
    }

    /**
     * Handles the event when the remove profile button is clicked.
     *
     * @param v The view that was clicked.
     */
    private void onRemoveProfileClicked(View v) {
        DocumentReference eventInfoDoc = appDb.collection("Organizer")
                .document(organizerID)
                .collection("Events")
                .document(eventID)
                .collection("Attendees")
                .document(attendeeID);

        eventInfoDoc.delete().addOnSuccessListener(aVoid -> {
            Log.d("EditProfileActivity", "DocumentSnapshot successfully deleted!");
            navigateToAttendeeView();
        }).addOnFailureListener(e -> {
            Log.w("EditProfileActivity", "Error deleting document", e);
        });
    }

    /**
     * Navigates to the attendee view activity after profile removal.
     */
    private void navigateToAttendeeView() {
        Intent intent = new Intent(this, AdminAttendeeView.class);
        intent.putExtra("ID", eventID);
        intent.putExtra("OrganizerID", organizerID);
        startActivity(intent);
        finish();
    }


    /**
     * Sets up the back button listener to navigate back to the attendee view.
     */
    private void setBackButtonListener() {
        TextView back = findViewById(R.id.profile_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, AdminAttendeeView.class);
                intent.putExtra("ID", eventID);
                intent.putExtra("OrganizerID", organizerID);
                startActivity(intent);
                finish();
            }
        });
    }
}