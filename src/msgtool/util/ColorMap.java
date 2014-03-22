// File: ColorMap.java - last edit:
// Yoshiki Shibata 13-Mar-03

// Copyright (c) 1998, 2003 by Yoshiki Shibata

package msgtool.util;

import java.awt.Color;
import java.awt.SystemColor;

public final class ColorMap {
    static private Object[][] colorMaps = {
            { StringDefs.WHITE,        Color.white },      // 255, 255, 255
            { StringDefs.LIGHT_GRAY,   Color.lightGray },  // 192, 192, 192
            { StringDefs.GRAY,         Color.gray },       // 128, 128, 128
            { StringDefs.DARK_GRAY,    Color.darkGray },   //  64,  64,  64
            { StringDefs.BLACK,        Color.black },      //   0,   0,   0
            { StringDefs.RED,          Color.red },        // 255,   0,   0
            { StringDefs.YELLOW,       Color.yellow },     // 255, 255,   0
            { StringDefs.ORANGE,       Color.orange },     // 255, 200,   0
            { StringDefs.PINK,         Color.pink },       // 255, 175, 175
            { StringDefs.MAGENTA,      Color.magenta },    // 255,   0, 255
            { StringDefs.CYAN,         Color.cyan },       //   0, 255, 255
            { StringDefs.GREEN,        Color.green },      //   0, 255,   0
            { StringDefs.BLUE,         Color.blue },       //   0,   0, 255
            { StringDefs.SYSTEM_TEXT,  SystemColor.text },
            { StringDefs.SYSTEM_WINDOW,SystemColor.window },
            { StringDefs.DESKTOP_COLOR,SystemColor.desktop} 
        };

    static public Color getColorByName(String colorName) {
        if (colorName == null) 
            return Color.lightGray;
            
        for (int i = 0; i < colorMaps.length; i++)
            if (colorName.equals((String) colorMaps[i][0])) 
                return((Color) colorMaps[i][1]);
                
        return Color.lightGray;
	}
    
    static public String getColorName(Color color) {
        for (int i = 0; i < colorMaps.length; i++)
            if (colorMaps[i][1] == color)
                return (String) colorMaps[i][0];
        return null;
	}
    
    static public String[] getColorNames() {
        String[] colorNames = new String[colorMaps.length];
        
        for (int i = 0; i < colorMaps.length; i++)
            colorNames[i] = (String) colorMaps[i][0];
    
        return colorNames;
	}
}
// LOG
// 1.95 : 18-Jul-98 Y.Shibata   created from the old Util.java
// 2.50 : 13-Apr-03	Y.Shibata	added Desktop Color

