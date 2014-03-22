// File: AboutUIImpl.java - last edit:
// Yoshiki Shibata 30-Dec-99

// Copyright (c) 1996 - 1999 by Yoshiki Shibata, All rights reserved.

package msgtool.swing;


import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;

import msgtool.MessagingToolVersion;
import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.db.PropertiesDB;
import msgtool.ui.AboutUI;
import msgtool.util.ComponentUtil;
import msgtool.util.NetUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class AboutUIImpl extends JDialog implements AboutUI
{
    private int             fWhichAbout     = 0;
    private Frame           fParentFrame    = null;
    private StyledTextArea  fTextArea       = null;
    private Container       fContentPane    = null; // Swing
    
    private JLabel   fTotalMemoryValueLabel   = null;
    private JLabel   fFreeMemoryValueLabel    = null;
    
    private PropertiesDB    fPropertiesDB   = PropertiesDB.getInstance();

	private GridBagLayout 		fGridBag		= new GridBagLayout();
	private GridBagConstraints	fConstraints 	= new GridBagConstraints();

    AboutUIImpl(
        Frame           parentFrame,
        int             whichAbout,
        String          title)
  	{
        super(parentFrame, title, false);
        fParentFrame = parentFrame;
		fWhichAbout  = whichAbout;

        fContentPane = getContentPane(); // Swing  
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        LFManager.getInstance().add(this);
        fContentPane.setLayout(fGridBag);

        fConstraints.anchor = GridBagConstraints.WEST;
        fConstraints.fill   = GridBagConstraints.NONE;
        fConstraints.weightx = 1.0;
        fConstraints.weighty = 1.0;
        fConstraints.gridwidth = GridBagConstraints.REMAINDER;

        switch (whichAbout) {
            case kMessagingTool: 
                aboutMessagingTool();
                setSize(new Dimension(400, 320)); 
                break;
            case kSystemProperties: 
                aboutSystemProperties();
                pack(); 
                break;
            case kMessageReceived:
                MessageReceived();
                pack(); 
                break;
  		}
	}

   // ===================
   // About MessagingTool
   // ===================
    private void aboutMessagingTool() 
  	{
        fTextArea = new StyledTextArea(false);
 	    BGColorManager.getInstance().add(fTextArea);
        fTextArea.setEditable(false);
        fConstraints.fill   = GridBagConstraints.BOTH;
        fGridBag.setConstraints(fTextArea, fConstraints);
        fContentPane.add(fTextArea);// Swing
        
        fTextArea.setTextFont(Context.getFont());
        
        fTextArea.setTextColor(Color.red);
        fTextArea.appendText(
                "MessagingTool V" + MessagingToolVersion.VERSION + " for JDK " + Context.getJDKVersion() + " (Swing)\n" +
                "Copyright (c) 1996 - 2000, 2003, 2004, 2007 by Yoshiki Shibata\n" +
                "All rights reserved\n\n");
        fTextArea.setTextColor(Color.black);
        fTextArea.appendText(
                "Redistribution and use in binary form without modification " +
                "are permitted provided that the following conditions are met:\n\n" +
                "1. One may not charge for this software or include it with software "+
                "which is sold.\n"+
                "2. Redistributions in binary form must reproduce the above copyright " +
                "notice, this list of conditions and the following disclaimer in the " +
                "documentation and/or other materials provided with the distribution.\n" +
                "3. Disclaimer of Warranty. THIS SOFTWARE IS PROVIDED BY THE AUTHOR " +
                "\"AS IS\" WITHOUT WARRANTY OF ANY KIND. ALL EXPRESS OR " +
                "IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY " +
                "OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, " +
                "ARE HEREBY EXCLUDED.\n"+
                "4. Limitation of Liability. THE AUTHOR SHALL NOT "+
                "BE LIABLE FOR ANY DAMAGES AS A RESULT OF USING OR DISTRIBUTING SOFTWARE. " +
                "IN NO EVENT WILL THE AUTHOR BE LIABLE FOR ANY LOST " +
                "REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, " +
                "CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER " +
                "CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING " +
                "OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF  " +
                "THE AUTHTOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.\n"+ 
                "\n");
        fTextArea.setTextColor(Color.red);
        fTextArea.appendText(
                "INFORMATION:\n");
        fTextArea.setTextColor(Color.blue);
        fTextArea.appendText(
                "  E-mail:   yshibata@ca2.so-net.ne.jp\n" + 
                "  Web page: http://www001.upp.so-net.ne.jp/yshibata\n\n" +
                "  This program is based on Xerox's MessagingTool:\n" +
                "  Copyright (c) 1987 - 1992 Xerox Corporation\n" +
                "  Copyright (c) 1991 - 1995 Fuji Xerox Co., Ltd.\n\n");
        fTextArea.setTextColor(Color.red);
        fTextArea.appendText(
                "ACKNOWLEDGMENTS:\n");
        fTextArea.setTextColor(Color.black);
        fTextArea.appendText(
                "   The author would like to express his greatest appreciation to all" +
                " the folks who've given him commentary and suggestions. They include");
        fTextArea.setTextColor(Color.blue);
        fTextArea.appendText( 
                " Loreene Terry," +
                " Terry Wells, Larry Bonham, Mark Yamnicky," +
                " Dennis Abramsohn, Adam Stein, Z Smith, David Nesbitt, Ed Reveche," +
                " Bruce Hamilton, Kouichi Akiyama, Takashi Hirata, Sumie Kurihara," +
                " Tadao Michimura, Motohisa Sodeyoshi, Tomoo Ishizawa, Chiya Yamashita," +
                " Hiroyuki Ishii, Sachiko Sato, Shinji Higuchi, Hiroyoshi Imaoka," +
                " Hiroyuki Toyama, Hiroyuki Kurokawa, Shiduo Nagashima, Shigeki Kondoh," +
                " Tadashi Namamura" + 
                ".\n\n");
        fTextArea.setTextColor(Color.red);
        fTextArea.appendText(
                "COMMENTS FROM USERS:\n");
        fTextArea.setTextColor(Color.blue);
        fTextArea.appendText(
                "[Terry Wells 9 Oct 1997]\n");
        fTextArea.setTextColor(Color.black);
        fTextArea.appendText(
                "   I've been using MessagingTool since the old XNS version. " +
                "I was very happy to see it ported to Java so it can be used " +
                "on many platforms. " + 
                "I find MessagingTool invaluable for keeping in touch " +
                "with coworkers as it fills that tricky gap between " + 
                "the phone and email.\n\n");
	  	fTextArea.setTextColor(Color.blue);
		fTextArea.appendText(
				"[John Wright 5 Oct 1999]\n");
	 	fTextArea.setTextColor(Color.black);
		fTextArea.appendText(
				"   Thank you for writing such a well done utility and providing " +
				"it to the world.");
  	}

    private JLabel appendNamedValue(
        String              name,
        String              value) 
	{   
        JLabel   nameLabel = new JLabel(name);
        
        fConstraints.anchor 		= GridBagConstraints.EAST;
        fConstraints.fill 			= GridBagConstraints.NONE;
        fConstraints.gridwidth 		= 1;
        fConstraints.weightx 		= 1.0;
        fConstraints.weighty 		= 1.0;
		fConstraints.insets.left 	= 4;
        fGridBag.setConstraints(nameLabel, fConstraints);
		fConstraints.insets.left 	= 0;
        fContentPane.add(nameLabel);
        
        JLabel	valueLabel = new JLabel(value);
        fConstraints.anchor    		= GridBagConstraints.WEST;
        fConstraints.fill      		= GridBagConstraints.NONE;
        fConstraints.gridwidth 		= GridBagConstraints.REMAINDER;
		fConstraints.insets.left 	= 4;
		fConstraints.insets.right 	= 2;
        fGridBag.setConstraints(valueLabel, fConstraints);
		fConstraints.insets.left 	= 0;
		fConstraints.insets.right 	= 0;
        fContentPane.add(valueLabel);
        
        return(valueLabel);
 	}
    // =======================
    // About System Properties
    // =======================
    private void aboutSystemProperties() 
   	{
        appendSystemPropertyValue("java.class.version");
        appendSystemPropertyValue("java.home");
        appendSystemPropertyValue("java.vendor");
        appendSystemPropertyValue("java.vendor.url");
        appendSystemPropertyValue("java.version");
        appendSystemPropertyValue("user.language");
        appendSystemPropertyValue("user.region");
        appendSystemPropertyValue("user.timezone");
        appendSystemPropertyValue("user.name");
        appendSystemPropertyValue("user.dir");
        appendSystemPropertyValue("user.home");
        appendSystemPropertyValue("os.arch");
        appendSystemPropertyValue("os.name");
        appendSystemPropertyValue("os.version");
        
        String myIPAddress = NetUtil.getMyIPAddress();
        
        if (myIPAddress != null)
            appendNamedValue("IP address:", myIPAddress);
        else
            appendNamedValue("IP address:", "unknown");
        
        //
        // E-Mail and registration
        //
        String email = fPropertiesDB.getEMail();
        int     registeredNumber = fPropertiesDB.getRegisteredNumber();
        if (email != null && email.length() != 0 && registeredNumber != -1) {
            appendNamedValue("E-Mail address:", email);
            appendNamedValue("Registered No.:", "" + registeredNumber);
      	}
        //
        // Initial Memory Information
        //
        fTotalMemoryValueLabel = appendNamedValue("Total Memory:", "");
        fFreeMemoryValueLabel = appendNamedValue( "Free Memory:", "");
        updateMemoryInformation();
        
        //
        // Enumerate all properties for debugging
        //
        // Properties   sysProps = System.getProperties();
        // if (sysProps == null)
        //     return;
        // 
        // Enumeration  propNames = sysProps.propertyNames();
        // while (propNames.hasMoreElements()) {
        //   String  propertyName = (String) propNames.nextElement();
        //   System.out.println("Property Name = " + propertyName);
        // }
 	}

    private void appendSystemPropertyValue(String propertyName) 
  	{
        String value = System.getProperty(propertyName);
        if (value != null)
            appendNamedValue(propertyName + ":", value);
        else
            appendNamedValue( propertyName + ":", "NO VALUE");
 	} 

    private void updateMemoryInformation()
 	{
        Runtime rt = Runtime.getRuntime();
        
        fTotalMemoryValueLabel.setText("" + rt.totalMemory());
        fFreeMemoryValueLabel.setText("" + rt.freeMemory()); 
 	}                         
  

    // =======================
    // Message Received Dialog
    // =======================
    private void MessageReceived() 
  	{
		JLabel msgLabel = new JLabel(StringDefs.NEW_MESSAGE_ARRIVED);
		fConstraints.anchor = GridBagConstraints.CENTER;
        fGridBag.setConstraints(msgLabel, fConstraints);
        fContentPane.add(msgLabel);
        //
        // In case of Message Received window, clicking the window,
        // will dismiss the window. [Feature requested by Kouchi Akiyama]
        //
        msgLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					setVisible(false);
			  	}
			});
 	}
    // ===========================================
    // SetVisible
    // ===========================================
    public void setVisible(boolean visible) 
	{
        //
        // Centering About windows 
        //
        if (visible && fWhichAbout != kMessageReceived)
            ComponentUtil.centerComponent(this, fParentFrame.getLocation(), fParentFrame.getSize());
        //
        // Restore / Save the location of Message Received Dialog.
        //
        if (fWhichAbout == kMessageReceived) {
            if (visible) 
                ComponentUtil.fitComponentIntoScreen(this, fPropertiesDB.getRcvDialogLocation());
            else {
                fPropertiesDB.setRcvDialogLocation(getLocation());
                fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
           	}
    	}
        //
        // Update memory usage if necesary
        //
        if (visible && fWhichAbout == kSystemProperties)
            updateMemoryInformation();
            
        super.setVisible(visible);
 	}
}

