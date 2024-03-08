package com.example.eventease2.Organizer;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.Organizer.OrganizerAttendeeListArrayAdapter;
import com.example.eventease2.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class OrganizerAttendeeListFragment extends AppCompatActivity {

    ListView attendeeList;
    List<String> attendeeDataList;
    OrganizerAttendeeListArrayAdapter attendeeArrayAdapter;

    private FirebaseFirestore db;
    private DocumentReference organizerRef;
    private DocumentReference attendeeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_attendee_list);

        attendeeDataList = new ArrayList<>();
        attendeeList = findViewById(R.id.organizer_attendee_list);
        attendeeArrayAdapter = new OrganizerAttendeeListArrayAdapter(this, attendeeDataList);
        attendeeList.setAdapter(attendeeArrayAdapter);

        db = FirebaseFirestore.getInstance();

        organizerRef = db.collection("EventEase").document("Organizer");
        attendeeRef = organizerRef.collection("NewEvent").document("TestEvent");

        attendeeRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", "Listen failed: ", error);
                    return;
                }
                if (value != null && value.exists()) {
                    attendeeDataList = (List<String>) value.get("NameList");
                }
                attendeeArrayAdapter.notifyDataSetChanged();
            }
        });

    }

}
