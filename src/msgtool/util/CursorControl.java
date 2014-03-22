// File: CursorControl.java - last edit:
// Yoshiki Shibata 27-Dec-03 

// Copyright (c) 1997, 2000, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.util;

import java.awt.Component;
import java.awt.Cursor;
import java.util.ArrayList;

public final class CursorControl {

    private class EnableState {
        public Component component;
        public boolean isEnabled;
        public EnableState(Component component, boolean isEnabled) {
            this.component = component;
            this.isEnabled = isEnabled; 
		}
	}

    private ArrayList<Component>    fCursorComponentList    = new ArrayList<Component>();
    private ArrayList<EnableState>    fEnablableComponentList = new ArrayList<EnableState>();
    private Cursor  fWaitCursor     = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    private Cursor  fDefaultCursor  = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    static private CursorControl    fInstance = new CursorControl();
    static public  CursorControl    instance() { return(fInstance); }
    
    private CursorControl() { }
        
    public void addCursorComponent(
        Component   component) {
        synchronized (fCursorComponentList) {
            fCursorComponentList.add(component);
		}
	}
    
    public void addEnablableComponent(
        Component   component) {
        synchronized (fEnablableComponentList) {
            fEnablableComponentList.add(new EnableState(component, component.isEnabled()));
		} 
	}
   
    public void setBusy(boolean busy) {
        Cursor      cursor  = busy ? fWaitCursor : fDefaultCursor;
        boolean     enabled = !busy;

        /*
         * Setting cursor/enabled and resetting cursor/enabled must be correctly nested.
         * Otherwise, the cursor will not be rest correctly. [V1.61]
         */
        if (busy) {
            setCursor(cursor);
            setEnabled(enabled);
		} else {
            setEnabled(enabled);
            setCursor(cursor);
		}
	}
     
    private void setCursor(Cursor cursor) {
        synchronized (fCursorComponentList) {
            for (Component c: fCursorComponentList) 
                c.setCursor(cursor);
		}
	}
    
    private void setEnabled(boolean enabled) {     
        synchronized (fEnablableComponentList) {
            for (EnableState state:  fEnablableComponentList) {
                if (!enabled) {
                    // Save the current isEnabled.
                    state.isEnabled = state.component.isEnabled();
                    state.component.setEnabled(enabled);
				} else {
                    // Restore the save state
                    state.component.setEnabled(state.isEnabled);
				}
			} 
		}
	}
}

// 1.60 : 13-Sep-97 Y.Shibata   created.
// 1.61 : 16-Sep-97 Y.Shibata   fixed a remained cursor problem.
// 1.70 : 19-Oct-97 Y.Shibata   When enabled, a saved state will be restored.
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.util
// 2.40 :  4-Jan-00	Y.Shibata	changed names of methods to lower-case.
// 3.50 : 19-Nov-03	Y.Shibata	modified to be compiled cleanly with "Tiger"(SDK1.5).
//      : 27-Dec-03 Y.Shibata   rewritten with Java Generics and the enhanced "for" 
