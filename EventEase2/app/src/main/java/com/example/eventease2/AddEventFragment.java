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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import java.io.ByteArrayOutputStream;
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

    String eventName;
    String description;
    String location;
    String duration;
    Boolean isAbleLocationTracking;
    private String id;
    private String organizerID;
    private static final int REQUEST_IMAGE_PICK = 1;
    private DocumentReference eventsRef;

    private  CollectionReference collectionRef;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public TelephonyManager tm;

    public String imei;
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
        Log.d("IMEI", imei);

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

        final String randomKey = UUID.randomUUID().toString();
        id = randomKey;
        organizerID = imei;
        Bitmap qrCode = OrganizerQRCodeMaker.generateQRCode(id);

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
                getInfo();
                Log.d("ANAME",eventName);

                HashMap<String, Object> data = new HashMap<>(); // Note the change in the type of the HashMap

                // Add Lists of attendees name, phoneNumbers, emails
                List<String> emailList = new ArrayList<>();
                emailList.add("email1");

                List<String> phoneList = new ArrayList<>();
                phoneList.add("phone1");

                List<String> nameList = new ArrayList<>();
                nameList.add("name1");

                List<String> attendeeList = new ArrayList<>();
                attendeeList.add("attendee1");

                // Put the list into the HashMap
                data.put("Name", eventName);
                data.put("ID", id);
                data.put("Location", location);
                data.put("Description", description);
                data.put("Duration", duration);
                data.put("IsAbleLocationTracking", isAbleLocationTracking);
                data.put("EmailList", emailList);
                data.put("PhoneList", phoneList);
                data.put("NameList", nameList);
                data.put("AttendeeList", attendeeList);
                CollectionReference newRef = eventsRef.collection(organizerID);
                newRef.document(id).set(data);

                StorageReference imageRef = storageRef.child("images/" + id);
                StorageReference qrRef = storageRef.child("QRCode/" + id);

                // Convert QR code bitmap to byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                qrCode.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] qrCodeByteArray = baos.toByteArray();

                // Upload QR code to Firebase Storage
                qrRef.putBytes(qrCodeByteArray);
                imageRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddEventFragment.this,"Success",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), OrganizerEventFrame.class);
                        intent.putExtra("ID",id);
                        intent.putExtra("OrganizerID",organizerID);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEventFragment.this,"Fail",Toast.LENGTH_LONG).show();
                    }
                });

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
        eventName = eventNameView.getText().toString();
        description = descriptionView.getText().toString();
        location = locationView.getText().toString();
        duration = durationView.getText().toString();
        isAbleLocationTracking = isAbleLocationTrackingView.isChecked();
    }

}
