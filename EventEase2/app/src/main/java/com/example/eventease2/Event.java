package com.example.eventease2;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Collection;

public class Event {
    private ImageView image;
    private String eventName;
    private String description;
    private String location;
    private boolean isAbleLocationTracking;
    private String duration;

    private Collection<String> emailList;
    private Collection<String> nameList;
    private Collection<String> phoneNumberList;
    private Uri imageUri;

    protected Bitmap qrCode;
    protected DocumentReference documentReference;
//    public Event(FirebaseFirestore db){
//        documentReference = db.collection("EventEase").document("Organizer").collection("NewEvent").document("TestEvent");
//
//        Log.d( "Document path: ",documentReference.getPath());
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        // Retrieve the value of the field by its name
//                        eventName = document.getString("Name");
//                        Log.d( "Name: ", eventName);
//                        if (eventName != null) {
//                            // Do something with the eventName
//                        } else {
//                            Log.d( "Name: ", "Not found");
//                        }
//                    } else {
//                        Log.d( "Document: ", "Not found");
//                    }
//                } else {
//                    // Handle errors here
//                    Log.d(TAG, "Error getting document", task.getException());
//                }
//            }
//        });
//    }
    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAbleLocationTracking() {
        return isAbleLocationTracking;
    }

    public void setAbleLocationTracking(boolean ableLocationTracking) {
        isAbleLocationTracking = ableLocationTracking;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

    public Event(ImageView image, String eventName, String description, String location, boolean isAbleLocationTracking, String duration, Bitmap qrCode) {
        this.image = image;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.isAbleLocationTracking = isAbleLocationTracking;
        this.duration = duration;
        this.qrCode = qrCode;
    }
}
