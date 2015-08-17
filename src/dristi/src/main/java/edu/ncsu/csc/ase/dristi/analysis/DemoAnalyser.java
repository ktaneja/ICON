 package edu.ncsu.csc.ase.dristi.analysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.displayBeans.NLPResult;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.PhraseReduction;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.SentenceCompletion;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.TokenReduction;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.WordNetSimilarity;
import edu.ncsu.csc.ase.dristi.heuristics.syntnax.POSSentenceSplitting;
import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;
import edu.ncsu.csc.ase.dristi.util.ConsoleUtil;
import edu.ncsu.csc.ase.dristi.util.FileUtilExcelOld;
import edu.stanford.nlp.trees.Tree;

/**
 * This Class contains logic to perform Semantic Analysis of REST API Text
 * @author rahul_pandita
 *
 */
public class DemoAnalyser 
{
	private static Logger logger = MyLoggerFactory.getLogger(SemanticAnalyser.class);
	
	private PhraseReduction phraseRed;
	
	private TokenReduction tokenRed;
	
	private SentenceCompletion sentenceCompletion;
	
	private POSSentenceSplitting sentenceSplitting;
	
	public DemoAnalyser()
	{
		phraseRed = PhraseReduction.getInstance();
		tokenRed = TokenReduction.getInstance();
		sentenceCompletion = SentenceCompletion.getInstance();
		sentenceSplitting = POSSentenceSplitting.getInstance();
	}
	
	public boolean isIntentSentnce1(Tree tree)
	{
		
		return false;
	}
	
	public String applyDecoration(String text)
	{
		logger.debug("begin applying reduction.");
		text = tokenRed.applyDecoration(text);
		text = phraseRed.applyDecoration(text);
		text = sentenceCompletion.applyDecoration(text);
		return text;
	}
	
	public List<String> applyReduction(String text)
	{
		logger.debug("begin applying reduction.");
		text = tokenRed.applyReduction(text);
		text = phraseRed.applyReduction(text);
		text = sentenceCompletion.applyReduction(text);
		List<String> sentenceList = sentenceSplitting.applyReduction(text);
		return sentenceList;
	}
	
	public void store(List<String> conceptList) {
		try {
			for(String str:conceptList)
				FileUtilExcelOld.getInstance().writeDataToExcel(str);
			
			ConsoleUtil.readConsole("Wrote File. Press Enter to save.");
			
			
			FileUtilExcelOld.getInstance().commitXlS();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		String query = "This implementation of the GET operation returns a list of all buckets owned by the authenticated sender of the request.To authenticate a request, you must use a valid AWS Access Key ID that is registered with Amazon S3.Anonymous requests cannot list buckets, and you cannot list buckets that you did not create.";
		List<NLPResult> str= new ArrayList<NLPResult>();
		NLPParser parser = NLPParser.getInstance();
		List<String> reducedSentenceList;
		DemoAnalyser da = new DemoAnalyser();
		reducedSentenceList = da.applyReduction(query);
		for(String redSen: reducedSentenceList)
		{
			str.addAll(parser.getDisplayList(redSen));
		}
		
		for(NLPResult res: str)
		{
			ITuple tup = NLPParser.getInstance().getTuples(res.sentnce).get(0);
			if(da.isIntentSentnce(tup))
				da.getIntent(tup);
		}
	}
	
	private void getIntent(ITuple tup) {
		// TODO Auto-generated method stub
		
	}

	public boolean isIntentSentnce(ITuple t) {
		if (t == null)
			return false;
		ArrayList<String> synonymList = new ArrayList<String>();
		synonymList.add("implementation");
		
		ArrayList<String> actionList = new ArrayList<String>();
		actionList.add("of");
		
		ArrayList<ITuple> tList = searchAtom(t, synonymList,new ArrayList<ITuple>());
		
		for (ITuple t1 : tList) {
			if (t1 != null) 
			{
				System.out.println(findAction(actionList, t, t1)); 
			{
				return true;
			}
			}
		}
		return false;
	}
	
	public static ArrayList<ITuple> searchAtom(ITuple t,
			ArrayList<String> atom, ArrayList<ITuple> tupleList) {
		if (t == null)
			return tupleList;
		if (t.isTerminal()) {
			for (String res : atom) {
				if (t.getEntity().getName().toString().toLowerCase()
						.contains(res)) {
					if (!tupleList.contains(t))
						tupleList.add(t);
				}
			}
		} else {
			for (String res : atom) {
				if (t.getRelation().getName().toLowerCase().contains(res)) {
					if (!tupleList.contains(t))
						tupleList.add(t);
				}
			}
			tupleList = searchAtom(t.getLeft(), atom, tupleList);
			tupleList = searchAtom(t.getRight(), atom, tupleList);
		}
		return tupleList;
	}
	
	private static ITuple findAction(ArrayList<String> actions, ITuple parent_Tuple, ITuple current_Tuple) {
		ITuple t_parent = parent_Tuple.findParent(current_Tuple);
		ITuple t_Sibling = parent_Tuple.findSibling(current_Tuple);
		String comp_Value = "";
		if ((t_parent != null) && !t_parent.same(current_Tuple)) {
			comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try {
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value,
							action)) {
						return t_parent;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (t_Sibling != null) {
				if (t_Sibling.isTerminal())
					comp_Value = t_Sibling.toString();
				else
					comp_Value = t_Sibling.getRelation().getName();
				for (String action : actions) {
					try {
						if (WordNetSimilarity.getInstance().isSimilar(
								comp_Value, action)) {
							return t_Sibling;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return findAction(actions, parent_Tuple, t_parent);
		} else if (t_parent.same(current_Tuple)) {
			if (t_parent.isTerminal())
				comp_Value = t_parent.toString();
			else
				comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try {
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value,
							action)) {
						return t_parent;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static boolean isAction(ArrayList<String> actions, ITuple t1, ITuple t2) {
		ITuple t_parent = t1.findParent(t2);
		ITuple t_Sibling = t1.findSibling(t2);
		String comp_Value = "";
		if ((t_parent != null) && !t_parent.same(t2)) {
			comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try {
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value,
							action)) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (t_Sibling != null) {
				if (t_Sibling.isTerminal())
					comp_Value = t_Sibling.toString();
				else
					comp_Value = t_Sibling.getRelation().getName();
				for (String action : actions) {
					try {
						if (WordNetSimilarity.getInstance().isSimilar(
								comp_Value, action)) {
							return true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return isAction(actions, t1, t_parent);
		} else if (t_parent.same(t2)) {
			if (t_parent.isTerminal())
				comp_Value = t_parent.toString();
			else
				comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try {
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value,
							action)) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
