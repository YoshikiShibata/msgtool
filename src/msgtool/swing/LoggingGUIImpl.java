// File : LoggingGUIImpl.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import msgtool.log.LoggingGUI;


class LoggingGUIImpl implements LoggingGUI {

	LoggingGUIImpl(
		JFrame	parentFrame,
		JMenu 	menu,
		JMenu	topMenu) {
		fParentFrame	= parentFrame;
		fMenu 			= menu;
		fTopMenu		= topMenu;
	}

	LoggingGUIImpl(
		JFrame	parentFrame,
		JMenu	menu) {
 		this(parentFrame, menu, null);
	}
	
	// ===========================
	// LoggingGUI implementation
	// ===========================

	public void clearAllMenus() {
		fMenu.removeAll();
	}

	public void addMenuItem(String title, Font font, ActionListener listener) {
		JMenuItem	menuItem = new JMenuItem(title);
		
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
		return (((JMenuItem)(event.getSource())).getText());
	}

	// ==============================
	// Private fields
	// ==============================
	private final JFrame	fParentFrame;
	private final JMenu		fMenu;
	private final JMenu		fTopMenu;

}

// LOG 
// 2.33 : 21-Aug-99	Y.Shibata	created [Refactoring]
