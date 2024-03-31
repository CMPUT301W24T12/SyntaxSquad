package com.example.eventease2.Organizer;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.DeviceInfoUtils;
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
    private Button backButton;
    private EditText maxLimitView;
    String eventName;
    String description;
    String location;
    String duration;
    Boolean isAbleLocationTracking;

    int maxNumberOfAttendee;
    private String id;
    private String organizerID;
    private static final int REQUEST_IMAGE_PICK = 1;
    private  CollectionReference collectionRef;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

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

        collectionRef = db.collection("Organizer");

        maxNumberOfAttendee = -1;   //negative as default that is no limit
        imageView = findViewById(R.id.attendeeProfileImage);
        backButton = findViewById(R.id.back_button);
        eventNameView = findViewById(R.id.editTextText);
        descriptionView = findViewById(R.id.editTextText2);
        locationView = findViewById(R.id.editTextText3);
        isAbleLocationTrackingView = findViewById(R.id.enable_location_checkbox);
        durationView = findViewById(R.id.editTextText4);
        generateButton = findViewById(R.id.button2);
        maxLimitView = findViewById(R.id.editTextText5);

        final String randomKey = UUID.randomUUID().toString();
        id = randomKey;
        organizerID = imei;
        String combinedID = id+"#"+organizerID;
        String checkInID = "*"+combinedID;
        Bitmap qrCode = OrganizerQRCodeMaker.generateQRCode(combinedID);
        Bitmap checkInQRCode = OrganizerQRCodeMaker.generateQRCode(checkInID);

//        db.collection("collections").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<String> collectionNames = new ArrayList<>();
//                            for (DocumentSnapshot document : task.getResult()) {
//                                // Get the name of each collection and add it to the list
//                                String collectionName = document.getId();
//                                collectionNames.add(collectionName);
//                            }
//                            Log.d(TAG, "Collections: " + collectionNames);
//                        } else {
//                            Log.d(TAG, "Error getting collections: ", task.getException());
//                        }
//                    }
//                });

        // let the user click to upload an image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        //go back to eventList
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventListFragment.class);
                intent.putExtra("ID",id);
                intent.putExtra("OrganizerID",organizerID);
//                startActivity(intent);
                finish();
            }
        });

        //generate qr code
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReuseQRCodeFragment.class);
                //intent.putExtra("ID",id);
                intent.putExtra("OrganizerID",organizerID);
                startActivity(intent);
//                try{
//                    getInfo();
//                    if (maxNumberOfAttendee <= 0) {
//                        // If negative, throw NumberFormatException
//                        throw new NumberFormatException();
//                    }
//
//                    putData();
//
//                    StorageReference imageRef = storageRef.child("images/" + id);
//                    StorageReference qrRef = storageRef.child("QRCode/" + id);
//                    StorageReference checkInRef = storageRef.child("CheckInQRCode/" + id);
//
//                    //check if image uploaded
//                    if (imageURI==null){
//                        int drawableResourceId = R.drawable._920px_the_event_2010_intertitle_svg; // Replace this with the actual resource ID
//                        imageURI = Uri.parse("android.resource://" + getPackageName() + "/" + drawableResourceId);
//                    }
//
//                    putQRCode(qrCode, checkInQRCode,qrRef,checkInRef);
//
//                    imageRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(AddEventFragment.this,"Success",Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(getApplicationContext(), OrganizerEventFrame.class);
//                            intent.putExtra("ID",id);
//                            intent.putExtra("OrganizerID",organizerID);
//                            startActivity(intent);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(AddEventFragment.this,"Fail",Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//                catch (NumberFormatException e){
//                    Toast.makeText(AddEventFragment.this, "Invalid max limit", Toast.LENGTH_LONG).show();
//                }

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
        String maxText = maxLimitView.getText().toString();
        if (maxText.equals("")){    // if the user don't want to input the maximum num of attendee
            maxNumberOfAttendee = Integer.MAX_VALUE;
        } else {
            maxNumberOfAttendee = Integer.parseInt(maxText);
        }
    }

    /**
     * put data to the firebase
     */
    void putData(){
        HashMap<String, Object> data = new HashMap<>(); // Note the change in the type of the HashMap

        // Put the list into the HashMap
        data.put("Max",maxNumberOfAttendee);
        data.put("Name", eventName);
        data.put("ID", id);
        data.put("Location", location);
        data.put("Description", description);
        data.put("Duration", duration);
        data.put("EventBody", "");
        data.put("IsAbleLocationTracking", isAbleLocationTracking);
        DocumentReference docRef = collectionRef.document(organizerID);

        HashMap<String, Object> dataID = new HashMap<>();
        dataID.put("OrganizerID",organizerID);
        CollectionReference newRef = docRef.collection("Events");
        docRef.set(dataID);
        newRef.document(id).set(data);
    }

    /**
     * put QR code to firebase storage
     */
    void putQRCode(Bitmap qrCode, Bitmap checkInQRCode, StorageReference qrRef, StorageReference checkInRef){
        // Convert QR code bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrCode.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] qrCodeByteArray = baos.toByteArray();

        ByteArrayOutputStream check = new ByteArrayOutputStream();
        checkInQRCode.compress(Bitmap.CompressFormat.PNG, 100,check);
        byte[] checkInCodeByteArray = check.toByteArray();

        // Upload QR code to Firebase Storage
        qrRef.putBytes(qrCodeByteArray);
        checkInRef.putBytes(checkInCodeByteArray);
    }
}
