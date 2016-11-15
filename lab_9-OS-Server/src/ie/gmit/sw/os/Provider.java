package ie.gmit.sw.os;


import java.io.*;
import java.net.*;
public class Provider{
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	Provider(){}
	void run()
	{
		try{
			//1. creating a server socket
			providerSocket = new ServerSocket(2004, 10);
			//2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from " + connection.getInetAddress().getHostName());
			//3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			//4. The two parts communicate via the input and output streams
			do{
				try{
					sendMessage("Enter first number:");
					message = (String)in.readObject();
					int numOne = Integer.parseInt(message);
					System.out.println("client>" + message);
					sendMessage("Enter second number:");
					message = (String)in.readObject();
					int numTwo = Integer.parseInt(message);
					System.out.println("client>" + message);
					sendMessage("Press 1 for addition or 2 for substraction:");
					message = (String)in.readObject();
					if(message.equals("1")){
						sendMessage(numOne + " + " + numTwo + " = " + (numOne + numTwo));
						
					} else {
						sendMessage(numOne + " - " + numTwo + " = " + (numOne - numTwo));
					}
					
					if (message.equals("bye"))
						sendMessage("bye");
				}
				catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}catch(Exception e){
					System.err.println("Data received not in Integer format");
				}
			}while(!message.equals("bye"));
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				providerSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Provider server = new Provider();
		while(true){
			server.run();
		}
	}
}
