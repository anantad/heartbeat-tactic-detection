package heartbeat_testcase3;

public class SenderThread extends Thread {
	
	 int counter =0;
	 
	 @Override
	 public void run(){
		 int data = 1234;
		
		 HeartBeatSender.sendHeartBeatInfinitely(data);
	    }
	 
	

	 
	 
}
