package com.example.eventease2;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.eventease2.Administrator.AdminAttendeeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminAttendeeViewTest {

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private CollectionReference mockCollectionReference;

    @Mock
    private Task<QuerySnapshot> mockTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAttendeeView() {
        Intent intent = new Intent();
        intent.putExtra("ID", "testEventID");
        intent.putExtra("OrganizerID", "testOrganizerID");
        intent.putExtra("ProfilePicture", "testProfilePicture");
        intent.putExtra("Email", "testEmail");
        intent.putExtra("Phone", "testPhone");

        when(mockFirestore.collection("Organizer/testOrganizerID/Events/testEventID/Attendees")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.get()).thenReturn(mockTask);
        when(mockTask.addOnCompleteListener(any(OnCompleteListener.class))).thenReturn(mockTask);
        ActivityScenario<AdminAttendeeView> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            onView(ViewMatchers.withId(R.id.back_text)).check(matches(isDisplayed()));
            onView(withText("Back")).perform(click());
        });
    }

    @After
    public void tearDown() {
    }
}