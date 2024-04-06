package com.example.eventease2;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.eventease2.Administrator.AdminAttendeeView;
import com.example.eventease2.Administrator.AppEventsActivity;
import com.example.eventease2.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AdminAttendeeViewTest {

    @Rule
    public ActivityScenarioRule<AdminAttendeeView> activityRule =
            new ActivityScenarioRule<>(AdminAttendeeView.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testBackButton() {
        onView(withId(R.id.back_text)).perform(click());
        intended(hasComponent(AppEventsActivity.class.getName()));
    }

    @Test
    public void testIntent() {
        Intent expectedIntent = new Intent(ApplicationProvider.getApplicationContext(), AdminAttendeeView.class);
        expectedIntent.putExtra("ID", "testID");
        expectedIntent.putExtra("OrganizerID", "testOrganizerID");
        expectedIntent.putExtra("ProfilePicture", "testProfilePicture");
        expectedIntent.putExtra("Email", "testEmail");
        expectedIntent.putExtra("Phone", "testPhone");

        activityRule.getScenario().onActivity(activity -> {
            Intent actualIntent = activity.getIntent();
            assertEquals(expectedIntent.getStringExtra("ID"), actualIntent.getStringExtra("ID"));
            assertEquals(expectedIntent.getStringExtra("OrganizerID"), actualIntent.getStringExtra("OrganizerID"));
            assertEquals(expectedIntent.getStringExtra("ProfilePicture"), actualIntent.getStringExtra("ProfilePicture"));
            assertEquals(expectedIntent.getStringExtra("Email"), actualIntent.getStringExtra("Email"));
            assertEquals(expectedIntent.getStringExtra("Phone"), actualIntent.getStringExtra("Phone"));
        });
    }
}