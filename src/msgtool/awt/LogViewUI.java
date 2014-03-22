// File: LogViewUI.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import msgtool.common.Context;
import msgtool.db.PropertiesDB;

@SuppressWarnings("serial")
public class LogViewUI 
    extends Dialog
    implements WindowListener, PropertyChangeListener { 

	private TextArea    fViewArea       = null;
    
    public LogViewUI(
        Frame       parentFrame,
        String      title,
        String      logText) {
        super(parentFrame, title, false);
        
        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Beans
        //
        PropertiesDB.getInstance().addPropertyChangeListener(this);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        setLayout(gridBag);
        //
        // ViewArea
        //
        fViewArea = new TextArea(logText, 15,  Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);  
        
        fViewArea.setEditable(false);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        gridBag.setConstraints(fViewArea, constraints);
        add(fViewArea);
        
        setFonts();
        pack();

        Point       location = parentFrame.getLocation();
        location.x += 32; 
        location.y += 32;
        setLocation(location);
        }
    
    public void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
            
        fViewArea.setFont(font);
        }
    
    public void setLogText(String   logText) {
        fViewArea.setText(logText);
        }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName))
            setFonts();
        }
        
    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) { setVisible(false);} 
    public void windowDeiconified(WindowEvent event) {}
    public void windowIconified(WindowEvent event) {}
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
    public void windowOpened(WindowEvent event) {}
    public void windowClosing(WindowEvent event) {setVisible(false);}
    }
// Log
// 1.46 : 17-Aug-97 Y.Shibata   created
// 1.70 : 18-Oct-97 Y.Shibata   deleted code which resizes the window.
// 1.95 : 16-Jul-98 Y.Shibata   modified to use msgtool.db.PropertiesDB;
// 2.35 : 24-Oct-99	Y.Shibaat	LogViewDialog -> LogViewUI
