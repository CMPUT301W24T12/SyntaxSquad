package com.example.eventease2.Administrator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
    ImageView eventImg;
    FirebaseStorage storage;
    Button removeImg;

    public static AppData appData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_editor);

        storage = FirebaseStorage.getInstance();
        eventDesciption = findViewById(R.id.event_detail);
        eventTitle = findViewById(R.id.event_title);
        deleteEvent = findViewById(R.id.event_remove_profile_button);
        backInstruct = findViewById(R.id.events_back_button);
        eventImg = findViewById(R.id.event_editable_photo);
        eventPic = findViewById(R.id.event_editable_photo);
        getBackInstruct = findViewById(R.id.events_back_button);
        removeImg = findViewById(R.id.event_remove_photo_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve data from intent extras
        eventID = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");
        posOfEvent = getIntent().getStringExtra("posOfEvent");

        // Get the StorageReference of the image using its ID
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + eventID);

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


        // Download the image data
        imageRef.getBytes(Long.MAX_VALUE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()) {
                    // Convert the byte array to a Bitmap
                    byte[] bytes = task.getResult();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    // Set the Bitmap to the ImageView
                    eventPic.setImageBitmap(bitmap);
                } else {
                    // Handle any errors that occurred while downloading the image
                    Log.e("EventEditorActivity", "Error downloading image: " + task.getException());
                }
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


        removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the StorageReference of the image using its ID
                StorageReference storageRef = storage.getReference();
                StorageReference imageRef = storageRef.child("images/" + eventID);

                // Delete the existing image from Firebase Storage
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Image deleted successfully, now upload the default image to Firebase Storage
                        StorageReference defaultImageRef = storageRef.child("images/" + eventID);
                        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_event);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        defaultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        defaultImageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Default image uploaded successfully, set the ImageView to display the default image
                                eventPic.setImageResource(R.drawable.default_event);
                                Toast.makeText(EventEditorActivity.this, "Image removed successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors that occurred while uploading the default image
                                Log.e("EventEditorActivity", "Error uploading default image: " + e.getMessage());
                                Toast.makeText(EventEditorActivity.this, "Failed to remove image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred while deleting the image
                        Log.e("EventEditorActivity", "Error deleting image: " + e.getMessage());
                        Toast.makeText(EventEditorActivity.this, "Failed to remove image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


}