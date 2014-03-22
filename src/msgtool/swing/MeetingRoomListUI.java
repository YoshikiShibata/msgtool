// File: MeetingRoomListUI.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. Arll rights reserved.

package msgtool.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.SortUtil;
import msgtool.db.PropertiesDB;
import msgtool.meeting.MeetingManager;
import msgtool.ui.MeetingRoomUI;
import msgtool.util.ComponentUtil;
import msgtool.util.CursorControl;

@SuppressWarnings("serial")
public final class MeetingRoomListUI
    extends JFrame12 
    implements  WindowListener, MouseListener,
                PropertyChangeListener {
    
    private StateList       fRoomList     = null; 
    private MeetingManager  fMeetingManager = null;
    private boolean         fIconified      = false;
    
    private PropertiesDB    fPropertiesDB   = PropertiesDB.getInstance();

    public MeetingRoomListUI(
        MeetingManager      meetingManager,
        String              title) {
        super(title);
        
        Container   contentPane = getContentPane();
        
        fMeetingManager = meetingManager;
        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Beans
        //
        fPropertiesDB.addPropertyChangeListener(this);
        //
        // Add self to LFManager
        // 
        LFManager.getInstance().add(this);
        
        CursorControl  cursorControl = CursorControl.instance();
        
        fRoomList = new StateList(5, false);
		BGColorManager.getInstance().add(fRoomList);
        cursorControl.addCursorComponent(fRoomList);
        cursorControl.addEnablableComponent(fRoomList);
        fRoomList.addMouseListener(this);
        
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        contentPane.setLayout(gridBag);
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        
        gridBag.setConstraints(fRoomList, constraints);
        contentPane.add(fRoomList);
        setFonts();
        Dimension   size = fPropertiesDB.getRoomListDialogSize();
        if (size == null) {
            pack();
            
            size = getSize();
            if (size.width > 128) {
                size.width = 128;
                setSize(size);
                }
            }
        else
            setSize(size);
        ComponentUtil.fitComponentIntoScreen(this, fPropertiesDB.getRoomListDialogLocation());
        
        cursorControl.addCursorComponent(this); 
        }
        
    public void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
            
        fRoomList.setFont(font);
        fRoomList.invalidate(); // V1.90
        }
  
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) {
            setFonts();
            validate(); // V1.90
            }
        }
        
    public void addMeetingRoom(final String roomName) {
        Runnable methodBody = new Runnable() {
            public void run() {
                fRoomList.setEnabled(false);
                try {
                    int position = fRoomList.getItemPosition(roomName);
            
                    if (position != -1)
                        return; // already registered.
                
                    String[] rooms = fRoomList.getItems();
                    if (rooms.length == 0) {
                        //
                        //  No room is listed. So just add this room
                        //
                        fRoomList.add(roomName);
                        return;
                        }
                    //
                    // Create a sorted list of all rooms including this room.
                    //
                    int noOfRooms = rooms.length;
                    String[] newRooms = new String[noOfRooms + 1];
                    for (int i = 0; i < noOfRooms; i++)
                        newRooms[i] = rooms[i];
                    newRooms[noOfRooms] = roomName;
                    SortUtil.sortStrings(newRooms);
                    //
                    // Find out the position of this room, and insert it to into the list.
                    //
                    for (int i = 0; i < (noOfRooms + 1); i++) {
                        if (newRooms[i] == roomName) {
                            fRoomList.add(roomName, i);
                            }
                        }     
                    }
                finally {
                    fRoomList.setEnabled(true);
                    }
                }
            };
        SwingUtilities.invokeLater(methodBody);
        }
        
    public void removeMeetingRoom(final String roomName) {
        Runnable methodBody = new Runnable() {
            public void run() {
                fRoomList.setEnabled(false);
                try {
                    fRoomList.remove(roomName);
                    }
                catch (IllegalArgumentException e) {}
                fRoomList.setEnabled(true);
                }
            };
        SwingUtilities.invokeLater(methodBody);
        }
        
    public void setNotInRoom(
        final String  roomName, 
        final boolean notInRoom) {
        Runnable methodBody = new Runnable() {
            public void run() {
                fRoomList.setEnabled(false);
                fRoomList.setNotBeThere(roomName, notInRoom);
                fRoomList.setEnabled(true);
                }
            };
        SwingUtilities.invokeLater(methodBody);
        }
 
    public void setMessageWaiting(
        final String  roomName,
        final boolean messageWaiting) {
        Runnable methodBody = new Runnable() {
            public void run() {
                fRoomList.setEnabled(false);
                fRoomList.setMessageWaiting(roomName, messageWaiting);
                fRoomList.setEnabled(true);
				MeetingRoomListUI.this.setState(Frame.NORMAL);
                }
            };
        SwingUtilities.invokeLater(methodBody);
        }
    
    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) { setVisible(false); } 
    public void windowDeiconified(WindowEvent event) { fIconified = false; }
    public void windowIconified(WindowEvent event) { fIconified = true; }
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
    public void windowOpened(WindowEvent event) {}
    public void windowClosing(WindowEvent event) { setVisible(false); }
    // ============================================
    // MouseListener
    // ===========================================    
    public void mouseClicked(MouseEvent e) {
        // System.out.println("mouseClicked " + e.getClickCount());
        //
        // JDK 1.1.5 introduced a bug that double click results in clickCount = 3.
        // Therefore as a workaround, check if the clickCount is greater than or equal
        // to 2. [V1.80]
        //
        if (e.getClickCount() >= 2) {
            String  roomName = fRoomList.getSelectedItem();
            
            if (roomName != null) {
                MeetingRoomUI meetingRoom = 
                        fMeetingManager.findMeetingRoomUIByExternalName(roomName);
                
                meetingRoom.setVisible(true);
				meetingRoom.setState(Frame.NORMAL);
                //
                // Deselect the selected item
                //
                fRoomList.deselect(fRoomList.getSelectedIndex());
                
                }
            }
        }
        
    public  void mousePressed(MouseEvent e) {}   
    public  void mouseReleased(MouseEvent e){}
    public  void mouseEntered(MouseEvent e) {}
    public  void mouseExited(MouseEvent e)  {}
    // =============================================
    // SaveState for saving location, size, visible
    // =============================================
    public void saveStates() {
        fPropertiesDB.setRoomListDialogVisible(isVisible());
        //
        // Save the location only if the window is deiconified. [V1.64]
        //
        if (!fIconified)
            fPropertiesDB.setRoomListDialogLocation(getLocation());
        fPropertiesDB.setRoomListDialogSize(getSize());
		//
		// Save the Joined Meeting Rooms [V2.14]
		//
		fPropertiesDB.setJoinedMeetingRooms(fRoomList.getItems());
		//
		// Save the properties
		//
        fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        }
    }


// LOG
// 1.69 : 11-Oct-97 Y.Shibata   created
// 1.80 :  6-Dec-97 Y.Shibata   added a workaround for a bug of JDK 1.1.5
// 1.83 : 20-Dec-97 Y.Shibata   modification for JDK1.2beta2:
//                                import java.awt.List (java.util.List is added to JDK1.2)
// 1.91b2 : 4-Apr-98    Y.Shibata modified to use SwingUtilities for thread safe.
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 2.14 : 13-Feb-98	Y.Shibata	calling Frame.setState() is added.
//								saving the list of joined meeting rooms is added.
// 2.35 : 24-Oct-99	Y.Shibata	MeetingRoomListFrame -> MeetingRoomListUI
