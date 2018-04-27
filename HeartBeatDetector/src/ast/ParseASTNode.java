package ast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;

import hbtengine.HBTAlgorithm;
import hbtengine.HBTclass;
import hbtengine.HBTelements;

public class ParseASTNode {

	public static  File outputfile =null;
	public static Writer fileWriter = null;

	public static void setoutputfile(String f){
		fileWriter = null;
		outputfile = new File(f);	
		try {
			fileWriter = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getNodes(ASTNode node ) {
		String content = null;
		List properties = node.structuralPropertiesForType();
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			Object descriptor = iterator.next();
			if (descriptor instanceof SimplePropertyDescriptor) {
				SimplePropertyDescriptor simple = (SimplePropertyDescriptor) descriptor;
				Object value = node.getStructuralProperty(simple);

				if((simple!=null)&&(value!=null))
					content = simple.getId() + " (" + value.toString() + ")";
				System.out.println("content = "+content);
				/*try {
					fileWriter.write(content);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			} else if (descriptor instanceof ChildPropertyDescriptor) {
				ChildPropertyDescriptor child = (ChildPropertyDescriptor) descriptor;
				ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
				if (childNode != null) {
					content = "Child (id=" + child.getId() + ",name="+ child.toString() +") {";
					/*	System.out.println(content);
					try {
						fileWriter.write(content);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
					getNodes(childNode);

					System.out.println("}");
					/*try {
						fileWriter.write("}");
					} catch (IOException e) {
						e.printStackTrace();
					}*/
				}
			} else {
				ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) descriptor;
				if(node!=null){
					//System.out.println("node = " + node);
					content = "List (" + list.getId() + "){";
					System.out.println(content);
					/*try {
						fileWriter.write(content);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
					print((List) node.getStructuralProperty(list));
					/*	System.out.println("}");
										try {
						fileWriter.write("}");
					} catch (IOException e) {
						e.printStackTrace();
					}*/
				}
			}
		}
	}
	private static void print(List  nodes) {
		for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
			getNodes((ASTNode) iterator.next());
		}
	}


	private static void printIsThread(List  nodes) {
		for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
			isThread((ASTNode) iterator.next());
		}
	}

	private static String classType = "default";
	private static boolean isSuperClass = false;
	
	private static boolean isSuperClassType(String childID){
		if(childID.equals("superclassType"))
			return true;
		else return false;
	}
	
	public static void resetclassType(){
		classType = "default";
	}
	public static void resetisSuperClass(){
		isSuperClass = false;
	}
	public static boolean isThread(ASTNode node ){

 		List properties = node.structuralPropertiesForType();
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			Object descriptor = iterator.next();
			if (descriptor instanceof SimplePropertyDescriptor) {
				SimplePropertyDescriptor simple = (SimplePropertyDescriptor) descriptor;
				Object value = node.getStructuralProperty(simple);

				if((simple!=null)&&(value!=null)){
 					if(classType.equals("superclassType") && simple.getId().equals("identifier") && value.toString().equals("Thread")){
						System.out.println("  IT IS A THREAD");
						isSuperClass = true;
						return true;
					}		
				}
 
			}else if (descriptor instanceof ChildPropertyDescriptor) {
				if(isSuperClass)
					return true;
				ChildPropertyDescriptor child = (ChildPropertyDescriptor) descriptor;
				ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
				if (childNode != null) {
					if(isSuperClassType(child.getId())){
						classType = "superclassType";
					} 
					isThread(childNode);
				}
			} else  {

				if(isSuperClass)
					return true;

				ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) descriptor;
				if(node!=null){
					printIsThread((List) node.getStructuralProperty(list));
				}
			}
		}
		if(isSuperClass)
			return true;
		else
			return false;

	}


	public static boolean isThread(ASTParser parser ){
		ASTNode node= parser.createAST(null);
		return isThread(node);  
 	}

	public static void main(String []args){

		String file = ".\\src\\heartbeat\\SharedObject.java";
		String source = Utility.getSourceFileContent(file);


		//ASTParser parser = Utility.getParsedSource(source);			 
		//final CompilationUnit cu = Utility.getCompilationUint(parser);

		ASTParser parser= ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());

		ASTNode node= parser.createAST(null);

		// to chekc if the class is a thread
		System.out.println( " THREAD = " + isThread(node));

		// To parse full file
		//ParseASTNode.getNodes(node);  


	}
}
