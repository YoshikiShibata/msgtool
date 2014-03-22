// File: EditUI.java - last edit:
// Yoshiki Shibata 13-Nov-2004

// Copyright (c) 1996 - 1999, 2003, 2004 by Yoshiki Shibata. 
// All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import msgtool.EditableDialog;
import msgtool.EditableListener;
import msgtool.common.BGColorManager;
import msgtool.common.FontManager;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public class EditUI extends Dialog
    implements ItemListener, EditableDialog {
	
	private List			fNameList		= new List(5, false);
    private Button       	fDeleteButton  	= null;
    private Button       	fUpdateButton  	= null;

    private Frame       	fParentFrame    = null;

	private EditableListener	 fEditableListener	= null;
	private Hashtable<String,TextField> fTextFieldHashTable = new Hashtable<String,TextField>();
    private Hashtable<String,Checkbox>  fCheckBoxHashTable = new Hashtable<String,Checkbox>();

	private BGColorManager	fBGColorManager	= BGColorManager.getInstance();
	private FontManager		fFontManager	= FontManager.getInstance();

    public EditUI(
        Frame               parentFrame,
        EditableListener	editableListener) {
        
        super(parentFrame, editableListener.title(), false); 
        fParentFrame 		= parentFrame;
		fEditableListener 	= editableListener;

        addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent event) {setVisible(false);}
			public void windowClosing(WindowEvent event) {
        		setVisible(false);
        		disableModification();
        		}
		});
   
        Button cancelButton = createCancelButton();
		Button setButton 	= createSetButton();
        Button addButton 	= createAddButton();
        fDeleteButton 		= createDeleteButton();
        fUpdateButton 		= createUpdateButton();
        
        disableModification();
        
        GridBagLayout   	gridBag = new GridBagLayout();
        GridBagConstraints 	gbc 	= new GridBagConstraints();
        setLayout(gridBag);

      	fBGColorManager.add(fNameList);
      	fNameList.addItemListener(this); 
      	gbc.anchor    = GridBagConstraints.NORTH;
      	gbc.fill      = GridBagConstraints.BOTH;
      	gbc.gridwidth = GridBagConstraints.REMAINDER;
      	gbc.weightx   = 1.0;
      	gbc.weighty   = 1.0;
      	gridBag.setConstraints(fNameList, gbc);
      	add(fNameList);
        
		fEditableListener.construct(this);

        Panel panel = new Panel();
        panel.add(addButton);
        panel.add(fDeleteButton);
        panel.add(fUpdateButton);
        panel.add(setButton);
        panel.add(cancelButton);

        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gridBag.setConstraints(panel, gbc);
        add(panel);

		fFontManager.addContainer(this);
        pack();
        }
    
    private void disableModification() {
		fDeleteButton.setEnabled(false);
        fUpdateButton.setEnabled(false);
	}
    
 	private void enableModification() {
		fUpdateButton.setEnabled(true);
        fDeleteButton.setEnabled(true);
	}

	private Button  createUpdateButton() {
		Button updateButton = new Button(StringDefs.UPDATE);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	int selectedIndex = fNameList.getSelectedIndex();

                if (selectedIndex >= 0 &&
                    selectedIndex < fNameList.getItemCount()) 
        			fEditableListener.update(selectedIndex);
                    
                disableModification();
            }
        });
		fFontManager.addComponent(updateButton);
		return updateButton;
	}
    
	private Button createDeleteButton() {
		Button deleteButton = new Button(StringDefs.DELETE);
        deleteButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		int selectedIndex = fNameList.getSelectedIndex();

            	if (selectedIndex >= 0 &&
                	selectedIndex < fNameList.getItemCount()) 
    				fEditableListener.delete(selectedIndex);
            	
            	disableModification();
            }
        });
		fFontManager.addComponent(deleteButton);
		return deleteButton;
	}
	
	private Button createAddButton() {
		Button addButton = new Button(StringDefs.ADD);
        addButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		fEditableListener.add();
        		disableModification();
        	}
        });
		fFontManager.addComponent(addButton);
		return addButton;
	}
	
	private Button createSetButton() {
		Button setButton = new Button(StringDefs.SET);
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
	
	private Button createCancelButton() {
		Button cancelButton = new Button(StringDefs.CANCEL);
        cancelButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent event) {
        		setVisible(false);
        		disableModification();
        	}
        });
		fFontManager.addComponent(cancelButton);
		return cancelButton;
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
    // ItemListener
    // ================================
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) 
            return;
        
        enableModification();
        
		int selectedIndex = fNameList.getSelectedIndex();
       	if (selectedIndex >= 0 &&
            	selectedIndex < fNameList.getItemCount())
 	   		fEditableListener.select(selectedIndex);
        }

	// =======================================
	// EditableDialog interface implementation
	// =======================================
	public void addItem(String	item) 	{ fNameList.add(item); }
	public void	removeAllItems() 		{ fNameList.removeAll(); }
	public void removeItemAt(int index) { fNameList.remove(index); }
	public String getItemAt(int index) 	{ return(fNameList.getItem(index));	}

	public int	getItemIndex(String item) {
		int noOfItems = getItemCount();
        
        for (int i = 0; i < noOfItems; i++)
            if (item.equals(fNameList.getItem(i)))
                return(i);
        
        return(-1);
	}

	public int getItemCount() { return(fNameList.getItemCount()); }

	public void 	addText(String fieldName) {
		TextField	textField = new TextField(20);
		addInputField(fieldName, textField);
		fBGColorManager.add(textField);
		fTextFieldHashTable.put(fieldName, textField);
		fFontManager.addComponent(textField);
	}

	public void 	setText(String fieldName, String text) {
		TextField	textField = fTextFieldHashTable.get(fieldName);

		if (textField != null)
			textField.setText(text);
	}

	public String 	getText(String fieldName) {
		TextField textField = fTextFieldHashTable.get(fieldName);

		if (textField != null)
			return(textField.getText());
	  	else
			return ("");
	}

	public void 	addBoolean(String fieldName) {
		Checkbox checkbox = new Checkbox();
		addInputField(fieldName, checkbox);
		fCheckBoxHashTable.put(fieldName, checkbox);
	}
	
	public void 	setBoolean(String fieldName, boolean value) {
		Checkbox checkbox = fCheckBoxHashTable.get(fieldName);
		checkbox.setState(value);
	}
	
	public boolean getBoolean(String fieldName) {
		Checkbox checkbox = fCheckBoxHashTable.get(fieldName);
		return(checkbox.getState());
	}
    // =============================
    // Private Utilities functions
    // =============================
    private void addInputField(
        String      fieldName,
        Component   inputField) {

    	GridBagLayout gridBag = (GridBagLayout) getLayout();
    	
        Label label = new Label(fieldName);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      	= GridBagConstraints.NONE;
        gbc.anchor    	= GridBagConstraints.EAST;
        gbc.gridwidth 	= 1;
        gbc.weightx   	= 0.0;
        gbc.weighty   	= 0.0;
		gbc.insets.top	= 2;
		gbc.insets.left	= 2;
		gbc.insets.bottom	= 0;
		gbc.insets.right	= 0; // (2,2,0,0)
        gridBag.setConstraints(label, gbc);
        add(label);
		fFontManager.addComponent(label);

        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1.0;
		gbc.insets.left	= 0;
		gbc.insets.right	= 2;  // (2, 0, 0, 2);
        gridBag.setConstraints(inputField, gbc);
        add(inputField);
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
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.CompoentUtil
// 1.95 : 16-Jul-98 Y.Shibata   modified to use msgtool.db.*
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.24 :  4-May-99	Y.Shibata	re-implemented with EditableDialog/EditableListener interface
// 2.35 : 24-Oct-99	Y.Shibata	EditDialog -> EditUI
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
// 2.52 : 13-Nov-04	Y.Shibata	refactored
