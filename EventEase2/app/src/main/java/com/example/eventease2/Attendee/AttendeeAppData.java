package com.example.eventease2.Attendee;

import java.util.ArrayList;

public class AttendeeAppData {
    private ArrayList<String> organizerList;
    private ArrayList<String> eventNameList;
    private ArrayList<String> eventInfoList;
    private ArrayList<String> eventIDs;
    private ArrayList<String> maxAttendeeList;
    private ArrayList<String> entriesAttendeeList;

    public AttendeeAppData() {
        organizerList = new ArrayList<String >();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        maxAttendeeList = new ArrayList<>();
        entriesAttendeeList = new ArrayList<>();
    }

    public ArrayList<String> getOrganizerList() {
        return organizerList;
    }

    public void setOrganizerList(ArrayList<String> organizerList) {
        this.organizerList = organizerList;
    }

    public ArrayList<String> getEventNameList() {
        return eventNameList;
    }

    public void setEventNameList(ArrayList<String> eventNameList) {
        this.eventNameList = eventNameList;
    }

    public ArrayList<String> getEventInfoList() {
        return eventInfoList;
    }

    public void setEventInfoList(ArrayList<String> eventInfoList) {
        this.eventInfoList = eventInfoList;
    }

    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }

    public void setEventIDs(ArrayList<String> eventIDs) {
        this.eventIDs = eventIDs;
    }

    public ArrayList<String> getMaxAttendeeList() {
        return maxAttendeeList;
    }

    public void setMaxAttendeeList(ArrayList<String> maxAttendeeList) {
        this.maxAttendeeList = maxAttendeeList;
    }

    // Method to delete organizer at a specific index
    public void deleteOrganizer(int index) {
        if (index >= 0 && index < organizerList.size()) {
            organizerList.remove(index);
        }
    }

    // Method to delete event name at a specific index
    public void deleteEventName(int index) {
        if (index >= 0 && index < eventNameList.size()) {
            eventNameList.remove(index);
        }
    }

    // Method to delete event info at a specific index
    public void deleteEventInfo(int index) {
        if (index >= 0 && index < eventInfoList.size()) {
            eventInfoList.remove(index);
        }
    }

    // Method to delete event ID at a specific index
    public void deleteEventID(int index) {
        if (index >= 0 && index < eventIDs.size()) {
            eventIDs.remove(index);
        }
    }

    // Method to delete participant count at a specific index
    public void deleteParticipantCount(int index) {
        if (index >= 0 && index < maxAttendeeList.size()) {
            maxAttendeeList.remove(index);
        }
    }

    public ArrayList<String> getEntriesAttendeeList() {
        return entriesAttendeeList;
    }

    public void setEntriesAttendeeList(ArrayList<String> entriesAttendeeList) {
        this.entriesAttendeeList = entriesAttendeeList;
    }
}
