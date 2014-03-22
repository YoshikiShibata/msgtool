/*
 * File: ServerSocketUtil.java - last edit:
 * Yoshiki Shibata 28-Aug-2004
 * 
 * Copyright (c) 2004 by Yoshiki Shibata
 */
package msgtool.protocol;

import java.io.IOException;
import java.net.ServerSocket;

import msgtool.util.TimerUtil;

public class ServerSocketUtil {

	// =====================
	// Server implementation
	// =====================
	static public ServerSocket createServerSocket(int socketNo) {
		ServerSocket serverSocket;
		int retryCount = 0;

		// When a user quits the MessagingTool, the tool makes all windows
		// invisible and sends Offline commands to all on-line recipients. 
		// In other words, if the user tries to invoke the tool again before 
		// the previous tool is still running, creating server socket might 
		// fail. So try 6 times.
		while (retryCount < 6) {
			try {
				serverSocket = new ServerSocket(socketNo);
			} catch (IOException e) {
				System.out.println("Could not listen on port: " + MessageProtocol.kSocketNo
						+ ", " + e);
				serverSocket = null;
			}
			if (serverSocket != null)
				return (serverSocket);
			//
			// Now creating ServerSocket failed. Sleep 1 seconds and try again.
			//
			retryCount++;
			TimerUtil.sleep(1000);
		}
		return (null);
	}
}

// LOG
// 2.52 : 28-Aug-04	Y.Shibata	movoed from MessageProtocol.
