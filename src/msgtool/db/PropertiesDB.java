// File: PropertiesDB.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1996 - 2000, 2003 by Yoshiki Shibata. All rights reserved.

// This file is common file for all versions: AWT and Swing.

package msgtool.db;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Properties;

import msgtool.util.FileUtil;
import msgtool.util.StringUtil;

public final class PropertiesDB {
    static public String kName    = "PropertiesDB";

    static private String kPropertyFile            	= "MessagingTool.prop";
    static private String kUserNameKey             	= "UserName";
    static private String kActivateOnReceptionKey  	= "ActivateOnReception";
    static private String kActivateWindowChoiceKey 	= "ActivateWindowChoice";
    static private String kDeliverKeyKey           	= "DeliverKey";
    static private String kFontNameKey             	= "FontName";
    static private String kFontStyleKey            	= "FontStyle";
    static private String kFontSizeKey             	= "FontSize";
    static private String kBeepOnReceptionKey      	= "BeepOnReception";
    static private String kEMailKey                	= "EMail";
    static private String kRegisteredNumberKey     	= "RegisteredNumber";
    static private String kMyIPAddressKey          	= "MyIPAddress";
    static private String kMyOldIPAddressKey       	= "MyOldIPAddress";
    static private String kLastLocationXKey        	= "LastLocationX";
    static private String kLastLocationYKey        	= "LastLocationY";
    static private String kLastWidthKey            	= "LastWidth";
    static private String kLastHeightKey           	= "LastHeight";
    static private String kLookAndFeelKey          	= "LookAndFeel";
    static private String kRcvDialogLocationXKey   	= "RcvDialogLocationX";
    static private String kRcvDialogLocationYKey   	= "RcvDialogLocationY";
    static private String kSaveMessagesKey         	= "SaveMessages";
    static private String kNoOfMessageFilesKey     	= "NoOfMessageFiles";
    static private String kOnlineLocationXKey      	= "OnlineDialogLocationX";
    static private String kOnlineLocationYKey      	= "OnlineDialogLocationY";
    static private String kOnlineWidthKey          	= "OnlineDialogWidth";
    static private String kOnlineHeightKey         	= "OnlineDialogHeight";
    static private String kOnlineVisibleKey        	= "OnlineDialogVisible";
    static private String kRoomListLocationXKey    	= "RoomListDialogLocationX";
    static private String kRoomListLocationYKey    	= "RoomListDialogLocationY";
    static private String kRoomListWidthKey        	= "RoomListDialogWidth";
    static private String kRoomListHeightKey       	= "RoomListDialogHeight";
    static private String kRoomListVisibleKey      	= "RoomListDialogVisible";
    static private String kTextBackgroundKey       	= "TextBackground";
	static private String kJoinedMeetingRoomsKey	= "JoinedMeetingRooms";

    static private String kLocationXSuffix    = ".LocationX";
    static private String kLocationYSuffix    = ".LocationY";
    static private String kSizeWidthSuffix    = ".Width";
    static private String kSizeHeightSuffix   = ".Height";
   
    private Properties  fPropertiesDB  = null;
    private PropertyChangeSupport fChangesButler;
    private boolean     fChanged = false;
    
    static private PropertiesDB fInstance = new PropertiesDB();
    
    static public PropertiesDB getInstance() { return(fInstance); }

    // ======================
    // Constructor
    // ======================
    private PropertiesDB() {
        fPropertiesDB = new Properties();
        FileUtil.loadProperties(kPropertyFile, fPropertiesDB);
        fChangesButler = new PropertyChangeSupport(this);
        fChanged = false;
 	}
        
