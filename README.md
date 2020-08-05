# Virtual Threads for Jetty
Enables Jetty to use project loom's virtual threads instead of plain old java threads.

1. copy jetty-threadpool.xml into ${JETTY_BASE}/etc
2. copy vt4jetty-1.0-SNAPSHOT.jar into ${JETTY_BASE}/lib/ext

if everything is set up properly you should see "VirtualThreadExecutor is active." in the console when the server starts.
