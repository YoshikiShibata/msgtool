// File: ImageCanvas.java - last edit:
// Yoshiki Shibata 27-Feb-00

// Copyright (c) 1997, 1998, 2000 by Yoshiki Shibata

package msgtool.util;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

public class ImageCanvas extends Canvas {
    /**
	 * default serialVersionUID. This class is not designed to be serialized.
	 */
	private static final long serialVersionUID = 1L;
	private Image   image = null;
	private boolean imageDrawn = false;
    
    public ImageCanvas() {}
    
    public ImageCanvas(Image image) {
        setImage(image);
	}
        
    public synchronized void paint(Graphics g) {
        if (image != null) {
            imageDrawn = g.drawImage(image, 0, 0, this);
			if (imageDrawn)
				notifyAll();
		}
	}

	public synchronized void waitForImageDrawn() {
		try {
			while (!imageDrawn)
				wait();
	 	} catch (InterruptedException e) {}
	}
        
    public void setImage(Image image) {
        waitForImage(this, image);
        
        this.image = image;
        
        setSize(image.getWidth(this), image.getHeight(this));
        
        if (isShowing())
            repaint();
	}
        
    public Dimension getPreferredSize() {
        if (image != null) 
            return(new Dimension(image.getWidth(this), image.getHeight(this)));
        else
            return(new Dimension(0,0));
	}
        
    private void waitForImage(
        Component   component,
        Image       image) {
        MediaTracker tracker    = new MediaTracker(component);
        try {
            tracker.addImage(image, 0);
            tracker.waitForID(0);
		} catch (InterruptedException e) {}
	}
}
// LOG
// 1.81 :  7-Dec-97 Y.Shibata   created
// 1.95 : 18-Jul-97 Y.Shibata   moved to msgtool.util
// 2.42 : 27-Feb-00	Y.Shibata	added watiForImageDrawn() to wait for the completion of drawing the image.
