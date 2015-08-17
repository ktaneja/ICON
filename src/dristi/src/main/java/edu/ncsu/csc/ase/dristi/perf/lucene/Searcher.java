package edu.ncsu.csc.ase.dristi.perf.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.util.ConsoleUtil;
import edu.ncsu.csc.ase.dristi.util.FileUtilExcel;

public class Searcher 
{
	
	private static CharArraySet swords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	
	public static void main(String[] args) 
	{
		Searcher searcher = new Searcher();
		String searchTerm;
		try 
		{
			searchTerm = ConsoleUtil.readConsole("Enter SearchTerm -> : ");
			while(searchTerm.length()>0)
			{
				searchTerm = QueryParser.escape(searchTerm);
				Query q = new QueryParser("COMMENT", new SimpleAnalyzer()).parse(searchTerm);
				searcher.search(q);
				searchTerm = ConsoleUtil.readConsole("Enter SearchTerm -> : ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void search(Query q) throws Exception {
		int hitsPerPage = 200;
		Directory index = FSDirectory.open(new File(Config.JAVA_API_LUCENE_SIMPLE_IDX_PATH));
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		System.err.println(hits.length);
		Map<String, Integer> freqMap = new HashMap<>();
		FileUtilExcel resultWriter = new FileUtilExcel("Perf1");
		for (int i = 0; i < hits.length; ++i) 
		{
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			//System.out.println("MTDNAME" + d.get("MTDNAME"));
			//System.out.println("TYPE" + d.get("TYPE"));
			//System.out.println("COMMENT"+ d.get("COMMENT"));
			resultWriter.writeDataToExcel(d.get("TYPE"),d.get("MTDNAME"),d.get("COMMENT"));
			Terms vector = reader.getTermVector(docId, "COMMENT");
			if(vector!= null)
				freqMap = getTermFreq(vector, freqMap);
		}
		resultWriter.commitXlS();
		prettyprint(freqMap);
		
	}
	
	private void prettyprint(Map<String, Integer> freqMap) {
		
		ValueComparator comparator = new ValueComparator(freqMap);
		Map<String, Integer> orderedFreqMap = new TreeMap<>(comparator);
		orderedFreqMap.putAll(freqMap);
		int i=0;
		for(String term: orderedFreqMap.keySet())
		{
			System.out.println(term + "\t" + freqMap.get(term));
			i++;
			if(i>50)
				break;
		}
		
	}

	private Map<String, Integer>  getTermFreq(Terms vector, Map<String, Integer> freqMap) throws IOException
	{
		TermsEnum termsEnum = null;
		termsEnum = vector.iterator(termsEnum);
		BytesRef text = null;
		String term;
		int freq;
		while ((text = termsEnum.next()) != null) 
		{
		    term = text.utf8ToString();
		    freq = (int) termsEnum.totalTermFreq();
		    if(!swords.contains(term))
			{
		    	if(freqMap.containsKey(term))
		    		freqMap.put(term, freqMap.get(term)+ freq);
				else
		    		freqMap.put(term, freq);
		    }
		}
		
		return freqMap;
	}
	
	
	class ValueComparator implements Comparator<String> {

	    Map<String, Integer> base;
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
}