// LOG
//         1-Sep-96 Y.Shibata   created
//        26-Oct-96 Y.Shibata   modified after reading GJT
//        20-Feb-97 Y.Shibata   for the final version of JDK 1.1
// 1.02 : 23-Feb-97 Y.Shibata   deleted blank lines from MessageReceived() and centerd the message
//        25-Feb-97 Y.Shibata   added IP address to System Properties.
// 1.32 : 20-Jun-97 Y.Shibata   clicking in the Message Received window will dismiss the window
// 1.33 : 30-Jun-97 Y.Shibata   changing window title when a message is received is added.
// 1.35 : 11-Jul-97 Y.Shibata   centering this window on the top of the main window.
// 1.38 : 20-Jul-97 Y.Shibata   added code to show memory usage.
// 1.40 :  2-Aug-97 Y.Shibata   One year anniversay version.
// 1.49 : 22-Aug-97 Y.Shibata   used Util.FitComponentIntoScreen()
// 1.67 :  5-Oct-97 Y.Shibata   added Copyright Notice.
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 1.95 : 18-Jul-98 Y.Shibata   restructuring.
// 2.15 : 27-Feb-99	Y.Shibata	modified after reviewing code
// 2.35 : 24-Oct-99	Y.Shibata	AboutDialog -> AboutUIImpl


