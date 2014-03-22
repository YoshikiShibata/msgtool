// File StringDefs.java - last edit:
// Yoshiki Shibata  18-Jan-04

// Copyright (c) 1997, 1998, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.util;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class StringDefs {
    private static ResourceBundle   messageResource    = null; 

	static {
		try {
        	messageResource = ResourceBundle.getBundle("msgtool.MessageResources"); 
        } catch (MissingResourceException e) {
        	assert false;
        }	
	}
	
    public final static String  ACTIVE_ON_RECEPTION_C  	= getString("Activate On Reception:");
    public final static String  ALL                    	= getString("ALL");
    public final static String  ALL_AREAS              	= getString("ALL-AREAS");
    public final static String  ABORTED                	= getString("Aborted");
    public final static String  ABOUT                  	= getString("About");
    public final static String  ABOUT_MESSAGING_TOOL   	= getString("About MessagingTool");
    public final static String  ABOUT_SYSTEM_PROPERTIES	= getString("About System Properties");
    public final static String  ADD                    	= getString("Add");
    public final static String  ADDITIONAL_TO_C        	= getString("Additional To:");
    public final static String  ADDRESS_C              	= getString("Address:");
    public final static String  ADDRESS_CACHE_PPP      	= getString("Address Cache ...");
    public final static String  ANOTHER_PPP            	= getString("Another ...");
    public final static String  ARE_YOU_SURE_QUITING   	= getString("Quit: Are you sure?");
	public final static String  BACK_ALL_NOTES			= getString("Back All Sticky Notes");
    public final static String  BEEP_ON_RECEPTION      	= getString("Beep On Reception:");
    public final static String  BLACK                  	= getString("Black");
    public final static String  BLUE                   	= getString("Blue");
    public final static String  BOLD                   	= getString("Bold");
    public final static String  BOLD_ITALIC            	= getString("Bold Italic");
    public final static String  BROADCAST_MESSAGE      	= getString("Broadcast Message");
    public final static String  BYTES                  	= getString("bytes");
    public final static String  CANCEL                 	= getString("Cancel");
    public final static String  CHOOSE_A_FILE          	= getString("Choose a file");
	public final static String  CLEAR_ALL			 	= getString("Clear All");
    public final static String  CLEAR_LOG              	= getString("Clear Log");
	public final static String  CLEAR_STYLE			 	= getString("Clear Style");
    public final static String  CLOSED_ROOM            	= getString("Closed Room:");
    public final static String  CLOSED                	= getString("CLOSED ");
    public final static String  COMPLETED              	= getString("Completed");
    public final static String  CONNECTING_PPP         	= getString("Connecting ...");
    public final static String  CONNECTED              	= getString("Connected");
    public final static String  COPY_INTO_TO_C         	= getString("Copy into To:");
    public final static String  COPY                   	= getString("Copy");
    public final static String  CYAN                   	= getString("Cyan");
    public final static String  CUT                    	= getString("Cut");
    public final static String  DARK_GRAY              	= getString("Dark Gray");
    public final static String  DELETE                 	= getString("Delete");
    public final static String  DELETE_MEETING_ROOM    	= getString("Delete Meeting Room");
    public final static String  DELETE_MEETING_ROOM_PPP	= getString("Delete Meeting Room ...") ;
	public final static String  DELETE_THIS_NOTE		  	= getString("Delete This Note");
    public final static String  DELIVER                	= getString("Deliver");
    public final static String  DELIVER_KEY_C          	= getString("Deliver Key:");
	public final static String  DESKTOP_COLOR			= getString("Desktop Color");
    public final static String  EDIT                   	= getString("Edit");
    public final static String	EDIT_PPP				= getString("Edit ...");
    public final static String  EDIT_ADDRESS_CACHE     	= getString("Edit: Address Cache");
	public final static String  EDIT_MEETING_ROOM	  	= getString("Edit: Meeting Room");
    public final static String  EDIT_RECIPIENT_HINTS		= getString("Edit: Recipient Hints");
    public final static String  EDIT_RECORDED_MESSAGE_HINTS = getString("Edit: Recorded Message Hints");
    public final static String  EMAIL_ADDRESS_C         	= getString("E-Mail Address:");
    public final static String  ERROR                  	= getString("Error");
    public final static String  FAILED                 	= getString("(failed)");
	public final static String  FETCH_LOG				= getString("Fetch log");
    public final static String  FILE                   	= getString("File");
	public final static String  FILE_EXISTS				= getString("File exits");
    public final static String  FILES_PPP              	= getString("Files ...");
    public final static String  FILE_NAME_C             	= getString("File Name:");
    public final static String  FILE_NOT_FILE    		= getString("Please specify a file");
    public final static String  FILE_NOT_FOUND 			= getString("File is not found");
    public final static String  FILE_NOT_READABLE 		= getString("File cannot be read");
    public final static String  FILE_NOT_SPECIFIED 		= getString("Please specify a file");
    public final static String  FILE_TRANSFER           	= getString("File Transfer");
	public final static String  FONT						= getString("Font");
    public final static String  FONT_NAME_C             	= getString("Font Name:");
    public final static String  FONT_SIZE_C             	= getString("Font Size:");
	public final static String  FONT_SIZE				= getString("Font Size");
	public final static String  FONT_STYLE				= getString("Font Style");
    public final static String  FONT_STYLE_C            	= getString("Font Style:");
	public final static String  FRONT_ALL_NOTES			= getString("Front All Sticky Notes");
    public final static String  FUNCTION_KEY            	= getString("Function Key");
    public final static String  GRAY                   	= getString("Gray");
    public final static String  GREEN                  	= getString("Green");
    public final static String  HELP                   	= getString("Help");
	public final static String  IMAGE_PPP				= getString("Image ...");
    public final static String  IM_NOT_IN_MY_OFFICE 		= getString("I'm not in my office");
    public final static String  INITIALIZE_SERVER_ERROR 	= getString("InitializeServerError");
	public final static String  INSERT					= getString("Insert");
    public final static String  ITALIC                 	= getString("Italic");
    public final static String  JOIN                   	= getString("Join");
    public final static String  JOINED                 	= getString("Joined");
    public final static String  JOIN_MEETING_ROOM      	= getString("Join Meeting Room");
    public final static String  JOIN_MEETING_ROOM_PPP  	= getString("Join Meeting Room ...");
	public final static String  JUSTIFICATION			= getString("Justification");
	public final static String  JUSTIFY_CENTER			= getString("Justify Center");
	public final static String  JUSTIFY_LEFT				= getString("Justify Left");
	public final static String  JUSTIFY_RIGHT			= getString("Justify Right");
    public final static String  LEAVE                  	= getString("Leave");
    public final static String  LEFT                   	= getString("Left");
    public final static String  LIGHT_GRAY              	= getString("Light Gray");
    public final static String  LIST                   	= getString("List");
    public final static String  LIST_AS_HINT       		= getString("List as hint:");
    public final static String  LOGGED_MEETING_MESSAGES 	= getString("Logged Meeting Messages");
    public final static String  LOG_C                  	= getString("Log:");
    public final static String  LOGGED_MESSAGES         	= getString("Logged Messages");
    public final static String  LOGGING                	= getString("Logging");
    public final static String  LOG_MESSAGES_C          	= getString("Log Messages:");
    public final static String  LOG_OVERFLOWED          	= getString("Log Overflowed");
    public final static String  LOOK_AND_FEEL_C			= getString("Look & Feel:");
    public final static String  MAGENTA                	= getString("Magenta");
    public final static String  MAX_LOG_FILES_C 			= getString("Max Log files:");
    public final static String  MEETING_ROOM            	= getString("Meeting Room");
	public final static String  MEETING_ROOM_PPP			= getString("Meeting Room ...");
    public final static String  MEETING_ROOMS           	= getString("Meeting Rooms");
    public final static String  MEETING_ROOM_LIST_PPP	= getString("Meeting Room List ...") ;
    public final static String  MESSAGE_C              	= getString("Message:");
    public final static String  MESSAGE_TOO_LONG			= getString("Message Too Long");
    public final static String  MESSAGE_WAITING 			= getString("Message Waiting");
    public final static String  MESSAGING_DIALOG			= getString("Messaging Dialog");
    public final static String  MESSAGING_TOOL_PPP		= getString("MessagingTool ...");
    public final static String  MESSAGING_TOOL_PROPERTIES = getString("MessagingTool Properties");
    public final static String  NAME_C                 	= getString("Name:");
    public final static String  MEMBERS_C              	= getString("Members:");
    public final static String  NEW_MESSAGE_ARRIVED		= getString("New message arrived");
	public final static String  NEW_STICKY_NOTE_PPP		= getString("New Sticky Note ...");
	public final static String  NO						= getString("No");
    public final static String  NO_HINT_AVAILABLE		= getString("No Hint Available");
    public final static String  NO_ROOM_AVAILABLE 		= getString("No room available");
    public final static String  NOT_IN_OFFICE    		= getString("Not In Office");
    public final static String  OK              			= getString("Ok");
    public final static String  ON_LINE        			= getString("OnLine");
    public final static String  ON_LINE_LIST_PPP 		= getString("OnLine List ...");
    public final static String  OPEN_MESSAGING_DIALOG 	= getString("Open Messaging Dialog");
    public final static String  ORANGE 					= getString("Orange");
	public final static String  OTHER_COLORS_PPP			= getString("Other Colors ...");
    public final static String  OVERVIEW_PPP 			= getString("Overview ...");
	public final static String  PAGE_SETUP_PPP			= getString("Page Setup ...");
    public final static String  PARTICIPANTS           	= getString("Participants");
    public final static String  PASTE                  	= getString("Paste");
    public final static String  PINK                   	= getString("Pink");
    public final static String  PLAIN                  	= getString("Plain");
    public final static String  PRINT_PPP              	= getString("Print...");
    public final static String  PREFERENCES            	= getString("Preferences");
    public final static String  PROGRESS_C             	= getString("Progress:");
    public final static String  PROPERTIES_PPP         	= getString("Properties ...");
    public final static String  QUIT                   	= getString("Quit");
    public final static String  RECEIVED               	= getString("(received)");
    public final static String  RECEPTION              	= getString("Reception");
    public final static String  RECEPTION_DIALOG        	= getString("Reception Dialog");
    public final static String  RECIPIENT              	= getString("Recipient");
    public final static String  RECIPIENT_C            	= getString("Recipient:");
    public final static String  RECIPIENT_HINTS_PPP     	= getString("Recipient Hints ...");
    public final static String  RECIPIENT_NAME_C        	= getString("Recipient Name:");
    public final static String  RECIPIENT_NOT_SPECIFIED	= getString("No recipient is specified");
    public final static String  RECORDED_MESSAGE        	= getString("Recorded Message");
    public final static String  RECORDED_MESSAGE_C      	= getString("Recorded Message:");
    public final static String  RECORDED_MESSAGE_HINTS_PPP= getString("Recorded Message Hints ...");
    public final static String  RED                    	= getString("Red");
    public final static String  REGISTER               	= getString("Register");
    public final static String  REGISTERED_NUMBER_C     	= getString("Registered Number:");
    public final static String  ROOMS                  	= getString("Rooms");
    public final static String  ROOM_NAME_C             	= getString("Room Name:");
	public final static String  SAVE_ALL_STICKY_NOTES	= getString("Save All Sticky Notes");
    public final static String  SAVE_FILE_AS 			= getString("Save File As");
    public final static String  SEARCH       			= getString("Search");
    public final static String  SEARCH_PPP  				= getString("Search ...");
    public final static String  SELECT_ALL 				= getString("Select All");
    public final static String  SEND  					= getString("Send");
    public final static String  SEND_A_FILE  			= getString("Send a file");
    public final static String  SEND_A_FILE_PPP 			= getString("Send a file ...");
    public final static String  SENDER_NAME_C 			= getString("Sender Name:");
    public final static String  SENDING_WINDOW 			= getString("Sending Window");
    public final static String  SET                 		= getString("Set");
    public final static String  SORT_KEY_C              	= getString("Sort Key:");
    public final static String  SYSTEM_PROPERTIES_PPP   	= getString("System Properties ...");
    public final static String  SYSTEM_TEXT             	= getString("System Text");
    public final static String  SYSTEM_WINDOW           	= getString("System Window");
    public final static String  TEXT_BACKGROUND_C       	= getString("Text Background:");
	public final static String  TEXT_COLOR				= getString("Text Color");
	public final static String  TIME_FORMAT1				= getString("Time Format 1");
	public final static String  TIME_FORMAT2				= getString("Time Format 2");
    public final static String  TO_C                   	= getString("To:");
    public final static String  TOOL                   	= getString("Tool");
    public final static String  TOPICS_PPP             	= getString("Topics ...");
    public final static String  USER                   	= getString("User");
    public final static String  USER_NAME_C             	= getString("User Name:");
	public final static String  UNDERLINE				= getString("Underline");
    public final static String  UNREGISTERED_COPY       	= getString("Unregistered Copy");
    public final static String  UNREGISTERED_MSG        	= getString("Unregistered Msg");
    public final static String  UPDATE                 	= getString("Update");
    public final static String  WHITE                  	= getString("White");
    public final static String  WINDOW                 	= getString("Window");
    public final static String  YELLOW                 	= getString("Yellow");
	public final static String  YES						= getString("Yes");
    
    // ====================================
    // Message Function for Internalization
    // ====================================
    final static private String getString(String keyString) {    
        if (messageResource == null) {
            return(keyString);
        }
        
        String  translatedMsg = null;
        try {
            translatedMsg = messageResource.getString(keyString);
        } catch(MissingResourceException e) {
            return(keyString);
        }
        return(translatedMsg);
    }
}

// LOG 
// 1.71 : 25-Oct-97 Y.Shibata   created.
// 1.77 : 19-Nov-97 Y.Shibata   added kClosed
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.util
// 2.50 : 13-Mar-03 Y.Shibata	refactored.
// 2.51 : 18-Jan-04	Y.Shibata	added BACK_ALL_NOTES
