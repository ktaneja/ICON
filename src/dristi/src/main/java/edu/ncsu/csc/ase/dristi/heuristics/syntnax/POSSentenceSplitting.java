package edu.ncsu.csc.ase.dristi.heuristics.syntnax;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;


import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;
import edu.ncsu.csc.ase.dristi.util.NLPUtil;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;

/**
 * This Singleton Class holds the templates to deal with complex sentences based
 * on the templates specified in Tregex and Tsurgeon
 * 
 * @author rahul_pandita
 * 
 */
public class POSSentenceSplitting {
	
	private static Logger logger = MyLoggerFactory.getLogger(POSSentenceSplitting.class);
	
	private static POSSentenceSplitting instance;

	private static Map<String, String> patternMap;

	/**
	 * Private Constructor for Singleton Pattern
	 */
	private POSSentenceSplitting() {
		populatePatternMap();
	}

	/**
	 * Method to access the singleton object
	 * 
	 * @return Singleton POSSentenceSplitting object
	 */
	public static synchronized POSSentenceSplitting getInstance() {
		if (instance == null)
			instance = new POSSentenceSplitting();
		return instance;
	}

	/**
	 * Populates the patternMap
	 */
	private void populatePatternMap() {
		patternMap = new LinkedHashMap<String, String>();

		patternMap.put("S < (S=s1 $ CC=ccc $ S= s2)",
				"delete ccc s2 :: delete s1 ccc");
		
		patternMap.put("S < (S=ssentence $ /,/=ccoma $ NP=nnoun $ VP=vverb)",
				"delete ssentence ccoma :: delete ccoma nnoun vverb");

		patternMap.put("VP < (VB=vverb $ NP=nnoun $ S=ssentence)",
				"delete ssentence :: delete vverb");
		
		patternMap.put("NP < (NP=nnoun $ (PRN=pprn < (/-LRB-/=llrb $ PP $ /-RRB-/=rrrb)))",
				"delete pprn :: delete llrb rrrb");
		
		patternMap.put("NP < (NP=nnoun $/-LRB-/=llrb $ NP=nnoun2 $ /-RRB-/=rrrb)",
				"delete llrb nnoun2 rrrb :: delete llrb rrrb");
		
		patternMap.put("NP < (NP=nnoun $ (PRN=pprn < (/-LRB-/=llrb $ SBAR $ /-RRB-/=rrrb)) $ PP=other)",
				"delete pprn :: delete llrb rrrb other");
		
		patternMap.put("S < (SBAR=ssbar $ /,/=ccoma $ NP=nnp $ VP= vvp)",
				"delete ssbar ccoma :: delete ccoma nnp vvp");
		
		patternMap.put("NP-TMP < (NP=nnp $ /,/=ccoma $ SBAR= ssbar)",
				"delete ccoma ssbar :: delete ccoma");
		
		patternMap.put("S < (NP $(VP <(VP=vvp1 $ CC=ccn $ VP=vvp2)))",
				"delete ccn vvp2 :: delete vvp1 ccn");
		
		patternMap.put("NP < (NP=np1 $ /,/=cc2 $ NP=np2 $ /,/=cc1)",
				"delete np2 :: delete cc1");
		
		patternMap.put("S < (NP=np1 $(VP <( VBZ $(VP < (/VB.?/ = vb1 $ CC=cc1 $ /VB.?/ = vb2)))))",
				"delete cc1 vb2 :: delete cc1 vb1");
		
		patternMap.put("FRAG < (SBAR < (IN=cc < (If|IF|if) $ S=sen))", "delete cc :: delete sen:");
		
		patternMap.put("FRAG < (SBAR <(SBAR=s1 $ CC=ccc $ SBAR= s2))",
				"delete ccc s2 :: delete s1 ccc");
		
	}

	/**
	 * Splits a sentence to smaller sentences based on predefined templates
	 * 
	 * @param sentence
	 *            the sentence to be analyzed
	 * @return list of sentences after splitting them into smaller sentences or
	 *         the original sentence if no template is applicable
	 */
	public List<String> applyReduction(String sentence) {
		List<String> retList = new ArrayList<String>();
		// Getting the POS trees of Sentence

		NLPParser parser = NLPParser.getInstance();
		List<Tree> t = parser.getPOSTree(sentence);

		// Creating/Populating map to store the sentence List
		// This map will serve as an iterator to keep track of reductions
		Map<String, Tree> sentenceMap = new LinkedHashMap<String, Tree>();
		for (Tree posTree : t) {
			sentenceMap.put(NLPUtil.getSentnce(posTree), posTree);
		}
		Set<String> sentnceSet;
		Tree posTemp;
		// iterating over list of patterns
		for (String pattern : patternMap.keySet()) {
			List<String> sentenceList = new ArrayList<String>();
			sentnceSet = sentenceMap.keySet();
			for (String sent : sentnceSet) {
				posTemp = sentenceMap.get(sent);
				sentenceList.addAll(applyReduction(pattern, posTemp));
			}

			// Flushing old POS trees and Creating Fresh Set
			t = new ArrayList<Tree>();
			for (String sen : sentenceList) {
				t.addAll(parser.getPOSTree(sen));
			}
			// Flushing Old Map and populating Fresh
			sentenceMap = new LinkedHashMap<String, Tree>();
			for (Tree posTree : t) {
				sentenceMap.put(NLPUtil.getSentnce(posTree), posTree);
			}
		}

		retList.addAll(sentenceMap.keySet());
		return retList;
	}

	public List<String> applyReduction(String treePattern, Tree posTree) {
		List<String> sentenceList = new ArrayList<String>();
		TregexPattern pattern;
		TregexMatcher matcher;
		TsurgeonPattern surgery;

		Tree copy1 = posTree.deepCopy();
		try {
			logger.debug(treePattern);
			pattern = TregexPattern.compile(treePattern);
			matcher = pattern.matcher(copy1);

			while (matcher.find()) {
				Tree candidate = matcher.getMatch();
				String[] patList = patternMap.get(treePattern).split("::");
				String[] subPatList = patList[0].trim().split(":");

				for (String pp : subPatList) {
					logger.debug("Applying reduction : " + pp + " on tree\n" + posTree.pennString());
					surgery = Tsurgeon.parseOperation(pp);
					Tree modifiedTree = Tsurgeon.processPattern(pattern, surgery, posTree);
					sentenceList.add(NLPUtil.getSentnce(modifiedTree));
				}

				subPatList = patList[1].trim().split(":");
				for (String pp1 : subPatList) {
					surgery = Tsurgeon.parseOperation(pp1);
					candidate = Tsurgeon.processPattern(pattern, surgery,
							candidate);
					sentenceList.add(NLPUtil.getSentnce(candidate));
				}
			}
		} catch (Exception e) {
			logger.error("Error in applying POS Reduction", e);
		}
		if (sentenceList.size() == 0) {
			sentenceList.add(NLPUtil.getSentnce(posTree));
		}
		return sentenceList;
	}

	public static void main(String[] args) {
		String sentence = "if this input stream has been closed by invoking its close method, or anIOerroroccurs.";
		POSSentenceSplitting pr = POSSentenceSplitting.getInstance();
		for (String sen : pr.applyReduction(sentence)) {
			System.err.println(sen);
		}

	}
}
