package edu.ncsu.csc.ase.dristi.icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.internal.eval.InstallException;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.analysis.SemanticAnalyser;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;

/**
 * 
 * @author Rahul Pandita
 * Created: Mar 6, 2014 1:36:40 AM
 */
public class ICONEval 
{
	protected static HashMap<String, List<ITuple>> mem2 = new HashMap<>();
	protected static HashMap<String, List<String>> mem1 = new HashMap<>();
	protected static HashMap<String, List<SemanticGraph>> mem3 = new HashMap<>();
	protected static Map<String, List<Tree>> mem4 = new HashMap<>();
	
	protected NLPParser parser;
	protected SemanticAnalyser analyser;
	
	public ICONEval() throws Exception
	{
		try
		{
			parser = NLPParser.getInstance();
			analyser = new SemanticAnalyser();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new InstallException("Error in either getting parser Instance or analyser instance");
		}
	}
	
	public boolean CandidateSelectionHeuristic(String text) 
	{
		if (text.length() > 140)
			return false;

		for (String startingVerb : SpecFeature.getStartingVerbList()) 
		{
			if (text.trim().toLowerCase().startsWith(startingVerb))
				return false;
		}

		List<Tree> posTrees;
		if (mem4.containsKey(text)) 
		{
			posTrees = mem4.get(text);
		} 
		else 
		{
			posTrees = parser.getPOSTree(text);
			mem4.put(text, posTrees);
		}

		for (Tree posTree : posTrees) 
		{
			List<TaggedWord> taggedWords = posTree.taggedYield();
			for (TaggedWord wrd : taggedWords) 
			{
				if (wrd.beginPosition() == 0) {
					if (wrd.tag().startsWith("VB")) {
						return false;
					}

				}
			}
		}

		List<SemanticGraph> typedDependencyList;
		if (mem3.containsKey(text)) {
			typedDependencyList = mem3.get(text);
		} else {
			typedDependencyList = parser.getStanfordDependencies(text);
			mem3.put(text, typedDependencyList);
		}
		GrammaticalRelation tmod = GrammaticalRelation.valueOf("tmod");
		//GrammaticalRelation mark = GrammaticalRelation.valueOf("mark");
		// GrammaticalRelation root = GrammaticalRelation.valueOf("root");
		// GrammaticalRelation auxpass = GrammaticalRelation.valueOf("auxpass");

		for (SemanticGraph posTree : typedDependencyList) {

			for (SemanticGraphEdge edge : posTree.edgeListSorted()) {
				if (edge.getRelation().equals(tmod)) {
					return true;
				}

			}

			IndexedWord wrd = posTree.getFirstRoot();
			if (wrd.tag().toUpperCase().trim().startsWith("VB")) {
				if (wrd.tag().toUpperCase().equals("VB")
						|| wrd.tag().toUpperCase().equals("VBD")
						|| wrd.tag().toUpperCase().equals("VBN"))
					return true;
			}

		}

		for (String keyword : SpecFeature.getSpecKeywords()) {
			if (text.trim().toLowerCase().contains(keyword)) {
				return true;
			}
		}

		return false;
	}
	
	public String test(String clazz, String text) 
	{
		StringBuffer buff = new StringBuffer("");
		if (!CandidateSelectionHeuristic(text))
			return buff.toString().trim();

		text = SpecFeature.textCorrection(text);

		// boolean val = false;
		//List<Tree> posTreeList1 = parser.getPOSTree(text);
		// for(Tree posTree:posTreeList1)
		// posTree.pennPrint();
		List<String> sentenceList;
		if (mem1.containsKey(text)) {
			sentenceList = mem1.get(text);
		} else {
			sentenceList = analyser.applyReduction(text);
			mem1.put(text, sentenceList);
		}

		for (String sentence : sentenceList) {
			// System.out.println(sentence);
			//List<Tree> posTreeList = parser.getPOSTree(sentence);
			// for(Tree posTree:posTreeList)
			// posTree.pennPrint();
			List<ITuple> tupleList;
			if (mem2.containsKey(sentence)) {
				tupleList = mem2.get(sentence);
			} else {
				tupleList = parser.getTuples(sentence);
				mem2.put(sentence, tupleList);
			}
			for (ITuple tuple : tupleList) {
				if (tuple != null) {
					System.out.println(tuple.toString());
					List<ITuple> tList=  new ArrayList<>();
					tList.addAll(analyser.getSequencingConstraints("Service", tuple));
					tList.addAll(analyser.getSequencingConstraints("cors", tuple));
					tList.addAll(analyser.getSequencingConstraints("lifecycle", tuple));
					tList.addAll(analyser.getSequencingConstraints("policy", tuple));
					tList.addAll(analyser.getSequencingConstraints("tagging", tuple));
					tList.addAll(analyser.getSequencingConstraints("website", tuple));
					tList.addAll(analyser.getSequencingConstraints("acl", tuple));
					tList.addAll(analyser.getSequencingConstraints("logging", tuple));
					tList.addAll(analyser.getSequencingConstraints("location", tuple));
					tList.addAll(analyser.getSequencingConstraints("notification", tuple));
					tList.addAll(analyser.getSequencingConstraints("requestPayment", tuple));
					tList.addAll(analyser.getSequencingConstraints("version", tuple));
					tList.addAll(analyser.getSequencingConstraints("bucket", tuple));
					tList.addAll(analyser.getSequencingConstraints("object", tuple));
					tList.addAll(analyser.getSequencingConstraints("multipleobject", tuple));
					tList.addAll(analyser.getSequencingConstraints("ObjectAcl", tuple));
					tList.addAll(analyser.getSequencingConstraints("ObjectTorrent", tuple));
					tList.addAll(analyser.getSequencingConstraints("ObjectRestore", tuple));
					tList.addAll(analyser.getSequencingConstraints("MultipartUpload", tuple));
					tList.addAll(analyser.getSequencingConstraints("Part", tuple));
					for (ITuple t : tList) {
						if (t != null) {
							// System.out.println(t.toString());
							buff.append(t.toString());
							buff.append("\n");
						}
					}
				}
			}
		}
		return buff.toString().trim();
	}
}
