package heartbeat_testcase1;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * @author ananta
 *
 * The sending process which repeatedly sends heartbeat messages to the
 * receiver process.
 */

public class HeartBeatSender {

	/*private String []SEND_MESSAGE_MECHANISM = {
												"MethodCall" , 			// SEND_MESSAGE_MECHANISM_TYPE = 1
												"SocketCommunication",	// SEND_MESSAGE_MECHANISM_TYPE = 2
												"SharedObject",			// SEND_MESSAGE_MECHANISM_TYPE = 3
												"FileOperation"			// SEND_MESSAGE_MECHANISM_TYPE = 4
												};*/
 
	
	static int counter =0;

	public  static void sendHeartBeatInfinitely(int data){
		while (true) {
			counter ++;
			if (counter ==100 ) counter = 0;
			sendHeartBeatMessage(data);
		}
	}

	public static void sendHeartBeatMessage(int data) {

		sendHeartBeatMsg(data);
	}
	public static void sendHeartBeatMsg(int data ){
		Controller.delay();	
		switch(Controller.MESSAGE_COOMUNICATION_TYPE){
		case 1:
			sendHeartBeatUsingMethodCall(data);
			break;

		case 2:
			sendHeartBeatUsingSocket(data);
			break;
		case 3:
			sendHeartBeatUsingSharedObject(data);
			break;
		case 4:
			sendHeartBeatUsingFileOperation(data);
			break;
		default:

			break;

		}

	}

	private static void sendHeartBeatUsingMethodCall(int send_data){
		System.out.println(">> Send data using method call = " + send_data );
		HeartBeatReceiver.updateData(send_data);
	}



	 /* Uses server socket to output heartbeat messages, which the receiver process reads.
	 *  The Server and Receiver are connected used localhost at port 40002 by default
	 */
	public static void sendHeartBeatUsingSocket(int data){
		try{
			ServerSocket ss; 
			Socket dataSocket;
			ss = new ServerSocket(40002);
			dataSocket = ss.accept();
			PrintStream socketOutput = new PrintStream(dataSocket.getOutputStream());
			String message = String.valueOf(data);
			socketOutput.println(message); //send the heartbeat message
			System.out.println(">> Send data using socket: " + message); //log the sent message
			socketOutput.flush();
			dataSocket.close();
			ss.close();		
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	
	
	public static void sendHeartBeatUsingSharedObject(int data){
		System.out.println(">> Send data using shared variable: " + data); 
 		SharedObject.setSharedVariable(new String(String.valueOf(data)));
	}
	
	public static void sendHeartBeatUsingFileOperation(int data){
		try {
			System.out.println(">> Send data using file operation: " + data);
			 FileOperations.writeToTextFile("heartbeatdata.txt", String.valueOf(data));
		} catch (IOException e) {
 			e.printStackTrace();
		}
	}
	
 
}