// File: UIFactoryImpl.java - last edit:
// Yoshiki Shibata 30-Dec-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.awt;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;

import msgtool.Deliverer;
import msgtool.ui.AboutUI;
import msgtool.ui.DedicatedUI;
import msgtool.ui.DedicatedUIManager;
import msgtool.ui.MainUI;
import msgtool.ui.MeetingRoomUI;
import msgtool.ui.OnlineListUI;
import msgtool.ui.ParticipantsUI;
import msgtool.ui.SearchUI;
import msgtool.ui.UIFactory;
import msgtool.ui.model.DedicatedModel;

class UIFactoryImpl implements UIFactory<MenuItem> {

	public DedicatedUI	createDedicatedUI(
		Frame               parentFrame,
        Deliverer           deliverer,
        String              senderName,
        String              senderIP,
        boolean             deliverEnabled)	{
		return new DedicatedUIImpl(parentFrame, new DedicatedModel(senderName, senderIP, deliverer), deliverEnabled);
	}

	public OnlineListUI	createOnlineListUI(
		String				title,
		MainUI				mainUI,
		DedicatedUIManager<MenuItem>	dedicatedUIManager) {
  		return new OnlineListUIImpl<MenuItem>(title, mainUI, dedicatedUIManager);
	}

	public MeetingRoomUI	createMeetingRoomUI(
		Frame	parentFrame,
		String	internalRoomName,
		String	externalRoomName,
		Object	topMenu) {
		return new MeetingRoomUIImpl(parentFrame, internalRoomName, externalRoomName, 
								(Menu) topMenu, fMeetingRoomListUI, this);
	}

  	public 	ParticipantsUI	createParticipantsUI(
		Frame	parentFrame,
		String	roomName) {
		return new ParticipantsUIImpl(parentFrame, roomName);
	}

	public SearchUI createSearchUI(Frame parentFrame) {
		return new SearchUIImpl(parentFrame);
	}

	public 	AboutUI	createMessageReceivedUI(Frame parentFrame) {
		return new AboutUIImpl(parentFrame, AboutUI.kMessageReceived, "MessagingTool Notice");
	}

	public void setMeetingRoomListUI(Object meetingRoomListUI) {
		fMeetingRoomListUI = (MeetingRoomListUI) meetingRoomListUI;
	}

	private MeetingRoomListUI fMeetingRoomListUI;
}

// LOG
// 2.35 : 30-Oct-99	Y.Shibata	created.
// 2.39 : 30-Dec-99	Y.Shibata	added setMeetingRoomListUI, createSearchUI, createMessageReceivedUI