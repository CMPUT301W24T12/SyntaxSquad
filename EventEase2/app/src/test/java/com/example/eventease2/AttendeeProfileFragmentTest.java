package com.example.eventease2;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static android.app.Activity.RESULT_OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.eventease2.Attendee.AttendeeItemViewModel;
import com.example.eventease2.Attendee.AttendeeProfileFragment;

@RunWith(AndroidJUnit4.class)
public class AttendeeProfileFragmentTest {

    @Test
    public void testAttendeeProfileFragment() {
        // Start a Fragment scenario
        ActivityScenario.launch(FragmentTestActivity.class).onActivity(activity -> {
            // Mock ViewModelProvider
            ViewModelProvider viewModelProvider = mock(ViewModelProvider.class);
            AttendeeItemViewModel viewModel = mock(AttendeeItemViewModel.class);
            Mockito.when(viewModelProvider.get(AttendeeItemViewModel.class)).thenReturn(viewModel);

            // Create an instance of AttendeeProfileFragment
            AttendeeProfileFragment fragment = new AttendeeProfileFragment("eventID", "organizerID");
            fragment.viewModel = viewModel; // Set the mocked ViewModel
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.fragment_attendee_profile, null);
            fragment.onViewCreated(view, null);

            // Verify that the text fields are set correctly
            EditText nameEditText = view.findViewById(R.id.editProfileName);
            EditText phoneEditText = view.findViewById(R.id.editTextPhone2);
            EditText emailEditText = view.findViewById(R.id.editProfileEmail);
            EditText bioEditText = view.findViewById(R.id.editBioText);
            ImageButton imageButton = view.findViewById(R.id.attendeeProfileImage);

            // Mock selected image URI
            Uri mockUri = Uri.parse("content://media/external/images/media/1");
            fragment.selectedImage = mockUri;
            fragment.setTextFromModel();

            assertEquals("", nameEditText.getText().toString()); // Name field should be empty initially
            assertEquals("", phoneEditText.getText().toString()); // Phone field should be empty initially
            assertEquals("", emailEditText.getText().toString()); // Email field should be empty initially
            assertEquals("", bioEditText.getText().toString()); // Bio field should be empty initially
            assertEquals(mockUri, imageButton.getImageURI()); // Image button should have the mock URI set
        });
    }

    @Test
    public void testImageSelection() {
        // Start a Fragment scenario
        ActivityScenario.launch(FragmentTestActivity.class).onActivity(activity -> {
            // Create an instance of AttendeeProfileFragment
            AttendeeProfileFragment fragment = new AttendeeProfileFragment("eventID", "organizerID");
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.fragment_attendee_profile, null);
            fragment.onViewCreated(view, null);

            // Mock image selection intent
            Intent mockIntent = new Intent();
            mockIntent.setData(Uri.parse("content://media/external/images/media/1"));

            // Call onActivityResult with mock data
            fragment.onActivityResult(3, RESULT_OK, mockIntent);

            // Verify that the image button's URI is set correctly
            ImageButton imageButton = view.findViewById(R.id.attendeeProfileImage);
            assertEquals(mockIntent.getData(), imageButton.getImageURI());
        });
    }
}

