package ie.gmit.sw.os.operations;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String op_code;
	private String status;
	private double balance;
	private Operation operation;
	private ArrayList<String> list;
	

	public ArrayList<String> getList() {
		return list;
	}
	public void setList(ArrayList<String> list) {
		this.list = list;
	}
	
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	
	public String getOp_code() {
		return op_code;
	}
	public void setOp_code(String op_code) {
		this.op_code = op_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

}
