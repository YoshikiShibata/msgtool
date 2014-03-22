// File: FileSendUI.java - last edit:
// Yoshiki Shibata 4-Jan-04

// Copyright (c) 1998, 1999, 2004 by Yoshiki Shibata

package msgtool.awt;

import java.awt.Button;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.FileSendThread;
import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.db.RecipientHintsDB;
import msgtool.dnd.FileDropAcceptor;
import msgtool.dnd.FileDropper;
import msgtool.util.ComponentUtil;
import msgtool.util.ShiftKeyAdapter;
import msgtool.util.StringDefs;
import msgtool.util.StringUtil;

@SuppressWarnings("serial")
public final class FileSendUI extends Frame
	implements ActionListener, WindowListener, PropertyChangeListener {
	
	private Frame fParentFrame = null;
	private Label fToLabel = null;
	private TextField fToList = null;
	private Label fFileLabel = null;
	private TextField fFileFullPathname = null;
	private Button fFileButton = null;
	private Button fSendButton = null;
	private Button fCancelButton = null;
	private FileDialog fFileDialog = null;

	private PopupMenu fRecipientHintsPopup = null;

	private PropertiesDB fPropertiesDB = PropertiesDB.getInstance();
	private RecipientHintsDB fRecipientHintsDB = RecipientHintsDB.getInstance();
	private AddressDB fAddressDB = AddressDB.instance();

	private boolean fPreserveZeroLocation = false;

	private BGColorManager fBGColorManager = BGColorManager.getInstance();
	private ShiftKeyAdapter fShiftKeyAdapter = new ShiftKeyAdapter();

	public FileSendUI(Frame parentFrame) {
		super(StringDefs.SEND_A_FILE);

		fParentFrame = parentFrame;
		addWindowListener(this);
		fPropertiesDB.addPropertyChangeListener(this);
		//
		// Window Layouts
		// 
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setBackground(Color.lightGray);
		setLayout(gridBag);

		fRecipientHintsPopup = new PopupMenu(StringDefs.RECIPIENT);
		//
		// To List
		//
		fToLabel = new Label(StringDefs.TO_C);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets.top = 4;
		constraints.insets.left = 2;
		constraints.insets.bottom = 0;
		constraints.insets.right = 0; // (4, 2, 0, 0)
		gridBag.setConstraints(fToLabel, constraints);
		add(fToLabel);
		fToLabel.addMouseListener(
			new PopupMenuAdapter(fToLabel, fRecipientHintsPopup));
		updateRecipientHintsPopup();

		fToList = new XTextField(1);
		fBGColorManager.add(fToList);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weightx = 1.0;
		constraints.insets.top = 2;
		constraints.insets.right = 2; // (2, 2, 0, 2)
		gridBag.setConstraints(fToList, constraints);
		add(fToList);
		fToList.addKeyListener(fShiftKeyAdapter);

		//
		// File  Name
		//
		fFileLabel = new Label(StringDefs.FILE_NAME_C);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 1;
		constraints.weightx = 0.0;
		constraints.insets.right = 0; // (2, 2, 0, 0)
		gridBag.setConstraints(fFileLabel, constraints);
		add(fFileLabel);

		fFileFullPathname = new TextField(40);
		fBGColorManager.add(fFileFullPathname);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 1.0;
		constraints.insets.right = 2; // (2, 2, 0, 2)
		gridBag.setConstraints(fFileFullPathname, constraints);
		add(fFileFullPathname);

		new DropTarget(
			fFileFullPathname,
			new FileDropper(new FileDropAcceptor() {
			public void accept(File file) {
				fFileFullPathname.setText(file.getAbsolutePath());
			}
		}));

		fFileButton = new Button(StringDefs.FILES_PPP);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weightx = 0.0;
		gridBag.setConstraints(fFileButton, constraints);
		add(fFileButton);
		fFileButton.addActionListener(this);
		//
		// Send / Cancel buttons
		// 
		Panel panel = new Panel();

		fSendButton = new Button(StringDefs.SEND);
		fCancelButton = new Button(StringDefs.CANCEL);

		fSendButton.addActionListener(this);
		fCancelButton.addActionListener(this);

		panel.add(fSendButton);
		panel.add(fCancelButton);

		constraints.weightx = 1.0;
		constraints.insets.bottom = 2; // (2, 2, 2, 2)
		gridBag.setConstraints(panel, constraints);
		add(panel);
		//
		// set Font
		//
		setFonts();

		pack();
	}
	//
	// set Fonts
	//
	public void setFonts() {
		Font font = Context.getFont();

		if (font == null)
			return;

		fToLabel.setFont(font);
		fToList.setFont(font);
		fFileLabel.setFont(font);
		fFileFullPathname.setFont(font);
		fFileButton.setFont(font);
		fSendButton.setFont(font);
		fCancelButton.setFont(font);
		Util.setFontsToMenu(fRecipientHintsPopup, font);
	}
	// =================================
	// WindowListener
	// =================================
	public void windowClosed(WindowEvent event) {
		setVisible(false);
	}
	public void windowDeiconified(WindowEvent event) {
	}
	public void windowIconified(WindowEvent event) {
	}
	public void windowActivated(WindowEvent event) {
	}
	public void windowDeactivated(WindowEvent event) {
	}
	public void windowOpened(WindowEvent event) {
	}
	public void windowClosing(WindowEvent event) {
		setVisible(false);
	}

	// ================================
	// ActionListener
	// ================================
	public void actionPerformed(ActionEvent event) {
		Object target = event.getSource();

		if (target == fSendButton) {
			//
			// make sure the the specified file exits
			//
			String fileName = fFileFullPathname.getText();
			File file = null;
			String[] recipients = null;
			WarningUI warning = null;

			if (fileName.length() == 0)
				warning = new WarningUI(
						fParentFrame,
						StringDefs.ERROR,
						StringDefs.FILE_NOT_SPECIFIED);
			else {
				file = new File(fileName);
				recipients = StringUtil.parseToArray(fToList.getText());

				if (!file.exists())
					warning = new WarningUI(
							fParentFrame,
							StringDefs.ERROR,
							StringDefs.FILE_NOT_FOUND);
				else if (!file.canRead())
					warning = new WarningUI(
							fParentFrame,
							StringDefs.ERROR,
							StringDefs.FILE_NOT_READABLE);
				else if (!file.isFile())
					warning = new WarningUI(
							fParentFrame,
							StringDefs.ERROR,
							StringDefs.FILE_NOT_FILE);
				else if (recipients == null)
					warning = new WarningUI(
							fParentFrame,
							StringDefs.ERROR,
							StringDefs.RECIPIENT_NOT_SPECIFIED);
			}

			if (warning != null)
				warning.setVisible(true);
			else {
				setVisible(false);
				FileSendThread fileSendThread =
					new FileSendThread(file, recipients);
				fileSendThread.start();
			}
		} else if (target == fCancelButton) {
			fFileFullPathname.setText("");
			setVisible(false);
		} else if (target == fFileButton) {
			if (fFileDialog == null)
				fFileDialog = new FileDialog(
						fParentFrame,
						StringDefs.CHOOSE_A_FILE,
						FileDialog.LOAD);

			ComponentUtil.overlapComponents(fFileDialog, this, -32, false);
			fFileDialog.setVisible(true);
			toFront();
			String directory = fFileDialog.getDirectory();
			String file = fFileDialog.getFile();

			if (directory != null && file != null)
				fFileFullPathname.setText(
					new File(directory, file).getAbsolutePath());
		} else if (target instanceof MenuItem) {
			MenuItem item = (MenuItem) target;
			String menuLabel = item.getLabel();
			String expandedHint =
				fRecipientHintsDB.getExpandedRecipients(menuLabel);

			Util.recipientHintSelected(
				expandedHint,
				fToList,
				fShiftKeyAdapter.isShiftKeyPressed());
		}
	}
	// ======================
	// PropertyChangeListener
	// ======================
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PropertiesDB.kName)) {
			setFonts();
		}
	}
	// ==========================================
	// Public Methods not defined in any Listener
	// ==========================================
	//
	// setVisible
	//
	public void setVisible(boolean visible) {
		if (visible) {
			fFileFullPathname.setText("");
			ComponentUtil.overlapComponents(
				this,
				fParentFrame,
				32,
				fPreserveZeroLocation);
			fPreserveZeroLocation = true;
		} else {
			if (fFileDialog != null)
				fFileDialog.setVisible(false);
		}
		super.setVisible(visible);
	}
	// =====================================
	// Public Methods defined by this class
	// =====================================    
	//
	// update recipient hints of the popup menu.
	//
	public void updateRecipientHintsPopup() {
		Util.updateHintsMenu(
			fRecipientHintsPopup,
			fRecipientHintsDB.getDB(),
			fAddressDB.getHintedAddressDB(),
			this);
		Util.setFontsToMenu(fRecipientHintsPopup, Context.getFont());
	}
}

// LOG
// 2.10 : 13-Sep-98 Y.Shibata   created
// 2.15 : 27-Feb-99	Y.Shibata	used PopupMenuAdapter class
// 2.35 :  5-Oct-99	Y.Shibata	replace TextField with XTextField.
//                              changed from Dialog to Frame
//        24-Oct-99	Y.Shibata	FileSendDialog -> FileSendUI
// 2.36 : 23-Nov-99	Y.Shibata	Refactoring....
// 2.50 :  4-Jan-04	Y.Shibata	Dropping a file is supported.
