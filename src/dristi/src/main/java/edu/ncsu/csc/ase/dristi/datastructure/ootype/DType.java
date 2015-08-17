package edu.ncsu.csc.ase.dristi.datastructure.ootype;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DType {
	
	private boolean javaType;
	
	private Class<?> javaClass;
	
	private String name;
	
	private int modifier;
	
	private boolean interfaze;
	
	private List<DMethod> methodList;
	
	private List<DMethod> constructorList;
	
	public DType(String name, int modifer, boolean interfaze, List<DMethod> methodList, List<DMethod> constructorList) {
		this.javaType = false;
		this.name = name;
		this.interfaze = interfaze;
		this.modifier = modifer;
		this.methodList = methodList;
		this.constructorList = constructorList;
	}
	
	public DType(Class<?> javaClass) throws ClassNotFoundException
	{
		
		this.javaClass = javaClass;
		javaType = true;
		this.name = javaClass.getCanonicalName();
		this.interfaze = javaClass.isInterface();
		this.modifier = javaClass.getModifiers();
		this.methodList = new ArrayList<>();
		this.constructorList = new ArrayList<>();
		
		//This is done to avoid race condition in case type associated with a method
		//references this type
		DTypeFactory.getInstance().putDType(name, this);
		for(Method mtd: javaClass.getDeclaredMethods())
		{
			// don't handle private or no modifier
			if ((mtd.getModifiers() == 0)
					|| (Modifier.isPrivate(mtd.getModifiers()))) {
				continue;
			}
					
			DMethod dmtd = new DMethod(mtd);
			dmtd.clazz = this;
			methodList.add(dmtd);
		}
			
		for(Constructor<?> constructor: javaClass.getConstructors())
		{
			// don't handle private or no modifier
			if ((constructor.getModifiers() == 0)
					|| (Modifier.isPrivate(constructor.getModifiers()))) {
				continue;
			}
			DMethod dmtd = new DMethod(constructor, this);
			constructorList.add(dmtd);
		}
	}

	public DMethod getDMethod(String mtd)
	{
		DMethod retMethod= null;
		if(mtd!=null)
		{
			for(DMethod method: this.methodList)
			{
				if(method.toString().equals(mtd))
				{
					retMethod = method;
					break;
				}
			}
		}
		return retMethod;
	}
	
	/**
	 * @return the javaType
	 */
	public boolean isJavaType() {
		return javaType;
	}

	/**
	 * @return the javaClass
	 */
	public Class<?> getJavaClass() throws NoSuchFieldException{
		if(!javaType)
			throw new NoSuchFieldException(this.name + " is not a Java Type");
		return javaClass;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the modifier
	 */
	public int getModifiers() {
		return modifier;
	}

	/**
	 * @return the interface
	 */
	public boolean isInterface() {
		return interfaze;
	}

	/**
	 * @return the methodList
	 */
	public List<DMethod> getMethodList() {
		return methodList;
	}

	/**
	 * @return the constructorList
	 */
	public List<DMethod> getConstructorList() {
		return constructorList;
	}

}
