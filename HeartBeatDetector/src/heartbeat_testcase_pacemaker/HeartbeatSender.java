package heartbeat_testcase_pacemaker;

public class HeartbeatSender   {
	private int sendingInterval;

	public HeartbeatSender(int sendingInterval) {
		this.sendingInterval = sendingInterval;
	}
	
	public void sendHeartBeat(long currentTime ){		
		HeartbeatReciever.updateTime(currentTime);
	}

	
	public void sendHB(long currentTime )	{
		try {
			while (true) {
			sendHeartBeat(currentTime );
			Thread.sleep(sendingInterval);
			}
		} catch (InterruptedException e) {
			System.err.println("Heartbeat sender appears to have crashed.");
			
		}
	}
}
