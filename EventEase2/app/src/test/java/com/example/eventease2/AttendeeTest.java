package com.example.eventease2;

import com.example.eventease2.Attendee.Attendee;

public class AttendeeTest {
    public static void main(String[] args) {
        // Test case 1: Create an attendee with default constructor
        Attendee attendee1 = new Attendee();
        System.out.println("Attendee Name: " + attendee1.getName()); // Output should be an empty string

        // Test case 2: Set attendee details using setter methods
        Attendee attendee2 = new Attendee();
        attendee2.setName("John Doe");
        attendee2.setEmail("john@example.com");
        attendee2.setPhone("1234567890");
        System.out.println("Attendee Name: " + attendee2.getName()); // Output should be "John Doe"
        System.out.println("Attendee Email: " + attendee2.getEmail()); // Output should be "john@example.com"
        System.out.println("Attendee Phone: " + attendee2.getPhone()); // Output should be "1234567890"
    }
}
