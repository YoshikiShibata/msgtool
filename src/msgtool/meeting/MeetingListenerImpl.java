// File: MeetingListenerImpl.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1997, 1999, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.meeting;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import msgtool.db.PropertiesDB;
import msgtool.protocol.MeetingListener;
import msgtool.protocol.MeetingProtocol;
import msgtool.ui.MeetingRoomUI;
import msgtool.util.StringDefs;
import msgtool.util.SwingWrapper;

public final class MeetingListenerImpl implements MeetingListener {

    public MeetingListenerImpl(
        MeetingManager  meetingManager) {
        fMeetingManager = meetingManager;
   	}
        
    public String[] getAllMeetingRooms() {
        synchronized (fAllMeetingRooms) {
            int noOfRooms = fAllMeetingRooms.size();
            
            if (noOfRooms == 0)
                return null;
             
            String[]    rooms = new String[noOfRooms];
            for (int i = 0; i < noOfRooms ; i++)
                rooms[i] = fAllMeetingRooms.elementAt(i);
            
            return rooms;
      	}
 	}
        
    public void onJoin(
        final String  internalRoomName,
        final String  participant,
        final String  ip) {
        final MeetingRoomUI    meeting = fMeetingManager.findMeetingRoomUI(internalRoomName);
        
        addMeetingRoom(internalRoomName);
        if (meeting != null) {
            Runnable methodBody = new Runnable() {
                public void run() {
                    meeting.appendLogText(false, fDateFormat.format(new Date()) + " " + 
                        participant + "(" + ip + ") " + StringDefs.JOINED + "\n\n", ip);
                    meeting.getParticipantsUI().join(participant);
                }
            };
            SwingWrapper.invokeLater(methodBody);
        }
    }
    
    public void onLeave(
        final String  internalRoomName,
        final String  participant,
        final String  ip) {
        final MeetingRoomUI    meeting = fMeetingManager.findMeetingRoomUI(internalRoomName);
        
        addMeetingRoom(internalRoomName);
        if (meeting != null) {
            Runnable methodBody = new Runnable() {
                public void run() {
                    meeting.appendLogText(false, fDateFormat.format(new Date()) + " " +
                        participant + "(" + ip + ") " + StringDefs.LEFT + "\n\n", ip);
                    meeting.getParticipantsUI().leave(participant);
                }
            };
            SwingWrapper.invokeLater(methodBody);
        }
   }
        
    public void onMessage(
        final String  internalRoomName,
        final String  participant,
        final String  ip,
        final String  message) {
        final MeetingRoomUI    meeting = fMeetingManager.findMeetingRoomUI(internalRoomName);
        
        addMeetingRoom(internalRoomName);
        if (meeting != null) {
            Runnable methodBody = new Runnable() {
                public void run() {
                    meeting.appendLogText(true, fDateFormat.format(new Date()) + " " +
                        participant + "(" + ip + ")\n" + message + "\n", ip);
                    meeting.getParticipantsUI().join(participant);
                }
            };
            SwingWrapper.invokeLater(methodBody);
        }
    }
    
    public void onParticipants(
        String  internalRoomName,
        String  ip) {
        MeetingRoomUI    meeting = fMeetingManager.findMeetingRoomUI(internalRoomName);
        
        addMeetingRoom(internalRoomName);
        if (meeting != null && meeting.isInRoom()) 
            fMeetingProtocol.participated(internalRoomName, 
                        fPropertiesDB.getUserName(), ip);
    }
    
    public void onParticipated(
        final String  internalRoomName,
        final String  ip,
        final String  participant) {
        final MeetingRoomUI    meeting = fMeetingManager.findMeetingRoomUI(internalRoomName);
        
        addMeetingRoom(internalRoomName);
        if (meeting != null) {
            Runnable methodBody = new Runnable() {
                public void run() {
                    meeting.getParticipantsUI().join(participant);
                }
            };
            SwingWrapper.invokeLater(methodBody);
        }
   } 
        
    public void onRoomOpened(
        String  internalRoomName,
        String  ip) {
        //
        // Please note that zero length name means a command which ask for known rooms.
        //
        if (internalRoomName.length() > 0)
            addMeetingRoom(internalRoomName);
        else {
            String[]    rooms = getAllMeetingRooms();
            if (rooms != null) {
                for (int i = 0; i < rooms.length; i++)
                    fMeetingProtocol.roomOpened(ip, rooms[i]);
             }
        }
   }
        
    public void onRoomDeleted(
        String  internalRoomName,
        String  ip) {
        if (fMeetingManager.deleteRoom(internalRoomName))
            removeMeetingRoom(internalRoomName);
    }
        
    public void onLogLengthRequest(
        String  internalRoomName,
        String  ip) {
        MeetingRoomUI    meeting = fMeetingManager.findMeetingRoomUI(internalRoomName);
        if (meeting != null)
            meeting.getLogMeeting().onLogLengthRequest(ip);
    }
        
    public void onLogLengthAnswer(
        String  internalRoomName,
        String  ip,
        int     length) {
        MeetingRoomUI    meeting = fMeetingManager.findMeetingRoomUI(internalRoomName);
        
        if (meeting != null)
            meeting.getLogMeeting().onLogLengthAnswer(ip, length);
    }

    private void addMeetingRoom(String  internalRoomName) {
        //
        // If Room name starts with "CLOSED:", then don't add
        //
        if (internalRoomName.startsWith(kStr_ClosedPrefix))
            return;
            
        synchronized (fAllMeetingRooms) {
            if (fAllMeetingRooms.indexOf(internalRoomName) == -1)
                fAllMeetingRooms.addElement(internalRoomName);
        }
    }
    
    private void removeMeetingRoom(String internalRoomName) {
        if (internalRoomName.startsWith(kStr_ClosedPrefix))
            return;
            
        synchronized (fAllMeetingRooms) {
            int index = fAllMeetingRooms.indexOf(internalRoomName);
            
            if (index != -1)
                fAllMeetingRooms.removeElementAt(index);
        }
    }

    private final MeetingManager  fMeetingManager;
    private DateFormat  fDateFormat = DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());
    //
    // fAllMeetingRooms is used to know all existing meeting rooms so that
    // a popup hint can be shown.
    //
    private Vector<String>  fAllMeetingRooms = new Vector<String>();
    
    private MeetingProtocol fMeetingProtocol    = MeetingProtocol.getInstance();
    private PropertiesDB    fPropertiesDB       = PropertiesDB.getInstance();
}
    

// LOG
// --- V1.67 ---
//  4-Oct-97    Y.Shibata   created.
// --- V1.68 ---
//  9-Oct-97    Y.Shibata   modified onRoomOpened.
// --- V1.69 ---
// 10-Oct-97    Y.Shibata   some kinds of messages such as Join will not be notified.
// 2.33 : 21-Aug-99	Y.Shibata	Refactoring .....
// 2.36 : 21-Nov-99	Y.Shibata	used SwingWrapper for both Swing/AWT versions
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics

