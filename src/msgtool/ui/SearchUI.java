// File: SearchUI.java - last edit:
// Yoshiki Shibata 30-Dec-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.ui;

import java.awt.Color;

public interface SearchUI {	
	void setVisible(boolean visible);
	void appendLog(String text, Color color);
}

// LOG
// 2.39 : 30-Dec-99	Y.Shibata	created