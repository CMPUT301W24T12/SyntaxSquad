package com.example.eventease2;


import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.eventease2.Administrator.EditProfileActivity;
import com.example.eventease2.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class EditProfileActivityTest {

    @Rule
    public ActivityTestRule<EditProfileActivity> activityRule =
            new ActivityTestRule<>(EditProfileActivity.class, true, false);

    @Before
    public void setUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EditProfileActivity.class);
        intent.putExtra("Name", "Test Name");
        intent.putExtra("ID", "Test ID");
        intent.putExtra("EventID", "Test EventID");
        intent.putExtra("OrganizerID", "Test OrganizerID");
        intent.putExtra("ProfilePicture", R.drawable.ellipse_9);
        activityRule.launchActivity(intent);
    }

    @Test
    public void testActivityNotNull() {
        assertNotNull(activityRule.getActivity());
    }

    @Test
    public void testAttendeeName() {
        TextView attendeeName = activityRule.getActivity().findViewById(R.id.textView2);
        assertEquals("Test Name", attendeeName.getText().toString());
    }

    @Test
    public void testRemovePicButton() {
        Button removePic = activityRule.getActivity().findViewById(R.id.remove_pic);
        assertNotNull(removePic);
    }

    @Test
    public void testRemoveProfileButton() {
        Button removeProfile = activityRule.getActivity().findViewById(R.id.remove_profile);
        assertNotNull(removeProfile);
    }

    @Test
    public void testProfilePic() {
        ImageView profilePic = activityRule.getActivity().findViewById(R.id.imageView2);
        assertNotNull(profilePic);
    }
}