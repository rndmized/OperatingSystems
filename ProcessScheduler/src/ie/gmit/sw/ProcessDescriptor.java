package ie.gmit.sw;

/* Process Descriptor class stores values relative to a process. 
 * Not inherent to it but required to schedule it */

public class ProcessDescriptor {
	
	private int waitTime;
	private int cycles;
	private int remainder;
	private int startTime = -1;
	
	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getRemainder() {
		return remainder;
	}

	public void setRemainder(int remaninder) {
		this.remainder = remaninder;
	}
	
	public void decreaseRemainder(int amount) {
		this.remainder -= amount;
	}

	public ProcessDescriptor(int wait, int cycles, int remainder){
		this.waitTime = wait;
		this.cycles = cycles;
		this.remainder = remainder;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void addWaitTime(int waitTime) {
		this.waitTime += waitTime;
	}

	public int getCycles() {
		return cycles;
	}

	public void addCycle() {
		this.cycles++;
	}
	
	

}
