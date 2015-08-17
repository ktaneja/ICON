package edu.ncsu.csc.ase.dristi.util;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.datastructure.Entity;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.datastructure.Relation;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;
import edu.stanford.nlp.ling.IndexedWord;

/**
 * Class responsible for providing utilities such as merge for TUPLE
 * @author Rahul Pandita
 *
 */
public class TupleUtil 
{
	private final static Logger logger = MyLoggerFactory.getLogger(TupleUtil.class);
	/**
	 * 
	 * @param t_first
	 * @param t_second
	 * @param wrd
	 * @return
	 */
	public static ITuple merge(ITuple t_first, ITuple t_second, String wrd) {
		ITuple t1 = copy(t_first);
		ITuple t2 = copy(t_second);
		ITuple retVal;

		if (t1.isSubset(t2)) {
			retVal = t1;
		} else if (t2.isSubset(t1)) {
			retVal = t2;
		} else if (t2.isTerminal()) {
			retVal = mergeTerminal(t1, t2, wrd);
		} else if (t1.isTerminal()) {
			retVal = mergeTerminal(t2, t1, wrd);
		} else {
			retVal = mergeRel(t1, t2, wrd);
		}

		minimise(retVal);
		return retVal;
	}
	
	/**
	 * Cleanup function to remove duplicate nodes as a result of merging
	 * @param tuple that is to be minimized
	 * @return minimized tuple
	 */
	public static ITuple minimise(ITuple tuple) 
	{
		//TODO Not entirely convinced with what is going on here.
		if(!tuple.isTerminal())
		{
			
			// two partial relationships
			if((tuple.getLeft() != null)&&(tuple.getLeft().isPartial()))
			{
				tuple.setRelation(new Relation(tuple.getLeft().getRelation().getName() + " " + tuple.getRelation().getName(), tuple.getRelation().getType()));
				tuple.setLeft(null);
			}
			
			if((tuple.getRight() != null)&&(tuple.getRight().isPartial()))
			{
				tuple.setRelation(new Relation(tuple.getRight().getRelation().getName() + " " + tuple.getRelation().getName(), tuple.getRelation().getType()));
				tuple.setRight(null);
			}
			
			// Same relation
			if((tuple.getLeft() != null)&&(!tuple.getLeft().isTerminal())&&(tuple.getLeft().getLeft()==null))
			{
				if(tuple.getLeft().getRelation().getName().trim().equalsIgnoreCase(tuple.getRelation().getName().trim()))
				{
					tuple.setLeft(tuple.getLeft().getRight());
				}
			}
			
			
			if((tuple.getRight() != null)&&(!tuple.getRight().isTerminal())&&(tuple.getRight().getLeft()==null))
			{
				if(tuple.getRight().getRelation().getName().trim().equalsIgnoreCase(tuple.getRelation().getName().trim()))
				{
					tuple.setRight(tuple.getLeft().getRight());
				}
			}
		}
		
		return tuple;
	}
	
