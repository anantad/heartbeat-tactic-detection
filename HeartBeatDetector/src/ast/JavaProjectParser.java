package ast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jface.text.Document;

public class JavaProjectParser {


	public static boolean testIt() throws CoreException {
		String projectName = "AST_Example";
		IJavaProject javaProject = getJavaProject(projectName);

		String targetMethodName = "world";
		SearchPattern pattern = SearchPattern.createPattern(
				targetMethodName, 
				IJavaSearchConstants.METHOD, 
				IJavaSearchConstants.REFERENCES,
				SearchPattern.R_CASE_SENSITIVE // <--- ????
				);

		boolean includeReferencedProjects = false;
		IJavaElement[] javaProjects = new IJavaElement[] {javaProject};
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(javaProjects, includeReferencedProjects); // <--- ????

		SearchRequestor requestor = new SearchRequestor() {


			@Override
			public void acceptSearchMatch(SearchMatch match) throws CoreException {
				System.out.println(match.getElement());				
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(
				pattern, 
				new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant()}, 
				scope, 
				requestor, 
				null);      
		
		return true;
	}

	private static IJavaProject getJavaProject(String projectName) throws CoreException
	{
		IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();
		IProject project= root.getProject(projectName);
		if (!project.exists()) {
			project.create(null);
		} else {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		}

		if (!project.isOpen()) {
			project.open(null);
		}

		IJavaProject jproject= JavaCore.create(project);

		//added by Ananta
		printProjectInfo(project);
		
		
		return jproject;    
	}



	private static void printProjectInfo(IProject project) throws CoreException,
	JavaModelException {
		System.out.println("Working in project " + project.getName());
		// check if we have a Java project
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			printPackageInfos(javaProject);
		}
	}

	private static void printPackageInfos(IJavaProject javaProject)
			throws JavaModelException {
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			// Package fragments include all packages in the
			// classpath
			// We will only look at the package from the source
			// folder
			// K_BINARY would include also included JARS, e.g.
			// rt.jar
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				System.out.println("Package " + mypackage.getElementName());
				printICompilationUnitInfo(mypackage);

			}

		}
	}

	private static void printICompilationUnitInfo(IPackageFragment mypackage)
			throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			printCompilationUnitDetails(unit);

		}
	}

	private static void printIMethods(ICompilationUnit unit) throws JavaModelException {
		IType[] allTypes = unit.getAllTypes();
		for (IType type : allTypes) {
			printIMethodDetails(type);
			getMethodsTrace(type);
		}
	}

	private static void printCompilationUnitDetails(ICompilationUnit unit)
			throws JavaModelException {
		System.out.println("Source file " + unit.getElementName());
		Document doc = new Document(unit.getSource());
		System.out.println("Has number of lines: " + doc.getNumberOfLines());
		printIMethods(unit);
	}

	private static void printIMethodDetails(IType type) throws JavaModelException {
		IMethod[] methods = type.getMethods();
		for (IMethod method : methods) {

			System.out.println("Method name " + method.getElementName());
			System.out.println("Signature " + method.getSignature());
			System.out.println("Return Type " + method.getReturnType());

		}
	}

	private static void getMethodsTrace(IType type) throws JavaModelException{
		
		
		CallHierarchyGenerator callGen = new CallHierarchyGenerator();

		IMethod m = callGen.findMethod(type, "world");
		Set<IMethod> methods = new HashSet<IMethod>();
		methods = callGen.getCallersOf(m);
		for (Iterator<IMethod> i = methods.iterator(); i.hasNext();)
		{
			System.out.println(i.next().toString());
		}

	}

	public static void main(String []a)
	{
		
		/*String source = Utility.getSourceFileContent(".\\src\\heartbeat\\Controller.java");
		ASTParser parser = Utility.getParsedSource(source);
		parser.setResolveBindings(true);
 		final CompilationUnit cu = Utility.getCompilationUint(parser);
 		
 		printCompilationUnitDetails(cu);
 		*/
 		 
		
		try {
			testIt();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}