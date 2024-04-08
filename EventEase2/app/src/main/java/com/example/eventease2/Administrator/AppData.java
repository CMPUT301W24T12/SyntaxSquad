package com.example.eventease2.Administrator;

import java.util.ArrayList;

/**
 * The AppData class represents the data structure for storing information related to events in the application.
 * It provides methods to manipulate and access this data.
 */

public class AppData {
    private ArrayList<String> organizerList;
    private ArrayList<String> eventNameList;
    private ArrayList<String> eventInfoList;
    private ArrayList<String> eventIDs;
    private ArrayList<String> participantCountList;


    /**
     * Constructs a new instance of the AppData class with empty lists initialized.
     */
    public AppData() {
        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        participantCountList = new ArrayList<>();
    }

    /**
     * Gets the list of organizers.
     * @return The list of organizers.
     */
    public ArrayList<String> getOrganizerList() {
        return organizerList;
    }

    /**
     * Sets the list of organizers.
     * @param organizerList The list of organizers to set.
     */
    public void setOrganizerList(ArrayList<String> organizerList) {
        this.organizerList = organizerList;
    }

    /**
     * Gets the list of event names.
     * @return The list of event names.
     */
    public ArrayList<String> getEventNameList() {
        return eventNameList;
    }

    /**
     * Sets the list of event names.
     * @param eventNameList The list of event names to set.
     */
    public void setEventNameList(ArrayList<String> eventNameList) {
        this.eventNameList = eventNameList;
    }

    /**
     * Gets the list of event information.
     * @return The list of event information.
     */
    public ArrayList<String> getEventInfoList() {
        return eventInfoList;
    }

    /**
     * Sets the list of event information.
     * @param eventInfoList The list of event information to set.
     */
    public void setEventInfoList(ArrayList<String> eventInfoList) {
        this.eventInfoList = eventInfoList;
    }

    /**
     * Gets the list of event IDs.
     * @return The list of event IDs.
     */
    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }

    /**
     * Sets the list of event IDs.
     * @param eventIDs The list of event IDs to set.
     */
    public void setEventIDs(ArrayList<String> eventIDs) {
        this.eventIDs = eventIDs;
    }

    /**
     * Deletes an organizer at the specified index.
     * @param index The index of the organizer to delete.
     */
    public void deleteOrganizer(int index) {
        if (index >= 0 && index < organizerList.size()) {
            organizerList.remove(index);
        }
    }

    /**
     * Deletes an event name at the specified index.
     * @param index The index of the event name to delete.
     */
    public void deleteEventName(int index) {
        if (index >= 0 && index < eventNameList.size()) {
            eventNameList.remove(index);
        }
    }

    /**
     * Deletes an event information at the specified index.
     * @param index The index of the event information to delete.
     */
    public void deleteEventInfo(int index) {
        if (index >= 0 && index < eventInfoList.size()) {
            eventInfoList.remove(index);
        }
    }

    /**
     * Deletes an event ID at the specified index.
     * @param index The index of the event ID to delete.
     */
    public void deleteEventID(int index) {
        if (index >= 0 && index < eventIDs.size()) {
            eventIDs.remove(index);
        }
    }

    /**
     * Deletes a participant count at the specified index.
     * @param index The index of the participant count to delete.
     */
    public void deleteParticipantCount(int index) {
        if (index >= 0 && index < participantCountList.size()) {
            participantCountList.remove(index);
        }
    }
}
