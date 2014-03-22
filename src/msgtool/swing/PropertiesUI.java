// File: PropertiesUI.java - last edit:
// Yoshiki Shibata  20-Dec-2002

// Copyright (c) 1996 - 1999, 2002 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import msgtool.db.PropertiesDB;
import msgtool.util.ColorMap;
import msgtool.util.ComponentUtil;
import msgtool.util.StringDefs;

@SuppressWarnings("serial")
public final class PropertiesUI extends JDialog implements ActionListener,
    WindowListener, ItemListener {
  // User pane
  private JTextField fUserName = null;
  // Reception pane
  private JCheckBox fActivateOnReception = null;
  private JComboBox fActivateWindow = null;
  private JCheckBox fBeepOnReception = null;
  // Deliver panel
  private JComboBox fDeliverKey = null;
  // Logging pane
  private JCheckBox fSaveMessages = null;
  private JComboBox fNoOfLogFiles = null;
  // Preference Pane
  private JComboBox fFontName = null;
  private String[] fFontNameValues = null;

  private JComboBox fFontStyle = null;
  private int[] fFontStyleValues = null;
  private static final int kNoOfFontStyles = 4;

  private JComboBox fFontSize = null;
  private static final int kBaseOfFontSize = 9;
  private static final int kMaxFontSize = 18;

  private JComboBox fTextBackground = null;

  private JComboBox fLookAndFeel = null;
  private String[] fLookAndFeelValues = null;

  // Ok / Cancel
  private JButton fCancelButton = null;
  private JButton fSetButton = null;

  private Frame fParentFrame = null;

  private PropertiesDB fPropertiesDB = PropertiesDB.getInstance();

  public PropertiesUI(Frame parentFrame, String title) {
    super(parentFrame, title, false);
    Container contentPane = getContentPane();

    fParentFrame = parentFrame;
    addWindowListener(this);
    LFManager.getInstance().add(this);

    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    contentPane.setLayout(gridBag);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab(StringDefs.USER, createUserPane());
    tabbedPane.addTab(StringDefs.RECEPTION, createReceptionPane());
    tabbedPane.addTab(StringDefs.FUNCTION_KEY, createDeliverPane());
    tabbedPane.addTab(StringDefs.LOGGING, createLoggingPane());
    tabbedPane.addTab(StringDefs.PREFERENCES, createPrefernecPane());

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;
    gridBag.setConstraints(tabbedPane, c);
    contentPane.add(tabbedPane);

    //
    // Cancel / Set buttons
    //
    JPanel panel = new JPanel();

    fCancelButton = new JButton(StringDefs.CANCEL);
    fSetButton = new JButton(StringDefs.SET);

    fCancelButton.addActionListener(this);
    fSetButton.addActionListener(this);

    panel.add(fSetButton);
    panel.add(fCancelButton);
    // c.fill = GridBagConstraints.HORIZONTAL;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.EAST;
    c.weightx = 1.0;
    c.weighty = 1.0;
    gridBag.setConstraints(panel, c);
    contentPane.add(panel);
    pack();
    setResizable(false);
  }

  private void setLabel(GridBagLayout gridBag, GridBagConstraints c,
      String name, JPanel panel) {
    JLabel label = new JLabel(name);

    c.anchor = GridBagConstraints.EAST;
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 1;
    c.weightx = 0.0;
    c.weighty = 0.0;
    c.insets.top = 2;
    c.insets.left = 2;
    c.insets.bottom = 0;
    c.insets.right = 2; // (2, 2, 0, 2)
    gridBag.setConstraints(label, c);
    panel.add(label);
  }

  private void setLabelLast(GridBagLayout gridBag, GridBagConstraints c,
      String name, JPanel panel) {
    JLabel label = new JLabel(name);

    c.anchor = GridBagConstraints.EAST;
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 1;
    c.weightx = 0.0;
    c.weighty = 0.0;
    c.insets.top = 2;
    c.insets.left = 2;
    c.insets.bottom = 2;
    c.insets.right = 2; // (2, 2, 2, 2)
    gridBag.setConstraints(label, c);
    panel.add(label);
  }

  private JPanel createUserPane() {
    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    JPanel panel = new JPanel();

    panel.setLayout(gridBag);
    setLabel(gridBag, c, StringDefs.USER_NAME_C, panel);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 0.0;
    fUserName = new JTextField(10);
    fUserName.setBackground(ColorMap.getColorByName(fPropertiesDB
        .getTextBackground()));
    gridBag.setConstraints(fUserName, c);
    panel.add(fUserName);
    return (panel);
  }

  private JPanel createReceptionPane() {
    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    JPanel panel = new JPanel();

    panel.setLayout(gridBag);
    setLabel(gridBag, c, StringDefs.ACTIVE_ON_RECEPTION_C, panel);

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = 1;
    c.weightx = 1.0;
    c.weighty = 0.0;
    fActivateOnReception = new JCheckBox();
    fActivateOnReception.addItemListener(this);
    gridBag.setConstraints(fActivateOnReception, c);
    panel.add(fActivateOnReception);

    fActivateWindow = new JComboBox();
    fActivateWindow.setEditable(false);
    fActivateWindow.addItem(StringDefs.MESSAGING_DIALOG);
    fActivateWindow.addItem(StringDefs.RECEPTION_DIALOG);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(fActivateWindow, c);
    panel.add(fActivateWindow);

    // 
    // Beep On Reception
    //
    setLabel(gridBag, c, StringDefs.BEEP_ON_RECEPTION, panel);

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 0.0;
    fBeepOnReception = new JCheckBox();
    gridBag.setConstraints(fBeepOnReception, c);
    panel.add(fBeepOnReception);
    return (panel);
  }

  private JPanel createDeliverPane() {
    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    JPanel panel = new JPanel();

    panel.setLayout(gridBag);
    setLabel(gridBag, c, StringDefs.DELIVER_KEY_C, panel);
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 0.0;
    fDeliverKey = new JComboBox();
    fDeliverKey.setEditable(false);
    fDeliverKey.addItem("F1");
    fDeliverKey.addItem("F2");
    fDeliverKey.addItem("F3");
    fDeliverKey.addItem("F4");
    fDeliverKey.addItem("F5");
    fDeliverKey.addItem("F6");
    fDeliverKey.addItem("F7");
    fDeliverKey.addItem("F8");
    fDeliverKey.addItem("F9");
    fDeliverKey.addItem("F10");
    fDeliverKey.addItem("F11");
    fDeliverKey.addItem("F12");
    gridBag.setConstraints(fDeliverKey, c);
    panel.add(fDeliverKey);
    return (panel);
  }

  private JPanel createLoggingPane() {
    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    JPanel panel = new JPanel();

    panel.setLayout(gridBag);
    setLabel(gridBag, c, StringDefs.LOG_MESSAGES_C, panel);

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 0.0;
    fSaveMessages = new JCheckBox();
    fSaveMessages.addItemListener(this);
    gridBag.setConstraints(fSaveMessages, c);
    panel.add(fSaveMessages);

    setLabel(gridBag, c, StringDefs.MAX_LOG_FILES_C, panel);
    fNoOfLogFiles = new JComboBox();
    fNoOfLogFiles.setEditable(false);
    fNoOfLogFiles.addItem("1");
    fNoOfLogFiles.addItem("2");
    fNoOfLogFiles.addItem("3");
    fNoOfLogFiles.addItem("4");
    fNoOfLogFiles.addItem("5");
    fNoOfLogFiles.addItem("6");
    fNoOfLogFiles.addItem("7");
    fNoOfLogFiles.addItem("8");
    fNoOfLogFiles.addItem("9");
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 0.0;
    gridBag.setConstraints(fNoOfLogFiles, c);
    panel.add(fNoOfLogFiles);
    return (panel);
  }

  private JPanel createPrefernecPane() {
    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    JPanel panel = new JPanel();

    panel.setLayout(gridBag);

    //
    // Font List
    //
    setLabel(gridBag, c, StringDefs.FONT_NAME_C, panel);

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;
    fFontName = new JComboBox();
    fFontName.setEditable(false);

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fonts = ge.getAvailableFontFamilyNames();

    fFontNameValues = new String[fonts.length];
    for (int i = 0; i < fonts.length; i++) {
      fFontName.addItem(fonts[i]);
      fFontNameValues[i] = fonts[i];
    }
    gridBag.setConstraints(fFontName, c);
    panel.add(fFontName);
    //
    // Font Style
    //
    setLabel(gridBag, c, StringDefs.FONT_STYLE_C, panel);

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;
    fFontStyle = new JComboBox();
    fFontStyle.setEditable(false);
    fFontStyleValues = new int[kNoOfFontStyles];

    fFontStyle.addItem(StringDefs.PLAIN);
    fFontStyleValues[0] = Font.PLAIN;
    fFontStyle.addItem(StringDefs.BOLD);
    fFontStyleValues[1] = Font.BOLD;
    fFontStyle.addItem(StringDefs.ITALIC);
    fFontStyleValues[2] = Font.ITALIC;
    fFontStyle.addItem(StringDefs.BOLD_ITALIC);
    fFontStyleValues[3] = Font.BOLD | Font.ITALIC;

    gridBag.setConstraints(fFontStyle, c);
    panel.add(fFontStyle);
    //
    // Font Size
    //
    setLabel(gridBag, c, StringDefs.FONT_SIZE_C, panel);

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;
    fFontSize = new JComboBox();
    fFontSize.setEditable(false);

    fFontSize.addItem("9");
    fFontSize.addItem("10");
    fFontSize.addItem("11");
    fFontSize.addItem("12");
    fFontSize.addItem("13");
    fFontSize.addItem("14");
    fFontSize.addItem("15");
    fFontSize.addItem("16");
    fFontSize.addItem("17");
    fFontSize.addItem("18");

    gridBag.setConstraints(fFontSize, c);
    panel.add(fFontSize);

    //
    // Text Background Color
    //
    setLabel(gridBag, c, StringDefs.TEXT_BACKGROUND_C, panel);

    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;

    fTextBackground = new JComboBox(new ColorListModel());
    fTextBackground.setRenderer(new ColorListRenderer());
    fTextBackground.setSelectedIndex(0);
    fTextBackground.setEditable(false);

    gridBag.setConstraints(fTextBackground, c);
    panel.add(fTextBackground);
    //
    // Look And Feel
    //
    setLabelLast(gridBag, c, StringDefs.LOOK_AND_FEEL_C, panel);
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.WEST;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;
    fLookAndFeel = new JComboBox();
    fLookAndFeel.setEditable(false);

    UIManager.LookAndFeelInfo[] lfInfos = UIManager.getInstalledLookAndFeels();
    fLookAndFeelValues = new String[lfInfos.length];

    for (int i = 0; i < lfInfos.length; i++) {
      fLookAndFeel.addItem(lfInfos[i].getName());
      fLookAndFeelValues[i] = lfInfos[i].getName();
    }
    gridBag.setConstraints(fLookAndFeel, c);
    panel.add(fLookAndFeel);
    return (panel);
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

    if (target == fSetButton) {
      String userName = fUserName.getText();
      if (userName != null && userName.length() > 0)
        fPropertiesDB.setUserName(userName);

      fPropertiesDB.setActivateOnReception(fActivateOnReception.isSelected());
      fPropertiesDB.setActivateWindowChoice(fActivateWindow.getSelectedIndex());
      fPropertiesDB.setBeepOnReception(fBeepOnReception.isSelected());
      fPropertiesDB.setDeliverKey(fDeliverKey.getSelectedIndex());
      fPropertiesDB.setSaveMessages(fSaveMessages.isSelected());
      fPropertiesDB.setNoOfMessageFiles(fNoOfLogFiles.getSelectedIndex() + 1);
      fPropertiesDB.setFontName((String) fFontName.getSelectedItem());
      fPropertiesDB
          .setFontStyle(fFontStyleValues[fFontStyle.getSelectedIndex()]);
      fPropertiesDB.setFontSize(fFontSize.getSelectedIndex() + kBaseOfFontSize);
      fPropertiesDB.setTextBackground((String) fTextBackground
          .getSelectedItem());
      fPropertiesDB.setLFName((String) fLookAndFeel.getSelectedItem());
      fPropertiesDB.saveProperties(new ActionEvent(this,
          ActionEvent.ACTION_PERFORMED, ""));
      setVisible(false);
    } else if (target == fCancelButton) {
      setVisible(false);
    }
  }

  // ================================
  // ItemListener
  // ================================
  public void itemStateChanged(ItemEvent event) {
    Object item = event.getItemSelectable();

    if (item == fActivateOnReception)
      fActivateWindow.setEnabled(fActivateOnReception.isSelected());
    else if (item == fSaveMessages)
      fNoOfLogFiles.setEnabled(fSaveMessages.isSelected());
  }

  // ==============================
  // Show
  // ==============================
  public void setVisible(boolean visible) {
    if (!visible) {
      super.setVisible(visible);
      return;
    }

    boolean activateOnReception = fPropertiesDB.isActivateOnReception();

    fUserName.setText(fPropertiesDB.getUserName());
    fActivateOnReception.setSelected(activateOnReception);
    fActivateWindow.setEnabled(activateOnReception);
    fBeepOnReception.setSelected(fPropertiesDB.isBeepOnReception());
    try {
      fDeliverKey.setSelectedIndex(fPropertiesDB.getDeliverKey());
      fActivateWindow.setSelectedIndex(fPropertiesDB.getActivateWindowChoice());

      boolean saveMessages = fPropertiesDB.isSaveMessages();
      fSaveMessages.setSelected(saveMessages);
      fNoOfLogFiles.setEnabled(saveMessages);
      int noOfMessageFiles = fPropertiesDB.getNoOfMessageFiles();
      fNoOfLogFiles.setSelectedIndex(noOfMessageFiles - 1);
      //
      // Font Name
      //
      String fontName = fPropertiesDB.getFontName();

      int fontIndex = 0; // in case of not found.
      for (int i = 0; i < fFontNameValues.length; i++) {
        if (fFontNameValues[i].equals(fontName)) {
          fontIndex = i;
          break;
        }
      }
      fFontName.setSelectedIndex(fontIndex);
      //
      // Font Style
      //
      int fontStyle = fPropertiesDB.getFontStyle();
      for (int i = 0; i < kNoOfFontStyles; i++)
        if (fFontStyleValues[i] == fontStyle) {
          fFontStyle.setSelectedIndex(i);
          break;
        }
      //
      // Font Size
      //
      int fontSize = fPropertiesDB.getFontSize();
      if (fontSize < kBaseOfFontSize)
        fontSize = kBaseOfFontSize;
      else if (fontSize > kMaxFontSize)
        fontSize = kMaxFontSize;
      fFontSize.setSelectedIndex(fontSize - kBaseOfFontSize);
      //
      // TextBackground
      //
      String textBackground = ColorMap.getColorName(ColorMap
          .getColorByName(fPropertiesDB.getTextBackground()));
      String[] colorNames = ColorMap.getColorNames();

      for (int i = 0; i < colorNames.length; i++)
        if (textBackground.equals(colorNames[i])) {
          fTextBackground.setSelectedIndex(i);
          break;
        }

      //
      // Look And Feel
      //
      String lfName = fPropertiesDB.getLFName();
      if (lfName == null)
        lfName = UIManager.getLookAndFeel().getName();

      for (int i = 0; i < fLookAndFeelValues.length; i++) {
        if (fLookAndFeelValues[i].equals(lfName)) {
          fLookAndFeel.setSelectedIndex(i);
          break;
        }
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    ComponentUtil.centerComponent(this, fParentFrame.getLocation(),
        fParentFrame.getSize());
    super.setVisible(true);
  }

}

// LOG
// 1-Sep-96 Y.Shibata created
// 15-Feb-97 Y.Shibata added "Beep On Reception"
// 1.02 : 22-Feb-97 Y.Shibata fixed the location of the choice of
// "Activate On Reception"
// 1.20 : 29-Mar-97 Y.Shibata chagned show() to setVisible()
// non-resizable window.
// 1.30 : 17-Apr-97 Y.Shibata deleted code for TimeZone
// 1.35 : 11-Jul-97 Y.Shibata centering this window on top of the main window.
// 1.37 : 17-Jul-97 Y.Shibata changed locations of buttons.
// 1.83 : 20-Dec-97 Y.Shibata modification for JDK1.2beta2:
// import java.awt.List (java.util.List is added to JDK1.2)
// --- Swing ---
// 1.83 : 23-Dec-97 Y.Shibata Dialog -> JDialog
// 1.94 : 5-Jul-98 Y.Shibata modified to use msgtool.util.Component
// 1.95b7 : 30-Aug-98 Y.Shibata used TabbedPane.
// 2.22 : 11-Apr-99 Y.Shibata modified not to new Insets
// 2.34 : 9-Sep-99 Y.Shibata changed the way to check JDK versions
// 2.35 : 24-Oct-99 Y.Shibata PropertiesDialog -> PropertiesUI
// 2.50 : 30-Dec-02 Y.Shibata got rid of 1.1 compatible code

