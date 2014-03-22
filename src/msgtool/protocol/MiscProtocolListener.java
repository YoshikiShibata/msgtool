// Fiile: MiscProtocolListener.java - last edit:
// Yoshiki Shibata 23-Nov-99

// Copyright (c) 1997 - 1999 Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

public interface MiscProtocolListener {

    void    onProbe(String senderIP);   
    void    onOffLine(String senderIP);   
    void    onReplaceIPAddress(String oldIP, String newIP);
    void    onNotInOffice(String senderIP);
    void    onInOffice(String senderIP);
    void    onLookForUser(String senderIP, String userName);
    void    onUserMatched(String senderIP, String name);
    void    onLogRequest(String senderIP, String internalRoomName);
    void    onRequestedLog(String senderIP, String internalRoomName, String log);
    void    onCommandLineMessage(String senderIP, String senderName, String message);
    }
    
// LOG
//         5-Aug-97 Y.Shibata   created
//        25-Sep-97 Y.Shibata   added P2P_LookForUser & P2P_UserMatched
// 1.71 : 25-Oct-97 Y.Shibata   Protocol2Processor -> MiscProtocolListener
// 1.74 :  3-Nov-97 Y.Shibata   added onLogRequest & onRequestedLog
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
// 2.00 :  5-Aug-98 Y.Shibata   added onCommandLineMessage.
// 2.10 : 11-Oct-98 Y.Shibata   added onFTP
// 2.36 : 23-Nov-99	Y.Shibata	moved onFTP() to FTPListener interface 
