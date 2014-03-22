/*
 * File: AboutUI.java - last edit:
 * Yoshiki Shibata 5-Sep-2004
 *
 * Copyright (c) 1999 - 2004 by Yoshiki Shibata. All rights reserved.
 */

package msgtool.ui;

public interface AboutUI {
    int     kMessagingTool      = 1;
    int     kSystemProperties   = 2;
    int     kMessageReceived    = 3;
    
    void setVisible(boolean visible);
	void toFront();
}

// LOG
// 2.39 : 31-Dec-99	Y.Shibata	created.