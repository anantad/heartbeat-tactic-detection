package hbtengine;

import java.util.ArrayList;
import java.util.List;

public class HBTmembervariable {
	
	private String variableName = null;
	private boolean isStatic = false;
	private String variableType = null;
	private boolean isAlias = false;
	private List<String> aliasNames = new ArrayList<String>(); // save alias variables with form "variable,class" as eachentry and parse later.
	private boolean isWritingIntoIt = false;
 	private boolean isReadingFromIt = false;
 	private String readingVariableName = null;
 	private String writingVariableName = null;
 	
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getWritingVariableName() {
		return writingVariableName;
	}
	public void setWritingVariableName(String writingVariableName) {
		this.writingVariableName = writingVariableName;
	}
	public String getReadingVariableName() {
		return readingVariableName;
	}
	public void setReadingVariableName(String readingVariableName) {
		this.readingVariableName = readingVariableName;
	}
	public boolean isReadingFromIt() {
		return isReadingFromIt;
	}
	public void setReadingFromIt(boolean isReadingFromIt) {
		this.isReadingFromIt = isReadingFromIt;
	}
	public boolean isWritingIntoIt() {
		return isWritingIntoIt;
	}
	public void setWritingIntoIt(boolean isWritingIntoIt) {
		this.isWritingIntoIt = isWritingIntoIt;
	}
	public String getVariableType() {
		return variableType;
	}
	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}
	public boolean isStatic() {
		return isStatic;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	public List<String> getAliasNames() {
		return aliasNames;
	}
	public void setAliasNames(List<String> aliasNames) {
		this.aliasNames = aliasNames;
	}
	public boolean isAlias() {
		return isAlias;
	}
	public void setAlias(boolean isAlias) {
		this.isAlias = isAlias;
	}


}
