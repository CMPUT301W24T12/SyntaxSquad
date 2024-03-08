package com.example.eventease2.Organizer;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.EventListFragment;
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

import java.util.HashMap;

/**A frame for the organizer
 * Show the event info
 */
public class OrganizerEventFrame extends AppCompatActivity {
    private ImageView imageView;
    private EditText descriptionView;
    private EditText eventTitleView;
    private EditText eventBodyView;
    private  Button editButton;
    private Button backButton;
    private Button doneButton;
    private String id;
    private String organizerID;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    String eventTitle;
    String description;

    String eventBody;
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

        doneButton = findViewById(R.id.done_button);
        doneButton.setEnabled(false);

        backButton = findViewById(R.id.back_button);
        editButton = findViewById(R.id.edit_button);
        imageView = findViewById(R.id.imageView2);
        descriptionView = findViewById(R.id.Description);
        eventBodyView = findViewById(R.id.editTextText2);
        eventTitleView = findViewById(R.id.eventTitle);
        id = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();


        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + id);
        DocumentReference documentReference = db.collection("Organizer").document(organizerID).collection("Events").document(id);
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

                            //set the URI
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Now you have the URI of the image
                                    image=uri;
                                    String uriString = uri.toString();
                                    Log.d("URI",uriString);
                                    // Use this imageURL as needed, for example, you can load it into an ImageView using a library like Glide or Picasso
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors that occurred while getting the download URL
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToEventList();
            }
        });
        /**
         * Let the user confirm to make the change
         */
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton.setEnabled(true);
            }
        });

        /**
         * Commit the change of the details of the event
         */
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                HashMap<String, Object> data = new HashMap<>(); // Note the change in the type of the HashMap

                // Put the list into the HashMap
                data.put("Name", eventTitle);
                data.put("Description", description);
                data.put("EventBody", eventBody);

                CollectionReference newRef = db.collection("Organizer").document(organizerID).collection("Events");
                newRef.document(id).update(data);

                StorageReference imageRef = storageRef.child("images/" + id);
                imageRef.putFile(image);

                intentToEventList();
            }
        });
    }
    /**
     * Call the launcher to select image
     */
    void selectImage() {
        pickImageLauncher.launch("image/*");
    }

    /**
     * Get all info for the event from the plain text boxes
     */
    void getInfo(){
        //get the event info to make an event
        eventTitle = eventTitleView.getText().toString();
        description = descriptionView.getText().toString();
        eventBody = eventBodyView.getText().toString();
    }

    /**
     * Intent back to the event list page
     */
    void intentToEventList(){
        Intent intent = new Intent(getApplicationContext(), EventListFragment.class);
        intent.putExtra("ID",id);
        intent.putExtra("OrganizerID",organizerID);
        startActivity(intent);
    }
}


