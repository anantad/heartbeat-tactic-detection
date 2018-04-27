package ast;



 
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.util.*;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

public class CallHierarchyGenerator {
	public HashSet<IMethod> getCallersOf(IMethod m) {

		CallHierarchy callHierarchy = CallHierarchy.getDefault();

		IMember[] members = { m };

	/*	MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		HashSet<IMethod> callers = new HashSet<IMethod>();
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			HashSet<IMethod> temp = getIMethods(mw2);
			callers.addAll(temp);
		}*/
		MethodWrapper mw = callHierarchy.getCallerRoot(m);
		HashSet<IMethod> callers = new HashSet<IMethod>();
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			HashSet<IMethod> temp = getIMethods(mw2);
			callers.addAll(temp);
		
		return callers;
	}

	HashSet<IMethod> getIMethods(MethodWrapper[] methodWrappers) {
		HashSet<IMethod> c = new HashSet<IMethod>();
		for (MethodWrapper m : methodWrappers) {
			IMethod im = getIMethodFromMethodWrapper(m);
			if (im != null) {
				c.add(im);
			}
		}
		return c;
	}

	IMethod getIMethodFromMethodWrapper(MethodWrapper m) {
		try {
			IMember im = m.getMember();
			if (im.getElementType() == IJavaElement.METHOD) {
				return (IMethod) m.getMember();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	IMethod findMethod(IType type, String methodName) throws JavaModelException
	{
		//IType type = project.findType(typeName);

		IMethod[] methods = type.getMethods();
		IMethod theMethod = null;

		for (int i = 0; i < methods.length; i++)
		{
			IMethod imethod = methods[i];
			if (imethod.getElementName().equals(methodName)) {
				theMethod = imethod;
			}
		}

		if (theMethod == null)
		{           
			System.out.println("Error, method" + methodName + " not found");
			return null;
		}

		return theMethod;
	}
 

	private void getMethodsTrace(IType type) throws JavaModelException{
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
		
	}

 
}