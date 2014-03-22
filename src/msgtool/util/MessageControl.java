// File: MessageControl.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1999, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.util;

import java.util.Vector;

import msgtool.db.AddressDB;

public final class MessageControl {

	public interface MessageListener {
		void allMessagesShown();
	}

	private MessageControl() {
	}

	public static MessageControl getInstance() {
		return fInstance;
	}

	public void setMessageListener(MessageListener l) {
		fListener = l;
	}

	public synchronized void setMessageWaiting(String recipient, boolean messageWaiting) {
		String ipAddress = AddressDB.instance().lookUpAddressCache(recipient);

		if (messageWaiting)
			messageWaiting(ipAddress);
	 	else
			messageShown(ipAddress);
	}

	public synchronized void clearAllMessagesWaiting() {
		// clearAllMessagesWaiting() doesn't call back the listner.
		// Because usually the listener is the ancestor of the caller of this method.
		fSendersList.setSize(0);
	}

	private void messageWaiting(String ipAddress) {
		if (fSendersList.indexOf(ipAddress) >= 0)
			return; // already exists

		fSendersList.add(ipAddress);
        // fSendersList.addElement(ipAddress);
	}

	private void messageShown(String ipAddress) {
		int index = fSendersList.indexOf(ipAddress);

		if (index >= 0)
			fSendersList.removeElementAt(index);

	  	if (fSendersList.size() == 0 && fListener != null)
			fListener.allMessagesShown();
	}

	private static MessageControl	fInstance 		= new MessageControl();
	private MessageListener			fListener 		= null;
	private Vector<String>			fSendersList	= new Vector<String>();
}

// LOG
// 2.37 :  5-Dec-99	Y.Shibata	created
//         9-Dec-99	Y.Shibata	added synchronized
// 3.50 : 27-Dec-03 Y.Shibata   use Java Generics
