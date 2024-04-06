package com.example.eventease2;

import static org.junit.Assert.assertTrue;

import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.eventease2.Administrator.EventEditorActivity;
import com.robotium.solo.Solo;
public class EventEditorActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<EventEditorActivity> rule =
            new ActivityTestRule<>(EventEditorActivity.class, true, true);

    @Before
    public void setUp() {
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//        Intent intent = new Intent();
//        intent.putExtra("ID", "your_event_id"); // Replace "your_event_id" with the actual event ID
//        intent.putExtra("OrganizerID", "your_organizer_id"); // Replace "your_organizer_id" with the actual organizer ID
//        intent.putExtra("posOfEvent", "your_position_of_event"); // Replace "your_position_of_event" with the actual position of the event
//        rule.launchActivity(intent);

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void correctActivity(){
        boolean isButtonPresent = solo.searchButton("REMOVE EVENT", true);
        assertTrue("Wrong activity initialized", isButtonPresent);
    }

}
