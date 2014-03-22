// File: PopupMenuAdapter.java - last edit:
// Yoshiki Shibata 27-Feb-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.awt;

import java.awt.Component;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PopupMenuAdapter implements MouseListener {
	private	Component 	fParentComponent;
	private PopupMenu	fPopupMenu;

	public PopupMenuAdapter (
		Component	parent,
		PopupMenu	popupMenu)
  	{
	fParentComponent	= parent;
	fPopupMenu			= popupMenu;

	parent.add(popupMenu);
	}

	public void mouseClicked(MouseEvent e) 	{ checkPopup(e); }
	public void mousePressed(MouseEvent e) 	{ checkPopup(e); }
	public void mouseReleased(MouseEvent e)	{ checkPopup(e); }
	public void mouseEntered(MouseEvent e)	{}
	public void mouseExited(MouseEvent e)	{}

	private void checkPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (e.getComponent() == fParentComponent) {
				fPopupMenu.show(fParentComponent, e.getX(), e.getY());
			}
		}
	}
}

// LOG
// 2.15 : 27-Feb-99	Y.Shibata	created


