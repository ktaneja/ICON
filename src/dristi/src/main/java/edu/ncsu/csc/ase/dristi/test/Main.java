package edu.ncsu.csc.ase.dristi.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.shallowparser.ParserFactory;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class Main 
{
	
	private static Main instance;
	
	private static StanfordCoreNLP pipeline;
	
	public static synchronized Main getInstance()
	{
		if(instance == null)
		{
			instance = new Main();
		}
		return instance;
	}
	
	private Main()
	{
		 // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    pipeline = new StanfordCoreNLP(props);
	}
	
	public static ArrayList<String> readInput(String file) 
	{
		ArrayList<String> reList = new ArrayList<String>();
		try 
		{
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			int i =0;
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				reList.add(strLine.trim());
				//System.out.println(strLine);
				if(strLine.startsWith("**"))
				{
					System.out.println(strLine);
					i++;
				}
			}
			System.err.println("Size of input ->" + (reList.size()-i));
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return reList;
	}
	

	public static void main1(String[] args) throws FileNotFoundException 
	{
		ArrayList<String> senList = readInput("/Users/rahulpandita/Documents/Research/FDA/eval/Data1.txt");
		ArrayList<String> senList1 = readInput("/Users/rahulpandita/Documents/Research/FDA/eval/Data.txt");
		
		PrintStream orgStream = null;
		PrintStream fileStream = null;
		PrintStream orgErrStream = null;

		// Saving the original stream
		orgStream = System.out;
		// Saving the original Error stream
		orgErrStream = System.err;
		fileStream = new PrintStream(new FileOutputStream("eval.txt", true));

		long startTime = System.currentTimeMillis();
		Main.getInstance();
		long endTime = System.currentTimeMillis();

		float seconds = (endTime - startTime) / 1000F;
		// redirecting console output to file
		System.setOut(fileStream);
		System.setErr(fileStream);
		System.out.println("Time to load the Drishti parser!");
		System.out.println(Float.toString(seconds) + " seconds.");
		System.out.println("_________________________________________________________________________________");
		
		long startTime1 = System.currentTimeMillis();
		ReVerbExtractor reverb = new ReVerbExtractor();
		long endTime1 = System.currentTimeMillis();

		float seconds1 = (endTime1 - startTime1) / 1000F;
		// redirecting console output to file
		System.setOut(fileStream);
		System.setErr(fileStream);
		System.out.println("Time to load the reverb parser!");
		System.out.println(Float.toString(seconds1) + " seconds.");
		System.out.println("_________________________________________________________________________________");
		System.setOut(orgStream);
		System.setErr(orgErrStream);
		
		Iterator<String> iter = senList.iterator();
		ArrayList<String> timeplot = new ArrayList<String>();
		int i=0;
		while (iter.hasNext()) 
		{
			String s = iter.next();
			String s1 = senList1.get(i);
			i++;
			if(s.startsWith("**"))
			{
				System.setOut(fileStream);
				System.setErr(fileStream);

				System.out.println(s);

				System.setOut(orgStream);
				System.setErr(orgErrStream);
			}
			else
			{
				try 
				{
					//InputStreamReader isr = new InputStreamReader(System.in);
					//BufferedReader br = new BufferedReader(isr);
					//System.out.println("Enter The Sentnce =>");
					//String s = br.readLine();
					//if (s.length() == 0)
					//	break;
	
					// redirecting console output to file
					System.setOut(fileStream);
					System.setErr(fileStream);
	
					System.out.println(s1);
	
					System.setOut(orgStream);
					System.setErr(orgErrStream);
	
					startTime = System.currentTimeMillis();
					Tuple t = Main.getInstance().parse(s);
					endTime = System.currentTimeMillis();
	
					seconds = (endTime - startTime) / 1000F;
					
					startTime1 = System.currentTimeMillis();
					Iterable<ChunkedBinaryExtraction> op = reverb.extractFromString(s1); 
			        
					
					endTime1 = System.currentTimeMillis();
	
					seconds1 = (endTime1 - startTime1) / 1000F;
					
					// redirecting console output to file
					System.setOut(fileStream);
					System.setErr(fileStream);
	
					// System.out.println(s);
					System.out.println("Drishti time ->" + Float.toString(seconds) + " seconds.");
					System.out.println("Reverb  time ->" + Float.toString(seconds1) + " seconds.");
					timeplot.add(Float.toString(seconds)+","+Float.toString(seconds1));
					System.out.println("_________________________________________________________________________________");
					System.out.println(t.toString());
					System.out.println("_________________________________________________________________________________");
					for (ChunkedBinaryExtraction e : op) {
			            System.out.println(e.getArgument1());
			            System.out.println(e.getRelation());
			            System.out.println(e.getArgument2());
			        }
					System.out.println("_________________________________________________________________________________");
					System.setOut(orgStream);
					System.setErr(orgErrStream);
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.setOut(fileStream);
					System.setErr(fileStream);
	
					System.out.println("ERROR OCCURED!");
					System.out.println("_________________________________________________________________________________");
	
					System.setOut(orgStream);
					System.setErr(orgErrStream);
				}
			}
		}
		
		Iterator<String> it = timeplot.iterator();
		System.out.println("_________________________________________________________________________________");
		System.out.println("_________________________________________________________________________________");
		while(it.hasNext())
		{
			System.setOut(fileStream);
			System.setErr(fileStream);

			System.out.println(it.next());
			

			System.setOut(orgStream);
			System.setErr(orgErrStream);
		}
	}
	
	public static void writeTuple(Tuple t) {
		
		try{
			FileWriter fstream = new FileWriter("out.txt",true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(t.toString());
			out.write("\n______________________________________________________________\n");
			out.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		
	}

	public Tuple parse(String text) 
	{
	    // read some text in the text variable
	    // except as used in the last sentence of section 702(a),
	   // String text = "The term (\"State\") means any State or Territory of the x1, the x2, and the x3 .";//... // Add your text here!
	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      /*for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class); 
	        //System.out.println(token.word() + " : " + ne);
	      }*/

	      // this is the parse tree of the current sentence
	      Tree tree = sentence.get(TreeAnnotation.class);
	      tree.pennPrint();
	       // this is the Stanford dependency graph of the current sentence
	      SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	      	      
	      dependencies.prettyPrint();
	      
	      Tuple tuple = parse(dependencies);
	      
	      
	      
	      return tuple;
	      
	    }

	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	    //Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
	    //System.out.println(graph);
	    return null;
	}

	private static Tuple parse(SemanticGraph dependencies) {
		Set<IndexedWord> visited = new HashSet<IndexedWord>();
		if(dependencies.vertexSet().size()!=0)
		{
			if(dependencies.getRoots().size()>0)
				return parse(dependencies.getFirstRoot(),dependencies,visited);
		} 
		return null;
	}

	private static Tuple parse(IndexedWord gov, SemanticGraph dependencies, Set<IndexedWord> visited) {
		return ParserFactory.getInstance().getParser("root").parse(gov, dependencies, visited);
	}
}
