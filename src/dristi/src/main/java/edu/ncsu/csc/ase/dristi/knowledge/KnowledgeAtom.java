package edu.ncsu.csc.ase.dristi.knowledge;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.ase.dristi.util.StringUtil;

/**
 * Object for holding the Knowledge Graph
 * @author rahul_pandita
 *
 */
public class KnowledgeAtom {
	
	/**
	 * Name of the Graph
	 */
	private String name;

	/**
	 * List of properties associated with Graph
	 */
	private List<String> properties;

	/**
	 * List of applicable actions
	 */
	private List<String> actions;

	/**
	 * List of dependencies
	 */
	private ArrayList<KnowledgeAtom> depends;

	/**
	 * List of synonyms
	 */
	private ArrayList<String> synonyms;

	public KnowledgeAtom(String name) {
		this.name = name;
		this.properties = new ArrayList<String>();
		this.actions = new ArrayList<String>();
		this.depends = new ArrayList<KnowledgeAtom>();
		this.synonyms = new ArrayList<String>();
		this.synonyms.add(name);
	}

	public String getName() {
		return name;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(String... prop) {
		for (String p : prop) {
			if (!this.properties.contains(p))
				this.properties.add(p);
		}
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(String... actionList) {
		for (String a : actionList) {
			if (!this.actions.contains(a))
				this.actions.add(a);
		}
	}

	public boolean isAlowedAction(String act) {
		// TODO add wordnetsim
		if (this.properties.contains(act))
			return true;
		return false;
	}

	public boolean isAlowedProperty(String prop) {
		// TODO add wordnetsim
		if (this.properties.contains(prop))
			return true;
		return false;
	}

	public ArrayList<KnowledgeAtom> getDepandants() {
		return depends;
	}

	public void setDependants(KnowledgeAtom... dependentList) {
		for (KnowledgeAtom d : dependentList) {
			if (!this.depends.contains(d))
				this.depends.add(d);
		}
	}

	public boolean isDependent(KnowledgeAtom dep) {
		// TODO add wordnetsim
		if (this.depends.contains(dep))
			return true;
		return false;
	}

	public ArrayList<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String... actionList) {
		for (String a : actionList) 
		{
			if (!this.synonyms.contains(a))
			{
				this.synonyms.add(a);
			}
			//Hack for adding Camel Case Splits
			a = StringUtil.splitCamelCase(a);
			if (!this.synonyms.contains(a))
			{
				this.synonyms.add(a);
			}
		}
	}

}
