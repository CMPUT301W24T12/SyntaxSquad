package com.example.eventease2;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This is the class to add a event into the event list
 */
public class AddEventFragment extends AppCompatActivity {
    private ImageView imageView;
    private Uri imageURI;
    private TextView eventNameView;
    private TextView descriptionView;
    private  TextView locationView;
    private CheckBox isAbleLocationTrackingView;
    private EditText durationView;
    private Button generateButton;
    private String id;
    private String organizerID;
    private static final int REQUEST_IMAGE_PICK = 1;
    private DocumentReference eventsRef;

    private  CollectionReference collectionRef;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    TelephonyManager tm;

    String imei;
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
                    imageURI = uri;
                }
            }
    );
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_page);

        // Copyright 2020 M. Fadli Zein
        imei = DeviceInfoUtils.getIMEI(getApplicationContext()); // device number


        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        collectionRef = db.collection("EventEase");
        eventsRef = collectionRef.document("Organizer");

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
                DocumentReference documentReference = db.collection("EventEase").document("Organizer").collection("b77fd05e-2ff1-445e-8187-33cbb3fc0f53").document("ThisEvent");
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve the value of the field by its name
                                String eventName = document.getString("Name");
                                Log.d( "Name: ", eventName);
                                if (eventName != null) {
                                    // Do something with the eventName
                                } else {
                                    Log.d( "Name: ", "Not found");
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

            }
        });

        //generate qr code
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.setImageBitmap(qrCode);
//                Event event = new Event(imageView , eventName, description, location, isAbleLocationTracking, duration, qrCode);
//                eventsRef.add(event);
                final String randomKey = UUID.randomUUID().toString();
                final String randomKey2 = UUID.randomUUID().toString();
                id = randomKey;
                organizerID = randomKey2;

                HashMap<String, Object> data = new HashMap<>(); // Note the change in the type of the HashMap

                // Add Lists of attendees name, phoneNumbers, emails
                List<String> emailList = new ArrayList<>();
                emailList.add("email1");

                List<String> phoneList = new ArrayList<>();
                phoneList.add("phone1");

                List<String> nameList = new ArrayList<>();
                nameList.add("name1");

                // Put the list into the HashMap
                data.put("Name", "name");
                data.put("ID", id);
                data.put("Location", "location");
                data.put("Description", "description");
                data.put("Duration", "duration");
                data.put("IsAbleLocationTracking", false);
                data.put("EmailList", emailList);
                data.put("PhoneList", phoneList);
                data.put("NameList", nameList);
                CollectionReference newRef = eventsRef.collection(organizerID);
                newRef.document(id).set(data);

                StorageReference imageRef = storageRef.child("images/" + id);
                imageRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddEventFragment.this,"Success",Toast.LENGTH_LONG).show();

                        // Create a new instance of the fragment
                        EventFragment eventFragment = new EventFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, eventFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEventFragment.this,"Fail",Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        //real time update
//        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot querySnapshots,
//                                @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Firestore", error.toString());
//                    return;
//                }
//                if (querySnapshots != null) {
//                    cityDataList.clear();
//                    for (QueryDocumentSnapshot doc: querySnapshots) {
//                        String city = doc.getId();
//                        String province = doc.getString("Province");
//                        Log.d("Firestore", String.format("City(%s, %s) fetched", city,
//                                province));
//                        cityDataList.add(new City(city, province));
//                    }
//                    cityArrayAdapter.notifyDataSetChanged();
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
