/*
 * File: DelivererImpl.java - last edit:
 * Yoshiki Shibata 22-Mar-2014
 *
 * Copyright (c) 1999, 2014 by Yoshiki Shibata
 * All rights reserved.
 */
package msgtool;

import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.log.LogArea;
import msgtool.protocol.MessageProtocol;
import msgtool.ui.DedicatedUI;
import msgtool.ui.DedicatedUIManager;
import msgtool.ui.InputArea;
import msgtool.ui.MainUI;
import msgtool.ui.OnlineListUI;
import msgtool.util.CursorControl;
import msgtool.util.StringDefs;
import msgtool.util.StringUtil;

public class DelivererImpl<T> implements Deliverer {

    private final MainUI mainUI;
    private final OnlineListUI onlineListUI;
    private final LogArea logArea;
    private final DedicatedUIManager<T> dedicatedUIManager;

    private final CursorControl cursorControl = CursorControl.instance();
    private final MessageProtocol messageProtocol = MessageProtocol.getInstance();
    private final AddressDB addressDB = AddressDB.instance();
    private final PropertiesDB propertiesDB = PropertiesDB.getInstance();

    public DelivererImpl(MainUI mainUI, LogArea logArea,
            OnlineListUI onlineListUI, DedicatedUIManager<T> dedicatedUIManager) {
        this.mainUI = mainUI;
        this.logArea = logArea;
        this.onlineListUI = onlineListUI;
        this.dedicatedUIManager = dedicatedUIManager;
        this.dedicatedUIManager.setDeliverer(this);
    }

    public void deliver(String toList, InputArea inputArea) {
        if (mainUI.isInOffice()) {
            mainUI.setNotInOfficeEnabled(false);
            deliverInternal(toList, inputArea, null);
            inputArea.requestFocus();
            mainUI.setNotInOfficeEnabled(true);
        }
    }

    public void deliver(String toList, InputArea inputArea, DedicatedUI originator) {
        if (mainUI.isInOffice()) {
            mainUI.setNotInOfficeEnabled(false);
            deliverInternal(toList, inputArea, originator);
            inputArea.requestFocus();
            mainUI.setNotInOfficeEnabled(true);
        }
    }

    private void deliverInternal(String toList, InputArea inputArea,
            DedicatedUI originator) {
        String[] recipientsList = createRecipientsList(toList);
        String inputMessage = inputArea.getText();

        if (!isValidOperation(recipientsList, inputMessage)) {
            return;
        }

        // Create a String array which holds IP addresses of recipients.   
        String[] remoteIPs = new String[recipientsList.length];

        String message = composeMessage(recipientsList, inputMessage);

        logArea.lock();
        cursorControl.setBusy(true);
        logArea.appendDate();

        boolean deliverOk = deliverMessage(recipientsList, remoteIPs, message);
        String deliveredLog = completeLog(deliverOk, inputMessage, inputArea);

        // Append the log into Dedicated dialogs only if this message is not
        // broadcast message.
        if (!isBroadcast(recipientsList[0])) {
            appendLogToDedicatedUIsOfRecipeints(recipientsList, remoteIPs,
                    originator, deliveredLog);
        }

        logArea.unlock();
        cursorControl.setBusy(false);
        Thread.yield();
    }

    private void appendLogToDedicatedUIsOfRecipeints(String[] recipientsList,
            String[] remoteIPs, DedicatedUI originator, String deliveredLog) {
        //
        // Append the deliveredLog to all corresponding recipients' dedicated
        // window.
        // if originator is not null, then the delivered log must be appended
        // to the originator window even if sending the message failed.
        //
        if (originator != null) {
            originator.appendLog(deliveredLog);
        }

        for (int i = 0; i < remoteIPs.length; i++) {
            if (remoteIPs[i] == null) {
                continue;
            }

            DedicatedUI dedicated = dedicatedUIManager.findOrCreate(remoteIPs[i],
                    recipientsList[i]);
            if (originator == null || originator != dedicated) {
                dedicated.appendLog(deliveredLog);
            }
        }
    }

