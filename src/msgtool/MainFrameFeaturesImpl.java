/*
 * File: MainFrameFeaturesImpl.java - last edit:
 * Yoshiki Shibata 23-Mar-2014
 *
 * Copyright (c) 2000, 2004, 2014 by Yoshiki Shibata. All rights reserved.
 */
package msgtool;

import java.awt.event.ActionEvent;

import msgtool.common.AddressNotifier;
import msgtool.db.MeetingRoomDB;
import msgtool.db.PropertiesDB;
import msgtool.log.LogArea;
import msgtool.ui.MeetingRoomUIManager;
import msgtool.util.NetUtil;

public class MainFrameFeaturesImpl<T> implements MainFrameFeatures {

    private final LogArea logArea;
    private final MeetingRoomUIManager<T> meetingRoomUIManager;

    public MainFrameFeaturesImpl(
            LogArea logArea,
            MeetingRoomUIManager<T> meetingRoomUIManager) {
        this.logArea = logArea;
        this.meetingRoomUIManager = meetingRoomUIManager;
    }

    @Override
    public void showMyAddress() {
        String myIPAddress = NetUtil.getMyIPAddress();

        if (myIPAddress != null) {
            logArea.appendText("----- running on " + myIPAddress + " -----\n\n");
        } else {
            logArea.appendText("----- running on unknown host address -----\n\n");
        }
    }

    @Override
    public void checkIfIPAddressChanged() {
        String myIPAddress = NetUtil.getMyIPAddress();

        // Set My IP Address
        if (myIPAddress == null) {
            return;
        }

        PropertiesDB propertiesDB = PropertiesDB.getInstance();
        String myPrevIPAddress = propertiesDB.getMyIPAddress();

        if (myPrevIPAddress == null) {
            propertiesDB.setMyIPAddress(myIPAddress);
            propertiesDB.setMyOldIPAddress(myIPAddress);
            propertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        } else if (!myPrevIPAddress.equals(myIPAddress)) {
            // IP address is changed.
            propertiesDB.setMyIPAddress(myIPAddress);
            propertiesDB.setMyOldIPAddress(myPrevIPAddress);
            propertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
            AddressNotifier.makeAddressList(myPrevIPAddress, myIPAddress);
        }
    }

    /**
     * Joins meeting rooms automatically.
     */
    @Override
    public void joinMeetingRooms() {
        MeetingRoomDB meetingRoomDB = MeetingRoomDB.getInstance();
        String[] rooms = meetingRoomDB.getRooms();
        for (String room : rooms) {
            meetingRoomUIManager.findOrCreate(
                    room,
                    false,
                    meetingRoomDB.getFetchLogValue(room)).joinRoom();
        }
    }
}

// LOG
// 2.40 :  3-Jan-00	Y.Shibata	created.
// 2.52 : 13-Nov-04	Y.Shibata	refactored.
// 2.60 : 23-Mar-14 Y.Shibata   refactored with Java 8
