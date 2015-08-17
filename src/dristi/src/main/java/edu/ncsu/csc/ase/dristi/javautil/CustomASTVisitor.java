package edu.ncsu.csc.ase.dristi.javautil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.jsoup.Jsoup;

import edu.ncsu.csc.ase.dristi.datastructure.ootype.DMethod;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DType;

/**
 * 
 * @author rahulpandita
 *
 */
public class CustomASTVisitor extends ASTVisitor {
	
	private DType dtype;
	
	public CustomASTVisitor (DType dtype)
	{
		super();
		this.dtype = dtype;
	}
	
	/**
	 * 
	 */
	public boolean visit(MethodDeclaration node) {
		
		LinkedHashMap<String, String> parameterIDMap = parameterIDMapCreator(node);
		LinkedHashMap<String, String> parameterCommentMap = parameterCommentMapCreator(parameterIDMap);
		
		LinkedHashMap<String, String> exceptionIDMap = exceptionDeclerationMapCreator(node);

		// Extract
		// Extract Javadoc Comments
		if (node.getJavadoc() != null) {
			DMethod method = retrieveDMethod(node, parameterIDMap);
			if (method == null)
				return true;

			for (Object tag : node.getJavadoc().tags()) 
			{
				TagElement te = (TagElement) tag;
				String text = getTextFromFragments(te);

				if (te.getTagName() == null) 
				{
					// Add method Summaries
					method.setMethodComments(text);
				} 
				else if (te.getTagName().trim().equals(TagElement.TAG_PARAM)) 
				{
					String paramID = getIDFromFragments(te);
					if (parameterCommentMap.containsKey(paramID))
						parameterCommentMap.put(paramID, text);
					else
						handleError(getIDFromFragments(te));
				} 
				else if (te.getTagName().trim().equals(TagElement.TAG_EXCEPTION)|| te.getTagName().trim().equals("@throws")) 
				{
					String exceptionType = getIDFromFragments(te);
					if (exceptionIDMap.containsKey(exceptionType))
						exceptionIDMap.put(exceptionType, text);
					else
						handleError(getIDFromFragments(te));
				} 
				else if (te.getTagName().equals(TagElement.TAG_RETURN))
					method.setReturnComments(text);
				else if (te.getTagName().trim().equals(TagElement.TAG_SEE))
					handleError("SEE ALSO " + text);
				else
					handleError("UNKNOWN " + te.getTagName() + " " + text);
			}

			int i = 0;
			for (String key : parameterCommentMap.keySet()) 
			{
				method.setParamComment(i, parameterCommentMap.get(key));
				i++;
			}
			i = 0;
			for (String key : exceptionIDMap.keySet()) 
			{
				try 
				{
					method.setExceptionComment(method.getExceptionType().get(i), exceptionIDMap.get(key));
					i++;
				} 
				catch (Exception e) 
				{
					System.out.println(key);
				}
			}
		}
		return true;
	}

	/**
	 * @return the dtype
	 */
	public DType getDtype() {
		return dtype;
	}

	private void handleError(String err)
	{
		System.out.println("ERROR:\t " + err);
	}
	/**
	 * Returns the text contained in the {@link TagElement}
	 * @param te the {@code javadoc} tag Element
	 * @return 
	 */
	public String getTextFromFragments(TagElement te) {
		StringBuffer buff = new StringBuffer();
		List<?> fragList = te.fragments();
		for(Object obj:fragList)
		{
			if(obj instanceof TextElement)
				buff.append(getTextFromFragments((TextElement)obj));
			else if (obj instanceof MethodRef)
				buff.append(getTextFromFragments((MethodRef)obj));
			else if (obj instanceof MemberRef)
				buff.append(getTextFromFragments((MemberRef)obj));
			else if (obj instanceof SimpleName)
				buff.append(getTextFromFragments((SimpleName)obj));
			else if(obj instanceof TagElement)
				buff.append(removeTrailingPeriod(getTextFromFragments((TagElement)obj)));
			else
				buff.append("&&"+ obj.getClass().getCanonicalName() + "&&");
			buff.append(" ");
		}
		return cleanText(buff.toString().trim());
	}
	
	private String removeTrailingPeriod(String textFromFragments) {
		
		if(textFromFragments.endsWith("."))
			textFromFragments = textFromFragments.substring(0,textFromFragments.length()-1);// TODO Auto-generated method stub
		return textFromFragments;
	}

	/**
	 * Returns the text contained in the {@link TextElement}
	 * @param te the {@code javadoc} text Element
	 * @return 
	 */
	private String getTextFromFragments(TextElement te) {
		return te.getText();
	}
	
	/**
	 * Returns the name in the {@link MethodRef}
	 * @param mRef the {@code javadoc} method ref Element
	 * @return 
	 */
	private String getTextFromFragments(MethodRef mRef) {
		return mRef.getName().getFullyQualifiedName();
	}
	
