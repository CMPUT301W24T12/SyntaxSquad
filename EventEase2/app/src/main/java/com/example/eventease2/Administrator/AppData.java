package com.example.eventease2.Administrator;

import java.util.ArrayList;

public class AppData {
    private ArrayList<String> organizerList;
    private ArrayList<String> eventNameList;
    private ArrayList<String> eventInfoList;
    private ArrayList<String> eventIDs;
    private ArrayList<String> participantCountList;

    public AppData() {
        organizerList = new ArrayList<String >();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        participantCountList = new ArrayList<>();
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

    public ArrayList<String> getParticipantCountList() {
        return participantCountList;
    }

    public void setParticipantCountList(ArrayList<String> participantCountList) {
        this.participantCountList = participantCountList;
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
        if (index >= 0 && index < participantCountList.size()) {
            participantCountList.remove(index);
        }
    }
}
