package edu.ncsu.csc.ase.dristi.datastructure;

import java.io.Serializable;

public abstract class ITuple implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5822951032698010781L;

	protected ITuple left;
	
	protected ITuple right;
	
	protected Relation relation;
	
	protected Entity entity;
	
	protected boolean terminal;
	
	
	public ITuple getLeft() {
		return left;
	}

	public void setLeft(ITuple left) {
		this.left = left;
	}

	public ITuple getRight() {
		return right;
	}

	public void setRight(ITuple right) {
		this.right = right;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public String toString() 
	{
		if(terminal)
			return entity.getName();
		else
		{
			StringBuilder str = new StringBuilder();
			str.append(relation.getName());
			str.append("\n");
			if(this.left != null)
			{
				String leftStr = left.toString();
				leftStr = leftStr.replaceAll("\\n", "\n\t");
				str.append("\t" + leftStr);
				str.append("\n");
			}
			
			if(this.right != null)
			{
				String rightStr = right.toString();
				rightStr = rightStr.replaceAll("\\n", "\n\t");
				str.append("\t"+rightStr);
			}
			
			return str.toString();
		}
		
	}
		
	public abstract boolean isPartial();

	public abstract ITuple searchTerminal(String word);
	
	public abstract ITuple searchRelation(String word);
	
	public abstract boolean same(ITuple t1);
	
	public abstract boolean isSubset(ITuple t);
	
	public abstract boolean replace(String word,Relation r, ITuple t1);
	
	public abstract ITuple complete(ITuple lhs, ITuple rhs);
	
	public abstract ITuple findParent(ITuple tuple);
	
	public abstract ITuple findSibling(ITuple tuple);
	
}
