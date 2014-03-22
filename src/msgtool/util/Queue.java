// File: Queue.java - last edit:
// Yoshiki Shibata 27-Dec-03

// Copyright (c) 1997 - 2000, 2003 by Yoshiki Shibata. All rights reserved.

package msgtool.util;

import java.util.ArrayList;

public final class Queue<E> {
    private ArrayList<E> fQueue = new ArrayList<E>();
      
    public synchronized void put(E  obj) {
        fQueue.add(obj);
        notifyAll();
   	}
        
    public synchronized E get() {
        while (fQueue.isEmpty()) {
            try {
                wait();
           	} catch (InterruptedException e) {}
      	}

        return fQueue.remove(0);
   	}
}

// LOG
// 1.70 : 18-Oct-97 Y.Shibata   created
// 1.95 : 12-Jul-98 Y.Shibata   moved to msgtool.util
// 2.35 :  7-Nov-99	Y.Shibata	modified.
// 2.40 :  4-Jan-00	Y.Shibata	changed names of methods to lower-case.
// 2.50 : 27-Dec-03 Y.Shibata   rewritten with Java Generics
