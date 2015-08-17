package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class ApposParser extends AbstractParser
{

	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new ApposParser();
		}
		return instance;
	}

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) 
	{
		
		//Tuple t1 = createTuple(gov);
		Relation r = new Relation("is", RelationType.One2One);
		Tuple t1 = new Tuple(createTuple(gov), r, createTuple(dep));
		/*
		 * Check for LeafNode
		 */
		if(depGraph.getChildren(dep).size()>0)
		{
			Tuple t2 = parse(dep, depGraph, visited);
			t1 = (Tuple)TupleUtil.merge(t1, t2, dep.word());
		}
		
		
		t = (Tuple)TupleUtil.merge(t, t1 , gov.word());
		
		logger.info(t.toString());
		return t;
		
		
	}

}
