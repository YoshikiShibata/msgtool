// File: FileDropAcceptor - last edit:
// Yoshiki Shibata 11-Jan-03

// Copyrigt (c) 2004 by Yoshiki Shibata

package msgtool.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;

import msgtool.exception.UncaughtExceptionHandler;

public class FileDropper extends DropTargetAdapter {
	private final FileDropAcceptor fFileDropAcceptor;

	public FileDropper(FileDropAcceptor fda) {
		if (fda == null)
			throw new NullPointerException();

		fFileDropAcceptor = fda;
	}
	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent e) {
		try {
			DropTargetContext context = e.getDropTargetContext();
			e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			Transferable t = e.getTransferable();
			Object data = null;

			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				data = t.getTransferData(DataFlavor.javaFileListFlavor);

				if (data instanceof java.util.List) {
					java.util.List<Object> list = (java.util.List<Object>) data;
					for (int i = 0; i < list.size(); i++) {
						Object dataLine = list.get(i);
						if (dataLine instanceof File) {
							fFileDropAcceptor.accept((File) dataLine);
							break;
						}
					}
				}
				
			} else {
				data = t.getTransferData(DataFlavor.stringFlavor);
				if (data instanceof String) {
					fFileDropAcceptor.accept(
						new File(new URI(new URL((String) data).toString())));
				}
			}

			context.dropComplete(true);
		} catch (Exception ex) {
			new UncaughtExceptionHandler().handle(ex);
		}
	}
	
}
// LOG
// 3.50 :  4-Jan-04	Y.Shibata	created
//        11-Jan-04	Y.Shibata	modified to accept String.
