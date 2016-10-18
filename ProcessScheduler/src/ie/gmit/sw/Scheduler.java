package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/*
 * Scheduler class Implements Scheduling and implements the methods defined in such interface.
 * A scheduler takes a list of processes (ArrayList) and applies the requested scheduling algorithm on it
 * */
public class Scheduler implements Scheduling {
	
	/* ArrayList queue contains in order the number of processes to be scheduled */
	private ArrayList<Process> queue;
	
	/* 
	 * Overloaded Constructor.
	 * In order to instantiate a Scheduler, an ArrayList of Processes is required.
	 * */
	public Scheduler(ArrayList<Process> queue) {
		/* Class variable queue gets assigned the value of parameter queue */
		this.queue = queue;
	}

	
	/* Implementation of roundRobin */
	@Override
	public void roundRobin(int quantum) {
		
		/* HashMap links processes with a ProcessDescriptor, a class that stores values and information relevant
		 * to the scheduler (but irrelevant for the process itself))
		 * */
		HashMap<Process, ProcessDescriptor> prc = new HashMap<Process, ProcessDescriptor>();
		/* Local variable waitTime stores the processes wait time and increments every iteration accordingly */
		int waitTime = 0;
		/* Local variable count keeps track of finalized processes */
		int count = 0;
		/* Local variable avg represents the average wait time for all processes */
		float avg=0;
		
		System.out.println("Round Robbin");
		/* While the number of processes finished is smaller than the total number of processes */
		while(count < queue.size()){ 
			/* For every process in the queue */
			for (int i = 0; i < queue.size(); i++) {
				/* If the process is not in the HashMap, add it and attach a new process descriptor */
				if(!(prc.containsKey(queue.get(i)))){
					prc.put(queue.get(i), new ProcessDescriptor(waitTime, 0, queue.get(i).getDuration()));
				}
				/* If the start time of process (i) is not initialized, set it to current wait time */
				if (prc.get(queue.get(i)).getStartTime() == -1){
					prc.get(queue.get(i)).setStartTime(waitTime);
				}
				/* Check process remainder. If it is > 0 it still needs allocated time */
				if(prc.get(queue.get(i)).getRemainder() > 0){
					/* If remainder is < than quantum */
					if(prc.get(queue.get(i)).getRemainder() < quantum){
						/* Increment wait time since last time it ran */
						prc.get(queue.get(i)).addWaitTime((waitTime - prc.get(queue.get(i)).getWaitTime()));
						/* Increment local variable wait time the amount of time allocated to this process to run */
						waitTime += quantum -(quantum-prc.get(queue.get(i)).getRemainder());
						/* Add another quantum cycle to the process */
						prc.get(queue.get(i)).addCycle();
						/* Decrease the remainder of the process */
						prc.get(queue.get(i)).decreaseRemainder(quantum -(quantum-prc.get(queue.get(i)).getRemainder()));
						/* Since the process is finished increment count to indicate a process from the list has finished. */
						count++;
						/* Else if the  remainder of the process is > than the quantum or equal */
					} else if(prc.get(queue.get(i)).getRemainder() >= quantum){
						/* Add another quantum cycle to the process */
						prc.get(queue.get(i)).addCycle();
						/* Increment wait time since last time it ran */
						prc.get(queue.get(i)).addWaitTime((waitTime - prc.get(queue.get(i)).getWaitTime()));
						/* Increment local variable wait time the amount of time allocated to this process to run */
						waitTime += quantum;
						/* Decrease the remainder of the process */
						prc.get(queue.get(i)).decreaseRemainder(quantum);
						/* If after substracting the quantum to the remainder it is equal to 0. The process is finished */
						if(prc.get(queue.get(i)).getRemainder() == 0){
							/* Since the process is finished increment count to indicate a process from the list has finished. */
							count++;					
						}
					}
				}
			}	
		}
		/* For every process in the list. Increment Average (avg)*/
		for (int i = 0; i < queue.size(); i++) {
			avg += prc.get(queue.get(i)).getWaitTime() - ((prc.get(queue.get(i)).getCycles()-1) * quantum);		
			//System.out.println("P"+queue.get(i).getId() + " Wait Time: " + prc.get(queue.get(i)).getWaitTime());	
			System.out.println("P"+queue.get(i).getId() + " Start Time: " + prc.get(queue.get(i)).getStartTime());
			//System.out.println("P"+queue.get(i).getId() + " Cycles: " + prc.get(queue.get(i)).getCycles());
		}
		/* Divide avg bu the number of processes*/
		avg /= queue.size();
		System.out.println(avg);	
	}
		
	
	/* Implementation of First Come First Served */
	@Override
	public void fCFS(){
		System.out.println("First come first served");
		/* Call fCFS function with class variable (ArrayList) queue */
		this.fCFS(this.queue);
		
	}
	/* Implementation of First Come First Served taking an ArrayList of Processes as an argument */
	private void fCFS(ArrayList<Process> queue) {
		
		/* Local variable waitTime stores the processes wait time and increments every iteration accordingly */
		int waitTime=0;
		/* Local variable avg represents the average wait time for all processes */
		float avg =0;
		/* Local array of integers stores the wait times for every process */
		int[] wait = new int[queue.size()];
		
		/* For every process in the queue */
		for ( int i = 0; i < queue.size(); i++) {
			/* If it is the first process */
			if ( i == 0 ){
				/* Since the first process didn't had to wait add 0 the the wait time */
				waitTime += 0;
				System.out.println(queue.get(i).getId()+ ": " +waitTime);
				/* Set wait time from process (i) to waitTime */
				wait[i] = waitTime;

			} else {
				/* Increment the wait time equal to the duration of the previous process */
				waitTime += queue.get(i-1).getDuration();
				/* Set wait time from process (i) to waitTime */
				wait[i]=waitTime;
				System.out.println(queue.get(i).getId()+ ": " +waitTime);

			}
			/* Average increments with every wait time */
			avg += wait[i];
		}
		/* Divide avg by the number of processes in the list to obtain average */
		avg /= queue.size();
		System.out.println(avg);
	}

	@Override
	public void sJF() {
		System.out.println("Shortest Job First");
		/* Create ArrayList and assign value of a clone of queue 
		 * (CLoning is required to avoid changes on the main list)*/
		@SuppressWarnings("unchecked")
		ArrayList<Process> sortedQueue = (ArrayList<Process>) this.queue.clone();
		/* Sort sortedQueue using Collection.sort method */
		Collections.sort(sortedQueue);
		/* Once the list is sorted the algorithm is the same as the fCFS.
		 * Call fCFS function passing the sorted queue as an argument.*/
		this.fCFS(sortedQueue);
	}

}
