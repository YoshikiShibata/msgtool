// File: FTPProgressFrame.java - last edit:
// Yoshiki Shibata 11-Apr-99

// Copyright (c) 1998, 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.common;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import msgtool.db.AddressDB;
import msgtool.protocol.FTPProgressListener;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class FTPProgressFrame 
    extends Frame
    implements FTPProgressListener, ActionListener
    {
    public final static int SEND_MODE       = 0;
    public final static int RECEIVE_MODE    = 1;
    
    private Label   fPeerNameLabel          = null;
    private Label   fFileNameLabel          = null;
    private Label   fTransferedBytesLabel   = null;
    private Button  fMultiPurposeButton     = null;
    private boolean fCanceledByUser         = false;
    private boolean fCancelEnabled          = true;

	FontMetrics		fFontMetrics 	= null;
	int				fMaxStringWidth = 0;
	long			fStartTime		= 0;

	Component		fBaseFrame = null;
    
    public FTPProgressFrame(int  mode, Component baseFrame) {
        setTitle(StringDefs.FILE_TRANSFER);
		fBaseFrame	= baseFrame;
        setBackground(Color.lightGray);

        GridBagLayout gridBag = new GridBagLayout();
        setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();
         
        if (mode == SEND_MODE) 
            fPeerNameLabel = createLabel(gridBag, c, StringDefs.RECIPIENT_NAME_C);
        else 
            fPeerNameLabel = createLabel(gridBag, c, StringDefs.SENDER_NAME_C);
            
        fFileNameLabel 			= createLabel(gridBag,c, StringDefs.FILE_NAME_C);
        fTransferedBytesLabel 	= createLabel(gridBag, c, StringDefs.PROGRESS_C);
        
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.0;
        c.weighty = 0.0;
  
        fMultiPurposeButton = new Button(StringDefs.CANCEL);
        fMultiPurposeButton.addActionListener(this);
        gridBag.setConstraints(fMultiPurposeButton, c);
        add(fMultiPurposeButton);
  	}
    // ==================================
    // ActionListener Implementation
    // ==================================
    synchronized public void actionPerformed(ActionEvent event) {
        if (fCancelEnabled) {
            fCancelEnabled = false;
            fCanceledByUser = true;
      	}
        setVisible(false);
		dispose();
  	}
        
    // ==================================
    // FTPProgressListener Implementation
    // ==================================
    public void     setFileName(String  fileName) {
		setLabelText(fFileNameLabel, fileName);
   	}
        
    public void     setPeerName(String  peerName) {
		String	registeredName = AddressDB.instance().lookUpName(peerName);

		if (registeredName != null)
			setLabelText(fPeerNameLabel, registeredName);
	 	else
			setLabelText(fPeerNameLabel, peerName);
    }
    
    public void     onConnecting() {
		setLabelText(fTransferedBytesLabel, StringDefs.CONNECTING_PPP);
        
        if (!isVisible())
            setVisible(true);
    }
        
    public void     onConnected() {
        setLabelText(fTransferedBytesLabel, StringDefs.CONNECTED);
        if (!isVisible())
            setVisible(true);
   		fStartTime = System.currentTimeMillis();
    }
        
    synchronized public boolean  onBeingTransfered(long totalOfTransferedBytes) {
        setLabelText(fTransferedBytesLabel, Long.toString(totalOfTransferedBytes) + " " + StringDefs.BYTES);
        if (fCanceledByUser)
            return(false);
        else
            return(true);
  	}
        
    synchronized public void onCompleted(long totalOfTransferedBytes) {
		long	ellapsedTime = System.currentTimeMillis() - fStartTime;
		
		if (ellapsedTime == 0)
			setLabelText(fTransferedBytesLabel, 
				Long.toString(totalOfTransferedBytes) + " " + StringDefs.BYTES + " " +
				StringDefs.COMPLETED);	
		else
        	setLabelText(fTransferedBytesLabel, 
				Long.toString(totalOfTransferedBytes) + " " + StringDefs.BYTES + " " +
				StringDefs.COMPLETED +
				"(" + (totalOfTransferedBytes * 1000)/ ellapsedTime + " bytes/s)");
        fMultiPurposeButton.setLabel(StringDefs.OK);
        fCancelEnabled = false;
		repaint();
  	}
        
    synchronized public void onCanceled() {
        setLabelText(fTransferedBytesLabel, StringDefs.ABORTED);
        fMultiPurposeButton.setLabel(StringDefs.OK);
        fCancelEnabled = false;
    }
   	// ===========================
	// pain() 
	// ===========================
	public void paint(Graphics g) {
		if (fFontMetrics == null)
			fFontMetrics = g.getFontMetrics();
		super.paint(g);
	}

    // ===========================
    // setVisible()
    // ===========================
    public void setVisible(boolean visible) {
        if (visible) {
            pack();
			if (fBaseFrame != null)
				ComponentUtil.overlapComponents(this, fBaseFrame, 32, false);
	 	}
        super.setVisible(visible);
 	}

    // ===========================
    // Private methods
    // ===========================
    private Label createLabel(
        GridBagLayout       gridBag,
        GridBagConstraints  c,
        String              titleName) {
        
        Label   titleLabel = new Label(titleName);
        
        c.anchor 		= GridBagConstraints.EAST;
        c.fill 			= GridBagConstraints.NONE;
        c.gridwidth 	= 1;
        c.weightx 		= 1.0;
        c.weighty 		= 0.0;
		c.insets.top	= 0;
		c.insets.left	= 4;
		c.insets.bottom	= 0;
		c.insets.right	= 0; // (0, 4, 0, 0)
        gridBag.setConstraints(titleLabel, c);
        add(titleLabel);
        
        Label valueLabel	= new Label();
        c.anchor    		= GridBagConstraints.WEST;
        c.gridwidth 		= GridBagConstraints.REMAINDER;
		c.insets.left		= 0;
		c.insets.right		= 2; // (0, 0, 0, 2)
        gridBag.setConstraints(valueLabel, c);
        add(valueLabel);
        
        return(valueLabel);
  	}
 	
 	private void setLabelText(
		Label	label,
		String	text) {
		label.setText(text);
		if (fFontMetrics != null) {
			int	newStringWidth = fFontMetrics.stringWidth(text);
			
			if (fMaxStringWidth < newStringWidth) {
				fMaxStringWidth = newStringWidth;
				label.invalidate();
				pack();
			}
		}
	}
    // =======================
    // Unit Test
    // =======================
    public static void main(String[] args) {
    
        FTPProgressFrame    frame = new FTPProgressFrame(FTPProgressFrame.RECEIVE_MODE, null);
        
        frame.setPeerName("Yoshiki");
        frame.setFileName("Test File");
        
        try {
            frame.onConnecting();
            Thread.sleep(5000);
            frame.onConnected();
            Thread.sleep(5000);
            for (int i = 0; i < 20; i++) {
                frame.onBeingTransfered(i * 1024);
                Thread.sleep(1000);
 			}
            frame.onCompleted(1024 * 20);
 		}
        catch (InterruptedException e) {}
	}
}
// LOG
// 2.10 : 18-Oct-98 Y.Shibata   created.
// 2.11 :  6-Feb-98	Y.Shibata	modified to correctly resize the window automatically.
// 2.14 : 13-Feb-99	Y.Shibata	background color is lightGray
//								translate peerName correctly.
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
