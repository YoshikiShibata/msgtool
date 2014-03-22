// File: PrintContext.java - last edit:
// Yoshiki Shibata 11-Mar-00

// Copyright (c) 2000 Yoshiki Shibata
// All rights reserved.

package msgtool.print;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

public class PrintContext {

	public PageFormat	getPageFormat() {
		if (fPageFormat == null)
			fPageFormat = PrinterJob.getPrinterJob().defaultPage();
	  	return fPageFormat;
	}

	public void setPageFormat(PageFormat pageFormat) {
		fPageFormat = pageFormat;
	}

	private	PageFormat	fPageFormat	= null;
}

// LOG
// 2.44 : 11-Mar-00	Y.Shibata	created.