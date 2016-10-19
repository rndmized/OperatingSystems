package ie.gmit.sw;

/* Process Descriptor class stores values relative to a process, 
 * not inherent to it but required to schedule it */

public class ProcessDescriptor {
	/* Total wait time of a single process */
	private int waitTime;
	/* Number of quantum the process ran (Including partial quantum) */
	private int cycles;
	/* The remaining amount of time for a process to complete */
	private int remainder;
	/* The first time the process runs */
	private int startTime = -1;
	
	/* Overloaded constructor
	 * A Process Descriptor requires: 
	 * - How long the process waited, 
	 * - Number of cycles ran,
	 * - The remaining time for the process to finish */
	public ProcessDescriptor(int wait, int cycles, int remainder){
		/* Set variable values to arguments */
		this.waitTime = wait;
		this.cycles = cycles;
		this.remainder = remainder;
	}
	
	/* Return startTime */
	public int getStartTime() {
		return startTime;
	}
	/* Set startTime value to argument */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	/* Return remainder */
	public int getRemainder() {
		return remainder;
	}
	
	/* Decrease remainder by the amount provided as argument */
	public void decreaseRemainder(int amount) {
		this.remainder -= amount;
	}

	/* Return waitTime*/
	public int getWaitTime() {
		return waitTime;
	}
	
	/* Increase waitTime by the amount provided as argument */
	public void addWaitTime(int waitTime) {
		this.waitTime += waitTime;
	}
	
	/* Return number of Cycles */
	public int getCycles() {
		return cycles;
	}
	
	/* Increase cycle by one */
	public void addCycle() {
		this.cycles++;
	}
	
}
