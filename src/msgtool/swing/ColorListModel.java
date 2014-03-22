// File: ColorListModel.java - last edit:
// Yoshiki Shibata 8-Mar-98

// Copyright (c) 1998, 2014 by Yoshiki Shibata. All rights reserved.

package msgtool.swing;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;

import msgtool.util.ColorMap;

@SuppressWarnings("serial")
final class ColorListModel extends DefaultListModel<String> 
                                            implements ComboBoxModel<String> {

    private final String[] colorNames;
    private String selectedItem = null;

    public ColorListModel() {
        colorNames = ColorMap.getColorNames();
    }

    // List model
    
    @Override
    public int getSize() {
        return colorNames.length;
    }

    @Override
    public String getElementAt(int index) {
        return colorNames[index];
    }
    
    // ComboBoxModel
    
    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void setSelectedItem(Object object) {
        selectedItem = (String) object;
        fireContentsChanged(this, -1, -1);
    }
}

// LOG
// 2.60 : 22-Mar-14 Y.Shibata Modified with Java 8
