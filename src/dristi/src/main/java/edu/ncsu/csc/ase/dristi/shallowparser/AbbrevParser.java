package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.EntityType;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class AbbrevParser extends AbstractParser
{

	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new AbbrevParser();
		}
		return instance;
	}

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph graph, Tuple t, Set<IndexedWord> visited) 
	{
		
		Entity e = new Entity(dep.word(), EntityType.Name);
		Tuple t1 = new Tuple(e);
		Relation r = new Relation("is", RelationType.One2One);
		
		if(t== null)
		{
			Entity e1 = new Entity(gov.word(), EntityType.Name);
			Tuple t2 = new Tuple(e1);
			t = new Tuple(t1,r,t2);
		}
		else
		{
			t = new Tuple(t1,r,t);
		}
		return t;
	}

}
