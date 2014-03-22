// File: PageSetupAction.java - last edit:
// Yoshiki Shibata 11-Mar-00

// Copyright (c) 2000 Yoshiki Shibata
// All rights reserved.

package msgtool.print;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class PageSetupAction extends AbstractAction {

	public PageSetupAction(PrintContext printContext) {
		super("Page setup ...");
		fPrintContext = printContext;
	}

	public void actionPerformed(ActionEvent e) {
		PrinterJob	printerJob = PrinterJob.getPrinterJob();

		fPrintContext.setPageFormat(printerJob.pageDialog(fPrintContext.getPageFormat()));
	}

	private final PrintContext	fPrintContext;
}

// LOG
// 2.44 : 11-Mar-00	Y.Shibata	created.