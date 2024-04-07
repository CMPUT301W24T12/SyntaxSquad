package com.example.eventease2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.firebase.firestore.util.Assert.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import static java.util.regex.Pattern.matches;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.ViewAssertion;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.eventease2.Administrator.AppEventsActivity;
import com.robotium.solo.Solo;

import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AdminEventControlTester {
    private Solo solo;

    @Rule
    public ActivityTestRule<RoleChooseActivity> rule =
            new ActivityTestRule<>(RoleChooseActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void correctActivity(){
        boolean isTextPresent = solo.searchText("Welcome", true);
        assertTrue("Not in the Main Page", isTextPresent);
    }

    @Test
    public void testNavigToEventList() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        boolean isTextPresent = solo.searchText("Entries", true);
        assertTrue("TextView with text 'Welcome' is not present", isTextPresent);
    }

    @Test
    public void testNavigToIndivEvent() {
        // Click on the "VIEW ATTENDEES" Button
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        boolean isButtonPresent = solo.searchButton("Remove Picture", true);
        assertTrue("Remove Picture button is not present", isButtonPresent);
    }

    @Test
    public void testNavigFromEventListToRole(){
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnText("Back");
        boolean isButtonPresent = solo.searchText("Welcome", true);
        assertTrue("Remove Picture button is not present", isButtonPresent);
    }


    @Test
    public void testNavigFromIndivEventToRole(){
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        solo.clickOnText("Back");
        solo.clickOnText("Back");
        boolean isButtonPresent = solo.searchText("Welcome", true);
        assertTrue("Remove Picture button is not present", isButtonPresent);

    }

    @Test
    public void testImageRemoval() {
        // Click on the image button to initiate removal
        solo.clickOnImageButton(2);
        // Confirm the removal action
        solo.clickOnButton("Confirm");
        // Navigate to the event details where the image is displayed
        solo.clickOnButton("EVENT DETAILS");
        // Attempt to remove the picture
        solo.clickOnButton("Remove Picture");
        boolean isButtonPresent = solo.searchButton("Remove Picture", true);
        assertTrue("Remove Picture button is not present", isButtonPresent);
    }

    @Test
    public void testEventRemoval() {
        // Click on the image button to initiate removal
        solo.clickOnImageButton(2);
        // Confirm the removal action
        solo.clickOnButton("Confirm");
        // Navigate to the event details where the image is displayed
        solo.clickOnButton("EVENT DETAILS");
        // Attempt to remove the picture
        solo.clickOnButton("Remove Event");
        boolean isTextPresent = solo.searchText("Entries", true);
        assertTrue("Remove Picture button is not present", isTextPresent);
    }

    @Test
    public void testShowMoreButton() {
        solo.clickOnImageButton(2);
        // Confirm the removal action
        solo.clickOnButton("Confirm");
        if (solo.searchButton("Show More", true)) {
            solo.clickOnButton("Show More");
            boolean isTextPresent = solo.searchText("Entries", true);
            assertTrue("Entries text is not present", isTextPresent);
        }
    }

    @Test
    public void testEventNavigationWithBackButton(){
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        solo.clickOnText("Back");
        solo.clickOnText("Back");
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        boolean isTextPresent = solo.searchText("Entries", true);
        assertTrue("TextView with text 'Welcome' is not present", isTextPresent);
    }

}

