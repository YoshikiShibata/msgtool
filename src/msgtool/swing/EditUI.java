// File: EditUI.java - last edit:
// Yoshiki Shibata 13-Nov-2004

// Copyright (c) 1996 - 1999, 2003, 2004 by Yoshiki Shibata. 
// All rights reserved.

package msgtool.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import msgtool.EditableDialog;
import msgtool.EditableListener;
import msgtool.common.BGColorManager;
import msgtool.common.FontManager;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class EditUI 
    extends JDialog
    implements ListSelectionListener, EditableDialog {

    private Container   fContentPane    = null; // Swing

    private JButton     fDeleteButton  	= null;
    private JButton     fUpdateButton  	= null;
    private StateList   fContentList    = null;

    private Frame       fParentFrame    = null;

    private Vector<JLabel> 		fListOfLabels 		= new Vector<JLabel>();
    private Hashtable<String, JTextField> fTextFieldHashTable = new Hashtable<String, JTextField>();
    private Hashtable<String, JCheckBox> fCheckBoxHashTable = new Hashtable<String, JCheckBox>();
	private EditableListener	fEditableListener	= null;
    
	private BGColorManager	fBGColorManager	= BGColorManager.getInstance();
	private FontManager		fFontManager	= FontManager.getInstance();
    

    public EditUI(
        Frame               parentFrame,
		EditableListener	editableListener) {
        super(parentFrame, editableListener.title(), false);
        fContentPane 		= getContentPane(); // Swing; 
        fParentFrame 		= parentFrame;
		fEditableListener	= editableListener;

        addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent event) {setVisible(false);} 
    		public void windowClosing(WindowEvent event) {
        		setVisible(false);
        		disableModification();
        	}
		});
        LFManager.getInstance().add(this);
   
        JButton cancelButton = createCancelButton();
        JButton setButton 	 = createSetButton();
        JButton addButton 	 = createAddButton();
        fDeleteButton 		 = createDeleteButton();
        fUpdateButton 		 = createUpdateButton();

        disableModification();

        GridBagLayout gridBag      = new GridBagLayout();
        GridBagConstraints gbc     = new GridBagConstraints();
        fContentPane.setLayout(gridBag);
        
        fContentList = new StateList(5);
		fBGColorManager.add(fContentList);
        fContentList.addListSelectionListener(this);
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gridBag.setConstraints(fContentList, gbc);
        fContentPane.add(fContentList);

		fEditableListener.construct(this);

        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(fDeleteButton);
        panel.add(fUpdateButton);
        panel.add(setButton);
        panel.add(cancelButton);

        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gridBag.setConstraints(panel, gbc);
        fContentPane.add(panel);
		
		fFontManager.addContainer(this);
        pack();
        }

    private JButton createUpdateButton() {
		JButton updateButton = new JButton(StringDefs.UPDATE);
        updateButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		int selectedIndex = fContentList.getSelectedIndex();

            	if (selectedIndex >= 0 &&
                	selectedIndex < fContentList.getItemCount()) {
    				fEditableListener.update(selectedIndex);
                }
            	disableModification();
        	}
        });
        fFontManager.addComponent(updateButton);
        return updateButton;
	}

	private JButton createDeleteButton() {
		JButton deleteButton = new JButton(StringDefs.DELETE);
        deleteButton.addActionListener(new ActionListener() { 
        	public void actionPerformed(ActionEvent event) {
        		int selectedIndex = fContentList.getSelectedIndex();

            	if (selectedIndex >= 0 &&
                	selectedIndex < fContentList.getItemCount()) {
    				fEditableListener.delete(selectedIndex);
                }
            	disableModification();
        	}
        });
		fFontManager.addComponent(deleteButton);
		return deleteButton;
	}

	private JButton createAddButton() {
		JButton addButton    = new JButton(StringDefs.ADD);
        addButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		fEditableListener.add();
        		disableModification();
        	}
        });
		fFontManager.addComponent(addButton);
		return addButton;
	}

	private JButton createSetButton() {
		JButton setButton    = new JButton(StringDefs.SET);
        setButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		fEditableListener.save();
                setVisible(false);
                disableModification();
        	}
        });
		fFontManager.addComponent(setButton);
		return setButton;
	}

	private JButton createCancelButton() {
		JButton cancelButton = new JButton(StringDefs.CANCEL);
        cancelButton.addActionListener(new ActionListener() {
        	 public void actionPerformed(ActionEvent event) {
        	 	setVisible(false);
        	 	disableModification();
        	 }
        });
		fFontManager.addComponent(cancelButton);
		return cancelButton;
	}

	private void disableModification() {
		fDeleteButton.setEnabled(false);
        fUpdateButton.setEnabled(false);
	}

    public void setVisible(boolean visible) {
        if (!visible) {
            super.setVisible(false);
            return;
        } 
        fEditableListener.show();
        ComponentUtil.centerComponent(this, fParentFrame.getLocation(), fParentFrame.getSize());
        super.setVisible(true);
    }
    // ================================
    //  ListSelectionListener
    // ================================
    public void valueChanged(ListSelectionEvent e) {
        if (fContentList.getSelectedIndex() < 0)
            return;
        
        fUpdateButton.setEnabled(true);
        fDeleteButton.setEnabled(true);

        int selectedIndex = fContentList.getSelectedIndex();

       	if (selectedIndex >= 0 &&
            	selectedIndex < fContentList.getItemCount()) {
				fEditableListener.select(selectedIndex);
        }
    }
	// =======================================
	// EditableDialog interface implementation
	// =======================================
	public void addItem(String	item) 	{ fContentList.add(item); }
	public void	removeAllItems() 		{ fContentList.removeAll(); }
	public void removeItemAt(int index) { fContentList.remove(index); }
	public String getItemAt(int index) 	{ return(fContentList.getItem(index));	}
	public int getItemIndex(String item) { return(fContentList.getItemPosition(item)); }
	public int getItemCount() { return(fContentList.getItemCount()); }

	public void 	addText(String fieldName) {
		JTextField	textField = new JTextField(20);
		addInputField(fieldName, textField);
		fBGColorManager.add(textField);
		fTextFieldHashTable.put(fieldName, textField);
		fFontManager.addComponent(textField);
	}

	public void 	setText(String fieldName, String text) {
		JTextField	textField = fTextFieldHashTable.get(fieldName);

		if (textField != null)
			textField.setText(text);
	}

	public String 	getText(String fieldName) {
		JTextField textField = fTextFieldHashTable.get(fieldName);

		if (textField != null)
			return(textField.getText());
	  	else
			return ("");
	}

	public void 	addBoolean(String fieldName) {
		JCheckBox checkbox = new JCheckBox();
		addInputField(fieldName, checkbox);
		fCheckBoxHashTable.put(fieldName, checkbox);
	}
	
	public void 	setBoolean(String fieldName, boolean value) {
		JCheckBox checkbox = fCheckBoxHashTable.get(fieldName);
		checkbox.setSelected(value);
	}
	
	public boolean getBoolean(String fieldName) {
		JCheckBox checkbox = fCheckBoxHashTable.get(fieldName);
		return(checkbox.isSelected());
	}
    // =============================
    // Private Utilities functions
    // =============================
    private void addInputField(
        String      fieldName,
        Component   inputField) {

    	GridBagLayout gridBag = (GridBagLayout) getLayout();
    	GridBagConstraints gbc = new GridBagConstraints();
        JLabel label = new JLabel(fieldName);
        gbc.fill      	= GridBagConstraints.NONE;
        gbc.anchor    	= GridBagConstraints.EAST;
        gbc.gridwidth 	= 1;
        gbc.weightx   	= 0.0;
        gbc.weighty   	= 0.0;
		gbc.insets.top	= 2;
		gbc.insets.left	= 2;
		gbc.insets.bottom	= 0;
		gbc.insets.right	= 2; // (2, 2, 0, 2)
        gridBag.setConstraints(label, gbc);
        fContentPane.add(label);
        fListOfLabels.addElement(label);
		fFontManager.addComponent(label);

        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1.0;
        gridBag.setConstraints(inputField, gbc);
        fContentPane.add(inputField);
        }
   }

