// File: UIFactory.java - last edit:
// Yoshiki Shibata 30-Dec-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.ui;

import java.awt.Frame;

import msgtool.Deliverer;

public interface UIFactory<T>
{
	DedicatedUI	createDedicatedUI(
		Frame               parentFrame,
        Deliverer           deliverer,
        String              senderName,
        String              senderIP,
        boolean             deliverEnabled);

	OnlineListUI	createOnlineListUI(
		String				title,
		MainUI				mainUI,
		DedicatedUIManager<T>	dedicatedUIManager);

 	MeetingRoomUI	createMeetingRoomUI(
		Frame	parentFrame,
		String	internalRoomName,
		String	externalRoomName,
		Object	topMenu);

	ParticipantsUI	createParticipantsUI(
		Frame	parentFrame,
		String	roomName);

 	SearchUI		createSearchUI(Frame	parentFrame);

	AboutUI			createMessageReceivedUI(Frame parentFrame);

 	void setMeetingRoomListUI(Object meetingRoomListUI);
}

// LOG
// 2.35 : 24-Oct-99	Y.Shibata	created.
// 2.39 : 30-Dec-99	Y.Shibata	added setMeetingRoomListUI//								added createSearchUI
