// File: ImageFilePreviewer.java - last edit:
// Yoshiki Shibata 20-Feb-00

// Copyright (c) 2000 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing.tools;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ImageFilePreviewer extends JLabel implements PropertyChangeListener {

	public ImageFilePreviewer(JFileChooser chooser) {
		setPreferredSize(new Dimension(kViewSize, kViewSize));
		chooser.addPropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();
		if (propertyName == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
			File file = (File) e.getNewValue();
			if (file != null) {
				String	name = file.getAbsolutePath();
				icon = new FixedSizeIcon(name, this, kViewSize);
				setIcon(icon);
			}
		}
	}

	private static final int	kViewSize = 144;
	private ImageIcon			icon = null;
}

// LOG
// 2.41 : 20-Feb-00	Y.Shibata	created.