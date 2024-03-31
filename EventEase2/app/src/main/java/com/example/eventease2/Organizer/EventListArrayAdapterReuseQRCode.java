package com.example.eventease2.Organizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventease2.EventListFragment;
import com.example.eventease2.R;

import java.util.ArrayList;

/**
 * Custom ListArray Adapter for the Event List page
 * uses the event_list.xml to display the events in the Event Page.
 * Recieves information fromEventListFragment
 * @author Adeel Khan + Jimmy
 * @param Context context, ArrayList<String> eventNames, ArrayList<String> eventDescription, String organizerID, ArrayList<String> eventIDs
 * @return view
 */
public class EventListArrayAdapterReuseQRCode extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private String organizerID;
    private ArrayList<String> eventIDs;
    private Context context;
    String eventID;

    public EventListArrayAdapterReuseQRCode(Context context, ArrayList<String> eventNames, ArrayList<String> eventDescription, String organizerID, ArrayList<String> eventIDs) {
        super(context, 0, eventNames);
        this.eventNames = eventNames;
        this.eventDescription = eventDescription;
        this.organizerID = organizerID;
        this.eventIDs = eventIDs;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.reuse_qr_code_content, parent, false);
        }

        String name = eventNames.get(position);
        String description = eventDescription.get(position);
        eventID = eventIDs.get(position);

        TextView eventName = view.findViewById(R.id.event_title);
        TextView eventDetails = view.findViewById(R.id.event_description);
        Button chooseButton = view.findViewById(R.id.chooseButton);

        eventName.setText(name);
        eventDetails.setText(description);

        Button eventInfo = view.findViewById(R.id.event_details);
        eventInfo.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrganizerEventFrame.class);
            intent.putExtra("ID", eventID);
            intent.putExtra("OrganizerID", organizerID);
            context.startActivity(intent);
        });

        Button viewAttendees = view.findViewById(R.id.view_attendees);
        viewAttendees.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrganizerAttendeeListFragment.class);
            intent.putExtra("ID", eventID);
            intent.putExtra("OrganizerID", organizerID);
            context.startActivity(intent);
        });


        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OrganizerWarningDialog warningDialog = new OrganizerWarningDialog();
//                warningDialog.show();
                  showWarningDialog(eventName.getText().toString());
            }
        });

        return view;
    }
    private void showWarningDialog(String eventName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning")
                .setMessage("Are you sure you want to proceed with " + eventName + "?\nYou  may lose the data for all attendees and event details.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Set the result with the string to be delivered back to the previous activity
                        Intent intent = new Intent();
                        intent.putExtra("SelectedID", eventID);
                        ((Activity) context).setResult(Activity.RESULT_OK, intent);
                        AddEventFragment.updateEventID(eventID);
                        // Finish the current activity
                        ((Activity) context).finish();
                    }
                })
                .setNegativeButton("Cancel", null) // Do nothing if "No" is clicked
                .show();
    }
}
