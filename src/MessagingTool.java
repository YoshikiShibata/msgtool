/*
 * File: MessagingTool.java - last edit:
 * Yoshiki Shibata 22-Mar-14
 *
 * Copyright (c) 1996 - 1999, 2002 - 2004, 2014 by Yoshiki Shibata.
 * All rights reserved.
 */

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

import msgtool.MainFrameFeatures;
import msgtool.common.AddressNotifier;
import msgtool.exception.UncaughtExceptionHandler;
import msgtool.exception.XThreadGroup;
import msgtool.protocol.FTP;
import msgtool.protocol.MBPClientTypeDef;
import msgtool.protocol.MBProtocol;
import msgtool.protocol.MeetingProtocol;
import msgtool.protocol.MessageProtocol;
import msgtool.protocol.MiscProtocol;
import msgtool.util.SplashScreen;
import msgtool.util.StringDefs;
import msgtool.util.SwingWrapper;

/**
 * The main class of MessagingTool.
 *
 * @author Yoshiki
 */
public final class MessagingTool {

    private final String[] fArgs;

    public static void main(String[] args) {
        UncaughtExceptionHandler.installForAWEEvent();
        MessagingTool msgtool = new MessagingTool(args);
        msgtool.start();
    }

    public MessagingTool(String[] args) {
        fArgs = args;
    }

    public void start() {
        Thread t = new Thread(new XThreadGroup("MessagingTool"),
                () -> create(fArgs)); // Lambda expression
        t.start();
    }

    private void create(String[] args) {
        boolean clientOnlyMode = false;
        boolean useAWT = false;

        System.setErr(System.out);

        SplashScreen splashScreen = new SplashScreen("/images/welcome.gif");
        splashScreen.setVisible(true);

        for (String arg : args) {
            if (arg.equals("-client")) {
                clientOnlyMode = true;
            }
            if (arg.equals("-awt")) {
                useAWT = true;
            }
        }
        MeetingProtocol meetingProtocol = MeetingProtocol.getInstance();
        MBProtocol mbProtocol = MBProtocol.getInstance();

        addDispatchers(meetingProtocol, mbProtocol);

        //
        // All the GUI components must be created on the event dispatch thread.
        // Otherwise, creating them on this thread deadlocks with the event
        // dispatch thread: this thread holds the AWT tree lock and waits for a
        // document read lock, while the event dispatch thread holds that
        // document (notifying its listeners) and waits for the AWT tree lock.
        //
        final boolean awtMode = useAWT;
        AtomicReference<Frame> frameHolder = new AtomicReference<>();
        runOnEventThread(() -> frameHolder.set(awtMode ? new msgtool.awt.MainFrame()
                : new msgtool.swing.MainFrame()));
        Frame mainFrame = frameHolder.get();
        SwingWrapper.setSwingMode(!useAWT);

        if (!clientOnlyMode) {
            if (!MessageProtocol.getInstance().initializeServerSocket()
                    || !MiscProtocol.getInstance().initializeServerSocket()) {
                splashScreen.setVisible(false);
                runOnEventThread(() -> showInUseWarningDialog(awtMode, frameHolder.get()));
                System.exit(1);
            }
            MBProtocol.getInstance().startServer();
        }

        MainFrameFeatures frameFeature = (MainFrameFeatures) mainFrame;
        runOnEventThread(() -> {
            frameFeature.showMyAddress();
            frameFeature.checkIfIPAddressChanged();
        });

        startThreads();

        runOnEventThread(frameFeature::joinMeetingRooms);

        splashScreen.setVisible(false);
        runOnEventThread(() -> mainFrame.setVisible(true));
    }

