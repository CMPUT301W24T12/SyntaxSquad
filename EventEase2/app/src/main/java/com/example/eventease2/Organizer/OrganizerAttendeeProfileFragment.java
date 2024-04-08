package com.example.eventease2.Organizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eventease2.Administrator.AppData;
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


import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;


/**
 * This fragments shows the user the empty profile unless saved changes were updated.
 */
public class OrganizerAttendeeProfileFragment extends AppCompatActivity {

    FirebaseFirestore appDb = FirebaseFirestore.getInstance();
    TextView attendeeName;
    TextView attendeeBio;
    TextView email;
    TextView phone;
    TextView checked_in;
    Button removePic;
    Button removeProfile;
    String attendeeID;
    String name;
    String eventID; // Add eventID variable
    String organizerID; // Add organizerID variable
    String posOfEvent;
    DocumentReference eventInfoDoc;
    ImageView profile_pic;
    int originalProfilePicResId = R.drawable.ellipse_9;
    public static AppData appData;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_attendee_profile);


        attendeeName = findViewById(R.id.textView2);
        attendeeBio = findViewById(R.id.textView3);
        email = findViewById(R.id.editTextText2);
        phone = findViewById(R.id.editTextText3);
        checked_in = findViewById(R.id.count_textview);
        profile_pic = findViewById(R.id.imageView2);
        int profilePicResId = getIntent().getIntExtra("ProfilePicture", originalProfilePicResId);
        profile_pic.setImageResource(profilePicResId);
        // Retrieve data from intent extras
        //attendeeName.setText(getIntent().getStringExtra("Name"));
        attendeeID = getIntent().getStringExtra("ID");
        eventID = getIntent().getStringExtra("EventID");
        organizerID = getIntent().getStringExtra("OrganizerID");
        //email.setText(getIntent().getStringExtra("Email"));
        //phone.setText(getIntent().getStringExtra("Phone"));
        profile_pic.setImageResource(profilePicResId);

        if (eventID == null || organizerID == null) {
            Log.e("EditProfileActivity", "EventID or OrganizerID is null");
            // Handle the error or finish the activity
            finish();
            return;
        }

        eventInfoDoc = appDb.collection("Organizer")
                .document(organizerID)
                .collection("Events")
                .document(eventID)
                .collection("Attendees")
                .document(attendeeID);

        eventInfoDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilepics");
                profilePicRef.listAll().addOnSuccessListener(listResult -> {
                    for (StorageReference itemRef : listResult.getItems()) {
                        // Check if the item name starts with attendeeID
                        if (itemRef.getName().startsWith(attendeeID)) {
                            itemRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Load the image into the ImageView
                                Glide.with(OrganizerAttendeeProfileFragment.this)
                                        .load(uri)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(profile_pic);
                            }).addOnFailureListener(e -> {
                                // Handle any errors
                                Log.e("EditProfileActivity", "Failed to download profile picture: " + e.getMessage());
                                profile_pic.setImageResource(profilePicResId);
                            });
                            break; // Found the file with the right ID, so no need to continue the loop
                        }
                    }
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                    Log.d("EditProfileActivity", "Error: " + exception.getMessage());
                });
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String nameStr = document.getString("Name");
                        if (nameStr != null && !nameStr.isEmpty()) {
                            attendeeName.setText(nameStr);
                        }
                        String bioStr = document.getString("Bio");
                        if (bioStr != null && !bioStr.isEmpty()) {
                            attendeeBio.setText(bioStr);
                        }
                        String emailStr = document.getString("Email");
                        if (emailStr != null && !emailStr.isEmpty()) {
                            email.setText(String.format("Email: %s", emailStr));
                        }
                        String phoneStr = document.getString("Phone");
                        if (phoneStr != null && !phoneStr.isEmpty()) {
                            phone.setText(String.format("Phone: %s", phoneStr));
                        }
                        String count = document.getString("Number of Check ins:");
                        if (count != null && !count.isEmpty()) {
                            checked_in.setText(count);
                        }

                        // Download the profile picture from Firebase Storage
                        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Load the image into the ImageView
                                Glide.with(OrganizerAttendeeProfileFragment.this)
                                        .load(uri)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(profile_pic);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                                Log.e("EditProfileActivity", "Failed to download profile picture: " + e.getMessage());
                                profile_pic.setImageResource(profilePicResId);

                            }
                        });
                    } else {
                        Log.d("EditProfileActivity", "No such document");
                    }
                } else {
                    Log.d("EditProfileActivity", "get failed with ", task.getException());
                }
            }
        });

        TextView back = findViewById(R.id.profile_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerAttendeeProfileFragment.this, OrganizerAttendeeListFragment.class);
                intent.putExtra("OrganizerID", organizerID);
                intent.putExtra("EventID", eventID);
                // Start the new activity
                startActivity(intent);
                // Finish the current activity
                finish();
            }
        });
    }
}
