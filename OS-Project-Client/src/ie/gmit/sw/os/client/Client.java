package ie.gmit.sw.os.client;
/*
 * This class contains gui and methods to access and modify server information
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import ie.gmit.sw.os.operations.Operation;
import ie.gmit.sw.os.operations.Response;

public class Client {
	/*
	 * Declaring Variables
	 */
	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message = "";
	private String ipaddress;
	private Scanner stdin;

	private volatile Operation profile;
	private Response res;

	private volatile boolean finish = false;

	/* This method connects to the server for sending data back and forth */
	/**
	 * 
	 */
	void run() {
		stdin = new Scanner(System.in);
		try {
			// 1. creating a socket to connect to the server
			System.out.println("Please Enter your IP Address");
			System.out.println("127.0.0.1");
			ipaddress = stdin.next();
			requestSocket = new Socket(ipaddress, 2004);
			System.out.println("Connected to " + ipaddress + " in port 2004");
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());

			// 3: Communicating with the server
			try {
				message = (String) in.readObject();
				System.out.println(message);
			} catch (Exception e) {
			}

			/* If login returns true allow access to main menu */
			if (this.login()) {
				do {
					/* Save users details from response from server */
					profile = res.getOperation();
					finish = this.menu();
				} while (!finish);
			}
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	/* This method takes a string and sends it to the server */
	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/* This method takes a operation object and sends it to the server */
	/* This method takes an operation object and sends it to the server */
	void sendOperation(Operation operation) {
		try {
			out.writeObject(operation);
			out.flush();
			System.out.println(
					"client> Operation: " + operation.getOp_code() + " sent. Amount: " + operation.getAmount());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/* This method displays a menu for the user to sign up or log in */

	private boolean login() {
		boolean login = false;

		Operation op = new Operation();

		do {

			/* Print Menu */
			System.out.println("Please select an option to proceed: ");
			System.out.println("1. Login.");
			System.out.println("2. Sign Up.");

			/* Take user input */
			String status = null;
			String choice = null;
			String input = null;
			choice = stdin.next();

			/* Menu */
			/* If user choose Log in */
			if (choice.equals("1")) {
				/* take user input and set operation fields accordingly */
				System.out.println("Logging In.");
				op = new Operation();
				op.setOp_code("LOGIN");
				System.out.println("Enter user: ");
				input = stdin.next();
				op.setUser(input);
				System.out.println("Enter password: ");
				input = stdin.next();
				op.setPassword(input);
				/* Send Operation */
				sendOperation(op);
				/* Wait Response */
				try {
					res = (Response) in.readObject();
					System.out.println(res.getStatus());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				/* Check response status, if valid */
				if (res.getStatus().equals("LOG_OK")) {
					/* Allow login */
					login = true;
					/* Print Balance */
					System.out.println("Balance: " + res.getBalance() + "€");
				} else {
					op = null;
				}
				/* If user choose sign up */
			} else if (choice.equals("2")) {
				/* Take user Input and set operation values accordingly */
				System.out.println("Signing Up.");
				op.setOp_code("SIGNUP");
				System.out.println("Enter Name: ");
				input = stdin.next();
				op.setName(input);
				System.out.println("Enter Address: ");
				input = stdin.next();
				op.setAddress(input);
				System.out.println("Enter Account Number: ");
				input = stdin.next();
				op.setAccountNumber(Integer.parseInt(input));
				System.out.println("Enter username: ");
				input = stdin.next();
				op.setUser(input);
				System.out.println("Enter password: ");
				input = stdin.next();
				op.setPassword(input);
				/* Send Operation to server */
				sendOperation(op);
				/* Wait Response */
				try {
					status = (String) in.readObject();
					System.out.println(status);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				/* Check response status, if valid */
				if (status.equals("SIGN_OK")) {
					System.out.println("Signed Up Correctly");
				}
			}
			/* Repeat loop until user logs */
		} while (!login);
		/*
		 * when login = true return true allowing the user to access main menu
		 */
		return true;
	}

	/* Main Menu */

	/* This method displays the main menu */
	private boolean menu() {
		boolean menu = true;
		String choice = null;

		while (menu) {

			System.out.println(profile.getName() + ", Welcome to Internet Banking Account System");
			System.out.println("Please, select an option to proceed:");
			System.out.println("1. Change Details.");
			System.out.println("2. Make Lodgement.");
			System.out.println("3. Make Withdrawal.");
			System.out.println("4. View last 10 transactions.");
			System.out.println("5. Exit.");
			choice = stdin.next();

			if (choice.equals("1")) {
				// Change Details
				this.detailsMenu();
			} else if (choice.equals("2")) {
				// Lodgement
				if (this.makeLodgement(profile.getAccountNumber())) {
					System.out.println("Lodgment OK");
				}

			} else if (choice.equals("3")) {
				// Withdrawal
				if (this.makeWithdrawal(profile.getAccountNumber())) {
					System.out.println("Withdrawal OK");
				}

			} else if (choice.equals("4")) {
				// Last 10 trans
				this.getTransactions(profile.getAccountNumber());
			} else if (choice.equals("5")) {
				// Exit
				menu = false;
				sendMessage("FIN");
			}
		}
		return true;
	}

	/*
	 * This method retrieves a list with the last ten transactions and prints
	 * its contents
	 */
	private boolean getTransactions(int account) {
		/* Declaring Instances */
		Operation op = new Operation();
		Response response = new Response();
		/* Set operation values */
		op.setAccountNumber(account);
		op.setOp_code("LOG");
		/* Send Operation to server */
		sendOperation(op);
		/* Wait Response */
		try {
			response = (Response) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		/* Print a log line for every line in list */
		for (String string : response.getList()) {
			System.out.println(string);
		}
		return true;
	}

	/*
	 * This function send operation to change name based on account number
	 */
	private boolean changeName(int account) {
		/* Declaring Instances */
		Operation op = new Operation();
		Response response = new Response();

		System.out.println("Enter new name: ");
		String new_name = stdin.next();
		/* Set operation values */
		op.setName(new_name);
		op.setAccountNumber(account);
		op.setOp_code("DET_NME");
		/* Send Operation to server */
		sendOperation(op);
		/* Wait Response */
		try {
			response = (Response) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * Depending on response display message and set new values to profile
		 */
		if (response.getStatus().equals("DET_NME_OK")) {
			System.out.println("Name changed successfully.");
			profile.setName(op.getName());
			return true;

		} else {

			System.out.println("Name change unsuccessful.");
			return false;
		}

	}
	/*
	 * This function send operation to change address based on account number
	 */

	/* This method changes the user´s address for a given account */
	private boolean changeAddress(int account) {
		/* Declaring Instances */
		Operation op = new Operation();
		Response response = new Response();

		System.out.println("Enter new Address: ");
		String new_address = stdin.next();
		/* Set operation values */
		op.setAddress(new_address);
		op.setAccountNumber(account);
		op.setOp_code("DET_ADDR");
		/* Send Operation to server */
		sendOperation(op);
		/* Wait Response */
		try {
			response = (Response) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * Depending on response display message and set new values to profile
		 */
		if (response.getStatus().endsWith("_OK")) {
			System.out.println("Address changed successfully.");
			profile.setAddress(op.getAddress());
			return true;
		} else {
			System.out.println("Address change unsuccessful.");
			return false;
		}

	}
	/*
	 * This function send operation to change username based on previous
	 * username
	 */

	/* This method changes the user´s username for a given user */
	private boolean changeUsername(String old_username) {
		/* Declaring Instances */
		Operation op = new Operation();
		Response response = new Response();

		System.out.println("Enter new username: ");
		String new_username = stdin.next();
		/* Set operation values */
		op.setUser(old_username);
		op.setWildcard(new_username);
		op.setOp_code("DET_USR");
		/* Send Operation to server */
		sendOperation(op);
		/* Wait Response */
		try {
			response = (Response) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Depending on response display message and set new values to profile
		 */
		if (response.getStatus().equals("DET_USR_OK")) {
			System.out.println("Username changed successfully.");
			profile.setUser(op.getWildcard());
			return true;
		} else {
			System.out.println("Username change unsuccessful.");
			return false;
		}

	}
	/*
	 * This function send operation to change password based on username
	 */

	/* This method changes the user´s password for a given user */
	private boolean changePassword(String username) {
		/* Declaring Instances */
		Operation op = new Operation();
		Response response = new Response();

		System.out.println("Enter old password: ");
		String old_pwd = stdin.next();
		/* Set operation values */
		op.setOp_code("DET_PWD");
		op.setUser(username);
		op.setPassword(old_pwd);
		System.out.println("Enter new password: ");
		String new_pwd = stdin.next();
		op.setWildcard(new_pwd);
		/* Send Operation to server */
		sendOperation(op);
		/* Wait Response */
		try {
			response = (Response) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Depending on response display message */
		if (response.getStatus().equals("DET_PWD_OK")) {
			System.out.println("Password changed successfully.");

			return true;
		} else {
			System.out.println("Password change unsuccesful.");
			return false;
		}

	}
	/*
	 * This function display menu to change details
	 */

	/* This method displays a menu for changing account details */
	private void detailsMenu() {
		boolean details_menu = true;

		while (details_menu) {

			System.out.println("Operation INFO: Acc: " + profile.getAccountNumber() + " User:" + profile.getUser()
					+ " Address: " + profile.getAddress() + " ");
			System.out.println("Please, select an option to proceed:");
			System.out.println("1. Change Name.");
			System.out.println("2. Change Address.");
			System.out.println("3. Change User name.");
			System.out.println("4. Change password.");
			System.out.println("5. Exit to main menu.");
			String input = stdin.next();

			if (input.equals("1")) {
				System.out.println(profile.getAccountNumber());

				if (changeName(profile.getAccountNumber())) {
					System.out.println("Name Changed");
				}
			} else if (input.equals("2")) {
				if (changeAddress(profile.getAccountNumber())) {
					System.out.println("Address Changed");
				}

			} else if (input.equals("3")) {
				if (changeUsername(profile.getUser())) {
					System.out.println("Username Changed");
				}
			} else if (input.equals("4")) {
				if (changePassword(profile.getUser())) {
					System.out.println("Password Changed");
				}

			} else if (input.equals("5")) {
				details_menu = false;
			}
		}
	}
	/*
	 * This function send operation to change make a lodgement based on account
	 * number
	 */

	/* This method allows the user to make a lodgement for a given account */
	private boolean makeLodgement(int account) {
		/* Declaring Instances */
		Operation op = new Operation();
		Response response = new Response();

		System.out.println("Enter amount to lodge: ");
		double amount = stdin.nextDouble();
		/* Set operation values */
		op.setOp_code("LODT");
		op.setAccountNumber(account);
		op.setAmount(amount);
		op.setUser(profile.getUser());
		System.out.println("Amount: " + op.getAmount());
		/* Send Operation to server */
		sendOperation(op);
		/* Wait Response */
		try {
			response = (Response) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Depending on response display message */
		if (response.getStatus().endsWith("_OK")) {
			System.out.println("Lodgement successful.");
			System.out.println("Balance: " + response.getBalance() + "€");
			return true;
		} else {
			System.out.println("Lodgement unsuccessful.");
			return false;
		}
	}

	/*
	 * This function send operation to make a withdrawal based on account number
	 */
	/* This method allows the user to make a withdrawal for a given account */
	private boolean makeWithdrawal(int account) {
		/* Declaring Instances */
		Operation op = new Operation();
		Response response = new Response();

		System.out.println("Enter amount to withdraw: ");
		double amount = stdin.nextDouble();
		/* Set operation values */
		op.setOp_code("WDWL");
		op.setAmount(amount);
		op.setAccountNumber(account);
		op.setUser(profile.getUser());
		System.out.println("Amount: " + op.getAmount());
		/* Send Operation to server */
		sendOperation(op);
		/* Wait Response */
		try {
			response = (Response) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Depending on response display message */
		if (response.getStatus().endsWith("_OK")) {
			System.out.println("Withdrawal successful.");
			System.out.println("Balance: " + response.getBalance() + "€");
			return true;
		} else {
			System.out.println("Withdrawal unsuccessful.");
			return false;
		}

	}

	/* Main method */
	public static void main(String args[]) {
		Client client = new Client();
		client.run();
	}
}
/*
 * End of Client
 */