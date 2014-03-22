// File: StyledTextArea.java - last edit:
// Yoshiki Shibata 27-Dec-03 

// Copyright (c) 1998 - 2000, 2003 by Yoshiki Shibata

package msgtool.swing;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import msgtool.common.FontManager;
import msgtool.common.SortUtil;
import msgtool.print.PageSetupAction;
import msgtool.print.PrintAction;
import msgtool.print.PrintContext;
import msgtool.swing.print.PrintableStyledTextArea;
import msgtool.ui.InputArea;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class StyledTextArea  
    extends JScrollPane
    implements InputArea {
     // ============================
     // Constructors
     // ============================
	public StyledTextArea()	{
		this(new DefaultStyledDocument());
	}
	
	public StyledTextArea(StyledDocument styledDocument) {
		this(styledDocument, true);
	}

	public StyledTextArea(StyledDocument styledDocument,
			boolean createShortCutMenu) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	  	document = styledDocument;
        textPane = new TextPane(document); // inner class defined below
        getViewport().add(textPane);

        keepMinimumSize = false;
        
		mouseShortCutMenus = new JPopupMenu();
		if (createShortCutMenu) {
			createMouseShortCutMenus();
			createMouseShortCutActions();
		}
		createPrintMenus(createShortCutMenu);
		initialized = true;

		// getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
	}
	

   	public StyledTextArea(
		boolean	keepMinimumSize,
		Color	backgroundColor) {
		this(keepMinimumSize);
		setBackground(backgroundColor);
	}

    public StyledTextArea(
        boolean keepMinimumSize) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
              
        document = new DefaultStyledDocument();
        textPane = new TextPane(document); // inner class defined below
        getViewport().add(textPane);
        
        this.keepMinimumSize = keepMinimumSize;
        
		mouseShortCutMenus = new JPopupMenu();
        createMouseShortCutMenus();
        createMouseShortCutActions();
		createPrintMenus(true);
		initialized = true;
		
		// getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
  	} 
  	
  	public static StyledTextArea createStyledTextArea(boolean createShortCutMenu) {
  		return new StyledTextArea(new DefaultStyledDocument(), false);
  	}
    // ============================
    // Public Methods
    // ============================

	public void addFocusListenerToTextPane(FocusListener l) {
		// This method name should not be addFocusListener, because this method adds
		// the listener to the TextPane.
		textPane.addFocusListener(l);
	}

    public void addKeyListener(KeyListener l) {
        textPane.addKeyListener(l);
        keyListener = l;
	}
    
	public void addMouseListener(MouseListener l) {
		textPane.addMouseListener(l);
	}

	public void addMouseMotionListener(MouseMotionListener l) {
		textPane.addMouseMotionListener(l);
	}

	public void addMouseShortCutMenu(JMenuItem menuItem) {
		mouseShortCutMenus.add(menuItem);
		FontManager.getInstance().addComponent(menuItem);
	}
   	
	public void addMouseShortCutMenuSeparator() {
		mouseShortCutMenus.addSeparator();
	}
    public void appendText(String aText) {
        try {
            document.insertString(document.getLength(), aText, additionalAttributes);
        } catch (BadLocationException e) {}
 	}
    
	public void clearText() // InputArea interface 
	{
		setText("");
	}
    public void deselectAll() {
        if (document.getLength() == 0)
            return;
        textPane.select(0,0);
	}
    
	public Action getAction(String actionName) {
		return(commandsMap.get(actionName));
	}

	public String[] getActionNames(String name) {
		int	noOfNames = 0;

		for (int i = 0; i < actionNames.length; i++) {
		 	if (actionNames[i].startsWith(name))
				noOfNames ++;
		}
	   	if (noOfNames == 0)
			return(null);

	  	String[] names = new String[noOfNames];
		int index = 0;
		for (int i = 0; i < actionNames.length; i++) {
			if (actionNames[i].startsWith(name))
				names[index++] = actionNames[i];
		 }
	 	return(names);
	}

   	public Color getBackground() {
		if (initialized)
			return(textPane.getBackground());
	   	else
			return(super.getBackground());
	}

    public AttributeSet getCharacterAttributes() {
    	return (textPane.getCharacterAttributes());
   	}

    public Cursor getCursor() {
        if (cursor == null) {
			cursor = super.getCursor();
			}
        return(cursor);
  	}
        
    public int getLength() {
        return(document.getLength());
  	}
        
    public Dimension getMinimumSize() {
        //
        // To put this StyledTextArea into a splitPane, the height of MinimumSize()
        // must be small. Otherwise, splitting doesn't work well. fKeepMinimumSize is
        // used to control the height.
        // 
        if (keepMinimumSize) {
            Dimension size = super.getMinimumSize();
            size.height = 32;
            return(size);
       	} else
            return(super.getMinimumSize());
  	}
  	
  	public void setSize(Dimension size) {
  		super.setSize(size);
  	}
  	
  	public void setBounds(int x, int y, int width, int height) {
  		super.setBounds(x, y, width, height);
  	}
            
    public String getText() {
        String  text = null;
        
        try {
            text = document.getText(0, document.getLength());
      	} catch (BadLocationException e) {
            text = "";
        }
        return(text);
	}
   
	public StyledDocument getStyledDocument() {	return(textPane.getStyledDocument());}
    public Color 		getTextColor() 	{ return(StyleConstants.getForeground(additionalAttributes)); } 
    public Font 		getTextFont() 	{ return(font); } 
    public JTextPane 	getTextPane() 	{ return(textPane); } 
	public void 		insertIcon(Icon	icon) { textPane.insertIcon(icon); }	
    public boolean 		isEditable() 	{ return(textPane.isEditable()); }     
    public boolean 		isEnabled() 	{ return(enabled); }

    public void removeKeyListener(KeyListener l) {
        textPane.removeKeyListener(l);
        keyListener = null;
  	}

   	public void replaceSelection(String text) {
		textPane.replaceSelection(text);
	}

    public void requestFocus() {
        textPane.requestFocus();
  	}
    
    public void restoreSelectionForMouseShortCutMenus() {
		textPane.setSelectionStart(selectionStart);
		textPane.setSelectionEnd(selectionEnd);
	}

	/*
    public void scrollToBottom() {
        //
		// To scroll correctly, recompute correct sizes of view and extent by
		// invalidating viewport & textpane and validating parent which is a 
		// container. 
		//
		JViewport viewport = getViewport();

		fTextPane.invalidate();
		viewport.invalidate();
		invalidate();
		getParent().validate();
		//
		// Now we can obtain correct sizes of view and extent.
		//
		Dimension viewSize = viewport.getViewSize();
        Dimension extentSize = viewport.getExtentSize();
		//
		// Set a new view position to scroll to the bottom.
		//
        viewport.setViewPosition(new Point(0, viewSize.height - extentSize.height));
  	}
    */
	public void scrollToBottom() {
		try {
			Document doc = textPane.getDocument();
			Rectangle r = textPane.modelToView(doc.getLength());

			if (r != null)
				textPane.scrollRectToVisible(r);
	 	} catch (BadLocationException e) {}
	}
   
	public void setBackground(Color	color) {
		if (initialized) {
			backgroundColor = color;
			textPane.setBackground(color);
			textPane.setSelectionColor(color.darker());
		}
		else
			super.setBackground(color);
	}

    public void setCharacterAttributes(AttributeSet attr, boolean replace) {
		textPane.setCharacterAttributes(attr, replace);
	}
         
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        textPane.setCursor(cursor);
        super.setCursor(cursor);
  	}
    
    public void setEditable(boolean editable) {
        textPane.setEditable(editable);
        
        cutMenuItem.setEnabled(editable);
        pasteMenuItem.setEnabled(editable);
 	}
                 
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        textPane.setEnabled(enabled);
        super.setEnabled(enabled);
  	}
    
    public void setStyledDocument(StyledDocument aDocument) {
        textPane.setStyledDocument(aDocument);
        document = aDocument;
        validate();
  	}

    public void setText(String  text) {
        try {
            document.remove(0, document.getLength());

            if (text.length() != 0) 
                document.insertString(0, text, additionalAttributes);
            else 
                textPane.setCharacterAttributes(additionalAttributes, true);
            }
        catch (BadLocationException e) {}
        }
    
    public void setTextColor(Color  color) {
		StyleConstants.setForeground(additionalAttributes, color);
        textPane.setCharacterAttributes(additionalAttributes, false);
 	}
    
    public void setFont(Font font) {
    	// setFont is supposed to set the font to the TextPane here,
		// but this method is called during construction.
		if (initialized)
			setTextFont(font); 
		else
			super.setFont(font);
		}

    public void setTextFont(Font    font) {
        if (font == null)
            return;
        // Setting the font to the TextPane means that it actually set the font
		// to the default style of the DefaultSytledDocument.
		// see "Core Swing advanced programming", p.163 [2.43]
		textPane.setFont(font);
		Util.setFontsToMenu(mouseShortCutMenus, font);
 	}

    // =============================
    // Private Methods
    // =============================
    private void createMouseShortCutMenus() {        
        mouseShortCutMenus.add(cutMenuItem = new JMenuItem(StringDefs.CUT));
        mouseShortCutMenus.add(copyMenuItem = new JMenuItem(StringDefs.COPY));
        mouseShortCutMenus.add(pasteMenuItem = new JMenuItem(StringDefs.PASTE));
        mouseShortCutMenus.addSeparator();
        mouseShortCutMenus.add(selectAllMenuItem = new JMenuItem(StringDefs.SELECT_ALL));
		mouseShortCutMenus.addSeparator();
		mouseShortCutMenus.add(clearAllMenuItem = new JMenuItem(StringDefs.CLEAR_ALL));
        textPane.add(mouseShortCutMenus);

		FontManager	fm = FontManager.getInstance();
		fm.addComponent(cutMenuItem);
		fm.addComponent(copyMenuItem);
		fm.addComponent(pasteMenuItem);
		fm.addComponent(selectAllMenuItem);
		fm.addComponent(clearAllMenuItem);
 	}
    
    private void createMouseShortCutActions() {
        Action[]    actions = textPane.getActions();
		actionNames = new String[actions.length];

        // create action and action name table(fCommandsTable).
        for (int i = 0; i < actions.length; i++) {
 	    	actionNames[i] = (String) actions[i].getValue(Action.NAME);
            commandsMap.put(actionNames[i], actions[i]);
		}
	 	SortUtil.sortStrings(actionNames);
        
        // Cut menu.
        Action action = commandsMap.get(DefaultEditorKit.cutAction);
        if (action == null)
            cutMenuItem.setEnabled(false);
        else {
            cutMenuItem.setActionCommand(DefaultEditorKit.cutAction);
            cutMenuItem.addActionListener(new ShortCutMenuActionAdapter(action));
        }    

        // Paste
        action = commandsMap.get(DefaultEditorKit.pasteAction);
        if (action == null)
            pasteMenuItem.setEnabled(false);
        else {
            pasteMenuItem.setActionCommand(DefaultEditorKit.pasteAction);
            pasteMenuItem.addActionListener(new ShortCutMenuActionAdapter(action));
        }    

        // Copy
        action = commandsMap.get(DefaultEditorKit.copyAction);
        if (action == null)
            copyMenuItem.setEnabled(false);
        else {
            copyMenuItem.setActionCommand(DefaultEditorKit.copyAction);
            copyMenuItem.addActionListener(new ShortCutMenuActionAdapter(action));
        }

        //  Select All
        selectAllMenuItem.setActionCommand("select all");
        selectAllMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
            	textPane.selectAll();
          	}
       	});

		// Clear All
		clearAllMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setText("");
			}
		});
	}

	private void createPrintMenus(boolean needSeparator) {
		try {
			PrintContext	c = new PrintContext();

			JMenuItem pageSetupMenu = createMenuItem(StringDefs.PAGE_SETUP_PPP, new PageSetupAction(c));
			JMenuItem printMenu = createMenuItem(StringDefs.PRINT_PPP, new PrintAction(
				new PrintableStyledTextArea(this), c));
	 		
	 		if (needSeparator)
				mouseShortCutMenus.addSeparator();
			mouseShortCutMenus.add(pageSetupMenu);
			mouseShortCutMenus.add(printMenu);
	   	} catch (NoClassDefFoundError e) {
			// ignore this error. This error happens with JDK1.1
		}
	}

	private JMenuItem createMenuItem(String title, ActionListener listener) {
		FontManager	fm = FontManager.getInstance();	
		JMenuItem	menuItem = new JMenuItem(title);

		fm.addComponent(menuItem);
		menuItem.addActionListener(listener);
		mouseShortCutMenus.add(menuItem);
		return menuItem;
	}
            
    private boolean popupCheck(MouseEvent e) {
		if (mouseShortCutMenus == null)
			return(false);

        if (e.isPopupTrigger()) {
            if (e.getComponent() == textPane) {
				saveSelectionForMouseShortCutMenus();
				Point	location = new Point(e.getX(), e.getY());
				location = ComponentUtil.fitPopupMenuInsideScreen(textPane, mouseShortCutMenus, location);
                mouseShortCutMenus.show(textPane, location.x, location.y);
                return(true);
          	}
     	}
        return(false);
 	}
   
   	private void saveSelectionForMouseShortCutMenus() {
		selectionStart 	= textPane.getSelectionStart();
		selectionEnd	= textPane.getSelectionEnd();
	}
    //
    // Fields
    //
    private boolean 				initialized 		= false;
    private StyledDocument          document           	= null;
    private TextPane                textPane           	= null;
    private Font                    font               	= null;
    private SimpleAttributeSet      additionalAttributes	= new SimpleAttributeSet();;
    private boolean                 keepMinimumSize    	= false;
    private Cursor                  cursor             	= null;
    private boolean                 enabled          	= true;
    private JPopupMenu              mouseShortCutMenus 	= null;
    
    private JMenuItem   cutMenuItem        = null;
    private JMenuItem   copyMenuItem       = null;
    private JMenuItem   pasteMenuItem      = null;
    private JMenuItem   selectAllMenuItem  = null;
	private JMenuItem	clearAllMenuItem	= null;
    
    @SuppressWarnings("unused")
	private KeyListener keyListener        = null;
    @SuppressWarnings("unused")
	private Color       backgroundColor    = null;

	private int			selectionStart	= 0;
	private int			selectionEnd	= 0;
	private HashMap<String, Action>	 commandsMap = new HashMap<String, Action>();
	private String[]	actionNames		= null;
    
	// ==================================
	// Private inner classes
	// ==================================
	private class ShortCutMenuActionAdapter implements ActionListener {
		ShortCutMenuActionAdapter(Action action) {
			this.action = action;
		}

	   	public void actionPerformed(ActionEvent e) {
			restoreSelectionForMouseShortCutMenus();
			action.actionPerformed(e);
		}
		
	  	private Action action;
	}
	/**
	 * Inner class for JTextPane. The original JTextPane doesn't
	 * allow a user to select text when it is disabled. Therefore
	 * this class allow a user to select text even if it is disabled.
	 */
    private class TextPane extends JTextPane {
    
        public TextPane(StyledDocument document) {
            super(document);
            enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
            this.addFocusListener(new FocusAdapter() {
            	public void focusLost(FocusEvent event) {
					setSelectionEnd(getSelectionStart());
            	}
            });
      	}
        
        public void setEditable(boolean editable) {
            // super class must be always editable.
            super.setEditable(true);
            this.editable = editable;
       	}
      
        public boolean isEditable() {
        	return editable;
      	}
          
        public void processKeyEvent(KeyEvent event) {
            if (editable || isCopyOperation(event)) {    
            	super.processKeyEvent(event);
          	} else {
                event.consume();
            }
    	}

		private boolean isCopyOperation(KeyEvent keyEvent) {
			if ((!keyEvent.isControlDown()) || 
			    (keyEvent.getID() != KeyEvent.KEY_PRESSED))
				return false;
	   		if ((keyEvent.getKeyCode() == KeyEvent.VK_C) ||   // Metal & Windows
			    (keyEvent.getKeyCode() == KeyEvent.VK_INSERT))// Motif
				return true;
		 	return false;
		}
        
        public void processMouseEvent(MouseEvent event) {
            if (popupCheck(event)) {
                event.consume();
          	} else {
                int modifiers = event.getModifiers();
                
                if (((modifiers & InputEvent.BUTTON2_MASK) != 0) ||
                    ((modifiers & InputEvent.BUTTON3_MASK) != 0)) {
                    event.consume();
              	} else {
                    super.processMouseEvent(event);
				}
         	}
   		}
                       
        private boolean  editable = true; 
	}
        
} 
// LOG
// 1.85 : 24-Jan-98 Y.Shibata  created
// 1.92 beta 4 : 8-May-98 Y.Shibata modified for JDK1.1.6: The behavior of 
//              toFront() has been changed.
// 2.15 : 27-Feb-99	Y.Shibata	deleted unncessary MouseListener code.
// 2.20 : 30-Mar-99	Y.Shibata	NativeInputWindow.setActive() is modified.
// 2.22 : 17-Mar-99	Y.Shibata	added replaceSelection & insertIcon
// 2.24 : 16-May-99	Y.Shibata	added "delete all"
//								scrollToBottom() is correctly re-implemented.
// 2.31 : 31-May-99	Y.Shibata	fixed a bug of Stack Overflow
// 2.32 :  3-Aug-99	Y.Shibata	enabled WindowBlit operation for Swing1.1.1 or JDK1.2.2. But it didn't work.
// 2.42 : 27-Feb-00	Y.Shibata	Copy operation for Motif is different from Metal/Windows. So 
//								isCopyOperation method is added.
//        28-Feb-00	Y.Shibata	added addFocusListenerToTextPane()
// 2.43 :  5-Mar-00	Y.Shibata	re-implemented setTextFont() correctly. 
//								Original implementation is completely wrong.
// 2.50 : 13-Oct-03 Y.Shibata	NativeInputWindow is no longer supported.
//        27-Dec-03 Y.Shibata   used Java Generics
