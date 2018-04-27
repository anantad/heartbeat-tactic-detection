package heartbeat_testcase5;

public class SenderThread extends Thread {

	int counter =0;

	@Override
	public void run(){

		int data = 100;

		HeartBeatSender.sendHeartBeatcontinuously(data);

	}
}