    // ================================
    // add/remove PropertyChangeListner
    // ================================
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        fChangesButler.addPropertyChangeListener(listener);
  	}
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        fChangesButler.removePropertyChangeListener(listener);
  	}
    // ================================
    // saveProperties()
    // ================================
    public void saveProperties(ActionEvent e) {
        save();
  	}
    // =====================
    // Save Properties
    // ======================
    private synchronized void save() {
        FileUtil.saveProperties(kPropertyFile, fPropertiesDB,
            " Properties for Java MessagingTool by Y.Shibata");
        if (fChanged)  {
            fChangesButler.firePropertyChange(kName, null, null);
            fChanged = false;
  		}
	}

    private void setChanged() {
        fChanged = true;
	}
    // ============================================
    // Common Utitilies
    // ============================================
    // Boolean
    private boolean getValue(String key, boolean defaultValue) {
        String stringValue = fPropertiesDB.getProperty(key);
        
        if (stringValue == null) {
            setValue(key, defaultValue);
            save();
            return(defaultValue);
		}
		return Boolean.valueOf(stringValue).booleanValue();
	}
    
    private void setValue(String key, boolean value) {
        fPropertiesDB.setProperty(key, String.valueOf(value));
	}

    // Int    
    private int getValue(String key, int defaultValue) {
        String stringValue = fPropertiesDB.getProperty(key);
        
        if (stringValue == null) {
            setValue(key, defaultValue);
            save();
            return defaultValue;
		}    
		return Integer.valueOf(stringValue).intValue();
	}
    
    public synchronized void setValue(String key, int value) {
        fPropertiesDB.setProperty(key, String.valueOf(value));
	}
    // ======================
    // User Name
    // ======================
	public synchronized boolean isUserNameRegistered() {
		String userName = fPropertiesDB.getProperty(kUserNameKey);

		return userName != null;
	}

    public synchronized String getUserName() {
        String      userName = fPropertiesDB.getProperty(kUserNameKey);
        if (userName == null) {
            //
            // User Name is not found in the Properties database. Then 
            // try to get user.name from the System properties.
            //
            userName = System.getProperty("user.name");
            //
            // If userName is still null, then assign "unknown user"
            //
            if (userName == null)
                userName = "unknown user";
      
            setUserName(userName);
            save();
		}
        return(userName);
	}

    public synchronized void setUserName(String userName) {
        if (userName != null && userName.length() > 0)
            fPropertiesDB.setProperty(kUserNameKey, userName);
	}
    // ======================
    // Activate On Reception
    // ======================
    public synchronized boolean isActivateOnReception() {
        return getValue(kActivateOnReceptionKey, false);
	}

    public synchronized void setActivateOnReception(boolean activateOnReception) {
        setValue(kActivateOnReceptionKey, activateOnReception);
	}
    // ======================
    // Beep On Reception
    // ======================
    public synchronized boolean isBeepOnReception() {
        return getValue(kBeepOnReceptionKey, true);
	}
    
    public synchronized void setBeepOnReception(boolean beepOnReception) {
        setValue(kBeepOnReceptionKey, beepOnReception);
	}
    // ======================
    // Activate Window Choice
    // ======================
    public synchronized int getActivateWindowChoice() {
        return getValue(kActivateWindowChoiceKey, 1);
	}
  
    public synchronized void setActivateWindowChoice(int choice) {    
        if (0 <= choice && choice <= 1)
            setValue(kActivateWindowChoiceKey, choice);
        else
            setValue(kActivateWindowChoiceKey, 0);
	}
    // ======================
    // Delive Key Choice
    // ======================
    public int getDeliverKey() { // F1: key = 0, F2: key = 1 ...
        return getValue(kDeliverKeyKey, 0);
	}

    public void setDeliverKey(int key) {
        //
        // key must be between 0 and 11
        //
        if (0 <= key && key <= 11)
            setValue(kDeliverKeyKey, key);
        else
            setValue(kDeliverKeyKey, 0);
	}

    // ===================
    // Font Choice
    // ===================
    public String getFontName() {
        return fPropertiesDB.getProperty(kFontNameKey);
	}
   
    public void setFontName(String fontName) {
		String currentFontName = getFontName();
      
        if (currentFontName != null && currentFontName.equals(fontName))
            return;
      
        setChanged();
        fPropertiesDB.setProperty(kFontNameKey, fontName);
	}
   
    public int getFontStyle() {
        String stringValue = fPropertiesDB.getProperty(kFontStyleKey);
        if (stringValue == null) {
            setFontStyleInternal(0);
            save();
            return 0;
		}
        return Integer.valueOf(stringValue).intValue();
	}
   
    public void setFontStyle(int style) {
        if (style == getFontStyle())
            return;
      
        setChanged();
        setFontStyleInternal(style);
	}
   
    private void setFontStyleInternal(int style) {
        fPropertiesDB.setProperty(kFontStyleKey, String.valueOf(style));
	}
      
    public int getFontSize() {
        String stringValue = fPropertiesDB.getProperty(kFontSizeKey);
        if (stringValue == null) {
            setFontSizeInternal(12);
            save();
            return 12;
		}
        return(Integer.valueOf(stringValue).intValue());
        }
   
    public void setFontSize(int fontSize) {
        if (fontSize == getFontSize())
            return;
      
        setChanged();
        setFontSizeInternal(fontSize);
	}
   
    private void setFontSizeInternal(int fontSize) {
        fPropertiesDB.setProperty(kFontSizeKey, String.valueOf(fontSize));
	}
    // ============================
    // Registered E-mail and number
    // ============================ 
    public synchronized String getEMail() {
        return fPropertiesDB.getProperty(kEMailKey);
	}
    
    public synchronized void setEMail(String email) {
        fPropertiesDB.setProperty(kEMailKey, email);
	}
    
    public synchronized int getRegisteredNumber() {
        return getValue(kRegisteredNumberKey, -1);
	}
    
    public synchronized void setRegisteredNumber(int registeredNumber) {
		setValue(kRegisteredNumberKey, registeredNumber);
	}
    // ============================
    // IP addresses
    // ============================
    public synchronized String getMyIPAddress() {
        return(fPropertiesDB.getProperty(kMyIPAddressKey));
	}
    
    public synchronized void setMyIPAddress(String ip) {
        fPropertiesDB.setProperty(kMyIPAddressKey, ip);
	}
        
    public synchronized String getMyOldIPAddress() {
        return fPropertiesDB.getProperty(kMyOldIPAddressKey);
	}
    
    public synchronized void setMyOldIPAddress(String ip) {
        fPropertiesDB.setProperty(kMyOldIPAddressKey, ip);
	}
    // ====================================
    // Location & Dimensions common methods
    // ====================================
    private synchronized Point getLocation(String xKey, String yKey) {
        return new Point(getValue(xKey, 0), getValue(yKey, 0));
	}

    private synchronized Point getLocation(String xKey, String yKey, int xDefault, int yDefault) {
        return new Point(getValue(xKey, xDefault), getValue(yKey, yDefault));
	}
    
    private synchronized void setLocation(Point location, String xKey, String yKey) {
        setValue(xKey, location.x);
        setValue(yKey, location.y);
	}

    private synchronized Dimension getSize(String widthKey, String heightKey) {
        Dimension   size = new Dimension(
                                getValue(widthKey, 0),
                                getValue(heightKey, 0));
        if (size.width == 0 || size.height == 0)
            return (null);
        else
            return(size);
	}

    private synchronized void setSize(Dimension size, String widthKey, String heightKey) {
        setValue(widthKey, size.width);
        setValue(heightKey, size.height);
	}
    // =================================
    // Location & Dimension of MainFrame
    // =================================
    public Point getLastLocation() {
        return getLocation(kLastLocationXKey, kLastLocationYKey);
	}
    
    public void setLastLocation(Point location) {
        setLocation(location, kLastLocationXKey, kLastLocationYKey);
	}

    public Dimension getLastSize() {
        return getSize(kLastWidthKey, kLastHeightKey);
	}

    public void setLastSize(Dimension size) {
        setSize(size, kLastWidthKey, kLastHeightKey);
	}
    // ==================================
    // Location of Receive Message Dialog
    // ==================================
    public Point getRcvDialogLocation() {
        return(getLocation(kRcvDialogLocationXKey, kRcvDialogLocationYKey));
	}
    
    public void setRcvDialogLocation(Point location) {
        setLocation(location, kRcvDialogLocationXKey, kRcvDialogLocationYKey);
	} 
    // ==================================
    // Save Messages related functions 
    // ==================================
    public synchronized boolean isSaveMessages() {
        return getValue(kSaveMessagesKey, false);
	}
   
    public synchronized void setSaveMessages(boolean save) {
        if (save == isSaveMessages())
            return;
        
        setValue(kSaveMessagesKey, save);
        setChanged();
	}
        
    public synchronized int getNoOfMessageFiles() {
        return getValue(kNoOfMessageFilesKey, 1);
	}
    
    public synchronized void setNoOfMessageFiles(int noOfFiles) {
        if (noOfFiles == getNoOfMessageFiles())
            return;
        
        setValue(kNoOfMessageFilesKey, noOfFiles);
        setChanged();
	}
    // ==================================
    // Location and Size of Online Dialog
    // ==================================
    public Point getOnlineDialogLocation() {
        return getLocation(kOnlineLocationXKey, kOnlineLocationYKey);
	}
   
    public void setOnlineDialogLocation(Point location) {
        setLocation(location, kOnlineLocationXKey, kOnlineLocationYKey);
	}
        
    public Dimension getOnlineDialogSize() {
        return getSize(kOnlineWidthKey, kOnlineHeightKey);
	}
        
    public void setOnlineDialogSize(Dimension size) {
        setSize(size, kOnlineWidthKey, kOnlineHeightKey);
	}
  
    public synchronized boolean isOnlineDialogVisible() {
        return getValue(kOnlineVisibleKey, false);
	}
        
    public synchronized void setOnlineDialogVisible(boolean visible) {
        setValue(kOnlineVisibleKey, visible);
	}
        
    // ====================================
    // Location and Size of RoomList Dialog
    // ====================================
    public Point getRoomListDialogLocation() {
        return getLocation(kRoomListLocationXKey, kRoomListLocationYKey);
	}
   
    public void setRoomListDialogLocation(Point location) {
        setLocation(location, kRoomListLocationXKey, kRoomListLocationYKey);
	}
        
    public Dimension getRoomListDialogSize() {
        return getSize(kRoomListWidthKey, kRoomListHeightKey);
	}
        
    public void setRoomListDialogSize(Dimension size) {
        setSize(size, kRoomListWidthKey, kRoomListHeightKey);
	}
  
    public synchronized boolean isRoomListDialogVisible() {
        return getValue(kRoomListVisibleKey, false);
	}
        
    public synchronized void setRoomListDialogVisible(boolean visible) {
        setValue(kRoomListVisibleKey, visible);
	}
    // ========================================
    // Locatioon and Size of Decdicated Dialogs
    // ========================================
    public Point getLocation(String prefix) {
        Point location = getLocation(prefix + kLocationXSuffix, prefix + kLocationYSuffix, -1, -1);

        if (location.x == -1 || location.y == -1)
            return null;
        else
            return location;
	}

    public void setLocation(String prefix, Point location ) {
        setLocation(location, prefix + kLocationXSuffix, prefix + kLocationYSuffix);
	}

    public Dimension getSize(String prefix) {
        return getSize(prefix + kSizeWidthSuffix, prefix + kSizeHeightSuffix);
	}

    public void setSize(String prefix, Dimension size) {
        setSize(size, prefix + kSizeWidthSuffix, prefix + kSizeHeightSuffix);
	}
    // ===================
    // Look And Feel Choice
    // ===================
    public String getLFName() {
        return fPropertiesDB.getProperty(kLookAndFeelKey);
	}
   
    public void setLFName(String lfName) {
          String currentLFName = getLFName();
      
        if (currentLFName != null && currentLFName.equals(lfName))
            return;
      
        setChanged();
        fPropertiesDB.setProperty(kLookAndFeelKey, lfName);
	}
    // ==================
    // Text Background
    // ==================
    public String getTextBackground() {
        return (String)fPropertiesDB.getProperty(kTextBackgroundKey);
	}
    
    public void setTextBackground(String colorName) {
        String currentColorName = getTextBackground();
        
        if (currentColorName != null && currentColorName.equals(colorName))
            return;
      
        fPropertiesDB.setProperty(kTextBackgroundKey, colorName);
		setChanged();
	}
 	// ====================
	// Joined Meeting Rooms 
	// ====================
	public String[] getJoinedMeetingRooms() {
		String	roomsList = (String) fPropertiesDB.getProperty(kJoinedMeetingRoomsKey);

		if (roomsList == null)
			return null;

		return StringUtil.parseToArray(roomsList);
	}

  	public void setJoinedMeetingRooms(String[] joinedMeetingRooms) {
		if (joinedMeetingRooms == null)
			fPropertiesDB.setProperty(kJoinedMeetingRoomsKey, "");
	  	else {
			String	roomsList = joinedMeetingRooms[0];

			for (int i = 1; i < joinedMeetingRooms.length; i++)
				roomsList += "," + joinedMeetingRooms[i];

			fPropertiesDB.setProperty(kJoinedMeetingRoomsKey, roomsList);
		}
	}
}

