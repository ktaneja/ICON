package edu.ncsu.csc.ase.icon.ml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import edu.ncsu.csc.ase.dristi.util.ConsoleUtil;
import edu.ncsu.csc.ase.icon.eval.Cache;
import edu.ncsu.csc.ase.icon.eval.ExcelRow;
import edu.ncsu.csc.ase.icon.eval.SentenceReader;

public class TFIDF {
	
	public static Map<String, List<String>> docWrdFreq = new HashMap<String, List<String>>();
	
	public static Map<String, List<String>> wrdDocFreq = new HashMap<String, List<String>>();
	
	private static List<String> dict = new ArrayList<String>();
	private static List<Integer> dict_freq = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		xx();
	}
	
	private static List<Instance> getInstanceList() {
		List<Instance> insList = new ArrayList<Instance>();
		//insList.add(new DataInstnace("true, true, false, false", "Temporal", "Java", "blah must be called b4"));
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		List<ExcelRow> rowList = sr.readSentences();
		tst(insList, rowList);
		return insList;
	}

	private static void tst(List<Instance> insList, List<ExcelRow> rowList) {
		int i=1;
		Instance ins;
		String ss;
		for(ExcelRow row: rowList)
		{
			ss = row.getComment();
			ss = Cache.getInstance().clean(ss);
			ss = Cache.getInstance().getFeatureSeq(ss);
			ins = new Instance(ss,row.getFinalMarked(), "Java", "aa");
			insList.add(ins);
			System.out.println("Processed "+ i+"/"+rowList.size());
			i=i+1;
		}
		System.out.println(insList.size());
	}
	
	private static void xx()
	{
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		
		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new TFIDFCompute(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		
		Pipe instancePipe = new SerialPipes(pipeList);
		
		InstanceList instances = new InstanceList (instancePipe);
		instances.addThruPipe(getInstanceList().iterator());
		compute();
	}
	
	public static synchronized void addList(String word, Object instance)
	{
		int i =0;
		int val= 1;
		word = word.toLowerCase();
		word = word.trim();
		
		if(dict.contains(word))
		{
			i = dict.indexOf(word);
			val = dict_freq.get(i) +1;
			dict_freq.set(i, val);
		}
		else
		{
			dict.add(word);
			dict_freq.add(val);
		}
		
		
		//IDSorter
		
	}
	
	private static void compute() {
		
		ConsoleUtil.readConsole("here");
		List<IDSorter> lst = new ArrayList<>();
		for(int i=0;i<dict.size();i++)
		{
			if(dict.get(i).equals("nested")){
				System.out.println(dict.get(i));
				System.out.println(dict_freq.get(i));
				System.out.println(new Double(dict_freq.get(i)));
			}
			lst.add(new IDSorter(i, Double.valueOf(String.valueOf(dict_freq.get(i)))));
		}
		IDSorter[] sortedArray = lst.toArray(new IDSorter[lst.size()]);
		Arrays.sort(sortedArray);
		for(int i=0; i< sortedArray.length; i++)
			System.out.println(dict.get(i)+"\t"+sortedArray[i].getWeight());

	}
}
