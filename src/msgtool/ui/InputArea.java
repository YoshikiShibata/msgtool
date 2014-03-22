// File: InputArea.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.ui;

public interface InputArea 
{
	String	getText();
	void	clearText();
	void	requestFocus();
};

// LOG
// 2.35 : 24-Oct-99	Y.Shibata	created