// File: MBPDispatcher.java - last edit:
// Yoshiki Shibata 12-Jul-98

// Copyright (c) 1997, 1998 Yoshiki Shibata. All rights reserved

package msgtool.protocol;

import msgtool.MBPDataUnit;

public interface MBPDispatcher {

    public void dispatch(MBPDataUnit  dataUnit);
    }
    
// 1.54 :  6-Sep-97 Y.Shibata   created
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.protocol
