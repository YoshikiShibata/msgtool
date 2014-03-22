// File: StickyNoteInfo2.java - last edit:
// Yoshiki Shibata 16-May-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing.tools;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;

class StickyNoteInfo2 implements Serializable {

	static final long serialVersionUID = 4453492917198049424L;

	StickyNoteInfo2 (
		Rectangle			bounds,
		Color				color,
		String				text,
		long				timer) 
  	{
	this.bounds 		= bounds;
	this.color			= color;
	this.text			= text;
	this.timer			= timer;
	}

	Rectangle 		bounds;
	Color			color;
	String 			text;
	long			timer;	
}

// LOG
// 2.24 :  9-May-99	Y.Shibata	created.
//		  16-May-99	Y.Shibata	added serialVersionUID
