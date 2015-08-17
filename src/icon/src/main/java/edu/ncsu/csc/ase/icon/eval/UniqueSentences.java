package edu.ncsu.csc.ase.icon.eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniqueSentences {
	
	private List<ExcelRow> senList;
	
	public static void main(String[] args) {
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		sr = new SentenceReader();
		List<ExcelRow> senList = sr.readTemporalSentences();
		UniqueSentences obj = new UniqueSentences(sr.readSentences());
		obj.prettyPrint(obj.getMultipleSentnces());
		System.out.println(obj.getMultipleSentnces().size());
		System.out.println(obj.getUniqueSentnces().size());
		System.out.println(senList.size());
	}
	
	public UniqueSentences(List<ExcelRow> senList) {
		this.senList = senList;
	}
	
	
	public List<String> getMultipleSentnces() {
		Map<String, Integer> map =  getUniqueSenMap(senList);
		List<String> returnList = new ArrayList<String>();
		for(String sen: map.keySet())
		{
			if (map.get(sen)>1)
				returnList.add(map.get(sen) + "\t" + sen);
		}
		return returnList;
	}
	
	public List<String> getUniqueSentnces() {
		Map<String, Integer> map =  getUniqueSenMap(senList);
		List<String> returnList = new ArrayList<String>();
		returnList.addAll(map.keySet());
		return returnList;
	}
	
	public void prettyPrint(List<String> list)
	{
		for(String str: list)
		{
			System.out.println(str);
		}
	}
	
	private Map<String, Integer> getUniqueSenMap(List<ExcelRow> senList)
	{
		Map<String, Integer> retMap = new HashMap<String, Integer>();
		String comment;
		for(ExcelRow row: senList)
		{
			comment = row.getComment().trim().toLowerCase();
			if(retMap.containsKey(comment))
				retMap.put(comment, retMap.get(comment)+1);
			else
				retMap.put(comment, 1);
		}
		return retMap;
	}
}
