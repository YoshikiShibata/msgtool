// File: DedicatedUIImpl.java - last edit:
// Yoshiki Shibata 15-Nov-03

// Copyright (c) 1997 - 1999, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.FontManager;
import msgtool.common.KeyUtil;
import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.db.RecipientHintsDB;
import msgtool.log.LogArea;
import msgtool.ui.DedicatedUI;
import msgtool.ui.model.DedicatedModel;
import msgtool.util.ComponentUtil;
import msgtool.util.CursorControl;
import msgtool.util.ShiftKeyAdapter;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class DedicatedUIImpl 
    extends JDialog
    implements DedicatedUI { 
    
    private JButton      fDeliverButton	= null;
    private JTextField   fToList        = null;
    private JLabel       fToLabel       = null;
    
    private StyledTextArea  fInputArea	= null;
    private JSplitPane      fSplitter   = null;
    private JPopupMenu  	fRecipientHintsPopup    = null;

    private boolean			fActivated      = false;
    
    private Frame      		fParentFrame    = null;
	private DedicatedModel	fModel			= null;

    private JMenuItem   fMenuItem   	= null;
    
    private ShiftKeyAdapter	fShiftKeyAdapter = new ShiftKeyAdapter();

    private boolean fPreserveZeroLocation = false;
    
    private CursorControl       fCursorControl      = CursorControl.instance();
    private PropertiesDB        fPropertiesDB       = PropertiesDB.getInstance();
    private RecipientHintsDB    fRecipientHintsDB   = RecipientHintsDB.getInstance();
    private AddressDB           fAddressDB          = AddressDB.instance(); 
	private BGColorManager		fBGColorManager		= BGColorManager.getInstance();
	private FontManager			fFontManager		= FontManager.getInstance();

	private LogArea		fLogArea	= null;

    public DedicatedUIImpl(
        Frame               parentFrame,
		DedicatedModel		model,
        boolean             deliverEnabled) {
        setTitle(model.getTitle());

        Container   contentPane = getContentPane(); // Swing
        
        fParentFrame            = parentFrame; 
		fModel					= model;
		fModel.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				setTitle(fModel.getTitle());
				if (fMenuItem != null)
					fMenuItem.setText(fModel.getTitle());
			}
		});

        fRecipientHintsPopup    = new JPopupMenu(/* StringDefs.kRecipient*/ );
        fActivated = false;
        
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        LFManager.getInstance().add(this);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        contentPane.setLayout(gridBag); // Swing.
        //
        // Deliver Button
        //
        fDeliverButton      = new JButton(StringDefs.DELIVER);
        fDeliverButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
				fModel.deliver(fModel.getSenderName() + ", " + fToList.getText(),
								fInputArea, 
								DedicatedUIImpl.this);
        	}
        });
        fDeliverButton.setEnabled(deliverEnabled);
        fCursorControl.addCursorComponent(fDeliverButton);
        fCursorControl.addEnablableComponent(fDeliverButton);
		fFontManager.addComponent(fDeliverButton);
        
        constraints.fill 			= GridBagConstraints.NONE;
        constraints.anchor 			= GridBagConstraints.WEST;
        constraints.gridwidth 		= 1;
        constraints.weightx 		= 0.0;
        constraints.weighty 		= 0.0;
		constraints.insets.top		= 2;
		constraints.insets.left		= 2;
		constraints.insets.bottom	= 2;
		constraints.insets.right	= 2; // (2,2,2,2) 
        gridBag.setConstraints(fDeliverButton, constraints);
        contentPane.add(fDeliverButton); // Swing
        //
        // To List
        //
        fToLabel    = new JLabel(StringDefs.ADDITIONAL_TO_C);
        fCursorControl.addCursorComponent(fToLabel);
        fCursorControl.addEnablableComponent(fToLabel);
		fFontManager.addComponent(fToLabel);
		
		constraints.insets.left		= 0;
		constraints.insets.right	= 0; // (2,0,2,0)
        gridBag.setConstraints(fToLabel, constraints);
        contentPane.add(fToLabel); 
        fToLabel.addMouseListener(new JPopupMenuAdapter(fToLabel, fRecipientHintsPopup));
        updateRecipientHintsPopup();
        
        fToList     = new XJTextField(1 /* Context.kWindowWidth - 24 */);
        fBGColorManager.add(fToList);
        fCursorControl.addCursorComponent(fToList);
        fCursorControl.addEnablableComponent(fToList);
		fFontManager.addComponent(fToList);

        fToList.addKeyListener(fShiftKeyAdapter);
        constraints.fill 			= GridBagConstraints.HORIZONTAL;
        constraints.weightx 		= 1.0;
		constraints.insets.right	= 2; // (2,0,2,2)
		constraints.gridwidth   	= GridBagConstraints.REMAINDER;
        gridBag.setConstraints(fToList, constraints);
        contentPane.add(fToList); // Swing
        //
        // Input area
        //
        fInputArea = new StyledTextArea(true);
		fBGColorManager.add(fInputArea);
        fInputArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) { 
      			int keyCode = keyEvent.getKeyCode();
      			if (KeyUtil.isDeliverKey(keyCode)) {
         			fModel.deliver(fModel.getSenderName() + ", " + fToList.getText(), 
										fInputArea, DedicatedUIImpl.this);
         		}
			}
		});
        
        fCursorControl.addCursorComponent(fInputArea);
        fCursorControl.addEnablableComponent(fInputArea);
		fFontManager.addComponent(fInputArea);
        //
        // Log Area
        //
        StyledTextArea logTextArea = new StyledTextArea(true);
		fBGColorManager.add(logTextArea); 
        fCursorControl.addCursorComponent(logTextArea);
        logTextArea.setEditable(false);
		fFontManager.addComponent(logTextArea);

        fSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fInputArea, logTextArea);
        fSplitter.setContinuousLayout(true);
        fSplitter.setDividerSize(Context.SPLITTER_WIDTH);
        fSplitter.setOneTouchExpandable(true);
        constraints.fill 			= GridBagConstraints.BOTH;
        constraints.anchor 			= GridBagConstraints.WEST;
        constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.weightx 		= 1.0;
        constraints.weighty 		= 1.0;
		constraints.insets.top		= 0;
		constraints.insets.left		= 0;
		constraints.insets.bottom	= 0;
		constraints.insets.right	= 0; // (0,0,0,0) 
        gridBag.setConstraints(fSplitter, constraints);
        contentPane.add(fSplitter);  // Swing

        Dimension size = fPropertiesDB.getSize(fModel.getSenderIP());
        if (size != null)
            setSize(size);
        else
            setSize(fParentFrame.getSize());
		fFontManager.addContainer(this, true);
            
        Point   location = fPropertiesDB.getLocation(fModel.getSenderIP());
        if (location != null) {
            setLocation(location);
            fPreserveZeroLocation = true;
       	}
        fCursorControl.addCursorComponent(this);

		fLogArea = new LogAreaImpl(logTextArea);
  	}
    
    public void setFont(Font font) {
        Util.setFontsToMenu(fRecipientHintsPopup, font);
        //
        // Note that when SetFonts() is called from the constructor, 
        // fMenuItem is still null. A value will be given immediately after
        // the contructor is called.
        //
        if (fMenuItem != null) {
            fMenuItem.setFont(font);
            fMenuItem.invalidate();
     	}
  	}
   
    public void updateRecipientHintsPopup() {
        Util.updateHintsMenu(fRecipientHintsPopup, 
                fRecipientHintsDB.getDB(), 
                fAddressDB.getHintedAddressDB(),
                
                new ActionListener() {
                	public void actionPerformed(ActionEvent event) {
                		JMenuItem item = (JMenuItem) event.getSource();
                		String menuLabel = item.getText();
                		
						Util.recipientHintSelected(
							fRecipientHintsDB.getExpandedRecipients(menuLabel),
							fToList, 
							fShiftKeyAdapter.isShiftKeyPressed());
                	}
                });
                
        Util.setFontsToMenu(fRecipientHintsPopup, Context.getFont());
   	}
    
    public void setDeliverEnabled(boolean state) {
        fDeliverButton.setEnabled(state);
  	}

    public void setVisible(boolean visible) {
        if (visible) {
            ComponentUtil.overlapComponents(this, fParentFrame, 32, fPreserveZeroLocation);
            fActivated = true;
            fPreserveZeroLocation = true;
      	} else {
            fActivated = false;
            fPropertiesDB.setLocation(fModel.getSenderIP(), getLocation());
            fPropertiesDB.setSize(fModel.getSenderIP(), getSize());
     	}
        
        super.setVisible(visible);
        //
        // Always locate the divider at the ratio of 3:7.
        //
        if (visible) {
            fSplitter.setDividerLocation(0.3); 
            //
            // Make sure that the log is scrolled down to the last position.
            // Note that this selection must be done after the window 
            // is visible. Because making this window visible will
            // scroll this window up to the first position. [V1.65]
            //
            fLogArea.scrollDownToEnd();
       	}
   	}

    public boolean IsActivated() {
        return(fActivated);
   	}
    // ========================
    // Log Area
    // ========================
    public void appendLog(String text) {	
        if (text != null) {
			fLogArea.appendText(text);
			fLogArea.scrollDownToEnd();
        }
   	}
    
    public void appendLog(String log, Color color) {
		fLogArea.appendText(log, color);
  	}
        
    // =======================
    // SenderIP
    // =======================
    public String   getSenderIP() {
        return(fModel.getSenderIP());
  	}
    // ============================
    // Set MenuItem for this window
    // ============================
    public void setMenuItem(Object    menuItem) {
        fMenuItem = (JMenuItem) menuItem;
  	}
    // ============================
    // Set Additional To: list
    // ============================
    public void setAdditionalRecipients(String  recipients) {
        fToList.setText(recipients);
  	}
}
// Log
//        21-Mar-97 Y.Shibata   created from AnotherDialog.java
// 1.20 : 29-Mar-97 Y.Shibata   changed show() to setVisible()
// 1.31 : 30-Apr-97 Y.Shibata   select the last char to scroll up correctly in Log_AppendText
//        19-Jun-97 Y.Shibata   clicking Deliver button didn't work.
// 1.32 : 20-Jun-97 Y.Shibata   commented out the code to scroll up
// 1.34 :  6-Jul-97 Y.Shibata   MainFrame.Deliver() automatically records log..
//                              Modified code to make the recipient hint
// 1.37 : 17-Jul-97 Y.Shibata   set font correctly to the menuItem of the menu list of all dedicated dialogs.
// 1.53 :  3-Sep-97 Y.Shibata   fixed a bug that a failed message was not logged.
// 1.56 : 11-Sep-97 Y.Shibata   adjust the initial location.
// 1.60 : 13-Sep-97 Y.Shibata   use CursorControl
// 1.61 : 16-Sep-97 Y.Shibata   added fInputArea.requestFocus(); to get focus back.
// 1.63 : 20-Sep-97 Y.Shibata   added SetAdditionalTo().
//        21-Sep-97 Y.Shibata   backed out the code for 1.61
// 1.65 : 27-Sep-97 Y.Shibata   scroll the log window down to the last position when visible.
// 1.69 : 11-Oct-97 Y.Shibata   added "Clear Log" button
// 1.75 :  9-Nov-97 Y.Shibata   preserve the zero location
//                              Save/Restore location and size.
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 2.15 : 27-Feb-99	Y.Shibata	used JPopupMenuAdapter class
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.24 :  9-May-99	Y.Shibata	used XJTextField for Drag and Drop
// 2.33 : 21-Aug-99	Y.Shibata	used LogArea class [Refactoring]
// 2.35 : 24-Oct-99	Y.Shibata	DedicatedDialog -> DedicatedUI
//		  30-Oct-99	Y.Shibata	DedicatedUI -> DedicatedUIImpl
// 2.50 :  9-Nov-03	Y.Shibata	refactored.
//        15-Nov-03 Y.Shibata	modified not to use the parent frame for JDialog.

