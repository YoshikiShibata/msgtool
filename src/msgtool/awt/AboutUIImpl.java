/*
 * File: AboutUIImpl.java - last edit:
 * Yoshiki Shibata 27-Feb-99
 *
 * Copyright (c) 1996 - 1999 by Yoshiki Shibata, All rights reserved.
 */
package msgtool.awt;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import msgtool.MessagingToolVersion;
import msgtool.common.Context;
import msgtool.db.PropertiesDB;
import msgtool.ui.AboutUI;
import msgtool.util.ComponentUtil;
import msgtool.util.NetUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class AboutUIImpl extends Dialog implements AboutUI {

    private int fWhichAbout = 0;
    private Frame fParentFrame = null;
    private TextArea fTextArea = null;

    private Label fTotalMemoryValueLabel = null;
    private Label fFreeMemoryValueLabel = null;

    private PropertiesDB fPropertiesDB = PropertiesDB.getInstance();

    private GridBagLayout fGridBag = new GridBagLayout();
    private GridBagConstraints fConstraints = new GridBagConstraints();

    AboutUIImpl(
            Frame parentFrame,
            int whichAbout,
            String title) {
        super(parentFrame, title, false);
        fParentFrame = parentFrame;
        //
        // Register as WindowListener
        //
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                setVisible(false);
            }
        });
        fWhichAbout = whichAbout;

        setLayout(fGridBag);

        fConstraints.anchor = GridBagConstraints.WEST;
        fConstraints.fill = GridBagConstraints.NONE;
        fConstraints.weightx = 1.0;
        fConstraints.weighty = 1.0;
        fConstraints.gridwidth = GridBagConstraints.REMAINDER;

        switch (whichAbout) {
            case kMessagingTool:
                AboutMessagingTool();
                break;
            case kSystemProperties:
                AboutSystemProperties();
                break;
            case kMessageReceived:
                MessageReceived();
                break;
        }

        pack();
    }
   // ===================
    // About MessagingTool
    // ===================
    private void AboutMessagingTool() {
        fTextArea = new TextArea("", 12, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
        fTextArea.setEditable(false);
        fConstraints.fill = GridBagConstraints.BOTH;
        fGridBag.setConstraints(fTextArea, fConstraints);
        add(fTextArea);

        fTextArea.setFont(Context.getFont());

        fTextArea.setText(
                "MessagingTool V" + MessagingToolVersion.VERSION + " for JDK " + Context.getJDKVersion() + " (AWT)\n"
                + "Copyright (c) 1996 - 2000, 2003, 2004 by Yoshiki Shibata\n"
                + "All rights reserved\n\n"
                + "Redistribution and use in binary form without modification "
                + "are permitted provided that the following conditions are met:\n\n"
                + "1. One may not charge for this software or include it with software "
                + "which is sold.\n"
                + "2. Redistributions in binary form must reproduce the above copyright "
                + "notice, this list of conditions and the following disclaimer in the "
                + "documentation and/or other materials provided with the distribution.\n"
                + "3. Disclaimer of Warranty. THIS SOFTWARE IS PROVIDED BY THE AUTHOR "
                + "``AS IS'' WITHOUT WARRANTY OF ANY KIND. ALL EXPRESS OR "
                + "IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY "
                + "OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, "
                + "ARE HEREBY EXCLUDED.\n"
                + "4. Limitation of Liability. THE AUTHOR SHALL NOT "
                + "BE LIABLE FOR ANY DAMAGES AS A RESULT OF USING OR DISTRIBUTING SOFTWARE. "
                + "IN NO EVENT WILL THE AUTHOR BE LIABLE FOR ANY LOST "
                + "REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, "
                + "CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER "
                + "CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING "
                + "OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF  "
                + "THE AUTHTOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.\n"
                + "\n"
                + "INFORMATION:\n"
                + "  E-mail:   yshibata@ca2.so-net.ne.jp\n"
                + "  Web page: http://www001.upp.so-net.ne.jp/yshibata\n\n"
                + "  This program is based on Xerox's MessagingTool:\n"
                + "  Copyright (c) 1987 - 1992 Xerox Corporation\n"
                + "  Copyright (c) 1991 - 1995 Fuji Xerox Co., Ltd.\n\n"
                + "ACKNOWLEDGMENTS:\n"
                + "   The author would like to express his greatest appreciation to all"
                + " the folks who've given him commentary and suggestions. They include"
                + " Loreene Terry,"
                + " Terry Wells, Larry Bonham, Mark Yamnicky,"
                + " Dennis Abramsohn, Adam Stein, Z Smith, David Nesbitt, Ed Reveche,"
                + " Bruce Hamilton, Kouichi Akiyama, Takashi Hirata, Sumie Kurihara,"
                + " Tadao Michimura, Motohisa Sodeyoshi, Tomoo Ishizawa, Chiya Yamashita,"
                + " Hiroyuki Ishii, Sachiko Sato, Shinji Higuchi, Hiroyoshi Imaoka,"
                + " Hiroyuki Toyama, Hiroyuki Kurokawa, Shiduo Nagashima, Shigeki Kondoh,"
                + " Tadashi Namamura"
                + ".\n\n"
                + "COMMENTS FROM USERS:\n"
                + "[Terry Wells 9 Oct 1997]\n"
                + "   I've been using MessagingTool since the old XNS version. "
                + "I was very happy to see it ported to Java so it can be used "
                + "on many platforms. "
                + "I find MessagingTool invaluable for keeping in touch "
                + "with coworkers as it fills that tricky gap between "
                + "the phone and email.\n\n"
                + "[John Wright 5 Oct 1999]\n"
                + "   Thank you for writing such a well done utility and providing it to the world.");
    }

    private Label appendNamedValue(
            String name,
            String value) {
        Label nameLabel = new Label(name);

        fConstraints.anchor = GridBagConstraints.EAST;
        fConstraints.fill = GridBagConstraints.NONE;
        fConstraints.gridwidth = 1;
        fConstraints.weightx = 1.0;
        fConstraints.weighty = 1.0;
        fConstraints.insets.left = 4;
        fGridBag.setConstraints(nameLabel, fConstraints);
        fConstraints.insets.left = 0;
        add(nameLabel);

        Label valueLabel = new Label(value);
        fConstraints.anchor = GridBagConstraints.WEST;
        fConstraints.gridwidth = GridBagConstraints.REMAINDER;
        fConstraints.insets.right = 2;
        fGridBag.setConstraints(valueLabel, fConstraints);
        fConstraints.insets.right = 0;
        add(valueLabel);

        return (valueLabel);
    }

    // =======================
    // About System Properteis
    // =======================
    private void AboutSystemProperties() {
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

        if (myIPAddress != null) {
            appendNamedValue("IP address:", myIPAddress);
        } else {
            appendNamedValue("IP address:", "unknown");
        }

        //
        // E-Mail and registration
        //
        String email = fPropertiesDB.getEMail();
        int registeredNumber = fPropertiesDB.getRegisteredNumber();
        if (email != null && email.length() != 0 && registeredNumber != -1) {
            appendNamedValue("E-Mail address:", email);
            appendNamedValue("Registered No.:", "" + registeredNumber);
        }
        //
        // Initial Memory Information
        //
        fTotalMemoryValueLabel = appendNamedValue("Total Memory:", "");
        fFreeMemoryValueLabel = appendNamedValue("Free Memory:", "");
        updateMemoryInformation();
    }

    private void appendSystemPropertyValue(
            String propertyName) {
        String value = System.getProperty(propertyName);
        if (value != null) {
            appendNamedValue(propertyName + ":", value);
        } else {
            appendNamedValue(propertyName + ":", "NO VALUE");
        }
    }

    private void updateMemoryInformation() {
        Runtime rt = Runtime.getRuntime();

        fTotalMemoryValueLabel.setText("" + rt.totalMemory());
        fFreeMemoryValueLabel.setText("" + rt.freeMemory());
    }

    // =======================
    // Message Received Dialog
    // =======================
    private void MessageReceived() {
        Label msgLabel = new Label(StringDefs.NEW_MESSAGE_ARRIVED);
        fConstraints.anchor = GridBagConstraints.CENTER;
        fGridBag.setConstraints(msgLabel, fConstraints);
        add(msgLabel);
        //
        // In case of Message Received window, clicking the window,
        // will dismiss the window. [Feature requested by Kouchi Akiyama]
        //
        msgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
            }
        });
    }

    // ===========================================
    // setVisible
    // ===========================================
    @Override
    public void setVisible(boolean visible) {
        //
        // Centering About windows 
        //
        if (visible && fWhichAbout != kMessageReceived) {
            ComponentUtil.centerComponent(this, fParentFrame.getLocation(), fParentFrame.getSize());
        }
        //
        // Restore / Save the location of Message Received Dialog.
        //
        if (fWhichAbout == kMessageReceived) {
            if (visible) {
                ComponentUtil.fitComponentIntoScreen(this, fPropertiesDB.getRcvDialogLocation());
            } else {
                fPropertiesDB.setRcvDialogLocation(getLocation());
                fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
            }
        }
        //
        // Update memory usage if necesary
        //
        if (visible && fWhichAbout == kSystemProperties) {
            updateMemoryInformation();
        }

        super.setVisible(visible);
    }
}

