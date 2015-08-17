package edu.ncsu.csc.ase.dristi.heuristics.syntnax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This singleton class contains functions to split a sentence into smaller sentences using the predefined templates based on regular expressions
 * @author rahul_pandita
 *
 */
public class SentenceSplitting {
	
	private static SentenceSplitting instance;

	private static Map<String, String> patternMap;

	private static List<String> patternList;

	/**
	 * Private Constructor for Singleton Pattern
	 */
	private SentenceSplitting() {
		populatePatternMap();
	}

	/**
	 * Method to access the singleton object
	 * 
	 * @return Singleton SentenceSplitting object
	 */
	public static synchronized SentenceSplitting getInstance() {
		if (instance == null)
			instance = new SentenceSplitting();
		return instance;
	}

	/**
	 * Populates the patternMap
	 */
	private void populatePatternMap() {
		patternList = new ArrayList<String>();
		patternMap = new HashMap<String, String>();

		patternList.add("S < (S=ssentence $ /,/=ccoma $ NP=nnoun $ VP=vverb)");
		patternMap.put("S < (S=ssentence $ /,/=ccoma $ NP=nnoun $ VP=vverb)",
				"delete ssentence ccoma :: delete ccoma nnoun vverb");

		patternList.add("VP < (VB=vverb $ NP=nnoun $ S=ssentence)");
		patternMap.put("VP < (VB=vverb $ NP=nnoun $ S=ssentence)",
				"delete ssentence :: delete vverb");

		// patternMap.put("S=sen $- NP $- VB=verb", "delete sen :: prune verb");

	}

	/**
	 * Splits a sentence to smaller sentences based on predefined templates
	 * @param sentence the sentence to be analyzed
	 * @return list of sentences after splitting them into smaller sentences
	 * or the original sentence if no template is applicable
	 */
	public List<String> applyReduction(String sentence) {
		List<String> retList = new ArrayList<String>();
		//TODO write reductions 
		return retList;
	}

	public static void main(String[] args) {
		String sentence = "All objects in the bucket must be deleted before the bucket itself can be deleted.";
		SentenceSplitting pr = SentenceSplitting.getInstance();
		for (String sen : pr.applyReduction(sentence)) {
			System.err.println(sen);
		}

	}
}
