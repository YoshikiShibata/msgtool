// File: StringDropTraget.java - last edit:
// Yoshiki Shibata 9-May-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.dnd;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StringDropTarget implements DropTargetListener {

	public StringDropTarget(Component component, 
							String methodName,
							Class<?>[] args) {
		this.component = component;
 		dropTarget = new DropTarget(component, 
							DnDConstants.ACTION_COPY_OR_MOVE,
							this); 
		try {
			dropMethod = component.getClass().getMethod(methodName, args);
		} catch(NoSuchMethodException ex) {
			ex.printStackTrace();
		}
	}
	// =================================
	// DropTragetListener implementation
	// =================================
	public void dragEnter(DropTargetDragEvent e) {
		e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}

	public void drop(DropTargetDropEvent e) {
		try {
			Transferable tr = e.getTransferable();

			if(tr.isDataFlavorSupported(DataFlavor.stringFlavor)){
				e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				String s = (String)tr.getTransferData (DataFlavor.stringFlavor);

				dropMethod.invoke(component, new Object[]{s});
				e.dropComplete(true);
			}		
		} catch(IOException io) {
			io.printStackTrace();
			e.rejectDrop();
		} catch(UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
			e.rejectDrop();
		} catch(IllegalAccessException iae) {
			iae.printStackTrace();	
			e.rejectDrop();
		} catch(InvocationTargetException ite) {
			ite.printStackTrace();
			e.rejectDrop();
		}
	}
	public void dragExit (DropTargetEvent e) { }
	public void dragOver (DropTargetDragEvent e) { }
	public void dropActionChanged (DropTargetDragEvent e) { }
	// =====================
	// Private fields
	// =====================
	private Component component;
	@SuppressWarnings("unused")
	private	DropTarget dropTarget;
	private	Method dropMethod;
}// LOG
// 2.24 :  9-May-99	Y.Shibata	created from "Graphic Java"
