package edu.ncsu.csc.ase.dristi.icon;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DMethod;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DType;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DTypeFactory;
import edu.ncsu.csc.ase.dristi.javautil.JavaDependencyBuilder;
import edu.ncsu.csc.ase.dristi.javautil.Src2JavaDocReader;
import edu.ncsu.csc.ase.dristi.util.FileUtilExcel;

/**
 * These set of classes are responsible for evaluation of ICON project.
 * THis class extracts a list of sentences from the public methods of java.io package  
 * and puts them in a xls sheet.
 * 
 * @author Rahul Pandita
 * @date 1/17/2014
 */
public class JavaSentenceExtractor extends SentenceExtractor
{
	
	
	public static void main(String[] args) throws Exception {
		
		JavaDependencyBuilder.main(args);
		Src2JavaDocReader t = new Src2JavaDocReader();
		List<String> typeNameList = t.getTypeNamesFromDirectory(Config.JDK_SRC_PATH);
		Map<String, DType> subList = new LinkedHashMap<String, DType>();
		for (String typeName : typeNameList)
		{
			DType clazz = DTypeFactory.getInstance().getDType(typeName);
			if ((clazz.getModifiers() == 0)
					|| (Modifier.isPrivate(clazz.getModifiers()))
					|| (Modifier.isAbstract(clazz.getModifiers()))
					|| (Modifier.isInterface(clazz.getModifiers()))) {
				// do nothing
				continue;
			}
			// Build Dependencies on type
			try {
				subList.put(typeName, t.updateTypeComments(typeName));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		}
		
		FileUtilExcel xlsWriter = new FileUtilExcel("senList1");
		for(String typeName : subList.keySet())
		{
			DType clazz = subList.get(typeName);
			xlsWriter.writeDataToExcelRedItalic(typeName, "");
			count  = count + 1;
			writeComments2Xls(xlsWriter, clazz);
		}
		xlsWriter.commitXlS();
		System.err.println(count);
		
	}
	
	private static void writeComments2Xls(FileUtilExcel xlsWriter, DType clazz) throws Exception {
		
		List<DMethod> mtdList = clazz.getConstructorList();
		mtdList.addAll(clazz.getMethodList());
		for(DMethod mtd: mtdList)
		{
			String name = getMethodName(mtd);
			xlsWriter.writeDataToExcelBold(name, "");
			count  = count + 1;
			prettyPrint(mtd, xlsWriter);
			/*for(String sen: prettyPrint(mtd))
			{
				xlsWriter.writeDataToExcel("", sen);
				count  = count + 1;
			}*/
		}
		
	}

	private static String getMethodName(DMethod mtd) {
		if(mtd.toString()!="")
			return mtd.toString();
		StringBuffer buff = new StringBuffer();
		buff.append(mtd.getMethodName());
		buff.append("(");
		for(int i=0; i<mtd.getParamList().size();i++)
		{
			buff.append(mtd.getParamList().get(i).getName());
			buff.append(", ");
		}
		buff.append(")");
		
		return buff.toString();
	}

	public static void prettyPrint(DMethod mtd,FileUtilExcel xlsWriter ) throws Exception 
	{
		String temp = "";
		temp = mtd.getMethodComments()==null?"":mtd.getMethodComments();
		writeComment(xlsWriter, temp, "SUMMARY");
		StringBuffer buff = new StringBuffer();
		for(int i=0; i<mtd.getParamList().size();i++)
		{
			temp = mtd.getParameterComment(i)==null?"":mtd.getParameterComment(i);
			buff.append(temp);
			buff.append("\n");
		}
		temp = buff.toString();
		writeComment(xlsWriter, temp, "PARAM");
		temp = mtd.getReturnComments()==null?"":mtd.getReturnComments();
		writeComment(xlsWriter, temp, "RETURN");
		buff = new StringBuffer();
		for(int i=0; i<mtd.getExceptionType().size();i++)
		{
			
			temp = mtd.getExceptionComment(i)==null?"":mtd.getExceptionComment(i);
			buff.append(temp);
			buff.append("\n");
		}
		temp = buff.toString();
		writeComment(xlsWriter, temp, "EXCEPTION");
		
	}

	public static List<String> prettyPrint(DMethod mtd) 
	{
		StringBuffer buff = new StringBuffer();
		String temp = "";
		temp = mtd.getMethodComments()==null?"":mtd.getMethodComments();
		buff.append(temp);
		buff.append("\n");
		for(int i=0; i<mtd.getParamList().size();i++)
		{
			temp = mtd.getParameterComment(i)==null?"":mtd.getParameterComment(i);
			buff.append(temp);
			buff.append("\n");
		}
		temp = mtd.getReturnComments()==null?"":mtd.getReturnComments();
		buff.append(temp);
		buff.append("\n");
		for(int i=0; i<mtd.getExceptionType().size();i++)
		{
			
			temp = mtd.getExceptionComment(i)==null?"":mtd.getExceptionComment(i);
			buff.append(temp);
			buff.append("\n");
		}
		List<String> senList = sentnceBoundaryApprox(buff.toString());
		
		System.out.println(mtd.toString());
		
		for(String x:senList)
			System.out.println(x);
		/*buff.append(returnType.getName());
		buff.append(" ");
		buff.append(methodName);
		buff.append("(");
		for(int i=0; i<paramList.size();i++)
		{
			buff.append(paramList.get(i).getName());
			buff.append(", ");
		}
		buff.append(")");*/
		System.out.println("\n");
		return senList;
	}
	
	
}
