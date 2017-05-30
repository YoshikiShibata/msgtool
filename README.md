# MessagingTool

MessagingTool is a simple so-called chat tool. But its origin goes way back to 1980s. Originally MessagingTool was implemented in **Mesa** language and run on top of **XDE** (Xerox Development Environment), using the  **XNS** (Xerox Network Systems) protocol.

In 1991, I implemented another MessagingTool in C language which supported both the XNS protocol and TCP/IP protocol: actually this C version used TCP/IP-based Sun's RPC. This C version could be used to exchange messages with the Mesa version becuase it supported the XNS protocol.

In Summer, 1996, I started to learn Java language and implemented another MessageTool in Java. This Java version is not compatible with the Mesa and C versions: this Java version uses TCP/IP but not Sun'S RPC.  Originally, the Java version  was implemented with Java 1.02 which didn't support inner class. 
