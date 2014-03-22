// File: XThreadGroup.java - last edit:
// Yoshiki Shibata 18-Jul-99

// Copyright (c) 1999 by Yoshiki Shibata. All rights reserved.

package msgtool.exception;

//
// This is an eXtended ThreadGroup which is desgined to catch uncaught exception.
//
/*
	[HOW TO USE]

	class Application implements Runnable {
		private String[]			myArgs;
		private static XThreadGroup	threadGroup;

		Application(Strings[] args) {
			myArgs = args; // save args		
		}

		static public void main(Strings[] args) {
			//
			// Install a handler to catch uncaught exceptions for AWT Event
			//
			UncaughtExceptionHandler.installForAWTEvent();
			//
			// create an eXtended ThreadGroup so that any uncaught exception
			// caused by threads which are created by this application will
 		    // be handled.
			//
			Application app = new Applications(args);
			threadGroup = XThreadGroup("myApp");
			Thread	thread = new Thread(threadGroup, app);
			thread.start();
		}
		
		public void run() {
			realMain(myArgs);	
		}

		private void realMain(Strings[] args) {
			// Implement the original main code here
		}
	}

*/
public class XThreadGroup extends ThreadGroup {

	public XThreadGroup(String name) {
		super(name);
	}

	public void uncaughtException(Thread thread, Throwable e) {
		UncaughtExceptionHandler handler = new UncaughtExceptionHandler();
		handler.handle(e);
	}
}

// LOG
// 2.32 : 17-Jul-99	Y.Shibata	created. 
