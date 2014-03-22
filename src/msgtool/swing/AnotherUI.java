// File: AnotherUI.java - last edit:
// Yoshiki Shibata 9-May-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

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
public final class AnotherUI 
    extends JDialog
    implements ActionListener { 
    
    private Frame       fParentFrame    = null;
    private JButton     fDeliverButton  = null;
    private JTextField  fToList         = null;
    private JLabel      fToLabel        = null;
    private StyledTextArea  fInputArea = null;

    private Deliverer   fDeliverer      = null;
    private JPopupMenu  fRecipientHintsPopup    = null;
    
    private AnotherUI   fNext = null;
    private boolean         fActivated      = false;

    private boolean fPreserveZeroLocation = false;
    
    private CursorControl       fCursorControl      = CursorControl.instance();
    private RecipientHintsDB    fRecipientHintsDB   = RecipientHintsDB.getInstance();
    private AddressDB           fAddressDB          = AddressDB.instance();

	private BGColorManager		fBGColorManager		= BGColorManager.getInstance();
	private FontManager			fFontManager		= FontManager.getInstance();
	private ShiftKeyAdapter		fShiftKeyAdapter	= new ShiftKeyAdapter();
    
    public AnotherUI(
        Frame       parentFrame,
        Deliverer   deliverer,
        String      title,
        boolean     deliverEnabled) {
        super(parentFrame, title, false);
        
        Container   contentPane = getContentPane();     // swing
        
        fParentFrame    = parentFrame;
        fDeliverer      = deliverer;

        fRecipientHintsPopup    = new JPopupMenu(/* StringDefs.kRecipient*/);
        fActivated = false;
        fNext      = null;
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        LFManager.getInstance().add(this);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        contentPane.setLayout(gridBag); // swing
        //
        // Deliver Button
        //
        fDeliverButton      = new JButton(StringDefs.DELIVER);
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
		constraints.insets.top		= 2;
		constraints.insets.left		= 2;
		constraints.insets.bottom	= 2;
		constraints.insets.right	= 2; // (2,2,2,2)
        gridBag.setConstraints(fDeliverButton, constraints);
        contentPane.add(fDeliverButton); // swing
        //
        // To List
        //
        fToLabel    = new JLabel(StringDefs.TO_C);
        fCursorControl.addCursorComponent(fToLabel);
        fCursorControl.addEnablableComponent(fToLabel);
        constraints.gridwidth 		= 1;
		constraints.insets.left		= 0;
		constraints.insets.right	= 0; // (2,0,2,0)
        gridBag.setConstraints(fToLabel, constraints);
        contentPane.add(fToLabel); // swing
        fToLabel.addMouseListener(new JPopupMenuAdapter(fToLabel, fRecipientHintsPopup));
        updateRecipientHintsPopup();
		fFontManager.addComponent(fToLabel);
        
        fToList     = new XJTextField(1 /* Context.kWindowWidth - 5 */);
        fBGColorManager.add(fToList);
        fCursorControl.addCursorComponent(fToList);
        fCursorControl.addEnablableComponent(fToList);
        fToList.addKeyListener(fShiftKeyAdapter);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        gridBag.setConstraints(fToList, constraints);
        contentPane.add(fToList); // swing
		fFontManager.addComponent(fToList);
        //
        // Input area
        //
        fInputArea = new StyledTextArea(false);
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
        
        constraints.fill 			= GridBagConstraints.BOTH;
        constraints.anchor 			= GridBagConstraints.WEST;
        constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.weighty 		= 1.0;
		constraints.insets.top		= 0;
		constraints.insets.bottom	= 0; // (0,0,0,0)
        gridBag.setConstraints(fInputArea, constraints);
        contentPane.add(fInputArea); // swing
        
		fFontManager.addComponent(this);
		fFontManager.addContainer(this, true);
        //
        // The size of dialog must be specified for Swing version, because the
        // the preferred size of biggest component(JTextPane) is too small
        //
        Dimension size = fParentFrame.getSize();
        
        size.height = size.height / 3;
        setSize(size);
         
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
        
        if (target instanceof JMenuItem) {
            JMenuItem        item       = (JMenuItem)target;
            String          menuLabel  = item.getText();
            String  expandedHint = fRecipientHintsDB.getExpandedRecipients(menuLabel);
            
            Util.recipientHintSelected(expandedHint, fToList, fShiftKeyAdapter.isShiftKeyPressed());
            }
        }
    // =============================
    // Functions for Linked List
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
    public boolean IsActivated() {
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
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 1.95 : 18-Jul-98 Y.Shibata   restructuring.
// 2.15 : 27-Feb-99	Y.Shibata	used JPopupMenuAdapter class
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.24 :  9-May-99	Y.Shibata	used XJTextField for Drag and Drop
// 2.35 : 24-Oct-99	Y.Shibata	AnotherDialog -> AnotherUI