// LOG
//        31-Aug-96 Y.Shibata   created
//         5-Sep-96 Y.Shibata   added GetFontIndex/SetFontIndex
//        26-Oct-96 Y.Shibata   made this class Observable.
//        15-Dec-96 Y.Shibata   added TimeZone
//         9-Feb-97 Y.Shibata   fixed SetTimeZone so that setChanges() is called inside it.
//        15-Feb-97 Y.Shibata   added Set/GetBeepOnReception()s
// 1.02 : 25-Feb-97 Y.Shibata   use user.timezone for the first time invokation.
// 1.20 : 31-Mar-97 Y.Shibata   Set/GetEMail()s and Set/GetRegisteredNumber()s.
// 1.30 : 17-Apr-97 Y.Shibata   deleted code for TimeZone
// 1.36 : 14-Jul-97 Y.Shibata   added Get/SetLastLocation.
// 1.38 : 20-Jul-97 Y.Shibata   added Get/SetRcvDialogLocation.
// 1.46 : 13-Aug-97 Y.Shibata   added functions for saving messages
// 1.47 : 19-Aug-97 Y.Shibata   added functions for OnlineDialog
// 1.63 : 20-Sep-97 Y.Shibata   modified initial values of ActivateOnReception
//        21-Sep-97 Y.Shibata   renamed Get/Set to get/set/is
// 1.64 : 23-Sep-97 Y.Shibata   moved to msgtool.beans package.
// 1.69 : 11-Oct-97 Y.Shibata   added functions for RoomList
// 1.75 :  9-Nov-97 Y.Shibata   added generic get/setLocation(), get/setSize()
// 1.90 :  1-Mar-98 Y.Shibata   added get/setLFName().
// 1.90 :  8-Mar-98 Y.Shibata   added get/setTextBackground
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.FileUtil
// 2.14 : 14-Feb-99	Y.Shibata	added isUserNameRegistered()
//								added get/setJoinedMeetingRooms
// 2.40 :  4-Jan-00	Y.Shibata	changed names of methods to lower-case.
// 2.50 : 13-Dec-03 Y.Shibata   all "put" method calls were replaced with "setProperty"
