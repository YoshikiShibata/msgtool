// File: MainFrame.java - last edit:
// Yoshiki Shibata 22-Mar-14

// Copyright (c) 1996 - 2000, 2003, 2014 by Yoshiki Shibata. 
// All rights reserved.

package msgtool.swing;

import java.awt.AWTEvent;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import msgtool.Deliverer;
import msgtool.DelivererImpl;
import msgtool.MainFrameFeatures;
import msgtool.MainFrameFeaturesImpl;
import msgtool.MessagingToolVersion;
import msgtool.OnlineWatcher;
import msgtool.common.AddressEditImpl;
import msgtool.common.BGColorManager;
import msgtool.common.Context;
import msgtool.common.FTPListenerFactoryImpl;
import msgtool.common.KeyUtil;
import msgtool.common.MeetingRoomEditImpl;
import msgtool.common.MessageProtocolListenerImpl;
import msgtool.common.MiscProtocolListenerImpl;
import msgtool.common.MultipleNotifier;
import msgtool.common.RecipientEditImpl;
import msgtool.common.RecordedMsgEditImpl;
import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.db.RecipientHintsDB;
import msgtool.db.RecordedMsgHintsDB;
import msgtool.log.LogArea;
import msgtool.log.Logging;
import msgtool.meeting.MeetingListenerImpl;
import msgtool.meeting.MeetingManager;
import msgtool.meeting.MeetingManagerImpl;
import msgtool.protocol.FTP;
import msgtool.protocol.MeetingListener;
import msgtool.protocol.MeetingProtocol;
import msgtool.protocol.MessageProtocol;
import msgtool.protocol.MessageProtocolListener;
import msgtool.protocol.MiscProtocol;
import msgtool.swing.tools.Stickies;
import msgtool.swing.tools.StickyNote;
import msgtool.ui.AboutUI;
import msgtool.ui.DedicatedUI;
import msgtool.ui.DedicatedUIManager;
import msgtool.ui.MainUI;
import msgtool.ui.MeetingRoomUI;
import msgtool.ui.MeetingRoomUIManager;
import msgtool.ui.OnlineListUI;
import msgtool.ui.SearchUI;
import msgtool.ui.UIFactory;
import msgtool.util.ColorMap;
import msgtool.util.ComponentUtil;
import msgtool.util.CursorControl;
import msgtool.util.MessageControl;
import msgtool.util.ShiftKeyAdapter;
import msgtool.util.StringDefs;
import msgtool.util.TimerUtil;

