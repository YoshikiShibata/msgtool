// File: RecordedMsgHintsDB.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1996, 1998, 2000, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.db;

import java.util.Observable;
import java.util.Properties;

import msgtool.util.FileUtil;
import msgtool.util.StringUtil;

public final class RecordedMsgHintsDB extends Observable {
    static private String kRecordedMsgFile = "MessagingTool.hint2";
    private Properties fRecordedMsgHintsDB = null;
    
    static private RecordedMsgHintsDB fInstance = new RecordedMsgHintsDB();
    static public RecordedMsgHintsDB getInstance() { return (fInstance); }

    private RecordedMsgHintsDB() {
        fRecordedMsgHintsDB = new Properties();
        FileUtil.loadProperties(kRecordedMsgFile,fRecordedMsgHintsDB);
	}

    public synchronized void save() {
        FileUtil.saveProperties(kRecordedMsgFile, fRecordedMsgHintsDB,
            " Recorded Message hints for Java MessagingTool by Y.Shibata");
        //
        // Notify to all Observers
        //
        setChanged();
        notifyObservers();
	}

    public synchronized void clearRecordedMsgHints() {
        fRecordedMsgHintsDB.clear();
	}

    public synchronized String[] getHintsOfRecordedMsg() {
        return StringUtil.getPropertyNames(fRecordedMsgHintsDB);
	}

    public void addRecordedMsgHint(String message) {
        fRecordedMsgHintsDB.setProperty(StringUtil.space2UnderScore(message), "");
	}
    
    public Properties getDB() {
        return fRecordedMsgHintsDB;
	}
}

// LOG
//        31-Aug-96    Y.Shibata   created
// 1.94 :  5-Jul-98 Y.Shibata   modified to use msgtool.util.FileUtil.
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.db
// 2.40 :  4-Jan-00	Y.Shibata	changed names of methods to lower-case.
// 3.50 : 27-Dec-03 Y.Shibata   replaced Properties.put with Properties.setProperty
