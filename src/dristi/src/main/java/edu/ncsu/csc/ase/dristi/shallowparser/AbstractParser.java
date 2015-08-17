package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;

/**
 * 
 * @author rpandit
 *
 */
public abstract class AbstractParser 
{
	protected final Logger logger = MyLoggerFactory.getLogger(AbstractParser.class);
	
	protected String POSTag;
	
	protected HashMap<String, String> regex = new HashMap<String, String>();
	
	public abstract Tuple parse(IndexedWord gov, IndexedWord dep, SemanticGraph graph, Tuple t, Set<IndexedWord> visited);
	
	public Tuple parse(IndexedWord gov, SemanticGraph dependencies, Set<IndexedWord> visited)
	{
		Tuple t = createTuple(gov);
		
		visited.add(gov);
		
		if(dependencies.getChildList(gov).size()>0)
		{
			
			Collection<IndexedWord> children = dependencies.getChildren(gov);
			
			/*
			 * Code to see if the node is also a child of children 
			 */
			List<IndexedWord> modList = new ArrayList<IndexedWord>();
			
			for(IndexedWord wrd : children)
			{
				for(IndexedWord wrd1 : children)
				{
					if(!wrd.equals(wrd1))
					{
						if(dependencies.getCommonAncestor(wrd, wrd1).equals(wrd))
						{	
							modList.add(wrd1);
						}
					}
				}
			}
			
			//modList = new ArrayList<IndexedWord>();
			
			for(IndexedWord dep : children)
			{
				if(!visited.contains(dep) && !modList.contains(dep))
				{
					SemanticGraphEdge e = dependencies.getEdge(gov, dep);
					try
					{
						AbstractParser parser = ParserFactory.getInstance().getParser(e.getRelation().getShortName());
						t = parser.parse(gov, dep, dependencies,t, visited);
						logger.info("Logging" + gov.word() + " : " + e.getRelation().getShortName() + " : " + dep.word());
					}
					catch (Exception ex) {
						logger.error("Logging" + gov.word() + " : " + e.getRelation().getShortName() + " : " + dep.word());
						ex.printStackTrace();
					}
				}
			}
		}
		return t;
	}
	
	public Tuple createTuple(IndexedWord gov) 
	{
		Tuple t;
		if(gov.tag().toUpperCase().trim().startsWith("VB"))
			t = new Tuple(new Relation(gov));
		else if(gov.tag().toUpperCase().trim().startsWith("IN"))
			t = new Tuple(new Relation(gov));
		else
			t = new Tuple(new Entity(gov));
		return t;
	}
	
	public Tuple createTuple(IndexedWord gov, IndexedWord dep) 
	{
		Tuple tg = createTuple(gov);
		Tuple td = createTuple(dep);
		
		if(tg.isTerminal() && td.isTerminal())
		{
			return new Tuple(new Entity(dep, gov));
		}
		else if(tg.isPartial() && td.isPartial())
		{
			return new Tuple(new Relation(dep, gov));
		}
		else if(tg.isPartial())
		{
			return new Tuple(new Relation(gov), new Tuple(new Entity(dep)));
		}
		else
			return new Tuple(new Relation(dep), new Tuple(new Entity(gov))); 
		
	
	}
	
	public void getPOSString(IndexedWord gov, IndexedWord dep)
	{
		POSTag = gov.tag().toUpperCase() + ":" + dep.tag().toUpperCase();
		
	}

	
}
