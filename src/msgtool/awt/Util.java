// File: Util.java - last edit:
// Yoshiki Shibata 29-Dec-03

// Copyright (c) 1996, 1997, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.util.Properties;

import msgtool.common.SortUtil;
import msgtool.util.PropertiesUtil;
import msgtool.util.StringDefs;
import msgtool.util.StringUtil;

public class Util {
    
    // ==================
    // Update popup hints 
    // ==================
    final static public void updateHintsMenu(
        PopupMenu      hintsMenu,
        Properties     db,
        ActionListener actionListener) {
        MenuItem    mi = null;
        hintsMenu.removeAll();

        String hints[] = PropertiesUtil.propertyNamesToArray(db);
        if (hints.length == 0) {
            hintsMenu.add(new MenuItem(StringDefs.NO_HINT_AVAILABLE));
        } else {
            for (int i = 0; i < hints.length; i++) 
                hints[i] = StringUtil.underScore2Space(hints[i]);
      
            SortUtil.sortStrings(hints);
            for (int i = 0; i < hints.length; i++) {
                mi = new MenuItem(hints[i]);
                mi.addActionListener(actionListener);
                hintsMenu.add(mi);
            }
        }
    }
    
    final static public void  updateHintsMenu(
        PopupMenu       hintsMenu,
        Properties      db1,
        Properties      db2,
        ActionListener actionListener) {
        if (db2 == null) {
            updateHintsMenu(hintsMenu, db1, actionListener);
            return;
        }
            
        MenuItem    mi = null;
        hintsMenu.removeAll();

        String hints1[] = PropertiesUtil.propertyNamesToArray(db1);
        String hints2[] = PropertiesUtil.propertyNamesToArray(db2);

        if (hints1.length == 0 && hints2.length == 0) {
            hintsMenu.add(new MenuItem(StringDefs.NO_HINT_AVAILABLE));
        } else {
            String[]    hints = new String[hints1.length + hints2.length];
            int         index = 0;
            
            for (int i = 0; i < hints1.length; i++)
                hints[index++] = StringUtil.underScore2Space(hints1[i]);
            for (int i = 0; i < hints2.length; i++)
                hints[index++] = StringUtil.underScore2Space(hints2[i]);
      
            SortUtil.sortStringsBySortKey(hints);
            String      prevHint = null;
            for (int i = 0; i < hints.length; i++) {
                if (prevHint == null || prevHint.equals(hints[i]) == false) {
                    mi = new MenuItem(hints[i]);
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
    static final public void recipientHintSelected(
        String      hintString,
        TextField   toList,
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
        Menu    menu,
        Font    font) {
        int         count   = menu.getItemCount();
        MenuItem    item    = null;
        
        menu.setFont(font);
        for (int i = 0; i < count; i++) {
            item = menu.getItem(i);
            
            if (item instanceof Menu)
                setFontsToMenu((Menu) item, font);
            else {
                item.setFont(font);
				}
            }
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
// 3.50 : 29-Dec-03 Y.Shibata   rewritten with msgtool.util.PropertiesUtil
