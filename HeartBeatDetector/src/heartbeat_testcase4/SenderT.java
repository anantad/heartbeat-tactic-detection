package heartbeat_testcase4;

public class SenderT extends Thread {

	int counter =0;
 	int data = 1234;
 
	@Override
	public void run(){ 

		HBSender.sendHeartBeatInfinitely(data);
	}
 

}
