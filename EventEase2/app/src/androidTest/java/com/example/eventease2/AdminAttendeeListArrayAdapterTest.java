package com.example.eventease2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import com.example.eventease2.Administrator.AdminAttendeeListArrayAdapter;
import com.example.eventease2.Administrator.MockActivity;

@RunWith(AndroidJUnit4.class)
public class AdminAttendeeListArrayAdapterTest {

    @Test
    public void testAdminAttendeeListArrayAdapter() {
        ActivityScenario.launch(MockActivity.class).onActivity(activity -> {
            // Prepare sample data
            Context context = activity.getApplicationContext();
            String eventID = "event123";
            String organizerID = "organizer123";
            String profile_pic = "profile.jpg";
            String email = "john@example.com";
            String phone = "123456789";

            // Sample attendee data
            ArrayList<String> attendeeIDs = new ArrayList<>();
            attendeeIDs.add("attendee1");
            ArrayList<String> attendeeNames = new ArrayList<>();
            attendeeNames.add("John Doe");

            // Create adapter instance with sample data
            AdminAttendeeListArrayAdapter adapter = new AdminAttendeeListArrayAdapter(
                    context,
                    attendeeIDs,
                    attendeeNames,
                    eventID,
                    organizerID,
                    profile_pic,
                    email,
                    phone
            );

            // Create a mock parent view
            ViewGroup mockParent = new MockView(context);

            // Inflate the view
            View view = adapter.getView(0, null, mockParent);

            // Verify that text views are updated correctly
            TextView attendeeName = view.findViewById(R.id.attendee_name);
            assertEquals("John Doe", attendeeName.getText().toString()); // Attendee name should match the sample data

            // Add more assertions as needed for other views and behaviors
        });
    }

    // MockView class used as a placeholder for ViewGroup
    private static class MockView extends ViewGroup {
        public MockView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            // No implementation needed for testing purposes
        }
    }
}
