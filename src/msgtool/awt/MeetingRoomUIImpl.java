// File: MeetingRoomUIImpl.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.FontManager;
import msgtool.common.KeyUtil;
import msgtool.db.PropertiesDB;
import msgtool.log.LogArea;
import msgtool.log.LogMeeting;
import msgtool.log.Logging;
import msgtool.protocol.MeetingProtocol;
import msgtool.ui.MeetingRoomUI;
import msgtool.ui.ParticipantsUI;
import msgtool.ui.UIFactory;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;
import msgtool.util.StringUtil;

@SuppressWarnings("serial")
public class MeetingRoomUIImpl 
    extends Frame12
    implements  ActionListener, WindowListener, 
                PropertyChangeListener, MeetingRoomUI { 
	private Button          fDeliverButton      = null;
    private Button          fParticipantsButton = null;
    private Button          fRoomButton         = null;
    private Button          fClearLogButton     = null;
    private TextArea        fInputArea          = null;
    private TextArea        fLogTextArea            = null;
    
    private Frame       fParentFrame        = null;
    private String      fInternalRoomName   = null;
    private String      fExternalRoomName   = null;
    private MeetingRoomListUI   fListUI = null;
    
    private MenuItem    fMenuItem       = null;
    private boolean     fInRoom         = false;
    private boolean     fSavedInRoom    = false;

    private boolean     fIconified      = false;
    
    private ParticipantsUI fParticipantsUI  = null;
    private boolean     fSavedVisibleOfDialog       = false;
    
    private Menu        	fTopMenu            = null;
    private Menu        	fMeetingMessagesMenu= null;
	private Logging			fLogging			= null;
	private LogArea	fLogArea		= null;
	private LogMeeting		fLogMeeting			= null;
    
    private NoticeUI    fTooLongMessageUI = null;

    private boolean fPreserveZeroLocation   = false;
    
    private PropertiesDB    fPropertiesDB       = PropertiesDB.getInstance();
    private MeetingProtocol fMeetingProtocol    = MeetingProtocol.getInstance();
    
    public MeetingRoomUIImpl(
        Frame               parentFrame,
        String              internalRoomName,
        String              externalRoomName,
        Menu    			topMenu,
        MeetingRoomListUI   listUI,
		UIFactory<MenuItem>			uiFactory) {
        super(externalRoomName);
        
        fParentFrame        = parentFrame;
        fInternalRoomName   = internalRoomName;
        fExternalRoomName   = externalRoomName; 
        fTopMenu            = topMenu;
        fListUI          	= listUI;
        fTooLongMessageUI   = new NoticeUI(this, StringDefs.MESSAGE_TOO_LONG);
        
        listUI.addMeetingRoom(externalRoomName);
        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Beans
        //
        fPropertiesDB.addPropertyChangeListener(this);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        setLayout(gridBag);

		FontManager	fontManager = FontManager.getInstance();
        //
        // Deliver Button
        //
        fDeliverButton      = new Button(StringDefs.DELIVER);
        fDeliverButton.addActionListener(this);
        fDeliverButton.setEnabled(false);
		fontManager.addComponent(fDeliverButton);
        
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
        add(fDeliverButton);
        //
        // Participants Button
        //
        fParticipantsButton = new Button(StringDefs.PARTICIPANTS);
        fParticipantsButton.addActionListener(this);
		fontManager.addComponent(fParticipantsButton);

		constraints.insets.left		= 0; // (2,0,2,2) 
        gridBag.setConstraints(fParticipantsButton, constraints);
        add(fParticipantsButton);
        //
        // Leave Room button
        //
        fRoomButton = new Button(StringDefs.JOIN);
        fRoomButton.addActionListener(this);
		fontManager.addComponent(fRoomButton);
        gridBag.setConstraints(fRoomButton, constraints);
        add(fRoomButton); 
        
        //
        // Clear Log button
        //
        fClearLogButton = new Button(StringDefs.CLEAR_LOG);
        fClearLogButton.addActionListener(this);
  	    fontManager.addComponent(fClearLogButton);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.EAST;
        gridBag.setConstraints(fClearLogButton, constraints);
        add(fClearLogButton); 
        
        //
        // Input area
        //
        fInputArea = new TextArea("", 5,  Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);
		fontManager.addComponent(fInputArea);
		BGColorManager.getInstance().add(fInputArea);
        fInputArea.setEnabled(false); 
        fInputArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) { 
        		int keyCode = keyEvent.getKeyCode();
        		if (KeyUtil.isDeliverKey(keyCode))
            		sendMessage();
        	}
		});
        
        constraints.fill 			= GridBagConstraints.BOTH;
        constraints.anchor 			= GridBagConstraints.WEST;
        constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.weightx 		= 1.0;
        constraints.weighty 		= 1.0;
		constraints.insets.top		= 0;
		constraints.insets.left		= 0;
		constraints.insets.bottom	= 0;
		constraints.insets.right	= 0; // (0,0,0,0)
        gridBag.setConstraints(fInputArea, constraints);
        add(fInputArea);
        //
        // Log Area
        //
        fLogTextArea = new TextArea("", 15, Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);
		fontManager.addComponent(fLogTextArea);
		BGColorManager.getInstance().add(fLogTextArea);
        fLogTextArea.setEditable(false);
        gridBag.setConstraints(fLogTextArea, constraints);
        add(fLogTextArea); 

        setFonts();

        //
        // Restore the size and location [V1.75]
        //
        Point   location = fPropertiesDB.getLocation(StringUtil.space2UnderScore(fInternalRoomName));
        if (location != null) {
            setLocation(location);
            fPreserveZeroLocation = true;
            }

        Dimension size = fPropertiesDB.getSize(StringUtil.space2UnderScore(fInternalRoomName));
        if (size != null)
            setSize(size);
        else
            pack();
        //
        // Participants Dialog
        //
        fParticipantsUI = uiFactory.createParticipantsUI(this, fExternalRoomName);
        //
        // Logging messages
        //
        fMeetingMessagesMenu = new Menu(fExternalRoomName);
        fMeetingMessagesMenu.setFont(Context.getFont());
        fTopMenu.add(fMeetingMessagesMenu);
        fMeetingMessagesMenu.setEnabled(false);

		fLogging = new Logging("MessagingTool.mt." + fExternalRoomName + ".log",
							   new LoggingGUIImpl(this, fMeetingMessagesMenu, fTopMenu),
							   fExternalRoomName);
		fLogArea	= new LogAreaImpl(this, fLogTextArea, fLogging);
		fLogMeeting	= new LogMeeting(fInternalRoomName, fLogArea);
        }
   
    public void setEnabled(boolean enable) {
        if (enable) 
            fListUI.addMeetingRoom(fExternalRoomName);
        else
            fListUI.removeMeetingRoom(fExternalRoomName);
            
        super.setEnabled(enable);
        }
    
    public synchronized void setNotInOffice(boolean  notInOffice) {
        if (notInOffice) {
            fSavedInRoom    = fInRoom;
            if (fInRoom)
                leaveRoom();
            }
        else {
            if (fSavedInRoom)
                joinRoom();
            }
        }    
        
    public void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;

        if (fMenuItem != null) {
            fMenuItem.setFont(font);
            }
        }
    
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) { 
            setFonts();
            validate();
			fLogging.update();		
            }
        }
    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) { setVisible(false);} 
    public void windowDeiconified(WindowEvent event) {
        fIconified = false;
        setTitle(fExternalRoomName);
        fListUI.setMessageWaiting(fExternalRoomName, false);
        }
    public void windowIconified(WindowEvent event) { fIconified = true;}
    public void windowActivated(WindowEvent event) {}  
    public void windowDeactivated(WindowEvent event) {}
    public void windowOpened(WindowEvent event) {}
    public void windowClosing(WindowEvent event) {
        setVisible(false);
        fIconified = false;
        }
    // ===========================
    // ActionListener
    // ===========================
    public synchronized void actionPerformed(ActionEvent event) {
        Object  target = event.getSource();
        
        if (target == fDeliverButton) {
            sendMessage();
            }
        else if (target == fParticipantsButton) {
            fParticipantsUI.clearList();
            fParticipantsUI.setVisible(true);
            fMeetingProtocol.participants(fInternalRoomName);
            }                                                          
        else if (target == fRoomButton) {
            if (fInRoom)
                leaveRoom();
            else
                joinRoom();
            }
        else if (target == fClearLogButton)
 	    	fLogArea.clear();
        }
    // =============================
    // Funtions for Linked List
    // =============================
    public void setVisible(boolean visible) {
        if (visible) {
            ComponentUtil.overlapComponents(this, fParentFrame, 32, fPreserveZeroLocation);
            fPreserveZeroLocation = true;
            }
        else {
            fSavedVisibleOfDialog = fParticipantsUI.isVisible();
            fParticipantsUI.setVisible(false);
            //
            // Save the location and size
            //
            fPropertiesDB.setLocation(StringUtil.space2UnderScore(fInternalRoomName), getLocation());
            fPropertiesDB.setSize(StringUtil.space2UnderScore(fInternalRoomName), getSize());
            }
        
        super.setVisible(visible);
        if (visible) {
            //
            // Make sure that the log is scrolled down to the last position.
            // Note that this selection must be done after the window 
            // is visible. Because making this window visible will
            // scroll this window up to the first position. [V1.65]
            //
			fLogArea.scrollDownToEnd();
            //
            // If the parcicipants dialog was visible before, then 
            // make it visible only if this main frame is not iconified. 
            // If this main frame is iconified, showing the dialog prevent
            // the main frame from being deiconified.[V1.69]
            //
            if (fSavedVisibleOfDialog && !fIconified)
                fParticipantsUI.setVisible(true);
            //
            // If not iconified, then clear message waiting.
            //
            if (!fIconified)
                fListUI.setMessageWaiting(fExternalRoomName, false);
            }
        }
    // ========================
    // Log Area
    // ========================
	public void appendLogText(
		boolean notifyMessage, 
		String text, 
		String sourceIP) {
		appendLogText(notifyMessage, text);
	}

    public synchronized void appendLogText(
        boolean notifyMessage,
        String  text) {
        if (text != null) {
			fLogArea.appendText(text);
            //
            // Some kind of messages such as Join will not be notified. [V1.69]
            // 
            if (notifyMessage) {
                //
                // Change title if iconified.
                //
                if (fIconified) {
                    setTitle(StringDefs.MESSAGE_WAITING);
                    }
                //
                // Set Message waiting if either invisible or iconified.
                // 
                if (!isVisible() || fIconified)
                    fListUI.setMessageWaiting(fExternalRoomName, true);
                //
                // Beep On Reception
                //
                if (fPropertiesDB.isBeepOnReception() && fInRoom)
                    getToolkit().beep();
                //
                // Appent this log to the fLogMeeting so that only messages
                // will be appended to the fLogMeeting. [V1.75]
                //
                fLogMeeting.append(text);
                }
            }
        }
    // ============================
    // clear saved log
    // ============================
    public void clearSavedLog() {
        fLogMeeting.clear();
        }
    // ============================
    // Set MenuItem for this window
    // ============================
    public void setMenuItem(Object    menuItem) {
        fMenuItem = (MenuItem) menuItem;
        }
    // ============================
    // Get Room Name
    // ============================
    public String getInternalRoomName() { return(fInternalRoomName); }
    public String getExternalRoomName() { return(fExternalRoomName); }
    // ============================
    // Join Room and Leave Room 
    // ============================
    public synchronized void joinRoom() {
        if (!fInRoom) {
            fMeetingProtocol.join(fInternalRoomName,
                    fPropertiesDB.getUserName());
            fInRoom = true;
            fRoomButton.setLabel(StringDefs.LEAVE);
            fDeliverButton.setEnabled(true);
            fInputArea.setEnabled(true);
            fListUI.setNotInRoom(fExternalRoomName, false);
            }
        }
        
    public synchronized void leaveRoom() {
        if (fInRoom) {
            setVisible(false);
            fMeetingProtocol.leave(fInternalRoomName,
                    fPropertiesDB.getUserName());
            fInRoom = false;
            fRoomButton.setLabel(StringDefs.JOIN);
            fDeliverButton.setEnabled(false);
            fInputArea.setEnabled(false);
            fListUI.setNotInRoom(fExternalRoomName, true);
            }
        }
   
    private synchronized void sendMessage() {
        if (fInRoom) {
            String  message = fInputArea.getText();
            
            if (message.length() == 0)
                return;
            
            if (message.charAt(message.length() - 1) != '\n')
                message += "\n";
            
            if (fMeetingProtocol.message(
                    fInternalRoomName,
                    fPropertiesDB.getUserName(),
                    message))
                fInputArea.setText("");
            else
                fTooLongMessageUI.setVisible(true);
            }
        }
    
    public ParticipantsUI getParticipantsUI() {
        return(fParticipantsUI);
        }
        
    public boolean isInRoom() {
        return(fInRoom);
        }

  	public LogMeeting	getLogMeeting() {
		return(fLogMeeting);
	}      
}

// Log
// 1.65 : 27-Sep-97 Y.Shibata   created.
// 1.67 :  4-Oct-97 Y.Shibata   changed behavior of join/leave.
// 1.69 : 11-Oct-97 Y.Shibata   added "Clear Log" button.
// 1.71 : 25-Oct-97 Y.Shibata   added code to show "Message Too Long" dialog.
// 1.74 :  3-Nov-97 Y.Shibata   added code to acquire the longest log.
// 1.75 :  9-Nov-97 Y.Shibata   No joinRoom/left message will be transfered as the result of the log request.
//                              Save/Restore location and size.
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 1.95 : 16-Jul-98 Y.Shibata   modified to use msgtool.db, msgtool.protocol
// 2.14 : 13-Feb-99	Y.Shibata	calling Frame.setState() is added.
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.33 : 17-Aug-99	Y.Shibata	used LogArea class [Refactoring]
//        21-Aug-99	Y.Shibata	used Logging class [Refactoring]
// 2.35 : 24-Oct-99	Y.Shibata	MeetingRoomFrame -> MeetingRoomUI
// 2.36 : 21-Nov-99	Y.Shibata	MeetingRoomUI -> MeetingRoomUIImpl

