package edu.ncsu.csc.ase.dristi.datastructure;

import java.io.Serializable;

import edu.ncsu.csc.ase.dristi.datastructure.type.RelationType;
import edu.stanford.nlp.ling.IndexedWord;

/**
 * Abstract Class Defining Relation
 * @author rpandit
 *
 */
@SuppressWarnings("serial")
public class Relation implements Serializable{
	
	private String name;
	
	private RelationType type;
	
	public String getName() {
		return name;
	}

	public RelationType getType() {
		return type;
	}

	public Relation(String name, RelationType type)
	{
		this.name = name.trim();
		this.type = type;
	}
	
	public Relation(IndexedWord... wrd)
	{
		this.name = "";
		for(IndexedWord w : wrd)
		{
			this.name = this.name + " " + w.word();
		}
		
		this.name = this.name.trim();
		this.type = RelationType.temp;
	}

}
