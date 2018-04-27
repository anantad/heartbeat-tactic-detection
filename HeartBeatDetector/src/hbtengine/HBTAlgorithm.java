package hbtengine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import ast.ParseASTNode;
import ast.ParseASTVisitor;
import ast.TreeNode;
import ast.Utility;

public class HBTAlgorithm {


	private static final int VARIABLE_DECLARATION_STATEMENT = 60; //local variable
	private static final int FIELD_DECLARATION = 23; // instance variables

	private static File dirs = new File(".");
	private static String dirPath =null;	
	private static List<String> files = new ArrayList<String>();

	//AST elements
	private static List<MethodDeclaration> methodDeclarationList = new ArrayList<MethodDeclaration>();
	private static List<VariableDeclarationFragment> variableDeclarationFragmentList = new ArrayList<VariableDeclarationFragment>();
	private static List<MethodInvocation> methodInvocationList = new ArrayList<MethodInvocation>();
	private static Map<String,String> methodDeclarationWithClassMap = null;
	private static Map<String,String> assigmentWithMethodMap = new HashMap<String,String>();
	private static Map<String,Boolean> classIsThread= new HashMap<String,Boolean>();
	private static Map<String,Map<String,List<String>>> allCallflows= new HashMap<String,Map<String,List<String>>>();
	//Map<Class,,Map<DeclaredMethod,List<InvokedMethodFlow>>>

	//HBT elements
	private static List<HBTclass> hbtClassesList = new ArrayList<HBTclass>();	
	//private static List<HBTmethod> hbtMethodsDeclarationList = new ArrayList<HBTmethod>(); 
	private static List<HBTmembervariable>  hbtMembervariablesList = new ArrayList<HBTmembervariable>(); 
	//Map<ClassName,List<Map<CallerMethod,CalledMethod>>>
	private static List<HBTDeclaredMethod>  hbtDeclaredMethods = new ArrayList<HBTDeclaredMethod>();
	private static List<HBTInvokedMethod>  hbtInvokedMethods = new ArrayList<HBTInvokedMethod>();

	private static Map<String,String> callerOfMethodMap = new HashMap<String,String>();
	private static Map<String,WhileStatement> methodInLoopMap = new HashMap<String,WhileStatement>();

	private static List<String> getListOfFiles(String heartbeat_testcase_pkg){
		try {

			files = Utility.getFilesInDir(heartbeat_testcase_pkg);
			dirPath = dirs.getCanonicalPath() + File.separator+"output"+File.separator;
		} catch (IOException e) { 
			e.printStackTrace();
		}
		return files;
	}
	private static void saveParsedOutput(String file){
		String fileName = file.substring(file.lastIndexOf('\\'), file.length() );
		fileName = fileName.substring(0, fileName.indexOf('.'));
		String outputfile= dirPath+ fileName + ".txt" ;
		ParseASTNode.setoutputfile(outputfile);

	}

	private static void  getAllinvokedMethodsFromInsideTheMethod(MethodDeclaration methodDeclaration, String className){
		List<MethodInvocation> methodInvocationList  = ParseASTVisitor.getMethodInvocationList();

		//System.out.println("\n !!!!  getAllinvokedMethodsFromInsideTheMethod methodDeclaration = " + methodDeclaration.getName().toString() );
	 	//System.out.println("!!!!  getAllinvokedMethodsFromInsideTheMethod  methodDeclaration.getBody() = " + methodDeclaration.getBody().toString() );

		for(MethodInvocation  methodInvocation :methodInvocationList){
			 if(methodDeclaration.getBody().toString().contains(methodInvocation.getName().toString())){
				 
				// System.out.println("!!!!  getAllinvokedMethodsFromInsideTheMethod  BODY CONTAIN = " + methodInvocation.getName().toString() );
				 	
 				//HBTmethod hbtMethod = new HBTmethod();
				//hbtMethod.setMethodName(methodInvocation.getName().toString());
				HBTInvokedMethod  hbtInvokedMethod = new HBTInvokedMethod();

				hbtInvokedMethod.setMethodName(methodInvocation.getName().toString());
				hbtInvokedMethod.setArguments(methodInvocation.arguments().toString());
				hbtInvokedMethod.setCallerMethod(methodDeclaration.getName().toString());
				hbtInvokedMethod.setClassName(className);
				hbtInvokedMethods.add(hbtInvokedMethod);

			}
		}		
	}


