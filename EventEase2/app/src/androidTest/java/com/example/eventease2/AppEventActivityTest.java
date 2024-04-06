package com.example.eventease2;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.eventease2.Administrator.AppEventsActivity;
import com.robotium.solo.Solo;

public class AppEventActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<AppEventsActivity> rule =
            new ActivityTestRule<>(AppEventsActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void correctActivity(){
        boolean isTextPresent = solo.searchText("Entries", true);
        assertTrue("TextView with text 'Welcome' is not present", isTextPresent);
    }

    @Test
    public void testClickEventDetailButton() {
        // Click on the "VIEW ATTENDEES" Button
        solo.clickOnButton("EVENT DETAILS");
        boolean isButtonPresent = solo.searchButton("Remove Picture", true);
        assertTrue("Remove Picture button is not present", isButtonPresent);
    }

    @Test
    public void testClickViewAttendeeButton() {
        solo.clickOnButton("VIEW ATTENDEE");
        boolean isTextViewPresent = solo.searchText("Attendees", true);
        assertTrue("TextView with text 'attendees' is not present", isTextViewPresent);
    }

    @Test
    public void testClickBackButton() {
        solo.clickOnText("Back");
        boolean isTextViewPresent = solo.searchText("Welcome", true);
        assertTrue("TextView with text 'Welcome' is not present", isTextViewPresent);
    }
}