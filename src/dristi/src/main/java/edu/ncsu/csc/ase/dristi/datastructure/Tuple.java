package edu.ncsu.csc.ase.dristi.datastructure;

@SuppressWarnings("serial")
public class Tuple  extends ITuple
{
	public Tuple(ITuple left, Relation rel, ITuple right)
	{
		this.left = left;
		this.relation = rel;
		this.right = right;
		this.terminal = false;
		this.entity = null;
	}
	
	public Tuple(Relation rel, ITuple right)
	{
		this.left = null;
		this.relation = rel;
		this.right = right;
		this.terminal = false;
		this.entity = null;
	}
	
	public Tuple(Entity entity)
	{
		this.left = null;
		this.relation = null;
		this.right = null;
		this.terminal = true;
		this.entity = entity;
	}
	
	public Tuple(Relation r)
	{
		this.left = null;
		this.relation = r;
		this.right = null;
		this.terminal = false;
		this.entity = null;
	}
	
	public boolean isSubset(ITuple t)
	{
		ITuple t1 = null;
		if(!t.isTerminal())
			t1 = searchRelation(t.getRelation().getName());
		else
			t1 = searchTerminal(t.getEntity().getName());
		if(t1 != null)
			return t1.same(t);
		else
			return false;
	}
	
	public ITuple searchTerminal(String word) {
		if(terminal)
		{
			if(this.entity.getName().trim().contains(word))
			{
				return this;
			}
			else
				return null;
		}
		else
		{
			ITuple retVal = null;
			
			if(left!=null)
				retVal = left.searchTerminal(word);
			
			if((retVal==null) &&(right!=null))
				retVal = right.searchTerminal(word);
			
			return retVal;
		}
	}
	
	public ITuple searchRelation(String word) {
		if(terminal)
		{
			return null;
		}
		else
		{
			ITuple retVal = null;
			if(this.relation.getName().trim().contains(word))
				return this;
			if(left!=null)
				retVal = left.searchRelation(word);
			
			if((retVal==null) &&(right!=null))
				retVal = right.searchRelation(word);
			
			return retVal;
		}
	}
	
	public ITuple complete(ITuple lhs, ITuple rhs) 
	{
		if(lhs == null)
			throw new NullPointerException("Cannot have null lhs ->"+ lhs);
		if(isPartial())
		{
			this.left = lhs;
			this.right = rhs;
			this.terminal = false;
			return this;
		}
		else
			throw new NullPointerException("No place for new tuples in existing tuples \n[\n"+ this + "\n]" );
		
	}

	public ITuple findParent(ITuple tuple) 
	{
		ITuple retVal = null;
		if(tuple == null)
		{
			retVal = this;
		}
		else if (this.same(tuple))
		{
			retVal = this;
		}
		else
		{
			if(this.left != null)
			{
				
				if(this.left.same(tuple))
					retVal = this;
				else
					retVal = this.left.findParent(tuple);
			}
			if ((retVal == null) && (this.right != null))
			{
				if(this.right.same(tuple))
					retVal = this;
				else
					retVal = this.right.findParent(tuple);
			}
				
		}
		return retVal;
	}
	
	/**
	 * Returns the sibling of this tuple. If none exists returns null
	 */
	public ITuple findSibling(ITuple tuple) 
	{
		ITuple retVal = null;
		if(tuple == null)
		{
			retVal = this;
		}
		else if (this.same(tuple))
		{
			//Trying to find sibling not the parent
			retVal = null;
		}
		else
		{
			if(this.left != null)
			{
				
				if(this.left.same(tuple))
					retVal = this.right;
				else
					retVal = this.left.findSibling(tuple);
			}
			if ((retVal == null) && (this.right != null))
			{
				if(this.right.same(tuple))
					retVal = this.left;
				else
					retVal = this.right.findSibling(tuple);
			}
				
		}
		return retVal;
	}
	
	/**
	 * returns true if the tuple is a partial tuple else returns false
	 */
	public boolean isPartial() {
		if(this.left == null && this.right == null && !this.terminal)
			return true;
		return false;
	}

	public boolean replace(String word,Relation r, ITuple t1) 
	{
		if(terminal)
		{
			if(this.entity.getName().trim().contains(word))
			{
				Tuple tn = new Tuple(new Entity(this.entity.getName(),this.entity.getType()));
				this.relation = r;
				this.left = t1;
				this.right = tn;
				this.entity = null;
				this.terminal = false;
				return true;
			}
			else
				return false;
		}
		else
		{
			boolean retVal = false;
			if(this.relation.getName().trim().contains(word))
			{
				Tuple tn = new Tuple(this.getLeft(),this.getRelation(),this.getRight());
				this.relation = r;
				this.left = tn;
				this.right = t1;
				this.entity = null;
				this.terminal = false;
				return true;
			}
				
			if(left!=null)
				retVal = retVal || left.replace(word,r, t1);
			else if((!retVal)&&(right!=null))
				retVal = retVal || right.replace(word,r, t1);
			return retVal;
		}
		
	}
	
	

	@Override
	public boolean same(ITuple t) 
	{
		boolean retVal = true;
		if (t == null)
			retVal = false;
		else
		{
			if(this.isTerminal() != t.isTerminal())
				retVal = false;
			
			if(this.isPartial() != t.isPartial())
				retVal = false;
			if(this.entity == null)
			{
				if(t.getEntity() != null)
					retVal = false;
			}
			else if(t.getEntity() == null)
			{
				retVal = false;
			}
			else if(!this.entity.getName().trim().equalsIgnoreCase(t.getEntity().getName().trim()))
				retVal = false;
			
			if(this.relation == null)
			{
				if(t.getRelation() != null)
					retVal = false;
			}
			else if(t.getRelation() == null)
			{
				retVal = false;
			}
			else if(!this.relation.getName().trim().equalsIgnoreCase(t.getRelation().getName().trim()))
				retVal = false;
			
			if(this.left == null)
			{
				if(t.getLeft() !=null)
					retVal = false;
			}
			else if(!this.left.same(t.getLeft()))
				retVal = false;
			
			if(this.right == null)
			{
				if(t.getRight() !=null)
					retVal = false;
			}
			else if(!this.right.same(t.getRight()))
				retVal = false;
		}
		return retVal;
	}

}
