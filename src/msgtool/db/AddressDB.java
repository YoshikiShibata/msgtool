// File: AddressDB.java - last edit:
// Yoshiki Shibata 13-Nov-2004

// Copyright (c) 1996 - 2000, 2003, 2004 by Yoshiki Shibata. 
// All rights reserved.

// This file is common for all versions: AWT and Swing.

package msgtool.db;

import java.util.Observable;
import java.util.Properties;

import msgtool.util.FileUtil;
import msgtool.util.PropertiesUtil;
import msgtool.util.StringUtil;

public final class AddressDB extends Observable {
      
    static private String kAddressFile        = "MessagingTool.addr";
    static private String kSortKeyFile        = "MessagingTool.key";
    static private String kHintKeySuffix      = ".hinted";
    
    private Properties fAddressCacheDB      = null;
    private Properties fSortKeyCacheDB      = null;
    private Properties fHintListCacheDB     = null;
    
    static private AddressDB  fInstance = new AddressDB();
    static public AddressDB  instance() { return(fInstance); }

    private AddressDB() {
        fAddressCacheDB = new Properties();
        FileUtil.loadProperties(kAddressFile, fAddressCacheDB);
        fSortKeyCacheDB = new Properties();
        FileUtil.loadProperties(kSortKeyFile, fSortKeyCacheDB);
        fHintListCacheDB = fSortKeyCacheDB;
  	}
        
    public AddressDB(String addressFile) {
        fAddressCacheDB = new Properties();
        FileUtil.loadProperties(addressFile, fAddressCacheDB);
   	}

    public synchronized void save() {
        FileUtil.saveProperties(kAddressFile, fAddressCacheDB,
            " Address cache for Java version of MessagingTool by Y.Shibata");
        FileUtil.saveProperties(kSortKeyFile, fSortKeyCacheDB,
             " Sort Key cache for Java version of MessagingTool by Y.Shibata"); 
        // Notify to all observers
        setChanged();
        notifyObservers();
  	}
        
    public synchronized void save(String addressFile) {
        FileUtil.saveProperties(addressFile, fAddressCacheDB,
         " Address cache for Java version of MessagingTool by Y.Shibata");
        // Notify to all observers
        setChanged();
        notifyObservers();
  	}

    public synchronized void clearAddressCache() {
        fAddressCacheDB.clear();
    }

    public synchronized void clearKeyCache() {
        fSortKeyCacheDB.clear();
    }
        
    public synchronized String[] getListOfAddressCache() {
        return StringUtil.getPropertyNames(fAddressCacheDB) ;
   	}

    public synchronized String lookUpAddressCache(String recipient) {
		return StringUtil.getProperty(fAddressCacheDB, recipient) ;
  	}
  
    public synchronized String lookUpKeyCache(String recipient) {
		String key = StringUtil.getProperty(fSortKeyCacheDB,recipient); 
        
		return (key == null ? "" : key);
   	}
    
    public synchronized String lookUpName(String address) {
        String names[] = PropertiesUtil.propertyNamesToArray(fAddressCacheDB);

        for (int i = 0; i < names.length; i++) {
            if (address.equals(fAddressCacheDB.getProperty(names[i])))
                return StringUtil.underScore2Space(names[i]);    
        }
        return null ;
    }

    public synchronized void addAddressCache(
        String recipient,
        String address) {
		StringUtil.setProperty(fAddressCacheDB, recipient, address); 
  	}
    
    public synchronized void addKeyCache(
        String  recipient,
        String  sortKey) {
		StringUtil.setProperty(fSortKeyCacheDB, recipient, sortKey); 
  	}
    
    public synchronized void replaceIPAddress(
        String  oldIPAddress,
        String  newIPAddress) {
        String names[] = PropertiesUtil.propertyNamesToArray(fAddressCacheDB);
        for (int i = 0; i < names.length; i++) {
            if (oldIPAddress.equals(fAddressCacheDB.getProperty(names[i]))) {
                fAddressCacheDB.setProperty(names[i], newIPAddress);
                break;
            }
        }
    }
        
    public Properties getDB() {
        return fAddressCacheDB;
  	}
    // ==================
    // Hinted
    // ==================
    public synchronized boolean isHinted(String recipient) {
		String stringValue = StringUtil.getProperty(fHintListCacheDB, recipient + kHintKeySuffix);
        
		// If the recipient is not found, it is considered as hinted.
		return stringValue == null ? true : Boolean.valueOf(stringValue).booleanValue();
  	}
        
    public synchronized void setHinted(String recipient, boolean isHinted) {
        Boolean bool = new Boolean(isHinted);
		StringUtil.setProperty(fHintListCacheDB,recipient + kHintKeySuffix, bool.toString()); 
  	}
        
    public synchronized Properties getHintedAddressDB() {
    	Properties hintedAddressDB = new Properties();
        String[] recipients = getListOfAddressCache();
         
        for (String recipient: recipients) {
        	if (isHinted(recipient))
        		StringUtil.setProperty(hintedAddressDB,recipient, ""); 
      	}
        return hintedAddressDB;
  	}
}
// LOG
//        31-Aug-96 Y.Shibata   created
// 1.10 : 21-Mar-97 Y.Shibata   made all methods synchronized.
// 1.11 : 27-Mar-97 Y.Shibata   added LookUpName() and GetDB()
// 1.34 :  6-Jul-97 Y.Shibata   made this class observable. 
// 1.39 : 27-Jul-97 Y.Shibata   added another constructor and Save().
// 1.78 :  6-Dec-97 Y.Shibata   added isHinted() and setHinted().
// 1.81 : 11-Dec-97 Y.Shibata   fixed NullPointerException bug in GetHintedAddressDB().
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.FileUtil.
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.db
// 2.24 : 25-Apr-99	Y.Shibata	used StringUtil.setProperty()/getProperty()
// 2.40 :  3-Jan-00	Y.Shibata	changed names of methods to lower-case.
// 2.50 : 27-Dec-03 Y.Shibata   replace Properties.put call with Properties.setProperty
//        29-Dec-03 Y.Shibata   rewritten with msgtool.util.PropertiesUtil
// 2.52 : 13-Nov-04 Y.Shibata	refactored.
