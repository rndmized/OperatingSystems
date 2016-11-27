package ie.gmit.sw.os;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RunnerClass {
	//Creating the lock
	static Lock lock = new ReentrantLock();
	//Creating the finish condition
	private static boolean finish = false;
	
	public static void main(String[] args) {
		//Thread 1
		Thread requester = new Thread(new Runnable() {
			public void run() {
				//While is not finished
				while (!finish) {
					//Lock the lock
					synchronized (lock) {
						//Print question
						System.out.println("What city are we in?");
	                    	try {
	                    		//Unlock the lock
	                    		lock.notify();
	                            lock.wait();
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	        });
			//Thread 2
	        Thread responser = new Thread(new Runnable() {
	            public void run() {
	            	//While is not finished
	            	while (!finish) {
	            		//Lock the lock
	                    synchronized (lock) {
	                    	//Print question
	                        System.out.println("Galway!");
	                        try {
	                        	//Unlock the lock
	                            lock.notify();
	                            lock.wait();
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	        });
	        
	        try {
	        	//Give thread one higher priority
	        	requester.setPriority(Thread.NORM_PRIORITY+1);
	        	//Start Threads
	            requester.start();
	            responser.start();
	            //Main thread sleeps for ten seconds
	            Thread.sleep(10000);
	        } catch (Exception e) {}
	        //Set condition to true
	        finish = true;
	        try {
	        	//Tell the threads to stop
	        	requester.join();
	            responser.join();
			} catch (Exception e) {
				// TODO: handle exception
			}
	    }
}
