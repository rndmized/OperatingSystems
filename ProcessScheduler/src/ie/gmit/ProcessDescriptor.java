package ie.gmit;

public class ProcessDescriptor {
	
	private int waitTime;
	private int cycles;
	private int remaninder;
	private int startTime = -1;
	
	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getRemaninder() {
		return remaninder;
	}

	public void setRemaninder(int remaninder) {
		this.remaninder = remaninder;
	}
	
	public void decreaseRemaninder(int amount) {
		this.remaninder -= amount;
	}

	public ProcessDescriptor(int wait, int cycles, int remainder){
		this.waitTime = wait;
		this.cycles = cycles;
		this.remaninder = remainder;
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
