// File: LogMeeting.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1999, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.log;

import java.util.Enumeration;
import java.util.Hashtable;

import msgtool.db.PropertiesDB;
import msgtool.protocol.MeetingProtocol;
import msgtool.protocol.MiscProtocol;

public class LogMeeting {

	public LogMeeting(
		String	internalRoomName,
		LogArea	logArea) {
		fLogArea		= logArea;
		fInternalRoomName	= internalRoomName;
   	}

	public void append(String	log) {
		fLogText.append(log);
	}

	public void clear() {
		fLogText.setLength(0);
	}

    public void acquireLog() {
        synchronized (fLogLengthTable) {
            String  ip          = null;
            String  logKey      = null;
            int     logLength   = 0;
            Integer lengthValue = null;
            
            fLogAcquired = false;
            MeetingProtocol.getInstance().logLengthRequest(fInternalRoomName);
            try {
                fLogLengthTable.wait(4 * 1000);   // 4 seconds
          	} catch (InterruptedException e) {}
            
            while (fLogLengthTable.size() > 0 && !fLogAcquired) {
                //
                // Find the longest log.
                //
                Enumeration<String> keys = fLogLengthTable.keys();
                ip 			= null;
                logKey 		= null;
                logLength 	= 0;
                
                while (keys.hasMoreElements()) {
                    logKey = keys.nextElement();
                    lengthValue = fLogLengthTable.get(logKey);
                    if (ip == null) {
                        ip = logKey;
                        logLength = lengthValue.intValue();
                    } else if (logLength < lengthValue.intValue()) {
                        ip = logKey;
                        logLength = lengthValue.intValue();
                    }
                } 
                //
                // Ask the log only if the logLength is greater than the length of
                // my log. In the near future, acquireLog() could be called anytime. [V1.75]
                //
                if (logLength > fLogText.length()) {
                    MiscProtocol.getInstance().logRequest(fInternalRoomName, ip);
                    try {
                        fLogLengthTable.wait(3 * 1000); // wait 3 seconds
                    } catch (InterruptedException e) {}

					if (fLogAcquired) {
						fLogArea.clear();
						fLogArea.appendText(fLogText.toString());
					}
                }
                
                fLogLengthTable.remove(ip);
            }
            fLogAcquired = true;
        }
    }
        
    public void onLogLengthRequest(String ip) {
        //
        // Do not answer if ip is my address
        // 
        if (ip.equals(PropertiesDB.getInstance().getMyIPAddress()))
            return;
        //
        // Send an answer only if the length of my log is not zero. Because
        // sending zero-length information doesn't make sense. Please note
        // that fLogText don't contain any messages on leaving and joinning, so
        // the length might be zero. [V1.75]
        //
        if (fLogText.length() > 0)
            MeetingProtocol.getInstance().logLengthAnswer(ip, fInternalRoomName, fLogText.length());
    }
        
    public void onLogLengthAnswer(String ip, int length) {
        synchronized (fLogLengthTable) {
			if (!fLogAcquired)
            	fLogLengthTable.put(ip, new Integer(length));
        }
    }
    
    public void onLogRequest(String ip) {
        MiscProtocol.getInstance().requestedLog(fInternalRoomName, ip, fLogText.toString());
    }
        
    public void onRequestedLog(String log) {
        synchronized (fLogLengthTable) {
            //
            // It's possible to receive several logs.
            // Pick the first one and discards others. 
            //
            if (fLogAcquired)
                return;
                
            fLogAcquired = true;
            fLogText.setLength(0);
            fLogText.append(log);
            fLogLengthTable.notifyAll();
        }
    }

	private Hashtable<String,Integer>			fLogLengthTable	= new Hashtable<String,Integer>();
	private boolean				fLogAcquired	= false;
	private StringBuilder		fLogText		= new StringBuilder();
	private final LogArea		fLogArea;
	private final String		fInternalRoomName;
}

// LOG
// 2.33 : 21-Aug-99	Y.Shibata	created. [Refactoring]
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
