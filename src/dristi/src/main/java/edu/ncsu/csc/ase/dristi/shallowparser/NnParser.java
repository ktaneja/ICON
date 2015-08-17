package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.EntityType;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class NnParser extends AbstractParser
{

	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		instance = new NnParser();
		return instance;
	}

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) {
		
		if(t == null)
		{
			Entity e  = new Entity(dep.word() + " " + gov.word(), EntityType.Object);
			t = new Tuple(e);
		}
		else
		{
			ITuple t1 = new Tuple(new Entity(dep, gov));
			t = (Tuple)TupleUtil.merge(t, t1, gov.word());
		}
		logger.info(t.toString());
		return t;
	}
}
