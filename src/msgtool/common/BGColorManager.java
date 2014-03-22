// File: BGColorManager.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1999, 2003 by Yoshiki Shibata
// All rights reserved.

package msgtool.common;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import msgtool.db.PropertiesDB;
import msgtool.util.ColorMap;

public class BGColorManager implements PropertyChangeListener {
	private static BGColorManager	fInstance = new BGColorManager();
	public static BGColorManager	getInstance() { return(fInstance); }

  	private ArrayList<Component> fComponentList = new ArrayList<Component>();
	PropertiesDB	fPropertiesDB	= PropertiesDB.getInstance();
	Color			fBGColor		= null;

	private BGColorManager() {
		fBGColor = ColorMap.getColorByName(fPropertiesDB.getTextBackground());
		fPropertiesDB.addPropertyChangeListener(this);
	}

	public void add(Component c) {
		c.setBackground(fBGColor);
		synchronized(fComponentList) {
			fComponentList.add(c);
	 	}
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PropertiesDB.kName)) {
			Color newColor =  ColorMap.getColorByName(fPropertiesDB.getTextBackground());
			if (newColor != fBGColor) {
				fBGColor = newColor;
				synchronized(fComponentList) {
                    for (Component c: fComponentList) 
						c.setBackground(fBGColor);
				}
			}
		}
	}
}

// LOG
// 2.50 : 19-Nov-03	Y.Shibata	modified to be compiled cleanly with "Tiger"(SDK1.5).
//        27-Dec-03 Y.Shibata   used Java Generics and the enhanced "for"
