package ie.gmit.sw.os.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ie.gmit.sw.os.operations.Operation;

public class SQLiteJDBC {

	public synchronized boolean login(String username, String pass) {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);

			stmt = c.prepareStatement("SELECT password FROM users WHERE users.username = ? ");

			stmt.setString(1, username);

			String password = null;

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				password = rs.getString("password");
			}
			rs.close();
			stmt.close();
			c.close();
			if (pass.equals(password)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public synchronized boolean signUp(Operation op) {

		if (this.check_account(op) && this.check_username(op.getUser())) {
			this.insert_holder(op);
			this.insert_user(op);
			this.insert_user_holder(op);
			return true;
		} else {
			return false;
		}

	}

	private synchronized boolean insert_holder(Operation op) {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.prepareStatement("INSERT INTO holders (account, name, address) VALUES (?,?,?);");
			stmt.setString(2, op.getName());
			stmt.setString(3, op.getAddress());
			stmt.setInt(1, op.getAccountNumber());
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Operation done successfully");
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	private synchronized boolean insert_user(Operation op) {

		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.prepareStatement("INSERT INTO users (username, password) VALUES (?,?);");
			stmt.setString(1, op.getUser());
			stmt.setString(2, op.getPassword());
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Operation done successfully");
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	private synchronized boolean insert_user_holder(Operation op) {

		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.prepareStatement("INSERT INTO holder_user (account, username) VALUES (?,?);");
			stmt.setInt(1, op.getAccountNumber());
			stmt.setString(2, op.getUser());
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Operation done successfully");
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized void insert_log(int account, String log_message) {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("INSERT INTO log (account, date, action) VALUES (?,?,?);");
			stmt.setInt(1, account);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			stmt.setString(2, dateFormat.format(date));
			stmt.setString(3, log_message);
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	private synchronized boolean check_account(Operation op) {
		System.out.println("Checking Account.");
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("SELECT COUNT(account) as account FROM holders WHERE account = ?");
			stmt.setInt(1, op.getAccountNumber());

			int account = -1;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				account = rs.getInt("account");
				System.out.println(account);
			}

			rs.close();
			stmt.close();
			c.close();

			if (account <= 0) {
				System.out.println("Account available.");
				return true;
			} else {
				System.out.println("Account not available.");
				return false;
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized boolean check_username(String username) {
		System.out.println("Checking Username");
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("SELECT COUNT(username) as username FROM users WHERE username = ?");
			stmt.setString(1, username);

			int user = -1;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				user = rs.getInt("username");
				System.out.println(username);
			}

			rs.close();
			stmt.close();
			c.close();
			if (user <= 0) {
				System.out.println("Username Available");
				return true;
			} else {
				System.out.println("Username Not Available");
				return false;
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized boolean check_password(Operation op) {
		System.out.println("Checking Password");
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("SELECT COUNT(password) as pwd FROM users WHERE password = ? AND username = ?");
			stmt.setString(1, op.getPassword());
			stmt.setString(2, op.getUser());

			int pwd = -1;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				pwd = rs.getInt("pwd");
			}

			rs.close();
			stmt.close();
			c.close();
			if (pwd == 1) {
				System.out.println("Password Match");
				return true;
			} else {
				System.out.println("Password Not Match");
				return false;
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized double getBalance(int account) {

		System.out.println("Checking Balance.");
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("SELECT balance FROM holders WHERE account = ?");
			stmt.setInt(1, account);

			double balance = 0;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				balance = rs.getInt("balance");
				System.out.println(balance);
			}

			rs.close();
			stmt.close();
			c.close();
			return balance;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return -9999;

		}

	}

	public synchronized Operation getAccountDetails(String username) {

		System.out.println("Getting user details.");
		Operation op = new Operation();

		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement(
					"SELECT holders.account, holders.name, holders.address, holders.balance FROM holders JOIN holder_user on holder_user.account = holders.account WHERE username = ?");
			stmt.setString(1, username);

			double balance = 0;
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				op.setAccountNumber(rs.getInt("account"));
				op.setName(rs.getString("name"));
				op.setAddress(rs.getString("address"));
				op.setAmount(rs.getDouble("balance"));
			}

			rs.close();
			stmt.close();
			c.close();
			return op;

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;

		}

	}

	public synchronized boolean updatePassword(String username, String new_pwd) {

		System.out.println("Updating user password.");

		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
			stmt.setString(1, new_pwd);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Returning from update");
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized boolean updateName(int account, String new_name) {

		System.out.println("Updating user´s name.");
		System.out.println("New name: " + new_name + " for Account Number: " + account);

		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("UPDATE holders SET name = ? WHERE account = ?");
			stmt.setString(1, new_name);
			stmt.setInt(2, account);
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Returning from update");
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized boolean updateAddress(int account, String new_address) {

		System.out.println("Updating user´s address.");

		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("UPDATE holders SET address = ? WHERE account = ?");
			stmt.setString(1, new_address);
			stmt.setInt(2, account);
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Returning OK from update");
			return true;
		} catch (Exception e) {
			System.out.println("Returning FAIL from update");
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized boolean updateUsername(String username, String new_username) {

		System.out.println("Updating user username.");

		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("UPDATE users SET username = ? WHERE username = ?");
			stmt.setString(1, new_username);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Returning from update");
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized boolean updateBalance(int account, double amount) {

		System.out.println("Updating user´s balance.");

		Connection c = null;
		PreparedStatement stmt = null;
		String sql = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			stmt = c.prepareStatement("UPDATE holders SET balance = balance + ? WHERE account = ?");
			stmt.setDouble(1, amount);
			stmt.setInt(2, account);
			stmt.executeUpdate();
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Returning from update");
			return true;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}

	}

	public synchronized ArrayList<String> getTransactions(int account){
		ArrayList<String> log_list = new ArrayList<String>();
		
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);

			stmt = c.prepareStatement("SELECT date, action FROM log WHERE log.account = ? ORDER BY date DESC LIMIT 10 ;");
			stmt.setInt(1, account);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String date = rs.getString("date");
				String action = rs.getString("action");
				String log_line = "Date: " + date + ". Transaction: " + action + "\n" ;
				System.out.println(log_line);
				log_list.add(log_line);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
		return log_list;
	}
}