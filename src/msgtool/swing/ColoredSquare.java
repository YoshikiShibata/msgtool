// File: ColoredSquare.java -- last edit;
// Yoshiki Shibata 8-Mar-97

// Copyright (c) 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

final public class ColoredSquare implements Icon {
    Color color;
    public ColoredSquare(Color c) {
        this.color = c;
        }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.fill3DRect(x,y,getIconWidth(), getIconHeight(), true);
        g.setColor(oldColor);
        }
        
    public int getIconWidth() { return 12; }
    public int getIconHeight() { return 12; }
    }
    
// 1.90 :  8-Mar-97 Y.Shibata   created.
