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
//    @Rule
//    public ActivityScenarioRule<RoleChooseActivity> scenario = new ActivityScenarioRule<>(RoleChooseActivity.class);
    private Solo solo;
    @Rule
    public ActivityTestRule<AddEventFragment> addEvent =
            new ActivityTestRule<>(AddEventFragment.class, true, true);
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), addEvent.getActivity());
    }
//    /**Click Organizer button and confirm
//     *check if it turns to the event list page
//     */
//    @Test
//    public void testGoToEventList(){
//        // Click on organizer icon
//        onView(withId(R.id.orgIcon)).perform(click());
//        //Click confirm to intent
//        onView(withId(R.id.confirmButton)).perform(click());
//        //Check if we are in the page of event list
//        onView(withText("EventEase")).check(matches(isDisplayed()));
//
//    }
    @Test
    public void testActivity(){
        boolean uploadText = solo.searchText("Upload an image for the event", true);
        assertTrue("Not in the Add Event Page", uploadText);
    }

    @Test
    public void testNavigateToEventList() {
        solo.clickOnButton("Back");
        boolean isAddButtonExist = false;
        try{
            onView(withId(R.id.attendeeProfileImage)).check(matches(isDisplayed()));
        } catch (Exception e){
            isAddButtonExist = true;
        }
        assertTrue("Not in eventList activity", isAddButtonExist);
    }

//    @Test
//    public void testImageSelection(){
//        intended(hasAction(Intent.ACTION_PICK));
//        intended(hasType("image/*"));
//    }

//    /**Add a event to firebase
//     *check if the event info are in the firebase
//     */
//    @Test
//    public void testAddEvent(){
//        AtomicReference<String> id = null;
//        //ActivityScenario<AddEventFragment> add = addScenario.getScenario();
//        ActivityScenario<RoleChooseActivity> add = scenario.getScenario();
//        // Click on organizer icon
//        onView(withId(R.id.orgIcon)).perform(click());
//        //Click confirm to intent
//        onView(withId(R.id.confirmButton)).perform(click());
//        //Click on add event button
//        onView(withId(R.id.imageButton)).perform(click());
//        //Add information to am event
//        // Type Test in the editText
//        onView(withId(R.id.editTextText)).perform(ViewActions.typeText("TestName"));
//        onView(withId(R.id.editTextText2)).perform(ViewActions.typeText("TestDescription"));
//        onView(withId(R.id.enable_location_checkbox)).perform(ViewActions.click());
//        onView(withId(R.id.editTextText4)).perform(ViewActions.typeText("TestDuration"));
//        //get the event id
//        scenario.getScenario().onActivity(activity -> {
//                    // Access instance variable of the activity
//                id.set(activity.getID());
//        });
//        //Click generate
//        onView(withId(R.id.button2)).perform(click());
//        //check the firebase
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String imei = DeviceInfoUtils.getIMEI(getApplicationContext());
//        DocumentReference eventRef = db.collection("Organizer").document(imei).collection("Events").document(id.get());
//
//    }


    //    @Test
    //    public void testAddCity(){
    //        // Click on Add City button
    //        onView(withId(R.id.button_add)).perform(click());
    //        // Type "Edmonton" in the editText
    //        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));
    //        // Click on Confirm
    //        onView(withId(R.id.button_confirm)).perform(click());
    //        // Check if text "Edmonton" is matched with any of the text displayed on the screen
    //        onView(withText("Edmonton")).check(matches(isDisplayed()));
    //    }
}
