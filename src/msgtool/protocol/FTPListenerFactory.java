// File: FTPListenerFactory.java - last edit:
// Yoshiki Shibata 18-Oct-1998

// Copyright (c) 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

public interface FTPListenerFactory {
    
    int     SEND    = 0;
    int     RECEIVE = 1;
    
    public FTPProgressListener  createProgressListener(int  type);
    
    }
    
// LOG
// 2.10 : 18-Oct-98 Y.Shibata   created.
