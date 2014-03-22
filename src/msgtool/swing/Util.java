// File: Util.java - last edit:
// Yoshiki Shibata 29-Dec-03

// Copyright (c) 1996, 1997, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;

import msgtool.common.SortUtil;
import msgtool.util.PropertiesUtil;
import msgtool.util.StringDefs;
import msgtool.util.StringUtil;

public final class Util {
    // ==================
    // Update popup hints 
    // ==================
    final static public void updateHintsMenu(
        JPopupMenu      hintsMenu,
        Properties     db,
        ActionListener actionListener) {
        JMenuItem    mi = null;
        hintsMenu.removeAll();

        String[] hints = PropertiesUtil.propertyNamesToArray(db);

        if (hints.length == 0) {
            hintsMenu.add(new JMenuItem(StringDefs.NO_HINT_AVAILABLE));
        } else {
            for (int i = 0; i < hints.length; i++)
                hints[i] = StringUtil.underScore2Space(hints[i]);
      
            SortUtil.sortStrings(hints);
            for (int i = 0; i < hints.length; i++) {
                mi = new JMenuItem(hints[i]);
                mi.addActionListener(actionListener);
                hintsMenu.add(mi);
            }
        }
    }
    
    final static public void  updateHintsMenu(
        JPopupMenu       hintsMenu,
        Properties      db1,
        Properties      db2,
        ActionListener actionListener) {
        if (db2 == null) {
            updateHintsMenu(hintsMenu, db1, actionListener);
            return;
        }
            
        JMenuItem    mi = null;
        hintsMenu.removeAll();

        String hints1[] = PropertiesUtil.propertyNamesToArray(db1);
        String hints2[] = PropertiesUtil.propertyNamesToArray(db2);
        
        if ((hints1.length + hints2.length) == 0) {
            hintsMenu.add(new JMenuItem(StringDefs.NO_HINT_AVAILABLE));
        } else {
            String[]    hints = new String[hints1.length + hints2.length];
            String      prevHint = null;
            int         index = 0;
            
            for (int i = 0; i < hints1.length; i++)
                hints[index++] = StringUtil.underScore2Space(hints1[i]);
            for (int i = 0; i < hints2.length; i++)
                hints[index++] = StringUtil.underScore2Space(hints2[i]);
      
            SortUtil.sortStringsBySortKey(hints);
            for (int i = 0; i < hints.length; i++) {
                if (prevHint == null || prevHint.equals(hints[i]) == false) {
                    mi = new JMenuItem(hints[i]);
                    mi.addActionListener(actionListener);
                    hintsMenu.add(mi);
                    prevHint = hints[i];
                }
            }
        }
    }
    
    // ============================
    // Selection on Popup Hints
    // ============================
    static public final void recipientHintSelected(
        String      hintString,
        JTextField  toList,
        boolean     shiftKeyPressed) {
        if (shiftKeyPressed) {
            String  currentList = toList.getText();
            
            if (currentList == null || currentList.length() == 0)
                toList.setText(hintString);
            else
                toList.setText(currentList + ", " + hintString);
            }
        else
            toList.setText(hintString);
        }
        
    // ===============================================================
    // SetFontsToMenu: set font to all menus and menuItems recursivley
    // ===============================================================     
    static public void setFontsToMenu(
        MenuElement    menuElement,
        Font           font) {
        MenuElement[] subElements = menuElement.getSubElements();
        
        menuElement.getComponent().setFont(font);
		menuElement.getComponent().invalidate();
        if (subElements == null)
            return;
            
        for (int i = 0; i < subElements.length; i++)
            setFontsToMenu(subElements[i], font);
        }

    // ===============================
    // Function for creating JMenuItem
    // ===============================
    static public JMenuItem createJMenuItem(
        JMenu           menu,
        String          text,
        int             mnemonic,
        int             acceleratorKey,
        String          toolTip,
        ActionListener  actionListener) {
        JMenuItem   menuItem = new JMenuItem();
        menuItem.setText(text);
        
        if (mnemonic > 0)
            menuItem.setMnemonic(mnemonic);
       
        if (acceleratorKey > 0)
            menuItem.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey, ActionEvent.CTRL_MASK));
       
        if (toolTip != null)
            menuItem.setToolTipText(toolTip);
        
        if (actionListener != null)
            menuItem.addActionListener(actionListener);
            
        menu.add(menuItem);
        return(menuItem);
        }
        
    }
// LOG
//        31-Aug-96 Y.Shibata   created
//        20-Feb-97 Y.Shibata   modified for the final version of JDK 1.1
// 1.02 : 22-Feb-97 Y.Shibata   Added UpdateFontOfHintsMenu()
// 1.03 :  1-Mar-97 Y.Shibata   SortStrings for UpdateHintsMenu()
// 1.11 : 26-Mar-97 Y.Shibata   added RecipientHintSelected()
//        27-Mar-97 Y.Shibata   added another UpdateHintsMenu()
// 1.35 : 11-Jul-97 Y.Shibata   added CenterComponent
// 1.49 : 22-Aug-97 Y.Shibata   added FitComponentIntoScreen()
// 1.53 :  3-Sep-97 Y.Shibata   added SortStringsBySortKey
// 1.67 :  5-Sep-97 Y.Shibata   added AlignComponents()
// 1.70 : 19-Oct-97 Y.Shibata   added SetFontsToMenu()
// 1.75 : 10-Nov-97 Y.Shibata   modified SortStringsBySortKey().
// 1.93b6 : 26-Jun-97 Y.Shibata added createJMenuItem().
// 1.95 : 18-Jul-98 Y.Shibata   restructuring
// 3.50 : 13-Oct-03 Y.SHibata	refactoring
//      : 29-Dec-03 Y.Shibata   rewritten with msgtool.util.PropertiesUtil
