// File: MBProtocol.java - last edit:
// Yoshiki Shibata 13-Nov-2004

// Copyright (c) 1997, 1998, 2003, 2004 Yoshiki Shibata. All rights reserved.

// This file is common for all versions: AWT and Swing.

package msgtool.protocol;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TreeSet;
import java.util.Vector;

import msgtool.MBPDataUnit;
import msgtool.db.AddressDB;
import msgtool.util.MemoryUtil;
import msgtool.util.NetUtil;
import msgtool.util.Queue;
import msgtool.util.TimerUtil;

public final class MBProtocol implements Runnable {

    private final static int kSocketNo      = 591117 & 0xffff; // my birthday 1959.11.17
    private final static int kMaxBufferSize = 1280;       // Max Buffer Siz in bytes.
    private final static int kMaxHopCount   = 15;
    private final static String kBroadcastAddress  = "255.255.255.255";
    private final static int kNoOfResend    = 3;
    private final static int kResendInterval= 500;  // milisecond
    private final static int kMaxNoOfUIDs   = 10;
    private final static int kDiscardUIDTime = 1 * 60 * 1000;  // 1 minutes
    
    private int                         fSerialNo       = 0;
    private Vector<UniversalID>         fUIDs           = null;
    private int                         fNoOfUIDs       = 0;
    private Vector<DispatchMap>         fDispatchMaps   = null;
    private SendMBPDataUnitThread       fSendThread     = null;
    private ReceiveMBPDataUnitThread    fReceiveThread  = null;
    private Queue<MBPDataUnit>          fReceiveQueue   = null;
    private TreeSet<String>             fHostsOnSubnet  = null;
    
    static private MBProtocol fInstance = new MBProtocol();
    static public MBProtocol getInstance() {return(fInstance); }
    // =======================
    // UniversalID definition
    // =======================
    private final class UniversalID {
        private String  fAddress;
        private long    fTimeStamp;
        private int     fSerialNo;
        private long    fCreatedTime;
        
        public UniversalID (MBPDataUnit mbpDataUnit) {
            fAddress    = mbpDataUnit.getOriginatorAddress();
            fTimeStamp  = mbpDataUnit.getOriginatorTimeStamp();
            fSerialNo   = mbpDataUnit.getOriginatorSerialNo();
            
            fCreatedTime = System.currentTimeMillis();
            }
            
        //
        // Overriding Object.equals()
        //    
        public boolean equals(Object obj) {
            UniversalID   uid = (UniversalID)obj;
            
            if (fSerialNo != uid.fSerialNo)
                return(false);    
            if (fTimeStamp != uid.fTimeStamp)
                return(false);
            if (!fAddress.equals(uid.fAddress))
                return(false);
            
            return(true);
            }
        //
        // get the creation time
        //
        public long getCreatedTime() {
            return(fCreatedTime);
            }
        //
        // For earlier GC
        //
        public void setAllNulls() {
            fAddress = null;
            }
        } 
    // =========================
    // Dispatch Map
    // =========================
    private class DispatchMap {
        private int             fClientType = 0;
        private MBPDispatcher   fDispatcher = null;
        
        public DispatchMap(
            int             clientType,
            MBPDispatcher   dispatcher) {
            fClientType = clientType;
            fDispatcher = dispatcher;
            }
            
        public boolean dispatch(MBPDataUnit dataUnit) {
            if (fClientType == dataUnit.getClientType()) {
                fDispatcher.dispatch(dataUnit);
                return(true);
                }
            return(false);
            }
        }      
    // =========================
    // Constructor
    // =========================
    protected MBProtocol() {
        fUIDs           = new Vector<UniversalID>();
        fDispatchMaps   = new Vector<DispatchMap>();
        fHostsOnSubnet  = new TreeSet<String>();
        fSendThread     = new SendMBPDataUnitThread();
        fSendThread.setDaemon(true);
        fSendThread.start();
        fReceiveQueue   = new Queue<MBPDataUnit>();
        fReceiveThread  = new ReceiveMBPDataUnitThread(fReceiveQueue);
        fReceiveThread.setDaemon(true);
        fReceiveThread.start();
        }
   
