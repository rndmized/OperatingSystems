package ie.gmit.sw.os.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSpinnerUI;

import ie.gmit.sw.os.db.SQLiteJDBC;
import ie.gmit.sw.os.operations.Operation;
import ie.gmit.sw.os.operations.Response;

public class EchoServer {

	private static SQLiteJDBC connector = new SQLiteJDBC();

	public static void main(String[] args) throws Exception {

		ServerSocket m_ServerSocket = new ServerSocket(2004, 10);
		int id = 0;
		while (true) {
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++, connector);
			cliThread.start();
		}
	}
}

class ClientServiceThread extends Thread {

	private Socket clientSocket;
	private String message;
	private int clientID = -1;
	private boolean running = true;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private SQLiteJDBC connector;

	ClientServiceThread(Socket s, int i, SQLiteJDBC connector) {
		this.clientSocket = s;
		this.clientID = i;
		this.connector = connector;
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client(sent)> " + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

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

	private void login(Operation op) {
		System.out.println("Logging");
		Response res = new Response();
		if (connector.login(op.getUser(), op.getPassword())) {
			res.setStatus("LOG_OK");
			
			Operation op_temp = connector.getAccountDetails(op.getUser());
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

	private void signup(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		System.out.println("Signing UP on Server.");
		if (connector.signUp(op)) {
			res.setStatus("SIGN_OK");
			System.out.println("SIGN_OK");
			String log_message = op.getOp_code() + ". User " + op.getUser() + " attempted log in with status "
					+ res.getStatus();
			connector.insert_log(op.getAccountNumber(), log_message);
			sendResponse(res);
		} else {
			System.out.println("SIGN_FAIL");
			res.setStatus("SIGN_FAIL");
			sendResponse(res);
		}
	}

	private void details(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());

		if (op.getOp_code().endsWith("PWD")) {
			System.out.println("Changing Password.");
			if (connector.check_password(op)) {
				connector.updatePassword(op.getUser(), op.getWildcard());
				res.setStatus("DET_PWD_OK");
				sendResponse(res);
			} else {
				res.setStatus("DET_PWD_FAIL");
				sendResponse(res);
			}

		} else if (op.getOp_code().endsWith("NME")) {
			System.out.println("Changing Name.");
			System.out.println(op.getAccountNumber());
			if (connector.updateName(op.getAccountNumber(), op.getName())) {
				res.setStatus("DET_NME_OK");
				sendResponse(res);
			} else {
				res.setStatus("DET_NME_FAIL");
				sendResponse(res);
			}
		} else if (op.getOp_code().endsWith("ADDR")) {
			System.out.println("Changing Address.");
			if (connector.updateAddress(op.getAccountNumber(), op.getAddress())) {
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
			if (connector.check_username(op.getWildcard())) {
				connector.updateUsername(op.getUser(), op.getWildcard());
				res.setStatus("DET_USR_OK");
				sendResponse(res);
			} else {
				res.setStatus("DET_USR_FAIL");
				sendResponse(res);
			}

		}
	}

	private void makeLodgement(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		if (connector.updateBalance(op.getAccountNumber(), op.getAmount())) {
			res.setStatus("LODT_OK");
			res.setBalance(connector.getBalance(op.getAccountNumber()));

			sendResponse(res);
		} else {
			res.setStatus("LODT_FAIL");
			sendResponse(res);
		}
		String log_message = op.getOp_code() + ". User " + op.getUser() + " attempted lodgement with status "
				+ res.getStatus()+" Amount: " + op.getAmount() + "Final Balance: " + res.getBalance() ;
		connector.insert_log(op.getAccountNumber(), log_message);

	}

	private void makeWithdrawal(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		if (connector.getBalance(op.getAccountNumber()) - op.getAmount() > -1000) {
			if (connector.updateBalance(op.getAccountNumber(), -op.getAmount())) {
				res.setStatus("WDWL_OK");
				res.setBalance(connector.getBalance(op.getAccountNumber()));
				sendResponse(res);
			} else {
				res.setStatus("WDWL_FAIL");
				sendResponse(res);
			}
		} else {
			res.setStatus("WDWL_INSUFFICIENT_FUNDS");
			res.setBalance(connector.getBalance(op.getAccountNumber()));
			sendResponse(res);
		}
		String log_message = op.getOp_code() + ". User " + op.getUser() + " attempted withdrawal with status "
				+ res.getStatus()+" Amount: " + op.getAmount()+ "Final Balance: " + res.getBalance() ;
		connector.insert_log(op.getAccountNumber(), log_message);

	}

	private void queryLog(Operation op) {
		Response res = new Response();
		res.setOp_code(op.getOp_code());
		ArrayList<String> log_list = connector.getTransactions(op.getAccountNumber());
		res.setList(log_list);
		sendResponse(res);
	}
}
