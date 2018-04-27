/* 
 * 
 * TRY BELOW CODE TO SEE USE OF BINDING
 * 
 * 
Find the corresponding compilation unit via binding of the method, parse it to another AST and get the declaration from the tree:

IMethodBinding binding = (IMethodBinding) node.getName().resolveBinding();
ICompilationUnit unit = (ICompilationUnit) binding.getJavaElement().getAncestor( IJavaElement.COMPILATION_UNIT );
if ( unit == null ) {
   // not available, external declaration
}b
ASTParser parser = ASTParser.newParser( AST.JLS8 );
parser.setKind( ASTParser.K_COMPILATION_UNIT );
parser.setSource( unit );
parser.setResolveBindings( true );
CompilationUnit cu = (CompilationUnit) parser.createAST( null );
MethodDeclaration decl = (MethodDeclaration)cu.findDeclaringNode( binding.getKey() );
 *
 *
 *
 *
 */


package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.internal.eval.VariablesInfo;
import org.eclipse.jdt.internal.ui.propertiesfileeditor.PropertyValueScanner.AssignmentDetector;



public class ParseASTVisitor {

	private static List<MethodDeclaration> methodDeclarationList = new ArrayList<MethodDeclaration>();
	private static List<VariableDeclarationFragment> variableDeclarationFragmentList = new ArrayList<VariableDeclarationFragment>();
	private static List<MethodInvocation> methodInvocationList = new ArrayList<MethodInvocation>();
	private static List<String> forStatements = new ArrayList<String>();
	private static List<String> variableDeclarationFragments = new ArrayList<String>();
	private static Map<String,String> methodDeclarationWithClassMap = new HashMap<String,String>();
	private static Map<String,String> instanceVariableDeclarationWithClassMap = new HashMap<String,String>();
	private static List<String> classInstanceCreations = new ArrayList<String>();
	private static List<Assignment> assignmenrtStatements = new ArrayList<Assignment>();
	private static Map<WhileStatement,String> whileStatements = new HashMap<WhileStatement,String>();
	private static List<WhileStatement> whileStatement = new ArrayList<WhileStatement>();

	public static final int VARIABLE_DECLARATION_STATEMENT = 60;
	public static final int FIELD_DECLARATION = 23;



