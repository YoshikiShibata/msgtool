// File: Logging.java - last edit:
// Yoshiki Shibata 21-Aug-99

// Copyright (c) 1999 by Yoshiki Shibata

package msgtool.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import msgtool.common.Context;
import msgtool.db.PropertiesDB;
import msgtool.util.StringDefs;

public class Logging implements ActionListener {

	public Logging(
		String		logFilePrefix,
		LoggingGUI	gui) {
		this(logFilePrefix, gui, null);
	}

	public Logging(
		String		logFilePrefix,
		LoggingGUI 	gui,
		String		middleTitle) {
		fGUI 			= gui;
		fMiddleTitle	= middleTitle;
		fLogMessages	= new LogMessages(logFilePrefix);
		fLoggingEnabled	= PropertiesDB.getInstance().isSaveMessages();
		fNoOfLogFiles 	= PropertiesDB.getInstance().getNoOfMessageFiles();
		if (fLoggingEnabled)
			start();
	}

	public void stop() {
		fLogMessages.stopLogging();
		fGUI.setMenuItemsEnabled(false);
	}

	public void update() {
		if (fLoggingEnabled != PropertiesDB.getInstance().isSaveMessages()) {
			fLoggingEnabled = !fLoggingEnabled;
			if (fLoggingEnabled)
		 		start();
			else
				stop();
		} else if (fLoggingEnabled &&
			(fNoOfLogFiles != PropertiesDB.getInstance().getNoOfMessageFiles())) {
	 		fNoOfLogFiles = PropertiesDB.getInstance().getNoOfMessageFiles();
			fLogMessages.setNoOfLogFiles(fNoOfLogFiles);
			createHeaderMenus();
		}
	}

	public void logMessage(String	message) {
		fLogMessages.logMessage(message);
	}
	// ===================================
	// ActionListener implementation
	// ===================================
	public void actionPerformed(ActionEvent event) {
		String	menuText = fGUI.getMenuText(event);

		if (fMiddleTitle == null)
			fGUI.showLog(StringDefs.LOG_C + menuText, 
						 fLogMessages.getContent(menuText));
	  	else
			fGUI.showLog(StringDefs.LOG_C + fMiddleTitle + ':' + menuText,
						 fLogMessages.getContent(menuText));
	}
	// ==========================
	// Private methods
	// ==========================
	private void createHeaderMenus() {
		String[]    logFiles = fLogMessages.getHeaders();

		fGUI.clearAllMenus();
		if ((logFiles == null) || (logFiles.length == 0))
			return;
	  	for (int i = 0; i < logFiles.length; i++) {
			fGUI.addMenuItem(logFiles[i], Context.getFont(), this);
		}
	}

	private void start() {
		fLogMessages.setNoOfLogFiles(PropertiesDB.getInstance().getNoOfMessageFiles());
		fLogMessages.startLogging();
		createHeaderMenus();
		fGUI.setMenuItemsEnabled(true);
	}

	// ===========================
	// Private fields
	// ===========================
	private final LoggingGUI	fGUI;
	private final String		fMiddleTitle;
	private final LogMessages	fLogMessages;
	private boolean				fLoggingEnabled;
	private int					fNoOfLogFiles;
}

// LOG
// 2.33 : 21-Aug-99	Y.Shibata	created
