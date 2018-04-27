package hbtengine;

import java.util.HashMap;
import java.util.Map;

public class HBTDeclaredMethod {

	private String className;
	private String methodName;
	private String returnType;
	private String arguments;
	private String modifier;
	private Map<String, String> loop = new HashMap<String, String>();
	
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getArguments() {
		return arguments;
	}
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}
	public Map<String, String> getLoop() {
		return loop;
	}
	public void setLoop(Map<String, String> loops) {
		this.loop = loop;
	}
	
	
}
