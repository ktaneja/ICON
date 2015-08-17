package com.accenture.labs.sa.REST.NLP.dataStructure;

import java.util.List;


@SuppressWarnings("serial")
public class Request extends Message 
{
	private String syntax;
	
	private String parameters;
	
	private List<Parameter> paramList;
	
	/**
	 * @return the syntax
	 */
	public String getSyntax() {
		return syntax;
	}

	/**
	 * @param syntax the syntax to set
	 */
	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}
	
	/**
	 * @return the paramList
	 */
	public List<Parameter> getParamList() {
		return paramList;
	}

	/**
	 * @param paramList the paramList to set
	 */
	public void setParamList(List<Parameter> paramList) {
		this.paramList = paramList;
	}

	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	

}
