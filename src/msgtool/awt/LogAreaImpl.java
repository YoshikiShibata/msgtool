// File: LogAreaImpl.java - last edit:
// Yoshiki Shibata 3-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved

package msgtool.awt;

import java.awt.Color;
import java.awt.Frame;
import java.awt.TextArea;

import msgtool.log.AbstractLogArea;
import msgtool.log.LogArea;
import msgtool.log.Logging;
import msgtool.util.StringDefs;


class LogAreaImpl extends AbstractLogArea implements LogArea {

	LogAreaImpl(
		Frame		parentFrame,
		TextArea 	textArea, 
		Logging logging) {
		super(logging);
		fParentFrame	= parentFrame;
		fTextArea 		= textArea;
  	}

	LogAreaImpl(
		Frame		parentFrame,
		TextArea 	textArea) {
		super();
		fParentFrame	= parentFrame;
		fTextArea		= textArea;
	}

    public synchronized void unlock() {
		if (isOverflowed()) {
            showOverflowWarning();
            resetWithLastMessage();
        }
        updateTotalLength();
		super.unlock();
   	}

    public void appendSubText(String text) {
		if (text != null) {
        	fTextArea.append(text);
			super.appendSubText(text);
	  	}
  	}
    
    public void clear() {
      	fTextArea.setText("");
		fTotalLength = 0;
   	}

	public void scrollDownToEnd() {
            // Always select a unexisting position, so that the window will scroll up
            fTextArea.select(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public void appendText(String text, Color color) {
		appendText(text); // color is not supported.
	}

	public Color getTextColor() {
		return fTextColor;
  	}

	public void setTextColor(Color color) {
		fTextColor = color;
	}
	// =========================
	// Private Methods
	// =========================
 	private boolean isOverflowed() {
        return (fTextArea.getText().length() < (fTotalLength + getLastMessageLength()));
	}

	private void resetWithLastMessage() {
		fTextArea.setText(getLastMessage());
	}

	private void showOverflowWarning() {
		new NoticeUI(fParentFrame, StringDefs.LOG_OVERFLOWED).setVisible(true);
	}

	private void updateTotalLength() {
		fTotalLength = fTextArea.getText().length();
	}
	// =========================
	// Private Fields
	// =========================
	private final TextArea 		fTextArea;
	private final Frame			fParentFrame;

	private int		fTotalLength 	= 0;
	private Color	fTextColor = Color.black;
}
// LOG
// 2.32 : 12-Aug-99	Y.Shibata	created from MainFrame.java [Refactoring]
// 2.35 :  3-Nov-99	Y.Shibata	implemented LogArea interface.
//								LogAreaHandler -> LogAreaImpl
