// File: AnotherUI.java - last edit:
// Yoshiki Shibata 9-May-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import msgtool.Deliverer;
import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.FontManager;
import msgtool.common.KeyUtil;
import msgtool.db.AddressDB;
import msgtool.db.RecipientHintsDB;
import msgtool.util.ComponentUtil;
import msgtool.util.CursorControl;
import msgtool.util.ShiftKeyAdapter;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class AnotherUI extends Dialog implements ActionListener {  
	
	private Frame       	fParentFrame    = null;
    private Button      	fDeliverButton  = null;
    private TextField   	fToList         = null;
    private Label       	fToLabel        = null;
    private InputTextArea   fInputArea      = null;
    private Deliverer   	fDeliverer      = null;
    private PopupMenu   	fRecipientHintsPopup    = null;
    
    private AnotherUI   	fNext 		= null;
    private boolean         fActivated 	= false;

    private boolean fPreserveZeroLocation = false;
    
    private RecipientHintsDB    fRecipientHintsDB   = RecipientHintsDB.getInstance();
    private AddressDB           fAddressDB          = AddressDB.instance();
    
    private CursorControl   fCursorControl  = CursorControl.instance();
	private BGColorManager	fBGColorManager	= BGColorManager.getInstance();
	private FontManager		fFontManager	= FontManager.getInstance();

	private ShiftKeyAdapter		fShiftKeyAdapter = new ShiftKeyAdapter();
    
    public AnotherUI(
        Frame       parentFrame,
        Deliverer   deliverer,
        String      title,
        boolean     deliverEnabled) {
        super(parentFrame, title, false);
        
        fParentFrame    = parentFrame;
        fDeliverer      = deliverer;

        fRecipientHintsPopup = new PopupMenu(StringDefs.RECIPIENT);
        fActivated = false;
        fNext      = null;
        //
        // Register as WindowListener
        //
        addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) { 
				setVisible(false); 
			}
		});
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        setLayout(gridBag);
        //
        // Deliver Button
        //
        fDeliverButton      = new Button(StringDefs.DELIVER);
        fDeliverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fDeliverer.deliver(fToList.getText(), fInputArea);
            	fInputArea.requestFocus();  // V1.61	
			}
		});
        fDeliverButton.setEnabled(deliverEnabled);
        fCursorControl.addCursorComponent(fDeliverButton);
        fCursorControl.addEnablableComponent(fDeliverButton);
		fFontManager.addComponent(fDeliverButton);
        
        constraints.fill 			= GridBagConstraints.NONE;
        constraints.anchor 			= GridBagConstraints.WEST;
        constraints.gridwidth 		= 2;
        constraints.weightx 		= 0.0;
        constraints.weighty 		= 0.0;
		constraints.insets.top 		= 2;
		constraints.insets.left 	= 2;
		constraints.insets.bottom	= 2;
		constraints.insets.right	= 2;
        gridBag.setConstraints(fDeliverButton, constraints);
        add(fDeliverButton);
        //
        // To List
        //
        fToLabel    = new Label(StringDefs.TO_C);
        fCursorControl.addCursorComponent(fToLabel);
        fCursorControl.addEnablableComponent(fToLabel);
		fFontManager.addComponent(fToLabel);

        constraints.gridwidth 		= 1;
		constraints.insets.left		= 0;
		constraints.insets.right	= 0; 
        gridBag.setConstraints(fToLabel, constraints);
        add(fToLabel);
        fToLabel.addMouseListener(new PopupMenuAdapter(fToLabel, fRecipientHintsPopup));
        updateRecipientHintsPopup();
        
        fToList     = new XTextField(1 /* Context.kWindowWidth - 5 */);
		fBGColorManager.add(fToList);
        fCursorControl.addCursorComponent(fToList);
        fCursorControl.addEnablableComponent(fToList);
        fToList.addKeyListener(fShiftKeyAdapter);
		fFontManager.addComponent(fToList);

        constraints.gridwidth   = GridBagConstraints.REMAINDER;
        constraints.fill        = GridBagConstraints.HORIZONTAL;
        constraints.weightx     = 1.0;
        gridBag.setConstraints(fToList, constraints);
        add(fToList);
        //
        // Input area
        //
        fInputArea = new InputTextArea(5,  Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);
		fBGColorManager.add(fInputArea);  
        fInputArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) { 
        		int keyCode = keyEvent.getKeyCode();
        		if (KeyUtil.isDeliverKey(keyCode))
            		fDeliverer.deliver(fToList.getText(), fInputArea);    
        	}
		});
        fCursorControl.addCursorComponent(fInputArea);
        fCursorControl.addEnablableComponent(fInputArea);
		fFontManager.addComponent(fInputArea);
        
        constraints.fill        	= GridBagConstraints.BOTH;
        constraints.weighty     	= 1.0;
		constraints.insets.top		= 0;
		constraints.insets.bottom	= 0;
        gridBag.setConstraints(fInputArea, constraints);
        add(fInputArea);
        
		fFontManager.addComponent(this);
		fFontManager.addContainer(this);
        pack();
        fCursorControl.addCursorComponent(this);
 	}
    
	public void setFont(Font font) {
        Util.setFontsToMenu(fRecipientHintsPopup, font);
 	}
   
    public void updateRecipientHintsPopup() {
        Util.updateHintsMenu(fRecipientHintsPopup, 
            fRecipientHintsDB.getDB(), 
            fAddressDB.getHintedAddressDB(), this);
        Util.setFontsToMenu(fRecipientHintsPopup, Context.getFont());
  	}
    
    public void setDeliverEnabled(boolean enabled) {
        fDeliverButton.setEnabled(enabled);
  	}
    
    // ===========================
    // ActionListener
    // ===========================
    public void actionPerformed(ActionEvent event) {
        Object  target = event.getSource();
        
        if (target instanceof MenuItem) {
            MenuItem        item       = (MenuItem)target;
            String          menuLabel  = item.getLabel();
            String  expandedHint = fRecipientHintsDB.getExpandedRecipients(menuLabel);
            
            Util.recipientHintSelected(expandedHint, fToList, fShiftKeyAdapter.isShiftKeyPressed());
        }
    }
    // =============================
    // Funtions for Linked List
    // =============================
    public void setVisible(boolean visible) {
        if (visible) {
            ComponentUtil.overlapComponents(this, fParentFrame, 32, fPreserveZeroLocation);
            fActivated = true;
            fPreserveZeroLocation = true;
        }
        else
            fActivated = false;
        super.setVisible(visible);
    }

    public boolean isActivated() {
        return(fActivated);
    }
    
    public void setNext(AnotherUI    next) {
        fNext = next;
    }
    
    public AnotherUI getNext() {
        return(fNext);
    }
}
// Log
//        16-Feb-97 Y.Shibata   created
//        20-Feb-97 Y.Shibata   modified for the final version of JDK 1.1
// 1.04 : 14-Mar-97 Y.Shibata   update the font of the popup hints
// 1.05 : 19-Mar-97 Y.Shibata   adjusted the locations of Popup Menus
// 1.10 : 21-Mar-97 Y.Shibata   fixed the adjustment of 1.05
// 1.20 : 29-Mar-97 Y.Shibata   changed show() to setVisible()
// 1.34 :  7-Jul-97 Y.Shibata   modified code which makes the recipient hints
// 1.49 : 22-Aug-97 Y.Shibata   use Util.FitComponentIntoScreen().
// 1.56 : 11-Aug-97 Y.Shibata   adjust the initial location.
// 1.60 : 13-Sep-97 Y.Shibata   use CursorControl
// 1.61 : 16-Sep-97 Y.Shibata   added fInputArea.requestFocus() to get focus back.
// 1.63 : 21-Sep-97 Y.Shibata   backed out the code of 1.61
// 1.75 :  9-Nov-97 Y.Shibata   preserve the zero location.
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 2.15 : 27-Feb-99	Y.Shibata	used PopupMenuAdapter class
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.24 :  5-May-99	Y.Shibata	used FontManager
//		   9-May-99	Y.Shibata	used XTextField for Dnd
// 2.34 : 24-Oct-99	Y.Shibata	AnotherDialog -> AboutUI

