// File: AbstractLogArea.java - last edit:
// Yoshiki Shibata 5-Oct-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.log;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class AbstractLogArea {

	public AbstractLogArea(Logging logging) {
		fLogging	= logging;
  	}

	public AbstractLogArea() {
		fLogging	= null;
	}

	public synchronized void lock() {
        while (fLocked) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        fLocked = true;
        fLastMessage.setLength(0);
  	}

    public synchronized void unlock() {
		saveLastMessage();
		scrollDownToEnd();
        fLocked = false;
        notifyAll();
   	}

    public String getLastMessage() {
        return (fLastMessage.toString());
  	}
        
    public void appendDate() {
		fCurrentDate.setTime(System.currentTimeMillis());
        appendSubText(fDateFormat.format(fCurrentDate) + " ");
   	}

	public void appendText(String text) {
   		lock();
		appendSubText(text);
		unlock();
	}

   	public void appendSubText(String text) {
		fLastMessage.append(text);
	}

	public void setTextColor(Color textColor) {
		fTextColor = textColor;
	}

	public Color getTextColor(Color textColor) {
		return (fTextColor);
	}

    public abstract void clear();
	public abstract void scrollDownToEnd();

	public int getLastMessageLength() {
		return (fLastMessage.length());
	}
	// =========================
	// Private Methods
	// =========================
	private void saveLastMessage() {
	    // Save the message into a Log message file.
        if (fLogging != null)
        	fLogging.logMessage(fLastMessage.toString());
	}
	// =========================
	// Private Fields
	// =========================
	private final Logging	fLogging;

	private StringBuilder	fLastMessage 	= new StringBuilder(256);
	private boolean			fLocked			= false;

	private DateFormat  	fDateFormat 	= DateFormat.getDateTimeInstance(
                        						DateFormat.MEDIUM, 
												DateFormat.MEDIUM, 
												Locale.getDefault());
   	private	Date			fCurrentDate 	= new Date();
	private Color			fTextColor		= Color.black;
}

// LOG
// 2.33 : 21-Aug-99	Y.Shibata	created.
// 2.35 :  5-Oct-99	Y.Shibata	replaced a tab with a space in appendDate()
//								LogAreaHandle -> AbstractLogArea
