package com.example.eventease2.Attendee;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

/**
 * This view model allows many attendee variables to be shared with QR, Profile, and Event
 * fragments. The Start activity also accesses the information in order to provide info for other fragments.
 * <p>
 * This class holds various attendee-related information such as event details, organizer information,
 * profile details, and check-in status. It is designed to facilitate communication between different
 * fragments and activities within the event management application.
 * <p>
 *
 * @author Sean
 */
public class AttendeeItemViewModel extends ViewModel {
    // Attendee information variables
    private String event = "";
    private String organizer = "";
    private String profileName = "";
    private String profilePhone = "";
    private String profileEmail = "";
    private String profileBio = "";
    private String attendeeID = "";
    private Uri profileImage = null;
    @SuppressLint("StaticFieldLeak")
    private ImageView tempProfileImage;
    private int checkIN = 0;

    // Getters and setters for attendee information

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfilePhone() {
        return profilePhone;
    }

    public void setProfilePhone(String profilePhone) {
        this.profilePhone = profilePhone;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getProfileBio() {
        return profileBio;
    }

    public void setProfileBio(String profileBio) {
        this.profileBio = profileBio;
    }

    public Uri getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Uri profileImage) {
        this.profileImage = profileImage;
    }

    public String getAttendeeID() {
        return attendeeID;
    }

    public void setAttendeeID(String attendeeID) {
        this.attendeeID = attendeeID;
    }

    public int getCheckIN() {
        return checkIN;
    }

    public void setCheckIN(int checkIN) {
        this.checkIN = checkIN;
    }

    public ImageView getTempProfileImage() {
        return tempProfileImage;
    }

    public void setTempProfileImage(ImageView tempProfileImage) {
        this.tempProfileImage = tempProfileImage;
    }
}
