// File: NetUtil.java - last edit:
// Yoshiki Shibata 8-Nov-03

// Copyright (c) 1998, 2003 by Yoshiki Shibata

package msgtool.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class NetUtil {
    // ========================
    // Get IP address as string
    // ========================
    static private String   myIPAddress = null;
    
	static public String getMyIPAddress() {
		if (myIPAddress != null)
			return myIPAddress;
        
		try {
			for (Enumeration<NetworkInterface> i =  NetworkInterface.getNetworkInterfaces(); i.hasMoreElements(); ) {
				NetworkInterface ni = i.nextElement();
				for (Enumeration<InetAddress> j = ni.getInetAddresses(); j.hasMoreElements();) {
					InetAddress ia = j.nextElement();	
					if (ia.getHostAddress().equals("127.0.0.1"))
						continue;
					myIPAddress = ia.getHostAddress();
					return myIPAddress;
				}
			}
		} catch (SocketException e) {
			// do nothing;
		}
		return null;
	}
        
    static public String getIPAddress(InetAddress inetAdd) {
        byte        rawIPAddress[] = inetAdd.getAddress();

        return (rawIPAddress[0] & 0xff) + "." +
               (rawIPAddress[1] & 0xff) + "." +
               (rawIPAddress[2] & 0xff) + "." +
               (rawIPAddress[3] & 0xff);
	}
}
    
// LOG
// 1.95 : 12-Jul-98 Y.Shibata   created from the old Util.java
// 2.50 :  8-Nov-03	Y.Shibata	modified for SDK 1.4
