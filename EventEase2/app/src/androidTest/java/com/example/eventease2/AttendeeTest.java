package com.example.eventease2;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import android.view.View;
import android.widget.Gallery;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
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
public class AttendeeTest {

    @Rule
    public ActivityScenarioRule<RoleChooseActivity> scenario = new
            ActivityScenarioRule<RoleChooseActivity>(RoleChooseActivity.class);
//    @Test
//    public void testAttendeeOpen() {
//        onView(withId(R.id.attendIcon)).perform(click());
//
//        onView(withId(R.id.confirmButton)).perform(click());
//
//        onView(withId(R.id.btnScanQR)).check(matches(isDisplayed()));
//    }
//    @Test
//    public void testAttendeeSwitchViews() throws InterruptedException {
//        onView(withId(R.id.attendIcon)).perform(click());
//
//        onView(withId(R.id.confirmButton)).perform(click());
//
//        onView(withId(R.id.Profile)).perform(click());
//
//        onView(withId(R.id.attendeeProfileImage)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.Event)).perform(click());
//
//        onView(withId(R.id.event_header)).check(matches(isDisplayed()));
//    }
//    @Test
//    public void testAttendeeProfileText(){
//        onView(withId(R.id.attendIcon)).perform(click());
//
//        onView(withId(R.id.confirmButton)).perform(click());
//
//        onView(withId(R.id.Profile)).perform(click());
//
//        onView(withId(R.id.attendeeProfileImage)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.editProfileName)).perform(ViewActions.typeText("Sean Piatt"));
//
//        onView(withId(R.id.editBioText)).perform(ViewActions.typeText("I am number 1"));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.editTextPhone2)).perform(ViewActions.typeText("7778889999"));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.editProfileEmail)).perform(ViewActions.typeText("awesean@hotmail.com"));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.AttendeeAddChanges)).perform(click());
//
//        onView(withId(R.id.QR_Scanner)).perform(click());
//
//        onView(withId(R.id.btnScanQR)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.Profile)).perform(click());
//
//        onView(withText("Sean Piatt")).check(matches(isDisplayed()));
//
//        onView(withText("I am number 1")).check(matches(isDisplayed()));
//
//        onView(withText("7778889999")).check(matches(isDisplayed()));
//
//        onView(withText("awesean@hotmail.com")).check(matches(isDisplayed()));
//    }
//    @Test
//    public void testAttendeeProfileImage(){
//        onView(withId(R.id.attendIcon)).perform(click());
//
//        onView(withId(R.id.confirmButton)).perform(click());
//
//        onView(withId(R.id.Profile)).perform(click());
//
//        onView(withId(R.id.attendeeProfileImage)).check(matches(isDisplayed()));
//    }

    @Test
    public void testAttendeeEventView() throws InterruptedException {
        onView(withId(R.id.attendIcon)).perform(click());

        onView(withId(R.id.confirmButton)).perform(click());

        onView(withId(R.id.Event)).perform(click());

        onView(withId(R.id.event_header)).check(matches(isDisplayed()));

        Thread.sleep(1000);

        Espresso.onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.attendee_event_list))
                        .atPosition(0).check(matches(isDisplayed()));

        Espresso.onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.attendee_event_list))
                .atPosition(0).onChildView(withId(R.id.event_details)).check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.attendee_event_photo)).check(matches(isDisplayed()));
    }
    @Test
    public void testAttendeeEventSignUp() throws InterruptedException {
        onView(withId(R.id.attendIcon)).perform(click());

        onView(withId(R.id.confirmButton)).perform(click());

        onView(withId(R.id.Event)).perform(click());

        onView(withId(R.id.event_header)).check(matches(isDisplayed()));

        Thread.sleep(1000);

        Espresso.onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.attendee_event_list))
                .atPosition(0).onChildView(withId(R.id.event_details)).check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.attendee_event_photo)).check(matches(isDisplayed()));

    }

}
