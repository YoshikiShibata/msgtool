// File: ParticipantsUIImpl.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. Arll rights reserved.

package msgtool.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;

import msgtool.common.Context;
import msgtool.common.SortUtil;
import msgtool.db.PropertiesDB;
import msgtool.ui.ParticipantsUI;
import msgtool.util.ColorMap;
import msgtool.util.ComponentUtil;

@SuppressWarnings("serial")
public final class ParticipantsUIImpl
    extends JDialog 
    implements  WindowListener, PropertyChangeListener, ParticipantsUI {
    
    private StateList        fParticipantsList     = null; // Current Displayed list.
    private Frame       fMainFrame      = null;
    
    ParticipantsUIImpl(
        Frame               mainFrame,
        String              roomName) {
        super(mainFrame, roomName, false);
        
        Container   contentPane = getContentPane();
        
        fMainFrame  = mainFrame;

        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Beans
        //
        PropertiesDB    propertiesDB    = PropertiesDB.getInstance();
        
        propertiesDB.addPropertyChangeListener(this);
        
        LFManager.getInstance().add(this);
        
        fParticipantsList = new StateList(10, ColorMap.getColorByName(propertiesDB.getTextBackground()));
        contentPane.add(fParticipantsList);
        setFonts();
        setToParentHeight();
        }
    
    private void setToParentHeight() {
        Dimension size = getSize();
        Dimension parentSize = fMainFrame.getSize();
        size.height = parentSize.height;
        if (size.width == 0)
            size.width = 128;
        setSize(size);
        }
            
    void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
            
        fParticipantsList.setFont(font);
        fParticipantsList.invalidate();
        }
  
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) {
            setFonts();
            setToParentHeight();
            validate();
            }
        }
        
    public synchronized void leave(String  participant) {
        int index = fParticipantsList.getItemPosition(participant);
        
        if (index == -1)
            return;     // already left
        
        fParticipantsList.remove(index);
        }
    
    public synchronized void join(String participant) {
        int index = fParticipantsList.getItemPosition(participant);
        
        if (index != -1)
            return;     // already join
        
        String[]    participants = fParticipantsList.getItems();
        
        if (participants == null || participants.length == 0)    
            fParticipantsList.add(participant);
        else {
            String[]    newParticipants = new String[participants.length + 1];
            for (int i = 0; i < participants.length; i++)
                newParticipants[i] = participants[i];
            newParticipants[participants.length] = participant;
            
            SortUtil.sortStringsBySortKey(newParticipants);
            for (int i = 0; i < newParticipants.length; i++)
                if (newParticipants[i] == participant) {
                    fParticipantsList.add(participant, i);
                    return;
                    }
            } 
        }
    
    public synchronized void clearList() {
        fParticipantsList.removeAll();
        }
        
    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) { } 
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
        if (visible) {
            setToParentHeight(); 
            ComponentUtil.alignComponents(this, fMainFrame);
            }
       
        super.setVisible(visible);
        }
    }


// LOG
// 1.67 :  4-Oct-97 Y.Shibata   created
// 1.83 : 20-Dec-97 Y.Shibata   modification for JDK1.2beta2:
//                                import java.awt.List (java.util.List is added to JDK1.2)
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 2.35 : 24-Oct-99	Y.Shibata	ParticipantsDialog -> ParticipantsUI
// 2.36 : 21-Nov-99	Y.Shibata	ParticipantsUI -> ParticipantsUIImpl
