// File: StringUtil.java - last edit:
// Yoshiki Shibata 13-Nov-2004

// Copyright (c) 1998, 1999, 2003, 2004 by Yoshiki Shibata

package msgtool.util;
import java.util.Properties;

public final class StringUtil {
	private static final String[] ZERO_LENGTH_ARRAY = new String[0];

    // ==================================
    // Space Conversion function for keys
    // ==================================
    static public String underScore2Space(String  string) {
        if (string.indexOf('_') == -1)
            return(string);
            
        int     len         = string.length();
        char[]  charArray   = new char[len];
        
        string.getChars(0, len, charArray, 0);
        for (int i = 0; i < len; i++) {
            if (charArray[i] == '_')
                charArray[i] = ' ';
        }

        return new String(charArray);
 	}

    static public String space2UnderScore(String string) {
        if (string.indexOf(' ') == -1)
            return string;
            
        int     len         = string.length();
        char[]  charArray   = new char[len];
        
        string.getChars(0, len, charArray, 0);
        for (int i = 0; i < len; i++) {
            if (charArray[i] == ' ') 
                charArray[i] = '_';
        }
        return new String(charArray);
   	}
        
    /**
     * Obtains the names of properties from the specified Properties.
     * 
     * @param db the properites
     * @return an array of String(names of properties). If there is no 
     *         properties, an array whoses length is zero will be returned.
     */
    static public String[] getPropertyNames(Properties db) {
    	if (db.size() == 0)
    		return ZERO_LENGTH_ARRAY;

        String hints[] = PropertiesUtil.propertyNamesToArray(db);
        for (int i = 0; i < hints.length; i++)
            hints[i] = underScore2Space(hints[i]);

        return hints;
  	}
    
   	static public String getProperty(Properties db, String key) {
		return db.getProperty(space2UnderScore(key));
	}

	static public void setProperty(Properties db, String key, String value) {
		db.setProperty(space2UnderScore(key), value);
	}
    // ======================================================
    // ParseToList parses "To: " field string into individual
    // recipients.
    // ======================================================
    static public String [] parseToArray(String toList) {
    	String[] recipients = toList.split(",");
    	int noOfRecipients = recipients.length;
    	
    	for (int i = 0; i < recipients.length; i++) {
    		recipients[i] = recipients[i].trim();
    		if (recipients[i].length() == 0) {
    			recipients[i] = null;
    			noOfRecipients--;
    		}
    	}
    	
    	if (noOfRecipients == 0)
    		return new String[0];
    	
    	String[] results = new String[noOfRecipients];
    	int index = 0;
    	for (int i = 0; i < recipients.length; i++) {
    		if (recipients[i] != null) 
    			results[index++] = recipients[i];
    	}
    	return results;
   	}

    static public void stripSpacesAfterName(StringBuilder name) {
        int len = name.length();
        if (len == 0)
            return;

        while (len > 0 && name.charAt(len - 1) == ' ') {
            name.setLength(len - 1);
            len --;
        }
  	}
    
    // =======================
    // RegionMatches
    // ======================
    static public boolean regionMatches(
        String  fullString,
        String  partString)
        {
        int lengthOfPartString = partString.length();
        int loopCount = fullString.length() - lengthOfPartString;
        
        if (loopCount < 0 || lengthOfPartString == 0)
            return false;
            
        for (int i = 0; i <= loopCount; i++) {
            if (fullString.regionMatches(true, i, partString, 0, lengthOfPartString))
                return true;
        }
        return false;
    }
}
    
// LOG
// 1.95 : 12-Jul-98 Y.Shibata   created from the old Util.java
//        19-Jul-98 Y.Shibata   performance tuning.
// 2.24 : 25-Apr-99	Y.Shibata	added getProperty(), and setProperty().
// 2.50 : 27-Dec-03 Y.Shibata   replaced "set" with "setProperty"
// 2.52 : 13-Nov-04 Y.Shibata	refactored.
