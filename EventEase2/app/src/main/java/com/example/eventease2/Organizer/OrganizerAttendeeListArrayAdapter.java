package com.example.eventease2.Organizer;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Adapter for Organizer Attendee List Fragment
 * revies information from fragment and displays it
 * @author Adeel Khan
 */
public class OrganizerAttendeeListArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> attendeeIDs;
    private ArrayList<String> attendeeNames;
    private Context context;
    private String organizerID;
    private String eventID;
    FirebaseFirestore appDb = FirebaseFirestore.getInstance();
    DocumentReference eventInfoDoc;
    private String attendeeID;
    int profilePicResId = R.drawable.ellipse_9;
    private String name = null;

    public OrganizerAttendeeListArrayAdapter(Context context, ArrayList<String> attendeeIDs, ArrayList<String> attendeeNames, String organizerID, String eventID) {
        super(context, 0, attendeeIDs);
        this.context = context;
        this.attendeeIDs = attendeeIDs;
        this.attendeeNames = attendeeNames;
        this.organizerID = organizerID;
        this.eventID = eventID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.attendees_content, parent, false);
        }

        if (!attendeeIDs.isEmpty()) {
            String name = attendeeNames.get(position);
        }

        TextView attendeeName = view.findViewById(R.id.attendee_name);
        if (name != null && !name.isEmpty()) {
            attendeeName.setText(name);
        }

        ImageView attendeePicture = view.findViewById(R.id.attendeePortrait);

        attendeeID = attendeeIDs.get(position);

        Log.d("OALAA", organizerID);
        Log.d("OALAA", eventID);
        Log.d("OALAA", attendeeID);
        eventInfoDoc = appDb.collection("Organizer")
                .document(organizerID)
                .collection("Events")
                .document(eventID)
                .collection("Attendees")
                .document(attendeeID);

        eventInfoDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profilepics");
                profilePicRef.listAll().addOnSuccessListener(listResult -> {
                    for (StorageReference itemRef : listResult.getItems()) {
                        // Check if the item name starts with attendeeID
                        if (itemRef.getName().startsWith(attendeeID)) {
                            itemRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Load the image into the ImageView
                                Glide.with(OrganizerAttendeeListArrayAdapter.this.getContext())
                                        .load(uri)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(attendeePicture);
                            }).addOnFailureListener(e -> {
                                // Handle any errors
                                Log.e("OALAA", "Failed to download profile picture: " + e.getMessage());
                                attendeePicture.setImageResource(profilePicResId);
                            });
                            break; // Found the file with the right ID, so no need to continue the loop
                        }
                    }
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                    Log.d("OALAA", "Error: " + exception.getMessage());
                });
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Download the profile picture from Firebase Storage
                        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Load the image into the ImageView
                                Glide.with(OrganizerAttendeeListArrayAdapter.this.getContext())
                                        .load(uri)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(attendeePicture);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                                Log.e("OALAA", "Failed to download profile picture: " + e.getMessage());
                                attendeePicture.setImageResource(profilePicResId);

                            }
                        });
                    } else {
                        Log.d("OALAA", "No such document");
                    }
                } else {
                    Log.d("OALAA", "get failed with ", task.getException());
                }
            }
        });

        return view;
    }
}