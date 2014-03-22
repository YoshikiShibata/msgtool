// File: MeetingManager.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997, 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.meeting;

import msgtool.ui.MeetingRoomUI;

public interface MeetingManager {

    public MeetingRoomUI findMeetingRoomUI(String roomName);
    public MeetingRoomUI findMeetingRoomUIByExternalName(String  externalRoomName);
    public void joinRoom(String roomName, boolean privateRoom, boolean fetchLog);
    public void deleteRoom(String roomName, boolean privateRoom);
    public boolean deleteRoom(String internalRoomName);
    }


// LOG
// 1.67 :  4-Oct-97 Y.Shibata   created.
// 2.35 : 24-Oct-99	Y.Shibata	MeetingRoomFrame -> MeetingRoomUI
