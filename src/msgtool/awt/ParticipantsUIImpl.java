// File: ParticipantsUIImpl.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. Arll rights reserved.

package msgtool.awt;

import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import msgtool.common.Context;
import msgtool.common.SortUtil;
import msgtool.db.PropertiesDB;
import msgtool.ui.ParticipantsUI;
import msgtool.util.ComponentUtil;

@SuppressWarnings("serial")
public class ParticipantsUIImpl
    extends Dialog 
    implements  WindowListener, PropertyChangeListener, ParticipantsUI {

	private List        fParticipantsList     = null; // Current Displayed list.
    private List        fParticipantsList1    = null;
    private List        fParticipantsList2    = null;
    private final static String kParticipantsList1 = "ParticipantsList1";
    private final static String kParticipantsList2 = "ParticipantsList2";
    private Panel       fPanel          = null;
    private Frame       fMainFrame      = null;
    
    ParticipantsUIImpl(
        Frame               mainFrame,
        String              roomName) {
        super(mainFrame, roomName, false);
        
        fMainFrame  = mainFrame;

        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Beans
        //
        PropertiesDB.getInstance().addPropertyChangeListener(this);
        
        fPanel = new Panel();
        fPanel.setLayout(new CardLayout());
        
        fParticipantsList1 = new List(10);
        fPanel.add(kParticipantsList1, fParticipantsList1);
        
        fParticipantsList2 = new List(10);
        fPanel.add(kParticipantsList2, fParticipantsList2);
        
        fParticipantsList = fParticipantsList1;
      
        add(fPanel);
        setFonts();

        pack();
        
        //
        // Stretch to the length of the main frame.
        //
        Dimension size = getSize();
        Dimension parentSize = fMainFrame.getSize();
        size.height = parentSize.height;
        setSize(size);
        }
        
    void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
            
        fParticipantsList1.setFont(font);
        fParticipantsList2.setFont(font);
        }
  
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) {
            setFonts();
            pack();
            }
        }
        
    public synchronized void leave(String  participant) {
        int index = getIndexFromParticipantsList(participant);
        
        if (index == -1)
            return;     // already left
        
        fParticipantsList.remove(index);
        }
    
    public synchronized void join(String participant) {
        int index = getIndexFromParticipantsList(participant);
        
        if (index != -1)
            return;     // already join
            
        fParticipantsList.add(participant);  
        showUpdatedParticipantsList();
        }
    
    public synchronized void clearList() {
        fParticipantsList.removeAll();
        }
        
    
    private int getIndexFromParticipantsList(String participant) {
        int count = fParticipantsList.getItemCount();
        
        if (count == 0)
            return(-1);
        
        for (int i = 0; i < count; i++) {
            if (participant.equals(fParticipantsList.getItem(i)))
                return(i);
            }
            
        return(-1);
        }
    
    private void showUpdatedParticipantsList() {
        String[]    participants = fParticipantsList.getItems();
        
        if (participants == null || participants.length == 0)
            return;
 
        if (fParticipantsList == fParticipantsList1)
            fParticipantsList = fParticipantsList2;
        else
            fParticipantsList = fParticipantsList1;
        //
        // Now start sort by sort key
        //    
        SortUtil.sortStringsBySortKey(participants);
        fParticipantsList.removeAll();
        
        for (int i = 0; i < participants.length; i++) 
            fParticipantsList.add(participants[i]);
        //
        // Now the background one is complete, then show it as the forground one.
        //
        if (fParticipantsList == fParticipantsList1) 
            ((CardLayout)fPanel.getLayout()).show(fPanel, kParticipantsList1);
        else
            ((CardLayout)fPanel.getLayout()).show(fPanel, kParticipantsList2);
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
    // =================================
    // setVisible
    // =================================
    public void setVisible(boolean visible) {
        if (visible) 
            ComponentUtil.alignComponents(this, fMainFrame);
       
        super.setVisible(visible);
        }
    }


// LOG
// 1.67 :  4-Oct-97 Y.Shibata   created
// 1.83 : 20-Dec-97 Y.Shibata   modification for JDK1.2beta2:
//                                import java.awt.List (java.util.List is added to JDK1.2)
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.ComponentUtil
// 2.35 : 24-Oct-99	Y.Shibata	ParticipantsDialog -> ParticipantsUI
// 2.36 : 21-Nov-99	Y.Shibata	ParticipantsUI -> ParticipantsUIImpl
