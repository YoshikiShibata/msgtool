// File: MeetingRoomUI.java - last edit:
// Yoshiki Shibata 21-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.ui;

import msgtool.log.LogMeeting;

public interface MeetingRoomUI {
	
	void joinRoom();
	void leaveRoom();
	boolean isInRoom();

	void setNotInOffice(boolean notInOffice);

	void appendLogText(boolean notifyMessage, String  text);
    void appendLogText(boolean notifyMessage, String text, String sourceIP);
	LogMeeting getLogMeeting();
	void clearSavedLog();

	String 			getInternalRoomName();
	String			getExternalRoomName();

	ParticipantsUI	getParticipantsUI();

	void setEnabled(boolean enabled);
	boolean isEnabled();
	void setVisible(boolean visible);
	void setMenuItem(Object menuItem);
	void setState(int state);
};

// LOG
// 2.36 : 21-Nov-99	Y.Shibata	created.