package edu.ncsu.csc.ase.dristi.datastructure.ootype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DTypeFactory 
{
	private Map<String, DType> typeMap;
	
	private static DTypeFactory instance;
	
	private DTypeFactory() {
		typeMap = new HashMap<>();
	}
	
	public synchronized static DTypeFactory getInstance()
	{
		if(instance == null)
			instance = new DTypeFactory();
		return instance;
	}
	
	
	void putDType(String clazz, DType type)
	{
		//all puts on typeMap should go through this method. 
		if(!typeMap.containsKey(clazz))
			typeMap.put(clazz, type);
	}
	public DType getDType(String clazz)
	{
		if(!typeMap.containsKey(clazz))
		{
			DType type = new DType(clazz, 0, false, new ArrayList<DMethod>(), new ArrayList<DMethod>());
			try
			{
				type = new DType(Class.forName(clazz));
			}
			catch(ClassNotFoundException e)
			{
				System.err.println("Class Def not found for "+ clazz + "  Creating dummy def");
			}
			catch(NoClassDefFoundError e)
			{
				System.err.println("Class Def not found for "+ clazz + "  Creating dummy def");
			}
			catch (ExceptionInInitializerError e) {
				System.err.println("Class Def not found for "+ clazz + "  Creating dummy def");
			}
			putDType(clazz, type);
		}
		return typeMap.get(clazz);
	}
	
	public DMethod getDMethod(String clazz, String mtd)
	{
		DType type = getDType(clazz);
		
		return type.getDMethod(mtd);
	}
}
