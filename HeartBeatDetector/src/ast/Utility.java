package ast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Utility {

	 
	
	public static String getSourceFileContent(String filePath){
		String source = null;
		try {
			source = FileUtils.readFileToString(new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return source;
	}
	public static ASTParser getParsedSource(String source){
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		return parser;
	}
	public static CompilationUnit getCompilationUint(ASTParser parser){
		return (CompilationUnit) parser.createAST(null);	
	}

	//loop directory to get file list
	public static List<String> getFilesInDir(String heartbeat_testcase_pkg) throws IOException{
		List<String>  fileList = new ArrayList<String>();
		File dirs = new File(".");
		//String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator+"heartbeat"+File.separator;
		String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator+ heartbeat_testcase_pkg +File.separator;

		File root = new File(dirPath);
		//System.out.println(rootDir.listFiles());
		File[] files = root.listFiles ( );
		String filePath = null;

		for (File f : files ) {
			filePath = f.getAbsolutePath();
			if(f.isFile()){
				fileList.add(filePath);
				//System.out.print("File name is : " + filePath  );
				//System.out.println("\n-----------------------------------------");
			}
		}
		return fileList;
	}

}
