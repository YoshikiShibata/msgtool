// File: ParticipantsUI.java - last edit:
// Yoshiki Shibata 21-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.ui;

public interface ParticipantsUI {
	void join(String participants);
	void leave(String participants);
	void clearList();
	void setVisible(boolean visible);
	boolean isVisible();
}

// LOG
// 2.36 : 21-Nov-99	Y.Shibata	created.