    public void addDispatcher(
        int             clientType,
        MBPDispatcher   dispatcher) {
        DispatchMap map = new DispatchMap(clientType, dispatcher);
        fDispatchMaps.addElement(map);
        }
    // =========================
    // Send Function
    // =========================
    private final class DataUnitDatagram {
        public DatagramPacket fPacket           = null;
        public int            fRemainedCount    = 0;
        
        public DataUnitDatagram(
            DatagramPacket packet,
            int            sendCount) {
            fPacket = packet;
            fRemainedCount = sendCount;
            }
        }
        
    private class SendMBPDataUnitThread extends Thread {
        private Vector<DataUnitDatagram> fDatagrams = null;
        
        public SendMBPDataUnitThread() {
            fDatagrams = new Vector<DataUnitDatagram>();
            }
        
        public void SendMBPDataUnit(
            byte[]  datagramData,
            String  destinationAddress,
            int     sendCount) {
            DatagramPacket  packet =  null;
            
            try {
                packet = new DatagramPacket(
                        datagramData, datagramData.length,
                        InetAddress.getByName(destinationAddress),
                        kSocketNo);
                }
            catch (UnknownHostException e) { return; }
            
            DataUnitDatagram datagram = new DataUnitDatagram(packet, sendCount);
            synchronized (fDatagrams) {
                fDatagrams.addElement(datagram);
                fDatagrams.notifyAll();
                } 
            }
        
        public void run() {
            DatagramSocket  socket = null;
            //
            // Create socket first.
            //
            try {
                socket = new DatagramSocket();
                }
            catch (SocketException e) {
                System.out.println("Socket Exception");
                return;
                }
                
            synchronized (fDatagrams) {
                while (true) {
                    //
                    // Wait for any send request
                    //
                    if (fDatagrams.isEmpty()) {
                        MemoryUtil.fullGC(); // [V1.95]
                        //
                        // Now there is no datagram. Wait for request.
                        //
                        while (fDatagrams.isEmpty()) {
                            try {
                                fDatagrams.wait();
                                }
                            catch(InterruptedException e) {}
                            }
                        }
                    else {
                        try {
                            fDatagrams.wait(kResendInterval);
                            }
                        catch(InterruptedException e) {}
                        }
                    //
                    // Send all datagrams
                    //
                    for (DataUnitDatagram datagram: fDatagrams) {
                        try {
                            socket.send(datagram.fPacket);
                            }
                        catch(IOException e) {}
                        datagram.fRemainedCount --;
                        }
                    //
                    // Delete datagrams if necessary
                    //
                    int noOfDatagrams = fDatagrams.size();
                    
                    for (int i = 0; i < noOfDatagrams; ) {
                        DataUnitDatagram datagram = fDatagrams.elementAt(i);
                        
                        if (datagram.fRemainedCount <= 0) {
                            fDatagrams.removeElementAt(i);
                            noOfDatagrams --;
                            datagram.fPacket = null; // GC.
                            }
                        else
                            i++;
                        } 
                    } 
                }
            }
        }
    
    private boolean SendMBPDataUnit(
        MBPDataUnit     dataUnit) {
        
        dataUnit.setSourceAddress(NetUtil.getMyIPAddress());
        
        try {
            //
            // Externalize the DataUnit into bytes
            //
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream  oos = new ObjectOutputStream(os);
        
            oos.writeObject(dataUnit);
            byte [] buffer = os.toByteArray();
            
            oos.close();
            os.close();
            
            if (buffer.length > kMaxBufferSize) {
                return(false);
                }
        
            fSendThread.SendMBPDataUnit(buffer, dataUnit.getDestinationAddress(),kNoOfResend);
            return(true);                               
            }
        catch (IOException e) {
            System.err.println("Exception1: " + e);
            return(false);
            }
        } 
        
    public boolean sendClientData(
        boolean globalBroadcast,
        int     clientType,
        byte[]  clientData) {
        return(sendClientData(globalBroadcast, clientType, clientData, kBroadcastAddress));
        }

