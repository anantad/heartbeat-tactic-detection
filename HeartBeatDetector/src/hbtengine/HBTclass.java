package hbtengine;

import java.util.ArrayList;
import java.util.List;

public class HBTclass {
	private String classname =null;
	private String modifier =null;
	private boolean isThread = false;
	private boolean isAbstract = false;
	private String subClassName = null;
	private   List<HBTmethod> declaredMethods= new  ArrayList<HBTmethod>();
	private   List<HBTmembervariable> hbtMembervariables= new  ArrayList<HBTmembervariable>();
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public List<HBTmembervariable> getHbtMembervariables() {
		return hbtMembervariables;
	}
	public void setHbtMembervariables(List<HBTmembervariable> hbtMembervariables) {
		this.hbtMembervariables = hbtMembervariables;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public boolean isThread() {
		return isThread;
	}
	public void setThread(boolean isThread) {
		this.isThread = isThread;
	}
	public boolean isAbstract() {
		return isAbstract;
	}
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	public String getSubClassName() {
		return subClassName;
	}
	public void setSubClassName(String subClassName) {
		this.subClassName = subClassName;
	}
	public List<HBTmethod> getDeclaredMethods() {
		return declaredMethods;
	}
	public void setDeclaredMethods(List<HBTmethod> declaredMethods) {
		this.declaredMethods = declaredMethods;
	}
 
 	 
	
}
