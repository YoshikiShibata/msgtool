// File: SortUtil.java  - last edit:
// Yoshiki Shibata 18-Jul-98

// Copyright (c) 1998 by Yoshiki Shibata

package msgtool.common;

import msgtool.db.AddressDB;

public final class SortUtil {

    static public void sortStrings(String[] strings) { 
        int i, j;
         
        for (i = strings.length; i >= 1; i--) {
            for (j = 2; j <= i; j++) {
                if (strings[j-2].compareTo(strings[j-1]) > 0) {
                   	String  tmp = strings[j-2];
                    strings[j-2] = strings[j-1];
                    strings[j-1] = tmp;
				}
			}
		}
	}

	static public void sortStringsByLength(String[] strings) {
		int i, j;
         
        for (i = strings.length; i >= 1; i--) {
            for (j = 2; j <= i; j++) {
                if ((strings[j-2].length() > strings[j-1].length()) ||
					(strings[j-2].compareTo(strings[j-1]) > 0)) {
                   	String  tmp = strings[j-2];
                    strings[j-2] = strings[j-1];
                    strings[j-1] = tmp;
				}
			}
		}
	}
        
    static public void sortStringsBySortKey(String[] strings) { 
        int i, j;
        String[]    keys        = new String[strings.length];
        String      sortKey     = null;
        AddressDB   addressDB   = AddressDB.instance();

        for (i = 0; i < strings.length; i++) {
            //
            // Any strings with not-null sortkey will be listed before
            // strings without sort-keys. [V1.75]
            //
            sortKey = addressDB.lookUpKeyCache(strings[i]);
            if (sortKey.length() != 0)
                keys[i] = "  " + addressDB.lookUpKeyCache(strings[i]) + " " + strings[i];
            else
                keys[i] = " " + strings[i];
		}
         
        for (i = strings.length; i >= 1; i--) {
            for (j = 2; j <= i; j++) {
                if (keys[j-2].compareTo(keys[j - 1]) > 0) {
                    String  tmp = strings[j-2];
                    strings[j-2] = strings[j-1];
                    strings[j-1] = tmp;
                    
                    tmp = keys[j-2];
                    keys[j-2] = keys[j-1];
                    keys[j-1] = tmp;
				}
			}
		}
	}
}

// Log
// 1.95 : 18-Jul-98 Y.Shibata   created from the old Util.java
