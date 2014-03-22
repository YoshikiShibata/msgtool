// File: Context.java - last edit:
// Yoshiki Shibata 12-Apr-2003

// Copyright (c) 1997 - 2000, 2002, 2003 Yoshiki Shibata. All rights reserved.

package msgtool.common;

import java.awt.Font;

import msgtool.db.PropertiesDB;

public final class Context {
    
	final static public int 	WINDOW_WIDTH    = 45;
	final static public int	SPLITTER_WIDTH 	= 8;	// used only by Swing version.
    
	static private Font    	displayFont = null;
	static private String		jdkVersion	= null;

	static {
		String	javaVersion = (String) System.getProperty("java.version");
		jdkVersion = javaVersion.substring(0, 3);
	}
    
	static public synchronized Font    getFont() {
		return displayFont;
	}
    
	static public synchronized void    setFont(Font font) {
		displayFont = font;
	}

	static public synchronized void 	updateFont() {
		PropertiesDB	propertiesDB = PropertiesDB.getInstance();
	 	String fontName = propertiesDB.getFontName();
        
        if (fontName == null) {
            setFont(new Font("Serif", 0, 12));
            propertiesDB.setFontName("Serif");
            propertiesDB.setFontStyle(0);
            propertiesDB.setFontSize(12);
   		} else {
            setFont(new Font(fontName,
            			propertiesDB.getFontStyle(),
                        propertiesDB.getFontSize()));
	  	}
	}

	static public synchronized String getJDKVersion() {
		return jdkVersion;
	}

	static public synchronized boolean isJDK12() {
		return jdkVersion.equals("1.2");
	}

	static public synchronized boolean isJDK13() {
		return jdkVersion.equals("1.3");
	}
}

// 1.46 :  9-Aug-97 Y.Shibata   created.
// 1.60 : 13-Sep-97 Y.Shibata   added CursorControl
// 1.71 : 25-Oct-97 Y.Shibata   protocol -> messageProtocol
//                              protocol2 -> miscProtocol
// 1.95 : 12-Jul-98 Y.Shibata    use msgtool.protocol.*
//        18-Jul-98 Y.Shibata   moved to msgtool.common
// 2.11 : 16-Jan-99	Y.Shibata	added kSplitterWidth
// 2.12 :  7-Feb-99	Y.Shibata	added set/getJDKVersion() 
// 2.34 :  9-Sep-99	Y.Shibata	added isJDK13()
// 2.40 :  4-Jan-00	Y.Shibata	changed names of methods to lower-case
// 2.43 :  5-Mar-00	Y.Shibata	added updateFont()
// 2.50 : 31-Dec-02 Y.Shibata	deleted setJDKVersion method
//		  12-Apr-03 Y.Shibata	refactored ...
