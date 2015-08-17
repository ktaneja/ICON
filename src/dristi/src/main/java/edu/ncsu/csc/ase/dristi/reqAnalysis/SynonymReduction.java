package edu.ncsu.csc.ase.dristi.reqAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import edu.ncsu.csc.ase.dristi.configuration.Config;

/**
 * Class to reduce the lexical tokens in Requirement sentences
 * @author rahulpandita
 *
 */
public class SynonymReduction 
{
	private ArrayList<String> senList = new ArrayList<>(); 
	private static String defaultPath = Config.PROJECT_PATH + "data" + File.separator + "senList.txt";
	public SynonymReduction(String path)
	{
		if(path!=null)
			defaultPath =path;
		loadSentnces();
	}

	private void loadSentnces() 
	{
		BufferedReader reader=null;
		try 
		{
			reader = new BufferedReader( new FileReader(defaultPath));
			String         line = null;
		    while( ( line = reader.readLine() ) != null ) 
		    {
		        line = removeLeadingTrailingQuotes(line);
		        senList.add(line);
		    }
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try 
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	    
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getSentences()
	{
		return (List<String>)senList.clone();
	}
	
	private String removeLeadingTrailingQuotes(String line) 
	{
		if(line.startsWith("'"))
		{
			line = line.substring(1);
		}
		if(line.endsWith("'"))
		{
			line = line.substring(0, line.length()-1);
		}
		return line;
	}
	
	public Map<String, Integer> termVector(String text)
	{
		Analyzer analyser = new EnglishAnalyzer();
		Map<String, Integer> termFreqMap = new HashMap<>();
		try 
		{
			TokenStream ts = analyser.tokenStream("abc", new StringReader(text));
			CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);
			ts.reset();
			while (ts.incrementToken()) 
			{
			    
				String term = charTermAttribute.toString();
			    if(termFreqMap.containsKey(term))
			    	termFreqMap.put(term, termFreqMap.get(term)+1);
			    else
			    	termFreqMap.put(term, 1);
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		analyser.close();
		return termFreqMap;
	}
	
	public static void main(String[] args) {
		SynonymReduction sr = new SynonymReduction(null);
		Map<String, Integer> termMap = sr.termVector("AA AB ABC AA AA AA AB AB AB ABC");
		System.out.println(termMap.keySet().size());
		for(String key: termMap.keySet())
			System.out.println(key+ " : " + termMap.get(key));
	}

}