	private static String isInfiniteLoop(String methodBody){
		// Verify with APROVE TOOL if this method/loop is infinite 
		return "T";
	}

	private static boolean methodBodyContaisLoop(String methoodBody , String loop){

		String s1 = methoodBody.replaceAll("\\s", "");
		String s2 = loop.replaceAll("\\s", "");

		//System.out.println(" !!!! methodBodyContaisLoop  s1 = "+ s1);
		//System.out.println(" !!!! methodBodyContaisLoop  s2 = "+ s2);

		if(s1.contains(s2))
			return true;
		else 
			return false;
	}

	private static HBTDeclaredMethod  getAllLoopsFromInsideTheMethod(HBTDeclaredMethod hbtDeclaredMethod , MethodDeclaration method, String className){
		Map<WhileStatement,String> whileStatementsMap =  ParseASTVisitor.getWhileStatements();
		Map<String,String> loop = new HashMap<String,String>();  
		//System.out.println(" !!!! getAllLoopsFromInsideTheMethod .. method =" + method.getName() + "  class =" +className);

		for (Entry<WhileStatement, String> entry : whileStatementsMap.entrySet()) {
			if(className.equals(entry.getValue())){		    	

				if(methodBodyContaisLoop(method.getBody().toString() , entry.getKey().toString())){
					System.out.println(" !!!! LOOP =  " + entry.getKey());
					//loop.put(whileStatement.toString(), isInfiniteLoop(method.getBody().toString()));
					String isInfinite = isInfiniteLoop(method.getBody().toString());
					if(isInfinite.equals("T")){
						hbtDeclaredMethod.getLoop().put(entry.getKey().toString(),isInfinite);	
						 
						System.out.println(" className.method.=" +className +"." +method.getName().toString() +" , whileStatement = "+entry.getKey().toString() );
						methodInLoopMap.put(className +"." +method.getName().toString() , entry.getKey()); // method.getName() is parent method which contains infinite while loop
					}
				}
			}
		}


		return   hbtDeclaredMethod;
	}

	private static List<HBTDeclaredMethod> populateMethodDeclarations(String className){
		methodDeclarationList = ParseASTVisitor.getMethodDeclarationNodes();

		for(MethodDeclaration method:methodDeclarationList){ 
			HBTDeclaredMethod hbtDeclaredMethod = new HBTDeclaredMethod();   
			hbtDeclaredMethod.setMethodName(method.getName().toString());
			if(method.getReturnType2()!=null)
				hbtDeclaredMethod.setReturnType(method.getReturnType2().toString());
			hbtDeclaredMethod.setArguments(method.typeParameters().toString());
			hbtDeclaredMethod.setModifier(method.getModifiersProperty().toString());
			hbtDeclaredMethod.setClassName(className);
			//printHBTDeclaredMethod(hbtDeclaredMethod);
			//Adding Loops
			hbtDeclaredMethod = getAllLoopsFromInsideTheMethod(hbtDeclaredMethod, method,className);

			//Adding invoked methods
			getAllinvokedMethodsFromInsideTheMethod(method , className);
			hbtDeclaredMethods.add(hbtDeclaredMethod); 
		} 
		return hbtDeclaredMethods ;
	}

	private static void populateInstanceVariables(){
		variableDeclarationFragmentList = ParseASTVisitor.getVariableDeclarationNodes();
		HBTmembervariable hbtMembervariables =  new HBTmembervariable();

		for(VariableDeclarationFragment node:variableDeclarationFragmentList){

			if(  node.getParent().getNodeType() ==FIELD_DECLARATION)	{
				hbtMembervariables.setVariableName(node.getName().toString());


				//hbtMembervariables.setAlias();//TODO
				/*hbtMembervariables.setReadingFromIt();			
			//hbtMembervariables.setStatic();//TODO			
			hbtMembervariables.setWritingIntoIt();
			hbtMembervariables.setReadingFromIt();
			hbtMembervariables.setReadingVariableName();*/

				//printVariablesNode(node );
			}
		}
	}

