// File: XJMenu.java - last edit:
// Yoshiki Shibata 22-Mar-99	

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class XJMenu extends JMenu {

	public XJMenu() 					{ super(); }
	public XJMenu(String s)				{ super(s); }
	public XJMenu(String s, boolean b) 	{ super(s, b); }

	public void setPopupMenuVisible(boolean b) {
   		if (!b) {
			super.setPopupMenuVisible(b);
			return;
			}
		boolean isVisible = isPopupMenuVisible();
		if (isVisible)
			return;
	 	
	   	if (fPopupMenu == null) {
			fPopupMenu = getPopupMenu();
			fPopupMenuSize = fPopupMenu.getPreferredSize();
			}

		if (isShowing()) {
			int	x = 0;
			int	y = 0;
			Dimension 	menuSize 		= getSize();
			Point		menuLocation 	= getLocationOnScreen();
			if (getParent() instanceof JPopupMenu) {
				if ((menuLocation.x + menuSize.width + fPopupMenuSize.width) > kScreenSize.width)
					x = -fPopupMenuSize.width;
			   	else
					x = menuSize.width;

			  	if ((menuLocation.y + fPopupMenuSize.height + kWindowsBarHeightOffset) > kScreenSize.height)
					y = kScreenSize.height - (menuLocation.y + fPopupMenuSize.height + kWindowsBarHeightOffset);
			  	else
					y = 0;
		 	} else {
				if ((menuLocation.x + fPopupMenuSize.width) > kScreenSize.width)
					x = kScreenSize.width - (menuLocation.x + fPopupMenuSize.width);
			  	else
					x = 0;

				y = menuSize.height;
		   	}

			fPopupMenu.show(this, x, y);
		}
	}

	private static final Dimension kScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int	kWindowsBarHeightOffset = 32;

	private JPopupMenu	fPopupMenu	= null;
	private Dimension 	fPopupMenuSize = null;
}

// LOG
// 2.17 : 22-Mar-99	Y.Shibata	created

