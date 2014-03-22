// File: DialogUtil.java - last edit:
// Yoshiki Shibata 13-Jun-98

// Copyright (c) 1998 by Yoshiki Shibata

package msgtool.swing;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import msgtool.util.StringDefs;

public final class  DialogUtil {

    public static void showWarning(
        final JFrame  parent,
        final String  title,
        final String  message,
        final boolean wait) {
        Thread thread = new Thread() {
        
            public void run() {
                Object[]    options = { StringDefs.OK };
        
                parent.getToolkit().beep();
        
                JOptionPane.showOptionDialog(parent, message, title,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]);
                }
            };
        
        thread.start();
        if (wait) {
            try {
                thread.join();
                }
            catch (InterruptedException e) {}
            }
        }
    }
 
// LOG
// 1.86s :  7-Feb-98 Y.Shibata  created 
// 1.93b5 : 13-Jun-98 Y.Shibata add "wait".   
