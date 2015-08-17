package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class DepParser extends AbstractParser 
{
	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new DepParser();
		}
		return instance;
	}
	

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) {
		getPOSString(gov, dep);
		
		
		Tuple t1 = null;
		Tuple t2 = new Tuple(t,new Relation("is",RelationType.temp),createTuple(dep));
		
		/*
		 * Check for LeafNode
		 */
		if(depGraph.getChildren(dep).size()>0)
		{
			t1 = parse(dep, depGraph, visited);
			t2 = (Tuple)TupleUtil.merge(t2, t1 , dep.word());
		}
		
		t = (Tuple)TupleUtil.merge(t, t2 , gov.word());
		logger.info(t.toString());
		return t;
	}

	
}
