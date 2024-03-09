package com.example.eventease2.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminAttendeeView extends AppCompatActivity {

    ListView listView;
    ArrayList<Event> eventDataList;
    AdminArrayAdapter adminArrayAdapter;
    private TextView eventNameText;
    private EditText eventTitle;
    private Button eventDetailsButton;
    private Button eventAttendeeButton;
    private Button toastattendee;
    private ListView eventListView;
    private FirebaseFirestore appDb;
    private CollectionReference eventIdRefrence;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list);
        eventDataList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        AdminArrayAdapter AdminAttendeeListArrayAdapter = new AdminArrayAdapter(this, eventDataList);
        listView.setAdapter(adminArrayAdapter);
        TextView back_text = findViewById(R.id.back_text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminAttendeeView.this, "Please choose an attendee!", Toast.LENGTH_SHORT).show();
            }
        });
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selected attendee =  parent.getItemAtPosition(position);
            }
        });

    }
}