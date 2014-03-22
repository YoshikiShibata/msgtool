// File: FileUtil.java - last edit:
// Yoshiki Shibata - 30-Dec-2002

// Copyright (c) 1996 - 1998, 2002 by Yoshiki Shibata. All rights reserved.

package msgtool.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class FileUtil {
    private static String   fUserHome               = System.getProperty("user.home");
    private static char     fFileSeparator          = System.getProperty("file.separator").charAt(0);
    private static char     fLastCharOfDirectory    = fUserHome.charAt(fUserHome.length() - 1);
    
    // ============================================================
    // create a full pathname string based on user.home properties.
    // ============================================================
    static public String makeFullPathname(String fileName) {
        if (fLastCharOfDirectory == fFileSeparator)
            return fUserHome + fileName;
        else
            return fUserHome + fFileSeparator + fileName;
	}
    // ===========================
    // Load properties from a file
    // ===========================
    static public void loadProperties(
        String     fileName,
        Properties properties) {
        try {
            FileInputStream     fis = new FileInputStream(makeFullPathname(fileName));
            BufferedInputStream bis = new BufferedInputStream(fis);
            properties.load(bis);
            bis.close();
            fis.close();
		} catch (FileNotFoundException e) {
            // Do nothing. Data file will be created eventaully.
		} catch (IOException e) {
            e.printStackTrace();
		}
	}
    // ===========================
    // Save properties into a file
    // ===========================
    final static public void saveProperties(
        String     fileName,
        Properties properties,
        String     title) {
        try {
            FileOutputStream        fos = new FileOutputStream(makeFullPathname(fileName));
            BufferedOutputStream    bos = new BufferedOutputStream(fos);

            properties.store(bos, title);
            bos.close();
            fos.close();
		} catch (IOException e) {
            e.printStackTrace();
		}
	}
}
    
// 1.95 :   5-Jul-98    Y.Shibata   created from the old Util.java
//         19-Jul-98    Y.Shibata   performance tuning.
// 2.50 :  30-Dec-02    Y.Shibata   got rid of 1.1 compatible code
