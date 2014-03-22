// File: MeetingListener.java -- last edit:
// Yoshiki Shibata 12-Jul-98

// Copyright (c) 1997 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

public interface MeetingListener {

    public static String    kStr_ClosedPrefix = "CLOSED ";

    public String[] getAllMeetingRooms();
    
    public void onJoin(String internalRoomName, String participant, String ip) ; 
    public void onLeave(String internalRoomName, String participant, String ip);  
    public void onMessage(String internalRoomName, String participant, String ip, String message);
    public void onParticipants(String internalRoomName, String ip);  
    public void onParticipated(String internalRoomName, String ip, String participant);
    public void onRoomOpened(String internalRoomName, String ip); 
    public void onRoomDeleted(String internalRoomName, String ip);
    public void onLogLengthRequest(String internalRoomName, String ip);
    public void onLogLengthAnswer(String internalRoomName, String ip, int length);
    }

// LOG
// 1.65 : 28-Sep-97 Y.Shibata   created.
// 1.68 :  9-Oct-97 Y.Shibata   modified onRoomOpened
// 1.74 :  3-Nov-97 Y.Shibata   added onLogLengthRequest & onLogLengthAnswer
// 1.95 : 12-Jul-98 Y.Shibata   moved msgtool.protocol
