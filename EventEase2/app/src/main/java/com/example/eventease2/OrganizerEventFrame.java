package com.example.eventease2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**A frame for the organizer
 * Show the event info
 */
public class OrganizerEventFrame extends AppCompatActivity {
    private ImageView imageView;
    private TextView descriptionView;
    private TextView eventTitleView;
    private TextView eventBodyView;

    private Button button;
    private String id;
    private String organizerID;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    String eventTitle;
    String description;
    Uri image;
    Bitmap imageBitmap;

    ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the picked image URI here
                if (uri != null) {
                    // Set the selected file to the image view
                    imageView.setImageURI(uri);
                    image = uri;
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_event_frame);

        button = findViewById(R.id.button3);
        imageView = findViewById(R.id.imageView2);
        descriptionView = findViewById(R.id.Description);
        eventBodyView = findViewById(R.id.eventBody);
        eventTitleView = findViewById(R.id.eventTitle);
        id = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();


        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + id);
        DocumentReference documentReference = db.collection("EventEase").document("Organizer").collection(organizerID).document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        // Inside the onComplete method
                        if (document.exists()) {
                            // Retrieve the value of the field by its name
                            eventTitle = document.getString("Name");
                            description = document.getString("Description");
                            Log.d("Title: ", eventTitle);
                            Log.d("Description: ", description);

                            // Set the eventTitleView and descriptionView here
                            eventTitleView.setText(eventTitle);
                            descriptionView.setText(description);

                            // Download the image from Firebase Storage
                            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    // Convert the byte array to a Bitmap
                                    imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                    // Set the Bitmap to the ImageView
                                    imageView.setImageBitmap(imageBitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors that occurred while downloading the image
                                }
                            });
                        } else {
                            Log.d("Document: ", "Not found");
                        }

                    } else {
                        Log.d( "Document: ", "Not found");
                    }
                } else {
                    // Handle errors here
                    Log.d(TAG, "Error getting document", task.getException());
                }
            }
        });
        //imageView.setImageURI(image);
        eventTitleView.setText(eventTitle);
        Log.d( "Title: ", eventTitleView.getText().toString());
        descriptionView.setText(description);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        eventTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        eventBodyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventListFragment.class);
                intent.putExtra("ID",id);
                intent.putExtra("OrganizerID",organizerID);
                startActivity(intent);
            }
        });
    }
    /**
     * Call the launcher to select image
     */
    void selectImage() {
        pickImageLauncher.launch("image/*");
    }
}
