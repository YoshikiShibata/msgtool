// File: DedicatedModel.java - last edit:
// Yoshiki Shibata 31-Oct-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.ui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import msgtool.Deliverer;
import msgtool.db.AddressDB;
import msgtool.ui.DedicatedUI;
import msgtool.ui.InputArea;

public final class DedicatedModel {
	public DedicatedModel( 
		String		senderName,
		String		senderIP,
	 	Deliverer	deliverer) {
		fSenderName = senderName;
		fSenderIP	= senderIP;
		fDeliverer	= deliverer;
		updateTitle();
		fChangesButler = new PropertyChangeSupport(this);
  	}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        fChangesButler.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        fChangesButler.removePropertyChangeListener(listener);
    }

	public void deliver(String recipients, InputArea ia, DedicatedUI ui) {
		update();
		fDeliverer.deliver(recipients, ia, ui);
	}

	public String	getSenderIP() {
		return fSenderIP;
	}

	public String	getSenderName() {
		return fSenderName;
	}
	public String	getTitle() {
		return fTitle;
	}

	public void update() {
	    String  nameInCache = AddressDB.instance().lookUpName(fSenderIP);
        
        //
        // If the name of sender is not found in the cache file,
        // then set fSenderName to fSenderIP, so that sending
        // a message from this window works. Note that if a name 
        // which is not found in the cache is used for sending 
        // message from this window, the sending might fail.
        //
        if (nameInCache == null && !fSenderName.equals(fSenderIP)) {
            fSenderName = fSenderIP;
			updateTitle();
			fChangesButler.firePropertyChange("SenderName", null, null);
		}
	 	else if (!nameInCache.equals(fSenderName)) {
            fSenderName =  nameInCache;
			updateTitle();
			fChangesButler.firePropertyChange("SenderName", null, null);
       	}
	}

	private void updateTitle() {
		fTitle = fSenderName + '(' + fSenderIP + ')';
	}
	private String		fSenderIP 	= null;
	private String		fSenderName	= null;
	private String		fTitle		= null;

	private final Deliverer	fDeliverer;
	private PropertyChangeSupport fChangesButler;
}

// LOG
// 2.35 : 31-Oct-99	Y.Shibata	created