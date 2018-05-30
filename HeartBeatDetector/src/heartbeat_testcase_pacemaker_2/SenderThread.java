package heartbeat_testcase_pacemaker_2;

public class SenderThread extends Thread{ 
	
	public void run() {
		long currentTime = System.currentTimeMillis(); 
		new HeartbeatSender(1000).sendHB(currentTime);
	 
	}
	
	
}
