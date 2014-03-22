// File: DedicatedUIManager.java - last edit:
// Yoshiki Shibata  27-Dec-03

// Copyright (c) 1999, 2002, 2003 by Yoshiki Shibata
// All rights reserved.

package msgtool.ui;

import java.awt.Frame;
import java.util.Hashtable;
import java.util.ArrayList;

import msgtool.Deliverer;
import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.protocol.MiscProtocol;

public final class DedicatedUIManager<K> {

	public DedicatedUIManager(
		MainUI 		mainUI,
		Frame		parentFrame,
		UIFactory<K>	uiFactory) {

        assert mainUI != null : "mainUI is null";
        assert parentFrame != null : "parentFrame is null";
        assert uiFactory != null : "uiFactory is null";

		fMainUI 		= mainUI;
		fParentFrame	= parentFrame;
		fUIFactory		= uiFactory;
	}

	public void setDeliverer(Deliverer deliverer) {
		fDeliverer = deliverer;
	}
	public synchronized DedicatedUI findOrCreate(
		String	senderIP,
		String	senderName) {
        DedicatedUI ui = null;
        
        for (DedicatedUI i: fUIs) {
            if (senderIP.equals(i.getSenderIP())) {
                ui = i;
                break;
            }
        }

       	if (ui == null) {
            String      nameInCache = fAddressDB.lookUpName(senderIP);
            
            //
            // If a user specified name for senderIP is found in
            // the address cache file, then use the specified name
            // instead of senderName passed as argument.
            //
            ui = fUIFactory.createDedicatedUI(
                    fParentFrame, fDeliverer, 
                    nameInCache == null ? senderName : nameInCache, 
                    senderIP,
                    fMainUI.isInOffice());

			fUIs.add(ui);
			fMainUI.addDedicatedUI(ui);
		}
	 	return(ui);
	}

	public synchronized void close() {
        for (DedicatedUI i: fUIs)
           	i.setVisible(false);
	}

	public synchronized void setDeliverEnabled(boolean enabled) {
        for (DedicatedUI i: fUIs)
            i.setDeliverEnabled(enabled);
	}

	public synchronized void updateRecipientHintsPopup() {
        for (DedicatedUI i: fUIs)
            i.updateRecipientHintsPopup();
	}

	public void put(K key, DedicatedUI ui) {
		fMenuMap.put(key, ui);
	}

	public DedicatedUI get(K key) {
		return fMenuMap.get(key);
	}

	public void open(String[]    onlines) {
        if (onlines == null || onlines.length == 0)
            return;
            
        DedicatedUI     ui = null;
        String          senderName = fPropertiesDB.getUserName();
        String          remoteIPAddress = null;
        //
        // Open only one dedicated window and remained recipients should
        // be set into the Additional To: field. [V1.63]
        //
        int index;
        
        for (index = 0; index < onlines.length; index++) {
            remoteIPAddress = fMiscProtocol.probe(senderName, onlines[index]);
            if (remoteIPAddress != null) {
                ui = findOrCreate(remoteIPAddress, onlines[index]);
                // 
                // Set null to the online recipient in onlines[] so that
                // not-null entries means Off-Line.
                //
                onlines[index] = null;
                index++;
                break;
           	}
      	}
        //
        // If no online recipient was found, then do nothing.
        //
        if (ui == null)
            return;
        //
        // If there are still remained onlines, then create a list of them.
        // 
        String  additionalTo = null;
        
        for (; index < onlines.length; index ++) {
            if (fMiscProtocol.probe(senderName, onlines[index]) != null) {
                if (additionalTo == null)
                    additionalTo = onlines[index];
                else
                    additionalTo += ", " + onlines[index];
                onlines[index] = null; // see above
          	}     
      	}
        if(additionalTo != null)
            ui.setAdditionalRecipients(additionalTo);
        else
            ui.setAdditionalRecipients("");
        //
        // Now make the DedicatedUI visible
        //
        ui.setVisible(true);
  	}

	private final MainUI	fMainUI;
	private final Frame		fParentFrame;
	private Deliverer		fDeliverer;
	private final UIFactory<K>	fUIFactory;

	private AddressDB		fAddressDB 		= AddressDB.instance();
	private PropertiesDB	fPropertiesDB	= PropertiesDB.getInstance();
	private MiscProtocol	fMiscProtocol	= MiscProtocol.getInstance();

	private ArrayList<DedicatedUI> fUIs = new ArrayList<DedicatedUI>();
	private Hashtable<K,DedicatedUI> fMenuMap	= new Hashtable<K, DedicatedUI>();
}

// LOG
// 2.35 :  6-Nov-99	Y.Shibata	created.
// 2.50 : 30-Dec-02 Y.Shibata   replaced Assert.assert with "assert".
//        19-Nov-03	Y.Shibata	modified to be compiled cleanly with "Tiger"(SDK1.5).
// 3.50 : 27-Dec-03 Y.Shibata   used Java Generics and the enhanced "for"