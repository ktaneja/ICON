package edu.ncsu.csc.ase.dristi.textanalytics;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.datastructure.IDFreqPair;
import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;

public class NGramDriver{

	private static Logger logger = MyLoggerFactory.getLogger(NGramDriver.class);
	
	public static List<IDFreqPair<String>> getNGrams(Reader reader, int threshold) {
		List<IDFreqPair<String>> freqList = new ArrayList<>();
		try {
			StandardAnalyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);

			ShingleAnalyzerWrapper shingleAnalyzer = new ShingleAnalyzerWrapper(analyzer, 10);

			TokenStream stream = shingleAnalyzer.tokenStream("contents", reader);
			CharTermAttribute charTermAttribute = stream.getAttribute(CharTermAttribute.class);

			stream.reset();
			Map<String, Integer> valMap = new HashMap<String, Integer>();
			String temp;
			while (stream.incrementToken()) {
				temp = charTermAttribute.toString();
				if (valMap.containsKey(temp))
					valMap.put(temp, valMap.get(temp) + 1);
				else
					valMap.put(temp, 1);
			}
			shingleAnalyzer.close();
			Integer count;
			//TODO remove overlap
			for (String val : valMap.keySet()) {
				count = valMap.get(val);
				if ((count >= threshold) && (val.contains(" ")))
					freqList.add(new IDFreqPair<String>(val, valMap.get(val)));
			}

			Collections.sort(freqList);
			Collections.reverse(freqList);
		} catch (Exception e) {
			logger.error("Parse Failed.  Reason: " + e.getMessage(), e);
		}
		return freqList;
	}

	/**
	 * @param freqList
	 */
	public static void prettyPrintNGrams(List<IDFreqPair<String>> freqList) {
		for (IDFreqPair<String> pair : freqList)
			System.out.println(pair.getFreq() + "\t" + pair.getId());
	}
	
	public static void main (String [] args){
        prettyPrintNGrams(getNGrams(getDescriptions(),10));
	}
    
	private static Reader getDescriptions()
	{
		Reader reader = null;
		File file = new File("data\\senList.txt");
    	try {
			reader = new FileReader(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return reader;
	}
	
}
