package edu.ncsu.csc.ase.dristi.datastructure.ootype;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DMethod {
	
	private String methodComments;
	
	private String returnComments;
	
	private String methodName;
	
	private List<String> exceptionComments;
	
	private List<String> paramComments;
	
	//There is reason for this no modifier for clazz field
	//TODO Document the reason
	DType clazz;
		
	private DType returnType;
	
	private List<DType> paramList;
	
	private List<DType> exceptionType;
	
	private Object javaMethod;
	
	private boolean java;
	
	private int modifier;
	
	public DMethod(Method javaMethod) {
		this.java = true;
		this.javaMethod = javaMethod;
		this.methodName = javaMethod.getName();
		//this.clazz = DTypeFactory.getInstance().getDType(javaMethod.getDeclaringClass());
		this.returnType = DTypeFactory.getInstance().getDType(javaMethod.getReturnType().getCanonicalName());
		this.paramList = new ArrayList<>();
		this.paramComments = new ArrayList<>();
		Class<?>[] prmList = javaMethod.getParameterTypes();
		for(Class<?> paramType:prmList)
		{
			this.paramList.add(DTypeFactory.getInstance().getDType(paramType.getCanonicalName()));
			this.paramComments.add("");
		}
		this.exceptionType = new ArrayList<>();
		this.exceptionComments = new ArrayList<>();
		for(Class<?> exceptionType:javaMethod.getExceptionTypes())
		{
			this.exceptionType.add(DTypeFactory.getInstance().getDType(exceptionType.getCanonicalName()));
			this.exceptionComments.add("");
		}
		this.modifier = javaMethod.getModifiers();
	}

	
	public DMethod(Constructor<?> javaMethod, DType dType) {
		this.java = true;
		this.javaMethod = javaMethod;
		this.methodName = javaMethod.getName().substring(javaMethod.getName().lastIndexOf(".")+1);
		//this.clazz = DTypeFactory.getInstance().getDType(javaMethod.getDeclaringClass());
		this.returnType = dType;
		this.paramList = new ArrayList<>();
		this.paramComments = new ArrayList<>();
		Class<?>[] prmList = javaMethod.getParameterTypes();
		for(Class<?> paramType:prmList)
		{
			this.paramList.add(DTypeFactory.getInstance().getDType(paramType.getCanonicalName()));
			this.paramComments.add("");
		}
		this.exceptionType = new ArrayList<>();
		this.exceptionComments = new ArrayList<>();
		for(Class<?> exceptionType:javaMethod.getExceptionTypes())
		{
			this.exceptionType.add(DTypeFactory.getInstance().getDType(exceptionType.getCanonicalName()));
			this.exceptionComments.add("");
		}
		this.modifier = javaMethod.getModifiers();
		this.clazz = dType;
	}


	public void setExceptionComment(DType exceptionType, String comment)
	{
		this.exceptionComments.add(this.exceptionType.indexOf(exceptionType), comment);
	}
	
	public String getExceptionComment(int i)
	{
		String returnVal=this.exceptionComments.get(i);
		returnVal =  returnVal==null?"":returnVal;
		return returnVal;
	}
	
	public void setParamComment(int paramNo, String comment)
	{
		this.paramComments.add(paramNo, comment);
	}
	
	public String getParameterComment(int i)
	{
		String returnVal=this.paramComments.get(i);
		returnVal =  returnVal==null?"":returnVal;
		return returnVal;
	}
	
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}



	/**
	 * @return the methodComments
	 */
	public String getMethodComments() {
		return methodComments;
	}

	/**
	 * @param methodComments the methodComments to set
	 */
	public void setMethodComments(String methodComments) {
		this.methodComments = methodComments;
	}

	/**
	 * @return the returnComments
	 */
	public String getReturnComments() {
		return returnComments;
	}

	/**
	 * @param returnComments the returnComments to set
	 */
	public void setReturnComments(String returnComments) {
		this.returnComments = returnComments;
	}

	/**
	 * @return the clazz
	 */
	public DType getClazz() {
		return clazz;
	}

	/**
	 * @return the returnType
	 */
	public DType getReturnType() {
		return returnType;
	}

	/**
	 * @return the paramList
	 */
	public List<DType> getParamList() {
		return paramList;
	}

	/**
	 * @return the exceptionType
	 */
	public List<DType> getExceptionType() {
		return exceptionType;
	}

	/**
	 * @return the javaMethod
	 * @throws NoSuchFieldException 
	 */
	public Object getJavaMethod() throws NoSuchFieldException {
		if(!java)
			throw new NoSuchFieldException(this.methodName + " is not a Java Method");
		return javaMethod;
	}

	/**
	 * @return the java
	 */
	public boolean isJava() {
		return java;
	}


	/**
	 * @return the modifier
	 */
	public int getModifiers() {
		return modifier;
	}
	
	@Override
	public String toString() {
		if(java)
			return this.javaMethod.toString();
		// TODO generate toString for non-Java Method
		//private byte[] java.io.BufferedInputStream.getBufIfOpen(java.io.InputStream,int,byte[]) throws java.io.IOException
		return "";
	}
	
	/**
	 * Equivalent to {@code toString} method but the output is formatted to be displayed on console
	 * @return formatted string representation of method
	 */
	public String prettyPrint() {
		StringBuffer buff = new StringBuffer();
		buff.append("Method Comments:\n");
		buff.append(this.methodComments);
		buff.append("\nParameter Comments\n");
		for(int i=0; i<paramList.size();i++)
		{
			buff.append(paramList.get(i).getName());
			buff.append(" : ");
			buff.append(paramComments.get(i));
			buff.append("\n");
		}
		buff.append("Returns\n");
		buff.append(returnComments);
		buff.append("\n Exception Comments\n");
		for(int i=0; i<exceptionType.size();i++)
		{
			
			buff.append(exceptionType.get(i).getName());
			buff.append(" : ");
			buff.append(exceptionComments.get(i));
			buff.append("\n");
		}
		
		buff.append(returnType.getName());
		buff.append(" ");
		buff.append(methodName);
		buff.append("(");
		for(int i=0; i<paramList.size();i++)
		{
			buff.append(paramList.get(i).getName());
			buff.append(", ");
		}
		buff.append(")");
		
		return buff.toString();
	}
	
}
