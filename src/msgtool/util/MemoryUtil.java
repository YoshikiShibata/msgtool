// File: MemoryUtil.java - last edit:
// Yoshiki Shibata 21-Jul-98

// Copyright (c) 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.util;

public final class MemoryUtil {

    private static int kMaxTryCount = 3;
    
    public static void fullGC() {
        Runtime rt = Runtime.getRuntime();
        long    isFree = rt.freeMemory();
        long    wasFree;
        
        for (int i = 0; i < kMaxTryCount; i++) {
            wasFree = isFree;
            rt.runFinalization();
            rt.gc();
            isFree = rt.freeMemory();
            if (isFree <= wasFree) 
                return;
		}
	}
}
    
// LOG
// 1.95 : 21-Jul-98 Y.Shibata   created.
