package com.example.eventease2.Attendee;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.example.eventease2.Administrator.AppData;
import com.example.eventease2.Event;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/**
 * An activity that displays details of an event for attendees and allows them to indicate
 * their intention to attend the event.
 * <p>
 * This activity retrieves event details from Firebase Firestore and displays them along with
 * an image representing the event. Attendees can toggle a switch to indicate their intention
 * to attend the event. If the event has a maximum attendee limit, the activity checks the
 * current number of attendees to ensure the limit is not exceeded before allowing attendance
 * confirmation.
 * <p>
 * Additionally, attendees can remove their attendance confirmation if they change their minds.
 * The activity updates the attendee count accordingly when attendance status changes.
 * <p>
 * The activity also handles UI elements like back navigation and ensures proper padding to
 * accommodate system bars.
 * <p>
 * Usage:
 * <p>
 * To use this activity, you need to pass necessary data through intent extras:
 * - "OrganizerID": The ID of the event organizer.
 * - "ID": The ID of the event.
 * - "AttendeeID": The ID of the attendee.
 * - "AttendeeName": The name of the attendee.
 * - "AttendeeEmail": The email of the attendee.
 * - "AttendeePhone": The phone number of the attendee.
 * <p>
 * The activity retrieves event details and attendee information based on the provided IDs.
 * Attendees can then toggle their attendance status using a switch.
 * <p>
 * Note: This activity assumes that the event photo is stored in Firebase Storage under
 * a folder named "images" with the event ID as the filename.
 * <p>
 * Dependencies:
 * <p>
 * This activity relies on Firebase Firestore to retrieve event and attendee information,
 * as well as Firebase Storage to fetch event photos.
 * <p>
 * @author Sean
 */
public class AttendeeEventDetailsActivity extends AppCompatActivity {
    public static AppData appData;
    private FirebaseFirestore appDb = FirebaseFirestore.getInstance();
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventDetails, entriesTextView;
    private String entries;
    private ImageView eventPhoto;
    int maxInt;
    private String eventID;
    private String organizerID;
    private String  attendeeId;
    private Switch promiseToGoSwitch;
    private DocumentReference eventInfoDoc;
    private CollectionReference attendeeList;
    private Event promisedEvent;
    private Bundle extras;
    private StorageReference pathReference;
    boolean sent = false;
    boolean noMax = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.attendee_activity_event_details);

        extras = getIntent().getExtras();
        organizerID = getIntent().getStringExtra("OrganizerID");
        eventID = getIntent().getStringExtra("ID");
        attendeeId = getIntent().getStringExtra("AttendeeID");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        pathReference = storageRef.child("images").child(eventID);

        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                eventPhoto.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });


        //extras = getIntent().getExtras();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve data from intent extras

        //posOfEvent = getIntent().getStringExtra("posOfEvent");
        eventInfoDoc = appDb.collection("Organizer").document(organizerID).collection("Events").document(eventID);
        attendeeList = eventInfoDoc.collection("Attendees");

        eventTitle = (TextView) findViewById(R.id.event_title);
        entriesTextView = findViewById(R.id.numOfSpotLeft);
        eventDescription = (TextView) findViewById(R.id.event_description);
        eventDetails = (TextView) findViewById(R.id.event_detail);
        eventPhoto = findViewById(R.id.attendee_event_photo);
        promiseToGoSwitch = findViewById(R.id.switch1);
        TextView back = findViewById(R.id.textView12);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the AppEvent activity
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
                finish(); // Finish the current activity
            }
        });
        eventInfoDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        eventTitle.setText((CharSequence) document.get("Name"));
                        eventDescription.setText((CharSequence) document.get("Description"));
                        eventDetails.setText((CharSequence) document.get("EventBody"));
                        if (document.get("Max") == null) {
                            noMax = true;
                        } else {
                            maxInt = Integer.parseInt(String.valueOf(document.get("Max")));
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        attendeeList.document(attendeeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        promiseToGoSwitch.setChecked(true);
                        sent = true;
                    } else {
                        sent = false;
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        Query query = attendeeList;
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    entries = String.valueOf(snapshot.getCount());
                    if (noMax) {
                        entriesTextView.setText("No Limit");
                    } else if (maxInt - Integer.parseInt(entries) < 0) {
                        entriesTextView.setText("0");
                    } else {
                        entries = String.valueOf(maxInt - Integer.parseInt(entries));
                        entriesTextView.setText(entries);
                    }
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });

        promiseToGoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Query query = eventInfoDoc.collection("Attendees");
                    AggregateQuery countQuery = query.count();
                    countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!sent) {
                                    // Count fetched successfully
                                    AggregateQuerySnapshot snapshot = task.getResult();
                                    Log.d(TAG, "Count: " + snapshot.getCount());
                                    if (maxInt > snapshot.getCount() || noMax) {
                                        addAttendeeData();
                                        promisedEvent = new Event(eventPhoto, eventTitle.toString(),
                                                eventDescription.toString(), null,
                                                false, null, null);
                                        if (!noMax) {
                                            entries = String.valueOf(Integer.parseInt(entries) - 1);
                                            entriesTextView.setText(entries);
                                        }
                                        sent = true;
                                    } else {
                                        //geolocation, and
                                        Toast.makeText(AttendeeEventDetailsActivity.this,
                                                "Sorry. All spots for this event are taken.",
                                                Toast.LENGTH_SHORT).show();
                                        promiseToGoSwitch.setChecked(false);
                                        sent = false;
                                    }
                                } else {
                                    sent = true;
                                }
                            } else {
                                Log.d(TAG, "Count failed: ", task.getException());
                            }
                        }
                    });

                } else {
                    if (sent) {
                        eventInfoDoc.collection("Attendees").document(attendeeId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        entries = String.valueOf(Integer.parseInt(entries) + 1);
                                        entriesTextView.setText(entries);
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        sent = false;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                }
            }
        });
    }

    public void addAttendeeData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("Name", extras.getString("AttendeeName"));
        data.put("Email", extras.getString("AttendeeEmail"));
        data.put("Phone", extras.getString("AttendeePhone"));
        attendeeList.document(extras.getString("AttendeeID")).set(data);
    }
}