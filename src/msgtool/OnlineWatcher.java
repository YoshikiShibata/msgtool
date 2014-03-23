/*
 * File: OnlineWatcher.java - last edit:
 * Yoshiki Shibata 22-Aug-2004
 *
 * Copyright (c) 1997, 1998, 2004 by Yoshiki Shibata. All rights reserved.
 */
package msgtool;

import java.util.Observable;
import java.util.Observer;

import msgtool.common.SortUtil;
import msgtool.db.AddressDB;
import msgtool.db.PropertiesDB;
import msgtool.protocol.MiscProtocol;
import msgtool.ui.OnlineListUI;

public final class OnlineWatcher extends Thread implements Observer {

	private OnlineListUI onlineListUI = null;

	private volatile boolean addressDBUpdated = false;

	public OnlineWatcher(OnlineListUI onlineListUI) {
		this.onlineListUI = onlineListUI;
		AddressDB.instance().addObserver(this);
	}

    @Override
	public void run() {
		AddressDB addressDB = AddressDB.instance();
		PropertiesDB propertiesDB = PropertiesDB.getInstance();
		MiscProtocol miscProtocol = MiscProtocol.getInstance();

		while (true) {
			String[] recipients = null;

			// When this tool is installed for the first time, recipients is
			// empty. So wait for any change of AddressCache.
			synchronized (this) {
				addressDBUpdated = false;
				while ((recipients = addressDB.getListOfAddressCache()).length == 0) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
			}

			// Sort recipients by sort key so that recipients will be checked
			// from the top in the OnLine list. [V1.76]
			SortUtil.sortStringsBySortKey(recipients);

			onlineListUI.clearList();

			// Until the AddressDB is updated, loop probing.
			while (!addressDBUpdated) {
				for (String recipient : recipients) {
					if (miscProtocol.probe(propertiesDB.getUserName(),
							recipient) != null)
						onlineListUI.setOnline(recipient);
					else
						onlineListUI.setOffline(recipient);

					if (addressDBUpdated)
						break;
				}

				if (!addressDBUpdated) {
					synchronized (this) {
						try {
							wait(300 * 1000); // 5 minutes wait.
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}
	}

	public synchronized void update(Observable observable, Object obj) {
		addressDBUpdated = true;
		notify();
	}
}

// LOG
// 1.41 :  4-Aug-97 Y.Shibata created
// 1.44 :  5-Aug-97 Y.Shibata changed interval from 3 min to 5 min
// 1.55 :  8-Aug-97 Y.Shibata fixed a bug (nullPointerException) which is caused
// 							  when this tool is installed for the first time.
// 1.76 : 13-Nov-97 Y.Shibata sort recipients by sort key.
// 2.19 : 27-Mar-99 Y.Shibata used OnlineLister interface
// 2.35 :  6-Oct-99 Y.Shibata used OnlineListUI instead of OnlineLineLister
// 2.52 : 22-Aug-04	Y.Shibata refactored.
