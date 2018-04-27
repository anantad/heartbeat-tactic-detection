package ast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hbtengine.HBTclass;
import hbtengine.HBTelements;

public class Main {

	public static void main(String []args){

		String dirPath =null;
		File dirs = new File(".");
		List<String> files = new ArrayList<String>();

		try {
			files = Utility.getFilesInDir("testpkg");
		} catch (IOException e) { 
			e.printStackTrace();
		}try {
			dirPath = dirs.getCanonicalPath() + File.separator+"output"+File.separator;
		} catch (IOException e) {
			e.printStackTrace();
		}


		HBTelements hbtEelements = HBTelements.getInstance();
		hbtEelements.setPackageNames("heartbeat");


		for(String file: files){
			String source = Utility.getSourceFileContent(file);
			ASTParser parser = Utility.getParsedSource(source);
			ASTNode astNode= parser.createAST(null);

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ AST of "+ file +" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			String fileName = file.substring(file.lastIndexOf('\\'), file.length() );
			fileName = fileName.substring(0, fileName.indexOf('.'));
			String outputfile= dirPath+ fileName + ".txt" ;
			ParseASTNode.setoutputfile(outputfile);

			HBTclass hbtClasses = new HBTclass();
			hbtClasses.setClassname(fileName);
			hbtEelements.getHBTClasses().add(hbtClasses);

			ParseASTNode.getNodes(astNode);
		}	



	}

}
