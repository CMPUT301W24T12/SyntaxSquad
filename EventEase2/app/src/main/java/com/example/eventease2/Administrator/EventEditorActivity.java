

package com.example.eventease2.Administrator;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.eventease2.EventListFragment;
import com.example.eventease2.Organizer.AddEventFragment;
import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;

/**
 * EventEditorActivity is an activity class responsible for editing and managing event details by the administrator.
 * This class allows the administrator to view, edit, and delete event information, including title, description, and image.
 * It communicates with Firestore for fetching event details and Firebase Storage for managing event images.
 */
public class EventEditorActivity extends AppCompatActivity {

    private FirebaseFirestore appDb;
    private FirebaseStorage storage;

    private TextView eventDesciption;
    private TextView eventTitle;
    private Button deleteEvent;
    private TextView backInstruct;
    private ImageView eventPic;
    private TextView getBackInstruct;
    private ImageView eventImg;
    private Button removeImg;

    private String eventID;
    private String organizerID;
    private String posOfEvent;
    private DocumentReference eventInfoDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        initializeViews();
        appDb = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        EdgeToEdge.enable(this);
        setWindowInsetsListener();
        extractIntentData();
        loadImageFromStorage();
        setEventHandlers();
        loadEventDataFromFirestore();
    }

    /**
     * Initializes views by finding them in the layout XML.
     */
    private void initializeViews() {
        eventDesciption = findViewById(R.id.event_detail);
        eventTitle = findViewById(R.id.event_title);
        deleteEvent = findViewById(R.id.event_remove_profile_button);
        backInstruct = findViewById(R.id.events_back_button);
        eventPic = findViewById(R.id.event_editable_photo);
        getBackInstruct = findViewById(R.id.events_back_button);
        eventImg = findViewById(R.id.event_editable_photo);
        removeImg = findViewById(R.id.event_remove_photo_button);
    }

    /**
     * Sets a listener to handle window insets for the main layout.
     */
    private void setWindowInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Extracts intent data passed to this activity.
     */
    private void extractIntentData() {
        eventID = getIntent().getStringExtra("ID");
        organizerID = getIntent().getStringExtra("OrganizerID");
        posOfEvent = getIntent().getStringExtra("posOfEvent");
    }

    /**
     * Loads the event image from Firebase Storage.
     */
    private void loadImageFromStorage() {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + eventID);
        imageRef.getBytes(Long.MAX_VALUE).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                byte[] bytes = task.getResult();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                eventPic.setImageBitmap(bitmap);
            } else {
                Log.e("EventEditorActivity", "Error downloading image: " + task.getException());
            }
        });
    }

    /**
     * Sets event handlers for buttons and views.
     */
    private void setEventHandlers() {
        deleteEvent.setOnClickListener(v -> deleteEvent());
        getBackInstruct.setOnClickListener(v -> finish());
        backInstruct.setOnClickListener(v -> finish());
        removeImg.setOnClickListener(v -> removeImage());
    }

    /**
     * Deletes the event from Firestore database.
     */
    private void deleteEvent() {
        if (posOfEvent != null) {
            eventInfoDoc.delete()
                    .addOnSuccessListener(aVoid -> {
                        // Deleting event from appData
                        AppData appData = new AppData();
                        appData.deleteEventID(Integer.parseInt(posOfEvent));
                        appData.deleteEventInfo(Integer.parseInt(posOfEvent));
                        appData.deleteOrganizer(Integer.parseInt(posOfEvent));
                        appData.deleteEventName(Integer.parseInt(posOfEvent));
                        appData.deleteParticipantCount(Integer.parseInt(posOfEvent));
                        Intent intent = new Intent(this, AppEventsActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Log.w("TAG", "Error deleting document", e));
        } else {
            Toast.makeText(this, "Can't have a null position!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads event data from Firestore database.
     */
    private void loadEventDataFromFirestore() {
        eventInfoDoc = appDb.collection("Organizer").document(organizerID).collection("Events").document(eventID);
        eventInfoDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String description = document.getString("Description");
                    String eventBody = document.getString("EventBody");
                    String name = document.getString("Name");
                    if (description != null && eventBody != null && name != null) {
                        eventDesciption.setText(description);
                        eventTitle.setText(name);
                    } else {
                        Log.d("Description", "Description, EventBody, or Name is null");
                    }
                } else {
                    Log.d("Description", "Document does not exist");
                }
            } else {
                Log.d("Description", "Error getting document: " + task.getException());
            }
        });
    }


    /**
     * Removes the event image from Firebase Storage.
     */
    private void removeImage() {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + eventID);
        imageRef.delete().addOnSuccessListener(aVoid -> {
            StorageReference defaultImageRef = storageRef.child("images/" + eventID);
            Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_event);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            defaultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            defaultImageRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                eventPic.setImageResource(R.drawable.default_event);
                Toast.makeText(EventEditorActivity.this, "Image removed successfully", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Log.e("EventEditorActivity", "Error uploading default image: " + e.getMessage());
                Toast.makeText(EventEditorActivity.this, "Failed to remove image", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Log.e("EventEditorActivity", "Error deleting image: " + e.getMessage());
            Toast.makeText(EventEditorActivity.this, "Failed to remove image", Toast.LENGTH_SHORT).show();
        });
    }
}