// File: EditableDialog.java - last edit:
// Yoshiki Shibata 4-May-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved

package msgtool;

public interface EditableDialog {
	void 	addItem(String	item);
	void	removeAllItems();
	void 	removeItemAt(int index);
	String	getItemAt(int index);
	int		getItemIndex(String item);
	int		getItemCount();

	void 	addText(String fieldName);
	void 	setText(String fieldName, String text);
	String 	getText(String fieldName);

	void 	addBoolean(String fieldName);
	void 	setBoolean(String fieldName, boolean value);
	boolean getBoolean(String fieldName);
}

// LOG
// 2.24 :  4-May-99	Y.Shibata	created.
