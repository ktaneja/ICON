package edu.ncsu.csc.ase.dristi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.cache.SentenceCache;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.displayBeans.NLPResult;
import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;
import edu.ncsu.csc.ase.dristi.shallowparser.ParserFactory;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * This class contains methods for accessing Stanford NLP Library
 * @author rahul_pandita
 *
 */
public class NLPParser 
{
	private static Logger log = MyLoggerFactory.getLogger(NLPParser.class);
	
	private static NLPParser instance;
	
	private static StanfordCoreNLP pipeline;
	
	private static SentenceCache senCache;
	
	public static synchronized NLPParser getInstance()
	{
		if(instance == null)
		{
			instance = new NLPParser();
			try 
			{
				senCache = SentenceCache.getInstace();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
		return instance;
	}
	
	/**
	 * Creates the instance of Stanford Parser
	 */
	private NLPParser()
	{
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    pipeline = new StanfordCoreNLP(props);
	}
	
	/**
	 * Creates the List of NLPResult of text, one for each sentence.
	 * Uses Stanford's standard sentence boundary detection.
	 * @param text : the text whose NLPResult representation needs to be created
	 * @return list of NLPResult objects of text, one for each sentence.
	 */
	public List<NLPResult> getDisplayList(String text)
	{
		List<NLPResult> resultList = new ArrayList<NLPResult>(); 
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences)
		{
			NLPResult res = new NLPResult();
			res.setSentnce(sentence.toString());
			res.setPosTree(sentence.get(TreeAnnotation.class).pennString());
			res.setStanfordTypedDependecy(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class).toString());
			if(senCache!= null)
			{
				if(senCache.getTuple(sentence.toString())!=null)
				{
					res.setTuple(senCache.getTuple(sentence.toString()).get(0).toString());
					log.info("Read from Cache!!");
				}
				else
				{
					List<ITuple> list = new ArrayList<>();
					list.add((Tuple)(parse(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class))));
					senCache.addSentence(sentence.toString(), list);
					res.setTuple(senCache.getTuple(sentence.toString()).toString());
					log.info("Wrote to Cache");
				}
			}
			else
			{
				res.setTuple(parse(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class)).toString());
			}
			
