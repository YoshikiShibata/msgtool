// File: LogArea.java - last edit:
// Yoshiki Shibata 3-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata

package msgtool.log;

import java.awt.Color;

public interface LogArea {
	void clear();

	void appendDate();
	void appendText(String text);
	void appendText(String text, Color color);
	void appendSubText(String text);

	String	getLastMessage();
	Color 	getTextColor();
	void 	setTextColor(Color color);

	void 	scrollDownToEnd();

	void lock();
	void unlock();
}

// LOG
// 2.35 :  3-Nov-99	Y.Shibata	created.