	private static void printVariablesNode(VariableDeclarationFragment node){
		SimpleName name = node.getName();
		System.out.println(" node.getName() " + node.getName());
		System.out.println(" node.getNodeType() " + node.getNodeType());
		System.out.println(" node.properties() " + node.properties());
		System.out.println(" node.getInitializer() " + node.getInitializer());
		if(node.getInitializer()!=null)
			System.out.println(" node.getInitializer().getNodeType() " + node.getInitializer().getNodeType());
		System.out.println(" node.getInitializer().getExtraDimensions() " + node.getExtraDimensions());
		System.out.println(" node.getExtraDimensions2Property() " + node.getExtraDimensions2Property());
		System.out.println("getLocationInParent = " +  node.getLocationInParent()); 

		System.out.println("\n<<----------------- ####### ------------------------>>");

	} 


	static TreeNode<String> methodTreeRoot = new TreeNode<>("HBT"); 

	static void printHBTDeclaredMethod(HBTDeclaredMethod hbtDeclaredMethod){

		//hbtDeclaredMethod
		System.out.println("~ hbtDeclaredMethod.getMethodName = " + hbtDeclaredMethod.getMethodName());
		System.out.println("~ hbtDeclaredMethod.getReturnType ="+hbtDeclaredMethod.getReturnType());
		System.out.println("~ hbtDeclaredMethod.getArguments = " + hbtDeclaredMethod.getArguments());
		System.out.println("~ hbtDeclaredMethod.getModifier = " + hbtDeclaredMethod.getModifier());
		System.out.println("~ hbtDeclaredMethod.getClassName ="+hbtDeclaredMethod.getClassName());


	}

	static void getInstanceAndLocalVariablePair(){

		List<Assignment> assignmenrtStatements =  ParseASTVisitor.getAssignmenrtStatements();
		Map<String,String> instanceVariableDeclarationWithClassMap =ParseASTVisitor.getInstanceVariableDeclarationWithClassMap();
		Map<String,String> assignmentMap = new HashMap<String,String>(); 

		List<String> lhs_InstanceVar = new ArrayList<String>() ;		
		List<String> rhs_localVar = new ArrayList<String>() ;

		// check if left hand side is a INSTANCE variable and right hand side is a LOCAL variable 
		// means..  the sender is sending the LOCAL variable's value through method calls and  receiver might be receiving  it in  the INSTANCE variable 
		// through some method call.  by processing the call hierarchy , declared method and instance method we can make out the send and 
		// receiver in this scenario. 

		for(Assignment assignment :assignmenrtStatements ){		
			assignmentMap.put(assignment.getLeftHandSide().toString(), assignment.getRightHandSide().toString());
			lhs_InstanceVar.add(assignment.getLeftHandSide().toString());
			rhs_localVar.add(assignment.getRightHandSide().toString()); 
		}
		for(String lsh_inst_Var: lhs_InstanceVar){
			String instance_Var_Class = instanceVariableDeclarationWithClassMap.get(lsh_inst_Var);
			if( instance_Var_Class != null){
				//System.out.println("\n  FOUND LHS '" + lsh_inst_Var + "' in " + instance_Var_Class + ", IT can be a receiver") ;

				String rhs_local_Var = assignmentMap.get(lsh_inst_Var);
				//System.out.println("\n  THE RHS  is '" + rhs_local_Var + "'\n" );

				//get Method name where this assignment is available
				for(MethodDeclaration method:methodDeclarationList){ 
 					if(isMethodBodyContainsAssignments(lsh_inst_Var,rhs_local_Var, method)){
 
 						String method_name = method.getName().toString();
						//add assigmentStament , className.Methodname in a map
						assigmentWithMethodMap.put( lsh_inst_Var+"="+rhs_local_Var ,method_name );
						System.out.println("\n  ASSIGNMENT IS :  " +  lsh_inst_Var + "=" +rhs_local_Var+ " IN  METHOD : "
								+ method.getName().toString() + " IN CLASS " + methodDeclarationWithClassMap.get(method_name));
 						System.out.println("\n ................................................................................. ");

						//backtrack call flow with rhs_local_Var to find sender of this.
						if(method.parameters().toString().contains(rhs_local_Var)){
							checkLocalVariableInCallFlowToFindSender(methodDeclarationWithClassMap.get(method_name)+"."+method_name, rhs_local_Var);

						}
						//Find for a Receiver who uses lsh_inst_Var and resets
						checkInstanceVariableinCallFlowToFindReceiver(lsh_inst_Var);
					}
				}
			}
		}
	}

