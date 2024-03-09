package com.example.eventease2;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;

public class EventListFragmentTest {
    private FirebaseFirestore db;

    @Before
    public void setUp() {
        // Initialize Firebase Firestore for testing
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getContext());
        db = FirebaseFirestore.getInstance();
    }
}
