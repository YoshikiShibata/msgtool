// File: MeetingManagerImpl.java - last edit:
// Yoshiki Shibata 30-Dec-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.meeting;

import msgtool.protocol.MeetingListener;
import msgtool.protocol.MeetingProtocol;
import msgtool.ui.MainUI;
import msgtool.ui.MeetingRoomUI;
import msgtool.ui.MeetingRoomUIManager;
import msgtool.util.SwingWrapper;

public class MeetingManagerImpl<T> implements MeetingManager{
	
	public MeetingManagerImpl(
		MeetingRoomUIManager<T>	meetingRoomUIManager,
		MainUI					mainUI) {
		fMeetingRoomUIManager	= meetingRoomUIManager;
		fMainUI					= mainUI;
	}

    public MeetingRoomUI findMeetingRoomUI(String internalRoomName) {
		return fMeetingRoomUIManager.find(internalRoomName);
    }

    public MeetingRoomUI findMeetingRoomUIByExternalName(String  externalRoomName) {
		return fMeetingRoomUIManager.findByExternalName(externalRoomName);
    }
            
    public void joinRoom(String roomName, boolean closed, boolean fetchLog) {
        final MeetingRoomUI meetingRoom = fMeetingRoomUIManager.findOrCreate(roomName, closed, fetchLog);
        
		if (fMainUI.isInOffice())  
            meetingRoom.joinRoom();

	  	Runnable r = new Runnable() {
 	    	public void run() {
				meetingRoom.setVisible(true);
				}
		};
        SwingWrapper.invokeLater(r);
    }
        
    public void deleteRoom(String roomName, boolean closed) {
        String  internalRoomName = null;
        
        if (closed)
            internalRoomName = MeetingListener.kStr_ClosedPrefix + roomName;
        else
            internalRoomName = roomName;
        fMeetingProtocol.roomDeleted(internalRoomName);
    }
    
    public boolean deleteRoom(String internalRoomName) {
        MeetingRoomUI    meetingRoom = findMeetingRoomUI(internalRoomName);

        if (meetingRoom == null)
            return true ;

        if (meetingRoom.isInRoom())
            return false ;
        
        meetingRoom.setVisible(false);
        meetingRoom.setEnabled(false);
        meetingRoom.clearSavedLog();
		fMainUI.removeMeetingRoomUI(meetingRoom);            
        return true;
   }

	private final MeetingRoomUIManager<T>	fMeetingRoomUIManager;
	private final MainUI				fMainUI;
	private final MeetingProtocol		fMeetingProtocol = MeetingProtocol.getInstance();
}

// LOG
// 2.39 : 30-Dec-99	Y.Shibata	created.