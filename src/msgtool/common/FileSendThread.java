// File: FileSendThread.java - last edit:
// Yoshiki Shibata  1-Dec-2007

// Copyright (c) 1999, 2007 by Yoshiki Shibata. All rights reserved.

package msgtool.common;

import java.io.File;

import msgtool.db.PropertiesDB;
import msgtool.protocol.FTP;
import msgtool.protocol.MiscProtocol;

public final class FileSendThread extends Thread {

	public FileSendThread(File file, String[] recipients) {
		fFile 		= file;
		fRecipients = recipients;
	}

	public void run() {      
  		String      fileName  	= fFile.getName();
        String      fullpath    = fFile.getAbsolutePath();
        long        fileLength  = fFile.length();
        
        for (String recipient: fRecipients) {
            MiscProtocol.getInstance().ftp(
				PropertiesDB.getInstance().getUserName(),
				recipient,
                fileName,
                fullpath,
                fileLength,
                FTP.getSocketNo());
     	}
	}

	private final File		fFile;
	private final String[]	fRecipients;
}

// LOG
// 2.36 : 23-Nov-99	Y.Shibata	created.
//  1-Dec-2007	Y.Shibata	refactored run() method