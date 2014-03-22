// File: MeetingProtocol.java - last edit:
// Yoshiki Shibata 12-Jul-98

// Copyright (c) 1997, 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import msgtool.MBPDataUnit;

public final class MeetingProtocol implements MBPDispatcher {

    private MeetingListener fMeetingListener = null;
    
    static private MeetingProtocol fInstance = new MeetingProtocol();
    static public MeetingProtocol getInstance() { return (fInstance); }
    
    private MeetingProtocol() {}

    public void setMeetingListener(MeetingListener listener) {
        fMeetingListener = listener;
        }
        
    // ==========================
    // public methods for meeting
    // ==========================
    public boolean  join(
        String  internalRoomName,
        String  participant) {
        return(MBPUtil.sendMessages(true, MBPClientTypeDef.kMeetingJoin, internalRoomName, participant));
        }
   
    public boolean leave(
        String  internalRoomName,
        String  participant) {
        return(MBPUtil.sendMessages(true, MBPClientTypeDef.kMeetingLeave, internalRoomName, participant));
        }
        
    public boolean  message(
        String  internalRoomName,
        String  participant,
        String  message) {
        return(MBPUtil.sendMessages(true, MBPClientTypeDef.kMeetingMessage, internalRoomName, participant, message));
        }
        
    public boolean  participants(
        String  internalRoomName) {
        return(MBPUtil.sendMessage(true, MBPClientTypeDef.kMeetingParticipants, internalRoomName));
        }
        
    public boolean  participated(
        String  internalRoomName,
        String  participant,
        String  ip) {
        return(MBPUtil.sendMessages(ip, false, MBPClientTypeDef.kMeetingParticipated, internalRoomName, participant));
        }
    
    public boolean  roomOpened(
        String  internalRoomName) {
        return(MBPUtil.sendMessage(true, MBPClientTypeDef.kMeetingRoomOpened, internalRoomName));
        }
    public boolean  roomOpened(
        String  ip,
        String  internalRoomName) {
        return(MBPUtil.sendMessage(ip, false, MBPClientTypeDef.kMeetingRoomOpened, internalRoomName));
        }
    
    public boolean  roomDeleted(
        String  internalRoomName) {
        return(MBPUtil.sendMessage(true, MBPClientTypeDef.kMeetingRoomDeleted, internalRoomName));
        }

    public boolean logLengthRequest(
        String  internalRoomName) {
        return(MBPUtil.sendMessage(true, MBPClientTypeDef.kMeetingLogLengthRequest, internalRoomName));
        }
        
    public boolean logLengthAnswer(
        String  ip,
        String  internalRoomName,
        int     length) {
        return(MBPUtil.sendMessages(ip, false, MBPClientTypeDef.kMeetingLogLengthAnswer, internalRoomName, 
                            Integer.toString(length)));
        }
    // =======================
    // MBPDispatcher method
    // =======================
    public void dispatch(MBPDataUnit dataUnit) {
        if (fMeetingListener == null)
            return;
            
        String  senderIP = dataUnit.getOriginatorAddress();
        int     clientType = dataUnit.getClientType();
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(dataUnit.getClientData());
            ObjectInputStream   ois = new ObjectInputStream(is);
            String internalRoomName = (String)ois.readObject();
            
            switch (clientType) {
                case MBPClientTypeDef.kMeetingJoin:
                        onJoin(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingLeave:
                        onLeave(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingMessage:
                        onMessage(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingParticipants:
                        onParticipants(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingParticipated:
                        onParticipated(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingRoomOpened:
                        onRoomOpened(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingRoomDeleted:
                        onRoomDeleted(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingLogLengthRequest:
                        onLogLengthRequest(senderIP, ois, internalRoomName);
                        break;
                case MBPClientTypeDef.kMeetingLogLengthAnswer:
                        onLogLengthAnswer(senderIP, ois, internalRoomName);
                        break;
                }
            ois.close();
            is.close();
            }
        catch (IOException e) {}
        catch (ClassNotFoundException e) {
            System.err.println(e.toString());
            }
        }
        
    private void onJoin(String  ip, ObjectInputStream ois, String internalRoomName) 
        throws IOException, ClassNotFoundException  {
        String  participant = (String)ois.readObject();
        
        fMeetingListener.onJoin(internalRoomName, participant, ip);
        }
        
    private void onLeave(String ip, ObjectInputStream ois, String internalRoomName) 
        throws IOException, ClassNotFoundException {
        String  participant = (String)ois.readObject();
        
        fMeetingListener.onLeave(internalRoomName, participant, ip);
        }
        
    private void onMessage(String ip, ObjectInputStream ois, String internalRoomName) 
        throws IOException, ClassNotFoundException {
        String  participant = (String)ois.readObject();
        String  message     = (String)ois.readObject();
        
        fMeetingListener.onMessage(internalRoomName, participant, ip, message);
        }
        
    private void onParticipants(String ip, ObjectInputStream ois, String internalRoomName){   
        fMeetingListener.onParticipants(internalRoomName, ip);
        }
        
    private void onParticipated(String ip, ObjectInputStream ois, String internalRoomName) 
        throws IOException, ClassNotFoundException  {
        String  participant = (String)ois.readObject();
        
        fMeetingListener.onParticipated(internalRoomName, ip, participant);
        }
        
    private void onRoomOpened(String ip, ObjectInputStream ois, String internalRoomName) {
        fMeetingListener.onRoomOpened(internalRoomName, ip);
        }
    
    private void onRoomDeleted(String ip, ObjectInputStream ois, String internalRoomName) {
        fMeetingListener.onRoomDeleted(internalRoomName, ip);
        }
    
    private void onLogLengthRequest(String ip, ObjectInputStream ois, String internalRoomName) {
        fMeetingListener.onLogLengthRequest(internalRoomName, ip);
        }
       
    private void onLogLengthAnswer(String ip, ObjectInputStream ois, String internalRoomName) 
        throws IOException, ClassNotFoundException {
        String  lengthValue = (String)ois.readObject();
        int     length = 0;
        
        try {
            length = Integer.parseInt(lengthValue);
            fMeetingListener.onLogLengthAnswer(internalRoomName, ip, length);
            }
        catch (NumberFormatException e) {}
        }  
    }

// LOG
// 1.65 : 28-Sep-97 Y.Shibata   created.
// 1.67 :  4-Oct-97 Y.Shibata   added participated.
// 1.74 :  3-Nov-97 Y.Shibata   added LogLengthRequest and LogLengthAnswer
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
