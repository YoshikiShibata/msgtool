/*
 * File: MBPDataUnit.java - last edit:
 * Yoshiki Shibata 12-Jul-98
 *
 * Copyright (c) 1997 Yoshiki Shibata. All rights reserved.
 */
package msgtool;

import java.io.Serializable;
//
// MBP(Message Broadcast Protocol) DataUnit
//
// NEVER CHANGE THE DECLARATION OF THIS CLASS!!
//

public class MBPDataUnit implements Serializable {

    static final long serialVersionUID = -1132686498099600025L;
    //
    // HopCount is used to discard looped messages.
    //
    private int fHopCount = 0;
    private boolean fGlobalBroadcast = false;
    //
    // Packet Destination / Source
    //
    private String fDestinationAddress = null;
    private String fSourceAddress = null;
    //
    // Universal ID for this packet.
    //
    private String fOriginatorAddress = null;
    private long fOriginatorTimeStamp = 0;
    private int fOriginatorSerialNo = 0;
    ;
    //
    // Upper layer information
    //
    private int fClientType = 0;
    ;
    private byte[] fClientData = null;

    // ===============================================
    // Constructor for DeSerilization
    // ===============================================
    public MBPDataUnit() {
    }

    // ===============================================
    // For early GC
    // ===============================================
    public void setAllNulls() {
        fDestinationAddress = null;
        fSourceAddress = null;
        fOriginatorAddress = null;
        fClientData = null;
    }

    // ===============================================
    // Properties setter/getter
    // ===============================================
    public void setHopCount(int count) {
        fHopCount = count;
    }

    public int getHopCount() {
        return fHopCount;
    }

    public void setGlobalBroadcast(boolean globalBroadcast) {
        fGlobalBroadcast = globalBroadcast;
    }

    public boolean getGlobalBroadcast() {
        return fGlobalBroadcast;
    }

    public void setDestinationAddress(String destinationAddress) {
        fDestinationAddress = destinationAddress;
    }

    public String getDestinationAddress() {
        return fDestinationAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        fSourceAddress = sourceAddress;
    }

    public String getSourceAddress() {
        return fSourceAddress;
    }

    public void setOriginatorAddress(String originatorAddress) {
        fOriginatorAddress = originatorAddress;
    }

    public String getOriginatorAddress() {
        return fOriginatorAddress;
    }

    public void setOriginatorTimeStamp(long timeStamp) {
        fOriginatorTimeStamp = timeStamp;
    }

    public long getOriginatorTimeStamp() {
        return fOriginatorTimeStamp;
    }

    public void setOriginatorSerialNo(int serialNo) {
        fOriginatorSerialNo = serialNo;
    }

    public int getOriginatorSerialNo() {
        return fOriginatorSerialNo;
    }

    public void setClientType(int clientType) {
        fClientType = clientType;
    }

    public int getClientType() {
        return fClientType;
    }

    public void setClientData(byte[] clientData) {
        fClientData = clientData;
    }

    public byte[] getClientData() {
        return fClientData;
    }
}

// 1.54 :  6-Sep-97 Y.Shibata   created
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
// 2.24 : 16-May-99	Y.Shibata	added serialVersionUID
