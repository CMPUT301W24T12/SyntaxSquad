package com.example.eventease2.Organizer;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventease2.R;

public class OrganizerAttendeeListArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> attendeeNames;
    private Context context;

    public OrganizerAttendeeListArrayAdapter(Context context, ArrayList<String> attendeeNames) {
        super(context, 0, attendeeNames);
        this.attendeeNames = attendeeNames;
        this.context = context;
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

        return view;
    }
}