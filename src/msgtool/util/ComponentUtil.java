// File: ComponentUtil.java - last edit:
// Yoshiki Shibata 13-Apr-2003

// Copyright (c) 1996 - 1999, 2003 by Yoshiki Shibata. All rights reserved.

// This file is common for all versions: AWT and Swing.

package msgtool.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

public final class ComponentUtil {
    private static Dimension 	SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int WINDOWS_BAR_HEIGHT_OFFSET = 32;
    
    // =============================
    // Centering component
    // =============================
    static public void centerComponent(
        Component   child,
        Point       parentLocation,
        Dimension   parentSize) {
        Dimension   childSize = child.getSize();
  
        if (childSize.width > parentSize.width)
            childSize.width = parentSize.width;
        if (childSize.height > parentSize.height)
            childSize.height = parentSize.height;
        child.setLocation(
            parentLocation.x + parentSize.width / 2 - childSize.width / 2, 
            parentLocation.y + parentSize.height / 2 - childSize.height / 2);
	}
    // =============================
    // AlignComponents
    // =============================
    static public void alignComponents(
        Component   child,
        Component   parent) {
        Point       location = parent.getLocation();
        Dimension   parentSize = parent.getSize();
        Dimension   childSize = child.getSize();
        
        if ((location.x - childSize.width) > 0) {
            location.x -= childSize.width;
            child.setLocation(location);
		} else if ((location.x + parentSize.width + childSize.width) < SCREEN_SIZE.width) {
            location.x += parentSize.width;
            child.setLocation(location);
		} else
            centerComponent(child, location, parentSize);
	}
    //
    // The base line of left component is adjusted to be same to the right component
	//
    static public void alignComponentsBaseHorizontal(
    	Component	left,	
		Component  	right) {
		Rectangle leftBounds = left.getBounds();
		Rectangle rightBounds = right.getBounds();

		leftBounds.y = rightBounds.y + rightBounds.height - leftBounds.height;
		left.setBounds(leftBounds);
	}
    // =============================
    // Fitting component into screen
    // =============================
	static public Point fitComponentInsideScreen(
		Dimension	componentSize,
		Point		location) {
		Point		newLocation = new Point(location.x, location.y);
        
        if (newLocation.x < 0)
            newLocation.x = 0;
        if (newLocation.y < 0)
            newLocation.y = 0;
            
        if ((newLocation.x + componentSize.width) > SCREEN_SIZE.width)
            newLocation.x = SCREEN_SIZE.width - componentSize.width;
        if ((newLocation.y + componentSize.height) > SCREEN_SIZE.height)
            newLocation.y = SCREEN_SIZE.height - componentSize.height;

	 	return(newLocation);
	}

    static public Point fitComponentInsideScreen(
        Component   component,
        Point       location) {
		return(fitComponentInsideScreen(component.getSize(), location));
	}

   	static public Point	fitPopupMenuInsideScreen(
		Component	parentComponent,
		Component	popupMenu,
		Point		relativeLocation) {
		Point		parentLocation = parentComponent.getLocationOnScreen();
		Dimension	preferredSize = popupMenu.getPreferredSize();
		preferredSize.height += WINDOWS_BAR_HEIGHT_OFFSET; 
		Point		absoluteLocation = new Point(parentLocation.x + relativeLocation.x,
											parentLocation.y + relativeLocation.y);
		Point		newAbsoluteLocation = fitComponentInsideScreen(
												preferredSize, 
												absoluteLocation);
 		
		return(new Point(newAbsoluteLocation.x - parentLocation.x,
						 newAbsoluteLocation.y - parentLocation.y));
	}

  	static public void fitComponentIntoScreen(
		Component	component,
		Point		location) {
		Point	newLocation = fitComponentInsideScreen(component, location);

		component.setLocation(newLocation);
	}

   	static public void fitComponentIntoScreen(
		Component	component) {
		fitComponentIntoScreen(component, component.getLocation());
	}
    // ===========================
    // OverlapComponents
    // ===========================
    static public void overlapComponents(
        Component   top,
        Component   bottom,
        int         offset,
        boolean     preserveZeroLocation) {
        //
        // If top's location is (0,0), then adjust the location so that
        // the first appearance will be on the top of the parent frame.
        //
        Point   location = top.getLocation();

        //
        // if preserveZeroLocation is true, it means that location(0,0) is
        // a valid location. [V1.75]
        //
        if (location.x == 0 && location.y == 0 && !preserveZeroLocation) {
            //
            // With Windows 95, if the main frame is iconified, its location()
            // might be out of screen. Therefore, if the its location is out
            // of screen, then position the top component into the center 
            // of the screen.
            //
            location = bottom.getLocation();
            
            if ((location.x > SCREEN_SIZE.width) || (location.y > SCREEN_SIZE.height)) 
                centerComponent(top, new Point(0,0), SCREEN_SIZE);
            else {        
                location.x += offset;
                location.y += offset;
                fitComponentIntoScreen(top, location);
			}
		} else
            fitComponentIntoScreen(top, location);
	}
}
    
// 1.94 :  5-Jul-98 Y.Shibata   created from the old Util.java
// 2.17 : 22-Mar-99	Y.Shibata	added fitPopupMenuInsideScreen
// 2.32 :  5-Jun-99	Y.Shibata	added another fitComponentIntoScreen and alignComponentsBaseHorizontal
// 2.50 : 13-Apr-03	Y.Shibata	refactored.
