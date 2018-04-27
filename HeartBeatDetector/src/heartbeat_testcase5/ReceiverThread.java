package heartbeat_testcase5;

public class ReceiverThread extends Thread{



	@Override
	public void run(){
		
		receiveHeartBeatInfinitely();
	}

	private void receiveHeartBeatInfinitely(){
		while (true) {
			Processor.delay();
			receiveHeartBeatMessage();
		}
	}

	private void receiveHeartBeatMessage(){
			HeartBeatReceiver.receiveHeartBeat();
	}



}
