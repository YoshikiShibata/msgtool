// File: MeetingRoomUIManager.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1999, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.ui;

import java.awt.Frame;
import java.util.Hashtable;
import java.util.ArrayList;

import msgtool.protocol.MeetingListener;
import msgtool.util.CursorControl;
import msgtool.util.StringDefs;

public class MeetingRoomUIManager<K> {

	public MeetingRoomUIManager(
		MainUI	mainUI,
		Frame	parentFrame,
		UIFactory<K>	uiFactory,
		Object		topMenu) {
		fMainUI 			= mainUI;
		fParentFrame		= parentFrame;
		fUIFactory			= uiFactory;
		fTopMenu			= topMenu;
	}

    public MeetingRoomUI findOrCreate(
        String roomName, 
        boolean closed,
		boolean	fetchLog) {
        String  internalRoomName = null;
        String  externalRoomName = null;
            
        if (closed) {
            internalRoomName = MeetingListener.kStr_ClosedPrefix + roomName;
            externalRoomName = StringDefs.CLOSED + roomName;
        } else {
            internalRoomName = roomName;
            externalRoomName = roomName;
        }
        MeetingRoomUI    meetingRoomUI = find(internalRoomName);
        
        if (meetingRoomUI == null || !meetingRoomUI.isEnabled()) { 
            if (meetingRoomUI == null) {
                meetingRoomUI = fUIFactory.createMeetingRoomUI(
                                    fParentFrame, 
                                    internalRoomName, externalRoomName, 
                                    fTopMenu);
				synchronized (this) {
			   		fUIs.add(meetingRoomUI);
			   	}
			  	if (fetchLog) {
                	fCursorControl.setBusy(true);
                	meetingRoomUI.getLogMeeting().acquireLog();
                	fCursorControl.setBusy(false);
               	}
			}       
            meetingRoomUI.setEnabled(true);
			fMainUI.addMeetingRoomUI(meetingRoomUI);
      	}
        return meetingRoomUI;
    }

   	public synchronized MeetingRoomUI find(String internalRoomName) {
        for (MeetingRoomUI i: fUIs) {
			if (i.getInternalRoomName().equals(internalRoomName))
				return i;
		}
		return null ;
  	}

	public synchronized MeetingRoomUI findByExternalName(String  externalRoomName) {
        for (MeetingRoomUI i: fUIs) {
			if (i.getExternalRoomName().equals(externalRoomName))
				return i;
		}
		return null;
 	}

	public synchronized void leaveAll() {
        for (MeetingRoomUI i: fUIs) 
            i.leaveRoom();
 	}

	public synchronized void setNotInOfficeToAll(boolean notInOffice) {
        for (MeetingRoomUI i: fUIs) 
            i.setNotInOffice(notInOffice);
 	}

	public void put(K key, MeetingRoomUI ui) {
		fMenuMap.put(key, ui);
	}

	public MeetingRoomUI get(K key) {
		return fMenuMap.get(key);
	}

	private final MainUI	fMainUI;
	private final Frame		fParentFrame;
	private final UIFactory<K>	fUIFactory;
	private final Object	fTopMenu;

	private ArrayList<MeetingRoomUI> fUIs = new ArrayList<MeetingRoomUI>();
	private Hashtable<K,MeetingRoomUI>		fMenuMap	= new Hashtable<K, MeetingRoomUI>();
	private CursorControl	fCursorControl	= CursorControl.instance();
}

// LOG
// 2.36 : 23-Nov-99	Y.Shibata	created.
// 2.39 : 30-Dec-99	Y.Shibata	got rid of MeetingRoomListUI
// 2.50 : 19-Nov-03	Y.Shibata	modified to be compiled cleanly with "Tiger"(SDK1.5).
//      : 27-Dec-03 Y.Shibata   used Java Generics and the enhanced "for".
