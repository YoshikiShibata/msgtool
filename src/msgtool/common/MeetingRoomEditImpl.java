// File: MeetingRoomEditImpl.java - last edit:
// Yoshiki Shibata 13-Nov-2004

// Copyright (c) 1999, 2003, 2004 by Yoshiki Shibata. All rights reserved

package msgtool.common;

import java.util.Vector;

import msgtool.EditableDialog;
import msgtool.EditableListener;
import msgtool.db.MeetingRoomDB;
import msgtool.util.StringDefs;

public class MeetingRoomEditImpl implements EditableListener {
	public MeetingRoomEditImpl() {}

	// ==================================
	// Implementation of EditableListener
	// ==================================
	public String title() { return (StringDefs.EDIT_MEETING_ROOM); }

	public void construct(EditableDialog dialog) {
		fEditableDialog = dialog;
		fEditableDialog.addText(kName);
		fEditableDialog.addBoolean(kFetchLog);
	}

	public void show() {
		clearAllFields();
		fEditableDialog.removeAllItems();
		fFetchLogList.removeAllElements();

		String[] names = fMeetingRoomDB.getRooms();
	 	SortUtil.sortStrings(names);
	 	for (String name: names) {
			fEditableDialog.addItem(name);
			fFetchLogList.addElement(
					new FetchLogInfo(fMeetingRoomDB.getFetchLogValue(name)));
		}
	}

	public void save() {
		int				noOfNames 		= fEditableDialog.getItemCount();
		FetchLogInfo	fetchLogInfo	= null;
		String			name 			= null;

		fMeetingRoomDB.clear();
		for (int i = 0; i < noOfNames; i++) {
			fetchLogInfo = fFetchLogList.elementAt(i);
			name = fEditableDialog.getItemAt(i);
			fMeetingRoomDB.add(name, fetchLogInfo.fetchLog);
		}
		fEditableDialog.removeAllItems();
		fFetchLogList.removeAllElements();
		fMeetingRoomDB.save();
	}

	public void add() {
		String	name = fEditableDialog.getText(kName);
		boolean	fetchLog	= fEditableDialog.getBoolean(kFetchLog);

		if (name.length() == 0)
			return;

	  	FetchLogInfo fetchLogInfo = new FetchLogInfo(fetchLog);
		//
		// Check if this meeting room name is already listed. If it is listed,
		// then just replace its associated fetchLogInfo
		//
		int index = fEditableDialog.getItemIndex(name);
		if (index == -1) {
			fEditableDialog.addItem(name);
			fFetchLogList.addElement(fetchLogInfo);
		} else
			fFetchLogList.setElementAt(fetchLogInfo, index);
	  	clearAllFields();
	}

	public void delete(int selectedIndex) {
		fEditableDialog.removeItemAt(selectedIndex);
		fFetchLogList.removeElementAt(selectedIndex);
		clearAllFields();
	}

	public void update(int selectedIndex) {
		fEditableDialog.removeItemAt(selectedIndex);
		fFetchLogList.removeElementAt(selectedIndex);
		add();
		clearAllFields();
	}

	public void select(int selectedIndex) {
		String	name = fEditableDialog.getItemAt(selectedIndex);
		FetchLogInfo	fetchLogInfo = fFetchLogList.elementAt(selectedIndex);

		fEditableDialog.setText(kName, name);
		fEditableDialog.setBoolean(kFetchLog, fetchLogInfo.fetchLog);
	}
	// ===================================
	// Private methods
	// ===================================
	private void clearAllFields() {
		fEditableDialog.setText(kName, "");
		fEditableDialog.setBoolean(kFetchLog, false);
	}
	// ===================================
	// Private fields
	// ===================================
	private final static String kName		= StringDefs.ROOM_NAME_C;
	private final static String kFetchLog	= StringDefs.FETCH_LOG;

	private MeetingRoomDB	       fMeetingRoomDB	= MeetingRoomDB.getInstance();
	private EditableDialog	       fEditableDialog	= null;
	private Vector<FetchLogInfo>   fFetchLogList	= new Vector<FetchLogInfo>();

	// ====================================
	// Private inner classes
	// ====================================
	private class FetchLogInfo {
		public boolean	fetchLog;
	
		public FetchLogInfo(boolean fetchLog) { this.fetchLog = fetchLog; }
 	}
}

// LOG
// 2.24 :  5-May-99	Y.Shibata	created.
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
// 2.52 : 13-Nov-04 Y.Shibata	refactored.
