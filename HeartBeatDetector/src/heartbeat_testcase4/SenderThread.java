package heartbeat_testcase4;

public class SenderThread extends Thread {

	int counter =0;
	int data = 1234;
 
	@Override
	public void run(){ 
		HeartBeatSender.sendHeartBeatInfinitely(data);
	}
 

}
