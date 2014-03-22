// File: MBPUtil.java - last edit:
// Yoshiki Shibata 12-Jul-98

// Copyright (c) 1997, 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class MBPUtil {

    public static boolean sendMessage(
        boolean globalBroadcast,
        int     clientType,
        String  message) {
        return(sendMessages(globalBroadcast, clientType, message, null, null));
        }

    public static boolean sendMessage(
        String  destinationIP,
        boolean globalBroadcast,
        int     clientType,
        String  message) {
        return(sendMessages(destinationIP, globalBroadcast, clientType, message, null, null));
        }
        
    public static boolean sendMessages(
        boolean globalBroadcast,
        int     clientType,
        String  message1,
        String  message2) {
        return(sendMessages(globalBroadcast, clientType, message1, message2, null));
        }

    public static boolean sendMessages(
        String  destinationIP,
        boolean globalBroadcast,
        int     clientType,
        String  message1,
        String  message2) {
        return(sendMessages(destinationIP, globalBroadcast, clientType, message1, message2, null));
        }
    
    public static boolean sendMessages(
        boolean globalBroadcast,
        int     clientType,
        String  message1,
        String  message2,
        String  message3) {
        return(sendMessages(null, globalBroadcast, clientType, message1, message2, message3));
        }


    public static boolean sendMessages(
        String  destinationIP,
        boolean globalBroadcast,
        int     clientType,
        String  message1,
        String  message2,
        String  message3) {
        boolean ok;
        
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream  oos = new ObjectOutputStream(os);
        
            oos.writeObject(message1);
            if (message2 != null) {
                oos.writeObject(message2);
                if (message3 != null)
                    oos.writeObject(message3);
                }
                
            byte [] buffer = os.toByteArray();
            if (destinationIP == null)
                ok = MBProtocol.getInstance().sendClientData(
                        globalBroadcast, 
                        clientType, 
                        buffer);
            else
                ok = MBProtocol.getInstance().sendClientData(
                        globalBroadcast, 
                        clientType, 
                        buffer,
                        destinationIP);
            
            oos.close();
            os.close();
            }
        catch (IOException e) {ok = false; }
        
        return(ok);
        }
    }

// LOG
// 1.65 : 28-Sep-97 Y.Shibata   created.
// 1.68 :  9-Oct-97 Y.Shibata   added some SendMessages.
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