	/**
	 * This method merges two tuples where the second tuple is a terminal 
	 * @param tuple1 the first tuple to be merged
	 * @param tuple2 the second tuple to be merged must be terminal
	 * @param word the word on which merge is to be performed
	 * @return the merged tuple
	 */
	public static ITuple mergeTerminal(ITuple tuple1, ITuple tuple2, String word) {
		
		if(!tuple2.getEntity().getName().trim().contains(word))
			Except(tuple1, tuple2);
		
		// Locate appropriate Terminal in tuple1 to merge the terminal tuple2
		ITuple tNew = tuple1.searchTerminal(word);
		if (tNew != null) {
			// Create the new name
			String newEntity = tNew.getEntity().getName();
			newEntity = newEntity.replace(word, tuple2.getEntity().getName());
			tNew.setEntity(new Entity(newEntity, tNew.getEntity().getType()));
		}else if(tuple1.searchRelation(word) != null){
			//TODO 
			tNew = tuple1.searchRelation(word);
			logger.error("Figure out what happens here! \n" + tuple1.toString() +"\n" +tuple2.toString());
			//Except(tuple1, tuple2);
			//DO NOTHING RETURN tuple1
		}else
			Except(tuple1, tuple2);
		return tuple1;
	}
	
	
	public static ITuple mergeRel(ITuple t1, ITuple t2, String word) 
	{
		
		if(searchEntity(t1, word)!= null)
		{
			ITuple t = searchEntity(t1, word);
			if(searchEntity(t2, word)!= null)
			{
				ITuple tt = searchEntity(t2, word);
				String newEntity = t.getEntity().getName();
				newEntity = newEntity.replace(word, tt.getEntity().getName());
				tt.setEntity(new Entity(newEntity, tt.getEntity().getType()));
				
			}
		}
		
		//Fallback to The old way of merging tuples
		if(t1.getRelation().getName().trim().contains(word))
		{
			if(t1.isPartial()&&t2.isPartial())
			{
				/*
				 * Combining partial relations!
				 */
				t1.setRelation(new Relation(t1.getRelation().getName().replace(word, t2.getRelation().getName()).trim(), t1.getRelation().getType()));
			}
			else if(t2.isPartial())
			{
				/*
				 * Combining contents of partial relations!
				 */
				t1.setRelation(new Relation(t1.getRelation().getName().replace(word, t2.getRelation().getName()).trim(), t1.getRelation().getType()));
			}
			else if(t1.isPartial())
			{
				t1.setEntity(t2.getEntity());
				t1.setLeft(t2.getLeft());
				t1.setRelation(new Relation(t1.getRelation().getName().replace(word, t2.getRelation().getName()).trim(), t1.getRelation().getType()));
				t1.setRight(t2.getRight());
				t1.setTerminal(t2.isTerminal());
			}
			else if(t2.searchRelation(word).isPartial())
			{
				ITuple temp = t2.searchRelation(word);
				temp.setRelation(new Relation(t1.getRelation().getName().replace(word, temp.getRelation().getName()).trim(), t1.getRelation().getType()));
				temp.setLeft(t1.getLeft());
				temp.setRight(t1.getRight());
				temp.setTerminal(t1.isTerminal());
				temp.setEntity(t1.getEntity());
				t1 = t2;
			}
			else if (t1.getLeft() == null && t1.getRight() != null)
			{
				if(t2.getLeft() == null && t2.getRight() !=null)
				{
					t1.setRelation(new Relation(t1.getRelation().getName().replace(word, t2.getRelation().getName()).trim(), t1.getRelation().getType()));
					t1.setLeft(t1.getRight());
					t1.setRight(t2.getRight());
					t1.setTerminal(t2.isTerminal());
					t1.setEntity(t2.getEntity());
				}
				else
				{
					Except(t1,t2);
				}
			}
			else if(t2.getRelation().getName().trim().contains(word))
			{
				if(t1.getLeft() == null && t1.getRight().isTerminal())
				{
					ITuple t = new Tuple( new Relation(t1.getRight().toString() + " " + t1.getRelation().getName(),t1.getRelation().getType()));
					t = TupleUtil.merge(t, t2 , word);
					t1 = t;
				}
				else if(t1.getLeft() == null && t1.getRight().getLeft()==null)
				{
					ITuple t = new Tuple(t1.getRight().getRelation());
					t.setLeft(t2);
					t.setRight(t1.getRight().getRight());
					t1 = t;
				}
				else if(t2.getLeft() == null && t2.getRight().isTerminal())
				{
					ITuple t = new Tuple(new Relation(t2.getRight().toString() + " " +t2.getRelation().getName(), t2.getRelation().getType()));
					t = TupleUtil.merge(t, t1 , word);
					t1 = t;
				}
				else if(t2.getLeft() == null && t2.getRight().getRight()==null)
				{
					ITuple t = new Tuple(t2.getRight().getRelation());
					t.setLeft(t1);
					t.setRight(t2.getRight().getRight());
					t1 = t;
				}
				else 
				{
					t1 = new Tuple(t1, new Relation("implies", RelationType.temp), t2);
				}
			}
			else
			{
				Except(t1,t2);
			}
			return t1;
		}
		else if(t1.getRelation().getName().trim().equalsIgnoreCase("is"))
		{
			if(t2.getLeft() == null && t2.getRight().isTerminal() && t2.getRight().getEntity().getName().trim().contains(word))
			{
				if(t1.getLeft().isTerminal() && t1.getLeft().getEntity().getName().trim().contains(word))
				{
					/*
					 * Combining partial relations!
					 */
					t1.setRelation(t2.getRelation());
					t1.setLeft(new Tuple(new Entity(t1.getLeft().getEntity().getName().replace(word, t2.getRight().getEntity().getName()), t1.getLeft().getEntity().getType())));
					return t1;
				}
				else if(t1.getRight().isTerminal() && t1.getRight().getEntity().getName().trim().contains(word))
				{
					/*
					 * Combining partial relations!
					 */
					t1.setRelation(t2.getRelation());
					t1.setRight(new Tuple(new Entity(t1.getRight().getEntity().getName().replace(word, t2.getRight().getEntity().getName()), t1.getRight().getEntity().getType())));
					return t1;
				}
			}
		}
		
		if(t1.getLeft()!=null)
		{
			ITuple tup = TupleUtil.merge(t1.getLeft() ,t2 ,word);
			if(tup.same(t1.getLeft()) && (t1.getRight()!=null))
				t1.setRight(TupleUtil.merge(t1.getRight(),t2, word));
			else 
				t1.setLeft(tup);
			
			return t1;
		}
		if(t1.getRight()!=null)
		{
			t1.setRight(TupleUtil.merge(t1.getRight(),t2, word));
			return t1;
		}
		else
		{
			Except(t1, t2);
		}
		return t1;
	}
	
	
	/**
	 * If you reach this function that means I have been lazy in covering all
	 * the cases for merging! :D
	 * 
	 * @param tuple1
	 * @param tuple2
	 */
	public static void Except(ITuple tuple1, ITuple tuple2) {
		String message = "New Case encountered! \n No place for new tuples in existing tuples \n["
				+ tuple1 + "]\n[" + tuple2 + "]";
		logger.error(message);
		throw new NullPointerException(message);
	}
	
