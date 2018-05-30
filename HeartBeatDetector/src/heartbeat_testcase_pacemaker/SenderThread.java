package heartbeat_testcase_pacemaker;

public class SenderThread implements  Runnable {
	
	@Override
	public void run() {
		long currentTime = System.currentTimeMillis(); 
		new HeartbeatSender(1000).sendHB(currentTime);
	 
	}
	
	
}
