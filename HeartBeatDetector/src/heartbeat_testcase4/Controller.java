package heartbeat_testcase4;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import messageCommunication.FileWatcher;

public class Controller {	

	public static int MESSAGE_COOMUNICATION_TYPE = 1;
	private SharedObject sharedObject = new SharedObject();
	private static SenderT senderThread = new SenderT();

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
	private static void fileWatacher()	{
		TimerTask task = new FileWatcher( new File("heartbeatdata.txt") ) {
			protected void onChange( File file ) {
				System.out.println( " ----------   File "+ file.getName() +" have changed ! ---------------" );
			}
		};
		Timer timer = new Timer();
		timer.schedule( task , new Date(), 1000 );
	}
 	public static void main(String []a){
		senderThread.start();	
		new ReceiverT().start();
		
		fileWatacher();

	}


}
