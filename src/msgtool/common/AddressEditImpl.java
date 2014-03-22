// File: AddressEditImpl.java - last edit:
// Yoshiki Shibata 13-Nov-2004

// Copyright (c) 1999, 2003, 2004 by Yoshiki Shibata
// All rights reserved.

package msgtool.common;

import java.util.Vector;

import msgtool.EditableDialog;
import msgtool.EditableListener;
import msgtool.db.AddressDB;
import msgtool.util.StringDefs;

public class AddressEditImpl implements EditableListener {

	public AddressEditImpl() { }

	// ==================================
	// Implementation of EditableListener
	// ==================================
	public String title() {
		return StringDefs.EDIT_ADDRESS_CACHE;
	}

  	public void construct(EditableDialog dialog) {
		fEditableDialog = dialog;
		fEditableDialog.addText(kName);
		fEditableDialog.addText(kSortKey);
		fEditableDialog.addText(kAddress);
		fEditableDialog.addBoolean(kIsHinted);
	}

	public void show() {
		clearAllFields();
		fEditableDialog.removeAllItems();
		fAddressInfoList.removeAllElements();

		String[] names = fAddressDB.getListOfAddressCache();

	  	SortUtil.sortStringsBySortKey(names);
	  	for (String name: names) {
			fEditableDialog.addItem(name);
			fAddressInfoList.addElement(new AddressInfo(
					fAddressDB.lookUpAddressCache(name),
					fAddressDB.lookUpKeyCache(name),
					fAddressDB.isHinted(name)));
		}
	}

	public void save() {
		int				noOfNames	= fEditableDialog.getItemCount();
		AddressInfo		addressInfo	= null;
		String			recipient	= null;

		fAddressDB.clearAddressCache();
		fAddressDB.clearKeyCache();
		for (int i = 0; i < noOfNames; i++) {
			addressInfo = fAddressInfoList.elementAt(i);
			recipient = fEditableDialog.getItemAt(i);
			fAddressDB.addAddressCache(recipient, addressInfo.address);
			fAddressDB.addKeyCache(recipient, addressInfo.sortKey);
			fAddressDB.setHinted(recipient, addressInfo.isHinted);
		}
		fEditableDialog.removeAllItems();
		fAddressInfoList.removeAllElements();
		fAddressDB.save();
	}

	public void add() {
		String	name		= fEditableDialog.getText(kName);
		String	address		= fEditableDialog.getText(kAddress);
		String	sortKey		= fEditableDialog.getText(kSortKey);
		boolean	isHinted	= fEditableDialog.getBoolean(kIsHinted);

		if (name.length() == 0 || address.length() == 0)
			return;

	  	AddressInfo	addressInfo = new AddressInfo(address, sortKey, isHinted);
		//
		// Check if this name is already listed. If it is listed,
		// then just replace its assocaited address.
		//
		int index = fEditableDialog.getItemIndex(name);
		if (index == -1) {
			fEditableDialog.addItem(name);
			fAddressInfoList.addElement(addressInfo);
			}
		else
			fAddressInfoList.setElementAt(addressInfo, index);

		clearAllFields();
	}

	public void delete(int selectedIndex) {
	  	fEditableDialog.removeItemAt(selectedIndex);
		fAddressInfoList.removeElementAt(selectedIndex);
		clearAllFields();
	}

	public void update(int selectedIndex) {
		fEditableDialog.removeItemAt(selectedIndex);
		fAddressInfoList.removeElementAt(selectedIndex);
		add();
		clearAllFields();
	}

	public void select(int selectedIndex) {
		String	name = fEditableDialog.getItemAt(selectedIndex);
		AddressInfo	addressInfo = fAddressInfoList.elementAt(selectedIndex);

		fEditableDialog.setText(kName, name);
		fEditableDialog.setText(kAddress, addressInfo.address);
		fEditableDialog.setText(kSortKey, addressInfo.sortKey);
		fEditableDialog.setBoolean(kIsHinted, addressInfo.isHinted);
	}

	// ===================================
	// Private methods
	// ===================================
	private void clearAllFields() {
		fEditableDialog.setText(kName, "");
		fEditableDialog.setText(kAddress, "");
		fEditableDialog.setText(kSortKey, "");
		fEditableDialog.setBoolean(kIsHinted, false);
	}
	// ===================================
	// Private fields
	// ===================================
	private final static String	kName		= StringDefs.NAME_C;
	private final static String	kSortKey	= StringDefs.SORT_KEY_C;
	private final static String	kAddress	= StringDefs.ADDRESS_C;
	private final static String	kIsHinted	= StringDefs.LIST_AS_HINT;

	private AddressDB	fAddressDB = AddressDB.instance();

 	private EditableDialog	fEditableDialog	= null;
	
	private Vector<AddressInfo>	fAddressInfoList	= new Vector<AddressInfo>();

	// ====================================
	// Private inner classes
	// ====================================
	private class AddressInfo {
		public String	address;
		public String	sortKey;
		public boolean	isHinted;

		public AddressInfo(
	  		String	address,
			String	sortKey,
			boolean	isHinted ) {
	 		this.address 	= address;
			this.sortKey	= sortKey;
			this.isHinted	= isHinted;
		}
	}
}

// LOG
// 2.24 :  4-May-99	Y.Shibata	created.
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
// 2.52 : 13-Nov-04	Y.Shibata	refactored
