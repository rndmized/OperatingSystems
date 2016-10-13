package ie.gmit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Scheduler implements Scheduling {
	
	private ArrayList<Process> queue = new ArrayList<Process>();
	
	public Scheduler(ArrayList<Process> queue) {
		this.queue = queue;
	}

	@Override
	public void roundRobin(int quantum) {
		
		ArrayList<Process> runningQueue = this.queue;
		HashMap<Process, ProcessDescriptor> prc = new HashMap<Process, ProcessDescriptor>();
		
		int waitTime = 0;
		int count = 0;
		float avg=0;
		
		for (int i = 0; i < runningQueue.size(); i++) {
			prc.put(runningQueue.get(i), new ProcessDescriptor(waitTime, 0, runningQueue.get(i).getDuration()));
		}

		
		while(count < runningQueue.size()){
			for (int i = 0; i < runningQueue.size(); i++) {
				if (prc.get(runningQueue.get(i)).getStartTime() == -1){
					prc.get(runningQueue.get(i)).setStartTime(waitTime);
				}
				if(prc.get(runningQueue.get(i)).getRemaninder() > 0){
					if(prc.get(runningQueue.get(i)).getRemaninder() < quantum){					
						prc.get(runningQueue.get(i)).addWaitTime((waitTime - prc.get(runningQueue.get(i)).getWaitTime()));
						waitTime += quantum -(quantum-prc.get(runningQueue.get(i)).getRemaninder());
						prc.get(runningQueue.get(i)).decreaseRemaninder(quantum -(quantum-prc.get(runningQueue.get(i)).getRemaninder()));
						count++;
					} else if(prc.get(runningQueue.get(i)).getRemaninder() >= quantum){	
						prc.get(runningQueue.get(i)).addCycle();
						prc.get(runningQueue.get(i)).addWaitTime((waitTime - prc.get(runningQueue.get(i)).getWaitTime()));
						waitTime += quantum;
						prc.get(runningQueue.get(i)).decreaseRemaninder(quantum);
						if(prc.get(runningQueue.get(i)).getRemaninder() == 0){
							count++;
							
						}
						//System.out.println(waitTime);
					}
				}
			}
			
		}
		for (int i = 0; i < runningQueue.size(); i++) {
			avg += prc.get(runningQueue.get(i)).getWaitTime() - (prc.get(runningQueue.get(i)).getCycles() * quantum);
					
			System.out.println("P"+runningQueue.get(i).getId() + " Wait Time: " + prc.get(runningQueue.get(i)).getWaitTime());	
			//System.out.println("P"+runningQueue.get(i).getId() + " Start Time: " + prc.get(runningQueue.get(i)).getStartTime());
			//System.out.println("P"+runningQueue.get(i).getId() + " Cycles: " + prc.get(runningQueue.get(i)).getCycles());
		}
		avg /= runningQueue.size();
		System.out.println(avg);

		
	}
		
	
	
	@Override
	public void fCFS(){
		this.fCFS(this.queue);
	}

	private void fCFS(ArrayList<Process> queue) {
		
		int waitTime=0;
		float avg =0;
		int[] wait = new int[queue.size()];
		

		for ( int i = 0; i < queue.size(); i++) {
			if ( i == 0 ){
				waitTime += 0;
				System.out.println(queue.get(i).getId()+ ": " +waitTime);
				wait[i] = waitTime;

			} else {
				waitTime += queue.get(i-1).getDuration();
				wait[i]=waitTime;
				System.out.println(queue.get(i).getId()+ ": " +waitTime);

			}

		}
		for ( int i = 0; i < wait.length; i++) {
			avg += wait[i];
		}

		avg /= queue.size();
		System.out.println(avg);
	}

	@Override
	public void sJF() {
		
		ArrayList<Process> sortedQueue = this.queue;
		Collections.sort(sortedQueue);
		this.fCFS(sortedQueue);

	}

}
