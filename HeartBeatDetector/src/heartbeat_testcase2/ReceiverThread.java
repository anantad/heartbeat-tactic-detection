package heartbeat_testcase2;

public class ReceiverThread {
 
	public void run(){
		
		receiveHeartBeatInfinitely();
	}

	private void receiveHeartBeatInfinitely(){
		while (true) {
			Controller.delay();
			receiveHeartBeatMessage();
		}
	}

	private void receiveHeartBeatMessage(){
			HeartBeatReceiver.receiveHeartBeat();
	}



}
