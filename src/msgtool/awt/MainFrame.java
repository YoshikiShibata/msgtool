// File: MainFrame.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1996 - 2000, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.awt;

import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuContainer;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.PrintJob;
import java.awt.TextArea;
import java.awt.TextField;
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
import msgtool.ui.AboutUI;
import msgtool.ui.DedicatedUI;
import msgtool.ui.DedicatedUIManager;
import msgtool.ui.MainUI;
import msgtool.ui.MeetingRoomUI;
import msgtool.ui.MeetingRoomUIManager;
import msgtool.ui.OnlineListUI;
import msgtool.ui.SearchUI;
import msgtool.ui.UIFactory;
import msgtool.util.ComponentUtil;
import msgtool.util.CursorControl;
import msgtool.util.MessageControl;
import msgtool.util.ShiftKeyAdapter;
import msgtool.util.StringDefs;
import msgtool.util.TimerUtil;

@SuppressWarnings("serial")
public class MainFrame 
    extends Frame 
    implements  Observer, ActionListener, ItemListener, WindowListener, PropertyChangeListener,
                MainFrameFeatures {
	private String kToolTitle	= null;
    
    private MenuBar     fMenuBar             = null;
    private Button      fDeliverButton       = null;
    private Checkbox    fNotInOfficeCheckBox = null;
    private Button      fClearLogButton      = null;
    private boolean     fNotInOffice       	 = false;
    private Panel       fSecondPanel         = null;
    private TextField   fToList              = null;
    private String      fRecordedMsg         = "";
    private String      fSavedMsg            = "";
    private Label       fToLabel             = null;
    private Label       fRecordedMsgLabel    = null;

    private InputTextArea   fInputArea          = null;
    private TextArea    	fLogTextArea            = null;
    
    private Menu        fFileMenu                   = null;
    private MenuItem    fFilePrintItem              = null;
    private Menu        fFileSavedMessages          = null;
    private Menu        fFileSavedMeetingMessages   = null;
    private MenuItem    fFileQuitItem               = null;
    
    private Menu        fHelpMenu                   = null;
    private Menu        fAboutMenu                  = null;
    private MenuItem    fAboutMessagingToolItem     = null;
    private MenuItem    fAboutSystemPropertiesItem  = null;
    private MenuItem    fOverviewItem               = null;
    private MenuItem    fTopicsItem                 = null;

    private Menu        fEditMenu                   = null;
    private MenuItem    fEditAddressFileItem        = null;
    private MenuItem    fEditRecipientFileItem      = null;
    private MenuItem    fEditRecordedMsgFileItem    = null;
	private MenuItem	fEditMeetingRoomFileItem	= null;
    private MenuItem    fEditPropertiesItem         = null;
    
    private Menu        fWindowMenu                 = null;
    private MenuItem    fWindowAnotherItem          = null;
    private Menu        fWindowMessagingDialogs     = null;
    private Menu        fWindowMeetingRooms         = null;
    private MenuItem    fWindowOnlineListItem       = null;
    private MenuItem    fWindowMeetingRoomListItem  = null;
    
    private Menu        fToolMenu                   = null;
    private MenuItem    fToolSendFileItem           = null;
    private MenuItem    fToolSearchUserItem         = null;
    private MenuItem    fToolNewMeetingRoomItem     = null;
    private MenuItem    fToolDeleteMeetingRoomItem  = null;
    
    private PopupMenu fRecipientHintsPopup      = null; 
    private PopupMenu fRecordedMsgHintsPopup    = null;
    
    final static String kToList      = StringDefs.TO_C;
    final static String kRecordedMsg = StringDefs.RECORDED_MESSAGE_C;
    
    private AboutUIImpl fAboutMessagingTool     = null;
    private AboutUIImpl fAboutSystemProperties  = null;
	private AboutUIImpl fAboutMessageReceived	= null;
    
    private EditUI 	fEditAddressFile         = null;
    private EditUI 	fEditRecipientFile       = null;
    private EditUI 	fEditRecordedMsgFile     = null;
	private EditUI	fEditMeetingRoomFile	= null;

    private PropertiesUI        fPropertiesUI		= null;
    private AnotherUI           fAnotherUI      	= null;
    private QuitUI              fQuitUI         	= null;
    private OnlineListUI        fOnlineListUI   	= null;
    private MeetingRoomListUI   fMeetingRoomListUI  = null;
    private SearchUI            fSearchUI       	= null;
    private NewMeetingRoomUI    fNewMeetingRoomUI   = null;
    private NewMeetingRoomUI    fDeleteMeetingRoomUI= null;
    private MeetingListener     fMeetingListener    = null;
    
    private final static int    kNormalIcon         = 0;
    private final static int    kNotInOfficeIcon    = 1;
    private final static int    kWaitingIcon        = 2;
    private boolean             fIconified          = false;
    
    private ShiftKeyAdapter		fShiftKeyAdapter	= new ShiftKeyAdapter();
    
    private FileSendUI      fFileSendUI     = null;
    
	private LogArea		fLogArea		= null;
	private Logging				fLogging			= null;
    
    private Dimension   fInitialSize = null;
    
    private PropertiesDB        fPropertiesDB       = PropertiesDB.getInstance();
    private AddressDB           fAddressDB          = AddressDB.instance();
    private RecipientHintsDB    fRecipientHintsDB   = RecipientHintsDB.getInstance();
    private RecordedMsgHintsDB  fRecordedMsgHintsDB = RecordedMsgHintsDB.getInstance();
    
    private MiscProtocol    fMiscProtocol       = MiscProtocol.getInstance();
    private MessageProtocol fMessageProtocol    = MessageProtocol.getInstance();
    private MeetingProtocol fMeetingProtocol    = MeetingProtocol.getInstance();
    
    private CursorControl   fCursorControl  = CursorControl.instance();
	private BGColorManager	fBGColorManager	= BGColorManager.getInstance();

	private UIFactory<MenuItem>			 fUIFactory 			= new UIFactoryImpl();
	private DedicatedUIManager<MenuItem> fDedicatedUIManager 	= null;
	private MeetingRoomUIManager<MenuItem>	fMeetingRoomUIManager	= null;
	private Deliverer				fDeliverer				= null;
	private MeetingManager			fMeetingManager			= null;
	private MainFrameFeatures		fMainFrameFeatures		= null;
    
    public MainFrame() 
        {

		kToolTitle     = "MessagingTool("+MessagingToolVersion.VERSION+") for JDK " +  Context.getJDKVersion() + " (AWT)";
		setTitle(kToolTitle);
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
        // Register this frame as Protocol2Processor
        //
		fMiscProtocol.addFTPListener(new FTPListenerImpl(this)); 
        
        GridBagLayout      gridbag     = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        setBackground(Color.lightGray);
        setLayout(gridbag);
        //
        // Menu bar
        //
        fMenuBar = new MenuBar();
        setMenuBar(fMenuBar);
        //
        // File menu
        //
        fFileMenu = new Menu(StringDefs.FILE);
        fMenuBar.add(fFileMenu);
        
        fFilePrintItem = new MenuItem(StringDefs.PRINT_PPP, new MenuShortcut('p'));
        fFilePrintItem.addActionListener(this);
        fFileMenu.add(fFilePrintItem);
        fFileMenu.addSeparator();
		fFilePrintItem.setEnabled(false);
        
        fFileSavedMessages = new Menu(StringDefs.LOGGED_MESSAGES);
        fFileSavedMessages.setEnabled(false);
        fFileMenu.add(fFileSavedMessages);
        
        fFileSavedMeetingMessages = new Menu(StringDefs.LOGGED_MEETING_MESSAGES);
        fFileSavedMeetingMessages.setEnabled(false);
        fFileMenu.add(fFileSavedMeetingMessages);
        fFileMenu.addSeparator();
        
        fFileQuitItem = new MenuItem(StringDefs.QUIT, new MenuShortcut('q')); 
        fFileQuitItem.addActionListener(this);
        fFileMenu.add(fFileQuitItem);
        //
        // Edit menu
        //
        fEditMenu = new Menu(StringDefs.EDIT);
        fMenuBar.add(fEditMenu);

        fEditAddressFileItem     = new MenuItem(StringDefs.ADDRESS_CACHE_PPP);
        fEditAddressFileItem.addActionListener(this);
        fEditRecipientFileItem   = new MenuItem(StringDefs.RECIPIENT_HINTS_PPP);
        fEditRecipientFileItem.addActionListener(this);
        fEditRecordedMsgFileItem = new MenuItem(StringDefs.RECORDED_MESSAGE_HINTS_PPP);
        fEditRecordedMsgFileItem.addActionListener(this);
		fEditMeetingRoomFileItem = new MenuItem(StringDefs.MEETING_ROOM_PPP);
		fEditMeetingRoomFileItem.addActionListener(this);
        fEditPropertiesItem        = new MenuItem(StringDefs.PROPERTIES_PPP);
        fEditPropertiesItem.addActionListener(this);
        fEditMenu.add(fEditAddressFileItem);
        fEditMenu.add(fEditRecipientFileItem);
        fEditMenu.add(fEditRecordedMsgFileItem);
		fEditMenu.add(fEditMeetingRoomFileItem);
        fEditMenu.addSeparator();
        fEditMenu.add(fEditPropertiesItem);
        //
        // Popup Menu
        //
        fRecipientHintsPopup    = new PopupMenu(StringDefs.RECIPIENT);
        fRecordedMsgHintsPopup  = new PopupMenu(StringDefs.RECORDED_MESSAGE);
        update(fRecipientHintsDB, null);
        update(fRecordedMsgHintsDB, null);
        //
        // Window
        //
        fWindowMenu = new Menu(StringDefs.WINDOW);
        fWindowAnotherItem = new MenuItem(StringDefs.ANOTHER_PPP);
        fWindowAnotherItem.addActionListener(this);
        fMenuBar.add(fWindowMenu);
        fWindowMenu.add(fWindowAnotherItem);
        
        fWindowMenu.addSeparator();
        fWindowMessagingDialogs = new Menu(StringDefs.MESSAGING_DIALOG);
        fWindowMessagingDialogs.setEnabled(false);
        fWindowMenu.add(fWindowMessagingDialogs);
        fWindowMeetingRooms = new Menu(StringDefs.MEETING_ROOM);
        fWindowMeetingRooms.setEnabled(false);
        fWindowMenu.add(fWindowMeetingRooms);
    
        fWindowMenu.addSeparator();
        fWindowOnlineListItem = new MenuItem(StringDefs.ON_LINE_LIST_PPP);
        fWindowOnlineListItem.addActionListener(this);
        fWindowMenu.add(fWindowOnlineListItem);
        fWindowMeetingRoomListItem = new MenuItem(StringDefs.MEETING_ROOM_LIST_PPP);
        fWindowMeetingRoomListItem.addActionListener(this);
        fWindowMenu.add(fWindowMeetingRoomListItem);
        //
        // Tool
        //
        fToolMenu = new Menu(StringDefs.TOOL);
        fMenuBar.add(fToolMenu);
        
        fToolSendFileItem = new MenuItem(StringDefs.SEND_A_FILE_PPP, new MenuShortcut('s'));
        fToolSendFileItem.addActionListener(this);
        fToolMenu.add(fToolSendFileItem);
        
        fToolMenu.addSeparator();
        fToolSearchUserItem = new MenuItem(StringDefs.SEARCH_PPP);
        fToolSearchUserItem.addActionListener(this); 
        fToolMenu.add(fToolSearchUserItem);
        
        fToolMenu.addSeparator();
        fToolNewMeetingRoomItem = new MenuItem(StringDefs.JOIN_MEETING_ROOM_PPP);
        fToolNewMeetingRoomItem.addActionListener(this);
        fToolMenu.add(fToolNewMeetingRoomItem);
        
        fToolMenu.addSeparator();
        fToolDeleteMeetingRoomItem = new MenuItem(StringDefs.DELETE_MEETING_ROOM_PPP);
        fToolDeleteMeetingRoomItem.addActionListener(this);
        fToolMenu.add(fToolDeleteMeetingRoomItem);
        //
        // Help menu
        //
        fAboutMenu = new Menu(StringDefs.ABOUT);
        fAboutMessagingToolItem = new MenuItem(StringDefs.MESSAGING_TOOL_PPP);
        fAboutMessagingToolItem.addActionListener(this);
        fAboutSystemPropertiesItem = new MenuItem(StringDefs.SYSTEM_PROPERTIES_PPP);
        fAboutSystemPropertiesItem.addActionListener(this);
        fAboutMenu.add(fAboutMessagingToolItem);
        fAboutMenu.add(fAboutSystemPropertiesItem);
        
        fHelpMenu = new Menu(StringDefs.HELP);
        
        fOverviewItem = new MenuItem(StringDefs.OVERVIEW_PPP);
        fTopicsItem = new MenuItem(StringDefs.TOPICS_PPP);
        fHelpMenu.add(fOverviewItem);
        fHelpMenu.add(fTopicsItem);
        fHelpMenu.add(fAboutMenu);
        fOverviewItem.setEnabled(false);
        fTopicsItem.setEnabled(false);
        
        fMenuBar.add(fHelpMenu);
        fMenuBar.setHelpMenu(fHelpMenu);
        
        //
        // Buttons, etc
        //
        fDeliverButton       = new Button(StringDefs.DELIVER);
        fDeliverButton.addActionListener(this);
        fCursorControl.addCursorComponent(fDeliverButton);
        fCursorControl.addEnablableComponent(fDeliverButton);
        constraints.fill        	= GridBagConstraints.NONE;
        constraints.anchor      	= GridBagConstraints.WEST;
        constraints.gridwidth   	= 1;
        constraints.weightx     	= 0.0;
        constraints.weighty     	= 0.0;
		constraints.insets.top		= 2;
		constraints.insets.left		= 2;
		constraints.insets.bottom	= 2;
		constraints.insets.right	= 2; // (2,2,2,2)
        gridbag.setConstraints(fDeliverButton, constraints);
        add(fDeliverButton);
        
        fNotInOfficeCheckBox = new Checkbox(StringDefs.NOT_IN_OFFICE);
        fNotInOfficeCheckBox.addItemListener(this);
        fCursorControl.addCursorComponent(fNotInOfficeCheckBox);
        fCursorControl.addEnablableComponent(fNotInOfficeCheckBox);

		constraints.insets.left		= 0;
		constraints.insets.right	= 0; // (2,0,2,0)
        gridbag.setConstraints(fNotInOfficeCheckBox, constraints);
        add(fNotInOfficeCheckBox);
        //
        // Clear Log Button
        //
        fClearLogButton = new Button(StringDefs.CLEAR_LOG);
        fClearLogButton.addActionListener(this);
        fCursorControl.addCursorComponent(fClearLogButton);
        fCursorControl.addEnablableComponent(fClearLogButton);
        constraints.gridwidth   	= GridBagConstraints.REMAINDER;
        constraints.anchor      	= GridBagConstraints.EAST;
		constraints.insets.right	= 2; // (2,0,2,2)
        gridbag.setConstraints(fClearLogButton, constraints);
        add(fClearLogButton);
        //
        // To List
        //
        fToList      = new XTextField( 1 /*Context.kWindowWidth */);
		fBGColorManager.add(fToList);
        fToList.addKeyListener(fShiftKeyAdapter);
        fCursorControl.addCursorComponent(fToList);
        fCursorControl.addEnablableComponent(fToList);
     
        fSecondPanel = new Panel();
        fSecondPanel.setLayout(new CardLayout());
        fCursorControl.addCursorComponent(fSecondPanel);

        Panel tempPanel = new Panel();
        fCursorControl.addCursorComponent(tempPanel);
        GridBagLayout tempGBL = new GridBagLayout();
        GridBagConstraints tempGBC = new GridBagConstraints();
        tempPanel.setLayout(tempGBL);
        
        fToLabel = new Label(kToList);
        fCursorControl.addCursorComponent(fToLabel);
        fCursorControl.addEnablableComponent(fToLabel);
        tempGBC.gridwidth   = 1;
        tempGBC.anchor      = GridBagConstraints.WEST;
		tempGBC.insets.left	= 2; // (0,2,0,0) 
        tempGBL.setConstraints(fToLabel, tempGBC);
        tempPanel.add(fToLabel);
        tempGBC.gridwidth   = GridBagConstraints.REMAINDER;
        tempGBC.fill        = GridBagConstraints.HORIZONTAL;
        tempGBC.weightx     = 1.0;
        tempGBL.setConstraints(fToList, tempGBC);
        tempPanel.add(fToList);
        fSecondPanel.add(kToList, tempPanel);
        //
        //
        //
        tempPanel = new Panel();
        fCursorControl.addCursorComponent(tempPanel);
        tempGBL = new GridBagLayout();
        tempGBC = new GridBagConstraints();
        tempPanel.setLayout(tempGBL);
        fRecordedMsgLabel = new Label(kRecordedMsg);
        tempGBC.gridwidth   = 1;
        tempGBC.anchor      = GridBagConstraints.WEST;
        tempGBL.setConstraints(fRecordedMsgLabel, tempGBC);
        tempPanel.add(fRecordedMsgLabel);

        fSecondPanel.add(kRecordedMsg, tempPanel);
        //
        // Add Popup hints to labels
        //
        fToLabel.addMouseListener(new PopupMenuAdapter(fToLabel, fRecipientHintsPopup));
        fRecordedMsgLabel.addMouseListener(new PopupMenuAdapter(fRecordedMsgLabel,fRecordedMsgHintsPopup));
        //
        // Create input area and log area
        //     
        fInputArea = new InputTextArea(5,  Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);
		fBGColorManager.add(fInputArea);
        fInputArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) { 
        		int keyCode = keyEvent.getKeyCode();
        		if (KeyUtil.isDeliverKey(keyCode)) 
            		fDeliverer.deliver(fToList.getText(), fInputArea);
		  	}
		});
        fCursorControl.addCursorComponent(fInputArea);
        fCursorControl.addEnablableComponent(fInputArea);
        fLogTextArea   = new TextArea("", 15, Context.WINDOW_WIDTH, TextArea.SCROLLBARS_VERTICAL_ONLY);
		fBGColorManager.add(fLogTextArea);
        fLogTextArea.setEditable(false);
        fCursorControl.addCursorComponent(fLogTextArea);
        
        constraints.fill        	= GridBagConstraints.HORIZONTAL;
        constraints.anchor      	= GridBagConstraints.WEST;
        constraints.gridwidth   	= GridBagConstraints.REMAINDER;
        constraints.weightx     	= 1.0;
        constraints.weighty     	= 0.0;
		constraints.insets.top		= 0;
		constraints.insets.left		= 0;
		constraints.insets.bottom	= 0;
		constraints.insets.right	= 0; // (0,0,0,0)
        gridbag.setConstraints(fSecondPanel, constraints);
        add(fSecondPanel);

        constraints.fill        = GridBagConstraints.BOTH;
        constraints.weighty     = 1.0;
        gridbag.setConstraints(fInputArea, constraints);
        add(fInputArea);

        gridbag.setConstraints(fLogTextArea, constraints);
        add(fLogTextArea);

        setFonts();
		fPropertiesDB.addPropertyChangeListener(this); // this must be done after SetFonts
        //
        // The loaded size is saved to fInitialSize which is 
        // used in setVisible() to reset the height.
        // 
        fInitialSize = fPropertiesDB.getLastSize();
        
        if (fInitialSize == null)
            pack();
        else 
            setSize(fInitialSize);
            
        fCursorControl.addCursorComponent(this);
        
        setIcon(kNormalIcon);
        // 
        // Restore the last location.
        // 
        ComponentUtil.fitComponentIntoScreen(this, fPropertiesDB.getLastLocation());

		fLogging 				= new Logging("MessagingTool.log", new LoggingGUIImpl(this, fFileSavedMessages));
		fLogArea 				= new LogAreaImpl(this, fLogTextArea, fLogging);
		MainUI mainUI 			= new MainUIImpl();
		fDedicatedUIManager		= new DedicatedUIManager<MenuItem>(mainUI, this, fUIFactory);
		fMeetingRoomUIManager	= new MeetingRoomUIManager<MenuItem>(mainUI, this, fUIFactory, fFileSavedMeetingMessages);
		fOnlineListUI 			= fUIFactory.createOnlineListUI(StringDefs.ON_LINE, mainUI, fDedicatedUIManager);
		fDeliverer 				= new DelivererImpl<MenuItem>(mainUI, fLogArea, fOnlineListUI, fDedicatedUIManager);
		MessageControl.getInstance().setMessageListener(new MessageControl.MessageListener() {
			public void allMessagesShown() { setIcon(kNormalIcon); }
		});
		fSearchUI				= fUIFactory.createSearchUI(this);

        // Register Meeting Listener
		fMeetingManager			= new MeetingManagerImpl<MenuItem>(fMeetingRoomUIManager, mainUI);
        fMeetingListener 		= new MeetingListenerImpl(fMeetingManager);
        fMeetingProtocol.setMeetingListener(fMeetingListener);
		// create MeetingRoomList
		fMeetingRoomListUI 		= new MeetingRoomListUI(fMeetingManager, StringDefs.MEETING_ROOMS); 
		fUIFactory.setMeetingRoomListUI(fMeetingRoomListUI);
		// MessagProtocol Listener
		fAboutMessageReceived = (AboutUIImpl) fUIFactory.createMessageReceivedUI(this);
		MessageProtocolListener messageProtocolListener = 
			new MessageProtocolListenerImpl<MenuItem>(
						mainUI, fOnlineListUI, fInputArea, fLogArea, fDedicatedUIManager, fAboutMessageReceived);
		fMessageProtocol.addMessageProtocolListener(messageProtocolListener);
		// MiscProtocolListener
		fMiscProtocol.addMiscProtocolListener(new MiscProtocolListenerImpl(
						mainUI, fOnlineListUI, fSearchUI, fMeetingManager, messageProtocolListener));
		// FTP Listener Factory
        FTP.setFTPListenerFactory(new FTPListenerFactoryImpl(this));
		// MainFrameFeatures
		fMainFrameFeatures = new MainFrameFeaturesImpl<MenuItem>(fLogArea, fMeetingRoomUIManager);
        }
    
	private class MainUIImpl implements MainUI {
		public void addDedicatedUI(DedicatedUI dedicatedUI) {
			MenuItem	menuItem = new MenuItem(dedicatedUI.getTitle());
			
			fDedicatedUIManager.put(menuItem, dedicatedUI);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					fDedicatedUIManager.get((MenuItem)event.getSource()).setVisible(true);
				}
			});
			menuItem.setFont(Context.getFont());
            fWindowMessagingDialogs.setEnabled(true);
            fWindowMessagingDialogs.add(menuItem);
            dedicatedUI.setMenuItem(menuItem);			
		}

		public void addMeetingRoomUI(MeetingRoomUI meetingRoomUI) {
			MenuItem	menuItem = new MenuItem(meetingRoomUI.getExternalRoomName());

			fMeetingRoomUIManager.put(menuItem, meetingRoomUI);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					fMeetingRoomUIManager.get((MenuItem)event.getSource()).setVisible(true);
				}
			});
			menuItem.setFont(Context.getFont());
			fWindowMeetingRooms.setEnabled(true);
			fWindowMeetingRooms.add(menuItem);
			meetingRoomUI.setMenuItem(menuItem);
		}

		public void removeMeetingRoomUI(MeetingRoomUI meetingRoomUI) {
        	int         count = fWindowMeetingRooms.getItemCount();
			String		externalName = meetingRoomUI.getExternalRoomName();
        	MenuItem    item  = null;

        	for (int i = 0; i < count ; i++) {
            	item = fWindowMeetingRooms.getItem(i);
            	if (externalName.equals(item.getLabel())) {
                	fWindowMeetingRooms.remove(item);
					if (count == 1)
						fWindowMeetingRooms.setEnabled(false); 
                	return;
                }
            }
		}

		public void beep() {
			MainFrame.this.getToolkit().beep();
		}

		public boolean isIconified() {
			return fIconified;
		}

		public boolean isInOffice() {
			return !fNotInOffice;
		}

		public boolean isEditAddressFileVisible() {
			if (fEditAddressFile == null)
				return false;
		  	return fEditAddressFile.isVisible();
		}

		public void setNotInOfficeEnabled(boolean	enabled) {
			fNotInOfficeCheckBox.setEnabled(enabled);
		}

		public void setToList(String toList) {
        	fToList.setText(toList);
        }

		public void setMessageWaiting(boolean waiting) {
			if (waiting)
				setIcon(kWaitingIcon);
		  	else
				setIcon(kNormalIcon);
		}

		public void toFront() {
			MainFrame.this.toFront();
		}
	}

    public void setVisible(boolean visible) {
		fLogTextArea.invalidate();  	// reshape fLogTextArea [V2.12]
        super.setVisible(visible);
        //
        // Note that visible(true) is called only once. A frame which has a menu bar
        // grows by a peer implementation. Therefore adjust the height. [V1.95]
        //  
        if (visible) {
            Dimension currentSize = getSize();
            
            if (fInitialSize != null && fInitialSize.height != currentSize.height)
                setSize(fInitialSize);
		 	//
			// validate to reshape fLogTextArea correctly [V2.30]
			//
			validate();
            }
        } 
           
    private void restoreVisibilityOfLists() {
        if (fPropertiesDB.isOnlineDialogVisible())
            fOnlineListUI.setVisible(true);
        if (fPropertiesDB.isRoomListDialogVisible())
            fMeetingRoomListUI.setVisible(true);
        }
    
    private void setIcon(int    iconType)
        {     
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
        switch(iconType) {
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
        Font    font = Context.getFont();
        
        if (font == null)
            return;
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
        fClearLogButton.setFont(font);
        fToLabel.setFont(font);
        fToList.setFont(font);
        fRecordedMsgLabel.setFont(font);
        fInputArea.setFont(font);
        fLogTextArea.setFont(font);
        //
        // Popup Hints
        //
        Util.setFontsToMenu(fRecipientHintsPopup, font);
        Util.setFontsToMenu(fRecordedMsgHintsPopup, font);
        }
     
    // =================================
    // WindowListener
    // =================================
    public void windowClosed(WindowEvent event) {
        setVisible(false);
        System.exit(0);
        } 
   
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
        }
   
    public void windowIconified(WindowEvent event) { 
        //
        // The following code doesn't work with JDK 1.1.
        // I need to file a bug report.
        //
        // if (fAboutMessageReceived != null)
        //   fAboutMessageReceived.setVisible(false);
        fIconified = true;
        if (fNotInOfficeCheckBox.getState())
            setIcon(kNotInOfficeIcon);
        }
      
    public void windowActivated(WindowEvent event) {}  
    public void windowDeactivated(WindowEvent event) {}
      
    public void windowOpened(WindowEvent event) {
        //
        // Now the main window is opened, so retore the visibility of
        // the online list. Opening the online list before opening the main
        // window doesn't work with jdb.
        //
        restoreVisibilityOfLists();
                   
        Thread  onlineWatcher = new OnlineWatcher(fOnlineListUI);
        onlineWatcher.setDaemon(true);
        onlineWatcher.start();
        }
   
    public void windowClosing(WindowEvent event) {
        if (fQuitUI == null)
            fQuitUI = new QuitUI(this);
        
        getToolkit().beep();
        if (fQuitUI.confirm()) 
            terminateTool(); 
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
        if (!fIconified)
            fPropertiesDB.setLastLocation(getLocation());
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
        
        if (fPropertiesUI != null)
            fPropertiesUI.setVisible(false);
        if (fAboutMessagingTool != null)
            fAboutMessagingTool.setVisible(false);
        if (fAboutSystemProperties != null)
            fAboutSystemProperties.setVisible(false);
        fAboutMessageReceived.setVisible(false);
        if (fEditAddressFile != null)
            fEditAddressFile.setVisible(false);
        if (fEditRecipientFile != null)
            fEditRecipientFile.setVisible(false);
        if (fEditRecordedMsgFile != null)
            fEditRecordedMsgFile.setVisible(false);
            
        AnotherUI   another = fAnotherUI;
        while (another != null ) {
            another.setVisible(false);
            another = another.getNext();
            }
	  	fDedicatedUIManager.close(); 
		fLogging.stop();
        //
        // Notify OffLine
        //
        String[]    onlines = fOnlineListUI.getOnlines();
        
        if (onlines != null && onlines.length > 0) {
            Thread  multipleNotifier = new MultipleNotifier(
                                fPropertiesDB.getUserName(),
                                onlines,
                                0,
                                MultipleNotifier.kOffLine);
            multipleNotifier.start();
            TimerUtil.sleep(1000); // sleep only 1 second.
            }
            
        System.exit(0);
        }
    // ================================
    // ItemListener
    // ================================
    public void itemStateChanged(ItemEvent event) {
        boolean isNotInOffice = fNotInOfficeCheckBox.getState();
        
        //
        // Note that with JDK 1.1, somehow itemStateChanged() is called twice with 
        // the same event. Therefore, ignore the second call of this function by
        // remembering the current stat of fNotInOfficeCheckBox.
        //
        if (fNotInOffice == isNotInOffice)
            return;
        else
            fNotInOffice = isNotInOffice;
     
        String[]    onlines = fOnlineListUI.getOnlines();
            
        if (isNotInOffice) {
            ((CardLayout)fSecondPanel.getLayout()).show(
                        fSecondPanel, kRecordedMsg);
            fDeliverButton.setEnabled(false);
            fSavedMsg = fInputArea.getText();
            fInputArea.setText(fRecordedMsg);
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
            }
        else {
            ((CardLayout)fSecondPanel.getLayout()).show(
                        fSecondPanel, kToList);
            fDeliverButton.setEnabled(true);
            fRecordedMsg = fInputArea.getText();
            fInputArea.setText(fSavedMsg);
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
            }
        else if (target == fClearLogButton) {
			fLogArea.clear();
            }
        else if (target == fFilePrintItem) {
            PrintJob    printJob = getToolkit().getPrintJob(this, "MessagingTool", null);
            
            if (printJob != null) {
                Graphics g = printJob.getGraphics();
                 
                if (g != null) {
                    fLogTextArea.printAll(g);
                    g.dispose();
                    }
                printJob.end();
                }
            }
        else if (target == fFileQuitItem) 
            terminateTool();
        else if (target == fAboutMessagingToolItem) {
            if (fAboutMessagingTool == null)
                fAboutMessagingTool = new AboutUIImpl(this,
                          AboutUI.kMessagingTool,
                          StringDefs.ABOUT_MESSAGING_TOOL);
                fAboutMessagingTool.setVisible(true);
                }
        else if (target == fAboutSystemPropertiesItem) {
            if (fAboutSystemProperties == null)
                fAboutSystemProperties = new AboutUIImpl(this,
                         AboutUI.kSystemProperties,
                         StringDefs.ABOUT_SYSTEM_PROPERTIES);
                fAboutSystemProperties.setVisible(true);
                }
        else if (target == fEditAddressFileItem) {
            if (fEditAddressFile == null)
                fEditAddressFile = new EditUI(this, new AddressEditImpl());
            fEditAddressFile.setVisible(true);
            }
        else if (target == fEditRecipientFileItem) {
            if (fEditRecipientFile == null)
                fEditRecipientFile = new EditUI(this, new RecipientEditImpl());
            fEditRecipientFile.setVisible(true);
            }
        else if (target == fEditRecordedMsgFileItem) {
            if (fEditRecordedMsgFile == null)
                fEditRecordedMsgFile = new EditUI(this, new RecordedMsgEditImpl());
            fEditRecordedMsgFile.setVisible(true);
            }
	  	else if (target == fEditMeetingRoomFileItem) {
			if (fEditMeetingRoomFile == null)
				fEditMeetingRoomFile = new EditUI(this, new MeetingRoomEditImpl());
		  	fEditMeetingRoomFile.setVisible(true);
			}
        else if (target == fEditPropertiesItem) {
            if (fPropertiesUI == null) {
                //
                // At initial, there is no entry for Font in PropertiesDB.
                // So, before opening the edit window, save the current font.
                //
                Font    font = fDeliverButton.getFont();
                fPropertiesDB.setFontName(font.getName());
                fPropertiesDB.setFontSize(font.getSize());
                fPropertiesDB.setFontStyle(font.getStyle());
                fPropertiesDB.saveProperties(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
                    
                fPropertiesUI = new PropertiesUI(this,
                    StringDefs.MESSAGING_TOOL_PROPERTIES);
                }
            fPropertiesUI.setVisible(true);
            }
        else if (target == fWindowAnotherItem) {
            if (fAnotherUI == null) {
                fAnotherUI = new AnotherUI(this, fDeliverer,
                                        StringDefs.SENDING_WINDOW,
                                        !fNotInOffice);
                fAnotherUI.setVisible(true);
                }
            else { 
                //
                // Search deactivated dialog.
                //
                AnotherUI   current   = fAnotherUI;
                boolean         activated = current.isActivated();
                AnotherUI   next      = current.getNext();
                
                while (activated == true) {
                    if (next == null) { 
                        AnotherUI   another = new AnotherUI(this, fDeliverer,
                                            StringDefs.SENDING_WINDOW,
                                            !fNotInOffice);

                        current.setNext(another);
                        another.setVisible(true);
                        return;
                        }
                     else {
                        current     = next;
                        activated   = current.isActivated();
                        next        = current.getNext();
                        }
                    }
                current.setVisible(true);
                } 
            }
        else if (target == fWindowOnlineListItem) {
            fOnlineListUI.setVisible(true);
            }
        else if (target == fWindowMeetingRoomListItem) {
            fMeetingRoomListUI.setVisible(true);
            }
        else if (target == fToolSendFileItem) {
            if (fFileSendUI == null)
                fFileSendUI = new FileSendUI(this);
            
            fFileSendUI.setVisible(true);
            }
        else if (target == fToolSearchUserItem) {
            fSearchUI.setVisible(true);
            }
        else if (target == fToolNewMeetingRoomItem) {
            if (fNewMeetingRoomUI == null) {
                fNewMeetingRoomUI = new NewMeetingRoomUI(this, fMeetingListener, fMeetingManager, true);
                fMeetingProtocol.roomOpened("");
                }
            fNewMeetingRoomUI.setVisible(true);
            }
        else if (target == fToolDeleteMeetingRoomItem) {
            if (fDeleteMeetingRoomUI == null) {
                fDeleteMeetingRoomUI = new NewMeetingRoomUI(this, fMeetingListener, fMeetingManager, false);
                fMeetingProtocol.roomOpened("");
                }
            fDeleteMeetingRoomUI.setVisible(true);
            }
        else if (target instanceof MenuItem) {
            MenuItem      item       = (MenuItem)target;
            String        menuLabel  = item.getLabel();
            MenuContainer parentMenu = item.getParent();

            if (parentMenu == fRecipientHintsPopup) {
                String  expandedHint = fRecipientHintsDB.getExpandedRecipients(menuLabel);
                
                Util.recipientHintSelected(expandedHint, fToList, fShiftKeyAdapter.isShiftKeyPressed());
                }
            else if (parentMenu == fRecordedMsgHintsPopup) {
                fInputArea.setText(menuLabel);
                }
            else if (parentMenu == fWindowMeetingRooms) {
                MeetingRoomUI meetingRoom = fMeetingManager.findMeetingRoomUIByExternalName(menuLabel);
                
                meetingRoom.setVisible(true);
                }
            }
        }
    // ============================================
    // Implementation of Observer Interface
    // ============================================
    public void update(
        Observable      observable, 
        Object          obj) {

        if (observable == fRecipientHintsDB ||
            observable == fAddressDB) {
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
            AnotherUI   next = fAnotherUI;
            while (next != null ) {
                next.updateRecipientHintsPopup();
                next = next.getNext();
                }

		  	if (fFileSendUI != null)
				fFileSendUI.updateRecipientHintsPopup();
            
			if (fDedicatedUIManager != null)
				fDedicatedUIManager.updateRecipientHintsPopup();
            }
        else if (observable == fRecordedMsgHintsDB) {
            Util.updateHintsMenu(fRecordedMsgHintsPopup, fRecordedMsgHintsDB.getDB(), this);
            Util.setFontsToMenu(fRecordedMsgHintsPopup, Context.getFont());
            }
        }
    // =======================
    // PropertyChangeListener
    // =======================
    public void propertyChange(PropertyChangeEvent event) {
        String  propertyName = event.getPropertyName();
        
        if (propertyName.equals(PropertiesDB.kName)) {
            setFonts();
            pack();

			fLogging.update(); 
            }
        }
    // ========================================
    // MainFrameFeatures implementation
    // ========================================
    public void showMyAddress() {
		fMainFrameFeatures.showMyAddress();
   	}
        
    public void checkIfIPAddressChanged() {
		fMainFrameFeatures.checkIfIPAddressChanged();
   	}

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
// 1.85 : 13-Jan-98 Y.Shibata   added code to call Logging_Stop () when terminating.
// 1.86 :  4-Jan-98 Y.Shibata   fixed a bug that "Clear Log" doesn't reset fLogLength.
// 1.94 :  4-Jul-98 Y.Shibata   modified to use msgtool.util.Component
// 1.95 : 11-Jul-98 Y.Shibata   modified to use msgtool.util.TimerUtil
//                              added setVisible() for workaround of a bug of JDK 1.1.6?
// 2.00 :  5-Aug-98 Y.Shibata   add onCommandLineMessage
// 2.10 : 12-Oct-98 Y.Shibata   FTP implementation.
// 2.14 : 13-Feb-99	Y.Shibata	"*" is used for user matching
// 2.15 : 27-Feb-99	Y.Shibata	used PopupMenuAdapter class
// 2.19 : 27-Mar-99	Y.Shibata	used BGColorManager
// 2.22 : 11-Apr-99	Y.Shibata	modified not to new Insets
// 2.30 :  9-May-99	Y.Shibata	used XTextField for Dnd
// 2.33 : 12-Aug-99	Y.Shibata	replaced Log_XXXX() methods with LogArea class [Refactoring]
//        21-Aug-99	Y.Shibata	replaced Logging_XXXX methods with Logging class [Refactoring]
// 2.35 :  6-Nov-99	Y.Shibata	REFACTORING......
// 2.38 : 29-Nov-99	Y.Shibata	The implementation of "Message Waiting Status" has been changed.
// 2.43 :  5-Mar-00	Y.Shibata	replaced SetFont() with Context.updateFont().
// 2.50 : 27-Dec-03 Y.Shibata   used Java Generics

