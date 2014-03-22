// File: PrintableStyledTextArea.java - last edit:
// Yoshiki Shibata 12-Mar-00

// Copyright (c) 2000 by Yoshiki Shibata. All rights reserved.

package msgtool.swing.print;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.swing.JFrame;
import javax.swing.text.StyledDocument;

import msgtool.print.PrintableComponent;
import msgtool.swing.StyledTextArea;

public class PrintableStyledTextArea implements Printable {

	public PrintableStyledTextArea(StyledTextArea textArea) {
		fTextArea	= textArea;
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (fPrintable == null)
			createPrintable(pageFormat);
		if (fPrintable.print(g, pageFormat, pageIndex) == PAGE_EXISTS)
			return PAGE_EXISTS;
	 	else {
			fPrintable = null;
			fPrintFrame.dispose();
			fPrintFrame = null;
			return NO_SUCH_PAGE;
		}
	}

	private void createPrintable(PageFormat pageFormat) {
		System.out.println("imageableWidth = " + pageFormat.getImageableWidth());
		System.out.println("imageableHeight = " + pageFormat.getImageableHeight());
		JFrame	fPrintFrame = new JFrame("invisible frame");
    	StyledTextArea	textArea = cloneTextArea();
		// textArea.setSize((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight());
		// textArea.doLayout();
		fPrintFrame.getContentPane().add(textArea);
		// textArea.revalidate();
		// frame.pack();
		fPrintFrame.setSize((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight());
		// frame.getContentPane().doLayout();
		fPrintFrame.setVisible(true);
		fPrintable = new PrintableComponent(textArea.getTextPane());
	}

	private StyledTextArea cloneTextArea() {
		StyledDocument	styledDocument = fTextArea.getStyledDocument();
		StyledTextArea	styledTextArea = new StyledTextArea(styledDocument);
		styledTextArea.setBackground(Color.white);
		return styledTextArea;
	}

	private final StyledTextArea	fTextArea;
	private PrintableComponent		fPrintable = null;
	private JFrame					fPrintFrame;
}

// LOG
// 2.44 : 12-Mar-00	Y.Shibata	created.