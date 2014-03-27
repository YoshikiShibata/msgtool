/*
 * File: DedicatedUIImpl.java - last edit:
 * Yoshiki Shibata 23-Mar-2014
 *
 * Copyright (c) 1997 - 1999, 2014 by Yoshiki Shibata. All rights reserved.
 */
package msgtool.awt;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
public final class DedicatedUIImpl extends Dialog
        implements DedicatedUI {

    private Button fDeliverButton = null;
    private TextField fToList = null;
    private Button fClearLogButton = null;
    private Label fToLabel = null;
    private InputTextArea fInputArea = null;
    private PopupMenu fRecipientHintsPopup = null;

    private boolean fActivated = false;

    private Frame fParentFrame = null;
    private DedicatedModel fModel = null;

    private MenuItem fMenuItem = null;

    private final ShiftKeyAdapter fShiftKeyAdapter = new ShiftKeyAdapter();

    private boolean fPreserveZeroLocation = false;

    private final PropertiesDB fPropertiesDB = PropertiesDB.getInstance();
    private final CursorControl fCursorControl = CursorControl.instance();
    private final RecipientHintsDB fRecipientHintsDB = RecipientHintsDB.getInstance();
    private final AddressDB fAddressDB = AddressDB.instance();
    private final BGColorManager fBGColorManager = BGColorManager.getInstance();
    private final FontManager fFontManager = FontManager.getInstance();
    
    private final ActionListener fActionListener;

    private LogArea fLogArea = null;

    public DedicatedUIImpl(
            Frame parentFrame,
            DedicatedModel model,
            boolean deliverEnabled) {
        super(parentFrame, model.getTitle(), false);

        fParentFrame = parentFrame;
        fModel = model;
        fModel.addPropertyChangeListener(event -> {
            setTitle(fModel.getTitle());
            if (fMenuItem != null) {
                fMenuItem.setLabel(fModel.getTitle());
            }
        });

        fRecipientHintsPopup = new PopupMenu(StringDefs.RECIPIENT);
        fActivated = false;
        //
        // Register as WindowListener
        //
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                setVisible(false);
            }
        });
        //
        // Window Layouts
        //
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        setLayout(gridBag);

        fActionListener = event -> {
            Object target = event.getSource();

            if (target == fDeliverButton) {
                fModel.deliver(fModel.getSenderName() + ", " + fToList.getText(), fInputArea, this);
            } else if (target == fClearLogButton) {
                fLogArea.clear();
            } else if (target instanceof MenuItem) {
                MenuItem item = (MenuItem) target;
                String menuLabel = item.getLabel();
                String expandedHint = fRecipientHintsDB.getExpandedRecipients(menuLabel);

                Util.recipientHintSelected(expandedHint, fToList, fShiftKeyAdapter.isShiftKeyPressed());
            }
        };
        //
        // Deliver Button
        //
        fDeliverButton = new Button(StringDefs.DELIVER);
        fDeliverButton.addActionListener(fActionListener);
        fDeliverButton.setEnabled(deliverEnabled);
        fCursorControl.addCursorComponent(fDeliverButton);
        fCursorControl.addEnablableComponent(fDeliverButton);
        fFontManager.addComponent(fDeliverButton);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.insets.top = 2;
        constraints.insets.left = 2;
        constraints.insets.bottom = 2;
        constraints.insets.right = 2; // (2, 2, 2, 2)
        gridBag.setConstraints(fDeliverButton, constraints);
        add(fDeliverButton);
        //
        // To List
        //
        fToLabel = new Label(StringDefs.ADDITIONAL_TO_C);
        fCursorControl.addCursorComponent(fToLabel);
        fCursorControl.addEnablableComponent(fToLabel);
        fFontManager.addComponent(fToLabel);

        constraints.insets.left = 0;
        constraints.insets.right = 0; // (2, 0, 2, 0)
        gridBag.setConstraints(fToLabel, constraints);

        add(fToLabel);
        fToLabel.addMouseListener(new PopupMenuAdapter(fToLabel, fRecipientHintsPopup));
        updateRecipientHintsPopup();

        fToList = new XTextField(1 /* Context.kWindowWidth - 24 */);
        fBGColorManager.add(fToList);
        fCursorControl.addCursorComponent(fToList);
        fCursorControl.addEnablableComponent(fToList);
        fFontManager.addComponent(fToList);

        fToList.addKeyListener(fShiftKeyAdapter);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets.right = 2; // (2, 0, 2, 2)
        gridBag.setConstraints(fToList, constraints);
        add(fToList);

        //
        // Clear Log Button
        //
        fClearLogButton = new Button(StringDefs.CLEAR_LOG);
        fClearLogButton.addActionListener(fActionListener);
        fCursorControl.addCursorComponent(fClearLogButton);
        fCursorControl.addEnablableComponent(fClearLogButton);
        fFontManager.addComponent(fClearLogButton);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 0.0;
        gridBag.setConstraints(fClearLogButton, constraints);
        add(fClearLogButton);

        //
        // Input area
        //
        fInputArea = new InputTextArea(5, Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);
        fBGColorManager.add(fInputArea);
        fInputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int keyCode = keyEvent.getKeyCode();
                if (KeyUtil.isDeliverKey(keyCode)) {
                    fModel.deliver(fModel.getSenderName() + ", " + fToList.getText(), fInputArea, DedicatedUIImpl.this);
                }
            }
        });
        fCursorControl.addCursorComponent(fInputArea);
        fCursorControl.addEnablableComponent(fInputArea);
        fFontManager.addComponent(fInputArea);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets.top = 0;
        constraints.insets.left = 0;
        constraints.insets.bottom = 0;
        constraints.insets.right = 0;
        gridBag.setConstraints(fInputArea, constraints);
        add(fInputArea);
        //
        // Log Area
        //
        TextArea logTextArea = new TextArea("", 15, Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);
        fBGColorManager.add(logTextArea);
        fCursorControl.addCursorComponent(logTextArea);
        logTextArea.setEditable(false);
        fFontManager.addComponent(logTextArea);

        gridBag.setConstraints(logTextArea, constraints);
        add(logTextArea);

        Dimension size = fPropertiesDB.getSize(fModel.getSenderIP());
        if (size != null) {
            setSize(size);
        } else {
            pack();
        }

        fFontManager.addContainer(this);

        Point location = fPropertiesDB.getLocation(fModel.getSenderIP());
        if (location != null) {
            setLocation(location);
            fPreserveZeroLocation = true;
        }
        fCursorControl.addCursorComponent(this);

        fLogArea = new LogAreaImpl(fParentFrame, logTextArea);
    }

    @Override
    public void setFont(Font font) {
        Util.setFontsToMenu(fRecipientHintsPopup, font);
        //
        // Note that when SetFonts() is called from the constructor, 
        // fMenuItem is still null. A value will be given immediately after
        // the contructor is called.
        //
        if (fMenuItem != null) {
            fMenuItem.setFont(font);
        }
    }

    @Override
    public void updateRecipientHintsPopup() {
        Util.updateHintsMenu(fRecipientHintsPopup,
                fRecipientHintsDB.getDB(),
                fAddressDB.getHintedAddressDB(), fActionListener);
        Util.setFontsToMenu(fRecipientHintsPopup, Context.getFont());
    }

    @Override
    public void setDeliverEnabled(boolean state) {
        fDeliverButton.setEnabled(state);
    }

    // =============================
    // Funtions for Linked List
    // =============================
    @Override
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
        // Make sure that the log is scrolled down to the last position.
        // Note that this selection must be done after the window 
        // is visible. Because making this window visible will
        // scroll this window up to the first position. [V1.65]
        //
        fLogArea.scrollDownToEnd();
    }

    public boolean isActivated() {
        return fActivated;
    }

    // ========================
    // Log Area
    // ========================
    @Override
    public void appendLog(String log) {
        fLogArea.appendText(log);
    }

    @Override
    public void appendLog(String log, Color color) {
        appendLog(log); // Color is not supported.
    }

    // ===============================
    // Sender Information: IP and Name
    // ===============================
    @Override
    public String getSenderIP() {
        return (fModel.getSenderIP());
    }

    // ============================
    // Set MenuItem for this window
    // ============================
    @Override
    public void setMenuItem(Object menuItem) {
        fMenuItem = (MenuItem) menuItem;
    }

    // ============================
    // Set Additional To: list
    // ============================
    @Override
    public void setAdditionalRecipients(String recipients) {
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
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 1.95 : 16-JUl-98 Y.Shibata   modified to use msgtool.db.*
// 2.15 : 27-Feb-99	Y.Shibata	used PopupMenuAdapter class
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.24 :  9-May-99	Y.Shibata	used XTextField for Dnd
// 2.33 : 12-Aug-99	Y.Shibata	reimplemented Log_AppendText with LogArea [Refactoring]
// 2.35 : 24-Oct-99	Y.Shibata	DedicatedDialog -> DedicatedUI
//		  30-Oct-99	Y.Shibata	DedicatedUI -> DedicatedUIImpl
// 2.60 : 23-Mar-14 Y.Shibata   refactored with Java 8 (Lambda expression)

