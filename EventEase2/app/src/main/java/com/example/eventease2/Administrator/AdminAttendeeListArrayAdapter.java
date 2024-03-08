package com.example.eventease2.Administrator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventease2.R;

import java.util.List;

public class AdminAttendeeListArrayAdapter extends ArrayAdapter<String> {
    private List<String> attendeeNames;
    private Context context;

    public AdminAttendeeListArrayAdapter(Context context, List<String> attendeeNames) {
        super(context, 0, attendeeNames);
        this.attendeeNames = attendeeNames;
        this.context = context;
    }

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

        return view;
    }
}
