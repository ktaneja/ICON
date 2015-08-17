package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.EntityType;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class AcompParser extends AbstractParser
{

	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new AcompParser();
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
		}
		else
		{
			t1 = new Tuple(new Entity(dep.word(), EntityType.Name));
		}
		
		
		if(t == null)
		{
			Entity e  = new Entity(dep.word(), EntityType.Object);
			Relation r = new Relation(gov.lemma(), RelationType.Symetric);
			
			t = new Tuple(r, new Tuple(e));
		}
		else if(t.getLeft() == null)
		{
			t = new Tuple(t.getRight(), t.getRelation(), t1);
		}
		else
		{
			logger.error("New Situation");
			System.err.println(100/0);
		}
		return t;
	}

}
