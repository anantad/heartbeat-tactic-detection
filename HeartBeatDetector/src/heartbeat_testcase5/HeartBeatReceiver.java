package heartbeat_testcase5;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class HeartBeatReceiver {

	private static int received_data;
	private static int received_data_previous;
	public static void updateData(int data) {
		received_data = data;
		received_data_previous = data;

	}

	public static void receiveHeartBeat(){
		Processor.delay();	
		switch(Processor.MESSAGE_COOMUNICATION_TYPE){
		case 1:
			processHeartBeat( );
			break;
 	 
		default:

			break;

		}
	 
	}

	
	 
	public static void processHeartBeat() {
		System.out.println("=> Received_data using method call = " + received_data );
		processReceivedData(received_data + 1000);
		received_data = -1; // reseting
	}

	private static void processReceivedData(int data ) {
 		
	} 
	
}


