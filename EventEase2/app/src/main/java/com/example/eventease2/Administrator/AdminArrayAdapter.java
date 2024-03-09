package com.example.eventease2.Administrator;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventease2.Event;
import com.example.eventease2.R;

import java.util.ArrayList;

public class AdminArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public AdminArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list, parent, false);
        }
        Event currentEvent = events.get(position);
        TextView eventName = view.findViewById(R.id.textView);

        //currentEvent.setEventName(eventName.toString());
//
//        Button eventDetailsButton = view.findViewById(R.id.event_details);
//        Button eventAttendeesButton = view.findViewById(R.id.view_attendees);
//        eventDetailsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "event "+currentEvent.getEventName(), Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(context, AdminEditEventFragment.class);
////                context.startActivity(intent);
//            }
//        });
//        eventAttendeesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "attendee", Toast.LENGTH_SHORT).show();
//                //Intent i = new Intent(AdminArrayAdapter.this,AdminE)
//            }
//        });
        return view;
    }
}
