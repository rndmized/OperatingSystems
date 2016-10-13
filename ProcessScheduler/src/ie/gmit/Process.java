package ie.gmit;

public class Process implements Comparable<Process>{
	private int id;
	private int duration;
	
	public Process(int id, int duration){
		
		this.id = id;
		this.duration = duration;
	}
	

	public int getId() {
		return id;
	}
	
	public int getDuration() {
		return duration;
	}

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
