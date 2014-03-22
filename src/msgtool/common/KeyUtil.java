// File: KeyUtil.java - last edit:
// Yoshiki Shibata 18-Jul-98

// Copyright (c) 1998 by Yoshiki Shibata

package msgtool.common;

import java.awt.event.KeyEvent;

import msgtool.db.PropertiesDB;

public final class KeyUtil {

    static private PropertiesDB propertiesDB = PropertiesDB.getInstance();

    static public boolean isDeliverKey(int keyCode) {
        if (KeyEvent.VK_F1 <= keyCode && keyCode <= KeyEvent.VK_F12) {
            //
            // Check if the function key assigned to Deliver Key is clicked.
            //
            if ((keyCode - KeyEvent.VK_F1) == propertiesDB.getDeliverKey())
                return(true);
		}
        return(false);
	}
}

// Log
// 1.95 : 18-Jul-98 Y.Shibata   created from the old Util.java