// LOG
//         1-Sep-96 Y.Shibata   created
// 		  26-Oct-96 Y.Shibata   modified after reading GJT
//        20-Feb-97 Y.Shibata   for the final version of JDK 1.1
// 1.02 : 23-Feb-97 Y.Shibata   deleted blank lines from MessageReceived() and centerd the message
// 		  25-Feb-97 Y.Shibata   added IP address to System Properties.
// 1.03 :  1-Mar-97 Y.Shibata   1.03
// 1.32 : 20-Jun-97 Y.Shibata   clicking in the Message Received window will dismiss the window
// 1.33 : 30-Jun-97 Y.Shibata   changing window title when a message is received is added.
// 1.35 : 11-Jul-97 Y.Shibata   centering this window on the top of the main window.
// 1.38 : 20-Jul-97 Y.Shibata   added code to show memory usage.
// 1.40 :  2-Aug-97 Y.Shibata   One year anniversay version.
// 1.49 : 22-Aug-97 Y.Shibata   used Util.FitComponentIntoScreen()
// 1.67 :  5-Oct-97 Y.Shibata   added Copyright Notice.
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.ComponentUtil
// 2.15 : 27-Feb-99	Y.Shibata	modified after reviewing code
// 2.35 : 24-Oct-99	Y.Shibata	AboutDialog -> AboutUIImpl
