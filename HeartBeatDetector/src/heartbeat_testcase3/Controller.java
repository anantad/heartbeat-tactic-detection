package heartbeat_testcase3;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import messageCommunication.FileWatcher;

public class Controller {	

	public static int MESSAGE_COOMUNICATION_TYPE = 4;
 	private static SenderThread senderThread = new SenderThread();

 
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
				// here we code the action on a change
				System.out.println( " ----------   File "+ file.getName() +" have changed ! ---------------" );
			}
		};

		Timer timer = new Timer();
		// repeat the check every second
		timer.schedule( task , new Date(), 1000 );
	}


	public static void main(String []a){
		senderThread.start();	
		new ReceiverThread().start();
		
		fileWatacher();

	}


}
