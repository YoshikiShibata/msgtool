// File: NewMeetingRoomUI.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.SortUtil;
import msgtool.db.PropertiesDB;
import msgtool.meeting.MeetingManager;
import msgtool.protocol.MeetingListener;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class NewMeetingRoomUI 
    extends JFrame 
    implements ActionListener, WindowListener, PropertyChangeListener {
    
    private JLabel       	fRoomNameLabel     	= null;
    private JTextField   	fRoomName          	= null;
    private JLabel       	fPrivateRoomLabel  	= null;
    private JCheckBox    	fPrivateRoom       	= null;
	private JLabel			fFetchLogLabel		= null;
	private JCheckBox		fFetchLog			= null;
    private JButton      	fJoinButton     	= null;
    private JButton      	fDeleteButton     	= null;
    private JButton      	fCancelButton      	= null;
    private Frame        	fParentFrame       	= null;
    private MeetingListener fMeetingListener 	= null;
    private MeetingManager  fMeetingManager 	= null;
    private JPopupMenu   	fRoomHintsPopup 	= null;
    
    public NewMeetingRoomUI(
        Frame           parentFrame,
        MeetingListener meetingListener,
        MeetingManager  meetingManager,
        boolean         newMeetingRoom) {
		super(newMeetingRoom ? StringDefs.JOIN_MEETING_ROOM : StringDefs.DELETE_MEETING_ROOM);

        // super(parentFrame, 
        //    newMeetingRoom ? StringDefs.kJoinMeetingRoom : StringDefs.kDeleteMeetingRoom,  
        //    false);
        
        Container   contentPane = getContentPane();
        
        addWindowListener(this);
        
        PropertiesDB    propertiesDB = PropertiesDB.getInstance();
        
        propertiesDB.addPropertyChangeListener(this);
        LFManager.getInstance().add(this);
        
        fParentFrame        = parentFrame;
        fMeetingListener    = meetingListener;
        fMeetingManager     = meetingManager;
         
        fRoomHintsPopup = new JPopupMenu(/* StringDefs.kRooms */);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        contentPane.setLayout(gridBag);
        //
        // Meeting Room Name
        //
        fRoomNameLabel = new JLabel(StringDefs.ROOM_NAME_C);
        constraints.fill 			= GridBagConstraints.NONE;
        constraints.anchor 			= GridBagConstraints.EAST;
        constraints.gridwidth 		= 1;
        constraints.weightx 		= 0.0;
        constraints.weighty 		= 0.0;
		constraints.insets.top 		= 4;
		constraints.insets.left 	= 4;
		constraints.insets.bottom 	= 2;
		constraints.insets.right	= 2;
        gridBag.setConstraints(fRoomNameLabel, constraints);
        contentPane.add(fRoomNameLabel);

		fRoomNameLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				updateRoomHintsPopup();	
			}
		});
        fRoomNameLabel.addMouseListener(new JPopupMenuAdapter(fRoomNameLabel, fRoomHintsPopup));
		
        
        fRoomName = new JTextField(12);
		BGColorManager.getInstance().add(fRoomName);
        constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.fill 			= GridBagConstraints.HORIZONTAL;
        constraints.weightx 		= 1.0;
		constraints.insets.left 	= 2;
		constraints.insets.right	= 4;
        gridBag.setConstraints(fRoomName, constraints);
        contentPane.add(fRoomName);
		//
		// Fetch the log
		//
		if (newMeetingRoom) {
			fFetchLogLabel = new JLabel(StringDefs.FETCH_LOG);
			fFetchLog = createCheckBox(contentPane, gridBag, constraints, fFetchLogLabel); 
			fFetchLog.setSelected(true);
			}

        //
        // Closed Room checkbox
        //
		fPrivateRoomLabel = new JLabel(StringDefs.CLOSED_ROOM);
 	    fPrivateRoom = createCheckBox(contentPane, gridBag, constraints, fPrivateRoomLabel);
        
        //
        // Addting this as actionListener so that "Enter" key can be used
        // as Join operation [V1.67]
        //
        fRoomName.addActionListener(this);
        //
        // Join / Delete/ Cancel buttons
        //
        JPanel   panel = new JPanel();
        if (newMeetingRoom) {
            fJoinButton = new JButton(StringDefs.JOIN);
            fJoinButton.addActionListener(this);
            }
        else {
            fDeleteButton = new JButton(StringDefs.DELETE);
            fDeleteButton.addActionListener(this);
            }
        fCancelButton = new JButton(StringDefs.CANCEL);
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
        
        contentPane.add(panel);
        setFonts();
        pack();

		updateRoomHintsPopup();
        }
    
    private JCheckBox createCheckBox(
		Container			contentPane,
		GridBagLayout 		gridBag,
		GridBagConstraints	constraints,
		JLabel				label) {
        constraints.fill 			= GridBagConstraints.NONE;
        constraints.anchor 			= GridBagConstraints.EAST;
        constraints.gridwidth 		= 1;
        constraints.weightx 		= 0.0;
        constraints.weighty 		= 0.0;
		constraints.insets.top 		= 2;
		constraints.insets.left 	= 4;
		constraints.insets.bottom 	= 2;
		constraints.insets.right	= 2;
        gridBag.setConstraints(label, constraints);
        contentPane.add(label);

		JCheckBox checkBox = new JCheckBox();
		constraints.gridwidth 		= GridBagConstraints.REMAINDER;
        constraints.fill 			= GridBagConstraints.HORIZONTAL;
		constraints.insets.left 	= 2;
		constraints.insets.right	= 4;
        gridBag.setConstraints(checkBox, constraints);
        contentPane.add(checkBox);

		return(checkBox);
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
        fPrivateRoom.setFont(font);
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
											 fPrivateRoom.isSelected(),
											 fFetchLog.isSelected());
                else 
                    fMeetingManager.deleteRoom(fRoomName.getText(), fPrivateRoom.isSelected());
                }
            else {
                //
                // See the comment in setVisible() below. Usually setVisible(false)
                // make the popup menu invisible.
                //
                fRoomHintsPopup.setVisible(false);
                }
            }
        else if (target == fCancelButton) {
            setVisible(false);
            }
        else if (target instanceof JMenuItem) {
            JMenuItem    item = (JMenuItem) target;
            String      menuLabel = item.getText();
            
            fRoomName.setText(menuLabel);
            }
        }
        
    public void setVisible(boolean visible) {
        if (visible) { 
            ComponentUtil.centerComponent(this, fParentFrame.getLocation(), fParentFrame.getSize());
            fRoomName.setText("");
            fPrivateRoom.setSelected(false);
			updateRoomHintsPopup();
            }
        else {
            //
            //  Somehow, with Swing 0.7, the pop-up menu doesn't automatically become 
            // invisible. So make sure to make it invisible.
            //
            fRoomHintsPopup.setVisible(false);
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
        JMenuItem    mi  = null;
        
        fRoomHintsPopup.removeAll();
        if (rooms == null)
            fRoomHintsPopup.add(new JMenuItem(StringDefs.NO_ROOM_AVAILABLE));
        else {
            SortUtil.sortStrings(rooms);
            for (int i = 0; i < rooms.length; i++) {
                mi = new JMenuItem(rooms[i]);
                mi.addActionListener(this);
                fRoomHintsPopup.add(mi);
                }
            }
    
        }  
    }
// LOG
// --- 1.65 ---
// 28-Sep-97    Y.Shibata   created
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 2.15 : 27-Feb-99	Y.Shibata	used JPopupMenuAdapter class
// 2.18 : 24-Mar-99	Y.Shibata	added another MouseListener which updates the hint.
// 2.35 : 24-Oct-99	Y.Shibata	NewMeetingRoomDialog -> NewMeetingRoomUI