@SuppressWarnings("serial")
public final class MainFrame
        extends JFrame
        implements Observer, ActionListener, ItemListener, WindowListener, PropertyChangeListener,
        MainFrameFeatures {

    private String kToolTitle = null;

    private JMenuBar fMenuBar = null;
    private JButton fDeliverButton = null;
    private JCheckBox fNotInOfficeCheckBox = null;
    private boolean fNotInOffice = false;
    private JPanel fSecondPanel = null;
    private JTextField fToList = null;
    private String fRecordedMsg = "";
    private String fSavedMsg = "";
    private JLabel fToLabel = null;
    private JLabel fRecordedMsgLabel = null;

    private StyledTextArea fInputArea = null;
    private StyledTextArea fLogTextArea = null;
    private JSplitPane fSplitter = null;

    private JMenu fFileMenu = null;
    private JMenu fFileSavedMessages = null;
    private JMenu fFileSavedMeetingMessages = null;
    private JMenuItem fFileQuitItem = null;

    private JMenu fHelpMenu = null;
    private JMenu fAboutMenu = null;
    private JMenuItem fAboutMessagingToolItem = null;
    private JMenuItem fAboutSystemPropertiesItem = null;
    private JMenuItem fOverviewItem = null;
    private JMenuItem fTopicsItem = null;

    private JMenu fEditMenu = null;
    private JMenuItem fEditAddressFileItem = null;
    private JMenuItem fEditRecipientFileItem = null;
    private JMenuItem fEditRecordedMsgFileItem = null;
    private JMenuItem fEditMeetingRoomFileItem = null;
    private JMenuItem fEditPropertiesItem = null;

    private JMenu fWindowMenu = null;
    private JMenuItem fWindowAnotherItem = null;
    private JMenu fWindowMessagingDialogs = null;
    private JMenu fWindowMeetingRooms = null;
    private JMenuItem fWindowOnlineListItem = null;
    private JMenuItem fWindowMeetingRoomListItem = null;

    private JMenu fToolMenu = null;
    private JMenuItem fToolSendFileItem = null;
    private JMenuItem fToolSearchUserItem = null;
    private JMenuItem fToolNewMeetingRoomItem = null;
    private JMenuItem fToolDeleteMeetingRoomItem = null;

    private JPopupMenu fRecipientHintsPopup = null;
    private JPopupMenu fRecordedMsgHintsPopup = null;

    final static String kToList = StringDefs.TO_C;
    final static String kRecordedMsg = StringDefs.RECORDED_MESSAGE_C;

    private AboutUIImpl fAboutMessagingTool = null;
    private AboutUIImpl fAboutSystemProperties = null;
    private AboutUIImpl fAboutMessageReceived = null;

    private EditUI fEditAddressFile = null;
    private EditUI fEditRecipientFile = null;
    private EditUI fEditRecordedMsgFile = null;
    private EditUI fEditMeetingRoomFile = null;

    private PropertiesUI fPropertiesUI = null;
    private AnotherUI fAnotherUI = null;
    private OnlineListUI fOnlineListUI = null;
    private MeetingRoomListUI fMeetingRoomListUI = null;
    private SearchUI fSearchUI = null;
    private NewMeetingRoomUI fNewMeetingRoomUI = null;
    private NewMeetingRoomUI fDeleteMeetingRoomUI = null;
    private MeetingListener fMeetingListener = null;

    private final static int kNormalIcon = 0;
    private final static int kNotInOfficeIcon = 1;
    private final static int kWaitingIcon = 2;
    private boolean fIconified = false;

    private ShiftKeyAdapter fShiftKeyAdapter = new ShiftKeyAdapter();

    private FileSendUI fFileSendUI = null;

    private LogArea fLogArea = null;
    private Logging fLogging = null;

    private PropertiesDB fPropertiesDB = PropertiesDB.getInstance();
    private AddressDB fAddressDB = AddressDB.instance();
    private RecipientHintsDB fRecipientHintsDB = RecipientHintsDB.getInstance();
    private RecordedMsgHintsDB fRecordedMsgHintsDB = RecordedMsgHintsDB.getInstance();

    private MiscProtocol fMiscProtocol = MiscProtocol.getInstance();
    private MessageProtocol fMessageProtocol = MessageProtocol.getInstance();
    private MeetingProtocol fMeetingProtocol = MeetingProtocol.getInstance();

    private CursorControl fCursorControl = CursorControl.instance();
    private BGColorManager fBGColorManager = BGColorManager.getInstance();

    private UIFactory<JMenuItem> fUIFactory = new UIFactoryImpl();
    private DedicatedUIManager<JMenuItem> fDedicatedUIManager = null;
    private MeetingRoomUIManager<JMenuItem> fMeetingRoomUIManager = null;
    private Deliverer fDeliverer = null;
    private MeetingManager fMeetingManager = null;
    private MainFrameFeatures fMainFrameFeatures = null;

    public MainFrame() {

        kToolTitle = "MessagingTool (" + MessagingToolVersion.VERSION + ") for JDK " + Context.getJDKVersion() + " (Swing)";
        Container contentPane = getContentPane(); // Swing
        //
        // Register as WindowListener
        //
        addWindowListener(this);
        //
        // Register this frame as an Observer.
        //
        fRecipientHintsDB.addObserver(this);
        fRecordedMsgHintsDB.addObserver(this);
        fAddressDB.addObserver(this);
        //
        // Register self to LFManager
        // 
        LFManager.getInstance().add(this);
        //
        // Register this frame as Protocol2Processor
        //
        fMiscProtocol.addFTPListener(new FTPListenerImpl(this));

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        setBackground(Color.lightGray);
        contentPane.setLayout(gridbag);
        //
        // Menu bar
        //
        fMenuBar = new JMenuBar();
        setJMenuBar(fMenuBar);
        //
        // File menu
        //
        fFileMenu = new XJMenu(StringDefs.FILE);
        fFileMenu.setMnemonic('F');
        fMenuBar.add(fFileMenu);
        //
        // Stickies
        //
        JMenuItem newStickyNote = Util.createJMenuItem(fFileMenu, StringDefs.NEW_STICKY_NOTE_PPP, 'N', 0, null, this);
        newStickyNote.addActionListener(actionEvent -> {
            StickyNote note = new StickyNote();
            ComponentUtil.centerComponent(note, getLocation(), getSize());
            note.setVisible(true);
        });

        JMenuItem saveStickyNotes = Util.createJMenuItem(fFileMenu, StringDefs.SAVE_ALL_STICKY_NOTES, 'S', 0, null, this);
        saveStickyNotes.addActionListener(actionEvent -> Stickies.saveNotes());

        JMenuItem frontNotes = Util.createJMenuItem(fFileMenu, StringDefs.FRONT_ALL_NOTES, 'T', 0, null, this);
        frontNotes.addActionListener(actionEvent -> Stickies.toFrontAllNotes());

        JMenuItem backNotes = Util.createJMenuItem(fFileMenu, StringDefs.BACK_ALL_NOTES, 'B', 0, null, this);
        backNotes.addActionListener(actionEvent -> Stickies.toBackAllNotes());

        fFileMenu.addSeparator();
        //
        // Logs
        //
        fFileSavedMessages = new XJMenu(StringDefs.LOGGED_MESSAGES);
        fFileSavedMessages.setEnabled(false);
        fFileMenu.add(fFileSavedMessages);

        fFileSavedMeetingMessages = new XJMenu(StringDefs.LOGGED_MEETING_MESSAGES);
        fFileSavedMeetingMessages.setEnabled(false);
        fFileMenu.add(fFileSavedMeetingMessages);
        fFileMenu.addSeparator();

        fFileQuitItem = Util.createJMenuItem(fFileMenu, StringDefs.QUIT, 'Q', KeyEvent.VK_Q, null, this);
        //
        // Edit menu
        //
        fEditMenu = new XJMenu(StringDefs.EDIT);
        fEditMenu.setMnemonic('E');
        fMenuBar.add(fEditMenu);

        fEditAddressFileItem = Util.createJMenuItem(fEditMenu, StringDefs.ADDRESS_CACHE_PPP, 'A', 0, null, this);
        fEditRecipientFileItem = Util.createJMenuItem(fEditMenu, StringDefs.RECIPIENT_HINTS_PPP, 'R', 0, null, this);
        fEditRecordedMsgFileItem = Util.createJMenuItem(fEditMenu, StringDefs.RECORDED_MESSAGE_HINTS_PPP, 'M', 0, null, this);
        fEditMeetingRoomFileItem = Util.createJMenuItem(fEditMenu, StringDefs.MEETING_ROOM_PPP, 'R', 0, null, this);
        fEditMenu.addSeparator();
        fEditPropertiesItem = Util.createJMenuItem(fEditMenu, StringDefs.PROPERTIES_PPP, 'P', 0, null, this);
        //
        // Popup Menu
        //
        fRecipientHintsPopup = new JPopupMenu();
        fRecordedMsgHintsPopup = new JPopupMenu();
        update(fRecipientHintsDB, null);
        update(fRecordedMsgHintsDB, null);
        //
        // Window
        //
        fWindowMenu = new XJMenu(StringDefs.WINDOW);
        fWindowMenu.setMnemonic('W');
        fMenuBar.add(fWindowMenu);

        fWindowAnotherItem = Util.createJMenuItem(fWindowMenu, StringDefs.ANOTHER_PPP, 'A', 0, null, this);

        fWindowMenu.addSeparator();
        fWindowMessagingDialogs = new XJMenu(StringDefs.MESSAGING_DIALOG);
        fWindowMessagingDialogs.setEnabled(false);
        fWindowMenu.add(fWindowMessagingDialogs);
        fWindowMeetingRooms = new XJMenu(StringDefs.MEETING_ROOM);
        fWindowMeetingRooms.setEnabled(false);
        fWindowMenu.add(fWindowMeetingRooms);

        fWindowMenu.addSeparator();
        fWindowOnlineListItem = Util.createJMenuItem(fWindowMenu, StringDefs.ON_LINE_LIST_PPP, 'O', 0, null, this);
        fWindowMeetingRoomListItem = Util.createJMenuItem(fWindowMenu, StringDefs.MEETING_ROOM_LIST_PPP, 'M', 0, null, this);
        //
        // Tool
        //
        fToolMenu = new XJMenu(StringDefs.TOOL);
        fToolMenu.setMnemonic('T');
        fMenuBar.add(fToolMenu);

        fToolSendFileItem = Util.createJMenuItem(fToolMenu, StringDefs.SEND_A_FILE_PPP, 'S', 0, null, this);
        fToolMenu.addSeparator();
        fToolSearchUserItem = Util.createJMenuItem(fToolMenu, StringDefs.SEARCH_PPP, 'U', 0, null, this);
        fToolMenu.addSeparator();
        fToolNewMeetingRoomItem = Util.createJMenuItem(fToolMenu, StringDefs.JOIN_MEETING_ROOM_PPP, 'J', 0, null, this);
        fToolMenu.addSeparator();
        fToolDeleteMeetingRoomItem = Util.createJMenuItem(fToolMenu, StringDefs.DELETE_MEETING_ROOM_PPP, 'D', 0, null, this);

        //
        // Help menu
        //
        fHelpMenu = new XJMenu(StringDefs.HELP);
        fHelpMenu.setMnemonic('H');
        fMenuBar.add(fHelpMenu);

        fOverviewItem = new JMenuItem(StringDefs.OVERVIEW_PPP);
        fOverviewItem.setEnabled(false);
        fTopicsItem = new JMenuItem(StringDefs.TOPICS_PPP);
        fTopicsItem.setEnabled(false);
        fHelpMenu.add(fOverviewItem);
        fHelpMenu.add(fTopicsItem);

        fAboutMenu = new XJMenu(StringDefs.ABOUT);
        fAboutMenu.setMnemonic('A');
        fHelpMenu.add(fAboutMenu);

        fAboutMessagingToolItem = Util.createJMenuItem(fAboutMenu, StringDefs.MESSAGING_TOOL_PPP, 'M', 0, null, this);
        fAboutSystemPropertiesItem = Util.createJMenuItem(fAboutMenu, StringDefs.SYSTEM_PROPERTIES_PPP, 'S', 0, null, this);

        // fMenuBar.setHelpMenu(fHelpMenu);
        //
        // Buttons, etc
        //
        fDeliverButton = new JButton(StringDefs.DELIVER);
        // fDeliverButton       = new JButton(new CircleIcon(Color.red));
        // fDeliverButton.setToolTipText(StringDefs.kDeliver);
        fDeliverButton.setToolTipText(
                "<html><font color=red>Deliver</font> button is used to send a message in "
                + "<p>the <font color=blue>Message Subwindow</font> below to recipients "
                + "<p>specified in <font color=red>To:</font> field. </html>");
        fDeliverButton.addActionListener(this);
        fCursorControl.addCursorComponent(fDeliverButton);
        fCursorControl.addEnablableComponent(fDeliverButton);

        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.insets.top = 2;
        constraints.insets.left = 2;
        constraints.insets.bottom = 2;
        constraints.insets.right = 2; // (2,2,2,2) 
        gridbag.setConstraints(fDeliverButton, constraints);
        contentPane.add(fDeliverButton); // Swing

        fNotInOfficeCheckBox = new JCheckBox(StringDefs.NOT_IN_OFFICE);
        fNotInOfficeCheckBox.setToolTipText(StringDefs.NOT_IN_OFFICE);
        fNotInOfficeCheckBox.addItemListener(this);
        fCursorControl.addCursorComponent(fNotInOfficeCheckBox);
        fCursorControl.addEnablableComponent(fNotInOfficeCheckBox);

        constraints.insets.left = 0;
        constraints.insets.right = 0; // (2,0,2,0)
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(fNotInOfficeCheckBox, constraints);
        contentPane.add(fNotInOfficeCheckBox); // Swing

        //
        // To List
        //
        fToList = new XJTextField(1 /*Context.kWindowWidth */);
        fToList.setToolTipText("<html>"
                + "A recipient name in the address cache or an <p>"
                + "host name or an IP address can be specified.</html>");
        fBGColorManager.add(fToList);
        fToList.setBackground(ColorMap.getColorByName(fPropertiesDB.getTextBackground()));
        fToList.addKeyListener(fShiftKeyAdapter);
        fCursorControl.addCursorComponent(fToList);
        fCursorControl.addEnablableComponent(fToList);

        fSecondPanel = new JPanel();
        fSecondPanel.setLayout(new CardLayout());
        fCursorControl.addCursorComponent(fSecondPanel);

        JPanel tempPanel = new JPanel();
        fCursorControl.addCursorComponent(tempPanel);
        GridBagLayout tempGBL = new GridBagLayout();
        GridBagConstraints tempGBC = new GridBagConstraints();
        tempPanel.setLayout(tempGBL);

        fToLabel = new JLabel(kToList);
        fToLabel.setToolTipText("<html>Try to show a Popup hint here</html>");
        fCursorControl.addCursorComponent(fToLabel);
        fCursorControl.addEnablableComponent(fToLabel);
        tempGBC.gridwidth = 1;
        tempGBC.anchor = GridBagConstraints.WEST;
        tempGBC.insets.top = 0;
        tempGBC.insets.left = 2;
        tempGBC.insets.bottom = 2;
        tempGBC.insets.right = 0; // (0,2,2,0) 
        tempGBL.setConstraints(fToLabel, tempGBC);
        tempPanel.add(fToLabel);
        tempGBC.gridwidth = GridBagConstraints.REMAINDER;
        tempGBC.fill = GridBagConstraints.HORIZONTAL;
        tempGBC.weightx = 1.0;
        tempGBL.setConstraints(fToList, tempGBC);
        tempPanel.add(fToList);
        fSecondPanel.add(kToList, tempPanel);
        //
        //
        //
        tempPanel = new JPanel();
        fCursorControl.addCursorComponent(tempPanel);
        tempGBL = new GridBagLayout();
        tempGBC = new GridBagConstraints();
        tempPanel.setLayout(tempGBL);
        fRecordedMsgLabel = new JLabel(kRecordedMsg);
        tempGBC.gridwidth = 1;
        tempGBC.anchor = GridBagConstraints.WEST;
        tempGBL.setConstraints(fRecordedMsgLabel, tempGBC);
        tempPanel.add(fRecordedMsgLabel);

        fSecondPanel.add(kRecordedMsg, tempPanel);
        //
        // Add Popup hints to labels
        //
        fToLabel.addMouseListener(new JPopupMenuAdapter(fToLabel, fRecipientHintsPopup));
        fRecordedMsgLabel.addMouseListener(new JPopupMenuAdapter(fRecordedMsgLabel, fRecordedMsgHintsPopup));
        //
        // Create input area and log area
        //
        fInputArea = new StyledTextArea(true,
                ColorMap.getColorByName(fPropertiesDB.getTextBackground()));
        fBGColorManager.add(fInputArea);
        fInputArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                int keyCode = keyEvent.getKeyCode();
                if (KeyUtil.isDeliverKey(keyCode)) {
                    fDeliverer.deliver(fToList.getText(), fInputArea);
                }
            }
        });

        fCursorControl.addCursorComponent(fInputArea);
        fCursorControl.addEnablableComponent(fInputArea);

        fLogTextArea = new StyledTextArea(true,
                ColorMap.getColorByName(fPropertiesDB.getTextBackground()));
        fBGColorManager.add(fLogTextArea);
        fLogTextArea.setEditable(false);
        fCursorControl.addCursorComponent(fLogTextArea);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.insets.top = 0;
        constraints.insets.left = 0;
        constraints.insets.bottom = 0;
        constraints.insets.right = 0; // (0,0,0,0) 
        gridbag.setConstraints(fSecondPanel, constraints);
        contentPane.add(fSecondPanel);  // Swing

        fSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fInputArea, fLogTextArea);
        fSplitter.setContinuousLayout(true);
        fSplitter.setDividerSize(Context.SPLITTER_WIDTH);
        fSplitter.setOneTouchExpandable(true);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        gridbag.setConstraints(fSplitter, constraints);
        contentPane.add(fSplitter);

        setFonts();
        fPropertiesDB.addPropertyChangeListener(this); // this must be done after SetFonts

        Dimension size = fPropertiesDB.getLastSize();

        if (size == null) {
            setSize(new Dimension(360, 410));
        } else {
            setSize(size);
        }

        fCursorControl.addCursorComponent(this);

        setIcon(kNormalIcon);
        // 
        // Restore the last location.
        // 
        ComponentUtil.fitComponentIntoScreen(this, fPropertiesDB.getLastLocation());

        MainUI mainUI = new MainUIImpl();
        fDedicatedUIManager = new DedicatedUIManager<JMenuItem>(mainUI, this, fUIFactory);
        fMeetingRoomUIManager = new MeetingRoomUIManager<JMenuItem>(mainUI, this, fUIFactory, fFileSavedMeetingMessages);
        fOnlineListUI = fUIFactory.createOnlineListUI(StringDefs.ON_LINE, mainUI, fDedicatedUIManager);
        fLogging = new Logging("MessagingTool.log", new LoggingGUIImpl(this, fFileSavedMessages));
        fLogArea = new LogAreaImpl(fLogTextArea, fLogging);
        fDeliverer = new DelivererImpl<JMenuItem>(mainUI, fLogArea, fOnlineListUI, fDedicatedUIManager);
        MessageControl.getInstance().setMessageListener(new MessageControl.MessageListener() {
            public void allMessagesShown() {
                setIcon(kNormalIcon);
            }
        });
        fSearchUI = fUIFactory.createSearchUI(this);
        // Register Meeting Listener
        fMeetingManager = new MeetingManagerImpl<JMenuItem>(fMeetingRoomUIManager, mainUI);
        fMeetingListener = new MeetingListenerImpl(fMeetingManager);
        fMeetingProtocol.setMeetingListener(fMeetingListener);
        // creat MeetingRoomList
        fMeetingRoomListUI = new MeetingRoomListUI(fMeetingManager, StringDefs.MEETING_ROOMS);
        fUIFactory.setMeetingRoomListUI(fMeetingRoomListUI);
        // MessagProtocol Listener
        fAboutMessageReceived = (AboutUIImpl) fUIFactory.createMessageReceivedUI(this);
        MessageProtocolListener messageProtocolListener
                = new MessageProtocolListenerImpl<JMenuItem>(
                        mainUI, fOnlineListUI, fInputArea, fLogArea, fDedicatedUIManager, fAboutMessageReceived);
        fMessageProtocol.addMessageProtocolListener(messageProtocolListener);
        // MiscProtocolListener
        fMiscProtocol.addMiscProtocolListener(new MiscProtocolListenerImpl(
                mainUI, fOnlineListUI, fSearchUI, fMeetingManager, messageProtocolListener));

        // FTP Listener Factory
        FTP.setFTPListenerFactory(new FTPListenerFactoryImpl(this));
        // MainFrameFeatures
        fMainFrameFeatures = new MainFrameFeaturesImpl<JMenuItem>(fLogArea, fMeetingRoomUIManager);
        //
        // Load Stickies ...
        //
        Stickies.loadNotes();
        //
        // Enable Window Event to show the Quit confirm dialog correctly.
        //
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    private class MainUIImpl implements MainUI {

        public void addDedicatedUI(DedicatedUI dedicatedUI) {
            JMenuItem menuItem = new JMenuItem(dedicatedUI.getTitle());

            fDedicatedUIManager.put(menuItem, dedicatedUI);
            menuItem.addActionListener(event
                    -> fDedicatedUIManager.get((JMenuItem) event.getSource()).setVisible(true));

            menuItem.setFont(Context.getFont());
            fWindowMessagingDialogs.setEnabled(true);
            fWindowMessagingDialogs.add(menuItem);
            dedicatedUI.setMenuItem(menuItem);
        }

        public void addMeetingRoomUI(MeetingRoomUI meetingRoomUI) {
            JMenuItem menuItem = new JMenuItem(meetingRoomUI.getExternalRoomName());

            fMeetingRoomUIManager.put(menuItem, meetingRoomUI);
            menuItem.addActionListener(event
                    -> fMeetingRoomUIManager.get((JMenuItem) event.getSource()).setVisible(true));

            menuItem.setFont(Context.getFont());
            fWindowMeetingRooms.setEnabled(true);
            fWindowMeetingRooms.add(menuItem);
            meetingRoomUI.setMenuItem(menuItem);
        }

        public void removeMeetingRoomUI(MeetingRoomUI meetingRoomUI) {
            int count = fWindowMeetingRooms.getItemCount();
            String externalName = meetingRoomUI.getExternalRoomName();
            JMenuItem item = null;

            for (int i = 0; i < count; i++) {
                item = fWindowMeetingRooms.getItem(i);
                if (externalName.equals(item.getText())) {
                    fWindowMeetingRooms.remove(item);
                    if (count == 1) {
                        fWindowMeetingRooms.setEnabled(false);
                    }
                    return;
                }
            }
        }

        @Override
        public void beep() {
            MainFrame.this.getToolkit().beep();
        }

        @Override
        public boolean isIconified() {
            return fIconified;
        }

        @Override
        public boolean isInOffice() {
            return !fNotInOffice;
        }

        @Override
        public boolean isEditAddressFileVisible() {
            if (fEditAddressFile == null) {
                return false;
            }
            return fEditAddressFile.isVisible();
        }

        @Override
        public void setNotInOfficeEnabled(boolean enabled) {
            fNotInOfficeCheckBox.setEnabled(enabled);
        }

        @Override
        public void setToList(String toList) {
            fToList.setText(toList);
        }

        @Override
        public void setMessageWaiting(boolean waiting) {
            if (waiting) {
                setIcon(kWaitingIcon);
            } else {
                setIcon(kNormalIcon);
            }
        }

        @Override
        public void toFront() {
            MainFrame.this.toFront();
        }

    }

    private void restoreVisibilityOfLists() {
        if (fPropertiesDB.isOnlineDialogVisible()) {
            fOnlineListUI.setVisible(true);
        }
        if (fPropertiesDB.isRoomListDialogVisible()) {
            fMeetingRoomListUI.setVisible(true);
        }
    }

    private void setIcon(int iconType) {
        /*
         * With JDK 1.1 final, setting Icon doesn't work.
         *
         String iconFile = null;
        
         switch (iconType) {
         case kNormalIcon: 
         iconFile = "NormalIcon.gif";
         break;
         case kNotInOfficeIcon:
         iconFile = "NotInOfficeIcon.gif";
         break;
         case kWaitingIcon:
         iconFile = "WaitingIcon.gif";
         break;
         }
        
         Image image = getToolkit().getImage(iconFile);
         MediaTracker    tracker = new MediaTracker(this);
         try {
         tracker.addImage(image, 0);
         tracker.waitForID(0);
         }
         catch (InterruptedException e) {}
        
         setIconImage(image);
         System.out.println("SetIcon: " + iconFile);
         */
        //
        // as well as icon, change the title of this window appropriately.
        //
        switch (iconType) {
            case kNormalIcon:
                setTitle(kToolTitle);
                break;
            case kNotInOfficeIcon:
                setTitle(StringDefs.NOT_IN_OFFICE);
                break;
            case kWaitingIcon:
                setTitle(StringDefs.MESSAGE_WAITING);
        }
    }

    void setFonts() {
        Context.updateFont();

        Font font = Context.getFont();

        if (font == null) {
            return;
        }
        //
        // Menu bar & Menus
        //
        fMenuBar.setFont(font);
        Util.setFontsToMenu(fFileMenu, font);
        Util.setFontsToMenu(fEditMenu, font);
        Util.setFontsToMenu(fWindowMenu, font);
        Util.setFontsToMenu(fToolMenu, font);
        Util.setFontsToMenu(fHelpMenu, font);
        //
        // Other
        //       
        fDeliverButton.setFont(font);
        fNotInOfficeCheckBox.setFont(font);
        fToLabel.setFont(font);
        fToList.setFont(font);
        fRecordedMsgLabel.setFont(font);
        fInputArea.setTextFont(font);
        fLogTextArea.setTextFont(font);
        //
        // Popup Hints
        //
        Util.setFontsToMenu(fRecipientHintsPopup, font);
        Util.setFontsToMenu(fRecordedMsgHintsPopup, font);
    }

    // =================================
    // WindowListener
    // =================================
    @Override
    public void windowClosed(WindowEvent event) {
        setVisible(false);
        System.exit(0);
    }

    @Override
    public void windowDeiconified(WindowEvent event) {
        fAboutMessageReceived.setVisible(false);

        fIconified = false;
        setIcon(kNormalIcon);
        //
        // Make sure the window is inside the screen. (V1.51)
        //
        ComponentUtil.fitComponentIntoScreen(this, getLocation());
        //
        // Now the main window is opened. So clear all message waiting flags
        // in the Online List window. (V1.51)
        //
        fOnlineListUI.clearAllMessageWaitings();
        //
        // Scroll to the bottom.
        // 
        fLogTextArea.scrollToBottom();
    }

    @Override
    public void windowIconified(WindowEvent event) {
        //
        // The following code doesn't work with JDK 1.1.
        // I need to file a bug report.
        //
        // if (fAboutMessageReceived != null)
        //   fAboutMessageReceived.setVisible(false);
        fIconified = true;
        if (fNotInOfficeCheckBox.isSelected()) {
            setIcon(kNotInOfficeIcon);
        }
        fLogTextArea.scrollToBottom();
    }

    @Override
    public void windowActivated(WindowEvent event) {
    }

    @Override
    public void windowDeactivated(WindowEvent event) {
    }

    @Override
    public void windowOpened(WindowEvent event) {
        //
        // Now the main window is opened, so retore the visibility of
        // the online list. Opening the online list before opening the main
        // window doesn't work with jdb.
        //
        restoreVisibilityOfLists();
        //
        // Alwyas locate the divider at the ratio of 3:7
        //
        fSplitter.setDividerLocation(0.3);
        //
        // Start Online Watcher ...
        // 
        Thread onlineWatcher = new OnlineWatcher(fOnlineListUI);
        onlineWatcher.setDaemon(true);
        onlineWatcher.start();
    }

    public void windowClosing(WindowEvent event) {
        //
        // Showing a confirm dialog is done by processWindowEvent() below.
        // If a user spcify the cancelation of closing window, windowClosing()
        // will not be called. In other words, when this windowClosing() is
        // called, the user spcifies quiting.
        // 
        terminateTool();
    }

    @Override
    public void processWindowEvent(WindowEvent event) {
        //
        // Dialog must be shown in this processWindowEvent(), because calling
        // super.processWindowEvent() always closes the main frame.
        // 
        if (event.getID() == WindowEvent.WINDOW_CLOSING) {
            Object[] options = {StringDefs.QUIT, StringDefs.CANCEL,};

            getToolkit().beep();
            int choice = JOptionPane.showOptionDialog(this,
                    StringDefs.ARE_YOU_SURE_QUITING,
                    "",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            //
            // If a user cancels quiting, then consume the event by
            // just returning.
            // 
            if (choice == JOptionPane.NO_OPTION
                    || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }

        }
        super.processWindowEvent(event);
    }

    private void terminateTool() {
        //
        // Leave all meeting rooms
        //
        fMeetingRoomUIManager.leaveAll();
        //
        // Save the current location only if the window is open.
        // Because that on my PC at office, getLocation() seems return
        // a coordination which is out of screen. [V1.64]
        // 
        if (!fIconified) {
            fPropertiesDB.setLastLocation(getLocation());
        }
        fPropertiesDB.setLastSize(getSize());
        fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        //
        // Stop the servers of protocol and MiscProtocol. Because
        // there will be 1 second interval before actually 
        // terminated.
        fMessageProtocol.stopServer();
        fMiscProtocol.stopServer();
        //
        // Close all dialogs and frames so that the user
        // might think that this tool exits.
        //   
        setVisible(false);
        fOnlineListUI.saveState();
        fOnlineListUI.setVisible(false);
        fMeetingRoomListUI.saveStates();
        fMeetingRoomListUI.setVisible(false);

        if (fPropertiesUI != null) {
            fPropertiesUI.setVisible(false);
        }
        if (fAboutMessagingTool != null) {
            fAboutMessagingTool.setVisible(false);
        }
        if (fAboutSystemProperties != null) {
            fAboutSystemProperties.setVisible(false);
        }
        fAboutMessageReceived.setVisible(false);
        if (fEditAddressFile != null) {
            fEditAddressFile.setVisible(false);
        }
        if (fEditRecipientFile != null) {
            fEditRecipientFile.setVisible(false);
        }
        if (fEditRecordedMsgFile != null) {
            fEditRecordedMsgFile.setVisible(false);
        }

        AnotherUI another = fAnotherUI;
        while (another != null) {
            another.setVisible(false);
            another = another.getNext();
        }

        fDedicatedUIManager.close();
        fLogging.stop();

        //
        // Notify OffLine
        //
        String[] onlines = fOnlineListUI.getOnlines();

        if (onlines != null && onlines.length > 0) {
            Thread multipleNotifier = new MultipleNotifier(
                    fPropertiesDB.getUserName(),
                    onlines,
                    0,
                    MultipleNotifier.kOffLine);
            multipleNotifier.start();
            TimerUtil.sleep(1000); // sleep only 1 second.
        }
        //
        // Save Stickies
        //
        Stickies.saveNotes();

        System.exit(0);
    }

    // ================================
    // ItemListener
    // ================================
    @Override
    public void itemStateChanged(ItemEvent event) {
        boolean isNotInOffice = fNotInOfficeCheckBox.isSelected();

        //
        // Note that with JDK 1.1, somehow itemStateChanged() is called twice with 
        // the same event. Therefore, ignore the second call of this function by
        // remembering the current stat of fNotInOfficeCheckBox.
        //
        if (fNotInOffice == isNotInOffice) {
            return;
        } else {
            fNotInOffice = isNotInOffice;
        }

        String[] onlines = fOnlineListUI.getOnlines();

        if (isNotInOffice) {
            ((CardLayout) fSecondPanel.getLayout()).show(
                    fSecondPanel, kRecordedMsg);
            fDeliverButton.setEnabled(false);
            fSavedMsg = fInputArea.getText();
            fInputArea.setText(fRecordedMsg);
            validate();
            //
            // Notify "Not In Office" to all onlines [V1.52]
            //
            if (onlines != null && onlines.length > 0) {
                Thread multipleNotifier = new MultipleNotifier(
                        fPropertiesDB.getUserName(),
                        onlines,
                        0,
                        MultipleNotifier.kNotInOffice);
                multipleNotifier.start();
            }
        } else {
            ((CardLayout) fSecondPanel.getLayout()).show(
                    fSecondPanel, kToList);
            fDeliverButton.setEnabled(true);
            fRecordedMsg = fInputArea.getText();
            fInputArea.setText(fSavedMsg);
            validate();
            //
            // Notify "In Office" to all onlines [V1.52]
            //
            if (onlines != null && onlines.length > 0) {
                Thread multipleNotifier = new MultipleNotifier(
                        fPropertiesDB.getUserName(),
                        onlines,
                        0,
                        MultipleNotifier.kInOffice);
                multipleNotifier.start();
            }
        }
        //
        // Set the isNotInOffice to all AnotherUI & DedicatedUI windows
        //
        AnotherUI another = fAnotherUI;
        while (another != null) {
            another.setDeliverEnabled(!isNotInOffice);
            another = another.getNext();
        }
        //
        // For dedicated dialogs, it must be synchronized because a new dedicated dialog
        // might be created due to a message reception during this operation.
        //
        fDedicatedUIManager.setDeliverEnabled(!isNotInOffice);
        //
        // If a room is joined, it will be left when the NotInOffice is checked. But
        // in this case, when the NotInOffice is unchecked, then the room will be joined
        // automatically.
        //
        fMeetingRoomUIManager.setNotInOfficeToAll(isNotInOffice);
    }

    // ================================
    // ActionListener
    // ================================
    public void actionPerformed(ActionEvent event) {
        Object target = event.getSource();

        if (target == fDeliverButton) {
            fDeliverer.deliver(fToList.getText(), fInputArea);
            fInputArea.requestFocus(); // V1.61
        } else if (target == fFileQuitItem) {
            terminateTool();
        } else if (target == fAboutMessagingToolItem) {
            if (fAboutMessagingTool == null) {
                fAboutMessagingTool = new AboutUIImpl(this,
                        AboutUI.kMessagingTool,
                        StringDefs.ABOUT_MESSAGING_TOOL);
            }
            fAboutMessagingTool.setVisible(true);
        } else if (target == fAboutSystemPropertiesItem) {
            if (fAboutSystemProperties == null) {
                fAboutSystemProperties = new AboutUIImpl(this,
                        AboutUI.kSystemProperties,
                        StringDefs.ABOUT_SYSTEM_PROPERTIES);
            }
            fAboutSystemProperties.setVisible(true);
        } else if (target == fEditAddressFileItem) {
            if (fEditAddressFile == null) {
                fEditAddressFile = new EditUI(this, new AddressEditImpl());
            }
            fEditAddressFile.setVisible(true);
        } else if (target == fEditRecipientFileItem) {
            if (fEditRecipientFile == null) {
                fEditRecipientFile = new EditUI(this, new RecipientEditImpl());
            }
            fEditRecipientFile.setVisible(true);
        } else if (target == fEditRecordedMsgFileItem) {
            if (fEditRecordedMsgFile == null) {
                fEditRecordedMsgFile = new EditUI(this, new RecordedMsgEditImpl());
            }
            fEditRecordedMsgFile.setVisible(true);
        } else if (target == fEditMeetingRoomFileItem) {
            if (fEditMeetingRoomFile == null) {
                fEditMeetingRoomFile = new EditUI(this, new MeetingRoomEditImpl());
            }
            fEditMeetingRoomFile.setVisible(true);
        } else if (target == fEditPropertiesItem) {
            if (fPropertiesUI == null) {
                //
                // At initial, there is no entry for Font in PropertiesDB.
                // So, before opening the edit window, save the current font.
                //
                Font font = fDeliverButton.getFont();
                fPropertiesDB.setFontName(font.getName());
                fPropertiesDB.setFontSize(font.getSize());
                fPropertiesDB.setFontStyle(font.getStyle());
                fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));

                fPropertiesUI = new PropertiesUI(this,
                        StringDefs.MESSAGING_TOOL_PROPERTIES);
            }
            fPropertiesUI.setVisible(true);
        } else if (target == fWindowAnotherItem) {
            if (fAnotherUI == null) {
                fAnotherUI = new AnotherUI(this, fDeliverer,
                        StringDefs.SENDING_WINDOW,
                        !fNotInOffice);
                fAnotherUI.setVisible(true);
            } else {
                //
                // Search deactivated dialog.
                //
                AnotherUI current = fAnotherUI;
                boolean activated = current.IsActivated();
                AnotherUI next = current.getNext();

                while (activated == true) {
                    if (next == null) {
                        AnotherUI another = new AnotherUI(this, fDeliverer,
                                StringDefs.SENDING_WINDOW,
                                !fNotInOffice);

                        current.setNext(another);
                        another.setVisible(true);
                        return;
                    } else {
                        current = next;
                        activated = current.IsActivated();
                        next = current.getNext();
                    }
                }
                current.setVisible(true);
            }
        } else if (target == fWindowOnlineListItem) {
            fOnlineListUI.setVisible(true);
        } else if (target == fWindowMeetingRoomListItem) {
            fMeetingRoomListUI.setVisible(true);
        } else if (target == fToolSendFileItem) {
            if (fFileSendUI == null) {
                fFileSendUI = new FileSendUI(this, fToolSendFileItem);
            }

            fFileSendUI.setVisible(true);
        } else if (target == fToolSearchUserItem) {
            fSearchUI.setVisible(true);
        } else if (target == fToolNewMeetingRoomItem) {
            if (fNewMeetingRoomUI == null) {
                fNewMeetingRoomUI = new NewMeetingRoomUI(this, fMeetingListener, fMeetingManager, true);
                fMeetingProtocol.roomOpened("");
            }
            fNewMeetingRoomUI.setVisible(true);
        } else if (target == fToolDeleteMeetingRoomItem) {
            if (fDeleteMeetingRoomUI == null) {
                fDeleteMeetingRoomUI = new NewMeetingRoomUI(this, fMeetingListener, fMeetingManager, false);
                fMeetingProtocol.roomOpened("");
            }
            fDeleteMeetingRoomUI.setVisible(true);
        } else if (target instanceof JMenuItem) {
            JMenuItem item = (JMenuItem) target;
            String menuLabel = item.getText();
            Container parentMenu = item.getParent();

            if (parentMenu == fRecipientHintsPopup) {
                String expandedHint = fRecipientHintsDB.getExpandedRecipients(menuLabel);

                Util.recipientHintSelected(expandedHint, fToList, fShiftKeyAdapter.isShiftKeyPressed());
            } else if (parentMenu == fRecordedMsgHintsPopup) {
                fInputArea.setText(menuLabel);
            }
        }
    }

    // ============================================
    // Implementation of Observer Interface
    // ============================================
    @Override
    public void update(
            Observable observable,
            Object obj) {

        if (observable == fRecipientHintsDB
                || observable == fAddressDB) {
            //
            // When either RecipientHintsDB or AddressDB is updated, then
            // update the hint for recipients. [V1.34]
            //
            Util.updateHintsMenu(fRecipientHintsPopup,
                    fRecipientHintsDB.getDB(),
                    fAddressDB.getHintedAddressDB(), this);
            Util.setFontsToMenu(fRecipientHintsPopup, Context.getFont());
            //
            // Update hints of all AnotherUI & DedicatedUI windows.
            //
            AnotherUI next = fAnotherUI;
            while (next != null) {
                next.updateRecipientHintsPopup();
                next = next.getNext();
            }

            if (fFileSendUI != null) {
                fFileSendUI.updateRecipientHintsPopup();
            }

            if (fDedicatedUIManager != null) {
                fDedicatedUIManager.updateRecipientHintsPopup();
            }
        } else if (observable == fRecordedMsgHintsDB) {
            Util.updateHintsMenu(fRecordedMsgHintsPopup, fRecordedMsgHintsDB.getDB(), this);
            Util.setFontsToMenu(fRecordedMsgHintsPopup, Context.getFont());
        }
    }

    // =======================
    // PropertyChangeListener
    // =======================
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getPropertyName();

        if (propertyName.equals(PropertiesDB.kName)) {
            setFonts();
            validate();
            fLogging.update();
        }
    }

    // ========================================
    // MainFrameFeatures implementation
    // ========================================
    @Override
    public void showMyAddress() {
        fMainFrameFeatures.showMyAddress();
    }

    @Override
    public void checkIfIPAddressChanged() {
        fMainFrameFeatures.checkIfIPAddressChanged();
    }

    @Override
    public void joinMeetingRooms() {
        fMainFrameFeatures.joinMeetingRooms();
    }
}
// LOG
//         8-Feb-97 Yoshiki     modified for JDK 1.1  
//        20-Feb-97 Y.Shibata   modified for the final version of JDK 1.1 
// 1.02 : 21-Feb-97 Y.Shibata   When iconified, close fAboutMessageReceived Window.
//        25-Feb-97 Y.Shibata   workaround for a garbage character with JDK 1.1 
// 1.04 : 14-Mar-97 Y.Shibata   changed the behavior of probe. 
//                              adjusted the locations of Popup Menus
//                              updated the font of popup hints when edited.
// 1.10 : 21-Mar-97 Y.Shibata   Now multiple lines are possible for recorded message.
//                              Address Cache is automatically updated. 
// 1.11 : 26-Mar-97 Y.Shibata   The behavior of Deliver is changed.
//        27-Mar-97 Y.Shibata   Initialized fRecordedMsg and fSavedMsg for Solaris.
//                              Don't dismiss dedicated windows when deiconified.
// 1.20 : 29-Mar-97 Y.Shibata   use setVisible() instead of show().
//                              use Toolkit.beep() for BeepOnReception.
// 1.30 : 17-Apr-97 Y.Shibata   recoded Log_ShowDate()
// 1.31 : 30-Apr-97 Y.Shibata   make sure to scroll up the Log Area when a log message is appended
// 1.32 : 20-Jun-97 Y.Shibata   commented out the code to scroll up in V1.31
// 1.33 : 30-Jun-97 Y.Shibata   Changing Title when a message is recived is added.
// 1.34 :  6-Jul-97 Y.Shibata   (1) added code to show up a Quit Confirm window.
//                              (2) Deliver from MainWidnow.Deliver() correctly records the delivered
//                                  log into individual dedicated window.
//                              (3) addresses in the AddressDB will be automatically added to
//                                  the hint for recipients.
//                              (4) The menu list of Dedicated Windows is correctly upated.
// 1.36 : 14-Jul-97 Y.Shibata   save / restore the location of the main window.
// 1.37 : 17-Jul-97 Y.Shibata   setting font correctly to the menu list of all dedicated dialogs.
//                              fixed a bug(crash with no recipient specified) introduced with V1.34.
// 1.41 :  4-Aug-97 Y.Shibata   added OnLine feature
// 1.42 :  5-Aug-97 Y.Shibata   Show "Received Message" dialog only when the main window is iconified.
//                              Update OnLine List when sending a message fails.
// 1.43 :  5-Aug-97 Y.Shibata   adjust the location of this window when it is shown.
// 1.44 :  5-Aug-97 Y.Shibata   Update OnLine list when sending a message is successfully done.
//                              Added Protocol2Processor code.
// 1.46 :  9-Aug-97 Y.Shibata   added SetToList().
//        17-Aug-97 Y.Shibata   added logging feature.
// 1.48 : 21-Aug-97 Y.Shibata   added RestoreVisibilityOfOnlineList().
// 1.49 : 22-Aug-97 Y.Shibata   re-code the restoring the lcoation with Util.FitComponentIntoScreen()
// 1.51 : 23-Aug-97 Y.Shibata   When the main window is open, make sure that it is inside the screen.
//                              Set/Clear MessageWaiting flag in the OnLine window.
//                              Disable "Not In Office" checkbox during sending a message.
// 1.52 :  2-Sep-97 Y.Shibata   added code to notify "Not In Office"/"In Office" immediately.
// 1.53 :  3-Sep-97 Y.Shibata   Fixed an incorrect setting of MessageWait status.
//                              A log of a failed message from a dedicated dialog will be appended to the dialog window
// 1.54 :  6-Sep-97 Y.Shibata   added code for MBP(Message Broadcast Protocol).
// 1.55 :  8-Sep-97 Y.Shibata   For OnLine, make sure that a recipient is registered as key in the Address DB
// 1.56 :  9-Sep-97 Y.Shibata   Don't log the message into dedicated dialogs if the message is broadcasted.
// 1.60 : 13-Sep-97 Y.Shibata   Fixed the busy cursor problem.
// 1.61 : 16-Sep-97 Y.Shibata   added fInputArea.requestFocus() to get focus back.
// 1.63 : 20-Sep-97 Y.Shibata   modified OpenDedicatedUIs()
//        21-Sep-97 Y.Shibata   modified code for requestFocus().
// 1.64 : 25-Sep-97 Y.Shibata   save the location only if deiconified.
// 1.69 : 11-Oct-97 Y.Shibata   added "Clear Log" button
//                              added Meeting Room List Dialog
// 1.75 :  9-Nov-97 Y.Shibata   save/restore the size of the base main window.
// 1.84 : 10-Jan-98 Y.Shibata   added toFront() to make sure that window or dialog will be shown.
// 1.85 : 13-Jan-98 Y.Shibata   added code to call Logging_Stop () when terminating
// 1.93b6 : 22-June-98 Y.Shibata    add code to call JSplitPane.setOneTouchExpandable().
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 1.95 : 11-Jul-98 Y.Shibata   modified to use msgtool.util.TimerUtil
// 2.00 :  5-Aug-98 Y.Shibata   add onCommandLineMessage
// 2.14 : 13-Feb-99	Y.Shibata	"*" is used for user matching
// 2.15 : 27-Feb-99	Y.Shibata	used JPopupMenuAdapter class
// 2.17 : 22-Mar-99	Y.Shibata	used XJMenu instead of JMenu directly.
// 2.19 : 27-Mar-99	Y.Shibata	added "Front All Sticky Notes" menu.
// 2.21 : 10-Apr-99	Y.Shibata	added code to check if a file exists for receiving the file.
// 2/22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.24 :  9-May-99	Y.Shibata	used XJTextField for Drag and Drop
// 2.33 : 21-Aug-99	Y.Shibata	used LogArea class [Refactoring]
//                              used Logging class [Refactoring]
// 2.38 : 29-Nov-99	Y.Shibata	The implementation of "Message Waiting Status" has been changed.
// 2.43 :  5-Mar-00	Y.Shibata	replaced SetFont() with Context.updateFont()
// 2.44 : 12-Mar-00	Y.Shibata	deleted Print menu because printing feature is now implemented by StyledTextArea
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics
// 2.51 :  1-Feb-04 Y.Shibata   added "To Back All Notes"
// 2.60 : 22-Mar-14 Y.Shibata   refactored with Java 8

