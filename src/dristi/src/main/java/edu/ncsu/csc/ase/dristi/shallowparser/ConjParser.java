package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class ConjParser extends AbstractParser 
{
	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new ConjParser();
		}
		return instance;
	}
	

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) {
		getPOSString(gov, dep);
		
		
		Tuple t1;
		/*
		 * Check for LeafNode
		 */
		String s = depGraph.getEdge(gov, dep).getRelation().getSpecific().toString();
		Relation r = new Relation(s, RelationType.temp);
		
		
		if(depGraph.getChildren(dep).size()>0)
		{
			t1 = parse(dep, depGraph, visited);
		}
		else
		{
			t1 = createTuple(dep);
		}	
		
		if(createTuple(gov).isPartial())
		{
			t1 = new Tuple(new Relation(gov), t1);
		}
		else
		{
			t1 = new Tuple(createTuple(gov), r, t1);
		}
		
		t = (Tuple)TupleUtil.merge(t, t1, gov.word());
		return t;
	}

	
}
