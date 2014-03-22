// File: MiscProtocol.java - last edit:
// Yoshiki Shibata 22-Aug-2004

// Copyright (c) 1996 - 1999, 2003, 2004 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import msgtool.MBPDataUnit;
import msgtool.db.AddressDB;
import msgtool.util.MemoryUtil;
import msgtool.util.NetUtil;
import msgtool.util.Queue;

public final class MiscProtocol implements Runnable, MBPDispatcher {
	final public static int kSocketNo = MessageProtocol.kSocketNo + 1;
	
	enum Command {
		PROBE("Probe"), 
		OFF_LINE("OffLine"), 
		UPDATE_IP_ADDRESS("UpdateIPAddress"),
		NOT_IN_OFFICE("NotInOffice"),
		IN_OFFICE("InOffice"),
		LOOK_FOR_USER("LookForUser"),
		USER_MATCHED("UserMatched"),
		LOG_REQUEST("LogRequest"),
		REQUESTED_LOG("RequestedLog"),
		COMMAND_LINE_MESSAGE("CommandLineMessage"),
		FTP("FTP");
		
		Command(String commandName) {
			this.commandName = commandName;
		}
		
		private final String commandName;
		public String commandName() {
			return commandName;
		}
	}
	
	final private static String kProbeCmd = "Probe";
	final private static String kOffLineCmd = "OffLine";
	final private static String kUpdateIPAddressCmd = "UpdateIPAddress";
	final private static String kNotInOfficeCmd = "NotInOffice";
	final private static String kInOfficeCmd = "InOffice";
	final private static String kLookForUser = "LookForUser";
	final private static String kUserMatched = "UserMatched";
	final private static String kLogRequest = "LogRequest";
	final private static String kRequestedLog = "RequestedLog";
	final private static String kCommandLineMessage = "CommandLineMessage";
	final private static String kFTPCmd = "FTP";

	private ServerSocket fServerSocket = null;
	private AddressDB fAddressDB = null;
	private boolean fRunServer = true;
	private Queue<ReceivedMessage> fReceiveQueue = null;
	private MiscProtocolListener fMiscProtocolListener = null;
	private FTPListener fFTPListener = null;

	static private MiscProtocol fInstance = new MiscProtocol();

	static public MiscProtocol getInstance() {
		return (fInstance);
	}

	private MiscProtocol() {
		fAddressDB = AddressDB.instance();
		fReceiveQueue = new Queue<ReceivedMessage>();
		Thread thread = new ProcessRcvMsgThread();
		thread.setDaemon(true);
		thread.start();
	}

	public String probe(String senderName, String recipient) {
		return (sendMessage(senderName, recipient, kProbeCmd, ""));
	}

	public String offLine(String senderName, String recipient) {
		return (sendMessage(senderName, recipient, kOffLineCmd, ""));
	}

	public String notifyNewAddress(String senderName, String recipient,
			String oldIPAddress) {
		return (sendMessage(senderName, recipient, kUpdateIPAddressCmd,
				oldIPAddress));
	}

	public String notInOffice(String senderName, String recipient) {
		return (sendMessage(senderName, recipient, kNotInOfficeCmd, ""));
	}

	public String inOffice(String senderName, String recipient) {
		return (sendMessage(senderName, recipient, kInOfficeCmd, ""));
	}

	public boolean lookForUser(String senderName, String userName) {
		return (broadcastMessage(senderName, kLookForUser, userName, true));
	}

	public String userMatched(String senderName, String recipient) {
		return (sendMessage(senderName, recipient, kUserMatched, ""));
	}

	public String logRequest(String internalRoomName, String recipient) {
		return (sendMessage(internalRoomName, recipient, kLogRequest, ""));
	}

	public String requestedLog(String internalRoomName, String recipient,
			String log) {
		return (sendMessage(internalRoomName, recipient, kRequestedLog, log));
	}

	public String commandLineMessage(String senderName, String recipient,
			String message) {
		return (sendMessage(senderName, recipient, kCommandLineMessage, message));
	}

	public String ftp(String senderName, String recipient, String fileName,
			String fullpathName, long fileLength, int socketNo) {
		String message = "" + socketNo + ":" + fileLength + ":"
				+ fileName.length() + ":" + fileName + fullpathName;
		return (sendMessage(senderName, recipient, kFTPCmd, message));
	}

