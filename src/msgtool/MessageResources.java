/*
 * File: MessageResources.java - last edit:
 * Yoshiki Shibata 5-Oct-99
 *
 * Copyright (c) 1997-1999 by Yoshiki Shibata. All rights reserved.
 */
package msgtool;

import java.util.ListResourceBundle; 

public final class MessageResources extends ListResourceBundle { 
    public Object[][] getContents() {
        return contents;
        }
    
    static final Object[][] contents = {
            { "Unregistered Msg",       "Thank you for trying MessagingTool.\n\n" +
                                        "This is an unregistered copy of MessagingTool. " +
                                        "To register this copy, send an e-mail to majordomo@mma.so-net.ne.jp " +
                                        "with the following command in the body of your email message:\n\n" +
                                        "    subscribe MessagingToolUsers\n\n"+
                                        "By sending the message above, you will subscribe for the following " +
                                        "mailing list:\n" + 
                                        "    MessagingToolUsers@mma.so-net.ne.jp\n"+
                                        "The mailing list is used to give you release information and "+
                                        "exchange information among users. Please note that it takes " +
                                        "a few days to issue your Registered Number.\n\n"+
                                        "Thank you,\n\n" +
                                        "Yoshiki Shibata" },
            { "InitializeServerError",  "There is another copy of MessagingTool already " +
                                        "running or stuck trying to quit.\nPlease quit " +
                                        "the other copy or kill the stuck process and try again."},
            { "Log Overflowed",         "The Log Window might have been overflowed.\n"+
                                        "The window will be automatically cleared."},
            { "Unregistered Copy",      "Unregistered Copy"},
            { "To:" ,                   "To:" },
            { "Additional To:",         "Additional To:" },
            { "Recorded Message:",      "Recorded Message:" },
            { "File",                   "File" },
			{ "Page Setup ...",			"Page Setup ..." },
            { "Print..." ,              "Print..." },
            { "Logged Messages",        "Logged Messages"},
            { "Logged Meeting Messages","Logged Meeting Messages"},
            { "Quit",                   "Quit" },
            { "Edit" ,                  "Edit" },
            { "Address Cache ...",      "Address Cache ..."},
            { "Recipient Hints ...",    "Recipient Hints ..."  },
            { "Recorded Message Hints ..." , "Recorded Message Hints ..." },
			{ "Meeting Room ..." ,		"Meeting Room ..." },
            { "Properties ...",         "Properties ..." },
            { "Recipient",              "Recipient"},
            { "Recorded Message",       "Recorded Message"},
            { "Window",                 "Window" },
            { "Another ..." ,           "Another ..." },
            { "Meeting Room",           "Meeting Room" },
            { "OnLine List ...",        "OnLine List ..."},
            { "Meeting Room List ...",  "Meeting Room List ..."},
            { "Clear Log",              "Clear Log"},
            { "Tool",                   "Tool"},
			{ "Send a file ...",        "Send a file ..." },
            { "Search ...",             "User Search ..."},
            { "Join Meeting Room ...",   "Join Meeting Room ..."},
            { "Delete Meeting Room ...","Delete Meeting Room ..."},
            { "About",                  "About"},
            { "MessagingTool ...",      "MessagingTool ..." },
            { "System Properties ..." , "System Properties ..." },
            { "Help",                   "Help" },
            { "Overview ..." ,          "Overview ..." },
            { "Topics ...",             "Topics ..." },
            { "Deliver",                "Deliver" },
            { "Not In Office",          "Not In Office" },
            { "About MessagingTool",    "About MessagingTool" },
            { "About System Properties","About System Properties" },
            { "Edit: Address Cache" ,   "Edit: Address Cache"},
            { "Edit: Recipient Hints",  "Edit: Recipient Hints" },
            { "Edit: Recorded Message Hints", "Edit: Recorded Message Hints" },
			{ "Edit: Meeting Room",		"Edit: Automatically Joining Meeting Rooms" },
            { "MessagingTool Properties", "MessagingTool Properties"},
            { "(received)",             "(received)" },
            { "(failed)",               "(failed)" },
            { "Recorded Message: ",     "Recorded Message: " },
            { "I'm not in my office" ,  "I'm not in my office" },
            { "MessagingTool Notice" ,  "MessagingTool Notice" },
            { "No Hint Available",      "No Hint Available" },
            // Properties window 
            { "User Name:",             "User Name:" } ,
            { "Activate On Reception:", "Activate On Reception:" } ,
            { "Messaging Dialog",       "Messaging Dialog" },
            { "Reception Dialog",       "Reception Dialog" },
            { "Beep On Reception:",     "Beep On Reception:" },
            { "Deliver Key:",           "Deliver Key:"},
            { "Log Messages:",          "Log Messages:"},
            { "Max Log files:",         "Max Log files:"},
            { "Font Name:",             "Font Name:"  },
            { "Font Style:",            "Font Style:"  },
            { "Plain",                  "Plain" },
            { "Bold",                   "Bold" },
            { "Italic",                 "Italic" },
            { "Bold Italic",            "Bold Italic" },
            { "Font Size:" ,            "Font Size:"  },
            { "Text Background:",       "Text Background:" },
            { "Look & Feel:",           "Look & Feel:" },
            { "Cancel" ,                "Cancel" },
            { "Set",                    "Ok"},
            // Edit Window
            { "Add",                    "Add" },
            { "Delete" ,                "Delete" },
            { "Update",                 "Update" },
            { "Recipient:",             "Recipient:"},
            { "Members:",               "Members:" },
            { "Message:",               "Message:"} ,
            { "Name:",                  "Name:" },
            { "Sort Key:",              "Sort Key:" },              
            { "Address:",               "Address:" } ,
            { "List as hint:",          "List as hint:" },
            // New Message Recived Dialog
            { "New message arrived",    "New message arrived" },
            // Sending Window
            { "Sending Window",         "Sending Window" },
            // Register Window
            { "Register",               "Register"},
            { "E-Mail Address:",        "E-Mail Address:" },
            { "Registered Number:",     "Registered Number:" },
            // -- Window Title --
            { "Message Waiting",        "Message Waiting"} ,
            // --- Quit Dialog Window ---
            { "Quit: Are you sure?",    "Quit: Are you sure?" },
            // --- Online list ----
            { "OnLine",                 "OnLine" },
            { "List",                   "List" },
            { "Copy into To:",          "Copy into To:"},
            { "Open Messaging Dialog",  "Open Messaging Dialog"},
            { "Select All",             "Select All"},
            // --- Message Log ----
            { "Log:",                   "Log:"},
            // --- Search Dialog ---
            { "Search",                 "Search"},
            // --- Meeting Room Dialog ---
            { "Join Meeting Room",      "Join Meeting Room"},
            { "Delete Meeting Room",    "Delete Meeting Room" },
            { "Room Name:",             "Room Name:"},
            { "Closed Room:",           "Private Room:"},
            { "Join",                   "Join"},
            { "Participants",           "Participants"},
            { "Leave",                  "Leave"},
            { "Joined",                 "Joined"},
            { "Left",                   "Left"},
            { "CLOSED ",                "PRIVATE "},
            { "No room available",      "No room available"},
            { "Meeting Rooms",          "Meeting Rooms"},
            { "Message Too Long",       "Message Too Long! Please shorten it."} ,
            { "Ok",                     "Ok" },
			{ "Fetch log",				"Fetch log:" },
            // --- Popup menu ---
            { "Cut",                    "Cut" },
            { "Copy",                   "Copy" },
            { "Paste",                  "Paste" },
			{ "Clear All",				"Clear All" },
            // --- Colors ----
            { "White",                  "White" },
            { "Light Gray",             "Light Gray" },
            { "Gray",                   "Gray" },
            { "Dark Gray",              "Dark Gray" },
            { "Black",                  "Black" },
            { "Red",                    "Red" },
            { "Yellow",                 "Yellow" },
            { "Orange",                 "Orange" },
            { "Pink",                   "Pink" },
            { "Magenta",                "Magenta" },
            { "Cyan",                   "Cyan" },
            { "Green",                  "Green" },
            { "Blue",                   "Blue" },
            { "System Text",            "System Text Background" },
            { "System Window",          "System Window Background" } ,
            { "Desktop Color",			"Desktop Color" },
            // Properties Dialog tabs
            { "User",                   "User" },
            { "Reception",              "Reception" },
            { "Function Key",           "Function Key" },
            { "Logging",                "Logging" },
            { "Preferences",            "Preferences" },
            // Broadcast Message
            { "Broadcast Message",      "[Broadcast Message] " },
			// File Send Dialog
            { "Send a file",            "Send a file" },
            { "File Name:",             "File Name:"},
            { "Files ...",              "Files ..."},
            { "Send",                   "Send"},
            { "File is not found",      "File is not found"},
            { "File cannot be read",    "File cannot be read"},
            { "Please specify a file",  "Please specify a file"},
            { "No recipient is specified", "No recipient is specified"},
            { "Please specify a file",  "Please specify a file"},
            { "File Transfer",          "File Transfer" },
            { "Recipient Name:",        "Recipient Name:"},
            { "Sender Name:",           "Sender Name:"},
            { "Progress:",              "Progress:" },
            { "Connecting ...",         "Connecting ..."},
            { "Connected",              "Connected" },
            { "bytes",                  "bytes" },
            { "Completed",              "Completed" } ,
            { "Aborted",                "Aborted" },
            { "Save File As",           "Save File As" },
			{ "File exits",			    "\nalready exists.\n\nOverwrite it?\n" },
			{ "Yes", 					"Yes" },
			{ "No",  					"No" },
			// Stickies
			{ "Edit ...",				"Edit ..."},
			{ "Font Size",				"Font Size" },
			{ "Text Color",				"Text Color" },
			{ "Font",					"Font" },
			{ "Justification",			"Justification" },
			{ "Justify Center",			"Center" },
			{ "Justify Left",			"Left" },
			{ "Justify Right",			"Right" },
			{ "Font Style",				"Font Style" },
			{ "Underline",				"Underline" },
			{ "Clear Style",			"Clear Style" },
			{ "New Sticky Note ...", 	"New Sticky Note ..." },
			{ "Save All Sticky Notes",	"Save All Sticky Notes" },
			{ "Other Colors ...",		"Other Colors ..." },
			{ "Front All Sticky Notes",	"Front All Sticky Notes"},
			{ "Back All Sticky Notes",  "Back All Sticky Notes"},
			{ "Insert",					"Insert" },
			{ "Time Format 1",			"Current Time (hh:mm:ss)" },
			{ "Time Format 2",			"Current Time (hh:mm)" },
			{ "Image ...", 				"Image ..." },
			{ "Delete This Note",		"Delete This Note"}
        };

    }

// LOG
// 11-Feb-97    Y.Shibata   created
// 2.35 :  5-Oct-99	Y.Shbata	cleared old logs and added "Delete This Note"


