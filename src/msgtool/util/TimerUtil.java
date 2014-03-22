// File: TimerUtil.java - last edit:
// Yoshiki Shibata 11-Jul-98

// Copyright (c) 1998 by Yoshiki Shibata

// This file is common for all versions: AWT and Swing.

package msgtool.util;

public class TimerUtil {

    public static void sleep(int    mseconds) {
        Object  obj = new Object();
        
        synchronized (obj) {
            try {
                obj.wait(mseconds);
       		} catch (InterruptedException e) {
				// do-nothing
			}
		}
        obj = null;
	}
}

// LOG
// 1.95: 11-Jul-98  Y.Shibata   created
