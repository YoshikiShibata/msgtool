// File: LogMessages.java - last edit
// Yoshiki Shibata 21-Aug-99

// Copyright (c) 1997 - 1999 Yoshiki Shibata. All rights reserved.

package msgtool.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

final class LogMessages {

    private final static String kUserHome       = System.getProperty("user.home");
    private final static int    kMaxLogFiles    = 9;
    
    private int     fNoOfLogFiles   = 0;
    private boolean fLoggingEnabled    = false;
    private File[]  fFiles          = null;

    private FileWriter  fFileWriter = null;
    private DateFormat  fDateFormat = DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());
    private String[]    fHeaders    = null;
    
    // ===================
    // Constructor  
    // ===================
    public LogMessages(String   filePrefix) {
            
        fFiles = new File[kMaxLogFiles];
        for (int i = 0; i < kMaxLogFiles; i++) 
            fFiles[i] = new File(kUserHome, filePrefix + i);
	}
    
    
    // ===================================
    // Public functions 
    // ===================================
    synchronized void setNoOfLogFiles(int noOfLogFiles) {
        fNoOfLogFiles = noOfLogFiles;
        if (fNoOfLogFiles < kMaxLogFiles) {
            for (int i = fNoOfLogFiles; i < kMaxLogFiles; i++)
                fFiles[i].delete();
		}
	}
   
    synchronized void startLogging() {
        if (fLoggingEnabled)
            return;
        //
        // If the first log file doesn't exist or its content length is 0,
        // then don't shit files. [V1.85]
        //
        if (!fFiles[0].exists() || readContent(0).length() == 0) 
            fFiles[0].delete();
        else {
            //
            // Shift files
            //
            fFiles[fNoOfLogFiles-1].delete();
            for (int i = fNoOfLogFiles - 2; i >= 0; i --)
                fFiles[i].renameTo(fFiles[i+1]);
		}
            
        //
        // Now create a FileWriter where
        //
        try { 
            fFileWriter = new FileWriter(fFiles[0]);
		} catch (FileNotFoundException e) {
			//
			// If the file name contains illegal characters, this exception will happen
			//
			return;
		} catch (IOException e) {
			System.out.println(e.toString()); 
		}
        
        String  timeStampHeader = fDateFormat.format(new Date()) + "\n";
        try {
            fFileWriter.write(timeStampHeader, 0, timeStampHeader.length());
            fFileWriter.flush();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
        
        fLoggingEnabled = true;
	}
        
    synchronized void stopLogging() {
        if (!fLoggingEnabled )
            return;
            
        try {
            fFileWriter.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
        fFileWriter = null;
        fLoggingEnabled = false;
	}
        
        
    synchronized void logMessage(String   message) {
        if (!fLoggingEnabled)
            return;
            
        try {
            fFileWriter.write(message, 0, message.length());
            fFileWriter.flush();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
        
    synchronized String[] getHeaders() {
        //
        // Count the number of existing files
        //
        int count = 0;
        
        for (int i = 0; i < fNoOfLogFiles; i++) {
            if (fFiles[i].exists())
                count ++;
            else
                break;
		}
            
        fHeaders = new String[count]; 
        boolean nullContained = false;
        for (int i = 0; i < count ; i++) {
            fHeaders[i] = readHeader(i);
            if (fHeaders[i] == null)
                nullContained = true;   
		}
        //
        // If there is null header(corrupted file), then
        // remake the headers to be returned.
        //
        if (nullContained) {
            int actualCount = 0;
            
            for (int i = 0; i < count; i++)
                if (fHeaders[i] != null)
                    actualCount++;
            //
            // Check if there is still any valid log file.
            // In most cases, actualCount should not be zero, because
            // the log0 file is always created. However, for safety,
            // the following checking is done.
            //        
            if (actualCount == 0)
                return(null);
            
            String[] headers = new String[actualCount];
            
            for (int i = 0, j = 0; i < count; i++)
                if (fHeaders[i] != null)
                    headers[j++] = fHeaders[i];
         
            return(headers);
		} else
            return(fHeaders);
	}
        
        
    synchronized String getContent(String  logDate) {
        for (int i = 0; i < fHeaders.length; i++)
            if (fHeaders[i] != null && logDate.equals(fHeaders[i]))
                return(readContent(i));
    
        return(null);
	}
    // ==================================
    // Private functions
    // ==================================
    private String  readHeaderFromFileReader(Reader reader) 
        throws IOException {
        int     ch      = reader.read();
        String  header  = "";
        
        while (ch != '\n' && ch != -1 ) {
            header += (char)ch;
            ch = reader.read();
		}
        //
        // Note that if ch is not supposed to be -1. But it might happen if 
        // the content of the file is corrupted. In that case, return null.
        //
        return (ch == -1) ? null : header; 
	}
        
    private String  readHeader(int index) {
        String  header = null;
        try {
            FileReader      fileReader      = new FileReader(fFiles[index]);
            BufferedReader  bufferedReader  = new BufferedReader(fileReader);
            
            header = readHeaderFromFileReader(bufferedReader);
            bufferedReader.close();
            fileReader.close();
		} catch (IOException e) {
			System.out.println(e.toString()); 
		}
        return(header);
	}
        
    
    private String  readContent(int index) {
        StringBuilder   content = null;
        char[]          buffer = new char[1024];
        int             readCount = 0;
        
        try {
            FileReader      fileReader      = new FileReader(fFiles[index]);
            BufferedReader  bufferedReader  = new BufferedReader(fileReader);
            
            //
            // If the header is null, it means this file is corrupted.
            // So just return zero length string. Please note that
            // the following check is not necessary, because index will 
            // not indicate a corrupted file. But for safey, the following
            // check is being done.
            //
            if (readHeaderFromFileReader(bufferedReader) == null)
                return("");
                
            readCount = bufferedReader.read(buffer, 0, buffer.length);
            while (readCount > 0) {
                if (content == null)
                    content = new StringBuilder((int)(fFiles[index].length()));
                
                content.append(buffer, 0, readCount);
                readCount = bufferedReader.read(buffer, 0, buffer.length);
			}
            bufferedReader.close();
            fileReader.close();
		} catch (IOException e) { 
			System.out.println(e.toString()); 
		}
        
        return (content == null) ? "" : content.toString();
	}
}

// 1.46 : 16-Aug-97 Y.Shibata   created.
// 1.47 : 20-Aug-97 Y.Shibata   fixed a bug that caused an infinite loop.
// 1.85 : 13-Jan-98 Y.Shibata   don't shift files if the first log is empty.
// 1.95 : 18-Jul-98 Y.Shibata   moved to msgtool.util
// 2.33 : 21-Aug-99	Y.Shibata	moved to msgtool.log package