	public static void parseNodes(final CompilationUnit cu , String className){
		Set names = new HashSet();

		cu.accept(new ASTVisitor() {
			//by add more visit method like the following below, then all king of statement can be visited.
			/*
			public boolean visit(SimpleType type) {
				System.out.println("\n<<----------------- SimpleType ------------------------>>");
				System.out.println(" Node type ID : " + type.getNodeType());
				System.out.println(" Node type name: " + type.getName());
				return false;
			}*/


			/*public boolean visit(SimpleName node) {
				System.out.println("\n<<----------------- SimpleName ------------------------>>");
				System.out.println(" FullyQualifiedName of SimpleNode : " + node.getFullyQualifiedName());
				System.out.println(" getNodeType : " + node.getNodeType());				
				System.out.println("getRoot().getNodeType() : " + node.getRoot().getNodeType()); 
				System.out.println("getClass : " + node.getClass().getName()); 
				if (node.getIdentifier()!=null)
					System.out.println(" Usage of '" + node + "' at line " + cu.getLineNumber(node.getStartPosition()));
				return false;
			}*/

			/*public boolean visit(ForStatement node) {
				System.out.println("\n -----------------------------------------");
				System.out.println("\n ForStatement content : " + node.toString() );
				System.out.println("\n -----------------------------------------");
				ArrayList<Integer> al = new ArrayList<Integer>();
				al.add(node.getStartPosition());
				al.add(node.getLength()); 
				return true;
			}
			 */
			/*public boolean visit(VariableDeclaration variableDeclaration) {
				System.out.println("\n<<----------------- VariableDeclaration ------------------------>>");
				System.out.println("getName = " +  variableDeclaration.getName());
				System.out.println("getLocationInParent = " +  variableDeclaration.getLocationInParent());
 				return false;
 			}*/
			public boolean visit(MethodDeclaration method) {
				methodDeclarationList.add(method);
				methodDeclarationWithClassMap.put(method.getName().toString(), className);

				//printMethodDeclaration(method);
				return true; // false --> do not continue , true -->continue 
			}
			public boolean visit(MethodInvocation methodInvocation) {
 				methodInvocationList.add(methodInvocation);				
				//printMethodInvocation(methodInvocation);
				return true;
			}

			@SuppressWarnings("unchecked")
			public boolean visit(VariableDeclarationFragment node) {

				variableDeclarationFragmentList.add(node);
				variableDeclarationFragments.add( node.getName().toString()); 

				if(node.getParent().getNodeType() == VARIABLE_DECLARATION_STATEMENT)
				{
					System.out.println(node.getName().toString() + " IS A LOCAL VARIABLE ");
				} else if (node.getParent().getNodeType() == FIELD_DECLARATION){
					System.out.println(node.getName().toString() + " IS A MEMBER VARIABLE ");

					instanceVariableDeclarationWithClassMap.put( node.getName().toString(), className);
				}  
				//printVariableDeclarationFragment(node);
				return false;  
			}

			public boolean visit(ClassInstanceCreation className) {
				//System.out.println("\n<<---------------ClassInstanceCreation -------------------------->>");
				//System.out.print( "\n  ClassInstance :  "+className.getParent().toString() +" with arguments =  "+className.arguments());
				return true; 
			}  
			public boolean visit(VariablesInfo  variablesInfo) {
				//System.out.println("\n<<---------------VariablesInfo -------------------------->>");
				//System.out.println( "\n  VariablesInfo :  "+ variablesInfo );
				return true; 
			}
			public boolean visit(AssignmentDetector  assignmentDetector) {
				/*System.out.println("\n<<---------------AssignmentDetector -------------------------->>");
				System.out.println( "\n  AssignmentDetector :  "+ assignmentDetector.toString() );
				*/return true; 
			}

			public boolean visit(MethodRef  methodRef) {
			/*	System.out.println("\n<<---------------MethodRef -------------------------->>");
				System.out.println( "\n  MethodRef :  "+ methodRef.toString() );
			*/	return true; 
			}
			public boolean visit(WhileStatement  st) {
				//System.out.println("\n<<---------------WhileStatement -------------------------->>");
				//System.out.println( "\n  WhileStatement getBody  :  "+ st.getBody() );

				//whileStatement.add(st);
				whileStatements.put(st,className);
				return true; 
			}
		/*	public boolean visit(ExpressionStatement  expressionStatement) {
				System.out.println("\n<<---------------ExpressionStatement -------------------------->>");
				System.out.println( "\n  ExpressionStatement getBody  :  "+ expressionStatement.toString() );
				return true; 
			}*/
			public boolean visit(ExpressionMethodReference  expressionMethodReference) {
				System.out.println("\n<<---------------ExpressionMethodReference -------------------------->>");
				System.out.println( "\n  ExpressionMethodReference getBody  :  "+ expressionMethodReference.toString() );
				return true; 
			}

			public boolean visit(Assignment  assignment) {
				//System.out.println("\n<<---------------Assignment -------------------------->>");
				//System.out.println( "\n  Assignment getBody  :  "+ assignment.toString() );
				assignmenrtStatements.add( assignment);
				return true; 
			} 
		});
		//adding while loops
		/*if(whileStatement.size()>0)
			whileStatements.put(whileStatement,className);
		whileStatement.clear();*/
	}

	public static void printvariableDeclarationFragment(){
 
		System.out.println("\n<<---------------List OF  variableDeclarationFragments-------------------------->>");
		for(String variableDeclarationFragment:variableDeclarationFragments){
			System.out.println(  variableDeclarationFragment   );
		}
	}	

	public static Map<WhileStatement ,String> getWhileStatements(){
		return  whileStatements ;
	}
	
