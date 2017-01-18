package ie.gmit.sw.os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import ie.gmit.sw.os.db.SQLiteJDBC;
import ie.gmit.sw.os.operations.Operation;
import ie.gmit.sw.os.operations.Response;
/**
 * @author RnDMizeD
 * @version 1.0
 */
public class EchoServer {

	private static Monitor monitor = new Monitor();
	//private static SQLiteJDBC monitor = new SQLiteJDBC();
	/**
	 *A thread for every client will be spawned
	 *
	 */
	public static void main(String[] args) throws Exception {

		ServerSocket m_ServerSocket = new ServerSocket(2004, 10);
		int id = 0;
		while (true) {
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++, monitor);
			cliThread.start();
		}
	}
}
/**
 * Client Thread
 *
 */

class ClientServiceThread extends Thread {

	private Socket clientSocket;
	private int clientID = -1;
	private boolean running = true;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private Monitor monitor;

	ClientServiceThread(Socket s, int i, Monitor monitor) {
		this.clientSocket = s;
		this.clientID = i;
		this.monitor = monitor;
	}
	/**
	 * Sends a string to a client
	 *
	 */

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client(sent)> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	/**
	 * Sends response instance to client
	 *
	 */
	void sendResponse(Response res) {
		try {
			out.writeObject(res);
			out.flush();
			System.out.println("client(sent)> " + res.getOp_code() + " with status " + res.getStatus() + ". Balance:  "
					+ res.getBalance());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	/**
	 * While thread is running will listen for operations from client and call method correspondent to operation code
	 *
	 */
	public void run() {
		System.out.println(
				"Accepted Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
		try {

			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream());
			sendMessage("Connection successful");

			do {
				try {

					System.out.println("Waiting Operation");
					Operation op = (Operation) in.readObject();

					System.out.println("Operation received.");

					if (op.getOp_code().equals("LOGIN")) {
						this.login(op);
					}

					if (op.getOp_code().equals("SIGNUP")) {
						this.signup(op);

					}

					if (op.getOp_code().startsWith("DET_")) {
						this.details(op);
					}

					if (op.getOp_code().equals("LODT")) {

						this.makeLodgement(op);
					}

					if (op.getOp_code().equals("WDWL")) {

						this.makeWithdrawal(op);
					}

					if (op.getOp_code().equals("LOG")) {

						this.queryLog(op);
					}
					
					System.out.println("Server Operation Finished. Operation Code: " + op.getOp_code());

					op = null;

				} catch (ClassNotFoundException classnot) {
					System.err.println("Data received in unknown format");
					System.err.println(classnot.getMessage());
				} catch (Exception e) {
					running = false;
				}

			} while (running);

			System.out.println(
					"Ending Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * If it is a login operation, check db and respond accordingly
	 *
	 */
	private void login(Operation op) {
		System.out.println("Logging");
		Response res = new Response();
		if (monitor.login(op.getUser(), op.getPassword())) {
			res.setStatus("LOG_OK");
			
			Operation op_temp = monitor.getAccountDetails(op.getUser());
			op.setAccountNumber(op_temp.getAccountNumber());
			op.setName(op_temp.getName());
			op.setAddress(op_temp.getAddress());

			res.setOperation(op);
			res.setBalance(op_temp.getAmount());
			sendResponse(res);

		} else {
			res.setStatus("LOG_FAIL");
			sendResponse(res);
		}

	}
	/**
	 * If it is a sign up operation, insert data in db and sends a response. 
	 *
	 */
	private void signup(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		System.out.println("Signing UP on Server.");
		if (monitor.signup(op)) {
			res.setStatus("SIGN_OK");
			System.out.println("SIGN_OK");
			String log_message = op.getOp_code() + ". User " + op.getUser() + " attempted log in with status "
					+ res.getStatus();
			monitor.insertLog(op.getAccountNumber(), log_message);
			sendResponse(res);
		} else {
			System.out.println("SIGN_FAIL");
			res.setStatus("SIGN_FAIL");
			sendResponse(res);
		}
	}
	/**
	 * Determines the detail type operation, execute changes and sends response
	 *
	 */
	private void details(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());

		if (op.getOp_code().endsWith("PWD")) {
			System.out.println("Changing Password.");
			if (monitor.check_password(op)) {
				monitor.updatePassword(op.getUser(), op.getWildcard());
				res.setStatus("DET_PWD_OK");
				sendResponse(res);
			} else {
				res.setStatus("DET_PWD_FAIL");
				sendResponse(res);
			}

		} else if (op.getOp_code().endsWith("NME")) {
			System.out.println("Changing Name.");
			System.out.println(op.getAccountNumber());
			if (monitor.updateName(op.getAccountNumber(), op.getName())) {
				res.setStatus("DET_NME_OK");
				sendResponse(res);
			} else {
				res.setStatus("DET_NME_FAIL");
				sendResponse(res);
			}
		} else if (op.getOp_code().endsWith("ADDR")) {
			System.out.println("Changing Address.");
			if (monitor.updateAddress(op.getAccountNumber(), op.getAddress())) {
				System.out.println("DET_ADDR_OK");
				res.setStatus("DET_ADDR_OK");
				sendResponse(res);
			} else {
				System.out.println("DET_ADDR_FAIL");
				res.setStatus("DET_ADDR_FAIL");
				sendResponse(res);
			}

		} else if (op.getOp_code().endsWith("USR")) {
			System.out.println("Changing Username.");
			if (monitor.checkUsername(op.getWildcard())) {
				monitor.updateUsername(op.getUser(), op.getWildcard());
				res.setStatus("DET_USR_OK");
				sendResponse(res);
			} else {
				res.setStatus("DET_USR_FAIL");
				sendResponse(res);
			}

		}
	}
	/**
	 * If operation is a lodegement add amount to balance and send response
	 *
	 */
	private void makeLodgement(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		if (monitor.updateBalance(op.getAccountNumber(), op.getAmount())) {
			res.setStatus("LODT_OK");
			res.setBalance(monitor.getBalance(op.getAccountNumber()));

			sendResponse(res);
		} else {
			res.setStatus("LODT_FAIL");
			sendResponse(res);
		}
		String log_message = op.getOp_code() + ". User " + op.getUser() + " attempted lodgement with status "
				+ res.getStatus()+" Amount: " + op.getAmount() + "Final Balance: " + res.getBalance() ;
		monitor.insertLog(op.getAccountNumber(), log_message);

	}
	/**
	 * If operation is a withdrawal subtract amount to balance and send response (send fail if final balance under -1000)
	 *
	 */
	private void makeWithdrawal(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		if (monitor.getBalance(op.getAccountNumber()) - op.getAmount() > -1000) {
			if (monitor.updateBalance(op.getAccountNumber(), -op.getAmount())) {
				res.setStatus("WDWL_OK");
				res.setBalance(monitor.getBalance(op.getAccountNumber()));
				sendResponse(res);
			} else {
				res.setStatus("WDWL_FAIL");
				sendResponse(res);
			}
		} else {
			res.setStatus("WDWL_INSUFFICIENT_FUNDS");
			res.setBalance(monitor.getBalance(op.getAccountNumber()));
			sendResponse(res);
		}
		String log_message = op.getOp_code() + ". User " + op.getUser() + " attempted withdrawal with status "
				+ res.getStatus()+" Amount: " + op.getAmount()+ "Final Balance: " + res.getBalance() ;
		monitor.insertLog(op.getAccountNumber(), log_message);

	}
	/**
	 * Request list with last ten entries from log for a given account
	 *
	 */
	private void queryLog(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		ArrayList<String> log_list = monitor.getTransactions(op.getAccountNumber());
		res.setList(log_list);
		sendResponse(res);
	}
}
