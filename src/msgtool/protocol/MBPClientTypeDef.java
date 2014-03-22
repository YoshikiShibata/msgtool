// File: MBPClientTypeDef.java - last edit:
// Yoshiki Shibata 12-Jul-98

// Copyright (c) 1997, 1998 Yoshiki Shibata. All rights reserved.

package msgtool.protocol;

public final class MBPClientTypeDef {

    public final static int kMessageProtocolMessage = 0;
    public final static int kMiscProtocolMessage     = 1;
    
    public final static int kMeetingJoin            = 10;
    public final static int kMeetingLeave           = 11;
    public final static int kMeetingMessage         = 12;
    public final static int kMeetingParticipants    = 13;
    public final static int kMeetingParticipated    = 14;
    public final static int kMeetingRoomOpened      = 15;
    public final static int kMeetingRoomDeleted     = 16;
    public final static int kMeetingLogLengthRequest= 17;
    public final static int kMeetingLogLengthAnswer = 18;
    }
// 1.54 :  6-Sep-97 Y.Shibata   created.
// 1.64 : 25-Sep-97 Y.Shibata   added kProtocol2Message
// 1.74 :  3-Nov-97 Y.Shibata   added kMeetingLogLengthRequest & kMeetingLogLengthAnswer
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol 