	private static void checkInstanceVariableinCallFlowToFindReceiver(String lsh_inst_Var) {
 		
	}
	private static String getMethodAruments( String caller  ){

		String method_name = caller.substring(caller.indexOf(".")+1,caller.length());

		for(MethodDeclaration method:methodDeclarationList){ 
			if(method.getName().toString().equals(method_name))
				return method.parameters().toString();
		}
		return null;
	}

	private static void checkIfMethodCalledFromThread(String methodName){
		String calssName = methodName.substring(0, methodName.indexOf("."));
		System.out.println("checkIfMethodCalledFromThread  methodName = " + methodName );
		System.out.println("checkIfMethodCalledFromThread  calssName = " + calssName );
		if(classIsThread.get(calssName).booleanValue() == true){
			System.out.println("\n ******************************************************************");
			System.out.println("\n ******************** SENDER CLASS IS : " + calssName + "***********");
			System.out.println("\n ******************************************************************");

		}
	}
	private static void checkLocalVariableInCallFlowToFindSender(String called_method, String rhs_local_Var) {
		
		System.out.println("\n *****************************  checkLocalVariableInCallFlowToFindSender  *************************************");

		String caller = callerOfMethodMap.get(called_method);
		WhileStatement whileStatement =  methodInLoopMap.get(callerOfMethodMap.get(called_method));

/*		System.out.println("checkLocalVariableInCallFlowToFindSender  rhs_local_Var =  " + rhs_local_Var );		
		System.out.println("checkLocalVariableInCallFlowToFindSender  Called_method =  " + called_method );
		System.out.println("checkLocalVariableInCallFlowToFindSender  Caller = "+ caller);
		System.out.println("checkLocalVariableInCallFlowToFindSender  whileStatement = "+ whileStatement);
*/
		if( caller!=null && getMethodAruments(caller).contains(rhs_local_Var) ){
			 String temp_called_method = called_method.substring(called_method.indexOf(".")+1, called_method.length());
				System.out.println("checkLocalVariableInCallFlowToFindSender  temp_called_method = "+ temp_called_method);
 
			if( whileStatement != null && whileStatement.getBody().toString().contains(temp_called_method)){
				checkIfMethodCalledFromThread( callerOfMethodMap.get(called_method));		
				checkIfMethodCalledFromThread(callerOfMethodMap.get(callerOfMethodMap.get(called_method)));		 	
			}else{
				checkLocalVariableInCallFlowToFindSender(caller, rhs_local_Var);
			}
		}

	}

	private static boolean isMethodBodyContainsAssignments(String lsh_inst_Var, String rhs_local_Var,
			MethodDeclaration method) {
		String methodBody = method.getBody().toString();
		methodBody = methodBody.replaceAll("\\s", "");
		if(methodBody.contains(lsh_inst_Var + "="+ rhs_local_Var)){
			//System.out.println("\n  METHOD contains ASSIGNEMENT");
			return true;
		}
		return false;
	}

