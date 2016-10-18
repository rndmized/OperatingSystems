package ie.gmit.sw;

import java.util.ArrayList;

import ie.gmit.sw.gui.InputFrame;
import ie.gmit.sw.gui.ProcessInputFrame;

public class Tasker {
	
	private Process p0 = new Process(0, 20);
	private Process p1 = new Process(1, 2);
	private Process p2 = new Process(2, 13);
	private Process p3 = new Process(3, 12);
	private Process p4 = new Process(4, 1);
	private Process p5 = new Process(5, 9);
	
	private ArrayList<Process> queue = new ArrayList<Process>();
	
	public Tasker(){
		
		queue.add(p0);
		queue.add(p1);
		queue.add(p2);
		queue.add(p3);
		queue.add(p4);
		queue.add(p5);

		
	}

	public static void main(String[] args) {
		
		//InputFrame pif = new InputFrame();
		//pif.setVisible(true);
		
		
		Tasker tsk = new Tasker();
		Scheduler sch = new Scheduler(tsk.queue);
		
		sch.fCFS();
		//sch.sJF();
		//sch.roundRobin(5);
		
		

	}

}
