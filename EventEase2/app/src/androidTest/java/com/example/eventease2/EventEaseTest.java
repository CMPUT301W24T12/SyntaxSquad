package com.example.eventease2;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.eventease2.Organizer.AddEventFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventEaseTest {
    @Rule
    public ActivityScenarioRule<RoleChooseActivity> scenario = new ActivityScenarioRule<>(RoleChooseActivity.class);
    public ActivityScenarioRule<AddEventFragment> addScenario = new ActivityScenarioRule<>(AddEventFragment.class);
    /**Click Organizer button and confirm
     *check if it turns to the event list page
     */
    @Test
    public void testGoToEventList(){
        // Click on organizer icon
        onView(withId(R.id.orgIcon)).perform(click());
        //Click confirm to intent
        onView(withId(R.id.confirmButton)).perform(click());
        //Check if we are in the page of event list
        onView(withText("EventEase")).check(matches(isDisplayed()));

    }
    @Test
    public void testLimitAttendee(){

    }
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