	/**
	 * Returns the text contained in the {@link MemberRef}
	 * @param mRef the {@code javadoc} Memeber Ref Element
	 * @return 
	 */
	private String getTextFromFragments(MemberRef mRef) {
		return mRef.getName().getFullyQualifiedName();
	}
	
	/**
	 * Returns the simple Name contained in the {@link SimpleName}
	 * NOTE: The method currently returns empty string
	 * @param sName the {@code javadoc} Simple Name Element
	 * @return 
	 */
	private String getTextFromFragments(SimpleName sName) {
		return "";
	}
	
	
	
	/**
	 * This method removes the html/code specific elements from the text   
	 * @param text the text that needs to be cleaned
	 * @return plain text 
	 */
	private String cleanText(String text) 
	{
		if(!text.trim().contains(" "))
		{
			return text.trim();
		}
		String[] sentnces = text.split("\\.\\s");
		StringBuffer buff = new StringBuffer();
		for (String sen : sentnces) 
		{
			// Add period for intermediate sentences
			if (!sen.endsWith("."))
				sen = sen + ".";

			while (sen.contains("<code>"))
				sen = sen.replace("<code>", " ");
			while (sen.contains("</code>"))
				sen = sen.replace("</code>", "MM ");
			while (sen.contains("  "))
				sen = sen.replace("  ", " ");
			
			buff.append(sen);
			buff.append(" ");
		}
		
		//remove HTML Tags
		String returnVal =  Jsoup.parse(buff.toString().trim()).text();
		return returnVal;
	}
	
	/**
	 * Creates a HashMap indexed by ExceptionType name explicitly declared by the method
	 * @param node
	 * @return HashMap key = ExceptionTypeName, value= EmptyString
	 */
	private LinkedHashMap<String, String> exceptionDeclerationMapCreator(MethodDeclaration node) 
	{
		LinkedHashMap<String, String> exceptionIDMap = new LinkedHashMap<>();
		//Exceptions
		List<?> exceptionList = node.thrownExceptions();
		for(Object obj: exceptionList)
		{
			Name exceptionType = (Name)obj;
			exceptionIDMap.put(exceptionType.getFullyQualifiedName(), "");
		}
		return exceptionIDMap;
	}
	
	/**
	 * Creates a HashMap indexed by parameter name accepted by the method
	 * @param node
	 * @return HashMap key = parameter name, value= parameter Type
	 */
	private LinkedHashMap<String, String> parameterIDMapCreator(MethodDeclaration node) 
	{
		LinkedHashMap<String, String> variableIDMap = new LinkedHashMap<>();
		//parameters
		List<?> paramList =  node.parameters();
		for(Object obj:paramList)
		{
			SingleVariableDeclaration param = (SingleVariableDeclaration)obj;
			String name = param.getName().getFullyQualifiedName();
			//Clean type for parameters and arrays
			String type = param.getType().toString();
			int dimentions = param.getExtraDimensions();
			if(type.contains("<"))
				type = type.substring(0,type.indexOf("<"));
			for(int i= 0;i<dimentions;i++)
				type = type + "[]";
			variableIDMap.put(name, type);
		}
		return variableIDMap;
	}
	
	/**
	 * Creates a HashMap indexed by parameter name accepted by the method
	 * @param node
	 * @return HashMap key = parameter name, value= emptyString
	 */
	private LinkedHashMap<String, String> parameterCommentMapCreator(LinkedHashMap<String, String> variableIDMap) 
	{
		LinkedHashMap<String, String> variableCommentMap = new LinkedHashMap<>();
		for(String paramName : variableIDMap.keySet())
		{
			variableCommentMap.put(paramName, "");
		}
		return variableCommentMap;
	}
	
	private String getIDFromFragments(TagElement te) {
		List<?> fragList = te.fragments();
		for(Object obj:fragList)
		{
			if (obj instanceof SimpleName)
			{
				return ((SimpleName)obj).getFullyQualifiedName();
			}
		}
		return "";
	}
	
	private DMethod retrieveDMethod(MethodDeclaration node, LinkedHashMap<String, String> variableIDMap) {
		DMethod method = null;
		List<DMethod> mtdList = new ArrayList<>();
		mtdList.addAll(dtype.getMethodList());
		mtdList.addAll(dtype.getConstructorList());
		for(DMethod mtd:mtdList)
		{
			if(mtd.getMethodName().equalsIgnoreCase(node.getName().getFullyQualifiedName()))
			{
				if(mtd.getParamList().size()==variableIDMap.size())
				{
					int i =0;
					method = mtd;
					for(String paramType:variableIDMap.keySet())
					{
						if(!mtd.getParamList().get(i).getName().endsWith(variableIDMap.get(paramType)))
						{
							mtd = null;
							break;
						}
						i++;
					}
				}
				if(method != null)
					break;
			}
		}
		return method;
	}
}
