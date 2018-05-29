package heartbeat_testcase4;

public class ReceiverT extends Thread{



	@Override
	public void run(){
		
		receiveHeartBeatInfinitely();
	}

	private void receiveHeartBeatInfinitely(){
		while (true) {
		 
			receiveHeartBeatMessage();
		}
	}

	private void receiveHeartBeatMessage(){
			HBReceiver.receiveHeartBeat();
	}



}
