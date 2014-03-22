// File: InputTextArea.java -last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.awt;

import java.awt.TextArea;

import msgtool.ui.InputArea;

@SuppressWarnings("serial")
class InputTextArea extends TextArea implements InputArea
{
	InputTextArea(int rows, int columns, int scrollbars)
	{
		super("", rows, columns, scrollbars);
	}

	public void clearText()
	{
		setText("");
	}
}

// LOG
// 2.35 : 24-Oct-99	Y.Shibata	created.