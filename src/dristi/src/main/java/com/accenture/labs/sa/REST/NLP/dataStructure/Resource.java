package com.accenture.labs.sa.REST.NLP.dataStructure;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Resource implements Serializable 
{
	private String name;
	
	private List<Operation> operations;

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
	 * @return the operations
	 */
	public List<Operation> getOperations() {
		return operations;
	}

	/**
	 * @param operations the operations to set
	 */
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
	
	/**
	 * @return the operations
	 */
	public boolean isValidOperation(Operation operation) 
	{
		return operations.contains(operation);
	}

	/**
	 * @param operations the operations to set
	 */
	public void addOperation(Operation operation) 
	{
		this.operations.add(operation);
	}
}
