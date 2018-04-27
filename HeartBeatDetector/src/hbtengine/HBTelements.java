package hbtengine;

import java.util.ArrayList;
import java.util.List;

public class HBTelements {

	private String packageName = null;
	private List<HBTclass> hbtClasses = new ArrayList<HBTclass>();
	private static HBTelements hbtEelements = null;

	private HBTelements(){

	}
	public static HBTelements getInstance()
	{
		if (hbtEelements == null)
			hbtEelements = new HBTelements();

		return hbtEelements;
	}

	public void setHBTClasses(  List<HBTclass> hbtClasses){
		this.hbtClasses =  hbtClasses;
	}
	public  List<HBTclass> getHBTClasses( ){
		return this.hbtClasses ;
	}

	public void setPackageNames(  String packageName){
		this.packageName =  packageName;
	}
	public  String getPackageName( ){
		return this.packageName ;
	}


}
