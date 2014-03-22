// File: MessageProtocolListenerImpl.java - last edit:
// Yoshiki Shibata 30-Dec-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.common;

import java.awt.Color;

import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.log.LogArea;
import msgtool.protocol.Message;
import msgtool.protocol.MessageProtocol;
import msgtool.protocol.MessageProtocolListener;
import msgtool.protocol.MiscProtocol;
import msgtool.ui.AboutUI;
import msgtool.ui.DedicatedUI;
import msgtool.ui.DedicatedUIManager;
import msgtool.ui.InputArea;
import msgtool.ui.MainUI;
import msgtool.ui.OnlineListUI;
import msgtool.util.StringDefs;
import msgtool.util.SwingWrapper;

public class MessageProtocolListenerImpl<T> implements MessageProtocolListener {

	public MessageProtocolListenerImpl(
		MainUI				mainUI,
		OnlineListUI		onlineListUI,
		InputArea			inputArea,
		LogArea				logArea,
		DedicatedUIManager<T>	dedicatedUIManager,
		AboutUI				messageReceivedUI) {
		fMainUI				= mainUI;
		fOnlineListUI		= onlineListUI;
		fInputArea			= inputArea;
		fLogArea			= logArea;
		fDedicatedUIManager	= dedicatedUIManager;
		fMessageReceivedUI	= messageReceivedUI;
 	}

    public void onMessage(final Message msg) {
        Runnable methodBody = new Runnable() {
            public void run() {
                onMessageBody(msg);
                }
            };
        SwingWrapper.invokeLater(methodBody);
	}
        
    private void onMessageBody(Message   msg) {
        String  message     = msg.getMessage();
        String  senderName  = msg.getSenderName();
        boolean broadcast   = msg.isBroadcast();
        String  senderIP    = msg.getSenderIP();
        String  cacheName   = fAddressDB.lookUpName(senderIP);
        //
        // Update Address Cache database if the edit window is not visible.
        // When a broadcast message is received, don't try to update the
        // Address cache. If a address cache were updated, the following 
        // situation would happen:
        //
        // [Dangerous Situation]
        //    Suppose there are more than a hundred MessagingTool users. Now
        //    The On-Line feature polls all entries in the Address cache: When
        //    a unknown user polls your MessagingTool, the user would be 
        //    automatically registered into your Address DB. So if you send a
        //    broadcaset message, and all recipients automatically register your name 
        //    and address into their Address DBs. So what will happen?
        //
        //    Answer is that suddenly your Address DB will be filled with all
        //    MessagingTool users on the net. Because all recipients of the
        //    broadcast message will suddenly start probing your MessagingTool, 
        //    and your MessagingTool would eventually register all probers addresses
        //    into your Address DB.
        //
        if (!broadcast && !fMainUI.isEditAddressFileVisible()) {
            //
            // At first, look the senderName up from the addressDB. If there is
            // any matching one, then it means that the user might specify the
            // address. Therefore keep the current setting.
            // 
            String  cacheAddress = fAddressDB.lookUpAddressCache(senderName);
            
            if (cacheAddress == null && cacheName == null) {
                fAddressDB.addAddressCache(senderName, senderIP);
                fAddressDB.save();
            }
        }
        //
        // if cacheName is not null, then the senderName must be replaced with it.
        // Otherwise, "Messaging Dialog" doesn't work.
        // 
        if (cacheName != null) {
            senderName = cacheName;
            //
            // Update Online List only the sender is registered in the address cache.
            // If it is a command line message, don't update the OnLine list.
            //
            if (!msg.isCommandLineMessage())
                fOnlineListUI.setOnline(cacheName);
        }
        //
        // Show the received message only if its length is greater
        // than zero. A zero length message is a probe.
        //
        if (message.length() > 0) {
            String  receivedLog = null;
            
            fLogArea.lock();
            Color originalColor = fLogArea.getTextColor();
            fLogArea.setTextColor(Color.blue);

            fLogArea.appendDate();
            fLogArea.appendSubText(senderName + "(" + senderIP + ")\n" + message);
            if (message.charAt(message.length() - 1) != '\n')
                fLogArea.appendSubText("\n\n");
            else
                fLogArea.appendSubText("\n");

            receivedLog = fLogArea.getLastMessage();
            fLogArea.setTextColor(originalColor);
            fLogArea.unlock();

            
            //
            // Now search for Dedicated Window. If found, then show it. 
            // If not found, then create a new one.
            //
            DedicatedUI dedicatedUI = fDedicatedUIManager.findOrCreate(senderIP, senderName);
            
            dedicatedUI.appendLog(receivedLog, Color.blue);
            //
            // BeepOnReception
            //
            if (fPropertiesDB.isBeepOnReception())
                fMainUI.beep();
            //
            // Show the window if requested. If necessary, set the message waiting
            // in the Online list window.
            //
            if (!fMainUI.isIconified())
                fMainUI.toFront(); 
                
            if (fPropertiesDB.isActivateOnReception()) {
                if (fPropertiesDB.getActivateWindowChoice() == 0) {
                    dedicatedUI.setVisible(true);
                    dedicatedUI.toFront(); 
                } else if (fMainUI.isIconified()) {
                    fMessageReceivedUI.setVisible(true);
                    fMessageReceivedUI.toFront();    
                }
            }
            //
            // The dedicated dialog might have been open when this message is 
            // received. So if the dedicated dialog is already in visible, don't
            // set the Message Waiting status. [V1.53]
            //
            if (fMainUI.isIconified() && !dedicatedUI.isVisible()) {
                fOnlineListUI.setMessageWaiting(senderName, true);
				fMainUI.setMessageWaiting(true);
			}
        }
        //
        // If the checkbox of "Not In Office" is checked, then
        // send a recorded message back to the sender.
        // Note that don't send a recorded message to a broadcasted message.
        //
        if (!broadcast && !fMainUI.isInOffice()) {
            String recordedMsg = fInputArea.getText();
            //
            // Send NotInOffice notification first, then send the recorded message.
            //
            fMiscProtocol.notInOffice(fPropertiesDB.getUserName(), senderIP);
            
            if (recordedMsg != null && recordedMsg.length() > 0) 
                fMessageProtocol.sendMessage(fPropertiesDB.getUserName(), 
                    senderIP,
                    StringDefs.RECORDED_MESSAGE_C + " " + recordedMsg);
            else 
                fMessageProtocol.sendMessage(fPropertiesDB.getUserName(), 
                    senderIP, 
                    StringDefs.RECORDED_MESSAGE_C + " " + StringDefs.IM_NOT_IN_MY_OFFICE);
        }
    }
   
	private final MainUI				fMainUI;
	private final OnlineListUI			fOnlineListUI;
	private final InputArea				fInputArea;
	private final LogArea				fLogArea;
	private final DedicatedUIManager<T>	fDedicatedUIManager;
	private final AboutUI				fMessageReceivedUI;

	private final AddressDB			fAddressDB 			= AddressDB.instance();
	private final PropertiesDB		fPropertiesDB		= PropertiesDB.getInstance();
	private final MessageProtocol	fMessageProtocol	= MessageProtocol.getInstance();
	private final MiscProtocol		fMiscProtocol		= MiscProtocol.getInstance();
}
// LOG
// 2.39 : 30-Dec-99	Y.Shibata	created
