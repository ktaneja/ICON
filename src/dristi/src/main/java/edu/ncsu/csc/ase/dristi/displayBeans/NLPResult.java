package edu.ncsu.csc.ase.dristi.displayBeans;

public class NLPResult 
{
	public String sentnce="";
	
	public String posTree="";
	
	public String stanfordTypedDependecy="";
	
	public String tuple="";
	
	

	/**
	 * @return the sentence
	 */
	public String getSentnce() {
		return sentnce;
	}

	/**
	 * @param sentnce the sentence to set
	 */
	public void setSentnce(String sentnce) {
		this.sentnce = sentnce;
	}

	/**
	 * @return the posTree
	 */
	public String getPosTree() {
		return posTree;
	}

	/**
	 * @param posTree the posTree to set
	 */
	public void setPosTree(String posTree) {
		this.posTree = posTree;
	}

	/**
	 * @return the stanfordTypedDependecy
	 */
	public String getStanfordTypedDependecy() {
		return stanfordTypedDependecy;
	}

	/**
	 * @param stanfordTypedDependecy the stanfordTypedDependecy to set
	 */
	public void setStanfordTypedDependecy(String stanfordTypedDependecy) {
		this.stanfordTypedDependecy = stanfordTypedDependecy;
	}

	/**
	 * @return the tuple
	 */
	public String getTuple() {
		return tuple;
	}

	/**
	 * @param tuple the tuple to set
	 */
	public void setTuple(String tuple) {
		this.tuple = tuple;
	}
	
	
}