    public boolean sendClientData(
        boolean globalBroadcast,
        int     clientType,
        byte[]  clientData,
        String  destinationAddress) {
        MBPDataUnit dataUnit = new MBPDataUnit();
        
        dataUnit.setHopCount(0);
        dataUnit.setGlobalBroadcast(globalBroadcast);
        
        dataUnit.setDestinationAddress(destinationAddress);
        
        dataUnit.setOriginatorAddress(NetUtil.getMyIPAddress());
        dataUnit.setOriginatorTimeStamp(System.currentTimeMillis());
        dataUnit.setOriginatorSerialNo(fSerialNo++);
         
        dataUnit.setClientType(clientType);
        dataUnit.setClientData(clientData);
        
        return(SendMBPDataUnit(dataUnit));
        }
          
    // =========================
    // Server function
    // =========================
    public void startServer() {
        Thread serverThread = new Thread(this);
        
        serverThread.start();
        } 
        
    public void run() {
        DatagramSocket  socket = null;
        int             retryCount = 0;
        
        //
        // When a user quits the MessagingTool. the tool makes all windows invisible and
        // sends OffLine command to all on-line recipients. In other words, if the user
        // tries to invoke the tool again before the previous tool is still running, creating
        // the server socket might fail. So try 6 times. Please see Protocol.java also.
        //
        while (retryCount < 6) {
            try {
                socket = new DatagramSocket(kSocketNo);
                } 
            catch (SocketException e) {
                System.out.println("Count not get a Datagram port: " + 
                            kSocketNo + ", " + e);
                socket = null;
                } 
            retryCount++;
            if (socket == null)
                TimerUtil.sleep(10*1000); // sleep 10 seconds
            else
                break;
            }
        //
        // If the socket is still null, then terminate this thread.
        //
        if (socket == null)
            return;
        //
        // Start receiving a data and process it.
        //
        byte[]  buffer = new byte[kMaxBufferSize];
        DatagramPacket packet;
        while (true) {
            try {
                //
                // clear buffer at first
                //
                for (int i = 0; i < kMaxBufferSize; i++)
                    buffer[i] = 0;
                
                packet = new DatagramPacket(buffer, kMaxBufferSize);    
                socket.receive(packet);
                //
                // Now deserialize the MBPDataUnit.
                //
                ByteArrayInputStream is = new ByteArrayInputStream(packet.getData());
                ObjectInputStream ois = new ObjectInputStream(is);
                
                MBPDataUnit dataUnit = (MBPDataUnit)ois.readObject();
                ois.close();
                is.close();
                //
                // Now the dataUnit is received. If this dataUnit has been already received,
                // then just discard it. 
                // 
                UniversalID  uid = new UniversalID(dataUnit);
                int         index = fUIDs.indexOf(uid);
                
                if (index != -1) {
                    uid.setAllNulls();
                    uid = null;
                    dataUnit.setAllNulls();
                    dataUnit = null;
                    continue;
                    }
                //
                // Make sure that the hopCount is greater than kMaxHopCount.
                //
                int hopCount = dataUnit.getHopCount();
                if (hopCount > kMaxHopCount) {
                    uid.setAllNulls();
                    uid = null;
                    dataUnit.setAllNulls();
                    dataUnit = null;
                    continue;
                    }
                else {
                    //
                    // In most cases, the same dataUnit will be received immediately.
                    // Insert this new UID into the top of fUIDs so that searching will
                    // be much fater. [V1.70]
                    //
                    dataUnit.setHopCount(hopCount + 1);
                    fUIDs.insertElementAt(uid, 0);
                    fNoOfUIDs ++;
                    }
                //
                // Check if the sender is the same network. If so, remember, its address.
                //
                if (dataUnit.getDestinationAddress().equals(kBroadcastAddress)) {
                    String  sourceAddress = dataUnit.getSourceAddress();
                    
                    fHostsOnSubnet.add(sourceAddress);
                    }  
                // 
                //
                // Process this MBPDataUnit with another thread, so that
                // next data can be received immediately.
                //
                fReceiveQueue.put(dataUnit);
                dataUnit = null; // GC
                //
                // If the number of saved UIDs are greater than kMaxNoOfUIDs,
                // then start discarding old UIDs.
                //
                if (fNoOfUIDs > kMaxNoOfUIDs) {
                    long    currentTime = System.currentTimeMillis();
                    
                    uid = fUIDs.firstElement();
                    while (fNoOfUIDs > kMaxNoOfUIDs) {
                        if ((uid.getCreatedTime() + kDiscardUIDTime) < currentTime) {
                            //
                            // Old UID. discard this
                            //
                            fUIDs.removeElementAt(0);
                            fNoOfUIDs --;
                            uid.setAllNulls();
                            uid = fUIDs.firstElement();
                            }
                        else
                            break;
                        }
                    }
                }
            catch (StreamCorruptedException e) {
                System.err.println("StreamCorruptedException: " + e);
                }
            catch (IOException e) {
                System.err.println("IOException:  " + e);
                }
            catch (ClassNotFoundException e ) {
                System.err.println(e.toString());
                }
            }   
        }
    // =======================================
    // ReceiveMBPDataUnitThread Implementation
    // =======================================
    private final class ReceiveMBPDataUnitThread extends Thread {
        private Queue<MBPDataUnit>       fRcvQueue = null;
        private AddressDB   fAddressDB = AddressDB.instance();

