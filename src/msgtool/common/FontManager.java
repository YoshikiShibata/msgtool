// File: FontManager.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1999, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import msgtool.db.PropertiesDB;

public class FontManager implements PropertyChangeListener {
	// ============================
	// public interface
	// ============================
	public interface FontListener {
		void setFont(Font font);
		void fontChanged(Font font);
	}
	// =================
	// Constructors
	// =================
	private FontManager() {
		fPropertiesDB.addPropertyChangeListener(this);
	}
	// =================
	// public methods
	// =================
	public void addComponent(Component c) {
		if (fFont == null)
			fFont = Context.getFont();
	 	if (fFont == null)
 	    	return;
		c.setFont(fFont);
		synchronized(fComponentList) {
			fComponentList.add(c);
		}
	}

	public void addFontListener(FontListener l) {
		if (fFont == null)
			fFont = Context.getFont();
	 	if (fFont == null)
			return;
	 	l.setFont(fFont);
		synchronized(fFontListenerList) {
			fFontListenerList.add(l);
		}
	}
	public void addContainer(Container c) {
		addContainer(c, false);
	}

	public void addContainer(Container c, boolean preserveSize) {
		ContainerInfo containerInfo = new ContainerInfo(c, preserveSize);
		synchronized(fContainerList) {
			fContainerList.add(containerInfo);
		}
	}
	public static FontManager	getInstance() { return (fInstance) ; }

	// =====================================
	// PropertyChangeListener implementation
	// =====================================
	public void propertyChange(PropertyChangeEvent event) {
		if (!(event.getPropertyName().equals(PropertiesDB.kName)))
			return;
		
		Context.updateFont();
		fFont = Context.getFont();
		if (fFont == null)
			return;

        synchronized(fFontListenerList) {
            for (FontListener l: fFontListenerList)
                l.fontChanged(fFont);
        }

		synchronized(fComponentList) {
            for (Component c: fComponentList) {
				c.setFont(fFont);
				c.invalidate();
			}
		}

		//
		// Forcing a Window to Resize
		// Please see p.341 of "Graphic Java: Mastering the JFC, 3rd edition, volume.1"
		//
		synchronized(fContainerList) {
            for (ContainerInfo containerInfo: fContainerList) {
				Container	container = containerInfo.container;
				if (containerInfo.preserveSize) {
					Dimension	size = container.getSize();
					container.setSize(size.width, size.height);
				} else {
					Dimension	preferredSize = container.getPreferredSize();
					container.setSize(preferredSize.width, preferredSize.height);
				}
			 	container.validate();
			}
		}
	}


	private static FontManager	fInstance 		= new FontManager();
	private ArrayList<Component> fComponentList	= new ArrayList<Component>();
    private ArrayList<FontListener> fFontListenerList = new ArrayList<FontListener>();
	private ArrayList<ContainerInfo> fContainerList	= new ArrayList<ContainerInfo>();
	private PropertiesDB		fPropertiesDB	= PropertiesDB.getInstance();
	private Font				fFont			= null;

	// ===================
	// Private inner class
	// ===================
	private class ContainerInfo {
		Container	container;
		boolean		preserveSize;

		public ContainerInfo(Container container, boolean preserveSize) {
			this.container 		= container;
			this.preserveSize	= preserveSize;
		}

		public ContainerInfo(Container container) {
			this.container		= container;
			this.preserveSize	= false;
		}
	}
}

// LOG
// 2.24 :  5-May-99	Y.Shibata	created
// 2.50 : 19-Nov-03	Y.Shibata	modified to be compiled cleanly with "Tiger"(SDK1.5).
//        27-Dec-03 Y.Shibata   used Java Generics and the enhanced "for"

