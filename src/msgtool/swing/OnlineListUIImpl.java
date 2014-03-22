// File: OnlineListUIImpl.java - last edit:
// Yoshiki Shibata 24-Oct-99

// Copyright (c) 1998, 1999 by Yoshiki Shibata. Arll rights reserved.

package msgtool.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.SortUtil;
import msgtool.db.PropertiesDB;
import msgtool.dnd.StringDragSource;
import msgtool.ui.DedicatedUIManager;
import msgtool.ui.MainUI;
import msgtool.ui.OnlineListUI;
import msgtool.util.ComponentUtil;
import msgtool.util.CursorControl;
import msgtool.util.MessageControl;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class OnlineListUIImpl
    extends JFrame12 
    implements  WindowListener, ActionListener, MouseListener,
                PropertyChangeListener,
			    OnlineListUI {
    
    private StateList        fOnlineList     = null; // Current Displayed list.
    private JMenuBar     fMenuBar        = null;
    private JMenu        fListMenu       = null;
    private JMenuItem    fCopyIntoToMenu = null;
    private JMenuItem    fOpenMessagingDialogMenu    = null;
    private JMenuItem    fSelectAllMenu  = null;
    private boolean     fIconified      = false;
    
    private PropertiesDB    fPropertiesDB   = PropertiesDB.getInstance();
    private CursorControl   fCursorControl  = CursorControl.instance();

	@SuppressWarnings("unused")
	private Object	fDragSource = null;

	private MainUI				fMainUI = null;
	private DedicatedUIManager<JMenuItem>	fDedicatedUIManager = null;
    
    public OnlineListUIImpl(
        String              title,
		MainUI				mainUI,
		DedicatedUIManager<JMenuItem>	dedicatedUIManager) {
        
        super(title);
        
        fMainUI				= mainUI;
		fDedicatedUIManager	= dedicatedUIManager;
        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Beans
        //
        fPropertiesDB.addPropertyChangeListener(this);
        //
        // Add self to LFManager
        //
        LFManager.getInstance().add(this);
      
        fMenuBar    = new JMenuBar();
        setJMenuBar(fMenuBar);
        fListMenu   = new XJMenu(StringDefs.LIST);
        fMenuBar.add(fListMenu);
        
        fCopyIntoToMenu = new JMenuItem(StringDefs.COPY_INTO_TO_C);
        fCopyIntoToMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        fCopyIntoToMenu.addActionListener(this);
        fOpenMessagingDialogMenu = new JMenuItem(StringDefs.OPEN_MESSAGING_DIALOG);
        fOpenMessagingDialogMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fOpenMessagingDialogMenu.addActionListener(this);
        fSelectAllMenu = new JMenuItem(StringDefs.SELECT_ALL);
        fSelectAllMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        fSelectAllMenu.addActionListener(this);
        
        fListMenu.add(fCopyIntoToMenu);
        fListMenu.addSeparator();
        fListMenu.add(fOpenMessagingDialogMenu);
        fListMenu.addSeparator();
        fListMenu.add(fSelectAllMenu);
        
        fOnlineList = new StateList(10, true);

		//
		// Drag and Drop support for only JDK1.2 or later versions
		//
		try {
			fDragSource = new StringDragSource(fOnlineList, "getSelectedItem", new Class[]{});
	  	} catch (NoClassDefFoundError e) {}

		BGColorManager.getInstance().add(fOnlineList);
        fCursorControl.addCursorComponent(fOnlineList);
        fCursorControl.addEnablableComponent(fOnlineList);
        fOnlineList.addMouseListener(this);
        
        GridBagLayout       gridBag     = new GridBagLayout();
        GridBagConstraints  constraints = new GridBagConstraints();
        Container           contentPane = getContentPane();
        contentPane.setLayout(gridBag);
        
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        
        gridBag.setConstraints(fOnlineList, constraints);
        contentPane.add(fOnlineList);
        
        setFonts();
        Dimension   size = fPropertiesDB.getOnlineDialogSize();
        if (size == null) {
            pack();
            
            size = getSize();
            if (size.width > 128) {
                size.width = 128;
                setSize(size);
          	}
      	} else
            setSize(size);

        ComponentUtil.fitComponentIntoScreen(this, fPropertiesDB.getOnlineDialogLocation());
        fCursorControl.addCursorComponent(this);
 	}
        
    public void setFonts() {
        Font    font = Context.getFont();
        
        if (font == null)
            return;
            
        fOnlineList.setFont(font);
        fOnlineList.invalidate();  // V1.90
        Util.setFontsToMenu(fListMenu, font);
  	}
  
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(PropertiesDB.kName)) {
            setFonts();
            validate(); // V1.90
    	}
  	}
	//
	// OnlineLister interface implementation
	//
    public void setOffline(final String  recipient) {
        Runnable methodBody = new Runnable() {
            public void run() {
                fOnlineList.setEnabled(false);
                try {
                    fOnlineList.remove(recipient);
             	} catch (IllegalArgumentException e) {}
                fOnlineList.setEnabled(true);
         	}
      	};
        SwingUtilities.invokeLater(methodBody);
  	}

    public void setOnline(final String recipient) {
        Runnable methodBody = new Runnable() {
            public void run() {
                fOnlineList.setEnabled(false);
                try {
                    int position = fOnlineList.getItemPosition(recipient);
        
                    if (position != -1)
                        return; // already online
       
                    String[] onlines = fOnlineList.getItems();
                    if (onlines == null) {
                        //
                        // No recipient is listed. So just add this recipient.
                        // 
                        fOnlineList.add(recipient);
                        return;
                  	}
                    //
                    // create a sorted list of all recipients including this recipient
                    //
                    int noOfRecipients = onlines.length; 
                    String[] newOnlines = new String[noOfRecipients + 1];
                    for (int i = 0; i < noOfRecipients; i++)
                        newOnlines[i] = onlines[i];
                        newOnlines[noOfRecipients] = recipient;
                    SortUtil.sortStringsBySortKey(newOnlines);
                    //
                    // Find out the position of this recipient, and add it the list.
                    //
                    for (int i = 0; i < (noOfRecipients + 1); i++) {
                        if (newOnlines[i] == recipient) {
                            fOnlineList.add(recipient, i);
                            return;
                     	}
              		}
             	} finally {
                    fOnlineList.setEnabled(true);
             	}
           	}
     	};
        SwingUtilities.invokeLater(methodBody); 
  	}
    
    public void clearList() {
        Runnable methodBody = new Runnable() {
            public void run() {
                fOnlineList.setEnabled(false);
                fOnlineList.removeAll();
                fOnlineList.setEnabled(true);
          	}
      	};
        SwingUtilities.invokeLater(methodBody);
   	}
    //
    //
	//
    public String[] getOnlines() {
        //
        // GetOnlineList() must be called from the event dispatcher thread.
        // Currently this method is called from MainFrame.java.
        // 
        return(fOnlineList.getItems());
 	}

    public void setNotInOffice(
        final String  recipient, 
        final boolean notInOffice) {
		setOnline(recipient); // V2.34

        Runnable methodBody = new Runnable() {
            public void run() {
                fOnlineList.setEnabled(false);
                fOnlineList.setNotBeThere(recipient, notInOffice);
                fOnlineList.setEnabled(true);
          	}
      	};
        SwingUtilities.invokeLater(methodBody);
 	}
 
    public void setMessageWaiting(
        final String  recipient,
        final boolean messageWaiting) {
		setOnline(recipient); // V2.34

        Runnable methodBody = new Runnable() {
            public void run() {
                fOnlineList.setEnabled(false);
                fOnlineList.setMessageWaiting(recipient, messageWaiting);
                fOnlineList.setEnabled(true);
				OnlineListUIImpl.this.setState(Frame.NORMAL);
				MessageControl.getInstance().setMessageWaiting(recipient, messageWaiting);
         	}
     	};
        SwingUtilities.invokeLater(methodBody);
 	}
        
    public void clearAllMessageWaitings() {
        Runnable methodBody = new Runnable() {
            public void run() {
                fOnlineList.setEnabled(false);
                fOnlineList.clearAllMessageWaitings();
                fOnlineList.setEnabled(true);
         	    MessageControl.getInstance().clearAllMessagesWaiting();
         	}
     	};
        SwingUtilities.invokeLater(methodBody);
 	}

    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) { setVisible(false); } 
    public void windowDeiconified(WindowEvent event) { fIconified = false; }
    public void windowIconified(WindowEvent event) { fIconified = true; }
    public void windowActivated(WindowEvent event) {}
    public void windowDeactivated(WindowEvent event) {}
    public void windowOpened(WindowEvent event) {}
    public void windowClosing(WindowEvent event) { setVisible(false); }
    // ===========================
    // ActionListener
    // ===========================
    public void actionPerformed(ActionEvent event) {
        Object  target = event.getSource();
        
        if (target instanceof JMenuItem) {
            JMenuItem        item       = (JMenuItem)target;
            int[]       indexes = fOnlineList.getSelectedIndexes();
            
            if (indexes == null || indexes.length == 0)
                return;

            if (item ==  fCopyIntoToMenu) {
                String  toList = "";
                    
                for (int i = 0; i < indexes.length; i++) {
                    toList += fOnlineList.getItem(indexes[i]);
                    if ((i + 1) < indexes.length) 
                        toList += ", ";
             	}
                fMainUI.setToList(toList);
                setAllSelected(false);
        	} else if (item == fOpenMessagingDialogMenu) {
                openAction(indexes);
                setAllSelected(false);
          	} else if (item == fSelectAllMenu) 
                setAllSelected(true);
     	}
 	}

    private void setAllSelected(boolean selected) {
        int itemCount = fOnlineList.getItemCount();
        
        if (selected) {
            fOnlineList.selectAll();
     	} else {
            for (int i = 0; i < itemCount; i++)
                fOnlineList.deselect(i);
     	}
 	}
        
    private void openAction(int[] indexes) {
        String[]    onlines = new String[indexes.length];
        
        for (int i = 0; i < indexes.length; i++)
            onlines[i] = fOnlineList.getItem(indexes[i]);
        //
        // Turn off message waiting first, then open.
        //
        for (int i = 0; i < onlines.length; i++) {
            fOnlineList.setMessageWaiting(onlines[i], false);
			MessageControl.getInstance().setMessageWaiting(onlines[i], false);
        }    
        fCursorControl.setBusy(true); 
        fDedicatedUIManager.open(onlines);
        fCursorControl.setBusy(false);
        //
        // Make failed recipients off-line, so that the user
        // can understand why opening a dedicated dialog failed.
        //
        for (int i = 0; i < onlines.length; i++)
            if (onlines[i] != null) 
                setOffline(onlines[i]);            
 	}
    // ============================================
    // MouseListener
    // ===========================================    
    public void mouseClicked(MouseEvent e) {
        // System.out.println("mouseClicked " + e.getClickCount());
        //
        // JDK 1.1.5 introduced a bug that double click results in clickCount = 3.
        // Therefore as a workaround, check if the clickCount is greater than or equal
        // to 2. [V1.80]
        //
        if (e.getClickCount() >= 2) {
            int[]       indexes = fOnlineList.getSelectedIndexes();
            
            // if (e.getClickCount() > 2) 
            //     System.out.println("BUG: Click count(" + e.getClickCount() + ") is greater than 2");
            if (indexes != null && indexes.length > 0) {
                openAction(indexes);
                setAllSelected(false);
        	}
    	}
 	}
        
    public  void mousePressed(MouseEvent e) {}   
    public  void mouseReleased(MouseEvent e){}
    public  void mouseEntered(MouseEvent e) {}
    public  void mouseExited(MouseEvent e)  {}
    // =============================================
    // SaveState for saving location, size, visible
    // =============================================
    public void saveState() {
        fPropertiesDB.setOnlineDialogVisible(isVisible());
        //
        // Save the location only if the window is deiconified. [V1.64]
        //
        if (!fIconified)
            fPropertiesDB.setOnlineDialogLocation(getLocation());
        fPropertiesDB.setOnlineDialogSize(getSize());
        fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
 	}
}


