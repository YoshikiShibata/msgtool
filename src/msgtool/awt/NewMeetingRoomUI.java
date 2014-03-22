// File: NewMeetingRoomUI.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.SortUtil;
import msgtool.db.PropertiesDB;
import msgtool.meeting.MeetingManager;
import msgtool.protocol.MeetingListener;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class NewMeetingRoomUI 
    extends Dialog 
    implements ActionListener, WindowListener, PropertyChangeListener {
	private Label       fRoomNameLabel      = null;
    private TextField   fRoomName           = null;
	private Label		fFetchLogLabel		= null;
	private Checkbox	fFetchLog			= null;
    private Label       fPrivateRoomLabel   = null;
    private Checkbox    fPrivateRoom        = null;
    private Button      fJoinButton         = null;
    private Button      fDeleteButton       = null;
    private Button      fCancelButton       = null;
    private Frame       fParentFrame        = null;
    private MeetingListener fMeetingListener = null;
    private MeetingManager  fMeetingManager = null;
    private PopupMenu   fRoomHintsPopup = null;
    
    public NewMeetingRoomUI(
        Frame           parentFrame,
        MeetingListener meetingListener,
        MeetingManager  meetingManager,
        boolean         newMeetingRoom) {
        super(parentFrame, 
            newMeetingRoom ? StringDefs.JOIN_MEETING_ROOM : StringDefs.DELETE_MEETING_ROOM,  
            false);
        
        addWindowListener(this);
        
        PropertiesDB    propertiesDB = PropertiesDB.getInstance();
        propertiesDB.addPropertyChangeListener(this);
        
        fParentFrame        = parentFrame;
        fMeetingListener    = meetingListener;
        fMeetingManager     = meetingManager;
         
        fRoomHintsPopup = new PopupMenu(StringDefs.ROOMS);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        setLayout(gridBag);
        //
        // Meeting Room Name
        //
        fRoomNameLabel = new Label(StringDefs.ROOM_NAME_C);
        constraints.fill 			= GridBagConstraints.NONE;
        constraints.anchor 			= GridBagConstraints.EAST;
        constraints.gridwidth 		= 1;
        constraints.weightx 		= 0.0;
        constraints.weighty 		= 0.0;
		constraints.insets.top 		= 4;
		constraints.insets.left		= 4;
		constraints.insets.bottom	= 2;
		constraints.insets.right	= 0;
        gridBag.setConstraints(fRoomNameLabel, constraints);
        add(fRoomNameLabel);
        fRoomNameLabel.addMouseListener(new PopupMenuAdapter(fRoomNameLabel, fRoomHintsPopup));
        
        fRoomName = new TextField(20);
		BGColorManager.getInstance().add(fRoomName);
        constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.fill 			= GridBagConstraints.HORIZONTAL;
        constraints.weightx 		= 1.0;
		constraints.insets.left		= 0;
		constraints.insets.right	= 4;
        gridBag.setConstraints(fRoomName, constraints);
        add(fRoomName);

		if (newMeetingRoom) {
			fFetchLogLabel = new Label(StringDefs.FETCH_LOG);
			fFetchLog = createCheckbox(gridBag, constraints, fFetchLogLabel);
			fFetchLog.setState(true);
			}
        //
        // Closed Room checkbox
        // 
        fPrivateRoomLabel = new Label(StringDefs.CLOSED_ROOM);
		fPrivateRoom = createCheckbox(gridBag, constraints, fPrivateRoomLabel);
        //
        // Addting this as actionListener so that "Enter" key can be used
        // as Join operation [V1.67]
        //
        fRoomName.addActionListener(this);
        //
        // Join / Delete/ Cancel buttons
        //
        Panel   panel = new Panel();
        if (newMeetingRoom) {
            fJoinButton = new Button(StringDefs.JOIN);
            fJoinButton.addActionListener(this);
            }
        else {
            fDeleteButton = new Button(StringDefs.DELETE);
            fDeleteButton.addActionListener(this);
            }
        fCancelButton = new Button(StringDefs.CANCEL);
        fCancelButton.addActionListener(this);
        if (newMeetingRoom)
            panel.add(fJoinButton);
        else
            panel.add(fDeleteButton);
        panel.add(fCancelButton);
        
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        gridBag.setConstraints(panel, constraints);
        
        add(panel);
        setFonts();
        pack();
        }
    
   	private Checkbox createCheckbox(
		GridBagLayout		gridBag,
		GridBagConstraints	constraints,
		Label				label) {
        constraints.fill 			= GridBagConstraints.NONE;
        constraints.anchor 			= GridBagConstraints.EAST;
        constraints.gridwidth 		= 1;
        constraints.weightx 		= 0.0;
        constraints.weighty 		= 0.0;
		constraints.insets.top 		= 2;
		constraints.insets.left		= 4;
		constraints.insets.bottom	= 0;
		constraints.insets.right	= 0;
        gridBag.setConstraints(label, constraints);
        add(label);
        
        Checkbox checkbox = new Checkbox();
        constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.fill 			= GridBagConstraints.HORIZONTAL;
		constraints.insets.left		= 0;
		constraints.insets.right	= 4;
        gridBag.setConstraints(checkbox, constraints);
        add(checkbox);
		return(checkbox);
		}

    public void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
        fRoomNameLabel.setFont(font);
        fRoomName.setFont(font);
		if (fFetchLogLabel != null)
			fFetchLogLabel.setFont(font);
        fPrivateRoomLabel.setFont(font);
        if (fJoinButton != null)
            fJoinButton.setFont(font);
        if (fDeleteButton != null)
            fDeleteButton.setFont(font);
        fCancelButton.setFont(font);
        Util.setFontsToMenu(fRoomHintsPopup, font);    
        }
    
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) 
            setFonts();
        }
    // ================================
    // ActionListener
    // ================================
    public void actionPerformed(ActionEvent event) {
        Object target = event.getSource();
      
        if (target == fJoinButton || target == fDeleteButton || target == fRoomName) {
            if (fRoomName.getText().length() > 0) {
                setVisible(false);
                if (fJoinButton != null) 
                    fMeetingManager.joinRoom(fRoomName.getText(), 
											fPrivateRoom.getState(),
											fFetchLog.getState());
                else 
                    fMeetingManager.deleteRoom(fRoomName.getText(), fPrivateRoom.getState());
                }
            }
        else if (target == fCancelButton) {
            setVisible(false);
            }
        else if (target instanceof MenuItem) {
            MenuItem    item = (MenuItem) target;
            String      menuLabel = item.getLabel();
            
            fRoomName.setText(menuLabel);
            }
        }
        
    public void setVisible(boolean visible) {
        if (visible) { 
            ComponentUtil.centerComponent(this, fParentFrame.getLocation(), fParentFrame.getSize());
            fRoomName.setText("");
			updateRoomHintsPopup();
            fPrivateRoom.setState(false);
            }
        super.setVisible(visible);
        if (visible)
            fRoomName.requestFocus();
        }

    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) {setVisible(false);} 
    public void windowDeiconified(WindowEvent event) {}
    public void windowIconified(WindowEvent event) {}
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
    public void windowOpened(WindowEvent event) {}
    public void windowClosing(WindowEvent event) {setVisible(false);}
    // ============================
    // UpdateRoomHintsPopup
    // ============================
    private void updateRoomHintsPopup() {
        String[]    rooms = fMeetingListener.getAllMeetingRooms();
        MenuItem    mi  = null;
        
        fRoomHintsPopup.removeAll();
        if (rooms == null)
            fRoomHintsPopup.add(new MenuItem(StringDefs.NO_ROOM_AVAILABLE));
        else {
            SortUtil.sortStrings(rooms);
            for (int i = 0; i < rooms.length; i++) {
                mi = new MenuItem(rooms[i]);
                mi.addActionListener(this);
                fRoomHintsPopup.add(mi);
                }
            }
    
        }  
    }
// LOG
// 1.65 : 28-Sep-97    Y.Shibata   created
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.ComponentUtil
// 1.95 : 16-Jul-98 Y.Shibata   modified to use msgtool.db.*
// 2.15 : 27-Feb-99	Y.Shibata	used PopupMenuAdapter class
// 2.35 : 24-Oct-99	Y.Shibata	NewMeetingRoomDialog -> NewMeetingRoomUI


