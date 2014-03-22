// File: SearchUIImpl.java - last edit:
// Yoshiki Shibata 24-Oct-99

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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.db.PropertiesDB;
import msgtool.protocol.MiscProtocol;
import msgtool.ui.SearchUI;
import msgtool.util.ComponentUtil;
import msgtool.util.CursorControl;
import msgtool.util.StringDefs;
import msgtool.util.StringUtil;
import msgtool.util.TimerUtil;

@SuppressWarnings("serial")
public final class SearchUIImpl 
    extends JDialog
    implements  ActionListener, WindowListener, PropertyChangeListener,
    			SearchUI { 
    
    private JButton         fSearchButton   = null;
    private JTextField      fNameList       = null;
    private JLabel          fNameLabel      = null;
    private StyledTextArea  fLogArea        = null; 
    private Frame           fParentFrame    = null;
    private boolean         fFirstTime    = true;

    private boolean     fPreserveZeroLocation = false;
    
    private PropertiesDB    fPropertiesDB   = PropertiesDB.getInstance();
    private CursorControl   fCursorControl  = CursorControl.instance();
    private MiscProtocol    fMiscProtocol   = MiscProtocol.getInstance();
	private BGColorManager	fBGColorManager	= BGColorManager.getInstance();
    
    public SearchUIImpl(
        Frame               parentFrame) {
        super(parentFrame, StringDefs.SEARCH, false);
        
        Container   contentPane = getContentPane();
        fParentFrame            = parentFrame; 
        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Beans
        //
        fPropertiesDB.addPropertyChangeListener(this);
        LFManager.getInstance().add(this);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        contentPane.setLayout(gridBag);
        //
        // Deliver Button
        //
        fSearchButton      = new JButton(StringDefs.SEARCH);
        fSearchButton.addActionListener(this);
        fCursorControl.addCursorComponent(fSearchButton);
        fCursorControl.addEnablableComponent(fSearchButton);
        constraints.fill 			= GridBagConstraints.NONE;
        constraints.anchor 			= GridBagConstraints.WEST;
        constraints.gridwidth 		= 1;
        constraints.weightx 		= 0.0;
        constraints.weighty 		= 0.0;
		constraints.insets.top		= 2;
		constraints.insets.left		= 2;
		constraints.insets.bottom	= 2;
		constraints.insets.right	= 2; // (2,2,2,2)
        gridBag.setConstraints(fSearchButton, constraints);
        contentPane.add(fSearchButton);
        
        //
        // Name List
        //
        fNameLabel    = new JLabel(StringDefs.NAME_C);
        fCursorControl.addCursorComponent(fNameLabel);
        fCursorControl.addEnablableComponent(fNameLabel);
		constraints.insets.left		= 0;
		constraints.insets.right	= 0; // (2,0,2,0) 
        gridBag.setConstraints(fNameLabel, constraints);
        contentPane.add(fNameLabel); 
        
        
        fNameList     = new JTextField(1 /* Context.kWindowWidth - 10 */);
		fBGColorManager.add(fNameList);
        fCursorControl.addCursorComponent(fNameList);
        fCursorControl.addEnablableComponent(fNameList);
        constraints.fill 			= GridBagConstraints.HORIZONTAL;
		constraints.gridwidth   	= GridBagConstraints.REMAINDER;
        constraints.weightx 		= 1.0;
		constraints.insets.right	= 2; // (2,0,2,2) 
        gridBag.setConstraints(fNameList, constraints);
        contentPane.add(fNameList);
        fNameList.addActionListener(this);
        
        //
        // Log Area
        //
        fLogArea = new StyledTextArea(false);
		fBGColorManager.add(fLogArea);
        fCursorControl.addCursorComponent(fLogArea);
        fLogArea.setEditable(false);
        constraints.fill 			= GridBagConstraints.BOTH;
        constraints.anchor 			= GridBagConstraints.WEST;
        constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.weightx 		= 1.0;
        constraints.weighty 		= 1.0;
		constraints.insets.top		= 0;
		constraints.insets.left		= 0;
		constraints.insets.bottom	= 0;
		constraints.insets.right	= 0;
        gridBag.setConstraints(fLogArea, constraints);
        contentPane.add(fLogArea); 
        
        
        setFonts();
        Dimension   parentSize = fParentFrame.getSize();
        
        setSize(new Dimension(parentSize.width, (parentSize.height * 2)/5));
        fCursorControl.addCursorComponent(this);
        }
    
    public void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
            
        fSearchButton.setFont(font);
        fNameLabel.setFont(font);
        fNameList.setFont(font);
        fLogArea.setTextFont(font);
        }
    
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) 
            setFonts();
        }
    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) { setVisible(false); } 
    public void windowDeiconified(WindowEvent event) {}
    public void windowIconified(WindowEvent event) {}  
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
    public void windowOpened(WindowEvent event) {}
    public void windowClosing(WindowEvent event) {setVisible(false);}
    
    // ===========================
    // ActionListener
    // ===========================
    public void actionPerformed(ActionEvent event) {
        Object  target = event.getSource();
        
        if (target == fSearchButton || target == fNameList) {
            search();
            }
        }

    // =============================
    // Funtions for Linked List
    // =============================
    public void setVisible(boolean visible) {
        if (visible) {
            ComponentUtil.overlapComponents(this, fParentFrame, 32, fPreserveZeroLocation);
            fPreserveZeroLocation = true;
            }

        super.setVisible(visible);
        //
        // Make sure that the log is scrolled down to the last position.
        // Note that this selection must be done after the window 
        // is visible. Because making this window visible will
        // scroll this window up to the first position.
        //
        fLogArea.scrollToBottom();
        //
        // Move the focus into the name field [V1.67]
        //
        if (visible)
            fNameList.requestFocus();
        }

    // ========================
    // Log Area
    // ========================
    private synchronized void appendLog(final String log) {
		if (log == null)
			return;

	   	Runnable methodBody = new Runnable() {
			public void run() {
            	fLogArea.appendText(log);
            	fLogArea.scrollToBottom();
				}
            };
		SwingUtilities.invokeLater(methodBody);
        }
        
    public synchronized void appendLog(final String log, final Color color) {
		if (log == null)
			return;

		Runnable methodBody = new Runnable() {
			public void run() {
            	Color   savedColor = fLogArea.getTextColor();
            
            	fLogArea.setTextColor(color);
            	fLogArea.appendText(log);
            	fLogArea.setTextColor(savedColor);
            	fLogArea.scrollToBottom();
            	}
			};
		SwingUtilities.invokeLater(methodBody);
        }
        
  
    // ========================
    // Search
    // ========================
    private class DoneThread extends Thread {
       
        public DoneThread() {}
        
        public void run() {
            TimerUtil.sleep(5 * 1000); // 5 seconds
		  	SwingUtilities.invokeLater(new Runnable() {
		  		public void run() {
		  			fCursorControl.setBusy(false);
		  		}
		  	});
            }
        }
        
    private void search() {
        String[]    names = StringUtil.parseToArray(fNameList.getText());
        
        if (names.length == 0)
        	return;
       
        fCursorControl.setBusy(true);
        
        if (fFirstTime) 
            fFirstTime = false;
        else 
            appendLog("\n");
            
        appendLog(StringDefs.SEARCH + " : ");
        appendLog(fNameList.getText() + "\n", Color.red);
        
        for (String name: names)
            fMiscProtocol.lookForUser(fPropertiesDB.getUserName(), name);

        new DoneThread().start();
        }

    }
// Log
// 1.65 : 27-Sep-97 Y.Shibata   created.
// 1.75 :  9-Nov-97 Y.Shibata   preserve the zero location.
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 1.95 : 11-Jul-98 Y.Shibata   modified to use msgtool.util.TimerUtil
// 2.14 : 13-Feb-99	Y.Shibata	modified for Thread-safe by using SwingUtilities
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.35 : 24-Oct-99	Y.Shibata	SearchDialog -> SearchUIImpl

