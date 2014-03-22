// File: ColorListRenderer.java - last edit:
// Yoshiki Shibata 8-Mar-98

// Copyright (c) 1998 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import msgtool.util.ColorMap;

@SuppressWarnings("serial")
final public class ColorListRenderer extends JLabel implements ListCellRenderer {
    
    public ColorListRenderer() {
        setOpaque(true);
        }

    public Component getListCellRendererComponent(
        JList   list,
        Object  value,
        int     index,
        boolean isSelected,
        boolean cellHasFocus) {
        String  colorName = (String)value;
        
        if (colorName == null) {
            setText("");
            setIcon(null);
            }
        else {
            setText(colorName);
            setIcon(new ColoredSquare(ColorMap.getColorByName(colorName)));
            }    
        
        setBackground(isSelected ?
            SystemColor.textHighlight : SystemColor.text);
        setForeground(isSelected ?
            SystemColor.textHighlightText: SystemColor.textText);
        return(this);
        }
        
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        
        dim.width += 16;
        
        return(dim);
        }

    }

