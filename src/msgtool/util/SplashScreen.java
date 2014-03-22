// File: SplashScreen.java - last edit:
// Yoshiki Shibata 27-Feb-00

// Copyright (c) 1997, 1998, 2000 by Yoshiki Shibata

package msgtool.util;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;

public class SplashScreen extends Frame {
    /**
	 * default serialVersionUID. This class is not designed to be serialized.
	 */
	private static final long serialVersionUID = 1L;
	private Window      window  = null;
    private ImageCanvas imageCanvas = null;
    
    public SplashScreen(String imageFileName) {
        Toolkit       toolkit = Toolkit.getDefaultToolkit();
        MediaTracker  tracker = new MediaTracker(this);
        Image         image   = toolkit.getImage(getClass().getResource(imageFileName));

        tracker.addImage(image, 0);
        try { tracker.waitForID(0); } catch (InterruptedException e) {}

        imageCanvas  = new ImageCanvas(image);
        
        window = new Window(this);
        
        window.add(imageCanvas, "Center");
        
        Dimension   screenSize  = toolkit.getScreenSize();
        int         imageWidth  = image.getWidth(this);
        int         imageHeight = image.getHeight(this);
        
        window.setLocation(
                    (screenSize.width - imageWidth) >> 1, 
                    (screenSize.height - imageHeight) >> 1);
        window.setSize(imageWidth, imageHeight);
   }
        
    public void setVisible(boolean visible) {
        if (visible) {
            window.setVisible(true);
            window.toFront();
            imageCanvas.waitForImageDrawn();
        }
        else
            window.dispose();
    }
}
// LOG
// 1.81 :  7-Dec-97 Y.Shibata   created.
// 1.95 : 18-Jul-98 Y.Shibata   moved to msgtool.util
// 2.42 : 27-Feb-00	Y.Shibata   setVisible() now calls the new method of ImageCanvas, waitForImageDrawn().
//                              Therefore, setVisible() waits for completion of drawing the image.
