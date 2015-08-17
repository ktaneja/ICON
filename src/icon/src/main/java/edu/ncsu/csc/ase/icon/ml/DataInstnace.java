package edu.ncsu.csc.ase.icon.ml;

import java.util.ArrayList;
import java.util.List;

import cc.mallet.types.Instance;
import edu.ncsu.csc.ase.icon.eval.Cache;
import edu.ncsu.csc.ase.icon.eval.ExcelRow;
import edu.ncsu.csc.ase.icon.eval.SpecFeature;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;

public class DataInstnace extends Instance{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataInstnace(String commentType, String sentnce, String target, String name, String source) {
		super(sentnce, target, name, source);
		StringBuffer buff = new StringBuffer();
		sentnce = SpecFeature.textCorrection(sentnce);
		buff.append(commentType).append(" ");
		buff.append("LENGTH").append(sentnce.length()/10).append(" ");
		buff.append(startingVerb2_Heuristic(sentnce)).append(" ");
		buff.append(gramaticalRelationFeatures(sentnce)).append(" ");
		buff.append(gramaticalRelationFirstRoot(sentnce)).append(" ");
		buff.append(keyword_Heuristic(sentnce)).append(" ");
		this.data = buff.toString();
	}
	
	public DataInstnace(ExcelRow row, String name, String source) {
		super(row.getComment(), String.valueOf(row.getFinalMarked()), name, source);
		String sentnce = SpecFeature.textCorrection(row.getComment());
		StringBuffer buff = new StringBuffer();
		buff.append(row.getCommentType()).append(" ");
		buff.append("LENGTH").append(sentnce.length()/10).append(" ");
		buff.append(startingVerb2_Heuristic(sentnce)).append(" ");
		buff.append(gramaticalRelationFeatures(sentnce)).append(" ");
		buff.append(gramaticalRelationFirstRoot(sentnce)).append(" ");
		buff.append(keyword_Heuristic(sentnce)).append(" ");
		this.data = buff.toString();
	}
	
	private String startingVerb2_Heuristic(String text)
	{
		Tree posTree = Cache.getInstance().getPOSTree(text);
		List<TaggedWord> taggedWords = posTree.taggedYield();
		for (TaggedWord wrd : taggedWords) 
		{
			if (wrd.beginPosition() == 0) {
				if (wrd.tag().startsWith("VB")) {
					return "STARTS_VB ";
				}
			}
		}
		return "";
	}
	
	private String gramaticalRelationFeatures(String text)
	{
		StringBuffer buff = new StringBuffer();
		SemanticGraph typedDependencies =  Cache.getInstance().getSemanticGraph(text);
		
		
		for (SemanticGraphEdge edge : typedDependencies.edgeListSorted()) {
			buff.append(edge.getRelation()).append(" ");
		}
		return buff.toString();
	}
	
	private String gramaticalRelationFirstRoot(String text)
	{
		
		SemanticGraph typedDependencies =  Cache.getInstance().getSemanticGraph(text);
		try{
			IndexedWord wrd = typedDependencies.getFirstRoot();
			return "FIRST_ROOT_TAG_"+wrd.tag().toUpperCase();
		}
		catch(Exception e)
		{
			
		}
		return "FIRST_ROOT_TAG_NA";
	}
	
	private String keyword_Heuristic(String text)
	{
		StringBuffer buff = new StringBuffer();
		for (String keyword : SpecFeature.getSpecKeywords()) 
		{
			if (text.trim().toLowerCase().contains(keyword)) {
				buff.append(keyword).append(" ");
			}
		}
		return buff.toString();
	}
	
	public static List<String> getSpecKeywords()
	{
		List<String> verbList = new ArrayList<>();
		verbList.add("if ");
		verbList.add("must ");
		verbList.add("will ");
		verbList.add("can ");
		verbList.add("could ");
		verbList.add("may ");
		verbList.add("should ");
		verbList.add("invoke ");
		verbList.add("invoking ");
		verbList.add("has been ");
		verbList.add("before ");
		verbList.add("after ");
		return verbList;
	}

}
