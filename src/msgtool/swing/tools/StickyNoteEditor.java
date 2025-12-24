// File: StickyNote.java - last edit:
// Yoshiki Shibata 24-Dec-2025

// Copyright (c) 2003, 2025 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing.tools;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import msgtool.common.FontManager;
import msgtool.common.SortUtil;
import msgtool.swing.ColoredSquare;
import msgtool.swing.StyledTextArea;
import msgtool.swing.XJMenu;
import msgtool.util.ColorMap;
import msgtool.util.StringDefs;

/**
 * @author Yoshiki
 *
 */
@SuppressWarnings("serial")
class StickyNoteEditor extends JFrame {

	StickyNoteEditor(Rectangle bounds,
					 Color textBackgroundColor,
					 StyledDocument styledDocument,
					 StickyNote stickyNote) {
		super("Sticky Note Editor");
		
		fStickyNote = stickyNote;
	
		fContentPane = getContentPane();
		GridBagLayout       gridBag     = new GridBagLayout();
		GridBagConstraints  constraints = new GridBagConstraints();

		fContentPane.setLayout(gridBag);
	
		fTextArea = new StyledTextArea(styledDocument);
	
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		gridBag.setConstraints(fTextArea, constraints);
		fContentPane.add(fTextArea);
		fTextBackgroundColor = textBackgroundColor;
		fTextArea.setBackground(fTextBackgroundColor);
		setBounds(bounds);
		// save the original width & height to adjust the size of this JFrame.
		fOriginalWidth = bounds.width;
		fOriginalHeight = bounds.height;
		
		fTextArea.addMouseShortCutMenuSeparator();
		createColorMenus();
		createFontFamilyMenus();
		createFontSizeMenus();
		createFontStyleMenus();

		fTextArea.addMouseShortCutMenuSeparator();
		createJustifyMenus();

		fTextArea.addMouseShortCutMenuSeparator();
		insertMenus();
		
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Dimension size = getSize();
				Insets insets = getInsets();
				fStickyNote.setSize(
					size.width - (insets.left +insets.right),
					size.height - (insets.top + insets.bottom));
				fStickyNote.invalidate();
				fStickyNote.validate();
				fStickyNote.repaint();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				Stickies.saveNotes();
			}
		});
	}
	
	public void addNotify() {
		super.addNotify();
		Insets insets = getInsets();
		setSize(insets.left + insets.right + fOriginalWidth,
				insets.top + insets.bottom + fOriginalHeight);
		
	}
	
	private void createColorMenus() {
		String[] colorNames = ColorMap.getColorNames();
		//
		// Background Color
		//
		fBackgroundColorMenu = new XJMenu(StringDefs.TEXT_BACKGROUND_C);
		fTextColorMenu = new XJMenu(StringDefs.TEXT_COLOR);

		for (int i = 0; i < colorNames.length; i++) {
			Color			color = ColorMap.getColorByName(colorNames[i]);
			ColoredSquare	coloredSquare = new ColoredSquare(color);

			JMenuItem	menuItem = new JMenuItem(colorNames[i], coloredSquare );

			menuItem.addActionListener(new BackgroundColorActionListener(color));
			fBackgroundColorMenu.add(menuItem);
			fFontManager.addComponent(menuItem);

			menuItem = new JMenuItem(colorNames[i], coloredSquare);
			menuItem.addActionListener(new TextColorActionListener(color));
			fTextColorMenu.add(menuItem);
			fFontManager.addComponent(menuItem);
		}

		// Add the color chooser to the last
		fBackgroundColorMenu.addSeparator();
		JMenuItem	otherColorsMenuItem = new JMenuItem(StringDefs.OTHER_COLORS_PPP);
		otherColorsMenuItem.addActionListener(new BackgroundColorActionListener());
		fBackgroundColorMenu.add(otherColorsMenuItem);
		fFontManager.addComponent(otherColorsMenuItem);

		fTextColorMenu.addSeparator();
		otherColorsMenuItem = new JMenuItem(StringDefs.OTHER_COLORS_PPP);
		otherColorsMenuItem.addActionListener(new TextColorActionListener());
		fTextColorMenu.add(otherColorsMenuItem);
		fFontManager.addComponent(otherColorsMenuItem);

		fTextArea.addMouseShortCutMenu(fBackgroundColorMenu);
		fTextArea.addMouseShortCutMenu(fTextColorMenu);
	}

	private void createFontSizeMenus() {
		String		prefix = "font-size-";
		String[]	actionNames = fTextArea.getActionNames(prefix);
		int			prefixLength = prefix.length();

		SortUtil.sortStringsByLength(actionNames);
		createMenus(StringDefs.FONT_SIZE, actionNames, prefixLength);
	}

	private void createFontFamilyMenus() {
		String		prefix	= "font-family-";
		String[]	actionNames = fTextArea.getActionNames(prefix);
		int 		prefixLength = prefix.length();

		createMenus(StringDefs.FONT, actionNames, prefixLength);
	}

	private void createJustifyMenus() {
		String[]	actionNames = { "left-justify", "center-justify", "right-justify" };
		String[]	menuNames 	= { StringDefs.JUSTIFY_LEFT, 
									StringDefs.JUSTIFY_CENTER, 
									StringDefs.JUSTIFY_RIGHT };

		createMenus(StringDefs.JUSTIFICATION, actionNames, menuNames);
	}

	private void createFontStyleMenus()	{
		String[]	actionNames	= { "font-bold", "font-italic", "font-underline" };
		String[]	menuNames 	= { StringDefs.BOLD, StringDefs.ITALIC, StringDefs.UNDERLINE };

		fFontStyleMenu = createMenus(StringDefs.FONT_STYLE, actionNames, menuNames);
		fFontStyleMenu.addSeparator();

		fClearAttributeSet = new SimpleAttributeSet();
		StyleConstants.setBold(fClearAttributeSet, false);
		StyleConstants.setItalic(fClearAttributeSet, false);
		StyleConstants.setUnderline(fClearAttributeSet, false);

		JMenuItem clearItem = new JMenuItem(StringDefs.CLEAR_STYLE);
		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fTextArea.restoreSelectionForMouseShortCutMenus();
				fTextArea.setCharacterAttributes(fClearAttributeSet, false);
				repaint();
			}
		});
		fFontStyleMenu.add(clearItem);
		fFontManager.addComponent(clearItem);
	}

	private String twoDigits(int value) {	
		if (value < 10)
			return("0" + value);
		else
			return(Integer.toString(value));
	}

	private void insertMenus() {
		JMenu		menu 		= new XJMenu(StringDefs.INSERT);
		JMenuItem	menuItem	= null;

		menuItem = new JMenuItem(StringDefs.TIME_FORMAT1);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Calendar calendar = Calendar.getInstance();
				fTextArea.restoreSelectionForMouseShortCutMenus();
				fTextArea.replaceSelection(
					twoDigits(calendar.get(Calendar.HOUR_OF_DAY)) + ':' +
					twoDigits(calendar.get(Calendar.MINUTE)) + ':' +
					twoDigits(calendar.get(Calendar.SECOND)));
			}
		});
		fFontManager.addComponent(menuItem);
	
		menuItem = new JMenuItem(StringDefs.TIME_FORMAT2);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Calendar calendar = Calendar.getInstance();
				fTextArea.restoreSelectionForMouseShortCutMenus();
				fTextArea.replaceSelection(
					twoDigits(calendar.get(Calendar.HOUR_OF_DAY)) + ':' +
					twoDigits(calendar.get(Calendar.MINUTE))); 
			}
		});
		fFontManager.addComponent(menuItem);

		menuItem = new JMenuItem(StringDefs.IMAGE_PPP);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fChooser == null) {
					fChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"JPG & GIF Images", "jpg", "gif");
					fChooser.setFileFilter(filter);
					fChooser.setAccessory(new ImageFilePreviewer(fChooser));
				}
				if (fChooser.showOpenDialog(StickyNoteEditor.this) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fChooser.getSelectedFile();
					Image image = Toolkit.getDefaultToolkit().getImage(selectedFile.getAbsolutePath());
					MediaTracker tracker = new MediaTracker(StickyNoteEditor.this);
					tracker.addImage(image, 0);
					try {
						tracker.waitForID(0);
					} catch (InterruptedException ex) {}

					fTextArea.insertIcon(new ImageIcon(image));
				}	
			}
		});
		fFontManager.addComponent(menuItem);

		fTextArea.addMouseShortCutMenu(menu); 		
	}
	
	private JMenu createMenus(String title, String[] actionNames, int prefixLength) {
		String[] menuNames = new String[actionNames.length];

		for (int i = 0; i < actionNames.length; i++)
			menuNames[i] = actionNames[i].substring(prefixLength);
	  	
		return(createMenus(title, actionNames, menuNames));
	}

	private JMenu createMenus(String title, String[] actionNames, String[] menuNames) {
		JMenu	menu = new XJMenu(title);

		for (int i = 0; i < actionNames.length; i++) {
			JMenuItem	menuItem = new JMenuItem(menuNames[i]);
			menuItem.addActionListener(new MenuActionAdapter(fTextArea.getAction(actionNames[i])));
			menu.add(menuItem);
			fFontManager.addComponent(menuItem);
		}
		fTextArea.addMouseShortCutMenu(menu);
		return(menu);
	}
	
	private class BackgroundColorActionListener implements ActionListener {
		public BackgroundColorActionListener(Color color) {
			fColor 				= color;
			fUseColorChooser 	= false;
		}

		public BackgroundColorActionListener(){
			fColor 				= Color.black;
			fUseColorChooser 	= true;
		}

		public void actionPerformed(ActionEvent e) {
			if (fUseColorChooser) {
				Color	color = JColorChooser.showDialog(
											StickyNoteEditor.this, 
											StringDefs.TEXT_BACKGROUND_C, 
											fTextBackgroundColor);
				if (color == null)
					return;
				else
					fColor = color;
			}
			fTextArea.setBackground(fColor);
			fTextBackgroundColor = fColor;
			fStickyNote.setTextBackground(fColor);
			repaint();
		}

		private Color			fColor;
		private boolean			fUseColorChooser;
	}

	private class TextColorActionListener implements ActionListener {
		public TextColorActionListener(Color color) {
			fColor 				= color;
			fUseColorChooser	= false;
		}

		public TextColorActionListener() {
			fColor				= Color.black;
			fUseColorChooser	= true;
		}

		public void actionPerformed(ActionEvent e) {
			if (fUseColorChooser) {
				if (fDummyFrame == null)
					fDummyFrame = new JFrame();
				Color	color = JColorChooser.showDialog(
										fDummyFrame, 
										StringDefs.TEXT_COLOR, 
										fColor);
				if (color == null)
					return;
				else
					fColor = color;
			}
			fTextArea.restoreSelectionForMouseShortCutMenus();
			fTextArea.setTextColor(fColor);
			repaint();
		}

		private Color 	fColor;
		private JFrame	fDummyFrame = null;
		private boolean	fUseColorChooser;
	}
	
	private class MenuActionAdapter implements ActionListener {
		public MenuActionAdapter(Action action) {
			fAction = action;
		}

		public void actionPerformed(ActionEvent e) {
			fTextArea.restoreSelectionForMouseShortCutMenus();
			fAction.actionPerformed(e);
			repaint();
		}

		private Action	fAction;
	}
	
	private StyledTextArea	fTextArea;
	private Container		fContentPane;
	
	private SimpleAttributeSet	fClearAttributeSet 		= null;
	
	private JMenu		fBackgroundColorMenu	= null;
	private JMenu		fTextColorMenu			= null;
	private JMenu		fFontStyleMenu			= null;
	
	private Color		fTextBackgroundColor = null;
	static private JFileChooser	fChooser	= null;
	private FontManager	fFontManager		= FontManager.getInstance();
	
	private final int			fOriginalWidth;
	private final int			fOriginalHeight;
	private final StickyNote	fStickyNote;
	
}

// LOG
//2.50 : 15-Nov-03 Y.Shibata	created from the old StickNote.java
//2.61 : 24-Dec-25 Y.Shibata	modified to the standard Swing
