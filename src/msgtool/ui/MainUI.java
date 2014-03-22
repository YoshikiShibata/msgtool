// File: MainUI.java - last edit:
// Yoshiki Shibata 29-Dec-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.ui;

public interface MainUI {
	
	void 	addDedicatedUI(DedicatedUI dedicatedUI);
	void	addMeetingRoomUI(MeetingRoomUI meetingRoomUI);
	void    removeMeetingRoomUI(MeetingRoomUI meetingRoomUI);
	void	beep();
	boolean	isIconified();
	boolean isInOffice();
	boolean isEditAddressFileVisible();
	void	setNotInOfficeEnabled(boolean	enabled);
	void 	setToList(String toList);
	void 	setMessageWaiting(boolean waiting);
	void	toFront();
}

// LOG
// 2.35 :  6-Nov-99	Y.Shibata	created.
// 2.38 : 29-Dec-99	Y.Shibata	added removeMeetingRoomUI
// 2.39 : 30-Dec-99	Y.Shibata	added isEditAddressFileVisible & isIconified