    private String completeLog(boolean deliverOk, String inputMessage,
            InputArea inputArea) {
        //
        // Note that if a message is sent successfully to at least
        // one of recipients, the Deliver operation should be considered
        // "success".
        //
        logArea.appendSubText(":\n");
        if (deliverOk) {
            int msgLen = inputMessage.length();
            logArea.appendSubText(inputMessage);
            if (msgLen > 0 && inputMessage.charAt(msgLen - 1) != '\n') {
                logArea.appendSubText("\n");
            }
            inputArea.clearText();
        }
        logArea.appendSubText("\n");
        return logArea.getLastMessage();
    }

    private boolean deliverMessage(String[] recipientsList, String[] remoteIPs,
            String message) {
        boolean deliverOK = false;

        for (int i = 0; i < recipientsList.length; i++) {
            remoteIPs[i] = null;

            if (i != 0) {
                logArea.appendSubText(", ");
            }
            logArea.appendSubText(recipientsList[i]);

            remoteIPs[i] = messageProtocol.sendMessage(propertiesDB.getUserName(),
                    recipientsList[i], message);

            if (isBroadcast(recipientsList[i])) {
                remoteIPs[i] = null; // no remote IP for broadcasts
                deliverOK = true;
            } else {
                updateOnlineStatus(recipientsList[i], remoteIPs[i] != null);
                if (remoteIPs[i] != null) {
                    logArea.appendSubText(StringDefs.RECEIVED);
                    deliverOK = true;
                } else {
                    logArea.appendSubText(StringDefs.FAILED);
                }
            }
        }
        return deliverOK;
    }

    private void updateOnlineStatus(String recipient, boolean online) {
        //
        // In case that the recipient is either an IP address or an Host name,
        // it should not be listed as OnLine. Therefore make sure the recipient
        // is registered as key in the Address DB. [V1.55]
        //
        if (addressDB.lookUpAddressCache(recipient) != null) {
            if (online) {
                onlineListUI.setOnline(recipient);
            } else {
                onlineListUI.setOffline(recipient);
            }
        }
    }

    private String[] createRecipientsList(String toList) {
        String[] recipientsList = StringUtil.parseToArray(toList);
        if (recipientsList.length == 0) {
            return null;
        }

        for (String recipient : recipientsList) {
            if (isBroadcast(recipient)) {
                return new String[]{recipient};
            }
        }

        return recipientsList;
    }

    private boolean isBroadcast(String recipient) {
        return recipient.equals(StringDefs.ALL)
                || recipient.equals(StringDefs.ALL_AREAS);
    }

    private String composeMessage(String[] recipientsList, String inputMessage) {
        if (recipientsList.length == 1) {
            if (isBroadcast(recipientsList[0])) {
                return StringDefs.BROADCAST_MESSAGE + inputMessage;
            } else {
                return inputMessage;
            }
        } else {
            return composeMultipleRecipientsMessage(recipientsList, inputMessage);
        }
    }

    private String composeMultipleRecipientsMessage(String[] recipientsList,
            String inputMessage) {
        StringBuilder message = new StringBuilder();

        message.append(StringDefs.TO_C).append(' ');
        for (int i = 0; i < recipientsList.length; i++) {
            if (i != 0) {
                message.append(", ");
            }
            message.append(recipientsList[i]);
        }
        message.append('\n').append(inputMessage);
        return message.toString();
    }

    private boolean isValidOperation(String[] recipients, String message) {
        if (recipients == null) {
            return false;
        }
        if (recipients.length == 0) {
            return false;
        }
        return !(isBroadcast(recipients[0]) && message.length() == 0);
    }
}

// LOG
// 2.35 : 6-Nov-99 Y.Shibata created.// 13-Nov-99 Y.Shibata did ExtractMethod
// refactoring.
