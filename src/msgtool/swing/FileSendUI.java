// File: FileSendUI.java - last edit:
// Yoshiki Shibata 4-Jan-04

// Copyright (c) 1998, 1999, 2004 by Yoshiki Shibata

package msgtool.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

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
public final class FileSendUI
	extends JFrame
	implements ActionListener, PropertyChangeListener {
	private Container fContentPane = null; // Swing
	private JFrame fParentFrame = null;
	private JMenuItem fMenuItem = null;
	private JLabel fToLabel = null;
	private JTextField fToList = null;
	private JLabel fFileLabel = null;
	private JTextField fFileFullPathname = null;
	private JButton fFileButton = null;
	private JButton fSendButton = null;
	private JButton fCancelButton = null;
	private JFileChooser fFileChooser = null;

	private JPopupMenu fRecipientHintsPopup = null;

	private PropertiesDB fPropertiesDB = PropertiesDB.getInstance();
	private RecipientHintsDB fRecipientHintsDB = RecipientHintsDB.getInstance();
	private AddressDB fAddressDB = AddressDB.instance();

	private boolean fPreserveZeroLocation = false;

	private BGColorManager fBGColorManager = BGColorManager.getInstance();
	private ShiftKeyAdapter fShiftKeyAdapter = new ShiftKeyAdapter();

	public FileSendUI(JFrame parentFrame, JMenuItem menuItem) {
		super(StringDefs.SEND_A_FILE);

		fContentPane = getContentPane(); // Swing;
		fParentFrame = parentFrame;
		fMenuItem = menuItem;
		fPropertiesDB.addPropertyChangeListener(this);

		LFManager.getInstance().add(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//
		// Window Layouts
		// 
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setBackground(Color.lightGray);
		fContentPane.setLayout(gridBag);

		fRecipientHintsPopup = new JPopupMenu(StringDefs.RECIPIENT);
		//
		// To List
		//
		fToLabel = new JLabel(StringDefs.TO_C);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets.top = 4;
		constraints.insets.left = 2; // (4,2,0,0)
		gridBag.setConstraints(fToLabel, constraints);
		fContentPane.add(fToLabel);
		fToLabel.addMouseListener(
			new JPopupMenuAdapter(fToLabel, fRecipientHintsPopup));
		updateRecipientHintsPopup();

		fToList = new XJTextField(1);
		fBGColorManager.add(fToList);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weightx = 1.0;
		constraints.insets.top = 2;
		constraints.insets.right = 2; // (2,2,0,2)
		gridBag.setConstraints(fToList, constraints);
		fContentPane.add(fToList);
		fToList.addKeyListener(fShiftKeyAdapter);

		//
		// File  Name
		//
		fFileLabel = new JLabel(StringDefs.FILE_NAME_C);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 1;
		constraints.weightx = 0.0;
		constraints.insets.right = 0; // (2,2,0,0)
		gridBag.setConstraints(fFileLabel, constraints);
		fContentPane.add(fFileLabel);

		fFileFullPathname = new JTextField(20);
		fBGColorManager.add(fFileFullPathname);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 1.0;
		constraints.insets.right = 2; // (2,2,0,2)
		gridBag.setConstraints(fFileFullPathname, constraints);
		fContentPane.add(fFileFullPathname);
		
		new DropTarget(fFileFullPathname, 
				new FileDropper(new FileDropAcceptor() {
					public void accept(File file) {
						fFileFullPathname.setText(file.getAbsolutePath());	
					}
				}));

		fFileButton = new JButton(StringDefs.FILES_PPP);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weightx = 0.0;
		gridBag.setConstraints(fFileButton, constraints);
		fContentPane.add(fFileButton);
		fFileButton.addActionListener(this);
		//
		// Send / Cancel buttons
		// 
		JPanel panel = new JPanel();

		fSendButton = new JButton(StringDefs.SEND);
		fCancelButton = new JButton(StringDefs.CANCEL);

		fSendButton.addActionListener(this);
		fCancelButton.addActionListener(this);

		panel.add(fSendButton);
		panel.add(fCancelButton);

		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weightx = 1.0;
		constraints.insets.bottom = 2; //(2,2,2,2) 
		gridBag.setConstraints(panel, constraints);
		fContentPane.add(panel);
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
			String warning = null;
			File file = null;
			String[] recipients = null;

			if (fileName.length() == 0)
				warning = StringDefs.FILE_NOT_SPECIFIED;
			else {
				file = new File(fileName);
				recipients = StringUtil.parseToArray(fToList.getText());

				if (!file.exists())
					warning = StringDefs.FILE_NOT_FOUND;
				else if (!file.canRead())
					warning = StringDefs.FILE_NOT_READABLE;
				else if (!file.isFile())
					warning = StringDefs.FILE_NOT_FILE;
				else if (recipients == null)
					warning = StringDefs.RECIPIENT_NOT_SPECIFIED;
			}

			if (warning != null) {
				DialogUtil.showWarning(
					fParentFrame,
					StringDefs.ERROR,
					warning,
					false);
				toFront();
			} else {
				setVisible(false);
				FileSendThread fileSendThread =
					new FileSendThread(file, recipients);
				fileSendThread.start();
			}
		} else if (target == fCancelButton) {
			fFileFullPathname.setText("");
			setVisible(false);
		} else if (target == fFileButton) {
			if (fFileChooser == null)
				// fFileChooser = new JFileDialog(fParentFrame, StringDefs.kChooseAFile, FileDialog.LOAD);
				fFileChooser = new JFileChooser(".");

			ComponentUtil.overlapComponents(fFileChooser, this, -32, false);
			int status = fFileChooser.showOpenDialog(this);
			toFront();

			if (status == JFileChooser.APPROVE_OPTION)
				fFileFullPathname.setText(
					fFileChooser.getSelectedFile().getAbsolutePath());
		} else if (target instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) target;
			String menuLabel = item.getText();
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
			fMenuItem.setEnabled(false);
		} else {
			fMenuItem.setEnabled(true);
		}
		super.setVisible(visible);
	}
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
// 2.14 : 13-Feb-99	Y.Shibata	use JFileChooser
//								changed from JDialog to JFrame
// 2.15 : 27-Feb-99	Y.Shibata	used JPopupMenuAdapter class
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.32 : 13-Jun-99	Y.Shibata	used setDefaultCloseOperation(DISPOSE_ON_CLOSE)
// 2.35 :  5-Oct-99	Y.Shibata	replaced JTextField with XJTextField
//		  24-Oct-99	Y.Shibata	FileSendDialog -> FileSendUI
// 2.36 : 23-Nov-99	Y.Shibata	Refactoring....
// 2.50 :  4-Jan-04	Y.Shibata	Dropping a file is supported.
