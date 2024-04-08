package com.example.eventease2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.eventease2.Organizer.AddEventFragment;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEventUITest {
    private Solo solo;
    private Solo solo2;
    @Rule
    public ActivityTestRule<AddEventFragment> addEvent =
            new ActivityTestRule<>(AddEventFragment.class, true, true);
    @Rule
    public ActivityTestRule<RoleChooseActivity> roleEvent =
            new ActivityTestRule<>(RoleChooseActivity.class, true, true);
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), addEvent.getActivity());
        solo2 = new Solo(InstrumentationRegistry.getInstrumentation(), roleEvent.getActivity());
    }
    @Test
    public void testActivity(){
        boolean uploadText = solo.searchText("Upload an image for the event", true);
        assertTrue("Not in the Add Event Page", uploadText);
    }

    /**
     * Test intent from role choose activity to event list
     */
    @Test
    public void testNavigateToEventList() {
        assertTrue(solo2.searchText("Welcome"));
        solo2.clickOnView(solo2.getView(R.id.orgIcon));
        solo2.clickOnView(solo2.getView(R.id.confirmButton));
        boolean isBackTestPresent = solo2.searchText("Back", true);
        assertTrue("Unable to Navigate to Event List Activity", isBackTestPresent);
    }

    /**
     * test intent to add event fragment
     */
    @Test
    public void testNavigateToAddEventFragment() {
        assertTrue(solo2.searchText("Welcome"));
        solo2.clickOnView(solo2.getView(R.id.orgIcon));
        solo2.clickOnView(solo2.getView(R.id.confirmButton));
        solo2.clickOnView(solo2.getView(R.id.attendeeProfileImage));
        boolean uploadText = solo2.searchText("Upload an image for the event", true);
        assertTrue("Unable to Navigate to Add Event Activity", uploadText);
    }
    /**
     * test intent to add event fragment
     */
    @Test
    public void testNavigateToReuseQRCode() {
        assertTrue(solo2.searchText("Welcome"));
        solo2.clickOnView(solo2.getView(R.id.orgIcon));
        solo2.clickOnView(solo2.getView(R.id.confirmButton));
        solo2.clickOnView(solo2.getView(R.id.attendeeProfileImage));
        solo2.clickOnView(solo2.getView(R.id.existing));
        boolean isChooseButtonPresent = solo2.searchText("Choose this QR code", true);
        assertTrue("Unable to Navigate to Reuse QR Code Activity", isChooseButtonPresent);
    }
    /**
     * test intent to event detail after a generation
     */
    @Test
    public void testNavigateToEventDetailsAfterGenerate() {
        assertTrue(solo2.searchText("Welcome"));
        solo2.clickOnView(solo2.getView(R.id.orgIcon));
        solo2.clickOnView(solo2.getView(R.id.confirmButton));
        solo2.clickOnView(solo2.getView(R.id.attendeeProfileImage));
        solo2.clickOnView(solo2.getView(R.id.new_qr));
        solo2.clickOnView(solo2.getView(R.id.button2));
        boolean isEditButtonPresent = solo2.searchText("Edit", true);
        assertTrue("Unable to Navigate to Event Detail Activity", isEditButtonPresent);
    }

//    /**
//     * test intent to event detail after clicking event detail button
//     */
//    @Test
//    public void testEventDetailButton() {
//        assertTrue(solo2.searchText("Welcome"));
//        solo2.clickOnView(solo2.getView(R.id.orgIcon));
//        solo2.clickOnView(solo2.getView(R.id.confirmButton));
////        solo2.clickOnView(solo2.getView(R.id.attendeeProfileImage));
////        solo2.clickOnView(solo2.getView(R.id.new_qr));
////        solo2.clickOnView(solo2.getView(R.id.button2));
////        solo2.clickOnView(solo2.getView(R.id.back_button));
//        solo.clickOnView(solo2.getView(R.id.event_details));
//        boolean isEditButtonPresent = solo2.searchText("Edit", true);
//        assertTrue("Unable to Navigate to Event Detail Activity", isEditButtonPresent);
//    }
//    /**
//     * test intent to between back buttons
//     */
//    @Test
//    public void testBackButton() {
//        assertTrue(solo2.searchText("Welcome"));
//        solo2.clickOnView(solo2.getView(R.id.orgIcon));
//        solo2.clickOnView(solo2.getView(R.id.confirmButton));
//        solo2.clickOnView(solo2.getView(R.id.attendeeProfileImage));
//        solo2.clickOnView(solo2.getView(R.id.new_qr));
//        solo2.clickOnView(solo2.getView(R.id.button2));
//        solo2.clickOnView(solo2.getView(R.id.back_button));
//        boolean isAddButtonPresent = false;
//        boolean isWelcomePresent = false;
//        try{
//            solo2.clickOnView(solo2.getView(R.id.attendeeProfileImage));
//
//            isAddButtonPresent = true;
//            assertTrue("Unable to Navigate back Event List Activity", isAddButtonPresent);
//            solo2.clickOnView(solo2.getView(R.id.back_button));
//            solo2.clickOnView(solo2.getView(R.id.home_event));
//            isWelcomePresent = solo2.searchText("Welcome", true);
//        }catch (Exception e){
//
//        }
//        //assertTrue("Unable to Navigate back Event List Activity", isAddButtonPresent);
//        assertTrue("Unable to Navigate to Main Page Activity", isWelcomePresent);
//    }
}
