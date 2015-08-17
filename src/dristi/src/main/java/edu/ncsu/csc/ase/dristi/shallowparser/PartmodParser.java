package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class PartmodParser extends AbstractParser
{

	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new PartmodParser();
		}
		return instance;
	}

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) 
	{
		Tuple t1;
		/*
		 * Check for LeafNode
		 */
		if(depGraph.getChildren(dep).size()>0)
		{
			t1 = parse(dep, depGraph, visited);
			Tuple t2 =createTuple(gov,dep);
			t1 = (Tuple)TupleUtil.merge(t1, t2 , dep.word());
		}
		else
		{
			t1 = createTuple(gov,dep);
		}
		
		t = (Tuple)TupleUtil.merge(t, t1 , gov.word());
		
		logger.info(t.toString());
		return t;
		
		
	}

	
	
}
