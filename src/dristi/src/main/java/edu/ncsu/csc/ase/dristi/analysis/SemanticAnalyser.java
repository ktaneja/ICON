 package edu.ncsu.csc.ase.dristi.analysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.TextAnalysisEngine;
import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.EntityType;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.PhraseReduction;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.SentenceCompletion;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.TokenReduction;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.WordNetSimilarity;
import edu.ncsu.csc.ase.dristi.heuristics.syntnax.POSSentenceSplitting;
import edu.ncsu.csc.ase.dristi.knowledge.Knowledge;
import edu.ncsu.csc.ase.dristi.knowledge.KnowledgeAtom;
import edu.ncsu.csc.ase.dristi.knowledge.RESTAPIKnowledge1;
import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;
import edu.ncsu.csc.ase.dristi.util.ConsoleUtil;
import edu.ncsu.csc.ase.dristi.util.FileUtil;
import edu.ncsu.csc.ase.dristi.util.FileUtilExcelOld;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.semgraph.SemanticGraph;

/**
 * This Class contains logic to perform Semantic Analysis of REST API Text
 * @author rahul_pandita
 *
 */
public class SemanticAnalyser 
{
	private static Logger logger = MyLoggerFactory.getLogger(SemanticAnalyser.class);
	
	private PhraseReduction phraseRed;
	
	private TokenReduction tokenRed;
	
	private SentenceCompletion sentenceCompletion;
	
	private POSSentenceSplitting sentenceSplitting;
	
	private NLPParser parser;
	
	public SemanticAnalyser()
	{
		phraseRed = PhraseReduction.getInstance();
		tokenRed = TokenReduction.getInstance();
		sentenceCompletion = SentenceCompletion.getInstance();
		sentenceSplitting = POSSentenceSplitting.getInstance();
		parser = NLPParser.getInstance();
		//JavaAPIKnowledge.addKnowledge();
		RESTAPIKnowledge1.addKnowledge();
	}
	
	public boolean isIntentSentnce(Tree tree)
	{
		return false;
	}
	
	public List<ITuple> getSequencingConstraints(String clazz, ITuple tuple)
	{
		List<ITuple> retList= new ArrayList<>();
		if(tuple!= null)
		{	
			Knowledge k = Knowledge.getInstance();
			KnowledgeAtom a = k.fetchAtom(clazz);
			if(a!=null)
			{
			System.out.println("");
			ArrayList<String> synList = a.getSynonyms();
			synList.add("thisObject");
			List<ITuple> tList = searchMatchingEntity(tuple, synList, new ArrayList<ITuple>());
			for(ITuple tMatch:tList)
			{
				String action = isAction(a.getActions(), tuple, tMatch);
				if(action!="")
				{
					Tuple e = new Tuple(new Entity(a.getName(), EntityType.Name));
					Tuple t = new Tuple(new Relation(action, RelationType.temp), e);
					
					retList.add(t);
					
				}
					
			}
			}
		}
		return retList;
	}
	
	
	private String isAction(List<String> actions, ITuple t1, ITuple t2) 
	{
		ITuple t_parent = t1.findParent(t2);
		String comp_Value = "";
		if ((t_parent != null) && !t_parent.same(t2)) {
			comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try 
				{
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value, action)) 
					{
						return action;
					}
				} 
				catch (Exception e) 
				{
					logger.error("", e);
				}
			}
			return isAction(actions, t1, t_parent);
		} 
		else if (t_parent.same(t2)) 
		{
			if (t_parent.isTerminal())
				comp_Value = "";
			else
				comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try 
				{
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value, action)) 
					{
						return action;
					}
				} catch (Exception e) 
				{
					logger.error("", e);
				}
			}
		}
		return "";
	}
	
	/**
	 * Searches for a given entity in the <code>tuple</code> representation
	 * @param tuple 
	 * @param synonymnList
	 * @param tupleList recursion variable
	 * @return List of {@code ITuples} that match the synonyms in the {@literal synonymList} 
	 */
	private List<ITuple> searchMatchingEntity(ITuple tuple, List<String> synonymnList, List<ITuple> tupleList) 
	{
		// 
		if (tuple == null)
			return tupleList;
		
		if (tuple.isTerminal()) 
		{
			for (String res : synonymnList) {
				if (tuple.getEntity().getName().toString().toLowerCase().contains(res.toLowerCase())) {
					if (!tupleList.contains(tuple))
					{
						tupleList.add(tuple);
					}
				}
			}
		} 
		else 
		{
			tupleList = searchMatchingEntity(tuple.getLeft(), synonymnList, tupleList);
			tupleList = searchMatchingEntity(tuple.getRight(), synonymnList, tupleList);
		}
		return tupleList;
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
	
	public List<String> analyse(String text)
	{
		logger.debug(text);
		List<String> senList = applyReduction(text);
		
		
		
		List<SemanticGraph> semGraphList = new ArrayList<SemanticGraph>();
		
		for(String sen: senList)
		{
			//System.out.println(sen);
			semGraphList.addAll(parser.getStanfordDependencies(sen));
		}
		
		List<String> conceptList = new ArrayList<String>();
		for(SemanticGraph semGraph : semGraphList)
		{
			try{
				TextAnalysisEngine.parse(semGraph);
				//System.out.println(t.toString());
		//		conceptList.addAll(getConcept(t));
			}
			catch(Exception e)
			{
				logger.error("error parsing sentnce \n" + semGraph , e);
			}
		}
		return conceptList;	
	}

	public void store(List<String> conceptList) {
		try {
			for(String str:conceptList)
				FileUtilExcelOld.getInstance().writeDataToExcel(str);
			
			ConsoleUtil.readConsole("Wrote File. Press Enter to save.");
			
			
			FileUtilExcelOld.getInstance().commitXlS();
			
		} catch (Exception e) {
			logger.error("", e);
		}
		
	}

	public List<String> getConcept(ITuple tuple) {
		List<String> tupleList = new ArrayList<String>();
		if (tuple == null)
			return tupleList;
		else if (tuple.isTerminal()) {
			tupleList.add(tuple.getEntity().getName());
		} else {
			tupleList.addAll(getConcept(tuple.getLeft()));
			tupleList.addAll(getConcept(tuple.getRight()));
		}
		return tupleList;
	}
	
//	public static void main1(String[] args) {
//		ArrayList<Operation> operList = AmazonS3RESTAPICrawler.readOperations();
//		SemanticAnalyser sa = new SemanticAnalyser();
//		List<String> conceptList = new ArrayList<String>();
//		for(Operation oper: operList)
//		{
//			System.err.println(oper.getDescription());
//			//
//			for(String str: sa.analyse(oper.getDescription()))
//			{
//				if(!conceptList.contains(str.trim()))
//					conceptList.add(str.trim());
//			}
//			
//			
//		}
//		ConsoleUtil.readConsole("Press Enter to View Concept List");
//		for(String str: conceptList)
//			System.out.println(str);
//		ConsoleUtil.readConsole("Press Enter to Continue");
//		sa.store(conceptList);
//	}
	
	public static void main(String[] args) {
		SemanticAnalyser sa = new SemanticAnalyser();
		List<String> senList = FileUtil.readFile(Config.DATA_FILE);
		for (String s : senList)
		{
			if(s.startsWith("**********"))
			{
				System.out.println(s.replace("**********", ""));
				
			}
			if(s.contains("must")||s.contains("can")||s.contains("before"))
			{
				try
				{
					sa.analyse(s);
				}catch(Exception e)
				{
					ConsoleUtil.readConsole(s);
				}
			}
		}
		
	}
	
}
