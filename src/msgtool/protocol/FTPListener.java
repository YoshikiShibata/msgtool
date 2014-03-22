// File: FTPListener.java - last edit:
// Yoshiki Shibata 23-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

public interface FTPListener {

	void onFTP(String senderIP, String senderName, String fileName, String fullpath, int socketNo, long fileLength);
}

// LOG
// 2.36 : 23-Nov-99	Y.Shibata	created.