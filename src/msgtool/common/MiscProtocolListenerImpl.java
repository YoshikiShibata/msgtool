// File: MiscProtocolListenerImpl.java - last edit:
// Yoshiki Shibata 30-Dec-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.common;

import java.awt.Color;

import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.meeting.MeetingManager;
import msgtool.protocol.Message;
import msgtool.protocol.MessageProtocolListener;
import msgtool.protocol.MiscProtocol;
import msgtool.protocol.MiscProtocolListener;
import msgtool.ui.MainUI;
import msgtool.ui.MeetingRoomUI;
import msgtool.ui.OnlineListUI;
import msgtool.ui.SearchUI;
import msgtool.util.StringUtil;

public class MiscProtocolListenerImpl implements MiscProtocolListener {

	public MiscProtocolListenerImpl(
		MainUI					mainUI,
		OnlineListUI			onlineListUI,
		SearchUI				searchUI,
		MeetingManager			meetingManager,
		MessageProtocolListener	messageProtocolListener) {
		fMainUI						= mainUI;
		fOnlineListUI 				= onlineListUI;
		fSearchUI					= searchUI;
		fMeetingManager				= meetingManager;
		fMessageProtocolListener	= messageProtocolListener;
	}

    public void    onProbe(String senderIP) {
        String  cacheName   = fAddressDB.lookUpName(senderIP);
            
        if (cacheName != null) 
            fOnlineListUI.setOnline(cacheName);

		if (fMainUI.isInOffice())
            fMiscProtocol.inOffice(fPropertiesDB.getUserName(), senderIP);
        else 
			fMiscProtocol.notInOffice(fPropertiesDB.getUserName(), senderIP);
  	} 
              
    public void    onOffLine(String senderIP) {
        String  cacheName   = fAddressDB.lookUpName(senderIP);
            
        if (cacheName != null) 
            fOnlineListUI.setOffline(cacheName);
  	}
             
    public void    onReplaceIPAddress(String oldIP, String newIP) {
        fAddressDB.replaceIPAddress(oldIP, newIP);
        fAddressDB.save();
   	}
   
    public void    onNotInOffice(String senderIP) {
        String  cacheName   = fAddressDB.lookUpName(senderIP);
        
        if (cacheName != null) {
            fOnlineListUI.setOnline(cacheName);
            fOnlineListUI.setNotInOffice(cacheName, true);
      	}
   	}
        
    public void onInOffice(String senderIP) {
        String cacheName    = fAddressDB.lookUpName(senderIP);
        
        if (cacheName != null) {
            fOnlineListUI.setOnline(cacheName);
            fOnlineListUI.setNotInOffice(cacheName, false);
      	}
 	}
   
    public void onLookForUser(String senderIP, String name) {
        String myName = fPropertiesDB.getUserName();
        //
        // Check user name registered as "Sender Name" in the property.
        // Then check the registered E-mail address
        //
		if (name.equals("*"))
			fMiscProtocol.userMatched(myName, senderIP);
        else if (StringUtil.regionMatches(myName, name))
            fMiscProtocol.userMatched(myName, senderIP);
        else {
            String eMail = fPropertiesDB.getEMail();
            
            if (eMail != null && StringUtil.regionMatches(eMail, name))
                fMiscProtocol.userMatched(eMail, senderIP);
    	}
  	}
    
    public void onUserMatched(String senderIP, String name) {
        fSearchUI.appendLog(name + " = " + senderIP + "\n", Color.blue);
 	}
        
    public void onLogRequest(String senderIP, String internalRoomName) {
        MeetingRoomUI    meetingRoom =  fMeetingManager.findMeetingRoomUI(internalRoomName);
        if (meetingRoom != null)
            meetingRoom.getLogMeeting().onLogRequest(senderIP);
   	}
           
    public void onRequestedLog(String senderIP, String internalRoomName, String log) {
        MeetingRoomUI    meetingRoom = fMeetingManager.findMeetingRoomUI(internalRoomName);
        if (meetingRoom != null)
            meetingRoom.getLogMeeting().onRequestedLog(log);
  	}
        
    public void onCommandLineMessage(String senderIP, String senderName, String message) {
        //
        // Treat the received message as broadcast so that no recorded message
        // will be sent back.
        //
        Message msg = new Message(senderIP, senderName, message, true);
        msg.setCommandLineMessage(true);
        
        fMessageProtocolListener.onMessage(msg);
 	}

	private final AddressDB		fAddressDB 		= AddressDB.instance();
	private final PropertiesDB	fPropertiesDB 	= PropertiesDB.getInstance();
	private final MiscProtocol	fMiscProtocol	= MiscProtocol.getInstance();

	private final OnlineListUI				fOnlineListUI;
	private final MainUI					fMainUI;
	private final SearchUI					fSearchUI;
	private final MeetingManager			fMeetingManager;
	private final MessageProtocolListener	fMessageProtocolListener;
}

// LOG
// 2.39 : 30-Dec-99	Y.Shibata	created.