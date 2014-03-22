// File: Message.java - last edit:
// Yoshiki Shibata 12-Jul-98    

// Copyright (c) 1997, 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

public final class Message {

    private String  fSenderIP;
    private String  fSenderName;
    private String  fMessage;
    private boolean fIsBroadcast;
    private boolean fIsCommandLineMessage;
    
    public Message(
        String  senderIP,
        String  senderName,
        String  message,
        boolean broadcast) {
        fSenderIP               = senderIP;
        fSenderName             = senderName;
        fMessage                = message;
        fIsBroadcast            = broadcast;
        fIsCommandLineMessage   = false;
        }
        
    public void setSenderIP(String senderIP) {
        fSenderIP = senderIP;
        }
    
    public String getSenderIP() {
        return(fSenderIP);
        }

    public void setSenderName(String senderName) {
        fSenderName = senderName;
        }
    
    public String getSenderName() {
        return(fSenderName);
        }

    public void setMessage(String message) {
        fMessage = message;
        }
    
    public String getMessage() {
        return(fMessage);
        }
        
    public void setBroadcast(boolean broadcast) {
        fIsBroadcast = broadcast;
        }
        
    public boolean isBroadcast() {
        return(fIsBroadcast);
        }
    
    public void setCommandLineMessage(boolean commandLine) {
        fIsCommandLineMessage   = commandLine;
        }
            
    public boolean isCommandLineMessage() {
        return(fIsCommandLineMessage);
        }
    }
    
// LOG
// 1.71 : 25-Oct-97 Y.Shibata   created.
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
// 2.00 :  5-Aug-98 Y.Shibata   added fIsCommandLineMessage


