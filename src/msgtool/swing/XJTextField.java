// File: XJTextField.java - last edit:
// Yoshiki Shibata  9-May-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved

package msgtool.swing;

import javax.swing.JTextField;

import msgtool.dnd.StringDropTarget;

@SuppressWarnings("serial")
public class XJTextField extends JTextField {
	
	public XJTextField(int size) {
		super(size);

		try {
			fDropTarget = new StringDropTarget(
					this, "appendText", new Class[]{String.class});
	   	} catch (NoClassDefFoundError e) {}
	}

	public void appendText(String text) {
		if (text == null)
			return;

		String	s = getText();
		if (s.length() == 0) {
			setText(text);
		} else 
			setText(s + ", " + text);
	}

	@SuppressWarnings("unused")
	private Object fDropTarget = null;
}

// LOG
// 2.24 :  9-May-99	Y.Shibata	created for Drag and Drop
