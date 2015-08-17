package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.EntityType;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class PrepcParser extends AbstractParser 
{
	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new PrepcParser();
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
		if(depGraph.getChildren(dep).size()>0)
		{
			t1 = parse(dep, depGraph, visited);
		}
		else
		{
			Entity e = new Entity(dep.word(), EntityType.Notion);
			t1 = new Tuple(e);
		}
		String s = depGraph.getEdge(gov, dep).getRelation().getSpecific();
		Relation r = new Relation(s, RelationType.One2One);
		
		if(t== null)
		{
			Entity e1 = new Entity(gov.word(), EntityType.Object);
			Tuple t2 = new Tuple(e1);
			t = new Tuple(t1,r,t2);
		}
		else
		{
			t = new Tuple(t1,r,t);
		}
		logger.info(t.toString());
		return t;
	}

	
}
