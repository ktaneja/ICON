package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class MarkParser extends AbstractParser 
{
	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new MarkParser();
		}
		return instance;
	}
	

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) {
		getPOSString(gov, dep);
		
		
		Tuple t1 = null;
		Relation r = new Relation(dep);
		
		/*
		 * Check for LeafNode
		 */
		if(depGraph.getChildren(dep).size()>0)
		{
			t1 = parse(dep, depGraph, visited);
		}
		if(t1==null)
		{
			t1 = new Tuple(r);
		}
		
		
		t = (Tuple)TupleUtil.merge(t, t1 , gov.word());
		
		
		logger.info(t.toString());
		return t;
	}

	
}
