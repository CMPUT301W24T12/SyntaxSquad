package com.example.eventease2.Attendee;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventease2.Administrator.AppData;
import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class AttendeeEventDetailsActivity extends AppCompatActivity {
    FirebaseFirestore appDb = FirebaseFirestore.getInstance();
    TextView eventTitle;
    TextView eventDescription;
    TextView eventDetails;
    ImageView eventPhoto;
    int maxInt;
    String eventID;
    String organizerID;
    String posOfEvent;
    Switch promiseToGoSwitch;
    DocumentReference eventInfoDoc;
    CollectionReference attendeeList;
    Event promisedEvent;
    Bundle extras;

    public static AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.attendee_activity_event_details);

        eventTitle = findViewById(R.id.event_title);
        eventDescription = findViewById(R.id.event_description);
        eventDetails = findViewById(R.id.event_detail);
        eventPhoto = findViewById(R.id.attendee_event_photo);

        promiseToGoSwitch = findViewById(R.id.switch1);
        extras = getIntent().getExtras();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve data from intent extras
        eventID = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");
        posOfEvent = getIntent().getStringExtra("posOfEvent");

        eventInfoDoc = appDb.collection("Organizer").document(organizerID).collection("Events").document(eventID);
        attendeeList = eventInfoDoc.collection("Attendees");
        promiseToGoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(AttendeeEventDetailsActivity.this, "Switch Checked "+isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked){
                    eventInfoDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (task.isSuccessful()) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                String max = document.get("Max").toString();
                                maxInt = Integer.parseInt(max);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
                    Query query = eventInfoDoc.collection("Attendees");
                    AggregateQuery countQuery = query.count();
                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Count fetched successfully
                                AggregateQuerySnapshot snapshot = task.getResult();
                                Log.d(TAG, "Count: " + snapshot.getCount());
                                if(maxInt != snapshot.getCount()){
                                    Toast.makeText(AttendeeEventDetailsActivity.this, extras.getString("AttendeeID"), Toast.LENGTH_SHORT).show();
                                    addAttendeeData();
                                    promisedEvent = new Event(eventPhoto,eventTitle.toString(),eventDescription.toString(),null,false,null, null);
                                }else{
                                    //geolocation, and
                                    Toast.makeText(AttendeeEventDetailsActivity.this, "Sorry. All spots for this event are taken.", Toast.LENGTH_SHORT).show();
                                    promiseToGoSwitch.setChecked(false);
                                }

                            } else {
                                Log.d(TAG, "Count failed: ", task.getException());
                            }
                        }
                    });

                }
            }
        });
    }
    public void addAttendeeData(){
        HashMap<String,String> data = new HashMap<>();
//        data.put("Name", viewModel.getProfileName());
//        data.put("Email", viewModel.getProfileEmail());
//        data.put("Phone", viewModel.getProfilePhone());
        attendeeList.document(extras.getString("AttendeeID")).set(data);
    }
}