package com.example.eventease2;

import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.eventease2.Attendee.AttendeeEventFragment;
import com.example.eventease2.Attendee.AttendeeItemViewModel;
import com.example.eventease2.Attendee.AttendeeProfileFragment;
import com.example.eventease2.Attendee.AttendeeQRFragment;
import com.example.eventease2.Attendee.AttendeeStartActivity;
import com.example.eventease2.R;
import com.example.eventease2.databinding.ActivityAttendeeStartBinding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class AttendeeStartActivityTest {

    @Test
    public void testBottomNavigationQRScanner() {
        // Start an Activity scenario
        ActivityScenario<AttendeeStartActivity> activityScenario = ActivityScenario.launch(AttendeeStartActivity.class);
        activityScenario.onActivity(activity -> {
            // Mock ViewModelProvider
            ViewModelProvider viewModelProvider = mock(ViewModelProvider.class);
            AttendeeItemViewModel viewModel = mock(AttendeeItemViewModel.class);
            Mockito.when(viewModelProvider.get(AttendeeItemViewModel.class)).thenReturn(viewModel);
            activity.viewModel = viewModel; // Set the mocked ViewModel

            // Mock FragmentManager and FragmentTransaction
            FragmentManager fragmentManager = mock(FragmentManager.class);
            FragmentTransaction fragmentTransaction = mock(FragmentTransaction.class);
            Mockito.when(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction);
            activity.getSupportFragmentManager = () -> fragmentManager;

            // Trigger bottom navigation item selection for QR Scanner
            FrameLayout frameLayout = new FrameLayout(activity);
            activity.binding = new ActivityAttendeeStartBinding();
            activity.binding.frameLayout = frameLayout;
            activity.binding.bottomNavigationView.setSelectedItemId(R.id.QR_Scanner);
            activity.binding.bottomNavigationView.getOnItemSelectedListener().onNavigationItemSelected(null);

            // Verify that replaceFragment() is called with AttendeeQRFragment
            verify(fragmentTransaction).replace(R.id.frameLayout, new AttendeeQRFragment());
            verify(fragmentTransaction).commit();
        });
    }

    // Add similar tests for the other bottom navigation items
}

