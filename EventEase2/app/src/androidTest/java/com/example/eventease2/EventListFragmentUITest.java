package com.example.eventease2;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class EventListFragmentUITest {
    @Rule
    public ActivityScenarioRule<EventListFragment> activityScenarioRule = new ActivityScenarioRule<>(EventListFragment.class);

    @Test
    public void testAddEventButton() {
        // Click on add/plus button
        onView(withId(R.id.attendeeProfileImage)).perform(click());

        onView(withId(R.id.editTextText)).check(matches(isDisplayed()));
    }
}
