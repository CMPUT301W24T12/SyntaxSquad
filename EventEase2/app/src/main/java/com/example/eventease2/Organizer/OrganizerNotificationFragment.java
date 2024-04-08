package com.example.eventease2.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventease2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Notification page to receive information from user to send notification to attendees checked in to event
 * Uses Postman and OkHTTP API's to send Notifications
 * @Author Adeel Khan
 * This class represents the fragment used by organizers to send notifications to attendees
 * of a specific event. It allows organizers to compose and send notifications, with the
 * option to specify a title and description for the notification. Notifications are sent
 * to a topic corresponding to the event, allowing all attendees of the event to receive
 * the notification.
 * The {@link #sendNotification(String, String, String)} method sends the notification to
 * the Firebase Cloud Messaging (FCM) server using the provided title, description, and
 * event ID. It constructs a JSON object containing the notification details and sends
 * a POST request to the FCM server.
 * The {@link #updateTopic(String)} method updates the topic variable in a Postman collection
 * corresponding to the event ID. This method is used to dynamically update the event ID
 * in a Postman collection, allowing organizers to manage notification subscriptions for
 * different events.
 */
public class OrganizerNotificationFragment extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_notification_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventID = getIntent().getStringExtra("EventID");
        Log.d("Notification Event", eventID);
        String organizerID = getIntent().getStringExtra("OrganizerID");

        //updateTopic(eventID);

        /*
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("Notification Token", "Failed token retrieval");
                        }

                        token = task.getResult();
                        Log.d("Notification Token", token);
                    }
                });
         */

        Button send = findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout title_header = findViewById(R.id.textInputLayout2);
                EditText title_input = title_header.getEditText();
                String title = null;
                if (title_input != null) {
                    title = title_input.getText().toString();
                }

                TextInputLayout body_header = findViewById(R.id.textInputLayout);
                EditText body_input = body_header.getEditText();
                String body = null;
                if (body_input != null) {
                    body = body_input.getText().toString();
                }

                sendNotification(title, body, eventID);
            }
        });

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Add Event Activity
                Intent intent = new Intent(OrganizerNotificationFragment.this, OrganizerAttendeeListFragment.class);
                intent.putExtra("OrganizerID", organizerID);
                intent.putExtra("EventID", eventID);
                // Start the new activity
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Method to send notifications
     * Uses Postman and OkHTTP API's to send Notifications
     * @param String header, String description, String eventID
     * @Author Adeel Khan
     */
    private void sendNotification(String header, String description, String eventID) {
        //Log.d("Notification Token", token);

        //updateTopic(eventID);

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonNotif = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        Log.d("Notification Event", eventID);
        try {
            jsonNotif.put("title", header);
            jsonNotif.put("body", description);
            jsonObj.put("to", "/topics/"+eventID);
            jsonObj.put("notification", jsonNotif);
        } catch (JSONException e) {
            Log.d("Notification Error", e.toString());
        }

        RequestBody rBody = RequestBody.create(mediaType, jsonObj.toString());
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .addHeader("Authorization", "key=AAAAFcVk5T8:APA91bEIzRCHa6sxcp8A3Sh7oZSXfFq-CaMFTaXDK4Z2TYQxt7WXAbpFYNNkCirHEbMSqWfgMruNzq5xt_olzrcKsHqxYORckvZgKgq74loX0IFMGuaPkiFclYAgUd-1K4HEwM6a3rpK")
                .addHeader("Content-Type", "application/json")
                .post(rBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Notification Error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("Notification", "Notification Sent");
                } else {
                    Log.d("Notification Error", response.toString());
                }
            }
        });
    }

    private void updateTopic(String eventID){
        OkHttpClient client = new OkHttpClient();

        String apiKey = "PMAK-6611e5fd895f54000150b1ad-dd69042ad56c77d5ba8a848cfad0ff5cd5";
        String collectionId = "34103086-2ce22f39-e512-42d0-889a-9f1da51b3a84";

        // Build your request
        RequestBody requestBody = new FormBody.Builder()
                .add("key", "topic")
                .add("value", eventID)
                .build();

        Request request = new Request.Builder()
                .url("https://api.getpostman.com/collections/" + collectionId + "/variables")
                .addHeader("X-Api-Key", apiKey)
                .put(requestBody)
                .build();

        // Execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Variable updated successfully
                    String responseBody = response.body().string();
                    Log.d("Notification Update", responseBody);
                } else {
                    // Request not successful
                    // Handle error
                    Log.d("Notification Update", "Unsuccessful");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Request failure
                // Handle error
                Log.d("Notification Update", "Unsuccessful2");
            }
        });
    }
}
