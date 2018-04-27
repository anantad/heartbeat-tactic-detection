package ast;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;


public class ParseJavaProject extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		// Loop over all projects
		for (IProject project : projects) {
			try {
				if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {

					IPackageFragment[] packages = JavaCore.create(project)
							.getPackageFragments();
					// parse(JavaCore.create(project));
					for (IPackageFragment mypackage : packages) {
						if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
							for (ICompilationUnit unit : mypackage
									.getCompilationUnits()) {
								// Now create the AST for the ICompilationUnits
								final CompilationUnit cu = parse(unit);
								/*MethodVisitor visitor = new MethodVisitor();
								parse.accept(visitor);

								for (MethodDeclaration method : visitor.getMethods()) {
									System.out.print("Method name: "
											+ method.getName()
											+ " Return type: "
											+ method.getReturnType2());
								} 
*/								
								cu.accept(new ASTVisitor() {
									 
									Set names = new HashSet();
						 

									public boolean visit(MethodDeclaration method) {
										 
										System.out.print("Method name: "
												+ method.getName()
												+ " Return type: "
												+ method.getReturnType2());
										return false; // do not continue 
									}
						
									/*public boolean visit(VariableDeclarationFragment node) {
										 
										SimpleName name = node.getName();
										this.names.add(name.getIdentifier());
										System.out.println("Declaration of '" + name + "' at line"
												+ cu.getLineNumber(name.getStartPosition()));
										return false; // do not continue 
									}
						 
									public boolean visit(SimpleName node) {
										if (this.names.contains(node.getIdentifier())) {
											System.out.println("Usage of '" + node + "' at line "
													+ cu.getLineNumber(node.getStartPosition()));
										}
										return true;
									}*/
								});
						 


							}
						}

					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the
	 * Java source file
	 *
	 * @param unit
	 * @return
	 */

	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
}



