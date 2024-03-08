package com.example.eventease2;

import com.example.eventease2.Attendee.AttendeeItemViewModel;

public class AttendeeItemViewModelTest {
    public static void main(String[] args) {
        // Test case 1: Create an attendee item view model and set profile details
        AttendeeItemViewModel viewModel = new AttendeeItemViewModel();
        viewModel.setProfileName("Alice");
        viewModel.setProfileEmail("alice@example.com");
        viewModel.setProfilePhone("9876543210");
        System.out.println("Profile Name: " + viewModel.getProfileName()); // Output should be "Alice"
        System.out.println("Profile Email: " + viewModel.getProfileEmail()); // Output should be "alice@example.com"
        System.out.println("Profile Phone: " + viewModel.getProfilePhone()); // Output should be "9876543210"
    }
}