			resultList.add(res);
		}
		return resultList;
	}
	
	public List<CoreMap> getSentenceList(String text)
	{
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		return sentences;
	}
	
	/**
	 * Returns the List of POS trees representation of text, one for each sentence.
	 * Uses Stanford's standard sentence boundary detection.
	 * 
	 * @param text: the text whose POS Trees representation needs to be created
	 * @return ordered list of POS Trees one, for each sentence in the text.
	 */
	public List<Tree> getPOSTree(String text)
	{
		List<Tree> posTreeList = senCache.getPOSTree(text);
		if(posTreeList!=null)
		 return posTreeList;
		
		posTreeList = new ArrayList<>(); 
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences)
		{
			posTreeList.add(sentence.get(TreeAnnotation.class));
		}
		senCache.addSentencePOS(text, posTreeList);
		return posTreeList;
	}
	
	/**
	 * Returns the Stanford-typed Dependencies representation of text, one for each sentence.
	 * Uses Stanford's standard sentence boundary detection.
	 * 
	 * @param text: the text whose Stanford dependency representation needs to be created
	 * @return ordered list of Stanford dependency representation, one for each sentence in the text.
	 */
	public List<SemanticGraph> getStanfordDependencies(String text)
	{
		List<SemanticGraph> semanticDepList = senCache.getTypedDependencies(text);
		
		if(semanticDepList!= null)
			return semanticDepList;
		
		semanticDepList = new ArrayList<SemanticGraph>();
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences)
		{
			semanticDepList.add(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class));
		}
		senCache.addSentenceTypedDependencies(text, semanticDepList);
		return semanticDepList;
	}
	
	/**
	 * Returns the Tuple representation of text, one for each sentence.
	 * Uses Stanford's standard sentence boundary detection.
	 * 
	 * @param text: the text whose Tuple representation needs to be created
	 * @return ordered list of Tuple representation, one for each sentence in the text.
	 */
	public List<ITuple> getTuples(String text)
	{
		List<ITuple> tupleList = senCache.getTuple(text); 
		if(tupleList!=null)
			return tupleList;
		
		tupleList = new ArrayList<ITuple>();
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences)
		{
			tupleList.add(parse(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class)));
		}
		senCache.addSentence(text, tupleList);
		return tupleList;
	}
	
	/**
	 * For exploration yet to determine how to use 
	 * @param text
	 * @return
	 */
	public List<SemanticGraph> getNamedEntities(String text)
	{
		List<SemanticGraph> semanticDepList = new ArrayList<SemanticGraph>(); 
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences)
		{
			// traversing the words in the current sentence
		    // a CoreLabel is a CoreMap with additional token-specific methods
		    for (CoreLabel token: sentence.get(TokensAnnotation.class)) 
		    {
		        // this is the text of the token
		        String word = token.get(TextAnnotation.class);
		        // this is the POS tag of the token
		        String pos = token.get(PartOfSpeechAnnotation.class);
		        // this is the NER label of the token
		        String ne = token.get(NamedEntityTagAnnotation.class); 
		        System.out.println(token.word() + " \t:\t " + word + " \t:\t " + pos + " \t:\t " + ne);
		    }
		    semanticDepList.add(sentence.get(CollapsedCCProcessedDependenciesAnnotation.class));
		}
		return semanticDepList;
	}
	
	/**
	 * Returns CoreMap Object 
	 * @param text
	 * @return
	 */
	public List<CoreMap> getCoreMap(String text)
	{
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		return sentences;
	}
	/**
	 * Parses the Stanford-typed Dependencies of a sentence and returns a Tuple representation
	 * 
	 * @param dependency: Stanford Dependency representation of a sentence
	 * @return Tuple Representation of the sentence represented by dependency 
	 */
	public ITuple parse(SemanticGraph dependency) {
		Set<IndexedWord> visited = new HashSet<IndexedWord>();
		if(dependency.vertexSet().size()!=0)
		{
			if(dependency.getRoots().size()>0)
			{
				List<IndexedWord> orderedList = dependency.vertexListSorted();
				Set<IndexedWord> visitedSet = new HashSet<>();
				
				ITuple tuple = ParserFactory.getInstance().getParser("root").parse(dependency.getFirstRoot(),dependency,visited);
				TupleUtil.reorder(tuple, orderedList, visitedSet);
				return tuple;
			}
		} 
		return null;
	}
	
	/**
	 * Parses the Stanford-typed Dependencies of a sentence and returns Co-Reference Chains
	 * @param text : the text whose Co-Reference Chains needs to be created
	 * @return List of CorefChain of the text
	 */
	public List<CorefChain> getCoRefDependencies(String text)
	{
		List<CorefChain> decorefDepList = new ArrayList<CorefChain>(); 
		Annotation document = new Annotation(text);
		
		pipeline.annotate(document);
		Map<Integer, CorefChain> decorefMap = document.get(CorefChainAnnotation.class);
		for(Integer mapIter: decorefMap.keySet())
		{
			decorefDepList.add(decorefMap.get(mapIter));
		}
		return decorefDepList;
	}
	
	/**
	 * Main method for testing to be removed in production or create a unit test
	 * @param args
	 */
	public static void main(String[] args) {
		String text = "How to pick an image from gallery (SD Card) for my app in Android?";
		
		NLPParser parser = NLPParser.getInstance();
		
		//List<Tree> posTreeList = parser.getPOSTree(text);
		//for(Tree postree: posTreeList)
		//	postree.pennPrint();
	/*	List<CorefChain> decorefDepList = parser.getCoRefDependencies(text);
		for(CorefChain c: decorefDepList)
		{
			System.out.println(c.getRepresentativeMention());
			System.out.println();
			for(CorefMention cm : c.getCorefMentions())
			{
				System.out.println(cm);
				System.out.println(cm.corefClusterID);
				
			}
		}*/
		
		List<SemanticGraph> stanford = parser.getStanfordDependencies(text);
		for(SemanticGraph c: stanford)
		{
			System.out.println(c.toFormattedString());
			System.out.println(c);
		}
		
		List<ITuple> tuples = parser.getTuples(text);
		for(ITuple tup: tuples)
			System.out.println(tup);
	}
}
