package heartbeat_testcase2;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import messageCommunication.FileWatcher;

public class Controller {	

	public static int MESSAGE_COOMUNICATION_TYPE = 1;
	private SharedObject sharedObject = new SharedObject();
	private static SenderThread senderThread = new SenderThread();

	public SharedObject getSharedObject(){
		return sharedObject;
	}
	public static void delay(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
 
	public static void main(String []a){
		senderThread.start();	
		new ReceiverThread().run();		
	 

	}


}
