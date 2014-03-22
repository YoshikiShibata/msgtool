// File: QuitUI.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import msgtool.common.Context;
import msgtool.db.PropertiesDB;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class QuitUI 
    extends Dialog 
    implements ActionListener, WindowListener, Runnable, PropertyChangeListener {

	private static final int kSecondsToWait = 10;

    private Button      fQuitButton     = null;
    private Button      fCancelButton   = null;
    private Label       fQuitMsg        = null;
    
    // Timer Threads Stuffs.
    private boolean     fQuitAccepted       = false;
    private Thread      fQuitTimerThread    = null;
    private Object      fQuitTimerObject    = null;
    private boolean     fQuitTimerStart     = false;
    private int         fQuitTimerCounter   = 0;
    
    public QuitUI(
        Frame           parentFrame) 
        {
        super(parentFrame, true);
        
        addWindowListener(this);

        PropertiesDB.getInstance().addPropertyChangeListener(this);

        GridBagLayout gridBag= new GridBagLayout();
        setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();

        c.anchor    = GridBagConstraints.CENTER;
        c.fill      = GridBagConstraints.NONE;
        c.weightx   = 1.0;
        c.weighty   = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        fQuitMsg = new Label(StringDefs.ARE_YOU_SURE_QUITING);
        fQuitMsg.setAlignment(Label.CENTER);
        gridBag.setConstraints(fQuitMsg, c);
        add(fQuitMsg);
        //
        // Cancel / Quit buttons
        //
        Panel panel = new Panel();

        fCancelButton  = new Button(StringDefs.CANCEL);
        fCancelButton.addActionListener(this);
        fQuitButton      = new Button(StringDefs.QUIT);
        fQuitButton.addActionListener(this);
        
        panel.add(fQuitButton);
        panel.add(fCancelButton);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridBag.setConstraints(panel, c);
        add(panel);

        setFonts();
        pack();
        setResizable(false);
        }
    
    public boolean confirm() {
        if (fQuitTimerThread == null) {
            fQuitTimerObject = new Object(); // dummy object for syncrhonization
            fQuitTimerThread = new Thread(this);
            fQuitTimerStart = false;
            fQuitTimerThread.setDaemon(true);
            fQuitTimerThread.start();
            }
        else if (System.getProperty("java.version").compareTo("1.1.4") < 0)
            fCancelButton.setEnabled(false);
        
        ComponentUtil.centerComponent(this, new Point(0,0), getToolkit().getScreenSize());
        fQuitAccepted = true;
        startTimer(kSecondsToWait);
        setVisible(true);
        stopTimer();
        return(fQuitAccepted);
        }

    private void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;

        fCancelButton.setFont(font);
        fQuitButton.setFont(font);
        fQuitMsg.setFont(font);
        }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) { 
            setFonts();
            pack();
            }
        }
    // ================================
    // ActionListener
    // ================================
    public void actionPerformed(ActionEvent event) {
        Object target = event.getSource();
      
        if (target == fQuitButton)
            fQuitAccepted = true;
        else if (target == fCancelButton) 
            fQuitAccepted = false;
        setVisible(false);
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
    public void windowClosing(WindowEvent event) {
        fQuitAccepted = false;
        setVisible(false);
        }
    // ===============================
    // Timer thread functions
    // ===============================
    private void startTimer(int secs) {
        synchronized (fQuitTimerObject) {
            fQuitTimerCounter = secs;
            fQuitTimerStart = true;
            fQuitTimerObject.notify();
            }
        }
    
    private void stopTimer() {
        synchronized(fQuitTimerObject) {
            fQuitTimerStart = false;
            fQuitTimerObject.notify();
            }
        } 
    // ===============================
    // Runnable 
    // ===============================
    public void run() {
        synchronized (fQuitTimerObject) {
            //
            // Loop forever.
            //
            while (true) {
                while (fQuitTimerStart == false)  {
                    try {
                        fQuitTimerObject.wait();
                        }   
                    catch (InterruptedException e) {}
                    }
                //
                // Thread.sleep() cannot be used here for sleeping, 
                // because fQuitTimerObject is locked here. Therefore
                // use wait() for sleeping.
                //      
                try {
                    fQuitTimerObject.wait(1000);
                    }
                catch (InterruptedException e) {}
                //
                // Check fQuitTimerStart to see if timer is running.
                // If the timer is still running, then decrement
                // fQuitTimerCounter. If fQuitTimerCounter <= 0, 
                // then set Visible to false, so that the quit dialog
                // winodw will disappear.
                //
                if (fQuitTimerStart) {
                    fQuitTimerCounter --;
                    if (fQuitTimerCounter <= 0) 
                        setVisible(false);
                    }
                }
            }
        }
    }

// LOG
//        6-Jul-97  Y.Shibata   created
// 1.35: 11-Jul-97  Y.Shibata   centering this window on the top of the main window.
// 1.37: 17-Jul-97  Y.Shibata   change the locations of buttons.
// 1.65: 27-Sep-97  Y.Shibata   deleted workaround code for a bug of JDK. The bug seemed fixed with 1.1.4
// 1.75:  9-Nov-97  Y.Shibata   changed the SecondsToWait from 5 seconds to 10 seconds.
//                              set Font correctly
// 1.94 :  5-Jul-97 Y.Shibata   modified to use msgtool.util.Component.
// 2.35 : 24-Oct-99	Y.Shibata	QuitDialog -> QuitUI
