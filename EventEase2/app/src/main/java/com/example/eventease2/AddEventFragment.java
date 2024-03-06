package com.example.eventease2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the class to add a event into the event list
 */
public class AddEventFragment extends AppCompatActivity {
    private ImageView imageView;
    private TextView eventNameView;
    private TextView descriptionView;
    private  TextView locationView;
    private CheckBox isAbleLocationTrackingView;
    private EditText durationView;

    private Button generateButton;

    private static final int REQUEST_IMAGE_PICK = 1;
    private CollectionReference eventsRef;
    private FirebaseFirestore db;
    /**
     * Define a launcher for picking images
     */
    ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the picked image URI here
                if (uri != null) {
                    // Set the selected file to the image view
                    imageView.setImageURI(uri);
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_page);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("Events");

        imageView = findViewById(R.id.imageButton);
        eventNameView = findViewById(R.id.editTextText);
        descriptionView = findViewById(R.id.editTextText2);
        locationView = findViewById(R.id.editTextText3);
        isAbleLocationTrackingView = findViewById(R.id.enable_location_checkbox);
        durationView = findViewById(R.id.editTextText4);
        generateButton = findViewById(R.id.button2);

        //get the event info to make an event
        String eventName = eventNameView.getText().toString();
        String description = descriptionView.getText().toString();
        String location = locationView.getText().toString();
        String duration = durationView.getText().toString();
        Boolean isAbleLocationTracking = isAbleLocationTrackingView.isChecked();
        Bitmap qrCode = OrganizerQRCodeMaker.generateQRCode("https://console.firebase.google.com/u/0/project/syntaxsquad-1d644/firestore/data/~2FEvents~2Fnewevent");

        db.collection("collections").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> collectionNames = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                // Get the name of each collection and add it to the list
                                String collectionName = document.getId();
                                collectionNames.add(collectionName);
                            }
                            Log.d(TAG, "Collections: " + collectionNames);
                        } else {
                            Log.d(TAG, "Error getting collections: ", task.getException());
                        }
                    }
                });

        // let the user click to upload an image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        //generate qr code
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(qrCode);
//                Event event = new Event(imageView , eventName, description, location, isAbleLocationTracking, duration, qrCode);
//                eventsRef.add(event);
//                HashMap<String, Event> data = new HashMap<>();
//                data.put("Event", event);
//                eventsRef.document("newEvent").set(data);
            }
        });
        //real time update
//        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshots,
//                                @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e(TAG, "Error getting documents: ", error);
//                    return;
//                }
//                for (QueryDocumentSnapshot doc : querySnapshots) {
//                    Log.d(TAG, doc.getId() + " => " + doc.getData());
//                }
//            }
//        });


    }

    /**
     * Call the launcher to select image
     */
    void selectImage() {
        pickImageLauncher.launch("image/*");
    }


}
