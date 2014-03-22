// File: ShiftKeyAdapter.java - last edit:
// Yoshiki Shibata 27-Feb-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ShiftKeyAdapter implements KeyListener {
  	private boolean	fShiftKeyPressed = false;

	public ShiftKeyAdapter() {
	}

	public boolean isShiftKeyPressed() {
		return fShiftKeyPressed;
	}
	// =====================
	// KeyListener
	// =====================
	public void keyPressed(KeyEvent keyEvent) {
		int keyCode = keyEvent.getKeyCode();

		if (keyCode == KeyEvent.VK_SHIFT)
            fShiftKeyPressed = true;
	}

	public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SHIFT)
            fShiftKeyPressed = false;
   	}

	public void keyTyped(KeyEvent keyEvent) {}
}

// LOG
// 2.15 : 27-Feb-99	Y.Shibata	created

