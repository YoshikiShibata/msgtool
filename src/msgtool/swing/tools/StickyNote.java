// File: StickyNote.java - last edit:
// Yoshiki Shibata 15-Nov-03

// Copyright (c) 1999, 2000, 2003 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JMenuItem;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.text.StyledDocument;

import msgtool.swing.StyledTextArea;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class StickyNote extends JWindow {

	public StickyNote() {
		this(Color.yellow, new Rectangle(100, 100, 100, 70));
	}

	public StickyNote(String note) {
	 	this();
		fTextArea.setText(note);
	}

	public StickyNote(Color color) {
		this(color, new Rectangle(100, 100, 100, 70));
	}

	public StickyNote(Color color, Rectangle bounds) {
		this(color, bounds, null);
	}

	public StickyNote(Color color, Rectangle bounds, StyledDocument styledDocument)	{
		fContentPane = getContentPane();
		GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();

		fContentPane.setLayout(gridBag);

		if (styledDocument == null)
			fTextArea = StyledTextArea.createStyledTextArea(false);
	  	else
			fTextArea = new StyledTextArea(styledDocument, false);
		fTextArea.setBorder(new StickyBorder());
		constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        gridBag.setConstraints(fTextArea, constraints);
        fContentPane.add(fTextArea);
		fTextBackgroundColor = color;
		fTextArea.setBackground(fTextBackgroundColor);
		setBounds(bounds);

		fTextArea.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) { processMouseDragged(e); }
		});
		
		fTextArea.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { processMousePressed(e); }
			public void mouseReleased(MouseEvent e) { processMouseReleased(e); }
		});

		fTextArea.addMouseShortCutMenuSeparator();
		createEditMenu();
		
		fTextArea.addMouseShortCutMenuSeparator();
		createFileMenus();

		fTextArea.addMouseShortCutMenuSeparator();
		fTextArea.addMouseShortCutMenu(fQuitMenuItem = new JMenuItem(StringDefs.DELETE_THIS_NOTE));
		fQuitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Stickies.removeNote(StickyNote.this);
			}
		});

		Stickies.addNote(this);
	}

	public void setText(String note) {
		fTextArea.setText(note);
	}

	public void setFont(Font font) {
		if (fTextArea != null)
			fTextArea.setFont(font);
	  	super.setFont(font);
	}

	public void update(Graphics g) {
		super.paint(g);
	}
	
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (fCurrentBounds != null) {
			fCurrentBounds.width = width;
			fCurrentBounds.height = height;
		}
	}
	
	void setTextBackground(Color backgroundColor) {
		fTextBackgroundColor = backgroundColor;
		fTextArea.setBackground(backgroundColor);
		repaint();
	}

	StickyNoteInfo	getNoteInfo() {
		StickyNoteInfo	note = new StickyNoteInfo(
				getBounds(), fTextBackgroundColor, fTextArea.getStyledDocument(), 0);
	  	return(note);
	}

	StickyNoteInfo2 getNoteInfo2() {
		StickyNoteInfo2	note = new StickyNoteInfo2(
				getBounds(), fTextBackgroundColor, fTextArea.getText(), 0);
	 	return(note);
	}
	
	private void createEditMenu() {
		JMenuItem editMenu = new JMenuItem(StringDefs.EDIT_PPP);
		editMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StickyNoteEditor editor = new StickyNoteEditor(getBounds(), 
					fTextBackgroundColor,
					fTextArea.getStyledDocument(),
					StickyNote.this);
				editor.setVisible(true);
                StickyNote.this.toBack();
			}
		});
		fTextArea.addMouseShortCutMenu(editMenu);
	}

	private void processMouseDragged(MouseEvent e) {
		if (fStartPointX == -1)
			return;
			
		int	newX = e.getX();
		int newY = e.getY();
		if (fCurrentBounds == null)
			fCurrentBounds = getBounds();

		fCurrentBounds.x += newX - fStartPointX;
		fCurrentBounds.y += newY - fStartPointY;
		setBounds(fCurrentBounds);
	}
	
	private void processMousePressed(MouseEvent e) {
		if (e.isPopupTrigger())
			return;
			
		fStartPointX = e.getX();
		fStartPointY = e.getY();
		if (fCurrentBounds == null)
			fCurrentBounds = getBounds();
	}
	
	private void processMouseReleased(MouseEvent e) {
		fStartPointX = -1;
		fStartPointY = -1;
	}


	private class StickyBorder implements Border {
		public StickyBorder() {
		}

		public void paintBorder(
			Component c, Graphics g,
			int x, int y, int width,  int height) {
			// do nothing
		}
		
		public Insets getBorderInsets(Component component) {
			return (new Insets(0, 0, 0, 0));
		}

		public boolean isBorderOpaque() {
			return (false);
		}
	}
	
	private void createFileMenus() {
		createMenuItem(StringDefs.NEW_STICKY_NOTE_PPP, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StickyNote note = new StickyNote(fTextBackgroundColor);
				ComponentUtil.alignComponents(note, StickyNote.this);
				ComponentUtil.alignComponentsBaseHorizontal(note, StickyNote.this);
				note.setVisible(true);
			}
		});
		createMenuItem(StringDefs.SAVE_ALL_STICKY_NOTES, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Stickies.saveNotes();
			}
		});
		createMenuItem(StringDefs.FRONT_ALL_NOTES, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Stickies.toFrontAllNotes();
			}
		});
        createMenuItem(StringDefs.BACK_ALL_NOTES, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Stickies.toBackAllNotes();
            }
        });
	}
	
	private void createMenuItem(String title, ActionListener listener) {
		JMenuItem	menuItem = new JMenuItem(title);

		menuItem.addActionListener(listener);
		fTextArea.addMouseShortCutMenu(menuItem);
	}
	
	private Container		fContentPane 	= null;
	private StyledTextArea	fTextArea		= null;

	private int			fStartPointX 		= 0;
	private int 		fStartPointY 		= 0;
	private	Rectangle	fCurrentBounds 		= null;

	private JMenuItem	fQuitMenuItem		= null;
	private Color		fTextBackgroundColor = null;
	
}

// LOG
// 2.15 : 28-Feb-99	Y.Shibata	created
// 2.17 : 21-Mar-99	Y.Shibata	added "Other Colors ..."
//		  22-Mar-99	Y.Shibata	used XJMenu instead of JMenu directly.
// 2.21 : 10-Apr-99	Y.Shibata	added "New Sticky Note" and "Save" menus
// 2.22 : 17-Apr-99	Y.Shibata	added "Front All Notes" menu.
//								added "Insert" menu
// 2.23 : 22-Apr-99	Y.Shibata	fixed a bug in twoDigits.
// 2.31 : 31-May-99	Y.Shibata	inherit the background color when a new note is created.
// 2.32 :  5-Jun-99	Y.Shibata	added code to call ComponentUtil.alignComponentsBaseHorizontal()
// 2.42 : 28-Feb-00	Y.Shibata	Now focus is handled correctly, and loosing focus automaticaly saves notes.
// 2.43 :  5-Mar-00	Y.Shibata	added setFont()
// 2.50 : 15-Nov-03 Y.Shibata	moved most code to StickyNoteEditor.
// 2.51 :  1-Feb-04 Y.Shibata   added toBackAllNotes
