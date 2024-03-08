package com.example.eventease2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.eventease2.Attendee.AttendeeItemViewModel;
import com.example.eventease2.Attendee.AttendeeQRFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class AttendeeQRFragmentTest {

    @Test
    public void testQRScanner() {
        // Start a Fragment scenario
        ActivityScenario.launch(FragmentTestActivity.class).onActivity(activity -> {
            // Mock ViewModelProvider
            ViewModelProvider viewModelProvider = mock(ViewModelProvider.class);
            AttendeeItemViewModel viewModel = mock(AttendeeItemViewModel.class);
            Mockito.when(viewModelProvider.get(AttendeeItemViewModel.class)).thenReturn(viewModel);

            // Create an instance of AttendeeQRFragment
            AttendeeQRFragment fragment = new AttendeeQRFragment();
            fragment.viewModel = viewModel; // Set the mocked ViewModel
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.fragment_attendee_q_r, null);
            fragment.onViewCreated(view, null);

            // Mock IntentIntegrator
            IntentIntegrator integrator = mock(IntentIntegrator.class);
            Mockito.when(IntentIntegrator.forSupportFragment(any(Fragment.class))).thenReturn(integrator);
            Button scanButton = view.findViewById(R.id.btnScanQR);
            scanButton.performClick();

            // Verify that startQRScanner() is called
            verify(integrator).initiateScan();
        });
    }

    @Test
    public void testQRScannerResult() {
        // Start a Fragment scenario
        ActivityScenario.launch(FragmentTestActivity.class).onActivity(activity -> {
            // Mock ViewModelProvider
            ViewModelProvider viewModelProvider = mock(ViewModelProvider.class);
            AttendeeItemViewModel viewModel = mock(AttendeeItemViewModel.class);
            Mockito.when(viewModelProvider.get(AttendeeItemViewModel.class)).thenReturn(viewModel);

            // Create an instance of AttendeeQRFragment
            AttendeeQRFragment fragment = new AttendeeQRFragment();
            fragment.viewModel = viewModel; // Set the mocked ViewModel
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.fragment_attendee_q_r, null);
            fragment.onViewCreated(view, null);

            // Mock IntentResult
            IntentResult result = mock(IntentResult.class);
            Mockito.when(result.getContents()).thenReturn("eventID#organizerID");
            Intent mockIntent = new Intent();
            mockIntent.putExtra("SCAN_RESULT", "eventID#organizerID");
            fragment.onActivityResult(0, 0, mockIntent);

            // Verify that sendDataToModel() is called with the correct data
            verify(viewModel).setEvent("eventID");
            verify(viewModel).setOrganizer("organizerID");
        });
    }
}

