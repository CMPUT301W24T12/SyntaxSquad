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
    public ActivityTestRule<RoleChooseActivity> activityRule =
            new ActivityTestRule<>(RoleChooseActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());

    }

    @Test
    public void testCorrectActivity() {
        boolean isWelcomeTextPresent = solo.searchText("Welcome", true);
        assertTrue("Not in the Main Page", isWelcomeTextPresent);
    }

    @Test
    public void testNavigateToEventList() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        boolean isEntriesTextPresent = solo.searchText("Entries", true);
        assertTrue("Unable to Navigate to Event List Activity", isEntriesTextPresent);
    }

    @Test
    public void testNavigateToIndividualEvent() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        boolean isRemovePictureButtonPresent = solo.searchButton("Remove Picture", true);
        assertTrue("Unable to Navigate to Individual Event's Activity", isRemovePictureButtonPresent);
    }

    @Test
    public void testNavigateFromEventListToRole() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnText("Back");
        boolean isWelcomeTextPresent = solo.searchText("Welcome", true);
        assertTrue("Back button from event list to role activity not working", isWelcomeTextPresent);
    }

    @Test
    public void testNavigateFromIndividualEventToRole() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        solo.clickOnText("Back");
        solo.clickOnText("Back");
        boolean isWelcomeTextPresent = solo.searchText("Welcome", true);
        assertTrue("Back button from individual event to role activity not working", isWelcomeTextPresent);
    }

    @Test
    public void testImageRemovalButtonPresence() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        solo.clickOnButton("Remove Picture");
        boolean isRemovePictureButtonPresent = solo.searchButton("Remove Picture", true);
        assertTrue("Remove Picture button not working", isRemovePictureButtonPresent);
    }

    @Test
    public void testEventRemoval() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        solo.clickOnButton("Remove Event");
        boolean isEntriesTextPresent = solo.searchText("Entries", true);
        assertTrue("Unable to remove event", isEntriesTextPresent);
    }

    @Test
    public void testShowMoreButton() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        if (solo.searchButton("Show More", true)) {
            solo.clickOnButton("Show More");
            boolean isEntriesTextPresent = solo.searchText("Entries", true);
            assertTrue("Show More Button not working", isEntriesTextPresent);
        }
    }

    @Test
    public void testEventNavigationWithBackButton() {
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        solo.clickOnButton("EVENT DETAILS");
        solo.clickOnText("Back");
        solo.clickOnText("Back");
        solo.clickOnImageButton(2);
        solo.clickOnButton("Confirm");
        boolean isEntriesTextPresent = solo.searchText("Entries", true);
        assertTrue("Back Button unable to work with multiple use", isEntriesTextPresent);
    }

}