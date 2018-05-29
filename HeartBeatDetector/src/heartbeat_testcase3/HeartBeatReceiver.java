package heartbeat_testcase3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class HeartBeatReceiver {

	private static int received_data;

	public static void updateData(int data) {
		received_data = 0;
	}

	public static void receiveHeartBeat(){
		Controller.delay();	
		switch(Controller.MESSAGE_COOMUNICATION_TYPE){
		case 1:
			receiveHeartBeatThroughMethodCall( );
			break;

		case 2:
			receiveHeartBeatUsingSocket( );
			break;
		case 3:
 			break;
		case 4:
			receiveHeartBeatUsingFileOperation();
			break;
		default:

			break;

		}

	}

	
	public static void receiveHeartBeatThroughMethodCall() {
		System.out.println("=> Received_data using method call = " + received_data );
	}

	public static void receiveHeartBeatUsingSocket() {
		try {
			InetAddress acceptorHost = InetAddress.getByName("localhost");
			int serverPortNum = 40002;
			 
				try {
					Socket clientSocket;
					clientSocket = new Socket(acceptorHost, serverPortNum);
					BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					String line = br.readLine(); //the message received from the server process
					if(line != null){ //check if there's a heartbeat message
						System.out.println("=> Receiver got data using socket: " + line); //log the message received
					}
					clientSocket.close();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
 						e.printStackTrace();
					}

				} catch (IOException e) {
 					System.out.println("No connection to Server. No heartbeat message received...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
 						e1.printStackTrace();
					}
				}
			
		} catch (UnknownHostException e) {
 			e.printStackTrace();
		}
	}

 
	private static void receiveHeartBeatUsingFileOperation(){
		try {
			String data = FileOperations.readTextFile("heartbeatdata.txt");
			System.out.println("=> Received data throught File Opearation " + data); 

		} catch (IOException e) {
 			e.printStackTrace();
		}
	}

}


