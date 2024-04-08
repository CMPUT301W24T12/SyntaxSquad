
package com.example.eventease2.Attendee;

import java.util.ArrayList;
/**
 * The AttendeeAppData class represents the data structure for managing attendee-related information
 * in an event management application.
 * <p>
 * It contains ArrayLists to store various types of information related to organizers, events, and attendees.
 * This class provides methods to manipulate and retrieve data from these lists.
 * </p>
 * <p>
 * The ArrayLists include:
 * <ul>
 *   <li>{@code organizerList}: List of organizers</li>
 *   <li>{@code eventNameList}: List of event names</li>
 *   <li>{@code eventInfoList}: List of event information</li>
 *   <li>{@code eventIDs}: List of event IDs</li>
 *   <li>{@code maxAttendeeList}: List of maximum attendees allowed for each event</li>
 *   <li>{@code entriesAttendeeList}: List of entries by attendees</li>
 * </ul>
 * </p>
 * <p>
 * This class also provides methods to retrieve, set, and delete elements from these lists.
 * </p>
 * @author Sean
 */
public class AttendeeAppData {
    // ArrayLists to store various information related to events and attendees
    private ArrayList<String> organizerList;
    private ArrayList<String> eventNameList;
    private ArrayList<String> eventInfoList;
    private ArrayList<String> eventIDs;
    private ArrayList<String> maxAttendeeList;
    private ArrayList<String> entriesAttendeeList;

    /**
     * Constructs an AttendeeAppData object with empty ArrayLists for each type of information.
     */
    public AttendeeAppData() {
        organizerList = new ArrayList<>();
        eventNameList = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        eventIDs = new ArrayList<>();
        maxAttendeeList = new ArrayList<>();
        entriesAttendeeList = new ArrayList<>();
    }

    /**
     * Retrieves the list of organizers.
     * @return The list of organizers.
     */
    public ArrayList<String> getOrganizerList() {
        return organizerList;
    }

    /**
     * Sets the list of organizers.
     * @param organizerList The list of organizers to be set.
     */
    public void setOrganizerList(ArrayList<String> organizerList) {
        this.organizerList = organizerList;
    }

    /**
     * Retrieves the list of event names.
     * @return The list of event names.
     */
    public ArrayList<String> getEventNameList() {
        return eventNameList;
    }

    /**
     * Sets the list of event names.
     * @param eventNameList The list of event names to be set.
     */
    public void setEventNameList(ArrayList<String> eventNameList) {
        this.eventNameList = eventNameList;
    }

    /**
     * Retrieves the list of event information.
     * @return The list of event information.
     */
    public ArrayList<String> getEventInfoList() {
        return eventInfoList;
    }

    /**
     * Sets the list of event information.
     * @param eventInfoList The list of event information to be set.
     */
    public void setEventInfoList(ArrayList<String> eventInfoList) {
        this.eventInfoList = eventInfoList;
    }

    /**
     * Retrieves the list of event IDs.
     * @return The list of event IDs.
     */
    public ArrayList<String> getEventIDs() {
        return eventIDs;
    }

    /**
     * Sets the list of event IDs.
     * @param eventIDs The list of event IDs to be set.
     */
    public void setEventIDs(ArrayList<String> eventIDs) {
        this.eventIDs = eventIDs;
    }

    /**
     * Retrieves the list of maximum attendees allowed for each event.
     * @return The list of maximum attendees.
     */
    public ArrayList<String> getMaxAttendeeList() {
        return maxAttendeeList;
    }

    /**
     * Sets the list of maximum attendees allowed for each event.
     * @param maxAttendeeList The list of maximum attendees to be set.
     */
    public void setMaxAttendeeList(ArrayList<String> maxAttendeeList) {
        this.maxAttendeeList = maxAttendeeList;
    }

    /**
     * Deletes the organizer at the specified index.
     * @param index The index of the organizer to be deleted.
     */
    public void deleteOrganizer(int index) {
        if (index >= 0 && index < organizerList.size()) {
            organizerList.remove(index);
        }
    }

    /**
     * Deletes the event name at the specified index.
     * @param index The index of the event name to be deleted.
     */
    public void deleteEventName(int index) {
        if (index >= 0 && index < eventNameList.size()) {
            eventNameList.remove(index);
        }
    }

    /**
     * Deletes the event information at the specified index.
     * @param index The index of the event information to be deleted.
     */
    public void deleteEventInfo(int index) {
        if (index >= 0 && index < eventInfoList.size()) {
            eventInfoList.remove(index);
        }
    }

    /**
     * Deletes the event ID at the specified index.
     * @param index The index of the event ID to be deleted.
     */
    public void deleteEventID(int index) {
        if (index >= 0 && index < eventIDs.size()) {
            eventIDs.remove(index);
        }
    }

    /**
     * Deletes the participant count at the specified index.
     * @param index The index of the participant count to be deleted.
     */
    public void deleteParticipantCount(int index) {
        if (index >= 0 && index < maxAttendeeList.size()) {
            maxAttendeeList.remove(index);
        }
    }

    /**
     * Retrieves the list of entries by attendees.
     * @return The list of entries by attendees.
     */
    public ArrayList<String> getEntriesAttendeeList() {
        return entriesAttendeeList;
    }

    /**
     * Sets the list of entries by attendees.
     * @param entriesAttendeeList The list of entries by attendees to be set.
     */
    public void setEntriesAttendeeList(ArrayList<String> entriesAttendeeList) {
        this.entriesAttendeeList = entriesAttendeeList;
    }
}
