// File: Frame12.java - last edit:
// Yoshiki Shibata 13-Feb-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Frame;

@SuppressWarnings("serial")
public class Frame12 extends Frame {
	private int state = Frame.NORMAL;

	public Frame12(String	title) { super(title); }
   	public Frame12() { super();}

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
