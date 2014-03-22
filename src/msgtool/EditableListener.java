// File: EditableListener.java - last edit:
// Yoshiki Shibata 4-May-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool;

public interface EditableListener {
	String	title();
	void	construct(EditableDialog	dialog);
	void 	show();
	void 	save();
	void 	add();
	void 	delete(int selectedIndex);
	void 	update(int selectedIndex);
	void 	select(int selectedIndex);
}

// LOG
// 2.24 :  4-May-99	Y.Shibata	created
