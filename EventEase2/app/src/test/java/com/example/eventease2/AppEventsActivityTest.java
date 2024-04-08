package com.example.eventease2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import com.example.eventease2.Administrator.AppEventsActivity;
import com.google.firebase.firestore.FirebaseFirestore;



public class AppEventsActivityTest {

    private AppEventsActivity appEventsActivity;

    @Mock
    private FirebaseFirestore mockFirestore;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        appEventsActivity = new AppEventsActivity();
        appEventsActivity.appDb = mockFirestore;
    }


    @Test
    public void testRefreshEventData_ClearsEventData() {
        appEventsActivity.refreshEventData();
        // Assert that all event data lists are cleared
        assertTrue(appEventsActivity.organizerList.isEmpty());
        assertTrue(appEventsActivity.eventNameList.isEmpty());
        assertTrue(appEventsActivity.eventInfoList.isEmpty());
        assertTrue(appEventsActivity.eventIDs.isEmpty());
        assertTrue(appEventsActivity.participantCountList.isEmpty());
    }


    @Test
    public void testFetchOrganizers_Success() {
        // Simulate a successful fetch of organizers
        // This test only checks if the fetchOrganizers method calls fetchEventsForOrganizer
        appEventsActivity.fetchOrganizers(mockFirestore.collection("Organizer"), mockFirestore);
        // Verify that fetchEventsForOrganizer is called
        verify(mockFirestore.collection("Organizer").document("organizerId").collection("Events")).get();
    }
}