	/**
	 * Utility function to perform a deep copy of tuple.
	 * @param tuple : the tuple to be copied
	 * @return deep copy of tuple
	 */
	public static ITuple copy(ITuple tuple)
	{
		ITuple t1;
		if(tuple.isTerminal())
			t1 = new Tuple(new Entity(tuple.getEntity().getName(),tuple.getEntity().getType()));
		else if(tuple.isPartial())
		{
			t1 = new Tuple(new Relation(tuple.getRelation().getName(),tuple.getRelation().getType()));
		}
		else
		{
			Relation r = new Relation(tuple.getRelation().getName(),tuple.getRelation().getType());
			ITuple left = null;
			ITuple right =  null;
			if(tuple.getLeft() != null)
				left = TupleUtil.copy(tuple.getLeft());
			if(tuple.getRight() != null)
				right = TupleUtil.copy(tuple.getRight());
			t1 = new Tuple(left, r, right);
		}
		return t1;
	} 
	
	
	/**
	 * Search for entities in the tuple that contain word
	 * @param tuple
	 * @param word
	 * @return
	 */
	public static ITuple searchEntity(ITuple tuple, String word) {
		ITuple retValue = null;
		if (tuple != null) {
			if (tuple.isTerminal()) {
				if (tuple.getEntity().getName().trim().contains(word))
					retValue = tuple;
			} else {
				retValue = searchEntity(tuple.getLeft(), word);
				if (retValue == null)
					retValue = searchEntity(tuple.getRight(), word);
			}
		}
		return retValue;
	}
	
	/**
	 * Search for relations in the tuple that contain word
	 * 
	 * @param tuple
	 * @param word
	 * @return
	 */
	public static ITuple searchRelation(ITuple tuple, String word) {
		ITuple retValue = null;
		if ((tuple != null) && (!tuple.isTerminal())) {
			if (tuple.getRelation().getName().trim().contains(word))
				retValue = tuple;
			else {
				retValue = searchRelation(tuple.getLeft(), word);
				if (retValue == null)
					retValue = searchRelation(tuple.getRight(), word);
			}
		}
		return retValue;
	}

	public static ITuple reorder(ITuple tuple, List<IndexedWord> orderedList, Set<IndexedWord> visitedSet) {
		if ((tuple != null) && (!tuple.isTerminal())) 
		{
			tuple.setRight(reorder(tuple.getRight(), orderedList, visitedSet));
			tuple.setLeft(reorder(tuple.getLeft(), orderedList, visitedSet));
			//Only Child should be left Child.
			if((tuple.getLeft()==null)&&(tuple.getRight()!=null))
			{
				tuple.setLeft(tuple.getRight());
				tuple.setRight(null);
			}
			
			if(tuple.getRight()!=null)
			{
				String leftwrdList,rightwrdList;
				if(tuple.getLeft().isTerminal())
					leftwrdList = tuple.getLeft().getEntity().getName();
				else
					leftwrdList = tuple.getLeft().getRelation().getName();
				
				if(tuple.getRight().isTerminal())
					rightwrdList = tuple.getRight().getEntity().getName();
				else
					rightwrdList = tuple.getRight().getRelation().getName();
				
				String[] leftWrdArray = leftwrdList.split("\\s");
				Integer lftidx = Integer.MAX_VALUE;
				for(String wrd:leftWrdArray)
				{
					if(wrd.trim()!= "")
					for (IndexedWord word: orderedList)
					{
						if(!visitedSet.contains(word))
						{
							if(wrd.equalsIgnoreCase(word.originalText()))
							{	
								visitedSet.add(word);
								if(lftidx> word.beginPosition())
									lftidx = word.beginPosition();
							}
						}
					}
				}
				Integer rightidx = Integer.MAX_VALUE;
				String[] rightWrdArray = rightwrdList.split("\\s");
				for(String wrd:rightWrdArray)
				{
					if(wrd.trim()!= "")
					for (IndexedWord word: orderedList)
					{
						if(!visitedSet.contains(word))
						{
							if(wrd.equalsIgnoreCase(word.originalText()))
							{	
								visitedSet.add(word);
								if(rightidx> word.beginPosition())
									rightidx = word.beginPosition();
							}
						}
					}
				}
				
				if(lftidx>rightidx)
				{
					ITuple tupleTemp = tuple.getLeft();
					tuple.setLeft(tuple.getRight());
					tuple.setRight(tupleTemp);
				}
			}
		}
		return tuple;
	}
}
