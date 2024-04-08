package com.example.eventease2.Administrator;
import com.bumptech.glide.request.RequestOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Adapter for Administrator Attendee List Fragment
 * receives information from fragment and displays it
 * @author Ashlyn Benoy
 */
public class AdminAttendeeListArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> attendeeIDs;
    private ArrayList<String> attendeeNames;
    private String eventID;
    private String organizerID;
    private String profile_pic;
    private String email;
    private String phone;
    private Context context;
    /**
     * Constructor for AdminAttendeeListArrayAdapter.
     * @param context The context in which the adapter will be used.
     * @param attendeeIDs The list of attendee IDs.
     * @param attendeeNames The list of attendee names.
     * @param eventID The ID of the event.
     * @param organizerID The ID of the organizer.
     * @param profile_pic The profile picture of the attendee.
     * @param email The email of the attendee.
     * @param phone The phone number of the attendee.
     */
    public AdminAttendeeListArrayAdapter(Context context, ArrayList<String> attendeeIDs, ArrayList<String> attendeeNames, String eventID, String organizerID, String profile_pic, String email, String phone) {
        super(context, 0, attendeeIDs);
        this.attendeeIDs = attendeeIDs;
        this.attendeeNames = attendeeNames;
        this.context = context;
        this.eventID = eventID;
        this.organizerID = organizerID;
        this.profile_pic = profile_pic;
        this.email = email;
        this.phone = phone;
    }
    /**
     * Get a View that displays the data at the specified position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.attendees_content, parent, false);
        }

        String name = attendeeNames.get(position);
        TextView attendeeName = view.findViewById(R.id.attendee_name);
        attendeeName.setText(name);

        // Load profile picture from Firebase Storage
        ImageView attendeePicture = view.findViewById(R.id.attendeePortrait);
        String fileNameWithExtension = attendeeIDs.get(position) + ".jpg";
        String fileNameWithoutExtension = attendeeIDs.get(position);
        StorageReference profilePicRefWithExtension = FirebaseStorage.getInstance().getReference().child("profilepics/" + fileNameWithExtension);
        StorageReference profilePicRefWithoutExtension = FirebaseStorage.getInstance().getReference().child("profilepics/" + fileNameWithoutExtension);

        profilePicRefWithExtension.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Load the image into the ImageView with extension
                        Glide.with(context)
                                .load(uri)
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.frame_4)
                                .error(R.drawable.frame_4)
                                .into(attendeePicture);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If failed to load with extension, try without extension
                        profilePicRefWithoutExtension.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Load the image into the ImageView without extension
                                        Glide.with(context)
                                                .load(uri)
                                                .apply(RequestOptions.circleCropTransform())
                                                .placeholder(R.drawable.frame_4)
                                                .error(R.drawable.frame_4)
                                                .into(attendeePicture);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Load default image (ellipse_9) when no profile picture found
                                        Glide.with(context)
                                                .load(R.drawable.ellipse_9)
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(attendeePicture);
                                    }
                                });
                    }
                });

        attendeeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAttendeeID = attendeeIDs.get(position);
                String selectedAttendeeName = attendeeNames.get(position);
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.putExtra("ID", selectedAttendeeID);
                intent.putExtra("Name", selectedAttendeeName);
                intent.putExtra("OrganizerID", organizerID);
                intent.putExtra("EventID", eventID);
                intent.putExtra("Email", email);
                intent.putExtra("Phone", phone);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        return view;
    }
}