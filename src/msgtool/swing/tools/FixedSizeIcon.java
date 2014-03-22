// File: FixedSizeIcon.java - last edit:
// Yoshiki Shibata 20-Feb-99

// Copyright (c) 2000 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing.tools;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class FixedSizeIcon extends ImageIcon {

	public FixedSizeIcon(String imageFileName, Component c, int size) {
		super(imageFileName);
		scaleImage(c, size);
	}

	public FixedSizeIcon(Image image, Component c, int size) {
		super(image);
		scaleImage(c, size);
	}

	private void scaleImage(Component c, int size) {
		Image image = c.createImage(size, size);
		Graphics g = image.getGraphics();

		if (g != null) {
			g.drawImage( getImage(), 0, 0, size, size, c);
			setImage(image);
			g.dispose();
	 	} else
			throw new IllegalArgumentException();
	}
}

// LOG
// 2.41 : 20-Feb-00	Y.Shibata	created.