package edu.ncsu.csc.ase.dristi.perf.lucene;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TagElement;

/**
 * 
 * @author rahulpandita
 * 
 * Created: Jan 28, 2014 12:56:12 PM
 *
 */
public class LuceneASTVisitor extends ASTVisitor {
	
	
	
	private Map<String, Map<CommentType, String>> commentMap;
	
	public Map<String, Map<CommentType, String>> getCommentMap() {
		return commentMap;
	}


	private String typeName;
	
	public LuceneASTVisitor (String type)
	{
		super();
		this.commentMap = new HashMap<String, Map<CommentType,String>>();
		this.typeName = type;
	}
	
	/**
	 * 
	 */
	public boolean visit(MethodDeclaration node) {
		
		String name = typeName.concat(" :: ").concat(extractMethodName(node));
		// Extract
		// Extract Javadoc Comments
		if (node.getJavadoc() != null) 
		{
			Map<CommentType, String> commentMap = getCommentMap(node.getJavadoc());
			this.commentMap.put(name, commentMap);
		}
		return true;
	}

	private Map<CommentType, String> getCommentMap(Javadoc javadoc) 
	{
		Map<CommentType, String> commentMap= new HashMap<>();
		
		commentMap.put(CommentType.SUMMARY, "");
		commentMap.put(CommentType.PARAM, "");
		commentMap.put(CommentType.RETURN, "");
		commentMap.put(CommentType.EXCEPTION, "");
		commentMap.put(CommentType.AUTHOR, "");
		commentMap.put(CommentType.SEE, "");
		commentMap.put(CommentType.SINCE, "");
		commentMap.put(CommentType.OTHER, "");
		
		for (Object tagObj : javadoc.tags() ) 
		{
			TagElement tag = (TagElement) tagObj;
			String tagName= tag.getTagName();
			if (tagName == null) 
			{
				commentMap.put(CommentType.SUMMARY, commentMap.get(CommentType.SUMMARY).concat(" ").concat(tag.toString()));
			} 
			else if (TagElement.TAG_PARAM.equals(tagName)) 
			{
				commentMap.put(CommentType.PARAM, commentMap.get(CommentType.PARAM).concat(" ").concat(tag.toString()));
			} 
			else if (TagElement.TAG_RETURN.equals(tagName)) 
			{
				commentMap.put(CommentType.RETURN, commentMap.get(CommentType.RETURN).concat(" ").concat(tag.toString()));
			} 
			else if (TagElement.TAG_EXCEPTION.equals(tagName) || TagElement.TAG_THROWS.equals(tagName)) 
			{
				commentMap.put(CommentType.EXCEPTION, commentMap.get(CommentType.EXCEPTION).concat(" ").concat(tag.toString()));
			} 
			else if (TagElement.TAG_AUTHOR.equals(tagName)) 
			{
				commentMap.put(CommentType.AUTHOR, commentMap.get(CommentType.AUTHOR).concat(" ").concat(tag.toString()));
			} 
			else if (TagElement.TAG_SEE.equals(tagName)) 
			{
				commentMap.put(CommentType.SEE, commentMap.get(CommentType.SEE).concat(" ").concat(tag.toString()));
			} 
			else if (TagElement.TAG_SINCE.equals(tagName)) 
			{
				commentMap.put(CommentType.SINCE, commentMap.get(CommentType.SINCE).concat(" ").concat(tag.toString()));
			} 
			else 
			{
				commentMap.put(CommentType.OTHER, commentMap.get(CommentType.OTHER).concat(" ").concat(tag.toString()));
			}
		}
		
		
		return commentMap;
	}
	

	private String extractMethodName(MethodDeclaration node)
	{
		String returnVal = node.toString();
		
		if(node.getJavadoc()!= null)
		{
			String javadoc = node.getJavadoc().toString();
			returnVal = returnVal.replace(javadoc, "").trim();
		}
		
		if(node.getBody()!= null)
		{
			String body = node.getBody().toString();
			returnVal = returnVal.substring(0,returnVal.length()-body.length()+1).trim();
		}
		
		return returnVal;
	}
}
