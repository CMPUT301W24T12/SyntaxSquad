package com.example.eventease2.Administrator;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventease2.R;

/**
 * Adapter for Administrator Attendee List Fragment
 * revies information from fragment and displays it
 * @author Ashlyn Benoy
 */
public class AdminAttendeeListArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> attendeeIDs;
    private ArrayList<String> attendeeNames;
    private String eventID;
    private String profile_pic;
    private String email;
    private String phone;
    private String organizerID;

    private Context context;

    public AdminAttendeeListArrayAdapter(Context context, ArrayList<String> attendeeIDs, ArrayList<String> attendeeNames, String eventID, String organizerID, String profile_pic, String email, String phone
    ) {
        super(context, 0, attendeeIDs);
        this.attendeeIDs = attendeeIDs;
        this.attendeeNames = attendeeNames;
        this.context = context;
        this.eventID = eventID; // save the eventID
        this.organizerID = organizerID; // save the organizerID
        this.profile_pic = profile_pic;
        this.email = email;
        this.phone = phone;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.attendees_content, parent, false);
        }

        String name = attendeeNames.get(position);
        TextView attendeeName = view.findViewById(R.id.attendee_name);
        attendeeName.setText(name);

        ImageView attendeePicture = view.findViewById(R.id.attendeePortrait);
        attendeePicture.setImageResource(R.drawable.frame_4);
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
                intent.putExtra("ProfilePicture", R.drawable.frame_4);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}