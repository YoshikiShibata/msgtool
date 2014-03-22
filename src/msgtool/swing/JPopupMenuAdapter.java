// File: JPopupMenuAdapter.java - last edit:
// Yoshiki Shibata 22-Mar-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import msgtool.util.ComponentUtil;

public class JPopupMenuAdapter implements MouseListener {
	private	Container 	fParentContainer;
	private JPopupMenu	fPopupMenu;
	private Point		fLocation = null;

	public JPopupMenuAdapter (
		Container	parent,
		JPopupMenu	popupMenu)
  	{
	fParentContainer	= parent;
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
			if (e.getComponent() == fParentContainer) {
				if (fLocation == null) 
					fLocation = new Point(e.getX(), e.getY());
			   	else {
					fLocation.x = e.getX();
					fLocation.y = e.getY();
					}
				Point location = ComponentUtil.fitPopupMenuInsideScreen(
												fParentContainer,
												fPopupMenu,
												fLocation);
				fPopupMenu.show(fParentContainer, location.x, location.y);
			}
		}
	}
}

// LOG
// 2.15 : 27-Feb-99	Y.Shibata	created
// 2.17 : 22-Mar-99	Y.Shibata	re-implemented b using ComponentUtil.fitPopupMenuInsideScreen


