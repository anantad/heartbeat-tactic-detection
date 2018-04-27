package heartbeat_testcase1;

public class ReceiverThread extends Thread{



	@Override
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