    /**
     * Runs the given task on the event dispatch thread and waits for its
     * completion.
     *
     * @param task task which touches GUI components
     */
    private void runOnEventThread(Runnable task) {
        if (EventQueue.isDispatchThread()) {
            task.run();
            return;
        }
        try {
            EventQueue.invokeAndWait(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Shows a warning dialog which says "MessagingTool has been already
     * running"
     *
     * @param useAWT    flag to indicate if AWT is used
     * @param mainFrame main frame
     */
    private void showInUseWarningDialog(boolean useAWT, Frame mainFrame) {
        if (useAWT) {
            Dialog warningDialog = new msgtool.awt.WarningUI(mainFrame,
                    "MessagingTool: " + StringDefs.ERROR,
                    StringDefs.INITIALIZE_SERVER_ERROR);
            warningDialog.setVisible(true);
        } else {
            msgtool.swing.DialogUtil.showWarning(
                    (javax.swing.JFrame) mainFrame, "MessagingTool: "
                            + StringDefs.ERROR,
                    StringDefs.INITIALIZE_SERVER_ERROR, true);
        }
    }

    /**
     * Starts all daemon threads.
     */
    private void startThreads() {
        startThreadAsDaemon(new Thread(MessageProtocol.getInstance(),
                "MessageProtocol"));
        startThreadAsDaemon(new Thread(MiscProtocol.getInstance(),
                "MiscProtocol"));
        startThreadAsDaemon(new AddressNotifier());
        startThreadAsDaemon(new Thread(FTP.getInstance(), "FTP"));
    }

    /**
     * Starts a thread as a Daemon thread
     *
     * @param thread thread to be started as a daemon thread
     */
    private void startThreadAsDaemon(Thread thread) {
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Adds dispatchers to the MBProtocol instance
     *
     * @param meetingProtocol instance of MeetingProtocol
     * @param mbProtocol      instance of MBProtocol
     */
    private void addDispatchers(MeetingProtocol meetingProtocol,
                                MBProtocol mbProtocol) {
        mbProtocol.addDispatcher(MBPClientTypeDef.kMessageProtocolMessage,
                MessageProtocol.getInstance());
        mbProtocol.addDispatcher(MBPClientTypeDef.kMiscProtocolMessage,
                MiscProtocol.getInstance());

        int[] meetingClientTypes = {
                MBPClientTypeDef.kMeetingJoin,
                MBPClientTypeDef.kMeetingLeave,
                MBPClientTypeDef.kMeetingMessage,
                MBPClientTypeDef.kMeetingParticipants,
                MBPClientTypeDef.kMeetingParticipated,
                MBPClientTypeDef.kMeetingRoomOpened,
                MBPClientTypeDef.kMeetingRoomDeleted,
                MBPClientTypeDef.kMeetingLogLengthRequest,
                MBPClientTypeDef.kMeetingLogLengthAnswer};

        for (int type : meetingClientTypes) {
            mbProtocol.addDispatcher(type, meetingProtocol);
        }
    }
}
// LOG
// 		  23-Sep-96 Y.Shibata created
// 1.03 :  8-Mar-97 Y.Shibata added -client option
// 1.34 :  6-Jul-97 Y.Shibata added protocol2
// 1.54 :  6-Sep-97 Y.Shibata added MBProtocol
// 1.80 :  6-Dec-97 Y.Shibata show an warning dialog when initialization of
// 							  severs failed.
// 1.81 :  7-Dec-97 Y.Shibata added a SplashScreen.
// 1.94 :  5-Jul-97 Y.Shibata No registration
// 2.12 :  7-Feb-98 Y.Shibata Common source file for all versions.
// 2.39 : 30-Dec-99 Y.Shibata fixed the problem with SwingWrapper which was
// 							  introduced in 2.36
// 2.50 : 31-Dec-02 Y.Shibata Refactoring ....
//        21-Jul-03 Y.Shibata changed to learn Eclipse with CVS
// 2.52 :  5-Sep-04	Y.Shibata refactored.
// 2.60 : 22-Mar-14 Y.Shibata refactored with Java 8 (Lambda expression)

