// File: FTPListenerImpl.java - last edit:
// Yoshiki Shibata 23-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import msgtool.protocol.FTP;
import msgtool.protocol.FTPListener;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

final class FTPListenerImpl implements FTPListener {
	
	FTPListenerImpl(Frame parentFrame) {
		fParentFrame	= parentFrame;
	}

    public void onFTP(
 		final String  senderIP, 
        final String  senderName, 
        final String  fileName,
        final String  fullpath,
        final int     socketNo,
        final long    fileLength) {
        //
        // Process this ftp request with a thread so that
        // further messages can be processed without waiting for
        // finishing of this ftp.
        //
        Thread ftpThread = new Thread() {
            public void run() {
				String  directory 	= null;
				String  file 		= null;

				synchronized (FTPListenerImpl.this) {
					if (fSaveDialog == null)
						fSaveDialog = new FileDialog(fParentFrame, 
                            "MessagingTool: " +  StringDefs.SAVE_FILE_AS, 
                            FileDialog.SAVE);
        
                	fSaveDialog.setFile(fileName);
					ComponentUtil.overlapComponents(fSaveDialog, fParentFrame, 32, false);
                	fSaveDialog.setVisible(true);
					fSaveDialog.dispose();
        
                	directory = fSaveDialog.getDirectory();
                	file      = fSaveDialog.getFile();
        
                	if (directory == null || file == null)
                    	return;
        
                	if (directory.length() == 0 || file.length() == 0)
                    	return;
				}
         
                FileOutputStream outStream = null;
                File    newlyCreatedFile = new File(directory, file);
                
                try {
                    outStream = new FileOutputStream(newlyCreatedFile);
                    if (FTP.getInstance().retrieve(senderIP, fullpath, file, outStream, socketNo, fileLength))
                        outStream.close();
                    else {
                        outStream.close();
                        newlyCreatedFile.delete();
                   	}
           		}
                catch (IOException e) {
                    System.out.println(e);
                    newlyCreatedFile.delete();
                    return;
               	}
         	}
      	};

        ftpThread.setDaemon(true);
        ftpThread.start();
    }
	private FileDialog	fSaveDialog		= null;
	private Frame		fParentFrame	= null;
}