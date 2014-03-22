// File: PrintableComponent.java - last edit:
// Yoshiki Shibata 10-Mar-00

// Copyright (c) 2000 by Yoshiki Shibata. All rights reserved.

package msgtool.print;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.swing.JComponent;

public class PrintableComponent implements Printable {

	public PrintableComponent(Component c) {
		fComponent = c;
		fSize = c.getPreferredSize();
	}

	// ===================
	// Printable interface 
	// ===================
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
   		Graphics2D g2d = (Graphics2D) g;

		if (!translatePageIndex(g2d, pageFormat, pageIndex))
			return NO_SUCH_PAGE;

		disableDoubleBuffering();
		fComponent.print(g2d);
		restoreDoubleBuffering();
		return PAGE_EXISTS;
	}

	// =================
	// private methods 
	// =================
	private void disableDoubleBuffering() {
		if (!isJComponent())
			return;

		JComponent jComponent = (JComponent) fComponent;
		fDoubleBuffered = jComponent.isDoubleBuffered();
		jComponent.setDoubleBuffered(false);	
	}

	private void restoreDoubleBuffering() {
		if (!isJComponent())
			return;

	  	((JComponent) fComponent).setDoubleBuffered(fDoubleBuffered);
	}

	private boolean isJComponent() {
		return (fComponent instanceof JComponent);
	}

	private boolean translatePageIndex(Graphics2D g2d, PageFormat pageFormat, int pageIndex) {
		System.out.println("pageFormat = " + pageFormat);
		System.out.println("size = " + fSize);
		System.out.println("pageIndex = " + pageIndex);
		double	yPosition = pageIndex * pageFormat.getImageableHeight();
		if (yPosition > fSize.height)
			return false; // no page exists
   		g2d.translate(pageFormat.getImageableX(), yPosition + pageFormat.getImageableY());
		return true;
	}

	private final Component	fComponent;
	private final Dimension	fSize;
	private boolean 		fDoubleBuffered;
}

// LOG
// 2.44 : 10-Mar-99	Y.Shibata	created.