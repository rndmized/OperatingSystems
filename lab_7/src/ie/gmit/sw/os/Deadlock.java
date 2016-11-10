package ie.gmit.sw.os;




public class Deadlock {
  public static void main(String[] args) {
    // These are the two resource objects we'll try to get locks for
    final Object resource1 = "resource1";
    final Object resource2 = "resource2";
    // Here's the first thread.  It tries to lock resource1 then resource2
    Thread t1 = new Thread() {
      public void run() {
        // Lock resource 1
        synchronized(resource1) {
          System.out.println("Thread 1: locked resource 1");

//Pause for a bit, simulating some file I/O or something.  
//Basically, we just want to give the other thread a chance to
//run. Threads and deadlock are asynchronous things, but we're
//trying to force deadlock to happen here...
  
        try { 
			Thread.sleep(50); 
		} 

	catch (InterruptedException e) {}
          
          // Now wait 'till we can get a lock on resource 2
          synchronized(resource2) {
            System.out.println("Thread 1: locked resource 2");
          }
        }
      }
    };
    
    // Here's the second thread.  It tries to lock resource2 then resource1
    Thread t2 = new Thread() {
      public void run() {
        // This thread locks resource 2 right away
        synchronized(resource2) {
          System.out.println("Thread 2: locked resource 2");

                    try { Thread.sleep(50); } catch (InterruptedException e) {}

                    synchronized(resource1) {
            System.out.println("Thread 2: locked resource 1");
          }
        }
      }
    };
    
    // Start the two threads. If all goes as planned, deadlock will occur, 
    // and the program will never exit.
    t1.start(); 
    t2.start();
  }
}
