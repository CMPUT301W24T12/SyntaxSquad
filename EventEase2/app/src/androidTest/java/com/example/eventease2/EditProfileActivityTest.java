package com.example.eventease2;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.eventease2.Administrator.EditProfileActivity;

@RunWith(AndroidJUnit4.class)
public class EditProfileActivityTest {

    @Rule
    public IntentsTestRule<EditProfileActivity> intentsTestRule =
            new IntentsTestRule<>(EditProfileActivity.class, true, false);

    @Test
    public void testProfileDisplay() {
        Intent startIntent = new Intent();
        startIntent.putExtra("Name", "Test Name");
        startIntent.putExtra("ID", "Test ID");
        startIntent.putExtra("EventID", "Test EventID");
        startIntent.putExtra("OrganizerID", "Test OrganizerID");
        intentsTestRule.launchActivity(startIntent);

        Espresso.onView(withId(R.id.textView2)).check(matches(withText("Test Name")));
    }

    @Test
    public void testRemovePicButton() {
        Intent startIntent = new Intent();
        startIntent.putExtra("Name", "Test Name");
        startIntent.putExtra("ID", "Test ID");
        startIntent.putExtra("EventID", "Test EventID");
        startIntent.putExtra("OrganizerID", "Test OrganizerID");
        intentsTestRule.launchActivity(startIntent);

        Espresso.onView(withId(R.id.remove_pic)).perform(click());
        Espresso.onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
    }
}