	static HBTclass hbtClass = new HBTclass();
	static void makecallflow(){

		for(HBTDeclaredMethod hbtDeclaredMethod :hbtDeclaredMethods){

			HBTmethod hbtDMethod = new HBTmethod(); 

			Map<String,List<String>> callflows= new  HashMap<String,List<String>>(); 
			List<String> callflow = new ArrayList<String>();
			methodDeclarationWithClassMap = ParseASTVisitor.getMethodDeclarationWithClassMap();
			String hbtDeclaredMethod_name = hbtDeclaredMethod.getMethodName();		
			String className = methodDeclarationWithClassMap.get(hbtDeclaredMethod_name);
			//callflow.add(className + "."+ hbtDeclaredMethod_name);
			List<HBTInvokedMethod> temp_hbtInvokedMethods = hbtInvokedMethods;	

			/*System.out.println("\n Adding class : " + className );
			System.out.println("\n #########  MAKING CALL FLOW FOR   : " + hbtDeclaredMethod_name );
			System.out.println("\n Adding Loop : " + hbtDeclaredMethod.getLoop().entrySet().toString() );
			 */
			hbtDMethod.setMethodName(hbtDeclaredMethod_name);
			hbtDMethod.setClassName(className);
			hbtDMethod.setLoop(hbtDeclaredMethod.getLoop());

			for(HBTInvokedMethod hbtInvokedMethod :temp_hbtInvokedMethods){
				if( hbtDeclaredMethod_name.equals(hbtInvokedMethod.getMethodName() /*  check for more conditions like return type etc.*/)){
					className = methodDeclarationWithClassMap.get(hbtInvokedMethod.getCallerMethod());
					//callflow.add(className + "." + hbtInvokedMethod.getCallerMethod());
					//System.out.println(" Adding  .....  hbtInvokedMethod.getMethodName()  : " + hbtInvokedMethod.getMethodName()  + "CALLER IS : " + hbtInvokedMethod.getCallerMethod());

					hbtDeclaredMethod_name = hbtInvokedMethod.getCallerMethod();
					HBTmethod hbtIMethod = new HBTmethod(); 
					hbtIMethod.setMethodName(hbtDeclaredMethod_name);
					hbtIMethod.setClassName(className);
					hbtDMethod.getInvokingMethods().add(hbtIMethod);
					temp_hbtInvokedMethods = hbtInvokedMethods;
				}
			}

			hbtClass.getDeclaredMethods().add(hbtDMethod);
			hbtClassesList.add(hbtClass);
			//callflows.put(hbtDeclaredMethod.getMethodName(), callflow);			
			//allCallflows.put(hbtDeclaredMethod.getClassName(), callflows);		 
		}
	}

	static void printCallflow(){

		String caller = null;
		String called = null;
		//for(HBTclass  h : hbtClassesList){
		//System.out.println("  Class : " +h.getClassname());			
		for(HBTmethod m : hbtClass.getDeclaredMethods() ){
			if(!m.getLoop().isEmpty()){
				System.out.println("  Method LOOP: " + m.getLoop());
			}

			caller = m.getClassName() + "." + m.getMethodName() ;
			System.out.println("  Declared Method  : " + caller );

			for(HBTmethod i : m.getInvokingMethods() ){
				called = i.getClassName() + "." +  i.getMethodName();
				
				callerOfMethodMap.put(caller,called );
				System.out.println("@@@  called = " +caller  +" caller = " + called  );
				caller = called;
			}
			//System.out.println(" #  callerOfMethodMap : " +callerOfMethodMap);
			System.out.println("\n ------------------------------------------------------------------\n");
			//}

		} 

	}

	private static void printAssigmentWithMethodMap() {
		System.out.println("\n  assigmentWithMethodMap	= " + assigmentWithMethodMap);
	}

	public static void main(String []args){

		//  Give the pkg name to be tested - test case one at a time.
		String heartbeat_testcase_pkg =  "heartbeat" ;//"heartbeat_testcase_pacemaker"; 
		getListOfFiles(heartbeat_testcase_pkg);

		HBTelements hbtEelements = HBTelements.getInstance();
		hbtEelements.setPackageNames(heartbeat_testcase_pkg);

		for(String file: files){
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ AST of "+ file +" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			saveParsedOutput(file); 
			String source = Utility.getSourceFileContent(file);
			ASTParser parser = Utility.getParsedSource(source);	

			String className = file.substring(file.lastIndexOf('\\'), file.length() );
			className = className.substring(1, className.indexOf('.'));

			//check if the class is a Thread
			ParseASTNode.resetclassType();
			ParseASTNode.resetisSuperClass();
			classIsThread.put(className, Boolean.valueOf(ParseASTNode.isThread(parser)));

			parser = Utility.getParsedSource(source);	
			final CompilationUnit cu = Utility.getCompilationUint(parser);

			//Parsing File 
			ParseASTVisitor.parseNodes(cu , className);

			//ParseASTNode.getNodes(astNode); // Use it later 
			populateMethodDeclarations(className);

			populateInstanceVariables();


			//print HBTElements
			//printHBTelements();

		}	

		System.out.println("\n **********************************  THREAD LIST ********************************");
		System.out.println( classIsThread);
		System.out.println("\n ****************************************  CALL FLOW  *****************************************************");
		makecallflow();
		printCallflow();
		System.out.println("\n ****************************************   INSTANCE AND LOCAL VARIABLE PAIR    *****************************************************");

 		getInstanceAndLocalVariablePair(); 
 
		//createMethodTree(methodTreeRoot);

	}
}
