package com.accenture.labs.sa.REST.NLP.dataStructure;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Header implements Serializable 
{
	private String name;
	
	private String description;
	
	private String type;
	
	private String Default;
	
	private String required;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the default
	 */
	public String getDefault() {
		return Default;
	}

	/**
	 * @param default1 the default to set
	 */
	public void setDefault(String default1) {
		Default = default1;
	}

	/**
	 * @return the required
	 */
	public String getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(String required) {
		this.required = required;
	}

}
