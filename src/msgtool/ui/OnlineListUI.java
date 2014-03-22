// File: OnlineListUI.java - last edit:
// Yoshiki Shibata 6-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.ui;

public interface OnlineListUI {

	void 		clearAllMessageWaitings();
	void		clearList();

	String[] 	getOnlines();
	void 		setOnline(String name);
	void 		setOffline(String name);
	void		setMessageWaiting(String name, boolean waiting);
	void		setNotInOffice(String name, boolean notInOffice);

	void 		saveState();
	void 		setVisible(boolean visible);
}

// LOG
// 2.35 :  6-Nov-99	Y.Shibata	created.