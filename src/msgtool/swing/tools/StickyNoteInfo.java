// File: StickyNoteInfo.java - last edit:
// Yoshiki Shibata 16-May-99

// Copyright (c) 1999 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing.tools;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.text.StyledDocument;

class StickyNoteInfo implements Serializable {

	static final long serialVersionUID = 8592035951483995892L;

	StickyNoteInfo(
		Rectangle			bounds,
		Color				color,
		StyledDocument		styledDocument,
		long				timer) 
  	{
	this.bounds 		= bounds;
	this.color			= color;
	this.styledDocument	= styledDocument;
	this.timer			= timer;
	}

	Rectangle 		bounds;
	Color			color;
	StyledDocument 	styledDocument;
	long			timer;	
}

// LOG
// 2.24 : 16-May-99	Y.Shibata	added serialVersionUID
