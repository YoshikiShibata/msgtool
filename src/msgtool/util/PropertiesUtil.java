// File: Properties.java -last edit
// Yoshiki Shibata 29-Dec-03

// Copyright (c) 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.util;

import java.util.Enumeration;
import java.util.Properties;

public class PropertiesUtil {

    private PropertiesUtil() { }

    public static String[] propertyNamesToArray(Properties properties) {
        String names[] = new String[properties.size()];

        if (names.length == 0)
            return names;

        Enumeration<?> e = properties.propertyNames();
        int index = 0;
        while (e.hasMoreElements())
            names[index++] = (String) e.nextElement();
        return names;
    }
}

// LOG
// 3.50 : 29-Dec-03 Y.Shibata   created to replace usages of java.util.Properties.
