package ast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
 
public class EclipseRefactoringAPIDemo extends Thread{ 
	
	public void processJavaFile(File file) throws IOException, MalformedTreeException, BadLocationException {
	    String source = FileUtils.readFileToString(file);
	    Document document = new Document(source);
	    ASTParser parser = ASTParser.newParser(AST.JLS3);
	    parser.setSource(document.get().toCharArray());
	    CompilationUnit unit = (CompilationUnit)parser.createAST(null);
	    unit.recordModifications();

	    // to get the imports from the file
	    List<ImportDeclaration> imports = unit.imports();
	    for (ImportDeclaration i : imports) {
	        System.out.println(i.getName().getFullyQualifiedName());
	    }

	    // to create a new import
	    AST ast = unit.getAST();
	    ImportDeclaration id = ast.newImportDeclaration();
	    String classToImport = "ast.Readable";
	    
	    id.setName(ast.newName(classToImport.split("\\.")));
	    unit.imports().add(id); // add import declaration at end

	    // to save the changed file
	    TextEdit edits = unit.rewrite(document, null);
	    edits.apply(document);
	    FileUtils.writeStringToFile(file, document.get());

	    // to iterate through methods
	    List<AbstractTypeDeclaration> types = unit.types();
	    for (AbstractTypeDeclaration type : types) {
	        if (type.getNodeType() == ASTNode.TYPE_DECLARATION) {
	            // Class def found
	            List<BodyDeclaration> bodies = type.bodyDeclarations();
	            for (BodyDeclaration body : bodies) {
	                if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
	                    MethodDeclaration method = (MethodDeclaration)body;
	                    System.out.println("name: " + method.getName().getFullyQualifiedName());
	                }
	            }
	        }
	    }
	}
	
	//use ASTParse to parse string
	@SuppressWarnings("deprecation")
	public static void parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		  
		   
/*		cu.accept(new ASTVisitor() {
 
			Set names = new HashSet();
 
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line"	+ cu.getLineNumber(name.getStartPosition()));
				return false; // do not continue 
			}
 
			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line " + cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}
		});
		
*/		//ananta
		 AST ast = cu.getAST();
		 ClassInstanceCreation classInstanceCreation = ast.newClassInstanceCreation();
		 System.out.println("classInstanceCreation.getName = "+  classInstanceCreation.getParent().toString());

		
	
	}
 
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
 
	//loop directory to get file list
	public static void ParseFilesInDir() throws IOException{
		File dirs = new File(".");
		String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator+"ast"+File.separator;
 
		File root = new File(dirPath);
		//System.out.println(rootDir.listFiles());
		File[] files = root.listFiles ( );
		String filePath = null;
 
		 for (File f : files ) {
			 filePath = f.getAbsolutePath();
			 if(f.isFile()){
				 parse(readFileToString(filePath));
			 }
		 }
	}
	public static void main(String[] args) throws Exception {
	
		//1.
		//EclipseRefactoringAPIDemo test = new EclipseRefactoringAPIDemo();
		//test.processJavaFile(new File("Readable.java"));
		
		//2.
		//ParseFilesInDir();
		
		//3.
		new ParseJavaProject().execute(null);
		
		
	}
	
}
