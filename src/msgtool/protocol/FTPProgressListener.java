// File: FTPProgressListener.java - last edit:
// Yoshiki Shibata 12-Oct-98

// Copyright (c) 1998 by Yoshiki Sibata. All rights reserved.

package msgtool.protocol;

public interface FTPProgressListener {

    public void     setFileName(String  fileName);
    public void     setPeerName(String  peerName);
    
    public void     onConnecting();
    public void     onConnected();
    public boolean  onBeingTransfered(long totalOfTransferedBytes);
    public void     onCompleted(long totalOfTransferedBytes);
    public void     onCanceled();
    }
    
// LOG
// 2.10 : 12-Oct-98 Y.Shibata   created
