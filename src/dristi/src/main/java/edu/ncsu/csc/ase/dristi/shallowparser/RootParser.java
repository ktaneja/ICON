package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.HashMap;
import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class RootParser extends AbstractParser 
{
	private static AbstractParser instance;
	
	private static HashMap<String, String> regex = new HashMap<String, String>();
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new RootParser();
			regex.put("det:nn:prep:dep", "nn[$][prep][dep]");
			
		}
		return instance;
	}
	

	


	public static boolean match(String posTag, String key) 
	{
		String[] posArray = posTag.split(":");
		String[] keyArray = key.split(":");
		if(keyArray.length == posArray.length)
		{
			for(int i=0;i<keyArray.length;i++)
			{
				if(!posArray[i].matches(keyArray[i]))
					return false;
			}
			return true;
		}
		return false;
	}





	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph graph,
			Tuple t, Set<IndexedWord> visited) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
