package com.example.eventease2;

import com.example.eventease2.Organizer.AddEventFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUnitTest {
    @Mock
    private FirebaseFirestore db;
    @Mock
    private CollectionReference collectionReference;
    @Mock
    private DocumentReference organizerRef;
    @Mock
    private CollectionReference eventRef;
    @Mock
    private DocumentReference documentReference;
    private String organizerID = "FirebaseTestOrganizer1";
    private String eventID = "FirebaseTestEvent1";


    @Before
    public void setUp() {
        // Initialize Firestore
        db = Mockito.mock(FirebaseFirestore.class);
        collectionReference = Mockito.mock(CollectionReference.class);
        organizerRef = Mockito.mock(DocumentReference.class);
        eventRef = Mockito.mock(CollectionReference.class);
        documentReference = Mockito.mock(DocumentReference.class);

        // Mock Firestore
        when(db.collection("Organizer")).thenReturn(collectionReference);
        when(collectionReference.document(organizerID)).thenReturn(organizerRef);
        when(organizerRef.collection("Events")).thenReturn(eventRef);
        when(eventRef.document(eventID)).thenReturn(documentReference);
    }

    /**
     * add an event to the organizer and test if the event exists
     */
    @Test
    public void addEvents() {
        // Mock data
        QuerySnapshot mockSnapshot = Mockito.mock(QuerySnapshot.class);
        DocumentSnapshot mockDocument = Mockito.mock(DocumentSnapshot.class);
        when(mockDocument.getString("Name")).thenReturn("AddTest");
        when(mockDocument.getString("Description")).thenReturn("AddDescription");
        when(mockDocument.getString("Location")).thenReturn("AddLocation");
        when(mockDocument.getString("StartTime")).thenReturn("06/04/2024/0:00");
        when(mockDocument.getString("EndTime")).thenReturn("06/04/2024/12:00");
        when(mockDocument.getLong("Max")).thenReturn(10L);
        List<DocumentSnapshot> documentList = Collections.singletonList(mockDocument);
        when(mockSnapshot.getDocuments()).thenReturn(documentList);

        // Simulate the Firestore task completion
        Task<QuerySnapshot> task = Tasks.forResult(mockSnapshot);

        // Call your method that interacts with Firestore
        AddEventFragment addEventFragment = Mockito.mock(AddEventFragment.class);
        addEventFragment.putData(10,"AddTest",eventID,"AddLocation","AddDescription",
                "06/04/2024/0:00","06/04/2024/12:00",true,collectionReference,organizerID);

        // Verify that the necessary Firestore interactions occurred
//        verify(eventRef).document(eventID);
//        Map<String, Object> eventData = new HashMap<>();
//        eventData.put("Name", "AddTest");
//        eventData.put("Description", "AddDescription");
//        eventData.put("Location", "AddLocation");
//        eventData.put("StartTime", "06/04/2024/0:00");
//        eventData.put("EndTime", "06/04/2024/12:00");
//        eventData.put("Max", 10L);
//
//        documentReference.set(eventData);
//        verify(documentReference).set(eventData); // Ensure correct parameters are passed
        Mockito.verify(db, Mockito.times(1)).collection("Organizer");

        // Verify that the assertions are correct based on the mocked data
//        assertEquals("AddTest", eventTitle[0]);
//        assertEquals("AddDescription", description[0]);
//        assertEquals("AddLocation", location[0]);
//        assertEquals("06/04/2024/0:00", from[0]);
//        assertEquals("06/04/2024/12:00", to[0]);
//        assertEquals(10, max[0]);
    }
}
