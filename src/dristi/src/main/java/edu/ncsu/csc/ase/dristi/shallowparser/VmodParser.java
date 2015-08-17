package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class VmodParser extends AbstractParser
{

	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new VmodParser();
		}
		return instance;
	}

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) {
		
		Tuple t1;
		/*
		 * Check for LeafNode
		 */
		if(depGraph.getChildren(dep).size()>0)
		{
			t1 = parse(dep, depGraph, visited);
			if(t1.isPartial())
			{
				t1 = new Tuple(t1.getRelation(),new Tuple(new Entity(gov)));
			}
			else if(t1.isTerminal())
				t1 =new Tuple(new Entity(t1.getEntity().getName() + " " + gov.word(),t1.getEntity().getType()));
			else
				t1 = new Tuple(new Tuple(new Entity(gov)), new Relation("is", RelationType.temp), t1);
			
		}
		else
		{
			t1 = new Tuple(new Entity(gov, dep));
		}
		
		t = (Tuple)TupleUtil.merge(t, t1 , gov.word());
		
		logger.info(t.toString());
		return t;
	}

}
