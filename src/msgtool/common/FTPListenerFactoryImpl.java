// File: FTPListenerFactoryImpl.java - last edit:
// Yoshiki Shibata 7-Feb-99

// Copyright (c) 1998, 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.common;

import java.awt.Component;

import msgtool.protocol.FTPListenerFactory;
import msgtool.protocol.FTPProgressListener;

public class FTPListenerFactoryImpl implements FTPListenerFactory 
    {
	private Component	fBaseFrame = null;

	public FTPListenerFactoryImpl(Component baseFrame) {
		fBaseFrame = baseFrame;
	}
  	
    public FTPProgressListener  createProgressListener(int  type) {
        if (type == SEND)
            return(new FTPProgressFrame(FTPProgressFrame.SEND_MODE, fBaseFrame));
        else
            return(new FTPProgressFrame(FTPProgressFrame.RECEIVE_MODE, fBaseFrame));
  	}
}

// LOG
// 2.10 : 18-Oct-98 Y.Shibata   created.
// 2.12 :  7-Feb-98	Y.Shibata	added fBaseFrame   
