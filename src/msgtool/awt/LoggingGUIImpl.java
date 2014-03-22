// File : LoggingGUIImpl.java - last edit:
// Yoshiki Shibata 21-Aug-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import msgtool.log.LoggingGUI;


class LoggingGUIImpl implements LoggingGUI {

	LoggingGUIImpl(
		Frame	parentFrame,
		Menu 	menu,
		Menu	topMenu) {
		fParentFrame	= parentFrame;
		fMenu 			= menu;
		fTopMenu		= topMenu;
	}

	LoggingGUIImpl(
		Frame	parentFrame,
		Menu	menu) {
 		this(parentFrame, menu, null);
	}
	
	// ===========================
	// LoggingGUI implementation
	// ===========================

	public void clearAllMenus() {
		fMenu.removeAll();
	}

	public void addMenuItem(String title, Font font, ActionListener listener) {
		MenuItem	menuItem = new MenuItem(title);
		
		menuItem.setFont(font);
		menuItem.addActionListener(listener);
		fMenu.add(menuItem);
	}

	public void setMenuItemsEnabled(boolean enabled) {
		fMenu.setEnabled(enabled);
		if (fTopMenu != null)
			fTopMenu.setEnabled(true);
	
	}
	public void showLog(String title, String log) {
		new LogViewUI(fParentFrame, title, log).setVisible(true);
	}

	public String getMenuText(ActionEvent event) {
		return (((MenuItem)(event.getSource())).getLabel());
	}

	// ==============================
	// Private fields
	// ==============================
	private final Frame	fParentFrame;
	private final Menu	fMenu;
	private final Menu	fTopMenu;

}

// LOG 
// 2.33 : 21-Aug-99	Y.Shibata	created [Refactoring]
