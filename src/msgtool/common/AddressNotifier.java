// File: AddressNotifier.java - last edit:
// Yoshiki Shibata 4-Jan-00

// Copyright (c) 1997, 1998, 2000 by Yoshiki Shibata. All rights reserved.

// This file is common file for all versions: AWT and Swing.

package msgtool.common;

import java.io.File;

import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.protocol.MiscProtocol;
import msgtool.util.FileUtil;
import msgtool.util.TimerUtil;

public final class AddressNotifier extends Thread {
    
    private final static String kAddressList = "MessagingTool.addr.list";

    public static void makeAddressList(
        String       oldIPAddress,
        String       newIPAddress) {
        // Make address list only if there is any entry in the address cache
        AddressDB   addressDB   = AddressDB.instance();
        
        String []   entries = addressDB.getListOfAddressCache();
        
        if (entries.length == 0)
            return;
       
        AddressDB   addressList = new AddressDB(kAddressList);
        
        // At first, replace all entries in the addressDB whose IP address
        // is the old IP address.
        addressDB.replaceIPAddress(oldIPAddress, newIPAddress);
        addressDB.save();

        // Copy all entries of addressDB into addressList.
        String      address = null;
        
        for (int i = 0; i < entries.length; i++) {
            address = addressDB.lookUpAddressCache(entries[i]);
            if (address == null)
                continue;
                      
            addressList.addAddressCache(entries[i], address);
		}

        // Ok. Now list of recipients who must be notified is created.
        addressList.save(kAddressList); 
	}
        
    // ======================
    // Constructor for Thread
    // ======================
    public AddressNotifier() {
		super("AddressNotifier");
	}
            
    public void run() {
        PropertiesDB propertiesDB   = PropertiesDB.getInstance();
        String      oldIPAddress    = propertiesDB.getMyOldIPAddress();
        AddressDB   addressList     = new AddressDB(kAddressList);
        String[]    entries         = addressList.getListOfAddressCache();
        String[]    addresses       = null;
        String      senderName      = propertiesDB.getUserName();
            
        while (entries.length > 0) {
            // Copy all addresses so that updated AddressList can be saved later
            if (addresses == null || 
            	addresses.length < entries.length)
                addresses = new String[entries.length];

            for (int i = 0; i < entries.length; i++)
                addresses[i] = addressList.lookUpAddressCache(entries[i]);

            // Now notify the address change.
            MiscProtocol    miscProtocol = MiscProtocol.getInstance();
            for (int i = 0; i < entries.length; i++) {
                if (miscProtocol.notifyNewAddress(senderName, entries[i], oldIPAddress) != null) {
                    // Successful
                    entries[i] = null;
				}
			}

            // Save the updated AddressList.
            addressList.clearAddressCache();
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != null)
                    addressList.addAddressCache(entries[i], addresses[i]);
			}
            addressList.save(kAddressList);

            // Now get the updated entries. If there are still remained entries, we should
            // wait for another ten minutes to repeat this process.
            entries = addressList.getListOfAddressCache();
            if (entries.length != 0) 
                TimerUtil.sleep(600 * 1000);
		}

        // Delete the addressList file.
        File    file = new File(FileUtil.makeFullPathname(kAddressList));
        
        file.delete();
	}
}
    
    
// LOG
// 1.39 : 27-Jul-97 Y.Shibata   created.
// 1.47 : 20-Aug-97 Y.Shibata   In MakeAddressList(), check if there is any etry in the address cache.
// 1.94 :  5-Jul-97 Y.Shibata   modified to use msgtool.util.FileUtil
// 1.95 : 11-Jul-97 Y.Shibata   modified to use msgtool.util.TimerUtil
//        12-Jul-98 Y.Shibata   moved to msgtool.common.
// 2.40 :  4-Jan-99	Y.Shibata	changed names of methods to lower-case.