// LOG
//         4-Aug-97  Y.Shibata   created
// 1.47 : 18-Aug-97  Y.Shibata   doubl-click opens a dedicated dialog.
// 1.49 : 22-Aug-97  Y.Shibata   use Util.FitComponentIntoScreen();
// 1.51 : 24-Aug-97  Y.Shibata   added "Select All" menu
//                               All selected items will be deselected after executing a menu command.
// 1.55 :  9-Sep-97  Y.Shibata   use two Lists to avoid flicker.
// 1.64 : 25-Sep-97  Y.Shibata   save the location only if deiconified.
// 1.70 : 19-Oct-97  Y.Shibata   added setListsEnabled() to avoid ArrayIndexException.
// 1.80 :  6-Dec-97  Y.Shibata   added a workaround for a bug of JDK 1.1.5
// 1.83 : 20-Dec-97  Y.Shibata   modification for JDK1.2beta2:
//                                import java.awt.List (java.util.List is added to JDK1.2)
// 1.91b2 : 4-Apr-98    Y.Shibata   modified to use SwingUtilities for thread safe.
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 2.19 : 27-Mar-99	Y.Shibata	used XJMenu instead of JMenu
// 2.24 :  9-May-99	Y.Shibata	modified to support Drag and Drop
// 2.34 :  9-Sep-99	Y.Shibata	fixed a bug: Uncaught Exception
// 2.35 : 24-Oct-99	Y.Shibata	OnlineListFrame -> OnlineListUI
//         6-Nov-99	Y.Shibata	OnlineListUI -> OnlineListUIImpl

