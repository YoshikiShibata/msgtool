// File: StringDragSource.java - last edit:
// Yoshiki Shibata 9-May-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.dnd;

import java.awt.Component;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StringDragSource implements DragSourceListener, DragGestureListener {

	public StringDragSource(Component component, 
							String methodName,
							Class<?>[] args) {
		this.component = component;
   		dragSource = new DragSource();

   		recognizer = dragSource.createDefaultDragGestureRecognizer(
   					component, // Component 
					DnDConstants.ACTION_COPY_OR_MOVE, // actions
					this); // DragGestureListener
		try {
			dragMethod = component.getClass().getMethod(methodName, args);
		} catch(NoSuchMethodException ex) {
			ex.printStackTrace();
		}
	}
	// ==================================
	// DragGestureListener implementation
	// ==================================
	public void dragGestureRecognized(DragGestureEvent e) {
		try {
			s = (String)dragMethod.invoke(component, new Object[]{});
		} catch(InvocationTargetException ex) {
			ex.printStackTrace();
		} catch(IllegalAccessException iae) {
			iae.printStackTrace();
		}
   		e.startDrag(DragSource.DefaultCopyDrop,  // cursor
					new StringSelection(s), // transferable
					this); // DragSourceListener
   	}
	// ===================================
	// DragSourceListener implementation
	// ===================================
   	public void dragDropEnd(DragSourceDropEvent e) {}
   	public void dragEnter(DragSourceDragEvent e) {}
   	public void dragExit(DragSourceEvent e) {}
   	public void dragOver(DragSourceDragEvent e) {}
   	public void dropActionChanged(DragSourceDragEvent e) {}

	// ===================================
	// Private fields
	// ===================================
 	private Component component;
	private	DragSource dragSource;
	@SuppressWarnings("unused")
	private	DragGestureRecognizer recognizer;
	private	Method dragMethod;
	private	String s;
}
// LOG
// 2.24 :  9-May-99	Y.Shibata	created from "Graphic Java"