	public void addMiscProtocolListener(MiscProtocolListener listener) {
		fMiscProtocolListener = listener;
	}

	public void addFTPListener(FTPListener listener) {
		fFTPListener = listener;
	}

	public void stopServer() {
		fRunServer = false;
	}

	// ===============================================
	// SendMessage will send a message to a recipient.
	// ===============================================
	/*
	private String sendMessage(String senderName, String recipient,
			Command command, String parameter) {
		return sendMessage(senderName, recipient, command.commandName(), parameter);
	}
	*/
	
	private String sendMessage(String senderName, String recipient,
			String command, String parameter) {
		String address = fAddressDB.lookUpAddressCache(recipient);
		String remoteIPAddress = null;

		try {
			Socket msgSocket = (address == null) ?
									new Socket(recipient, kSocketNo): 
									new Socket(address, kSocketNo);

			remoteIPAddress = NetUtil.getIPAddress(msgSocket.getInetAddress());
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(msgSocket.getOutputStream()));

			dos.writeUTF(senderName);
			dos.writeUTF(command);
			dos.writeUTF(parameter);
			dos.flush();

			dos.close();
			msgSocket.close();
		} catch (IOException e) {
		}

		return (remoteIPAddress);
	}

	// ==================================================
	// Send a message via MBP(Message Broadcast Protocol)
	// ==================================================
	private boolean broadcastMessage(String senderName, String command,
			String parameter, boolean globalBroadcast) {
		return (MBPUtil.sendMessages(globalBroadcast,
				MBPClientTypeDef.kMiscProtocolMessage, senderName, command,
				parameter));
	}

	// ========================================
	// MBP dispatcher
	// ========================================
	public void dispatch(MBPDataUnit dataUnit) {
		if (dataUnit.getClientType() == MBPClientTypeDef.kMiscProtocolMessage) {
			try {
				ByteArrayInputStream is = new ByteArrayInputStream(dataUnit
						.getClientData());
				ObjectInputStream ois = new ObjectInputStream(is);
				String senderName = (String) ois.readObject();
				String command = (String) ois.readObject();
				String parameter = (String) ois.readObject();
				//
				// Please note that senderIP is not the SourceAddress but
				// the OriginatorAddress. Because this message might be repeated
				// by other
				// MessagingTool and the SourceAddress might be the address of
				// the repeater.
				//
				String senderIP = dataUnit.getOriginatorAddress();
				ois.close();
				is.close();

				processReceivedCommand(new ReceivedMessage(senderName, command,
						parameter, senderIP));
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
				System.err.println(e.toString());
			}
		}

	}

	// =====================
	// Server implementation
	// =====================
	public boolean initializeServerSocket() {
		fServerSocket = ServerSocketUtil.createServerSocket(kSocketNo);
		return fServerSocket != null; 
	}

	public void run() {
		if (fServerSocket == null)
			return;

		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = fServerSocket.accept();
			} catch (IOException e) {
				System.out.println("Accept failed: " + kSocketNo + ", " + e);
				System.exit(1);
			}
			//
			// Check if the Server should stop. If so, close the clientSocket
			// immediately so that the sender detect an error and think that
			// this machine is not running.
			//
			if (!fRunServer) {
				try {
					clientSocket.close();
				} catch (IOException e) {
				}
				return;
			}

			try {
				DataInputStream is = new DataInputStream(
						new BufferedInputStream(clientSocket.getInputStream()));

				String senderName = is.readUTF();
				String command = is.readUTF();
				String parameter = is.readUTF();
				String senderIP = NetUtil.getIPAddress(clientSocket
						.getInetAddress());
				is.close();
				clientSocket.close();

				ReceivedMessage rcvMsg = new ReceivedMessage(senderName,
						command, parameter, senderIP);
				fReceiveQueue.put(rcvMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void finalize() {
		if (fServerSocket != null) {
			try {
				fServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fServerSocket = null;
		}
	}

	// ===============================
	// Process Received Message thread
	// ===============================
	class ReceivedMessage {
		public final String senderName;
		public final String command;
		public final String parameter;
		public final String senderIP;

		public ReceivedMessage(String senderName, String command,
				String parameter, String senderIP) {
			this.senderName = senderName;
			this.command = command;
			this.parameter = parameter;
			this.senderIP = senderIP;
		}
	}

	class ProcessRcvMsgThread extends Thread {

		public void run() {
			while (true) {
				ReceivedMessage rcvMsg = fReceiveQueue.get();
				processReceivedCommand(rcvMsg);
				rcvMsg = null; // GC
				MemoryUtil.fullGC(); // [V1.95]
			}
		}
	}

	public void processReceivedCommand(ReceivedMessage rcvMsg) {
		String command = rcvMsg.command;

		if (command.equals(kProbeCmd)) {
			fMiscProtocolListener.onProbe(rcvMsg.senderIP);
		} else if (command.equals(kOffLineCmd)) {
			fMiscProtocolListener.onOffLine(rcvMsg.senderIP);
		} else if (command.equals(kUpdateIPAddressCmd)) {
			//
			// senderIP is the new IP address,
			// parameter is the old IP address
			//
			fMiscProtocolListener.onReplaceIPAddress(rcvMsg.parameter,
					rcvMsg.senderIP);
		} else if (command.equals(kNotInOfficeCmd))
			fMiscProtocolListener.onNotInOffice(rcvMsg.senderIP);
		else if (command.equals(kInOfficeCmd))
			fMiscProtocolListener.onInOffice(rcvMsg.senderIP);
		else if (command.equals(kLookForUser))
			fMiscProtocolListener.onLookForUser(rcvMsg.senderIP,
					rcvMsg.parameter);
		else if (command.equals(kUserMatched))
			fMiscProtocolListener.onUserMatched(rcvMsg.senderIP,
					rcvMsg.senderName);
		else if (command.equals(kLogRequest))
			fMiscProtocolListener.onLogRequest(rcvMsg.senderIP,
					rcvMsg.senderName);
		else if (command.equals(kRequestedLog))
			fMiscProtocolListener.onRequestedLog(rcvMsg.senderIP,
					rcvMsg.senderName, rcvMsg.parameter);
		else if (command.equals(kCommandLineMessage))
			fMiscProtocolListener.onCommandLineMessage(rcvMsg.senderIP,
					rcvMsg.senderName, rcvMsg.parameter);
		else if (command.equals(kFTPCmd)) {
			//
			// parse the parameter
			//
			StringBuilder sb = new StringBuilder();
			int index = 0;
			char oneChar;

			long[] parsedLongs = new long[3];

			for (int i = 0; i < 3; i++) {
				oneChar = rcvMsg.parameter.charAt(index++);
				while (oneChar != ':') {
					sb.append(oneChar);
					oneChar = rcvMsg.parameter.charAt(index++);
				}
				parsedLongs[i] = 0;

				try {
					parsedLongs[i] = Long.parseLong(sb.toString());
				} catch (NumberFormatException e) {
					System.out.println(e);
					return;
				}
				sb.setLength(0);
			}
			//
			// get the file name
			//
			for (int i = 0; i < (int) parsedLongs[2]; i++)
				sb.append(rcvMsg.parameter.charAt(index++));
			String fileName = sb.toString();
			//
			// get the fullpath
			//
			String fullpath = rcvMsg.parameter.substring(index);

			fFTPListener.onFTP(rcvMsg.senderIP, rcvMsg.senderName, fileName,
					fullpath, (int) parsedLongs[0], parsedLongs[1]);

		}
	}

}

// LOG
//         6-Jul-97 Y.Shibata created
// 1.41 :  4-Aug-97 Y.Shibata added Probe for On-line list.
// 1.43 :  5-Aug-97 Y.Shibata Don't print error message when sending messages
// fails
// 1.47 : 21-Aug-97 Y.Shibata re-implemented InitializeServerSocket().
// 1.95 : 12-Jul-98 Y.Shibata moved to msgtool.protocol
//        21-Jul-98 Y.Shibata added code to call GC.
// 2.00 :  5-Aug-98 Y.Shibata added CommandLineMessage.
// 2.10 : 11-Oct-98 Y.Shibata added FTP
// 2.36 : 23-Nov-99 Y.Shibata used FTPListener interface.
// 3.50 : 27-Dec-03 Y.Shibata used Java Generics
// 3.52 : 22-Aug-04	Y.Shibata cleaned up.

