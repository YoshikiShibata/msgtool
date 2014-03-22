// File: Stickies.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1999, 2000, 2003 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing.tools;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import msgtool.common.FontManager;
import msgtool.util.FileUtil;


public class Stickies implements FontManager.FontListener
{
	// There is only one instance of this class. The instance is used
	// to be notified when a user changes the font.
	private Stickies() {
		FontManager.getInstance().addFontListener(this);
	}

	// FontManager.FontListener interface implementation
	public void setFont(Font font) 		{ fFont = font; }
	public void fontChanged(Font font) 	{ setFontToAllNotes(font); }

	private static Font fFont = null;
	
	/*
	 * Note that fInstance is used to keep stickies. So there is not local
	 * reference to it.
	 */
	@SuppressWarnings("unused")
	private static Stickies fInstance = new Stickies();

	private static final String	kFileName  = "MessagingTool.stickies";
	private static final String kFileName2 = "MessagingTool.stickies2";

	static void addNote(StickyNote	note) 
	{
		fNotes.addElement(note);
		if (fFont != null)
			note.setFont(fFont);
	}

	static void removeNote(StickyNote note)
	{
		fNotes.removeElement(note);
	}

	public static void saveNotes() 
	{
		//
		// Save all sticky notes with Styled Documents
		// 
		try {
			FileOutputStream fos = new FileOutputStream(FileUtil.makeFullPathname(kFileName));
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			StickyNoteInfo[] noteInfos = new StickyNoteInfo[fNotes.size()];
			for (int i = 0; i < noteInfos.length; i++) {
				noteInfos[i] = ((StickyNote) fNotes.elementAt(i)).getNoteInfo();
			}
			oos.writeObject(noteInfos);
			oos.flush();
			fos.close();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}

		//
		// Save all sticky notes only with its text
		//
		try {
			FileOutputStream fos = new FileOutputStream(FileUtil.makeFullPathname(kFileName2));
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			StickyNoteInfo2[] noteInfos = new StickyNoteInfo2[fNotes.size()];
			for (int i = 0; i < noteInfos.length; i++) {
				noteInfos[i] = ((StickyNote) fNotes.elementAt(i)).getNoteInfo2();
			}
			oos.writeObject(noteInfos);
			oos.flush();
			fos.close();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}

	public static void loadNotes()
	{
		StickyNoteInfo[]	noteInfos = null;

		try {
			FileInputStream fis = new FileInputStream(FileUtil.makeFullPathname(kFileName));
			ObjectInputStream ois = new ObjectInputStream(fis);

			noteInfos = (StickyNoteInfo[]) ois.readObject();
			fis.close();
		} catch (FileNotFoundException e) {
			// do nothing.
	   	} catch (InvalidClassException e) {
			noteInfos = null;
		} catch (IOException e) {
			e.printStackTrace();
			noteInfos = null;
	  	} catch (ClassNotFoundException e) {
			e.printStackTrace();
			noteInfos = null;
		} catch (Exception e) {
			e.printStackTrace();
			noteInfos = null;
		}

		if (noteInfos != null) {
			for (int i = 0; i < noteInfos.length; i++) {
				StickyNote note = new StickyNote(
							noteInfos[i].color, 
							noteInfos[i].bounds, 
							noteInfos[i].styledDocument);
				note.setVisible(true);
			}
			return;
		}

		//
		// Loading notes with StyledDocuments failed. So load notes with text
		//
		StickyNoteInfo2[] noteInfo2s = null;
		
		try {
			FileInputStream fis = new FileInputStream(FileUtil.makeFullPathname(kFileName2));
			ObjectInputStream ois = new ObjectInputStream(fis);

			noteInfo2s = (StickyNoteInfo2[]) ois.readObject();
			fis.close();
		} catch (FileNotFoundException e) {
			return;		// give up
		} catch (Exception e) {
			e.printStackTrace();
			return;		// give up.
		}

		if (noteInfo2s != null) {
			for (int i = 0; i < noteInfo2s.length; i++) {
				StickyNote note = new StickyNote(
							noteInfo2s[i].color,
							noteInfo2s[i].bounds,
							null);
			   	note.setText(noteInfo2s[i].text);
			   	note.setVisible(true);
			}
		}
		
	}

	public static void toFrontAllNotes() 
	{
		int	noOfNotes = fNotes.size();

		for (int i = 0; i < noOfNotes; i++) {
			((StickyNote) fNotes.elementAt(i)).toFront();
		}
	}

	private static void setFontToAllNotes(Font font) {
		int	noOfNotes = fNotes.size();

		for (int i = 0; i < noOfNotes; i++) {
			((StickyNote) fNotes.elementAt(i)).setFont(font);
		}
	}

    public static void toBackAllNotes() {
		int noOfNotes = fNotes.size();

		for (int i = 0; i < noOfNotes; i++) {
			((StickyNote) fNotes.elementAt(i)).toBack();
		}
	}


	public static void main(String[] args) {
		loadNotes();
		if (fNotes.size() == 0) {
			StickyNote	note = new StickyNote();
			note.setVisible(true);
		}
	}

 	static private Vector<StickyNote>	fNotes = new Vector<StickyNote>();
}
// LOG
// 2.15 :  7-Mar-99	Y.Shibata	created
// 2.19 : 27-Mar-99	Y.Shibata	added toFrontAllNotes:
// 2.24 :  9-May-99	Y.Shibata	added code to recover from error as much as possible.
// 2.43 :  5-Mar-99	Y.Shibata	added code to set font when a user changes fonts.
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
// 2.51 : 18-Jan-04 Y.Shibata   added toBackAllNotes()
