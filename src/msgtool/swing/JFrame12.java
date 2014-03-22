// File: JFrame12.java - last edit:
// Yoshiki Shibata 13-Feb-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Frame;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class JFrame12 extends JFrame {
	private int state = Frame.NORMAL;

	public JFrame12(String	title) { super(title); }
   	public JFrame12() { super();}

	public void setState(int state) {
		this.state = state;
		try {
			super.setState(state);
		}
	 	catch (NoSuchMethodError e) {}
	}

	public int getState() {
		return(state);	
	}
}

// LOG
// 2.14 : 13-Feb-99	Y.Shibata	created
