package com.example.eventease2.Administrator;

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

public class EditProfileActivity extends AppCompatActivity {
    FirebaseFirestore appDb = FirebaseFirestore.getInstance();

    TextView attendeeName;
    TextView attendeeBio;

    TextView email;
    TextView phone;
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
        setContentView(R.layout.activity_edit_profile);


        attendeeName = findViewById(R.id.textView2);
        attendeeBio = findViewById(R.id.textView3);
        email = findViewById(R.id.editTextText2);
        phone = findViewById(R.id.editTextText3);
        removePic = findViewById(R.id.remove_pic);
        removeProfile = findViewById(R.id.remove_profile);
        profile_pic = findViewById(R.id.imageView2);
        int profilePicResId = getIntent().getIntExtra("ProfilePicture", originalProfilePicResId);
        profile_pic.setImageResource(profilePicResId);
        // Retrieve data from intent extras
        attendeeName.setText(getIntent().getStringExtra("Name"));
        attendeeID = getIntent().getStringExtra("ID");
        eventID = getIntent().getStringExtra("EventID");
        organizerID = getIntent().getStringExtra("OrganizerID");
//        email.setText(getIntent().getStringExtra("Email"));
//        phone.setText(getIntent().getStringExtra("Phone"));
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
                StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilepics/" + attendeeID + ".jpg");

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String emailStr = document.getString("Email");
                        String phoneStr = document.getString("Phone");
                        String bioStr = document.getString("Bio");
                        email.setText(emailStr);
                        phone.setText(phoneStr);
                        attendeeBio.setText(bioStr);

                        // Download the profile picture from Firebase Storage
                        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Load the image into the ImageView
                                Glide.with(EditProfileActivity.this)
                                        .load(uri)
                                        .into(profile_pic);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                                Log.e("EditProfileActivity", "Failed to download profile picture: " + e.getMessage());
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
        removePic.setOnClickListener(v -> {
            profile_pic.setImageResource(originalProfilePicResId);

            StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilepics/" + attendeeID + ".jpg");

            profilePicRef.delete().addOnSuccessListener(aVoid -> {
                Log.d("EditProfileActivity", "onSuccess: deleted file");
            }).addOnFailureListener(exception -> {
                // Uh-oh, an error occurred!
                Log.d("EditProfileActivity", "onFailure: did not delete file");
            });
        });
        removeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventInfoDoc.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("EditProfileActivity", "DocumentSnapshot successfully deleted!");
                                // After successful deletion, go back to the attendee list page
                                Intent intent = new Intent(EditProfileActivity.this, AdminAttendeeView.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("EditProfileActivity", "Error deleting document", e);
                            }
                        });
            }
        });
    }
}