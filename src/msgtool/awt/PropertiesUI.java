// File: PropertiesUI.java - last edit:
// Yoshiki Shibata 30-Dec-2002

// Copyright (c) 1996 - 1999, 2002 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import msgtool.db.PropertiesDB;
import msgtool.util.ColorMap;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class PropertiesUI 
    extends Dialog 
    implements ActionListener, WindowListener, ItemListener {
	private Button      fSetButton              = null;
    private Button      fCancelButton           = null;
    private TextField   fUserName               = null;
    private Checkbox    fActivateOnReception    = null;
    private Choice      fActivateWindow         = null;
    private Checkbox    fBeepOnReception        = null;
    private Choice      fDeliverKey             = null;
    private Checkbox    fSaveMessages           = null;
    private Choice      fNoOfLogFiles           = null;
        
    private Choice      fFontName               = null;
    private String[]    fFontNameValues         = null;
        
    private Choice           fFontStyle         = null;
    private int[]            fFontStyleValues   = null;
    private static final int kNoOfFontStyles    = 4;
        
    private Choice           fFontSize          = null;
    private static final int kBaseOfFontSize    = 9;
    private static final int kMaxFontSize       = 18;
    
    private Choice      fTextBackground         = null;
        
    private Frame           fParentFrame        = null;
    
    private PropertiesDB    fPropertiesDB       = PropertiesDB.getInstance();
        
    private void SetLabel(
        GridBagLayout       gridBag,
        GridBagConstraints  c,
        String              name) {
        Label label = new Label(name);
            
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
		c.insets.top	= 2;
		c.insets.left	= 2;
		c.insets.bottom	= 0;
		c.insets.right	= 0; // (2, 2, 0, 0)
        gridBag.setConstraints(label, c);
        add(label);
		c.insets.left	= 0;
		c.insets.right	= 2; // (2, 0, 0, 2)
        }
 
    public PropertiesUI(
        Frame           parentFrame,
        String          title) 
        {
        super(parentFrame, title, false);
        fParentFrame = parentFrame;
        //
        // Register as WindowListener
        //
        addWindowListener(this);
        
        GridBagLayout gridBag= new GridBagLayout();
        setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();

        //
        // User Name
        //
        SetLabel(gridBag, c, StringDefs.USER_NAME_C);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        fUserName = new TextField(10);
        gridBag.setConstraints(fUserName, c);
        add(fUserName);
        //
        // Activate On Reception
        //
        SetLabel(gridBag, c, StringDefs.ACTIVE_ON_RECEPTION_C);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 1.0;
        fActivateOnReception = new Checkbox();
        fActivateOnReception.addItemListener(this);
        gridBag.setConstraints(fActivateOnReception, c);
        add(fActivateOnReception);
    
        fActivateWindow = new Choice();
        fActivateWindow.addItem(StringDefs.MESSAGING_DIALOG);
        fActivateWindow.addItem(StringDefs.RECEPTION_DIALOG);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(fActivateWindow, c);
        add(fActivateWindow);
        // 
        // Beep On Reception
        //
        SetLabel(gridBag, c, StringDefs.BEEP_ON_RECEPTION);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        fBeepOnReception = new Checkbox();
        gridBag.setConstraints(fBeepOnReception, c);
        add(fBeepOnReception);
        //
        // Deliver Key
        //
        SetLabel(gridBag, c, StringDefs.DELIVER_KEY_C);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        fDeliverKey = new Choice();
        fDeliverKey.addItem("F1");
        fDeliverKey.addItem("F2");
        fDeliverKey.addItem("F3");
        fDeliverKey.addItem("F4");
        fDeliverKey.addItem("F5");
        fDeliverKey.addItem("F6");
        fDeliverKey.addItem("F7");
        fDeliverKey.addItem("F8");
        fDeliverKey.addItem("F9");
        fDeliverKey.addItem("F10");
        fDeliverKey.addItem("F11");
        fDeliverKey.addItem("F12");
        gridBag.setConstraints(fDeliverKey, c);
        add(fDeliverKey);
        //
        // Save Messages
        //
        SetLabel(gridBag, c, StringDefs.LOG_MESSAGES_C);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.0;
        c.weighty = 1.0;
        fSaveMessages = new Checkbox();
        fSaveMessages.addItemListener(this);
        gridBag.setConstraints(fSaveMessages, c);
        add(fSaveMessages);
         
        SetLabel(gridBag, c, StringDefs.MAX_LOG_FILES_C);
        fNoOfLogFiles = new Choice();
        fNoOfLogFiles.addItem("1");
        fNoOfLogFiles.addItem("2");
        fNoOfLogFiles.addItem("3");
        fNoOfLogFiles.addItem("4");
        fNoOfLogFiles.addItem("5");
        fNoOfLogFiles.addItem("6");
        fNoOfLogFiles.addItem("7");
        fNoOfLogFiles.addItem("8");
        fNoOfLogFiles.addItem("9");
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(fNoOfLogFiles, c);
        add(fNoOfLogFiles);        
        //
        // Font List
        //
        SetLabel(gridBag, c, StringDefs.FONT_NAME_C);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        fFontName = new Choice();

	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = ge.getAvailableFontFamilyNames();

        fFontNameValues = new String[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            fFontName.addItem(fonts[i]);
            fFontNameValues[i] = fonts[i];
            }
        gridBag.setConstraints(fFontName, c);
        add(fFontName); 
        //
        // Font Style
        //
        SetLabel(gridBag, c, StringDefs.FONT_STYLE_C);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        fFontStyle = new Choice();
        fFontStyleValues = new int[kNoOfFontStyles];
        
        fFontStyle.addItem(StringDefs.PLAIN);
        fFontStyleValues[0] = Font.PLAIN;
        fFontStyle.addItem(StringDefs.BOLD);
        fFontStyleValues[1] = Font.BOLD;
        fFontStyle.addItem(StringDefs.ITALIC);
        fFontStyleValues[2] = Font.ITALIC;
        fFontStyle.addItem(StringDefs.BOLD_ITALIC);
        fFontStyleValues[3] = Font.BOLD | Font.ITALIC;
        
        gridBag.setConstraints(fFontStyle, c);
        add(fFontStyle);
        //
        // Font Size
        //
        SetLabel(gridBag, c, StringDefs.FONT_SIZE_C);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        fFontSize = new Choice();
        
        fFontSize.addItem("9");
        fFontSize.addItem("10");
        fFontSize.addItem("11");
        fFontSize.addItem("12");
        fFontSize.addItem("13");
        fFontSize.addItem("14");
        fFontSize.addItem("15");
        fFontSize.addItem("16");
        fFontSize.addItem("17");
        fFontSize.addItem("18");
    
        gridBag.setConstraints(fFontSize, c);
        add(fFontSize);
        //
        // Text Background Color
        //
        SetLabel(gridBag, c, StringDefs.TEXT_BACKGROUND_C);
        
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        
        String[] colorNames = ColorMap.getColorNames();
        fTextBackground = new Choice();
        for (int i = 0; i < colorNames.length; i++)
            fTextBackground.addItem(colorNames[i]);
        
        gridBag.setConstraints(fTextBackground, c);
        add(fTextBackground);    
        //
        // Cancle/Set buttons
        //
        Panel panel = new Panel();

        fCancelButton  = new Button(StringDefs.CANCEL);
        fCancelButton.addActionListener(this);
        fSetButton      = new Button(StringDefs.SET);
        fSetButton.addActionListener(this);
        
        panel.add(fSetButton);
        panel.add(fCancelButton);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridBag.setConstraints(panel, c);
        add(panel);

        pack();
        setResizable(false);
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
    public void windowClosing(WindowEvent event) { setVisible(false); }

    // ================================
    // ActionListener
    // ================================
    public void actionPerformed(ActionEvent event) {
        Object target = event.getSource();
      
        if (target == fSetButton) {        
            String    userName = fUserName.getText();
            if (userName != null && userName.length() > 0)
                fPropertiesDB.setUserName(userName);

            fPropertiesDB.setActivateOnReception(
                fActivateOnReception.getState());
            fPropertiesDB.setActivateWindowChoice(
                fActivateWindow.getSelectedIndex());
            fPropertiesDB.setBeepOnReception(
                fBeepOnReception.getState());
            fPropertiesDB.setDeliverKey(
                fDeliverKey.getSelectedIndex());
            fPropertiesDB.setSaveMessages(
                fSaveMessages.getState());
            fPropertiesDB.setNoOfMessageFiles(
                fNoOfLogFiles.getSelectedIndex() + 1);
            fPropertiesDB.setFontName(
                fFontName.getSelectedItem());
            fPropertiesDB.setFontStyle(
                fFontStyleValues[fFontStyle.getSelectedIndex()]);
            fPropertiesDB.setFontSize(
                fFontSize.getSelectedIndex() + kBaseOfFontSize);
            fPropertiesDB.setTextBackground(
                (String) fTextBackground.getSelectedItem());
            fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
            setVisible(false);
            }
        else if (target == fCancelButton) {
            setVisible(false);
            }
        }

    // ================================
    // ItemListener
    // ================================
    public void itemStateChanged(ItemEvent event) {
        Object item = event.getItemSelectable();
        
        if (item == fActivateOnReception)
            fActivateWindow.setEnabled(fActivateOnReception.getState());
        else if (item == fSaveMessages)
            fNoOfLogFiles.setEnabled(fSaveMessages.getState());
        }

    // ==============================
    // Show 
    // ==============================
    public void setVisible(
        boolean     visible) {
        if (!visible) {
            super.setVisible(visible);
            return;
            }
                   
        boolean activateOnReception = fPropertiesDB.isActivateOnReception();
    
        fUserName.setText(fPropertiesDB.getUserName());
        fActivateOnReception.setState(activateOnReception);
        fActivateWindow.setEnabled(activateOnReception);
        fBeepOnReception.setState(fPropertiesDB.isBeepOnReception());
        try {
            fDeliverKey.select(fPropertiesDB.getDeliverKey());
            fActivateWindow.select(fPropertiesDB.getActivateWindowChoice());
            
            boolean saveMessages = fPropertiesDB.isSaveMessages();
            fSaveMessages.setState(saveMessages);
            fNoOfLogFiles.setEnabled(saveMessages);
            int     noOfMessageFiles = fPropertiesDB.getNoOfMessageFiles();
            fNoOfLogFiles.select(noOfMessageFiles - 1);      
            //
            // Font Name
            //
            String fontName = fPropertiesDB.getFontName();
                        
            int fontIndex = 0; // in case of not found.
            for (int i = 0; i < fFontNameValues.length; i++) {
                if (fFontNameValues[i].equals(fontName)) {
                    fontIndex = i;
                    break;
                    }
                }
            fFontName.select(fontIndex);
            //
            // Font Style
            //
            int fontStyle = fPropertiesDB.getFontStyle();
            for (int i = 0; i < kNoOfFontStyles; i++)
                if (fFontStyleValues[i] == fontStyle) {
                    fFontStyle.select(i);
                    break;
                    }
            //
            // Font Size
            //
            int fontSize = fPropertiesDB.getFontSize();
            if (fontSize < kBaseOfFontSize)
                fontSize = kBaseOfFontSize;
            else if (fontSize > kMaxFontSize)
                fontSize = kMaxFontSize;
            fFontSize.select(fontSize - kBaseOfFontSize);
            //
            // TextBackground
            //
            String textBackground = ColorMap.getColorName(
                        ColorMap.getColorByName(fPropertiesDB.getTextBackground()));
            String[] colorNames = ColorMap.getColorNames();
            
            for (int i = 0; i < colorNames.length; i++)
                if (textBackground.equals(colorNames[i])) {
                    fTextBackground.select(i);
                    break;
                    } 
            }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            }
        ComponentUtil.centerComponent(this, fParentFrame.getLocation(), fParentFrame.getSize());
        super.setVisible(true);
        }
    }

