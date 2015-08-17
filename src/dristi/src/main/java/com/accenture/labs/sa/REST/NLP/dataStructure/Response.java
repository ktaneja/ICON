package com.accenture.labs.sa.REST.NLP.dataStructure;

import java.util.List;

@SuppressWarnings("serial")
public class Response extends Message 
{
	private String specialErrors;
	
	private List<Parameter> specialErrorsList;

	/**
	 * @return the specialErrors
	 */
	public String getSpecialErrors() {
		return specialErrors;
	}

	/**
	 * @param specialErrors the specialErrors to set
	 */
	public void setSpecialErrors(String specialErrors) {
		this.specialErrors = specialErrors;
	}

	/**
	 * @return the specialErrorsList
	 */
	public List<Parameter> getSpecialErrorsList() {
		return specialErrorsList;
	}

	/**
	 * @param specialErrorsList the specialErrorsList to set
	 */
	public void setSpecialErrorsList(List<Parameter> specialErrorsList) {
		this.specialErrorsList = specialErrorsList;
	}
}