// LOG
//        31-Aug-96 Y.Shibata   created
// 1.02 : 22-Feb-97 Y.Shibata   added FindIndexFromList() to avoid duplication
// 1.03 :  1-Mar-97 Y.Shibata   Hints and Addresses are sorted.
// 1.20 : 29-Mar-97 Y.Shibata   changed show() to setVisible().
//                              added "Update" button, and improved user interface
// 1.35 : 11-Jul-97 Y.Shibata   centering this window on top of the main window.
// 1.37 : 17-Jul-97 Y.Shibata   changed locations of buttons.
// 1.75 :  9-Nov-97 Y.Shibata   set Font correctly.
// 1.80 :  6-Dec-97 Y.Shibata   added List as hint.
// 1.83 : 20-Dec-97 Y.Shibata   modification for JDK1.2beta2:
//                                import java.awt.List (java.util.List is added to JDK1.2)
//                                addItem() -> add(), delItem() -> remove()
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata   Dialog -> JDialog
// 1.92 beta 3: 2-May-98 Y.Shibata  TextField -> JTextField
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.24 :  4-May-99	Y.Shibata	re-implemented with EditableDialog/EditableListener interfaces.
// 2.35 : 24-Oct-99	Y.Shibata	EditDialog -> EditUI
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
// 2.52 : 13-Nov-04 Y.Shibata	refactored

