// File: DedicatedUI.java - last edit:
// Yoshiki Shibata 30-Oct-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.ui;

import java.awt.Color;

public interface DedicatedUI {

	void appendLog(String log);
	void appendLog(String log, Color color);
	void updateRecipientHintsPopup();

	String	getSenderIP();
	String	getTitle();

	boolean isVisible();

	void setAdditionalRecipients(String	recipients);
	void setMenuItem(Object menuItem);
	void setDeliverEnabled(boolean deliverEnabled);
	void setVisible(boolean visible);

	void toFront();
}

// LOG
// 2.35 : 30-Oct-99	Y.Shibata	created.