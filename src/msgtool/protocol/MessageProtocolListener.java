// File: MessageProtocolListener.java - last edit:
// Yoshiki Shibata 12-Jul-98

// Copyright (c) 1997, 1998 by Yoshiki Shibata. All rights reserved.
                     
package msgtool.protocol;

public interface MessageProtocolListener {

    public void onMessage(Message   message);
    }
    
// LOG
// 1.71 - 25-Oct-97 Y.Shibata   created.
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
