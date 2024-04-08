package com.example.eventease2.Organizer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.DeviceInfoUtils;
import com.example.eventease2.EventListFragment;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * This class represents the functionality to add an event into the event list from the organizer's perspective.
 * It allows organizers to input event details such as name, description, location, start and end time,
 * and maximum number of attendees. Organizers can also upload an image for the event, select start and end times
 * from a spinner, and choose whether to enable location tracking for the event.
 * <p>
 * The {@link #selectImage()} method launches a launcher to select an image for the event.
 * </p>
 * <p>
 * The {@link #getInfo()} method retrieves all event information from the input fields.
 * </p>
 * <p>
 * The {@link #putData()} method puts the event data into Firebase Firestore.
 * </p>
 * <p>
 * The {@link #putQRCode(Bitmap, Bitmap, StorageReference, StorageReference)} method uploads QR codes for the event
 * to Firebase Storage.
 * </p>
 * <p>
 * The {@link #onQRCodeTypeChange(String)} method is called when the type of QR code to use (check-in or promotional)
 * is changed.
 * </p>
 * <p>
 * The {@link #intentTOReuseQRCode()} method directs the user to the section for reusing a QR code to make an event.
 * </p>
 * <p>
 * The {@link #showDatePickerDialog(String)} method displays a DatePickerDialog when the user clicks the button
 * to pick a date.
 * </p>
 * <p>
 * The {@link #updateEventID(String)} method is a static method for other classes to update the event ID.
 * </p>
 */

public class AddEventFragment extends AppCompatActivity implements OrganizerWarningDialog.QRCodeTypeChangeListener {
    private ImageView imageView;
    private Uri imageURI;
    private TextView eventNameView;
    private TextView descriptionView;
    private  TextView locationView;
    private CheckBox isAbleLocationTrackingView;
    private ImageButton startCalender;
    private ImageButton endCalender;
    private Spinner startSpinner;
    private Spinner endSpinner;
    private  TextView startTimeView;
    private TextView endTimeView;
    private Button generateButton;
    private Button backButton;
    private EditText maxLimitView;
    String eventName;
    String description;
    String location;
    String startHour;
    String endHour;
    String startDate;
    String endDate;
    String startTime;
    String endTime;
    Boolean isAbleLocationTracking;

    public String QRCodeType;

    int maxNumberOfAttendee;
    private static String id;
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

        OrganizerWarningDialog warningDialog = new OrganizerWarningDialog();
        warningDialog.show(getSupportFragmentManager(),"Choose existing QR Code");
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
        startSpinner = findViewById(R.id.StartSpinner);
        endSpinner = findViewById(R.id.EndSpinner);
        startCalender = findViewById(R.id.startCalender);
        endCalender = findViewById(R.id.EndCalender);
        generateButton = findViewById(R.id.button2);
        maxLimitView = findViewById(R.id.editTextText5);

        startTimeView = findViewById(R.id.textView10);
        endTimeView = findViewById(R.id.textView11);

        // Define an array of options
        String[] hours = {
                "0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00",
                "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
        };

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, hours);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        startSpinner.setAdapter(adapter);
        endSpinner.setAdapter(adapter);

        final String randomKey = UUID.randomUUID().toString();
        id = randomKey;
        organizerID = imei;
        String combinedID = id+"#"+organizerID;
        String checkInID = "*"+combinedID;
        Bitmap qrCode = OrganizerQRCodeMaker.generateQRCode(combinedID);
        Bitmap checkInQRCode = OrganizerQRCodeMaker.generateQRCode(checkInID);

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

        startSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Perform action based on the selected item
                startHour = (String) parentView.getItemAtPosition(position);
                //Toast.makeText(AddEventFragment.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
        endSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Perform action based on the selected item
                endHour = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        startCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog("start");
            }
        });

        endCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog("end");
            }
        });

        //generate qr code
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Log.d("ID_add",id);
                    getInfo();
                    if (maxNumberOfAttendee <= 0) {
                        // If negative, throw NumberFormatException
                        throw new NumberFormatException();
                    }

                    putData();

                    StorageReference imageRef = storageRef.child("images/" + id);
                    StorageReference qrRef = storageRef.child("QRCode/" + id);
                    StorageReference checkInRef = storageRef.child("CheckInQRCode/" + id);

                    //check if image uploaded
                    if (imageURI==null){
                        int drawableResourceId = R.drawable.default_event; // Replace this with the actual resource ID
                        imageURI = Uri.parse("android.resource://" + getPackageName() + "/" + drawableResourceId);
                    }
                    if (QRCodeType == "new"){
                        putQRCode(qrCode, checkInQRCode,qrRef,checkInRef);
                    }

                    imageRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddEventFragment.this,"Success",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), OrganizerEventFrame.class);
                            intent.putExtra("ID",id);
                            intent.putExtra("OrganizerID",organizerID);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddEventFragment.this,"Fail",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (NumberFormatException e){
                    Toast.makeText(AddEventFragment.this, "Invalid max limit", Toast.LENGTH_LONG).show();
                }
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
        //duration = durationView.getText().toString();
        startTime = startTimeView.getText().toString()+"/"+startHour;
        endTime = endTimeView.getText().toString()+"/"+endHour;
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
        //data.put("Duration", duration);
        data.put("StartTime",startTime);
        data.put("EndTime",endTime);
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

    /**
     * get the type of qr code that is to use a check in qr code or a promotional qr code
     * @param type
     */
    @Override
    public void onQRCodeTypeChange(String type) {
        this.QRCodeType = type;
    }

    /**
     * intent to the section of reusing a qr code to make an event
     */
    @Override
    public void intentTOReuseQRCode() {
        Intent intent = new Intent(getApplicationContext(), ReuseQRCodeFragment.class);
        intent.putExtra("ID",id);
        intent.putExtra("OrganizerID",organizerID);
        startActivity(intent);
    }

    /**
     * show the calendar when the user click the button to pick a date
     * @param time
     */
    private void showDatePickerDialog(String time) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventFragment.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Do something with the selected date
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        //Toast.makeText(AddEventFragment.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                        if (time.equals("start")){
                            startDate = selectedDate;
                            startTimeView.setText(selectedDate);
                        } else if (time.equals("end")){
                            endDate = selectedDate;
                            endTimeView.setText(selectedDate);
                        }
                    }
                }, year, month, day);

        // Show DatePickerDialog
        datePickerDialog.show();
    }

    /**
     *static method for other classes to change the get the id of the event
     * @param id
     */
    public static void updateEventID(String id) {
        AddEventFragment.id = id;
    }
}