        public ReceiveMBPDataUnitThread(Queue<MBPDataUnit>   receiveQueue) {
            fRcvQueue = receiveQueue;
            }
             
        public void run() {
            MBPDataUnit dataUnit;
            while(true) {
                dataUnit = null; // GC
                dataUnit = fRcvQueue.get();
                //
                // Call Upperlayer first, then do relayed broadcast if necessary.
                //
                for (DispatchMap map: fDispatchMaps) {
                   	if (map.dispatch(dataUnit))
                    	break;
              	}
                //
                // Now forward this message if necessary.
                // If the message is not GlobalBroacast, then just return;
                //
                if (!dataUnit.getGlobalBroadcast()) {
                    dataUnit.setAllNulls();
                    continue;
                    }
                //
                // Now the message is GlobalBroadcast. If this message
                // is received as broadcast, then just repeat. Otherwise,
                // broadcast and repeat.
                //
                String  sourceAddress = dataUnit.getSourceAddress();
            
                if (!kBroadcastAddress.equals(dataUnit.getDestinationAddress()))
                    BroadcastMessage(dataUnit);
                
                RepeatMessage(dataUnit, sourceAddress);
                }
            }
        
        private void BroadcastMessage(MBPDataUnit dataUnit) {
            dataUnit.setDestinationAddress(kBroadcastAddress);
            
            SendMBPDataUnit(dataUnit);
            }
            
        private void RepeatMessage(
            MBPDataUnit dataUnit,
            String sourceAddress) {
            String[]    recipients = fAddressDB.getListOfAddressCache();
               
            for (String recipient: recipients) {
                String address = fAddressDB.lookUpAddressCache(recipient);
                //
                // If the address is the broadcast address, then do not send.
                //
                if (kBroadcastAddress.equals(address))
                    continue;
                //
                // If the address is on the same network, then do no send [V1.70]
                // 
                if (fHostsOnSubnet.contains(address)) {
                    continue;
                    }
                    
                //
                // If the address is either the originator or source, then
                // don't repeat the message.
                //
                if (!address.equals(dataUnit.getOriginatorAddress()) &&
                    !address.equals(sourceAddress)) {
                    dataUnit.setDestinationAddress(address);
                    SendMBPDataUnit(dataUnit);
                    }
                }
            }
        } 
    } 

// 1.54 :  6-Sep-97 Y.Shibata   created
// 1.65 : 27-Sep-97 Y.Shibata   improved the performance by using a thread for sending data.
// 1.68 :  9-Oct-97 Y.Shibata   added another SendClientData
// 1.70 : 18-Oct-97 Y.Shibata   put a new UID into the top of list.
//                          kMaxNoOfUIDs : 20 -> 10
//                          kDiscardUIDTime: 10min -> 1 min
//                          completely rewritten SendMBPDataUnitThread
// 1.95 : 11-Jul-97 Y.Shibata   modified to use msgtool.util.TimerUtil
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
//        21-Jul-98 Y.Shibata   added code to call GC. 
// 2.50 : 19-Nov-03	Y.Shibata	modified to be compiled cleanly with "Tiger"(SDK1.5).
//      : 27-Dec-03 Y.Shibata   used Generics and the enhanced "for" statement
// 2.52 : 13-Nov-04 Y.Shibata	refactored
