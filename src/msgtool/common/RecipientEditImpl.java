// File: RecipientEditImpl.java - last edit:
// Yoshiki Shibata 13-Nov-2004	

// Copyright (c) 1999, 2003, 2004 by Yoshiki Shibata
// All rights reserved.

package msgtool.common;

import java.util.Vector;

import msgtool.EditableDialog;
import msgtool.EditableListener;
import msgtool.db.RecipientHintsDB;
import msgtool.util.StringDefs;

public class RecipientEditImpl implements EditableListener {

	public RecipientEditImpl() {}

	// ==================================
	// Implementation of EditableListener
	// ==================================
	public String title() { return(StringDefs.EDIT_RECIPIENT_HINTS); }

	public void construct(EditableDialog dialog) {
		fEditableDialog = dialog;
		fEditableDialog.addText(kRecipient);
		fEditableDialog.addText(kMembers);
	}
	public void show() {
		clearAllFields();
		fEditableDialog.removeAllItems();
		fMembers.removeAllElements();

		String hints[] = fRecipientHintsDB.getHintsOfRecipients();
	   	SortUtil.sortStrings(hints);
	   	for (String hint : hints) {
			fEditableDialog.addItem(hint);
			fMembers.addElement(fRecipientHintsDB.getExpandedHint(hint));
		}
	}

	public void save() {
		int	noOfRecipients	= fEditableDialog.getItemCount();

		fRecipientHintsDB.clearRecipientHints();
		for (int i = 0; i < noOfRecipients; i++) {
			fRecipientHintsDB.addRecipientHint(
				fEditableDialog.getItemAt(i),
				fMembers.elementAt(i));
		}
		fEditableDialog.removeAllItems();
		fMembers.removeAllElements();
		fRecipientHintsDB.save();
	}

	public void add() {
		String	hint		= fEditableDialog.getText(kRecipient);
		String	members		= fEditableDialog.getText(kMembers);

		if (hint.length() == 0)
			return;
		//
		// Check if this name is already listed. If it is listed,
		// then just replace its assocaited members.
		//
		int index = fEditableDialog.getItemIndex(hint);
		if (index == -1) {
			fEditableDialog.addItem(hint);
			fMembers.addElement(members);
			}
		else
			fMembers.setElementAt(members, index);

		clearAllFields();
	}

	public void delete(int selectedIndex) {
	  	fEditableDialog.removeItemAt(selectedIndex);
		fMembers.removeElementAt(selectedIndex);
		clearAllFields();
	}

	public void update(int selectedIndex) {
		fEditableDialog.removeItemAt(selectedIndex);
		fMembers.removeElementAt(selectedIndex);
		add();
		clearAllFields();
	}

	public void select(int selectedIndex) {
		String	hint = fEditableDialog.getItemAt(selectedIndex);
		String	members = fMembers.elementAt(selectedIndex);

		fEditableDialog.setText(kRecipient, hint);
		fEditableDialog.setText(kMembers, members);
	}

	// ===================================
	// Private methods
	// ===================================
	private void clearAllFields() {
		fEditableDialog.setText(kRecipient, "");
		fEditableDialog.setText(kMembers, "");
	}
	// ===================================
	// Private fields
	// ===================================
	private final static String	kRecipient	= StringDefs.RECIPIENT_C;
	private final static String	kMembers	= StringDefs.MEMBERS_C;

	private RecipientHintsDB	fRecipientHintsDB = RecipientHintsDB.getInstance();

 	private EditableDialog	fEditableDialog	= null;
	
	private Vector<String>	fMembers	= new Vector<String>();
}

// LOG
// 2.24 :  4-May-99	Y.Shibata	created.
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
// 2.52 : 13-Nov-04 Y.Shibata	refactored
