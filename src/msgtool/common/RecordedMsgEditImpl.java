// File: RecordedMsgEditImpl.java - last edit:
// Yoshiki Shibata 13-Nov-2004	

// Copyright (c) 1999, 2004 by Yoshiki Shibata
// All rights reserved.

package msgtool.common;

import msgtool.EditableDialog;
import msgtool.EditableListener;
import msgtool.db.RecordedMsgHintsDB;
import msgtool.util.StringDefs;

public class RecordedMsgEditImpl implements EditableListener {

	public RecordedMsgEditImpl() {} 

	// ==================================
	// Implementation of EditableListener
	// ==================================
	public String title() { return(StringDefs.EDIT_RECORDED_MESSAGE_HINTS); }

	public void construct(EditableDialog dialog) {
		fEditableDialog = dialog;
		fEditableDialog.addText(kRecordedMsg);
	}

	public void show() {
		clearAllFields();
		fEditableDialog.removeAllItems();

		String hints[] = fRecordedMsgHintsDB.getHintsOfRecordedMsg();
	  	SortUtil.sortStrings(hints);
	  	for (String hint : hints)
			fEditableDialog.addItem(hint);
	}

	public void save() {
		int	noOfRecordedMsg	= fEditableDialog.getItemCount();

		fRecordedMsgHintsDB.clearRecordedMsgHints();
		for (int i = 0; i < noOfRecordedMsg; i++) {
			fRecordedMsgHintsDB.addRecordedMsgHint(fEditableDialog.getItemAt(i));
		}
		fEditableDialog.removeAllItems();
		fRecordedMsgHintsDB.save();
	}

	public void add() {
		String	hint		= fEditableDialog.getText(kRecordedMsg);

		if (hint.length() == 0)
			return;
		//
		// Check if this message is listed, do nothing.
		//
		int index = fEditableDialog.getItemIndex(hint);
		if (index == -1) 
			fEditableDialog.addItem(hint);

		clearAllFields();
	}

	public void delete(int selectedIndex) {
	  	fEditableDialog.removeItemAt(selectedIndex);
		clearAllFields();
	}

	public void update(int selectedIndex) {
		fEditableDialog.removeItemAt(selectedIndex);
		add();
		clearAllFields();
	}

	public void select(int selectedIndex) {
		String	hint = fEditableDialog.getItemAt(selectedIndex);

		fEditableDialog.setText(kRecordedMsg, hint);
	}

	// ===================================
	// Private methods
	// ===================================
	private void clearAllFields() {
		fEditableDialog.setText(kRecordedMsg, "");
	}
	// ===================================
	// Private fields
	// ===================================
	private final static String	kRecordedMsg	= StringDefs.MESSAGE_C;

	private RecordedMsgHintsDB	fRecordedMsgHintsDB = RecordedMsgHintsDB.getInstance();

 	private EditableDialog	fEditableDialog	= null;
}

// LOG
// 2.24 :  4-May-99	Y.Shibata created.
// 2.52 : 13-Nov-04 Y.Shibata refactored.