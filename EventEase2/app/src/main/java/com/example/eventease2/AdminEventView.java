package com.example.eventease2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminEventView extends AppCompatActivity {

    ListView eventList;
    ArrayList<Event> eventDataList;
    AdminArrayAdapter adminArrayAdapter;
    private TextView eventNameText;
    private EditText eventTitle;
    private Button eventDetailsButton;
    private Button eventAttendeeButton;
    private Button backButton;
    private ListView eventListView;
    private FirebaseFirestore appDb;
    private CollectionReference eventIdRefrence;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_view);
    }}

