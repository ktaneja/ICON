package edu.ncsu.csc.ase.dristi.javautil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DType;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DTypeFactory;
import edu.ncsu.csc.ase.dristi.util.FileUtil;

/**
 * 
 * @author Rahul Pandita
 *
 */
public class Src2JavaDocReader 
{
	
	private static final String SRC_FILE_EXTENSION = "java";
	
	private static final String JAVA_PKG_SEPERATOR = ".";
	
	
	
	/**
	 * Returns {@code List<String>} of the Type names occurring in a java source
	 * directory. The method Recursively looks for all java source files in a
	 * directory.
	 * 
	 * @param srcDirectory
	 *            path of directory containing java source files
	 * @return {@code List<String>} of the Type names
	 * @throws IOException
	 *             in case an {@code IOError} occurs or {@code srcDirectory} is
	 *             invalid
	 */
	public List<String> getTypeNamesFromDirectory(String srcDirectory)
			throws IOException {
		List<String> returnList = new ArrayList<String>();
		String[] extensions = new String[] { SRC_FILE_EXTENSION };

		// List all Java files in the directory
		List<File> fileList = (List<File>) FileUtils.listFiles(new File(
				srcDirectory), extensions, true);

		// Iterate through fileList
		for (File file : fileList) {
			returnList.addAll(getTypeNamesFromJavaSrcFile(file));

		}
		return returnList;
	}


	/**
	 * Returns {@code List<String>} of the Type names occurring in a java source
	 * File. A source file may contain definitions of multiple Types. All of
	 * them are returned.
	 * 
	 * @param srcFile
	 *            {@link File} object of java source file. The {@code srcFile}
	 *            should be the object of java source file.
	 * @return {@code List<String>} of the Type names contained in
	 *         {@code srcFile}
	 * @throws IOException
	 *             in case an {@code IOError} occurs or {@code srcFile} is
	 *             invalid
	 */
	public List<String> getTypeNamesFromJavaSrcFile(File srcFile)
			throws IOException {
		List<String> returnList = new ArrayList<>();
		CompilationUnit compilation_unit = createCompilationUnit(srcFile);
		for (Object obj : compilation_unit.types()) {
			if (obj instanceof TypeDeclaration) 
			{
				TypeDeclaration td = (TypeDeclaration) obj;
				String name = getJavaName(compilation_unit, td);
				returnList.add(name);
				if(td.getTypes() != null)
				{
					TypeDeclaration[] subTypeList = td.getTypes();
					for(TypeDeclaration td1: subTypeList)
					{
						returnList.add(name + JAVA_PKG_SEPERATOR + td1.getName());
					}
				}
			}
		}
		return returnList;
	}


	private String getJavaName(CompilationUnit compilation_unit, TypeDeclaration td) {
		String name = ""; 
				
		if(compilation_unit.getPackage() == null)
			name = td.getName().toString();
		else
			name = (compilation_unit.getPackage().getName() + JAVA_PKG_SEPERATOR + td.getName());
		
		return name;
	}


	/**
	 * Creates a {@link CompilationUnit} object of {@code srcFile} Object
	 * 
	 * @param srcFile
	 *            {@link File} object of java source file. The {@code srcFile}
	 *            should be the object of java source file.
	 * @return {@link CompilationUnit} object of {@code srcFile} Object
	 * @throws IOException
	 *             in case an {@code IOError} occurs or {@code srcFile} is
	 *             invalid
	 */
	public CompilationUnit createCompilationUnit(File srcFile) throws IOException {
		String source = FileUtil.readFiletoString(srcFile.getCanonicalPath());
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		return ((CompilationUnit) parser.createAST(null));
	}
	
	public Map<String, DType> updateTypeComments(List<String> typeList) throws IOException 
	{
		Map<String, DType> typeMap = new HashMap<>();
		for (String typeName : typeList) 
		{
			DType dtype = updateTypeComments(typeName); 
			typeMap.put(typeName, dtype);
		}
		return typeMap;
	}


	/**
	 * @param typeName
	 * @return
	 * @throws IOException
	 */
	public DType updateTypeComments(String typeName) throws IOException {
		DType dtype = DTypeFactory.getInstance().getDType(typeName);
		CustomASTVisitor visitor = new CustomASTVisitor(dtype);
		updateTypeComments(typeName, visitor);
		dtype = visitor.getDtype();
		return dtype;
	}
	
	
	/**
	 * 
	 * @param typeName
	 * @param visitor
	 * @throws IOException
	 */
	public void updateTypeComments(String typeName, ASTVisitor visitor) throws IOException {
		File file;
		String fileName = filePathfromTypeName(typeName);
		file = new File(fileName);
		CompilationUnit compilation_unit = createCompilationUnit(file);
		compilation_unit.accept(visitor);
		
	}
	
	

	public String filePathfromTypeName(String typeName) 
	{
		while(typeName.contains(JAVA_PKG_SEPERATOR))
		{
			typeName = typeName.replace(JAVA_PKG_SEPERATOR, File.separator);
		}
		//Create File Path
		typeName = Config.JDK_SRC_PATH1.concat(File.separator).concat(typeName.concat(JAVA_PKG_SEPERATOR.concat(SRC_FILE_EXTENSION)));
		return typeName;
	}
	
}
