package ie.gmit.sw;

/* 
 * Process class implements Comparable to itself based on duration. Its attributes are id and duration.
 *
 **/

public class Process implements Comparable<Process>{
	
	private int id; /* Process ID number */
	private int duration; /* Process duration */
	
	/* Process constructor. In order to create a process and ID and its duration must be provided */
	public Process(int id, int duration){
		this.id = id;
		this.duration = duration;
	}
	

	/* Returns int ID */
	public int getId() {
		return id;
	}
	/* Returns int duration */
	public int getDuration() {
		return duration;
	}

	/* Implementation of Comparable */
	@Override
	public int compareTo(Process o) {
		if (this.duration < o.duration){
			return -1;
		} else if (this.duration > o.duration){
			return 1;
		}else{
			return 0;
		}
	}

}
