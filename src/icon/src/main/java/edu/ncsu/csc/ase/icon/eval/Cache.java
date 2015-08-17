package edu.ncsu.csc.ase.icon.eval;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.analysis.SemanticAnalyser;
import edu.ncsu.csc.ase.dristi.knowledge.JavaAPIKnowledge;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * 
 * @author Rahul Pandita
 *
 */
public class Cache {
	
	private static Cache instance; 
	
	private Map<String, Tree> POS_TreeMap;
	
	private Map<String, SemanticGraph> semanticGraphMap;
	
	private Map<String, CoreMap> coreMap;
	
	
	protected NLPParser parser;
	
	protected SemanticAnalyser analyser;
	
	private Cache() {
		POS_TreeMap = new HashMap<String, Tree>();
		semanticGraphMap = new HashMap<String, SemanticGraph>();
		read();
		try
		{
			//parser = NLPParser.getInstance();
			//analyser = new SemanticAnalyser();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}
	private void read() {
		try {
			FileInputStream fis = new FileInputStream("hashmap.ser");
	        ObjectInputStream ois = new ObjectInputStream(fis);
	         coreMap = (HashMap) ois.readObject();
	         ois.close();
	         fis.close();
		} catch (Exception ioe) {
			ioe.printStackTrace();
			coreMap = new HashMap<String, CoreMap>();
		}
	}
	
	public void write() {
		try {
			FileOutputStream fos = new FileOutputStream("hashmap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(coreMap);
			oos.close();
			fos.close();
			System.out.printf("Serialized HashMap data is saved in hashmap.ser");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public synchronized static Cache getInstance()
	{
		if(instance == null)
			instance = new Cache();
		return instance;
	}

	public synchronized Tree getPOSTree(String text) {
		if(!POS_TreeMap.containsKey(text))
		{
			POS_TreeMap.put(text, parser.getPOSTree(text).get(0));
		}
		return POS_TreeMap.get(text);
	}
	
	public synchronized SemanticGraph getSemanticGraph(String text) {
		if(!semanticGraphMap.containsKey(text))
		{
			semanticGraphMap.put(text, parser.getStanfordDependencies(text).get(0));
		}
		return semanticGraphMap.get(text);
	}
	
	
	public synchronized CoreMap getCoreMap(String text) {
		if(!coreMap.containsKey(text))
		{
			coreMap.put(text, parser.getCoreMap(text).get(0));
		}
		return coreMap.get(text);
	}
	
	public synchronized List<String> getSentnceList(String text)
	{
		text = SpecFeature.textCorrection(text);
		List<CoreMap> lst = parser.getSentenceList(text);
		List<String> senList = new ArrayList<String>();
		for(CoreMap mp : lst)
			senList.add(mp.toString());
		 return senList;
	}

	public String clean(String text) {
		return SpecFeature.textCorrection(text);
	}

	public String lemmatize(String ss) {
		StringBuffer buff = new StringBuffer();
		CoreMap sentence = getCoreMap(ss);
	            // Iterate over all tokens in a sentence
	            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	                // Retrieve and add the lemma for each word into the list of lemmas
	               if(!token.get(PartOfSpeechAnnotation.class).startsWith("NN"))
	            	buff.append(token.get(LemmaAnnotation.class)).append(" ");
	            }
	    
		return buff.toString().trim();
	}
	
	public String getFeatureSeq(String ss) {
		return getFeatureSeq(ss, new ArrayList<String>());
	}
	
	private String getWordFeatures(CoreMap sentence, List<String> actions) {
		StringBuffer buff = new StringBuffer();
		for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
			// Retrieve and add the lemma for each word into the list of lemmas
			if((token.get(PartOfSpeechAnnotation.class).startsWith("-LRB-"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("-RRB-"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("FW"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("SYM"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("."))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith(","))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("TO"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("CC"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("PRP"))
        			)
			{
				//omit these Do Nothing
			}
			else if((token.get(PartOfSpeechAnnotation.class).startsWith("NN"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("CD"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("DT"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("JJ"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("VBP"))
        			||(token.get(PartOfSpeechAnnotation.class).startsWith("VBZ"))
        			)
        	{
        		//continue;
        		//buff.append(Character.MAX_VALUE).append(" ");
        	}
        	else
            {
        		if(!SpecFeature.posStopWords().contains((token.get(LemmaAnnotation.class)) + ("_") + (token.get(PartOfSpeechAnnotation.class)).trim().toLowerCase()))
        		{
        			
        			for(String action:actions)
        			{
        				if(action.toLowerCase().trim().equals(token.get(LemmaAnnotation.class).toString().toLowerCase().trim()))
        				{
        					buff.append("MTDCALL").append("_").append(token.get(PartOfSpeechAnnotation.class)).append(" ");
        					break;
        				}
        			
        			}
        			buff.append(token.get(LemmaAnnotation.class)).append("_").append(token.get(PartOfSpeechAnnotation.class)).append(" ");
        		}
            }
            
            
        }
		String returnVal = buff.toString();
		//while(returnVal.contains(Character.MAX_VALUE+" "+Character.MAX_VALUE+" "))
		//	returnVal = returnVal.replace(Character.MAX_VALUE+" "+Character.MAX_VALUE+" ", Character.MAX_VALUE+" ");
		
		//return getPOSTreeFeatures(sentence.get(TreeAnnotation.class));
		return returnVal.trim();
	}
	
	private String getTypedDepenDenciesFeatures(SemanticGraph semanticGraph) {
		StringBuffer buff = new StringBuffer();
		for(IndexedWord wrd: semanticGraph.getRoots())
		{
			
			buff.append("root_").append(wrd.get(PartOfSpeechAnnotation.class).trim().toLowerCase()).append(" ");
		}
		for(SemanticGraphEdge edge :semanticGraph.edgeIterable())
		{
			if(edge.getRelation().getShortName().equalsIgnoreCase("advcl")
					|| edge.getRelation().getShortName().equalsIgnoreCase("aux")
					|| edge.getRelation().getShortName().equalsIgnoreCase("auxpass")
					|| edge.getRelation().getShortName().equalsIgnoreCase("mark")
					|| edge.getRelation().getShortName().equalsIgnoreCase("vmod")
					|| edge.getRelation().getShortName().equalsIgnoreCase("tmod"))
				buff.append(edge.getRelation().getShortName()).append(" ");
		}
		//for()
		return buff.toString().trim();
	}
	
	public String getPOSTreeFeatures(Tree tree) {
		StringBuffer buff = new StringBuffer();
		if(!(tree.isEmpty()||tree.isLeaf()||tree.isPreTerminal()))
		{
			boolean flag = false;
			String ss = tree.label().toString();
			
			for(Tree child:tree.children())
			{
				if(!(child.isEmpty()||child.isLeaf()||child.isPreTerminal()))
				{
					if(!ss.endsWith(child.label().toString()))
					{
						ss = ss  + "_" + child.label();
						flag= true;
					}
				}
			}
			if(flag)
			{
				if(!SpecFeature.stopwords().contains(ss.trim().toLowerCase()))
					buff.append(ss);
			}
			buff.append(" ");
			for(Tree child:tree.children())
			{
				buff.append(getPOSTreeFeatures(child)).append(" ");
				
			}
		}
		return buff.toString().trim();
	}
	public String getFeatureSeq(String ss, List<String> actions) {
		StringBuffer buff = new StringBuffer();
		
		CoreMap sentence = getCoreMap(ss);
        
		buff.append(getTypedDepenDenciesFeatures(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class))).append(" ");
		
		
		
		buff.append(getPOSTreeFeatures(sentence.get(TreeAnnotation.class))).append(" ");
		
		buff.append(getWordFeatures(sentence, actions)).append(" ");
		
		return buff.toString().trim();
	}
}
