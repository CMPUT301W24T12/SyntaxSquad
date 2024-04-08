package com.example.eventease2;


import android.content.Intent;
import android.widget.Toast;

import com.example.eventease2.Administrator.EventEditorActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;

public class EventEditorActivityTest {

    @Mock
    FirebaseFirestore mockFirestore;
    @Mock
    FirebaseStorage mockStorage;
    @Mock
    DocumentReference mockDocumentReference;
    @Mock
    StorageReference mockStorageReference;
    @Mock
    Task<Void> mockTask;

    private EventEditorActivity activity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Stubbing Firestore and Storage behavior
        when(mockFirestore.collection("Organizer")
                .document("organizerID")
                .collection("Events")
                .document("eventID"))
                .thenReturn(mockDocumentReference);

        when(mockStorage.getReference().child("images/eventID")).thenReturn(mockStorageReference);

        // Stubbing task success for loading event data
        when(mockDocumentReference.get()).thenReturn(mock(Task.class));

        // Initialize the activity
        activity = new EventEditorActivity();
        activity.appDb = mockFirestore;
        activity.storage = mockStorage;
        activity.eventInfoDoc = mockDocumentReference;
        activity.eventID = "eventID";
        activity.posOfEvent = "1";
    }

    @Test
    public void testDeleteEvent_Success() {
        // Mock successful deletion of event document
        when(mockDocumentReference.delete()).thenReturn(mock(Task.class));
        // Call the deleteEvent method
        activity.deleteEvent();
        // Verify that the document deletion task is invoked
        verify(mockDocumentReference).delete();
    }

    @Test
    public void testDeleteEvent_Failure() {
        // Mock failure of deletion task
        Exception exception = new RuntimeException("Delete failed");
        when(mockDocumentReference.delete()).thenReturn(mock(Task.class));
        when(mockTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Do nothing
            }
        })).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                // Invoke the failure listener
                activity.deleteEvent();
            }
        })).thenReturn(mockTask);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockTask.getException()).thenReturn(exception);

        // Call the deleteEvent method
        activity.deleteEvent();
    }

    @Test
    public void testRemoveImage_Success() {
        // Mock successful deletion of image from storage
        when(mockStorageReference.delete()).thenReturn(mock(Task.class));
        // Call the removeImage method
        activity.removeImage();
        // Verify that the storage reference deletion task is invoked
        verify(mockStorageReference).delete();
    }

    @Test
    public void testRemoveImage_Failure() {
        // Mock failure of image deletion task
        Exception exception = new RuntimeException("Delete failed");
        when(mockStorageReference.delete()).thenReturn(mock(Task.class));
        when(mockTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Do nothing
            }
        })).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Invoke the failure listener
                activity.removeImage();
            }
        })).thenReturn(mockTask);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockTask.getException()).thenReturn(exception);

        // Call the removeImage method
        activity.removeImage();

    }
}