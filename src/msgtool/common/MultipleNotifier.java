// File: MultipleNotifier.java - last edit:
// Yoshiki Shibata 12-Jul-98

// Copyright (c) 1997, 1998 Yoshiki Shibata. All rights reserved.

package msgtool.common;

import msgtool.protocol.MiscProtocol;

public final class MultipleNotifier extends Thread {

    private String[]    fOnlines    = null;
    private int         fIndex      = 0;
    private String      fSenderName = null;
    private int         fCommand    = 0;
    
    public final static int kOffLine        = 1;
    public final static int kNotInOffice    = 2;
    public final static int kInOffice       = 3;

    public MultipleNotifier(
        String      senderName,
        String []   onlines,
        int         index,
        int         command) {
        
        fSenderName = senderName;
        fOnlines    = onlines;
        fIndex      = index;
        fCommand    = command;
	}


    public void run() {
        if (fIndex < (fOnlines.length - 1)) {
            Thread  nextThread = new MultipleNotifier(fSenderName, fOnlines, fIndex + 1, fCommand);
            
            nextThread.start();
		}
        switch (fCommand) {
            case kOffLine:    
                MiscProtocol.getInstance().offLine(fSenderName, fOnlines[fIndex]);
                break;
            case kNotInOffice:
                MiscProtocol.getInstance().notInOffice(fSenderName, fOnlines[fIndex]);
                break;
            case kInOffice:
                MiscProtocol.getInstance().inOffice(fSenderName, fOnlines[fIndex]);
                break;
		}
	}
}
    
// LOG
// 1.52:  2-Sep-97  Y.Shibata   created from OfflineNotifier.java
// 1.95: 12-Jul-98  Y.Shibata   moved to msgtool.common
