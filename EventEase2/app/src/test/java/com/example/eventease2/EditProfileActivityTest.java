package com.example.eventease2;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.eventease2.Administrator.EditProfileActivity;
import com.example.eventease2.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@RunWith(AndroidJUnit4.class)
public class EditProfileActivityTest {

    @Mock
    private FirebaseFirestore mockFirestore;

    @Mock
    private DocumentReference mockDocumentReference;

    @Mock
    private CollectionReference mockCollectionReference;

    @Mock
    private Task<QuerySnapshot> mockTask;

    @Before
    public void setUp() {
//        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testActivityDisplay() {
//        Intent intent = new Intent();
//        intent.putExtra("Name", "Test Name");
//        intent.putExtra("ID", "06NtlwKsOCTUE0m");
//        intent.putExtra("EventID", "d2ea4e72-5d4c-47a6-8c4b-49a442a08a41");
//        intent.putExtra("OrganizerID", "ffffffff-8a86-b983-0000-0000380c0fa3");
//
//        when(mockFirestore.collection(any(String.class))).thenReturn(mockCollectionReference);
//        when(mockCollectionReference.document(any(String.class))).thenReturn(mockDocumentReference);
//        when(mockDocumentReference.collection(any(String.class))).thenReturn(mockCollectionReference);
//        when(mockCollectionReference.get()).thenReturn(mockTask);
//        when(mockTask.addOnCompleteListener(any())).thenReturn(mockTask);
//
//        ActivityScenario<EditProfileActivity> scenario = ActivityScenario.launch(intent);
//        scenario.onActivity(activity -> {
//            onView(withId(R.id.textView2)).check(matches(isDisplayed()));
//            onView(withId(R.id.textView3)).check(matches(isDisplayed()));
//            onView(withId(R.id.editTextText2)).check(matches(isDisplayed()));
//            onView(withId(R.id.editTextText3)).check(matches(isDisplayed()));
//            onView(withId(R.id.remove_pic)).check(matches(isDisplayed()));
//            onView(withId(R.id.remove_profile)).check(matches(isDisplayed()));
//            onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
//        });
    }
}