// File: PrintAction.java - last edit:
// Yoshiki Shibata 10-Mar-00

// Copyright (c) 2000 by Yoshiki Shibata. All rights reserved.

package msgtool.print;

import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class PrintAction extends AbstractAction {

	public PrintAction(Printable printable, PrintContext printContext) {
		super("Print ...");
    	fPrintable 		= printable;
		fPrintContext 	= printContext;
	}

	public void actionPerformed(ActionEvent event) {
		PrinterJob	printerJob = PrinterJob.getPrinterJob();
		PageFormat	pageFormat = fPrintContext.getPageFormat();

		if (printerJob.printDialog()) {
			printerJob.setPrintable(fPrintable, pageFormat);
			try {
				printerJob.print();
		   	} catch (PrinterException e) {
				e.printStackTrace();
			}
	  	}
	}

	private final Printable	fPrintable;
	private final PrintContext	fPrintContext;
}

// LOG
// 2.44 : 10-Mar-99	Y.Shibata	created.
//		  11-Mar-99	Y.Shibata	modified to use PrintContext