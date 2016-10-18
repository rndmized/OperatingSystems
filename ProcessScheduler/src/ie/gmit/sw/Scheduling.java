package ie.gmit.sw;


/*
 * Interface Scheduling defines what scheduling algorithms should be implemented (NOT how).
 *
 ***/

public interface Scheduling {
	
	/* Round Robin - Every process runs a fixed time based on quantum */
	public void roundRobin(int quantum);
	
	/* First come first served */
	public void fCFS();
	
	/* Shortest Job First */
	public void sJF();

}
