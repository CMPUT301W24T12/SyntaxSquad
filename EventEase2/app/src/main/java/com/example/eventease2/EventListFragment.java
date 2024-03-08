package com.example.eventease2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.Organizer.OrganizerAttendeeListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("ID");
        String organizerID = getIntent().getStringExtra("OrganizerID");

        DocumentReference docRef = db.collection("EventEase")
                .document("Organizer")
                .collection(organizerID)
                .document(id);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the value of a field named "fieldName" as a String
                    ArrayList<String> nameList = (ArrayList<String>) documentSnapshot.get("NameList");

                    // Create a new list and copy the contents of nameList to it
                    Log.d("List", nameList.get(0));
                } else {
                    Log.d("TAG", "No such document");
                }
            }
        });

        Button viewAttendees = findViewById(R.id.view_attendees);

        viewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListFragment.this, OrganizerAttendeeListFragment.class);
                startActivity(intent);
            }
        });
    }
}