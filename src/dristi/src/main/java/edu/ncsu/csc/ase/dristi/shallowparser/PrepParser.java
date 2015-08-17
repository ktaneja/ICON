package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class PrepParser extends AbstractParser 
{
	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new PrepParser();
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
		GrammaticalRelation gr = depGraph.getEdge(gov, dep).getRelation();
		String s = null;
		try
		{
			s = gr.getSpecific().toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(s==null)
		{
			t1 = createTuple(gov, dep);
			if(depGraph.getChildren(dep).size()>0)
			{
				Tuple t2 = parse(dep, depGraph, visited);
				t1 = (Tuple)TupleUtil.merge(t1, t2 , gov.word());
			}
		}
		else
		{
			
			Relation r = new Relation(s, RelationType.Symetric);
			//if(createTuple(gov).isPartial())
			//	t1 = new Tuple(new Relation(gov), new Tuple(r, createTuple(dep)));
			//else	
			t1 = new Tuple(createTuple(gov),r, createTuple(dep));
			if(depGraph.getChildren(dep).size()>0)
			{
				Tuple t2 = parse(dep, depGraph, visited);
				t1 = new Tuple(createTuple(gov),r, t2);
			}
		}
		
		
			
		t = (Tuple)TupleUtil.merge(t, t1 , gov.word());
		
		return t;
	}
	
	
	
}
