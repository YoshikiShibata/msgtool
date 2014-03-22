// File: MessageProtocol.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1996 - 1998, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import msgtool.MBPDataUnit;
import msgtool.db.AddressDB;
import msgtool.util.MemoryUtil;
import msgtool.util.NetUtil;
import msgtool.util.Queue;
import msgtool.util.StringDefs;

public final class MessageProtocol implements Runnable, MBPDispatcher {
	final public static int kSocketNo = 591117 & 0xffff; // my birthday
														 // 1959.11.17

	private ServerSocket fServerSocket = null;
	private AddressDB fAddressDB = null;
	private boolean fRunServer = true;
	private Queue<Socket> fClientSocketsQueue = null;
	private Queue<Message> fRcvMessagesQueue = null;
	private MessageProtocolListener fMessageProtocolListener = null;
	
	static private MessageProtocol fInstance = new MessageProtocol();

	static public MessageProtocol getInstance() {
		return (fInstance);
	}

	private MessageProtocol() {
		fAddressDB = AddressDB.instance();
		fClientSocketsQueue = new Queue<Socket>();
		fRcvMessagesQueue = new Queue<Message>();
		Thread thread = new ReadMessageThread();
		thread.setDaemon(true);
		thread.start();

		thread = new ProcessMessageThread();
		thread.setDaemon(true);
		thread.start();
	}

	public void addMessageProtocolListener(MessageProtocolListener listener) {
		fMessageProtocolListener = listener;
	}

	// ===============================================
	// SendMessage will send a message to a recipient.
	// ===============================================
	public String sendMessage(String senderName, String recipient,
			String message) {
		//
		// If recipient is "ALL", then broadcast this message
		//
		if (recipient.equals(StringDefs.ALL)) {
			if (broadcastMessage(senderName, message, false))
				return ("");
			else
				return (null);
		} else if (recipient.equals(StringDefs.ALL_AREAS)) {
			if (broadcastMessage(senderName, message, true))
				return ("");
			else
				return (null);
		}

		String address = fAddressDB.lookUpAddressCache(recipient);
		try {
			Socket msgSocket = null;
			String remoteIP = null;

			if (address == null)
				msgSocket = new Socket(recipient, kSocketNo);
			else
				msgSocket = new Socket(address, kSocketNo);

			remoteIP = NetUtil.getIPAddress(msgSocket.getInetAddress());
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(msgSocket.getOutputStream()));

			dos.writeUTF(senderName);
			dos.writeUTF(message);
			dos.flush();

			dos.close();
			msgSocket.close();
			return (remoteIP);
		} catch (IOException e) {
			return (null);
		}
	}

	// ==================================================
	// Send a message via MBP(Message Broadcast Protocol)
	// ==================================================
	private boolean broadcastMessage(String senderName, String message,
			boolean globalBroadcast) {
		return (MBPUtil.sendMessages(globalBroadcast,
				MBPClientTypeDef.kMessageProtocolMessage, senderName, message));
	}

	// ====================================================
	// MBP dispatcher
	// ====================================================
	public void dispatch(MBPDataUnit dataUnit) {
		if (dataUnit.getClientType() == MBPClientTypeDef.kMessageProtocolMessage) {
			try {
				ByteArrayInputStream is = new ByteArrayInputStream(dataUnit
						.getClientData());
				ObjectInputStream ois = new ObjectInputStream(is);
				String senderName = (String) ois.readObject();
				String message = (String) ois.readObject();
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

				fRcvMessagesQueue.put(new Message(senderIP, senderName,
						message, true));
			} catch (IOException e) {
			} catch (ClassNotFoundException e) {
				System.err.println(e.toString());
			}
		}
	}

	public void stopServer() {
		fRunServer = false;
	}

	public boolean initializeServerSocket() {
		return ((fServerSocket = ServerSocketUtil.createServerSocket(kSocketNo)) != null);
	}
	
	public InetAddress getServerAddress() {
		assert(fServerSocket != null);
		return fServerSocket.getInetAddress();
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

			// Check if the Server should stop. If so, close the clientSocket
			// immediately so that the sender detect an error and think that
			// this machine is not running.
			if (!fRunServer) {
				try {
					clientSocket.close();
				} catch (IOException e) {
				}
				return;
			}

			// Send the client socket to ReadMessageThread.
			fClientSocketsQueue.put(clientSocket);
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

	// ====================================
	// Thread to process a received message
	// ====================================
	private class ReadMessageThread extends Thread {

		public void run() {
			Socket clientSocket = null;

			while (true) {
				clientSocket = fClientSocketsQueue.get();
				try {
					DataInputStream is = new DataInputStream(
							new BufferedInputStream(clientSocket
									.getInputStream()));

					String senderName = is.readUTF();
					String message = is.readUTF();
					InetAddress senderAddress = clientSocket.getInetAddress();
					is.close();
					clientSocket.close();

					fRcvMessagesQueue.put(new Message(NetUtil
							.getIPAddress(senderAddress), senderName, message,
							false));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class ProcessMessageThread extends Thread {

		public void run() {
			while (true) {
				Message message = fRcvMessagesQueue.get();
				fMessageProtocolListener.onMessage(message);
				MemoryUtil.fullGC();
			}
		}
	}
}

// LOG
//  7-Sep-96 Y.Shibata created
//  5-Sep-96 Y.Shibata used a thread to process a received message
// 15-Dec-96 Y.Shibata modified for JDK 1.1
// 1.34 : 6-Jul-97 Y.Shibata added GetRemoteIPAddress();
// 1.43 : 5-Aug-97 Y.Shibata Don't print any error message when sending fails
// 1.48 : 21-Aug-97 Y.Shibata re-implemented InitializeServerSocket();
// 1.71 : 25-Oct-07 Y.Shibata renamed Protocol to MessageProtocol.
// 1.95 : 11-Jul-98 Y.Shibata modified to use msgtool.util.TimerUtil
//        12-Jul-98 Y.Shibata moved to msgtool.protocol
//        21-Jul-98 Y.Shibata added code to call GC.
// 3.50 : 27-Dec-03 Y.Shibata used Java Generics

