package edu.ncsu.csc.ase.dristi.datastructure;

import java.io.Serializable;

import edu.ncsu.csc.ase.dristi.datastructure.type.EntityType;
import edu.stanford.nlp.ling.IndexedWord;

@SuppressWarnings("serial")
public class Entity implements Serializable{
	
	private String name;
	
	private EntityType type;

	public String getName() {
		return name;
	}

	public EntityType getType() {
		return type;
	}
	
	public Entity(String name, EntityType type)
	{
		this.name = name.trim();
		this.type = type;
	}

	public Entity(IndexedWord... wrd) {
		this.name = "";
		
		for(IndexedWord w : wrd)
		{
			this.name = this.name + " " + w.word();
		}
		
		this.name = this.name.trim();
		this.type = EntityType.Unknown;
	}

}
