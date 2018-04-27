package heartbeat_testcase5;

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

	public  static void sendHeartBeatcontinuously(int data){
		for(;;) {
			counter ++;
			if (counter ==100 ) counter = 0;
			sendHeartBeatMessage(counter);
		}
	}

	public static void sendHeartBeatMessage(int data) {

		sendHeartBeatMsg(data);
	}
	public static void sendHeartBeatMsg(int data ){
		Processor.delay();	
		switch(Processor.MESSAGE_COOMUNICATION_TYPE){
		case 1:
			sendHeartBeatUsingMethodCall(data);
			break;

	 		default:

			break;

		}

	}

	private static void sendHeartBeatUsingMethodCall(int send_data){
		System.out.println(">> Send data using method call = " + send_data );
		HeartBeatReceiver.updateData(send_data);
	}
 	
 
}