	public static List<Assignment > getAssignmenrtStatements(){
		return assignmenrtStatements;
	}

	public static List<MethodDeclaration>  getMethodDeclarationNodes(){
		return methodDeclarationList;
	}
	public static List<VariableDeclarationFragment>  getVariableDeclarationNodes(){
		return variableDeclarationFragmentList;
	}
	public static List<MethodInvocation>  getMethodInvocationList(){
		return methodInvocationList;
	}
	public static Map<String,String>  getMethodDeclarationWithClassMap(){
		return methodDeclarationWithClassMap;
	}
	public static Map<String,String> getInstanceVariableDeclarationWithClassMap  (){
		return instanceVariableDeclarationWithClassMap;
	}



	/*private static void callHierarchy(){

	 CallHierarchy callHierarchy = CallHierarchy.getDefault();
	    IMember[] members = { method };
	    MethodWrapper[] callers = callHierarchy.getCallerRoots(members);

	}/
	 */

	private static void printVariableDeclarationFragment(VariableDeclarationFragment node)	{
		System.out.println("\n<<----------------- VariableDeclarationFragment ------------------------>>");
		SimpleName name = node.getName();
		System.out.println(" Declaration of '" + name );//+ "' at line"	+ cu.getLineNumber(name.getStartPosition()));


		if(node.getParent().getNodeType() == VARIABLE_DECLARATION_STATEMENT)
		{
			System.out.println(" IS A LOCAL VARIABLE ");
		} else if (node.getParent().getNodeType() == FIELD_DECLARATION){
			System.out.println(" IS A MEMBER VARIABLE "); 	
		}
		//System.out.println("getInitializerProperty = " +  node.getInitializerProperty()); 	

	}

	private static void printMethodInvocation(MethodInvocation methodInvocation){

		System.out.println("\n<<----------------- MethodInvocation ------------------------>>");


		SimpleName name = methodInvocation.getName();
		System.out.println(" Method Invocation of '" + name);// + "' at line"	+ cu.getLineNumber(name.getStartPosition()));
	/*	System.out.println(" getNodeType " + methodInvocation.getNodeType());
		System.out.println(" getParent " + methodInvocation.getParent());
		System.out.println(" arguments " + methodInvocation.arguments());
		System.out.println(" getExpression " + methodInvocation.getExpression());

		System.out.println(" getAST " + methodInvocation.getAST());
		IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
		if(methodBinding!=null && methodBinding.getMethodDeclaration()!=null)
			System.out.println(" ................. "+ methodBinding.getMethodDeclaration().getDeclaringClass().getName());

		if((ASTNode)methodInvocation.getExpression() instanceof ClassInstanceCreation ){
			System.out.println(" getExpression() instanceof ClassInstanceCreation  "  );
		}

		System.out.println("	methodInvocation.getExpression().structuralPropertiesForType() "+	methodInvocation.getExpression().structuralPropertiesForType() );

		if( methodInvocation.getExpression().structuralPropertiesForType() != null){
			System.out.println(" getExpression() instanceof ClassInstanceCreation  "  );
		}
*/
	}


	private static void printMethodDeclaration(MethodDeclaration method){
		System.out.println("\n<<---------------MethodDeclaration -------------------------->>");
		System.out.println("\nMethod name is : '" + method.getName()  +
				"\n'   getReturnType2 : "	+ method.getReturnType2()+
				"\n'   getModifiers: "	+ method.getModifiers()+
				"\n'   parameters: "	+ method.parameters()+
				"\n'   getLocationInParent: "	+ method.getLocationInParent()+
				"\n'   method.getBody().statements(): "	+method.getBody().statements()

				);


	}
	public static void main(String []a)	{
		String className = ".\\src\\heartbeat\\Controller.java";
		String source = Utility.getSourceFileContent(className);
		ASTParser parser = Utility.getParsedSource(source);
		parser.setResolveBindings(true);
		final CompilationUnit cu = Utility.getCompilationUint(parser);
		ParseASTVisitor.parseNodes(cu,className );  

	}
}
