// File: LoggingGUI.java - last edit:
// Yoshiki Shibata 21-Aug-99

// Copyright (c) 1999 by Yoshiki Shibata

package msgtool.log;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface LoggingGUI {
	void clearAllMenus();
	void addMenuItem(String title, Font font, ActionListener listener);
	void setMenuItemsEnabled(boolean enabled);
	void showLog(String title, String log);
	String getMenuText(ActionEvent event);
}

// LOG
// 2.33 : 21-Aug-99	Y.Shibata	created [Refactoring]
