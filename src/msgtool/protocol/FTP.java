// File: FTP.java - last edited:
// Yoshiki Shibata 30-Dec-2002

// Copyright (c) 1998, 2002 by Yoshiki Shibata

package msgtool.protocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import msgtool.db.AddressDB;
import msgtool.util.NetUtil;

//
// This FTP class supports only File Retrieval. 
//
public class FTP implements Runnable {
    //
    // Singleton pattern
    //
    static private FTP gInstance = new FTP();
    static public FTP getInstance() { return (gInstance); }
    
    //
    // private class members
    //
    static private ServerSocket         gServerSocket   = null;
    static private AddressDB            gAddressDB      = AddressDB.instance();
    static private final int            BUFFER_SIZE     = 3096;
    static private FTPListenerFactory   gListenerFactory        = null; 
    //
    // private constructor
    //
    private FTP() {}
    
    // ===================================
    // Interface Implementations
    // ===================================
    
    //
    // Runnable implementation
    // Create a server socket and wait for FTP requests
    // 
    public void run() {
        //
        // Create a ServerSocket with any free port.
        // Passing 0 to the ServerSocket constructor means any free port.
        //
        try {
            gServerSocket = new ServerSocket(0);
            }
        catch (IOException e) {
            System.out.println("Could not create a socket for FTP: " + e);
            gServerSocket = null;
            return;
            } 
        //
        // Wait for any connection request and invoke a File Send Thread
        //
        while (true) {
            Socket  clientSocket = null;
            
            try {
                clientSocket = gServerSocket.accept();
                }
            catch (IOException e) {
                System.out.println("Accept failed: " + e);
                System.exit(1);
                }
            FTPProgressListener listener = null;
            if (gListenerFactory != null)
                listener = gListenerFactory.createProgressListener(FTPListenerFactory.SEND);
            Thread  fileSendThread = new FileSendThread(clientSocket, listener);
            fileSendThread.setDaemon(true);
            fileSendThread.start();
            } 
    
        }
    // ===================================
    // Public methods for client
    // ===================================
    //
    // setFTPListenerFactory()
    //
    static public void setFTPListenerFactory(FTPListenerFactory    factory) {
        gListenerFactory = factory;
        }
    //
    // getSocketNo() returns the number of socket for receiving
    //
    static public int getSocketNo() {
        assert gServerSocket != null : "ServerSocket is not initialized";
        
        return(gServerSocket.getLocalPort());
        }
     
