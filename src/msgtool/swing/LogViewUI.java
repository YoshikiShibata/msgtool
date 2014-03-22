// File: LogViewUI.java - last edit:
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
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;

import msgtool.common.Context;
import msgtool.db.PropertiesDB;
import msgtool.util.ColorMap;

@SuppressWarnings("serial")
public final class LogViewUI 
    extends JDialog
    implements PropertyChangeListener { 
   
    private StyledTextArea    fViewArea       = null;
    
    public LogViewUI(
        Frame       parentFrame,
        String      title,
        String      logText) {
        super(parentFrame, title, false);
        
        Container   contentPane = getContentPane(); // Swing
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //
        // Beans
        //
        PropertiesDB    propertiesDB = PropertiesDB.getInstance();
        
        propertiesDB.addPropertyChangeListener(this);
        
        LFManager.getInstance().add(this);
        //
        // Window Layouts
        //
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        setBackground(Color.lightGray);
        contentPane.setLayout(gridBag);
        //
        // ViewArea
        //
        fViewArea = new StyledTextArea(false, 
                ColorMap.getColorByName(propertiesDB.getTextBackground()));
        setFonts();
        fViewArea.setText(logText);
        fViewArea.setEditable(false);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        gridBag.setConstraints(fViewArea, constraints);
        contentPane.add(fViewArea);
         
        Dimension size = parentFrame.getSize();
        size.height = size.height / 2;
        setSize(size);
     
        Point       location = parentFrame.getLocation();
        location.x += 32; 
        location.y += 32;
        setLocation(location);
        }
    
    public void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
            
        fViewArea.setTextFont(font);
        }
    
    public void setLogText(String   logText) {
        fViewArea.setText(logText);
        }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName))
            setFonts();
        }
        
    }
// Log
// 1.46 : 17-Aug-97    Y.Shibata   created
// 1.70 : 18-Oct-97    Y.Shibata   deleted code which resizes the window..
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.95 : 18-Jul-98 Y.Shibata   restructuring.
// 2.35 : 24-Oct-99	Y.Shibata	LogViewDialog -> LogViewUI
