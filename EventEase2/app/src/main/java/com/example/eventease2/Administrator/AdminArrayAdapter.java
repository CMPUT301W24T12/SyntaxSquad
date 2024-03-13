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

public class AdminArrayAdapter extends ArrayAdapter<String> {

    private ArrayList<String> eventNames;
    private ArrayList<String> eventDescription;
    private ArrayList<String> organizerID;
    private  ArrayList<String> eventIDs;
    private Context context;


    public AdminArrayAdapter(Context context, ArrayList<String> eventNames, ArrayList<String> eventDescription, ArrayList<String> organizerID, ArrayList<String> eventIDs) {
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
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event_list, parent, false);
        }

        TextView eventNameView = view.findViewById(R.id.event_title);
        TextView eventDetailsView = view.findViewById(R.id.event_description);
        eventNameView.setText(eventNames.get(position));
        eventDetailsView.setText(eventDescription.get(position));

        return view;
    }
}

