package com.example.eventease2;

import com.example.eventease2.Administrator.AppData;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class AppDataTest {
    private AppData appData;


    @Before
    public void setUp() {
        appData = new AppData();
    }


    @Test
    public void testDeleteOrganizer() {
        appData.getOrganizerList().add("Organizer 1");
        appData.getOrganizerList().add("Organizer 2");


        appData.deleteOrganizer(0);


        ArrayList<String> expected = new ArrayList<>();
        expected.add("Organizer 2");


        assert appData.getOrganizerList().equals(expected) : "testDeleteOrganizer failed";
    }


    @Test
    public void testDeleteEventName() {
        appData.getEventNameList().add("Event 1");
        appData.getEventNameList().add("Event 2");


        appData.deleteEventName(1);


        ArrayList<String> expected = new ArrayList<>();
        expected.add("Event 1");


        assert appData.getEventNameList().equals(expected) : "testDeleteEventName failed";
    }


    @Test
    public void testDeleteEventInfo() {
        appData.getEventInfoList().add("Info 1");
        appData.getEventInfoList().add("Info 2");


        appData.deleteEventInfo(0);


        ArrayList<String> expected = new ArrayList<>();
        expected.add("Info 2");


        assert appData.getEventInfoList().equals(expected) : "testDeleteEventInfo failed";
    }


    @Test
    public void testDeleteEventID() {
        appData.getEventIDs().add("ID1");
        appData.getEventIDs().add("ID2");


        appData.deleteEventID(1);


        ArrayList<String> expected = new ArrayList<>();
        expected.add("ID1");


        assert appData.getEventIDs().equals(expected) : "testDeleteEventID failed";
    }
}
