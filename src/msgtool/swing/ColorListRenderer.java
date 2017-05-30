// File: ColorListRenderer.java - last edit:
// Yoshiki Shibata 30-May-2017

// Copyright (c) 1998, 2017 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import msgtool.util.ColorMap;

@SuppressWarnings("serial")
public final class ColorListRenderer extends JLabel implements ListCellRenderer<String> {

  public ColorListRenderer() {
    setOpaque(true);
  }

  public Component getListCellRendererComponent(
      JList<? extends String> list,
      String value,
      int index,
      boolean isSelected,
      boolean cellHasFocus) {
    String colorName = value;

    if (colorName == null) {
      setText("");
      setIcon(null);
    } else {
      setText(colorName);
      setIcon(new ColoredSquare(ColorMap.getColorByName(colorName)));
    }

    setBackground(isSelected ? SystemColor.textHighlight : SystemColor.text);
    setForeground(isSelected ? SystemColor.textHighlightText : SystemColor.textText);
    return (this);
  }

  public Dimension getPreferredSize() {
    Dimension dim = super.getPreferredSize();

    dim.width += 16;

    return (dim);
  }
}
