package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class AdvclParser extends AbstractParser
{

	private static AbstractParser instance;
	
	public static synchronized AbstractParser getInstance() {
		if(instance == null)
		{
			instance = new AdvclParser();
		}
		return instance;
	}

	@Override
	public Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph depGraph, Tuple t, Set<IndexedWord> visited) 
	{
		
		Tuple t1;
		
		Tuple t2 = null;
		/*
		 * Check for LeafNode
		 */
		
		IndexedWord mark =  depGraph.getChildWithReln(dep, GrammaticalRelation.valueOf("mark"));
		if(mark != null && mark.word()!="")
		{
			t2 = parse(mark, depGraph, visited);
			//TODO add logic to make mark as root!
		}
		
		
		if(depGraph.getChildren(dep).size()>0)
		{
			t1 = parse(dep, depGraph, visited);
		}
		else
		{
			//TODO Check this
			t1 = new Tuple(new Entity(dep));
			logger.error("Potentially wrong formation!-->" + t1);
		}
		
		
		if(t2 != null)
		{
			t = (Tuple)t2.complete(t,t1);
		}
		else
		{
			t = (Tuple)TupleUtil.merge(t, t1 , gov.word());
		}
		
		logger.info(t.toString());
		return t;
	}

}
