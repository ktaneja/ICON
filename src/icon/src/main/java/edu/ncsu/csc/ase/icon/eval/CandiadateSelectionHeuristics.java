package edu.ncsu.csc.ase.icon.eval;

import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;

/**
 * 
 * 
 * @author Rahul Pandita
 *
 */
public class CandiadateSelectionHeuristics {
	
	public static boolean isCandidateSentnce(String text) 
	{
		
		if (length_Heuristic(text))
			return false;

		if(startingVerb_Heuristic(text))
			return false;
		
		if(startingVerb2_Heuristic(text))
			return false;
		

		if(gramaticalRelation_Heuristic(text))
			return true;
		
		if(gramaticalRelationFirstRoot_Heuristic(text))
			return true;

		if(keyword_Heuristic(text))
			return true;
		
		return false;
	}
	
	public static boolean length_Heuristic(String text)
	{
		return text.length() > 140;
	}
	
	public static boolean startingVerb_Heuristic(String text)
	{
		for (String startingVerb : SpecFeature.getStartingVerbList()) 
		{
			if (text.trim().toLowerCase().startsWith(startingVerb))
				return true;
		}
		return false;
	}
	
	
	public static boolean startingVerb2_Heuristic(String text)
	{
		Tree posTree = Cache.getInstance().getPOSTree(text);
		List<TaggedWord> taggedWords = posTree.taggedYield();
		for (TaggedWord wrd : taggedWords) 
		{
			if (wrd.beginPosition() == 0) {
				if (wrd.tag().startsWith("VB")) {
					return true;
				}

			}
		}
		return false;
	}
	
	public static boolean gramaticalRelation_Heuristic(String text)
	{
		
		SemanticGraph typedDependencies =  Cache.getInstance().getSemanticGraph(text);
		GrammaticalRelation tmod = GrammaticalRelation.valueOf("tmod");
		// GrammaticalRelation mark = GrammaticalRelation.valueOf("mark");
		// GrammaticalRelation root = GrammaticalRelation.valueOf("root");
		// GrammaticalRelation auxpass = GrammaticalRelation.valueOf("auxpass");
		
		for (SemanticGraphEdge edge : typedDependencies.edgeListSorted()) {
			if (edge.getRelation().equals(tmod)) {
				return true;
			}

		}
		return false;
	}
	
	private static boolean gramaticalRelationFirstRoot_Heuristic(String text)
	{
		
		SemanticGraph typedDependencies =  Cache.getInstance().getSemanticGraph(text);
		IndexedWord wrd = typedDependencies.getFirstRoot();
		if (wrd.tag().toUpperCase().trim().startsWith("VB")) {
			if (wrd.tag().toUpperCase().equals("VB")
					|| wrd.tag().toUpperCase().equals("VBD")
					|| wrd.tag().toUpperCase().equals("VBN"))
				return true;
		}
		return false;
	}
	
	private static boolean keyword_Heuristic(String text)
	{
		for (String keyword : SpecFeature.getSpecKeywords()) 
		{
			if (text.trim().toLowerCase().contains(keyword)) {
				return true;
			}
		}
		return false;
	}
	
}
