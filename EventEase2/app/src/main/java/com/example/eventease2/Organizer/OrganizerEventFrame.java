package com.example.eventease2.Organizer;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**A frame for the organizer
 * Show the event info, and the user is able to to edit the text in this frame
 */
public class OrganizerEventFrame extends AppCompatActivity {
    private ImageView imageView;
    private EditText descriptionView;
    private EditText eventTitleView;
    private EditText eventBodyView;
    private TextView fromView;
    private TextView toView;
    private EditText locationView;
    private  Button editButton;
    private Button share;
    private Button backButton;
    private Button shareButton;
    private Button doneButton;
    private ImageView QRView;
    private String id;
    private String organizerID;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    String eventTitle;
    String description;
    String location;
    String eventBody;
    Uri image;
    Bitmap imageBitmap;
    Bitmap QRBitmap;

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
        //doneButton.setEnabled(false);

        share = findViewById(R.id.share);
        shareButton = findViewById(R.id.share_button);
        backButton = findViewById(R.id.back_button);
        editButton = findViewById(R.id.edit_button);
        imageView = findViewById(R.id.imageView2);
        QRView = findViewById(R.id.imageView6);
        descriptionView = findViewById(R.id.Description);
        eventBodyView = findViewById(R.id.editTextText2);
        eventTitleView = findViewById(R.id.eventTitle);
        fromView = findViewById(R.id.FromView);
        toView = findViewById(R.id.ToView);
        locationView = findViewById(R.id.eventLocation);

        //share.setEnabled(false);

        id = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();


        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + id);
        //StorageReference QRRef = storageRef.child("QRCode/" + id);
        StorageReference CheckInRef = storageRef.child("CheckInQRCode/" + id);
        DocumentReference documentReference = db.collection("Organizer")
                .document(organizerID).collection("Events").document(id);
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
                            eventBody = document.getString("EventBody");
                            location = document.getString("Location");
                            String from="";
                            String to="";
                            try{
                                from = document.getString("StartTime");
                                to =  document.getString("EndTime");
                            }catch (Exception e){
                                Log.d("time","Missing data for Start time or end time");
                            }

                            Log.d("Title: ", eventTitle);
                            Log.d("Description: ", description);
                            Log.d("body",eventBody);

                            // Set the eventTitleView and descriptionView here
                            eventTitleView.setText(eventTitle);
                            descriptionView.setText(description);
                            eventBodyView.setText(eventBody);
                            fromView.setText(from);
                            if(!location.equals("")){
                                locationView.setText(location);
                            }
                            toView.setText(to);

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

                            // Download the QR Code from Firebase Storage
                            CheckInRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    // Convert the byte array to a Bitmap
                                    QRBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                    // Set the Bitmap to the ImageView
                                    QRView.setImageBitmap(QRBitmap);
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton.setVisibility(View.GONE);
                shareButton.setVisibility(View.VISIBLE);
                share.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
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
                doneButton.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.GONE);
                share.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
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
                data.put("Location",location);

                CollectionReference newRef = db.collection("Organizer").document(organizerID).collection("Events");
                newRef.document(id).update(data);

                StorageReference imageRef = storageRef.child("images/" + id);
                imageRef.putFile(image);

                intentToEventList();
            }
        });
        /**
         * Share the qr code to other apps
         */
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent with action ACTION_SEND
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                // Set the MIME type for the content
                shareIntent.setType("image/jpeg");

                // Add the QR code image as an extra to the Intent
                // share the qr code that link to the download page
                Uri imageUri = getImageUri(getApplicationContext(),QRBitmap );
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                // Start the chooser to select an app to share with
                startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
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
        location = locationView.getText().toString();
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

    /**
     * covert the bitmap qr code to image qr code
     * @param context
     * @param bitmap
     * @return the image uri of the qr code
     */
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "QR Code", null);
        return Uri.parse(path);
    }
}



