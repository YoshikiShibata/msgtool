// File: UncaughtExceptionFrame.java - last edit:
// Yoshiki Shibata 17-Jul-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.exception;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@SuppressWarnings("serial")
public class UncaughtExceptionHandler extends Frame {

    public UncaughtExceptionHandler() {
        super("Uncaught Exception");
        textArea = new TextArea();
        add(textArea);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
    }

    /*
     * installForAWEEvent() installs this class as an Exception Handler 
     * for AWT Event Dispath Thread. 
     * THIS IS NOT PUBLICLY PUBLISHED API. So please keep in mind that 
     * this code might not work  with future JDK versions.
     * 
     * This code was tested with JDK1.2.2. This code doesn't work with 
     * JDK1.2/JDK1.2.1 because of a bug of JDKs. And this code doesn't 
     * not work with JDK1.1.
     *
     * See java\awt\EventDispatchThread.java
     */
    static public void installForAWEEvent() {
        try {
            /*
             * Please note that System.setProperty is not provided with 
             * JDK1.1.
             */
            System.setProperty(
                "sun.awt.exception.handler",
                "msgtool.exception.UncaughtExceptionHandler");
        } catch (NoSuchMethodError e) {
        }
    }
    /*
     * THE NOT-PUBLICLY-PUBLISHED API mentioned above calls a method
     * whose signature is identical to handle() below.
     */
    public void handle(Throwable exception) {
        /*
         * Get the stack trace as String
         */
        ByteArrayOutputStream byteStreamOut = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStreamOut);
        exception.printStackTrace(printStream);
        String stackTrace = byteStreamOut.toString();
        printStream.close();
        try {
            byteStreamOut.close();
        } catch (IOException e) {
        }
        /*
         * show the stack trace
         */
        textArea.setText(stackTrace);
        textArea.invalidate();
        textArea.validate();
        pack();
        setVisible(true);
    }

    private TextArea textArea = null;
}

// LOG
// 2.32 : 17-Jul-99 Y.Shibata   created
//         3-Aug-99 Y.Shibata   modified installForAWTEvent for JDK1.1