// LOG
//         1-Sep-96 Y.Shibata   created
//        15-Feb-97 Y.Shibata   added "Beep On Reception"
// 1.02 : 22-Feb-97 Y.Shibata   fixed the location of the choice of "Activate On Reception"
// 1.20 : 29-Mar-97 Y.Shibata   chagned show() to setVisible()
//                              non-resizable window.
// 1.30 : 17-Apr-97 Y.Shibata   deleted code for TimeZone
// 1.35 : 11-Jul-97 Y.Shibata   centering this window on top of the main window.
// 1.37 : 17-Jul-97 Y.Shibata   changed locations of buttons.
// 1.83 : 20-Dec-97 Y.Shibata   modification for JDK1.2beta2:
//                                import java.awt.List (java.util.List is added to JDK1.2)
// 1.90 : 14-Mar-98 Y.Shibata   added TextBackground
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.ComponentUtil
// 1.95 : 16-Jul-98 Y.Shibata   modified to use msgtool.db.PropertiesDB, msgtool.util.StringDefs
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.34 :  9-Sep-99	Y.Shibata	changed the way to check JDK versions
// 2.35 : 24-Oct-99	Y.Shibata	PropertiesDialog -> PropertiesUI
// 2.50 : 30-Dec-02 Y.Shibata   got rid of 1.1 compatible code
