package heartbeat_testcase_pacemaker_1;

public class SenderThread extends Thread{

	
	@Override
	public void run() {
		long currentTime = System.currentTimeMillis(); 
		new HeartbeatSender(1000).sendHB(currentTime);
	 
	}
	
	
}
