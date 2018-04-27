package heartbeat_testcase4;

public class SharedObject {


	private static String sharedVariable = null;
	
	public static String  getSharedVariable(){
		return sharedVariable;
	}
	public static void setSharedVariable(String data){
		sharedVariable = data;
	}
	
	
	//the data at receiving end must be able to proove that it is not the old data that receiver got during last time . 
	//it could be a bug. detect this  as well.
	
}
