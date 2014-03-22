// File: MsgSend.java - last edit:
// Yoshiki Shibata  17-Oct-2004

// Copyright (c) 1998, 2004 by Yoshiki Shibata
// All rights reserved.

import msgtool.db.PropertiesDB;
import msgtool.db.RecipientHintsDB;
import msgtool.protocol.MeetingProtocol;
import msgtool.protocol.MiscProtocol;
import msgtool.util.StringDefs;
import msgtool.util.StringUtil;

public class MsgSend {

	public static void main(String[] args) {
		System.out.println("Message Sender (1.10) for MessagingTool");
		System.out.println("Copyright (c) 1998, 2004 by Yoshiki Shibata." + 
						   " All rights reserved\n");

		// Make sure there are only two arguments: recipients and message
		if (args.length != 2 && args.length != 3) {
            showUsage();
		}
        
        if (args[0].equals("-m")) {
            if (args.length != 3) 
                showUsage();
            
            String msg = args[2];
            if (msg.charAt(msg.length()-1) != '\n')
                msg = msg + '\n';
            
            if (MeetingProtocol.getInstance().message(
                    args[1],
                    PropertiesDB.getInstance().getUserName(),
                    msg)) {
            	System.out.println("message is delivered to " + args[1]);
                System.exit(0);
            } else {
                System.out.println("message is too long");
                System.exit(1);
            }
        }
        
        if (args.length != 2) 
            showUsage();
        
        // Check the message length    
        if (args[1].length() == 0) {
            System.out.println("Null message cannot be sent");
            System.exit(1);
        }
		
		String[] recipientsList = parseRecipients(args[0]);
		String message = createMessage(args[1], recipientsList);
		sendMessage(recipientsList, message);
	}

    private static void showUsage() {
        System.out.println("Usage: MsgSend recipients message");
        System.out.println("       MsgSend -m room message");
        System.exit(1);
    }

    private static void sendMessage(String[] recipientsList, String message) {
		// Now start sending the message
		MiscProtocol miscProtocol = MiscProtocol.getInstance();
		String senderName = PropertiesDB.getInstance().getUserName();
		for (String recipient : recipientsList) {
			System.out.print(recipient + " ... ");

			// ALL and ALL-AREAS are not supported yet.
			if (recipient.equals(StringDefs.ALL)
					|| recipient.equals(StringDefs.ALL_AREAS)) {
				System.out.println("Not Supported");
				continue;
			}

			if (miscProtocol.commandLineMessage(senderName, recipient, message) != null)
				System.out.println(StringDefs.RECEIVED);
			else
				System.out.println(StringDefs.FAILED);
		}
	}

	private static String createMessage(String text, String[] recipientsList) {
		if (recipientsList.length == 1) {
			return text;
		}

        int noOfRecipients = recipientsList.length;
		String headingMsg = StringDefs.TO_C + " ";

		for (String recipient : recipientsList) {
			headingMsg = headingMsg + recipient;
			if (--noOfRecipients > 0)
				headingMsg = headingMsg + ", ";
		}
		return headingMsg + "\n" + text;
	}

	private static String[] parseRecipients(String args) {
		// Parse the recipients and check recipients
		String[] recipients = StringUtil.parseToArray(args);
		RecipientHintsDB groupDB = RecipientHintsDB.getInstance();
		StringBuilder newRecipients = new StringBuilder();
        for (String recipient: recipients) {
                newRecipients.append(groupDB.getExpandedRecipients(recipient));
                newRecipients.append(",");  
        }
        
		recipients = StringUtil.parseToArray(newRecipients.toString());
		if (recipients.length == 0) {
			System.out.printf("Please specify at least one recipients");
			System.exit(1);
		}
		
		return recipients;
	}
}

// LOG
// 2.00 :  5-Aug-98 Y.Shibata created
// 2.52 : 14-Oct-04 Y.Shibata refactored and -m option implemented
