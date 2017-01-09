package ie.gmit.sw.os.server;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ie.gmit.sw.os.db.SQLiteJDBC;
import ie.gmit.sw.os.operations.Operation;
/**
 * This class controls access to the SQLite Connection
 * @author RnDMizeD
 * @version 1.1
 */
public class Monitor {

	private SQLiteJDBC sqlConnector;
	private Lock lock;

	/**
	 * Default Constructor
	 */
	public Monitor() {
		sqlConnector = new SQLiteJDBC();
		lock = new ReentrantLock();
	}

	/**
	 * This method compares log in details.
	 * @param username Name of the user trying to log in.
	 * @param pass Password for this user name.
	 * @return True or False whether the values match.
	 */
	public boolean login(String username, String pass) {
		boolean login_check;
		synchronized (lock) {
			login_check = sqlConnector.login(username, pass);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return login_check;

	}

	/**
	 * This method creates a new user in the database.
	 * @param op Details to be stored in the database.
	 * @return true or false whether the user is successfully added or not.
	 */
	public boolean signup(Operation op) {
		boolean signup_check;
		synchronized (lock) {
			signup_check = sqlConnector.signUp(op);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return signup_check;

	}

	/**
	 * This method checks whether the user name is available or not.
	 * @param username User name to be checked.
	 * @return true or false whether the user name is available or not.
	 */
	public boolean checkUsername(String username) {
		boolean username_check;
		synchronized (lock) {
			username_check = sqlConnector.check_username(username);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return username_check;

	}

	/**
	 * This method check if the password match the one in the database.
	 * @param op Details of user whose password is being checked.
	 * @return true or false whether the password matches.
	 */
	public boolean check_password(Operation op) {
		boolean password_check;
		synchronized (lock) {
			password_check = sqlConnector.check_password(op);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return password_check;
	}
	/**
	 * This method updates password of a user name in the database.
	 * @param username user whose password is to change.
	 * @param new_pwd new password for that user name.
	 * @return true or false whether the change succeeds.
	 */
	public boolean updatePassword(String username, String new_pwd) {
		boolean password_check;
		synchronized (lock) {
			password_check = sqlConnector.updatePassword(username, new_pwd);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return password_check;

	}

	/**
	 * This method updates the name of a user.
	 * @param account Account of the user.
	 * @param new_name New name for the user.
	 * @return true or false whether the change succeeds.
	 */
	public boolean updateName(int account, String new_name) {
		boolean name_check;
		synchronized (lock) {
			name_check = sqlConnector.updateName(account, new_name);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return name_check;

	}
	/**
	 * This method updates the address of a user.
	 * @param account Account of the user whose address is to be updated.
	 * @param new_address New address for the account.
	 * @return true or false whether the change succeeds.
	 */
	public boolean updateAddress(int account, String new_address) {
		boolean address_check;
		synchronized (lock) {
			address_check = sqlConnector.updateAddress(account, new_address);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return address_check;

	}
	/**
	 * This method updates the balance of an account.
	 * @param account Account whose balance is to be updated.
	 * @param amount Amount of credit to be updated.
	 * @return true or false whether the update succeeds.
	 */
	public boolean updateBalance(int account, double amount) {
		boolean balance_check;
		synchronized (lock) {
			balance_check = sqlConnector.updateBalance(account, amount);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return balance_check;

	}
	/**
	 * @deprecated This method is unsafe for use due to changes in the database.
	 * This method updates the user name of a user.
	 * @param username User name to be updated.
	 * @param new_username New user name.
	 * @return true or false whether the change succeeds.
	 */
	public boolean updateUsername(String username, String new_username) {
		boolean username_check;
		synchronized (lock) {
			username_check = sqlConnector.updateUsername(username, new_username);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return username_check;

	}
	/**
	 * This method inserts a log text into the database.
	 * @param account Account whose log belongs to
	 * @param log_message Message to be inserted as a log information.
	 */
	public void insertLog(int account, String log_message) {

		synchronized (lock) {
			sqlConnector.insert_log(account, log_message);;
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}

	}
	/**
	 * This method retrieve the balance of an account.
	 * @param account Account whose balance is to be retireved.
	 * @return Balance of an account.
	 */
	public double getBalance(int account) {
		double balance;
		synchronized (lock) {
			balance = sqlConnector.getBalance(account);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return balance;

	}
	/**
	 * This method returns the details of an account.
	 * @param username User name of the user whose details are to be returned. 
	 * @return Oparation containing the details of a user.
	 */
	public Operation getAccountDetails(String username) {
		Operation details;
		synchronized (lock) {
			details = sqlConnector.getAccountDetails(username);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return details;

	}
	/**
	 * This method returns the last ten transactions stored in the log of an account.
	 * @param account Account whose transactions are to be retrieved.
	 * @return ArrayList of Strings containing transactions details.
	 */
	public ArrayList<String> getTransactions(int account) {
		ArrayList<String> transactionList;
		synchronized (lock) {
			transactionList = sqlConnector.getTransactions(account);
		}
		try {
			lock.notify();
			lock.wait();
		} catch (Exception e) {

		}
		return transactionList;

	}

	
}
