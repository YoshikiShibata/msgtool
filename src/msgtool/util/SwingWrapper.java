// File: SwingWrapper.java - last edit:
// Yoshiki Shibata 21-Nov-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.util;

import javax.swing.SwingUtilities;

public class SwingWrapper {

	public static void setSwingMode(boolean swingMode) {
		SwingWrapper.swingMode = swingMode;
	}

	public static void invokeLater(Runnable runnable) {
		if (swingMode)
			SwingUtilities.invokeLater(runnable);
	 	else
			runnable.run();
	}

	private static boolean swingMode = false;
}

// LOG
// 2.36 : 21-Nov-99	Y.Shibata	created.