    //
    // retrieve() sends information on a file to be retrieved to sourceHost.
    // If the file exists, then the contents of the file will be retrieved.
    //
    public boolean retrieve(
        String              sourceHost,  // host address from where a file is retrieved.
        String              fullpathName,// full path name of the file on the sourceHose
        String              fileName,   //  file name
        FileOutputStream    fileOut,     // output file stream
        int                 socketNo,
        long                fileLength)    // socketNo to which a connection will be established.
        {

        assert sourceHost != null : "sourceHost is null";
        assert sourceHost.length() != 0 : "sourceHost is zero length";
        assert fullpathName != null : "fullpathName is null";
        assert fullpathName.length() != 0 : "fullpathName is zero length";
        assert socketNo > 0 : "socketNo is negative";

        FTPProgressListener listener = null;
        
        if (gListenerFactory != null) {
            listener = gListenerFactory.createProgressListener(FTPListenerFactory.RECEIVE);
            if (listener != null) {
                listener.setPeerName(sourceHost);
                listener.setFileName(fileName);
            }
        }
        //
        // Connect the host and send the fullpathName
        //
        String  address = gAddressDB.lookUpAddressCache(sourceHost);
        Socket  socket = null;
        
        if (listener != null)
            listener.onConnecting();
        try {
            if (address == null)
                socket = new Socket(sourceHost, socketNo);
            else
                socket = new Socket(address, socketNo);
            
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
                                            socket.getOutputStream()));
            dos.writeUTF(fullpathName);
            dos.flush();
        } catch (IOException e) {   
            return (false);
        }
        if (listener != null)
            listener.onConnected();
        //
        // Now start to receiving the contents 
        long totalOfTransferedBytes = 0;
        try {
            InputStream inStream = socket.getInputStream();
            byte[]  buffer = new byte[BUFFER_SIZE];
            int noOfBytes = 0;
            
            while(true) {
                if (listener != null) {
                    //
                    // onBeingTransfered() returns false if the user
                    // want to abort this transfer.
                    //
                    if (!listener.onBeingTransfered(totalOfTransferedBytes)) {
                        socket.close();
                        listener.onCanceled();
                        return(false);
                    }
                }
                noOfBytes = inStream.read(buffer);
                if (noOfBytes == -1)
                    break;
                if (noOfBytes > 0) {
                    fileOut.write(buffer, 0, noOfBytes);
                    totalOfTransferedBytes += noOfBytes;
                }
            }
            socket.close();
        } catch (IOException e) {
            if (listener != null)
                listener.onCanceled();
            return (false);
        }
        if (totalOfTransferedBytes == fileLength) {
            if (listener != null)
                listener.onCompleted(totalOfTransferedBytes);
            return(true);
        } else {
            if (listener != null) 
                listener.onCanceled();
            return(false);
        }
    }  
    // ===================================
    // Private methods
    // ===================================
    
    // ===================================
    // Private inner classes
    // ===================================
    
    //
    // File Send Thread
    //
    private class FileSendThread extends Thread {
        private Socket              fSocket     = null;
        private FTPProgressListener fListener   = null;
        
        public FileSendThread(
            Socket              transferSocket,
            FTPProgressListener listener) {
            fSocket     = transferSocket;
            fListener   = listener;
        }
        
        public void run() {
            //
            // Get the file name from the stream.
            //
            if (fListener != null) {
                String  receiverIP = NetUtil.getIPAddress(fSocket.getInetAddress());
                String  receiver = gAddressDB.lookUpName(receiverIP);
                
                if (receiver != null)
                    fListener.setPeerName(receiver);
                else
                    fListener.setPeerName(receiverIP);
            }
                
            try {
                DataInputStream inStream = new DataInputStream(
                        new BufferedInputStream(fSocket.getInputStream()));
                
                String  fullpathName = inStream.readUTF();
                File    file = new File(fullpathName);
                
                if (fListener != null) { 
                    fListener.setFileName(fullpathName);
                    fListener.onConnected();
                }
            
                if (!file.exists() || !file.isFile() || !file.canRead()) {
                    inStream.close();
                    fSocket.close();
                    if (fListener != null)
                        fListener.onCanceled();
                    return;
                }
                
                FileInputStream fileInStream    = new FileInputStream(file);
                OutputStream    socketOutStream = fSocket.getOutputStream();
                byte[]          buffer = new byte[BUFFER_SIZE];
                int             noOfBytes = 0;
                long            noOfTransferedBytes = 0;
                
                while(true) {
                    if (fListener != null) {
                        if (!fListener.onBeingTransfered(noOfTransferedBytes)) {
                            socketOutStream.close();
                            fileInStream.close();
                            fSocket.close();
                            return;
                        }
                    }
                        
                    noOfBytes = fileInStream.read(buffer);
                    if (noOfBytes == -1 ) {
                        socketOutStream.flush(); 
                        break;
                    } else if (noOfBytes > 0) {
                        socketOutStream.write(buffer, 0, noOfBytes);
                        noOfTransferedBytes += noOfBytes;
                    }
                }
                socketOutStream.close();
                fileInStream.close();
                fSocket.close();
                if (fListener != null)
                    fListener.onCompleted(noOfTransferedBytes);
            } catch(IOException e) {
                if (fListener != null)
                    fListener.onCanceled();
                System.out.println(e);
            }
        } 
    }      
}

// LOG
//        11-Oct-98 Y.Shibata   created
// 2.50 : 30-Dec-02 Y.Shibata   replaced Assert.assert with "assert".
//                              changed indents
