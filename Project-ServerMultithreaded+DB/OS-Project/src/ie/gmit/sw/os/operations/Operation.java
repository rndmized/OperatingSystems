package ie.gmit.sw.os.operations;

import java.io.Serializable;

public class Operation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String op_code;
	private String user;
	private String password;
	private double amount;
	private String name;
	private String address;
	private int accountNumber;
	private String wildcard;
	

	
	
	public String getWildcard() {
		return wildcard;
	}
	public void setWildcard(String wildcard) {
		this.wildcard = wildcard;
	}
	

	
	
	public String getOp_code() {
		return op_code;
	}
	public void setOp_code(String op_code) {
		this.op_code = op_code;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
