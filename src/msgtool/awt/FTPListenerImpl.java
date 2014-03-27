/*
 * File: FTPListenerImpl.java - last edit:
 * Yoshiki Shibata 27-Mar-2014
 *
 * Copyright (c) 1999, 2014 by Yoshiki Shibata. All rights reserved.
 */
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
    private FileDialog fSaveDialog = null;
    private final Frame fParentFrame;
    
    FTPListenerImpl(Frame parentFrame) {
        fParentFrame = parentFrame;
    }

    @Override
    public void onFTP(
            final String senderIP,
            final String senderName,
            final String fileName,
            final String fullpath,
            final int socketNo,
            final long fileLength) {
        //
        // Process this ftp request with a thread so that
        // further messages can be processed without waiting for
        // finishing of this ftp.
        //
        Thread ftpThread = new Thread() {
            @Override
            public void run() {
                String directory;
                String file;

                synchronized (FTPListenerImpl.this) {
                    if (fSaveDialog == null) {
                        fSaveDialog = new FileDialog(fParentFrame,
                                "MessagingTool: " + StringDefs.SAVE_FILE_AS,
                                FileDialog.SAVE);
                    }

                    fSaveDialog.setFile(fileName);
                    ComponentUtil.overlapComponents(fSaveDialog, fParentFrame, 32, false);
                    fSaveDialog.setVisible(true);
                    fSaveDialog.dispose();

                    directory = fSaveDialog.getDirectory();
                    file = fSaveDialog.getFile();

                    if (directory == null || file == null) {
                        return;
                    }

                    if (directory.length() == 0 || file.length() == 0) {
                        return;
                    }
                }

                File newlyCreatedFile = new File(directory, file);

                try (FileOutputStream os = new FileOutputStream(newlyCreatedFile);) {
                    if (!FTP.getInstance().retrieve(senderIP, fullpath, file, os, socketNo, fileLength)) {
                        newlyCreatedFile.delete();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    newlyCreatedFile.delete();
                }
            }
        };

        ftpThread.setDaemon(true);
        ftpThread.start();
    }
}
// LOG
// 2.60 : 27-Mar-14 Y.Shibata   refactored with Java8