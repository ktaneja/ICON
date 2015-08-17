package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class CcParser extends AbstractParser 
{
	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new CcompParser();
		}
		return instance;
	}
	

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) {
		getPOSString(gov, dep);
		
		
		/*
		 * TODO add logic to create question or statement from complm 
		 */
		Tuple t1;
		/*
		 * Check for LeafNode
		 */
		if(depGraph.getChildren(dep).size()>0)
			t1 = parse(dep, depGraph, visited);
		else
			t1 = new Tuple(new Entity(dep));
			
		if(gov.tag().toUpperCase().startsWith("VB"))
		{
			t1 = new Tuple(new Relation(gov),t1);
		}
		else
		{
			IndexedWord complm =  depGraph.getChildWithReln(dep, GrammaticalRelation.valueOf("complm"));
			if(complm != null && complm.word()!="")
			{
				t1 = new Tuple(new Tuple(new Entity(gov)),new Relation(complm),t1);
			}
			else
			{
				t1 = new Tuple(new Tuple(new Entity(gov)),new Relation("that",RelationType.temp),t1);
			}
		}
		
		t = (Tuple)TupleUtil.merge(t, t1 , gov.word());
		
		return t;
	}

	
}
