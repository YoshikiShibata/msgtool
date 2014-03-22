// File: FTPListenerImpl.java - last edit:
// Yoshiki Shibata 23-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import msgtool.protocol.FTP;
import msgtool.protocol.FTPListener;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

final class FTPListenerImpl implements FTPListener {

	FTPListenerImpl(Frame parentFrame) {
		fParentFrame = parentFrame;
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
				File 	selectedFile	= null;

				synchronized (FTPListenerImpl.this) {
					if (fFileChooser == null) {
						fFileChooser = new JFileChooser(".");
						fFileChooser.setDialogTitle("MessagingTool: " +  StringDefs.SAVE_FILE_AS);
					}
				 	selectedFile = new File(fFileChooser.getCurrentDirectory(), fileName);
					ComponentUtil.overlapComponents(fFileChooser, fParentFrame, 32, false);
					while (true) {
						fFileChooser.setSelectedFile(selectedFile);
						int status = fFileChooser.showSaveDialog(fParentFrame);
						if (status != JFileChooser.APPROVE_OPTION)
 				    		return;

				 		selectedFile = fFileChooser.getSelectedFile();
						if (!selectedFile.exists()) 
							break;
						else {
							Object[] options = { StringDefs.YES, StringDefs.NO };

							fParentFrame.getToolkit().beep();
							int choice = JOptionPane.showOptionDialog(fParentFrame,
								selectedFile + StringDefs.FILE_EXISTS,
								"",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE,
								null, options, options[0]);
					  		if (choice == JOptionPane.YES_OPTION)
								break;
					  	}
					}
				}
         
                FileOutputStream outStream = null;

                try {
                    outStream = new FileOutputStream(selectedFile);
                    if (FTP.getInstance().retrieve(senderIP, fullpath, fileName, outStream, socketNo, fileLength))
                        outStream.close();
                    else {
                        outStream.close();
                        selectedFile.delete();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    selectedFile.delete();
                    return;
                }
            }
        };

        ftpThread.setDaemon(true);
        ftpThread.start();
    }

	private JFileChooser	fFileChooser	= null;
	private Frame			fParentFrame	= null;
}

// LOG
// 2.36 : 23-Nov-99	Y.Shibata	created.