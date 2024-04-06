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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OrganizerNotificationFragment extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_notification_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventID = getIntent().getStringExtra("EventID");
        String organizerID = getIntent().getStringExtra("OrganizerID");

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

        Button send = findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout title_header = findViewById(R.id.textInputLayout2);
                EditText title_input = title_header.getEditText();
                String title = null;
                if (title_input != null) {
                    title = title_input.toString();
                }

                TextInputLayout body_header = findViewById(R.id.textInputLayout);
                EditText body_input = body_header.getEditText();
                String body = null;
                if (body_input != null) {
                    body = body_input.toString();
                }

                sendNotification(title, body);

                /*
                String topic = "test";
                // Get an instance of FirebaseMessaging
                FirebaseMessaging messaging = FirebaseMessaging.getInstance();
                // Create a message                               eventID
                RemoteMessage message = new RemoteMessage.Builder(topic)
                        .setMessageId("your-message-id")
                        .addData("key", "value") // optional data payload
                        .build();
                // Send the message
                messaging.send(message);
                 */

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
                //startActivity(intent);
                finish();
            }
        });
    }

    private void sendNotification(String header, String description) {
        Log.d("Notification Token", token);

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject jsonNotif = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonNotif.put("title", header);
            jsonNotif.put("body", description);
            jsonObj.put("to", token);
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
        /*
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            Log.d("Notification Error", e.toString());
        }
        */

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
}
