// File: MeetingRoomDB.java - last edit:
// Yoshiki Shibata 25-Apr-98

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.db;

import java.util.Properties;

import msgtool.util.FileUtil;
import msgtool.util.StringUtil;

public class MeetingRoomDB {
	private MeetingRoomDB() {
		FileUtil.loadProperties(kMeetingRoomDBFile, fRoomsCacheDB);
	}

	public synchronized void add(String	room, boolean fetchLog) {
		Boolean	bool	= new Boolean(fetchLog);

		StringUtil.setProperty(fRoomsCacheDB, room, bool.toString());
	}

	public synchronized void clear() {
		fRoomsCacheDB.clear();
   	}

	static public MeetingRoomDB	getInstance() { return (fInstance) ; }

	public synchronized String[] getRooms() {
		return(StringUtil.getPropertyNames(fRoomsCacheDB));
	}

	public synchronized boolean getFetchLogValue(String room) {
		String	valueString = StringUtil.getProperty(fRoomsCacheDB, room);

		if (valueString == null) 
			return(true);
	  	
		return(Boolean.valueOf(valueString).booleanValue());
	}
	public synchronized void save() {
		FileUtil.saveProperties(kMeetingRoomDBFile, fRoomsCacheDB,
			" Meeting Rooms for Java version of MessagingTool by Y.Shibata");
  	}

	// ====================
	// Private fields
	// ===================
	static final private String		kMeetingRoomDBFile	= "MessagingTool.rooms";
	static private MeetingRoomDB	fInstance 			= new MeetingRoomDB();

	private Properties 	fRoomsCacheDB	= new Properties();
}

// LOG
// 2.24 : 25-Apr-99	Y.Shibata	created.
