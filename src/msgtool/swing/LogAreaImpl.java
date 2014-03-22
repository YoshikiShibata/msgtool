// File: LogAreaImpl.java - last edit:
// Yoshiki Shibata 3-Nov-99

// Copyright (c) 1999 Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Color;

import msgtool.log.AbstractLogArea;
import msgtool.log.LogArea;
import msgtool.log.Logging;

class LogAreaImpl extends AbstractLogArea implements LogArea {

	LogAreaImpl(
		StyledTextArea 	textArea, 
		Logging 		logging) {
		super(logging);
		fTextArea 		= textArea;
  	}

	LogAreaImpl(
		StyledTextArea 	textArea) {
		super();
		fTextArea		= textArea;
	}

    public synchronized void unlock() {
		super.unlock();
   	}

    public void appendSubText(String text) {
		if (text != null && text.length() > 0) {
        	fTextArea.appendText(text);
			super.appendSubText(text);
	  	}
  	}

	public void appendText(String text, Color textColor) {
		if (text != null && text.length() > 0) {
			lock();
			Color   originalColor = fTextArea.getTextColor();

        	fTextArea.setTextColor(textColor);
        	appendSubText(text);
        	fTextArea.setTextColor(originalColor);
			unlock();
	 	}
	}
   
    public void clear() {
      	fTextArea.setText("");
   	}

	public void scrollDownToEnd() {
		fTextArea.scrollToBottom();
	}

	public void setTextColor(Color textColor) {
		fTextArea.setTextColor(textColor);
	}

	public Color getTextColor() {
		return (fTextArea.getTextColor());
	}
	// =========================
	// Private Fields
	// =========================
	private final StyledTextArea 	fTextArea;
}

// LOG
// 2.33 : 21-Aug-99	Y.Shibata	created
// 2.35 :  3-Nov-99	Y.Shibata	implemented LogArea interface
//								LogAreaHandler -> LogAreaImpl
