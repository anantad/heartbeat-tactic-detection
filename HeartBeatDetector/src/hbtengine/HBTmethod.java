package hbtengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HBTmethod {

	private String methodName = null;
	private String className = null;
	private int methodType = 0; // 1 -> declared method , 2->invoked method  
	private String returnType = null;
	private String retrunTypeModifier = null;
	private int argCount =0;
	private boolean isOvverridden = false;
	private String parentMethod = null;
	private Map<String,String> loop = new HashMap<String,String>();
	private List<String> argList = new ArrayList<String>(); 
	private List<String> localVariableList = new ArrayList<String>(); 
	private List<HBTmethod> invokingMethodList = new ArrayList<HBTmethod>();
	

 
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getLocalVariables() {
		return localVariableList;
	}

	public void setLocalVariableList(List<String> localVariables) {
		this.localVariableList = localVariables;
	}

	public List<String> getArgList() {
		return argList;
	}

	public void setArgList(List<String> argList) {
		this.argList = argList;
	}

	public int getArgCount() {
		return argCount;
	}

	public void setArgCount(int argCount) {
		this.argCount = argCount;
	}

 
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<HBTmethod> getInvokingMethods() {
		return invokingMethodList;
	}

	public void setInvokingMethods(List<HBTmethod> invokingMethods) {
		this.invokingMethodList = invokingMethods;
	}

	public boolean isOvverridden() {
		return isOvverridden;
	}

	public void setOvverridden(boolean isOvverridden) {
		this.isOvverridden = isOvverridden;
	}

 

	public String getRetrunTypeModifier() {
		return retrunTypeModifier;
	}

	public void setRetrunTypeModifier(String retrunTypeModifier) {
		this.retrunTypeModifier = retrunTypeModifier;
	}

	public String getParentMethod() {
		return parentMethod;
	}

	public void setParentMethod(String parentMethod) {
		this.parentMethod = parentMethod;
	}

	public int getMethodType() {
		return methodType;
	}

	public void setMethodType(int methodType) {
		this.methodType = methodType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String,String> getLoop() {
		return loop;
	}

	public void setLoop(Map<String,String> loop) {
		this.loop = loop;
	}

}
