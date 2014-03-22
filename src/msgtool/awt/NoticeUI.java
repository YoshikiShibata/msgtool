// File: NoticeUI.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1997 - 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class NoticeUI 
    extends Dialog 
    implements ActionListener, WindowListener, Runnable{
	private Button  fOkButton = null;
    private Label   fNoticeMessage = null;
    private Frame   fParentFrame = null; 
    
    public NoticeUI(
        Frame           parentFrame,
        String          message) 
        {
        super(parentFrame, true);
        
        addWindowListener(this);
        fParentFrame = parentFrame;
       
        GridBagLayout gridBag= new GridBagLayout();
        setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();

        c.anchor    = GridBagConstraints.CENTER;
        c.fill      = GridBagConstraints.NONE;
        c.weightx   = 1.0;
        c.weighty   = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        fNoticeMessage = new Label(message);
        fNoticeMessage.setAlignment(Label.CENTER);
        gridBag.setConstraints(fNoticeMessage, c);
        add(fNoticeMessage);
        //
        // Ok / Quit buttons
        //
        Panel panel = new Panel();

        fOkButton  = new Button(StringDefs.OK);
        fOkButton.addActionListener(this);
        
        panel.add(fOkButton);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.HORIZONTAL;
        gridBag.setConstraints(panel, c);
        add(panel);

        pack();
        setResizable(false);
        }
 
    public void setVisible(boolean visible) {
        if (visible) {
            Thread  thread = new Thread(this);
            thread.start();
            }
        else   
            super.setVisible(visible);
        }
 
    public void run() {
        ComponentUtil.centerComponent(this, fParentFrame.getLocation(), fParentFrame.getSize());
        super.setVisible(true);
        }
        
    // ================================
    // ActionListener
    // ================================
    public void actionPerformed(ActionEvent event) {
        Object target = event.getSource();
      
        if (target == fOkButton)
            setVisible(false);
        }
    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) {setVisible(false); } 
    public void windowDeiconified(WindowEvent event) {}
    public void windowIconified(WindowEvent event) {}
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
    public void windowOpened(WindowEvent event) {}
    public void windowClosing(WindowEvent event) { setVisible(false); }
    }

// LOG
// 1.71 : 25-Oct-97 Y.Shibata   created.
// 1.93 : 26-May-98 Y.Shibata   changed to modal dialog.
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.ComponentUtil
// 2.35 : 24-Oct-99	Y.Shibata	NoticeDialog -> NoticeUI
