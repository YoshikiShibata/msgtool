// File: MessageResources.java - last edit:
// Yoshiki Shibata 18-Jan-04

// Copyright (c) 1997, 1998, 2004 by Yoshiki Shibata. All rights reserved.

package msgtool;

import java.util.ListResourceBundle; 

public final class MessageResources_ja extends ListResourceBundle { 
    public Object[][] getContents() {
        return contents;
        }
    
    static final Object[][] contents = {
	    { "Unregistered Msg",	"MessagingTool\u3054\u5229\u7528\u3042\u308a\u304c\u3068\u3046\u3054\u3056\u3044\u307e\u3059\u3002\n\n" +
					"\u3054\u5229\u7528\u306eMessagingTool\u306f\u3001\u5229\u7528\u767b\u9332\u3055\u308c\u3066\u304a\u308a\u307e\u305b\u3093\u3002" +
					"\u767b\u9332\u3059\u308b\u70ba\u306b\u306f\u3001\u96fb\u5b50\u30e1\u30fc\u30eb\u3092majordomo@mma.so-net.ne.jp\u5b9b\u3066\u306b\u3001" + 
					" \u672c\u6587\u306b\u4ee5\u4e0b\u306e\u30e1\u30c3\u30bb\u30fc\u30b8\u3092\u66f8\u3044\u3066\u304a\u9001\u308a\u4e0b\u3055\u3044\u3002" +
					"\u306a\u304a\u3001\u4ef6\u540d\u306f\u7a7a\u767d\u3067\u69cb\u3044\u307e\u305b\u3093\u3002\n\n"+ 
            				"    subscribe MessagingToolUsers"+
					"\n\n" +
					"\u4e0a\u8a18\u30e1\u30fc\u30eb\u306b\u3088\u308a\u3001\u4ee5\u4e0b\u306e\u30e1\u30fc\u30ea\u30f3\u30b0\u30ea\u30b9\u30c8\u306b\u767b\u9332\u3055\u308c\u307e\u3059\u3002\n" +
					"    MessagingTooUsers@mma.so-net.ne.jp\n" +
					"\u3053\u306e\u30e1\u30fc\u30ea\u30f3\u30b0\u30ea\u30b9\u30c8\u306f\u3001\u4eca\u5f8c\u306e\u30ea\u30ea\u30fc\u30b9\u60c5\u5831\u306e\u914d\u5e03\u304a\u3088\u3073\u30e6\u30fc\u30b6\u30fc"+
					"\u9593\u306e\u60c5\u5831\u4ea4\u63db\u306b\u4f7f\u7528\u3055\u308c\u307e\u3059\u3002\u306a\u304a\u3001\u767b\u9332\u756a\u53f7\u306e\u767a\u884c\u306f\u3001\u6570\u65e5\u3092\u8981\u3057\u307e\u3059\u3002"+
					"\n\n"},
	    { "InitializeServerError",  "\u3059\u3067\u306b\u52d5\u4f5c\u3057\u3066\u3044\u308b\u304b\u3001\u3082\u3057\u304f\u306f\u7d42\u4e86\u3057\u3088\u3046\u3068\u3057\u3066\u30cf\u30f3\u30b0\u3057\u3066\u3044\u308b\n" +
					"MessagingTool\u304c\u3042\u308a\u307e\u3059\u3002\u305d\u306eMessagingTool\u3092\u7d42\u4e86\u3055\u305b\u308b\u304b\n" +
					"\u3042\u308b\u3044\u306f\u5f37\u5236\u7d42\u4e86\u3055\u305b\u3066\u304b\u3089\u3001\u518d\u5ea6MessagingTool\u3092\u8d77\u52d5\u3057\u3066\u4e0b\u3055\u3044\u3002" },
	    { "Log Overflowed",         "\u30ed\u30b0\u8868\u793a\u30a6\u30a3\u30f3\u30c9\u30a6\u304c\u30aa\u30fc\u30d0\u30fc\u30d5\u30ed\u30fc\u3057\u305f\u306e\u3067\u3001\u30af\u30ea\u30a2\u30fc\u3057\u307e\u3057\u305f\u3002"},
	    { "Error",			"\u30a8\u30e9\u30fc"},
            { "Unregistered Copy",      "Unregistered Copy"},
	    { "Unregistered Copy",	"\u672a\u767b\u9332\u30b3\u30d4\u30fc"},
            { "To:" ,                   "\u9001\u4fe1\u5148:" },
	    { "Additional To:",		"\u8ffd\u52a0\u9001\u4fe1\u5148:"},
            { "Recorded Message:",      "\u4e0d\u5728\u30e1\u30c3\u30bb\u30fc\u30b8:" },
            { "File",                   "\u30d5\u30a1\u30a4\u30eb" },
	    { "Page Setup ...",		"\u30da\u30fc\u30b8\u8a2d\u5b9a ..." },
            { "Print..." ,              "\u5370\u5237 ..." },
	    { "Logged Messages",        "\u30e1\u30c3\u30bb\u30fc\u30b8\u30ed\u30b0"},
	    { "Logged Meeting Messages","\u4f1a\u8b70\u5ba4\u30ed\u30b0"},
            { "Quit",                   "\u7d42\u4e86" },
            { "Edit" ,                  "\u7de8\u96c6" },
            { "Address Cache ...",      "\u30a2\u30c9\u30ec\u30b9\u30ad\u30e3\u30c3\u30b7\u30e5 ..."},
            { "Recipient Hints ...",    "\u9001\u4fe1\u5148\u30dd\u30c3\u30d7\u30a2\u30c3\u30d7\u30d2\u30f3\u30c8 ..."  },
            { "Recorded Message Hints ..." , "\u4e0d\u5728\u30e1\u30c3\u30bb\u30fc\u30b8\u30fb\u30dd\u30c3\u30d7\u30a2\u30c3\u30d7\u30d2\u30f3\u30c8 ..." },
	    { "Meeting Room ..." ,	"\u81ea\u52d5\u53c2\u52a0\u4f1a\u8b70\u5ba4 ..." },
            { "Properties ...",         "\u30d7\u30ed\u30d1\u30c6\u30a3 ..." },
            { "Recipient",              "\u9001\u4fe1\u5148"},
            { "Recorded Message",       "\u4e0d\u5728\u30e1\u30c3\u30bb\u30fc\u30b8"},
            { "Window",                 "\u30a6\u30a3\u30f3\u30c9\u30a6" },
            { "Another ..." ,           "\u65b0\u898f\u5165\u529b\u30a6\u30a3\u30f3\u30c9\u30a6 ..." },
	    { "Meeting Room",           "\u4f1a\u8b70\u5ba4" },
	    { "OnLine List ...",	"\u30aa\u30f3\u30e9\u30a4\u30f3\u30ea\u30b9\u30c8 ..." },
	    { "Meeting Room List ...",  "\u4f1a\u8b70\u5ba4\u30ea\u30b9\u30c8 ..."}, 
            { "Clear Log",              "\u30ed\u30b0\u6d88\u53bb"},
	    { "Tool",                   "\u30c4\u30fc\u30eb"},
	    { "Send a file ...",        "\u30d5\u30a1\u30a4\u30eb\u9001\u4fe1 ..." },
	    { "Search ...",             "\u30e6\u30fc\u30b6\u691c\u7d22 ..."},
	    { "Join Meeting Room ...",   "\u4f1a\u8b70\u5ba4\u53c2\u52a0 ..."},
	    { "Delete Meeting Room ...","\u4f1a\u8b70\u5ba4\u524a\u9664 ..."},
            { "About",                  "\u60c5\u5831"},
            { "MessagingTool ...",      "MessagingTool ..." },
            { "System Properties ..." , "\u30b7\u30b9\u30c6\u30e0\u30d7\u30ed\u30d1\u30c6\u30a3 ..." },
            { "Help",                   "\u30d8\u30eb\u30d7" },
            { "Overview ..." ,          "\u6982\u8981 ..." },
            { "Topics ...",             "\u30c8\u30d4\u30c3\u30af\u30b9 ..." },
            { "Deliver",                "\u9001\u4fe1" },
            { "Not In Office",		"\u7559\u5b88" },
            { "About MessagingTool",    "MessagingTool\u60c5\u5831" },
            { "About System Properties","\u30b7\u30b9\u30c6\u30e0\u30d7\u30ed\u30d1\u30c6\u30a3\u60c5\u5831" },
            { "Edit: Address Cache" ,   "\u30a2\u30c9\u30ec\u30b9\u30ad\u30e3\u30c3\u30b7\u30e5\u7de8\u96c6"},
            { "Edit: Recipient Hints",  "\u9001\u4fe1\u5148\u30dd\u30c3\u30d7\u30a2\u30c3\u30d7\u30d2\u30f3\u30c8\u7de8\u96c6" },
            { "Edit: Recorded Message Hints", "\u4e0d\u5728\u30e1\u30c3\u30bb\u30fc\u30b8\u30fb\u30dd\u30c3\u30d7\u30a2\u30c3\u30d7\u30d2\u30f3\u30c8\u7de8\u96c6" },
	    { "Edit: Meeting Room",	"\u81ea\u52d5\u53c2\u52a0\u4f1a\u8b70\u5ba4\u7de8\u96c6" },
            { "MessagingTool Properties", "MessagingTool\u30d7\u30ed\u30d1\u30c6\u30a3"},
            { "(received)",             "(\u6210\u529f)" },
            { "(failed)",               "(\u5931\u6557)" },
            { "Recorded Message: ",     "\u7559\u5b88\u9332\u30e1\u30c3\u30bb\u30fc\u30b8: " },
            { "I'm not in my office" ,  "\u4e0d\u5728\u3067\u3059" },
            { "MessagingTool Notice" ,  "\u901a\u77e5" },
	    { "No Hint Available", 	"\u30d2\u30f3\u30c8\u306f\u8a2d\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093" },
            // Properties window 
            { "User Name:",             "\u30e6\u30fc\u30b6\u540d" } ,
            { "Activate On Reception:", "\u53d7\u4fe1\u901a\u77e5" } ,
            { "Messaging Dialog",   	"\u5c02\u7528\u30e1\u30c3\u30bb\u30fc\u30b8\u30c0\u30a4\u30a2\u30ed\u30b0" },
            { "Reception Dialog",       "\u53d7\u4fe1\u901a\u77e5\u30c0\u30a4\u30a2\u30ed\u30b0" },
	    { "Beep On Reception:",     "\u53d7\u4fe1\u901a\u77e5\u97f3"},
            { "Deliver Key:",           "\u9001\u4fe1\uff77\uff70\u5272\u5f53"},
	    { "Log Messages:",          "\u30ed\u30b0\u3092\u6b8b\u3059"},
            { "Max Log files:",         "\u30ed\u30b0\u30d5\u30a1\u30a4\u30eb\u6570"},
            { "Font Name:",             "\u30d5\u30a9\u30f3\u30c8\u540d"  },
            { "Font Style:",            "\u30d5\u30a9\u30f3\u30c8\u30b9\u30bf\u30a4\u30eb"  },
            { "Plain",                  "\u6a19\u6e96" },
            { "Bold",                   "\u30dc\u30fc\u30eb\u30c9" },
            { "Italic",                 "\u30a4\u30bf\u30ea\u30c3\u30af" },
            { "Bold Italic",            "\u30dc\u30fc\u30eb\u30c9\u30fb\u30a4\u30bf\u30ea\u30c3\u30af" },
            { "Font Size:" ,            "\u30d5\u30a9\u30f3\u30c8\u30b5\u30a4\u30ba"  },
	    { "Text Background:",       "\u6587\u5b57\u80cc\u666f\u8272" },
	    { "Look & Feel:",           "\u30eb\u30c3\u30af & \u30d5\u30a3\u30fc\u30eb" },
            { "Cancel" ,                "\u53d6\u6d88" },
            { "Set",                    "\u5b8c\u4e86"},
            // Edit Window
            { "Add",                    "\u767b\u9332" },
            { "Delete" ,                "\u524a\u9664" },
	    { "Update", 		"\u4fee\u6b63" },
            { "Recipient:",             "\u9001\u4fe1\u5148:"},
	    { "Members:",		"\u30e1\u30f3\u30d0\u30fc:"},
            { "Message:",               "\u30e1\u30c3\u30bb\u30fc\u30b8:"},
            { "Name:",                  "\u540d\u524d:" },
	    { "Sort Key:",		"\u30d5\u30ea\u30ac\u30ca:" },
            { "Address:",               "\u30a2\u30c9\u30ec\u30b9:" },
	    { "List as hint:",		"\u30d2\u30f3\u30c8\uff84\u8868\u793a:"},
	    // Message Received Dialog
	    { "New message arrived",	"\u30e1\u30c3\u30bb\u30fc\u30b8\u3092\u53d7\u4fe1\u3057\u307e\u3057\u305f" },
	    // Sending Window
	    { "Sending Window",		"\u5165\u529b\u30a6\u30a3\u30f3\u30c9\u30a6" },
	    // Register Window
            { "Register",               "\u767b\u9332"},
            { "E-Mail Address:",        "\u96fb\u5b50\u30e1\u30fc\u30eb\u30a2\u30c9\u30ec\u30b9:" },
            { "Registered Number:",     "\u767b\u9332\u756a\u53f7:" },
	    // Window title
	    { "Message Waiting",	"\u30e1\u30c3\u30bb\u30fc\u30b8\u6709\u308a"},
	    // Quit Dialog
	    { "Quit: Are you sure?",  "\u7d42\u4e86\u3057\u307e\u3059\u304b\uff1f" },
	    // OnLine List
	    { "OnLine", 		"\u30aa\u30f3\u30e9\u30a4\u30f3" },
	    { "List",                   "\u30ea\u30b9\u30c8" },
            { "Copy into To:",          "\u9001\u4fe1\u5148\u3078\u30b3\u30d4\u30fc"},
            { "Open Messaging Dialog",  "\u5c02\u7528\u30e1\u30c3\u30bb\u30fc\u30b8\u30f3\u30b0\u30c0\u30a4\u30a2\u30ed\u30b0\u3092\u958b\u304f"},
	    { "Select All",		"\u3059\u3079\u3066\u9078\u629e"},
            // --- Message Log ----
            { "Log:",                   "\u30ed\u30b0:"},
	    { "Search",                 "\u691c\u7d22"},
	    // --- Meeting Room ----
	    { "Join Meeting Room",       "\u4f1a\u8b70\u5ba4\u53c2\u52a0"},
	    { "Delete Meeting Room",    "\u4f1a\u8b70\u5ba4\u524a\u9664" },
            { "Room Name:",             "\u4f1a\u8b70\u5ba4\u540d:"},
	    { "Closed Room:",           "\u975e\u516c\u958b:"},
            { "Join",                   "\u5165\u5ba4"},
	    { "Participants",           "\u53c2\u52a0\u8005\u4e00\u89a7"},
            { "Leave",             	"\u9000\u5ba4"},
	    { "Joined",                 "\u5165\u5ba4"},
            { "Left",                   "\u9000\u5ba4"},
	    { "No room available",      "\u4f1a\u8b70\u5ba4\u306a\u3057"}, 
	    { "CLOSED ",		"\u975e\u516c\u958b "},
	    { "Meeting Rooms",          "\u4f1a\u8b70\u5ba4"},
            { "Message Too Long",       "\uff92\uff6f\uff7e\uff70\uff7c\uff9e\u304c\u9577\u3059\u304e\u307e\u3059\u3002\u77ed\u304f\u3057\u3066\u4e0b\u3055\u3044\u3002"},
	    { "Ok",			"\u4e86\u89e3"},
	    { "Fetch log",		"\u30ed\u30b0\u53d6\u51fa\u3057:" },
	    // --- Popup menu ---
            { "Cut",                    "\u5207\u308a\u53d6\u308a" },
            { "Copy",                   "\u30b3\u30d4\u30fc" },
            { "Paste",                  "\u8cbc\u308a\u4ed8\u3051" },
	    { "Clear All", 		"\u3059\u3079\u3066\u524a\u9664"},
	    // --- Colors ----
            { "White",                  "\u767d\u8272" },
            { "Light Gray",             "\u8584\u7070\u8272" },
            { "Gray",                   "\u7070\u8272" },
            { "Dark Gray",              "\u6fc3\u7070\u8272" },
            { "Black",                  "\u9ed2\u8272" },
            { "Red",                    "\u8d64\u8272" },
            { "Yellow",                 "\u9ec4\u8272" },
            { "Orange",                 "\u30aa\u30ec\u30f3\u30b8\u8272" },
            { "Pink",                   "\u30d4\u30f3\u30b0\u8272" },
            { "Magenta",                "\u6df1\u7d05\u8272" },
            { "Cyan",                   "\u30b7\u30a2\u30f3\u8272" },
            { "Green",                  "\u7dd1\u8272" },
            { "Blue",                   "\u9752\u8272" },
            { "System Text",            "\u30b7\u30b9\u30c6\u30e0\u6587\u5b57\u80cc\u666f\u8272" },
	    { "System Window", 		"\u30b7\u30b9\u30c6\u30e0\u30fb\u30a6\u30a3\u30f3\u30c9\u30a6\u80cc\u666f\u8272"},
	    { "Desktop Color", 		"\u30c7\u30b9\u30af\u30c8\u30c3\u30d7\u8272"},
            // Properties Dialog tabs
            { "User",                   "\u30e6\u30fc\u30b6\u60c5\u5831" },
            { "Reception",              "\u53d7\u4fe1\u901a\u77e5" },
            { "Function Key",           "\u30d5\u30a1\u30f3\u30af\u30b7\u30e7\u30f3\u30ad\u30fc" },
            { "Logging",                "\u30ed\u30b0" },
            { "Preferences",            "\u305d\u306e\u4ed6" },
	    // Broadcast Message
            { "Broadcast Message",      "\u3010\u540c\u5831\u30e1\u30c3\u30bb\u30fc\u30b8\u3011" },
	    // File Send Dialog
            { "Send a file",            "\u30d5\u30a1\u30a4\u30eb\u9001\u4fe1" },
	    { "File Name:", 		"\u30d5\u30a1\u30a4\u30eb\u540d" },
            { "Files ...",              "\u30d5\u30a1\u30a4\u30eb\u9078\u629e ..."},
	    { "Send", 			"\u9001\u4fe1" },
	    { "File is not found",      "\u30d5\u30a1\u30a4\u30eb\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093"},
	    { "File cannot be read", 	"\u30d5\u30a1\u30a4\u30eb\u304c\u8aad\u3081\u307e\u305b\u3093" },
	    { "Please specify a file",  "\u30d5\u30a1\u30a4\u30eb\u3092\u6307\u5b9a\u3057\u3066\u304f\u3060\u3055\u3044"},
   	    { "No recipient is specified", "\u9001\u4fe1\u5148\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093"},
            { "Please specify a file",  "\u30d5\u30a1\u30a4\u30eb\u3092\u6307\u5b9a\u3057\u3066\u304f\u3060\u3055\u3044"},
            { "File Transfer",          "\u30d5\u30a1\u30a4\u30eb\u8ee2\u9001" },
            { "Recipient Name:",        "\u53d7\u4fe1\u8005:"},
            { "Sender Name:",           "\u9001\u4fe1\u8005:"},
            { "Progress:",              "\u30d5\u30a1\u30a4\u30eb\u8ee2\u9001:" },
            { "Connecting ...",         "\u63a5\u7d9a\u4e2d ..."}, 
            { "Connected",              "\u63a5\u7d9a\u3055\u308c\u307e\u3057\u305f" },
            { "bytes",                  "\u30d0\u30a4\u30c8" },
            { "Completed",              "\u5b8c\u4e86" },
            { "Aborted",                "\u4e2d\u65ad" },
            { "Save File As",           "\u30d5\u30a1\u30a4\u30eb\u4fdd\u5b58" },
	    { "File exits",		"\n\u3053\u306e\u30d5\u30a1\u30a4\u30eb\u306f\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059\u3002\n\n\u4e0a\u66f8\u304d\u3057\u307e\u3059\u304b\uff1f\n"},
	    { "Yes", 			"\u306f\u3044" },
	    { "No",  			"\u3044\u3044\u3048" },
	    // Stickies
	    { "Edit ...",				"\u7de8\u96c6 ..."},
	    { "Font Size",		"\u30d5\u30a9\u30f3\u30c8\u30b5\u30a4\u30ba" },
	    { "Text Color",		"\u6587\u5b57\u8272" },
	    { "Font",			"\u30d5\u30a9\u30f3\u30c8" },
	    { "Justification",		"\u914d\u7f6e" },
	    { "Justify Center",		"\u4e2d\u592e" },
	    { "Justify Left",		"\u5de6" },
	    { "Justify Right",		"\u53f3" },
	    { "Font Style",		"\u30d5\u30a9\u30f3\u30c8\u30b9\u30bf\u30a4\u30eb" },
	    { "Underline",		"\u4e0b\u7dda" },
	    { "Clear Style",		"\u30b9\u30bf\u30a4\u30eb\u53d6\u6d88"},
	    { "New Sticky Note ...", 	"\u65b0\u898f\u30b9\u30c6\u30a3\u30c3\u30ad\u30ce\u30fc\u30c8\u4f5c\u6210 ..." },
	    { "Save All Sticky Notes",	"\u30b9\u30c6\u30a3\u30c3\u30ad\u30ce\u30fc\u30c8\u3092\u3059\u3079\u3066\u4fdd\u5b58" },
	    { "Other Colors ...",	"\u4ed6\u306e\u8272 ..." },
	    { "Front All Sticky Notes",	"\u30b9\u30c6\u30a3\u30c3\u30ad\u30ce\u30fc\u30c8\u3092\u3059\u3079\u3066\u524d\u9762\u3078"},
	    { "Back All Sticky Notes",	"\u30b9\u30c6\u30a3\u30c3\u30ad\u30ce\u30fc\u30c8\u3092\u3059\u3079\u3066\u5f8c\u308d\u3078"},
	    { "Insert",			"\u633f\u5165" },
	    { "Time Format 1",		"\u73fe\u5728\u6642\u523b (hh:mm:ss)" },
	    { "Time Format 2",		"\u73fe\u5728\u6642\u523b (hh:mm)" },
	    { "Image ...", 		"\u30a4\u30e1\u30fc\u30b8 ..." },
	    { "Delete This Note",	"\u30b9\u30c6\u30a3\u30c3\u30ad\u30ce\u30fc\u30c8\u3092\u524a\u9664"}
        };

    }

// LOG
// 11-Feb-97    Y.Shibata   	created
// --- 1.02 ---
// 22-Feb-97	Y.Shibata	added "No Hint Available"
//				added "New message arrived"
// --- 1.30 ---
// 17-Apr-97	Y.Shibata	deleted TimeZone
// --- 1.33 ---
// 30-Jun-97	Y.Shibata	added Message Waiting
// --- 1.34 ---
//  6-Jul-97	Y.Shibata	added Quit Dialog message.
// --- 1.41 ---
//  4-Aug-97	Y.Shibata	added OnLine List
// --- 1.46 ---
//  9-Aug-97	Y.Shibata	added additionals for OnLine List
// --- 1.52 ---
//  2-Sep-97	Y.Shibata	added "Select All"
// --- 1.53 ---
//  3-Sep-97	Y.Shibata	added "Sort Key:"
// --- 1.63 ---
// 20-Sep-97	Y.Shibata	added "To: ", "Unregistered Copy"
// --- 1.65 ---
// 27-Sep-97	Y.Shibata	added messages for Search Dialog.
// 3.51: 18-Jan-04 Y.Shibata	added "Back All Notes"


