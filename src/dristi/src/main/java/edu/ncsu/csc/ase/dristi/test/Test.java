package edu.ncsu.csc.ase.dristi.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.analysis.SemanticAnalyser;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;

public class Test 
{
	private static HashMap<String, List<ITuple>> mem2 = new HashMap<>();
	private static HashMap<String, List<String>> mem1 = new HashMap<>();
	
	public static void main(String[] args) throws Exception
	{
		NLPParser parser = NLPParser.getInstance();
		SemanticAnalyser analyser = new SemanticAnalyser();
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		System.out.println("Enter The class =>");
		String clazz = br.readLine();
		System.out.println("Enter The Sentnce =>");
		String text = br.readLine();
		while(text.length()>0)
		{
			test(parser, analyser, clazz, text);
			System.out.println("Enter The class =>");
			clazz = br.readLine();
			System.out.println("Enter The Sentnce =>");
			text = br.readLine();
		}
			 
		
		
	}
	
	public static void main1(String[] args) throws Exception
	{
		NLPParser parser = NLPParser.getInstance();
		SemanticAnalyser analyser = new SemanticAnalyser();
		PrintWriter out = new PrintWriter("temp1.txt");
		
		List<String[]> dataList1 = TestData.prepareData1();
		dataList1 = TestData.prepareData12();
		for(String[] dd: dataList1)
		{ 
			boolean val = false;
			try
			{
				val = test(parser, analyser, dd[0], dd[1]);
			}
			catch(Exception e)
			{
				
			}
			int i = val?1:0;
			out.println(i);
		}
		
		out.close();
	}
	
	public static boolean test(NLPParser parser, SemanticAnalyser analyser, String clazz, String text) {
		System.out.println(text);
		if(text.length()>140)
			return false;
		if (text.trim().toLowerCase().startsWith("returns "))
			return false;
		else if (text.trim().toLowerCase().startsWith("creates "))
			return false;
		else if (text.trim().toLowerCase().startsWith("get "))
			return false;
		else if (text.trim().toLowerCase().startsWith("prints "))
			return false;
		else if (text.trim().toLowerCase().startsWith("reads "))
			return false;
		else if (text.trim().toLowerCase().startsWith("resets "))
			return false;
		else if (text.trim().toLowerCase().startsWith("sets "))
			return false;
		else if (text.trim().toLowerCase().startsWith("tells "))
			return false;
		else if (text.trim().toLowerCase().startsWith("flushes "))
			return false;
		else if (text.trim().toLowerCase().startsWith("for example "))
			return false;
		else if (text.trim().toLowerCase().startsWith("tests "))
			return false;
		else if (text.trim().toLowerCase().startsWith("marks "))
			return false;
		else if (text.trim().toLowerCase().startsWith("connects "))
			return false;
		else if (text.trim().toLowerCase().startsWith("closes "))
			return false;
		else if (text.trim().toLowerCase().startsWith("converts "))
			return false;
		else if (text.trim().toLowerCase().startsWith("constructs "))
			return false;
		else if (text.trim().toLowerCase().startsWith("appends "))
			return false;
		else if (text.trim().toLowerCase().startsWith("causes "))
			return false;
		else if (text.trim().toLowerCase().startsWith("checks "))
			return false;
		else if (text.trim().toLowerCase().startsWith("deletes "))
			return false;
		else if (text.trim().toLowerCase().startsWith("repositions "))
			return false;
		else if (text.trim().toLowerCase().startsWith("resets "))
			return false;
		else if (text.trim().toLowerCase().startsWith("skips "))
			return false;
		text = text.replace("&∨g.eclipse.jdt.core.dom.QualifiedName&&", "XX");
		
	
		boolean val = false;
		List<SemanticGraph> posTreeList1 = parser.getStanfordDependencies(text);
		for(SemanticGraph posTree:posTreeList1)
		{
			System.out.println(posTree.toFormattedString());
			for(SemanticGraphEdge edge :posTree.edgeListSorted())
			{
				if(edge.getRelation().equals(GrammaticalRelation.valueOf("tmod")))
					System.out.println("here");
				else if(edge.getRelation().equals(GrammaticalRelation.valueOf("mark")))
					System.out.println("here");	
						
			}
		}
		List<String> sentenceList;
		if(mem1.containsKey(text))
		{
			sentenceList = mem1.get(text);
		}
		else
		{
			sentenceList = analyser.applyReduction(text);
			mem1.put(text, sentenceList);
		}
		
		for(String sentence:sentenceList)
		{
			//System.out.println(sentence);
			/*List<Tree> posTreeList = parser.getPOSTree(sentence);
			for(Tree posTree:posTreeList)
				posTree.pennPrint();*/
			List<ITuple> tupleList;
			if(mem2.containsKey(sentence))
			{
				tupleList = mem2.get(sentence);
			}
			else
			{
				tupleList = parser.getTuples(sentence);
				mem2.put(sentence, tupleList);
			}
			for(ITuple tuple:tupleList)
			{
				if(tuple!=null)
				{
					//System.out.println(tuple.toString());
					List<ITuple> tList = analyser.getSequencingConstraints(clazz, tuple);
					for(ITuple t:tList)
					{
						if(t!=null)
						{
							System.out.println(t.toString());
							val = true;
						}
					}
				}
			}
		}
		return val;
	}
}
