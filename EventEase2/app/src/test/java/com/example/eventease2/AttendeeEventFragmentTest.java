package com.example.eventease2;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import com.example.eventease2.Attendee.AttendeeEventFragment;

@RunWith(AndroidJUnit4.class)
public class AttendeeEventFragmentTest {

    @Test
    public void testEventFragment() {
        // Start a Fragment scenario
        ActivityScenario.launch(FragmentActivity.class).onActivity(activity -> {
            // Create an instance of AttendeeEventFragment
            AttendeeEventFragment fragment = new AttendeeEventFragment("eventID", "organizerID");

            // Inflate the layout
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.fragment_attendee_event, null);

            // Manually call onViewCreated method to attach the view
            fragment.onViewCreated(view, null);

            // Verify that text views are updated correctly
            TextView eventTitle = view.findViewById(R.id.eventTitle);
            TextView eventDescription = view.findViewById(R.id.eventDescription);
            TextView eventBody = view.findViewById(R.id.eventBody);

            assertEquals("", eventTitle.getText().toString()); // Event title should be empty initially
            assertEquals("", eventDescription.getText().toString()); // Event description should be empty initially
            assertEquals("", eventBody.getText().toString()); // Event body should be empty initially
        });
    }
}

