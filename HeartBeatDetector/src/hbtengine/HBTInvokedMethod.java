package hbtengine;

public class HBTInvokedMethod {
 
	private String className;
	private String methodName;
	private String returnType;
	private String arguments;
	private String modifier;
	private String callerMethod; 
	
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
	public String getCallerMethod() {
		return callerMethod;
	}
	public void setCallerMethod(String callerMethod) {
		this.callerMethod = callerMethod;
	}


}

