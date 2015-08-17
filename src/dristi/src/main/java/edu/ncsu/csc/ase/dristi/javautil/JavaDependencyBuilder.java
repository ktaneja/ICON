package edu.ncsu.csc.ase.dristi.javautil;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DMethod;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DType;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DTypeFactory;

public class JavaDependencyBuilder {
	
	private static List<String> visitedTypeList = new ArrayList<>();

	private static Map<DType, List<DMethod>> returnMap = new HashMap<DType, List<DMethod>>();

	private static DirectedMultigraph<DMethod, DefaultEdge> methodSeq = new DirectedMultigraph<DMethod, DefaultEdge>(DefaultEdge.class);

	private static SimpleDirectedGraph<DType, DefaultEdge> classInheritance = new SimpleDirectedGraph<DType, DefaultEdge>(DefaultEdge.class);
	
	private static SimpleDirectedGraph<DType, DefaultEdge> interfaceInheritance = new SimpleDirectedGraph<DType, DefaultEdge>(DefaultEdge.class);

	public static void main(String[] args) throws IOException {
		Src2JavaDocReader t = new Src2JavaDocReader();
		List<String> typeNameList = t.getTypeNamesFromDirectory(Config.JDK_SRC_PATH);
		
		for (String typeName : typeNameList)
			buildInheritanceGraph(typeName);
		
		//DType type = t.updateTypeComments("java.io.BufferedInputStream");
		//analyse(type);
		for (String typeName : typeNameList) {

			DType clazz = DTypeFactory.getInstance().getDType(typeName);
			if ((clazz.getModifiers() == 0)
					|| (Modifier.isPrivate(clazz.getModifiers()))
					|| (Modifier.isAbstract(clazz.getModifiers()))
					|| (Modifier.isInterface(clazz.getModifiers()))) {
				// do nothing
				continue;
			}
			// Build Dependencies on type
			buildDepnedecyGraph(clazz);
		}
		
		printDependencies(DTypeFactory.getInstance().getDType("java.io.BufferedInputStream"));
		//System.out.println(visitedTypeList.size());
		
	}
	
	public static void analyse(DType analysis) 
	{
		for(DMethod method:analysis.getConstructorList())
		{
			System.out.println(method.prettyPrint());
		}
		for(DMethod method:analysis.getMethodList())
		{
			System.out.println(method.prettyPrint());
		}
		
	}
	
	private static void printDependencies(DType clazz) {
		// Constructor List for building returnsMap
		List<DMethod> consList = clazz.getConstructorList();
		for (DMethod c : consList) {
			printMethodDependencies(c); 
		}

		// Method List for building returnsMap
		List<DMethod> declaredMethodList = clazz.getMethodList();
		for (DMethod mtd : declaredMethodList) {
			// don't handle private or no modifier
			printMethodDependencies(mtd); 
		}

	}

	private static void printMethodDependencies(DMethod mtdCall) {
		
		if(methodSeq.containsVertex(mtdCall))
		{
			Set<DefaultEdge> incomming  = methodSeq.incomingEdgesOf(mtdCall);
			
			for(DefaultEdge edge: incomming)
			{
				System.out.println(methodSeq.getEdgeSource(edge).toString() + " -> " + mtdCall.toString());
			}
			
		}
		else
		{
			System.out.println(mtdCall.toString());
		}
		
	}

	private static void buildDepnedecyGraph(DType clazz) {
		
		// Constructor List for building returnsMap
		List<DMethod> consList = clazz.getConstructorList();

		for (DMethod c : consList) {
			// don't handle private/protected or no modifier
			if ((c.getModifiers() == 0)
					|| (Modifier.isPrivate(c.getModifiers()))
					|| (Modifier.isProtected(c.getModifiers()))) {
				continue;
			}
			
			
			List<DType> paramList = c.getParamList();
			for(DType paramType:paramList)
			{
				/*if (paramType.isArray())
				{
					//TODO 
					System.out.println(paramType.getComponentType());
				}*/
				if(!isBasicParamType(paramType))
				{
					List<DMethod> mtdCalls = getMethodCalls(paramType);
					addMtdSequences(c, mtdCalls);
				}
			}
		}

		// Method List for building returnsMap
		//Method[] memberMethodList = clazz.getMethods();
		List<DMethod> declaredMethodList = clazz.getMethodList();
		for (DMethod mtd : declaredMethodList) {
			// don't handle private or no modifier
			if ((mtd.getModifiers() == 0)
					|| (Modifier.isPrivate(mtd.getModifiers()))
					|| (Modifier.isProtected(mtd.getModifiers()))) {
				continue;
			}
			//If the current method is not static then the object of this 
			//class should be created prioir to accessing this method
			if(!Modifier.isStatic(mtd.getModifiers()))
			{
				List<DMethod> mtdCalls = getMethodCalls(clazz);
				addMtdSequences(mtd, mtdCalls);
			}
			
			List<DType> paramList = mtd.getParamList();
			for(DType paramType:paramList)
			{
				/*if (paramType.isArray())
				{
					//TODO 
					System.out.println(paramType.getComponentType());
				}*/
				if(!isBasicParamType(paramType))
				{
					List<DMethod> mtdCalls = getMethodCalls(paramType);
					addMtdSequences(mtd, mtdCalls);
				}
			}
		}

	}

	
	private static void addMtdSequences(DMethod mtd, List<DMethod> mtdCalls) 
	{
		//If the method call "mtd" is not present previously add it
		if(!methodSeq.containsVertex(mtd))
			methodSeq.addVertex(mtd);
		
		for(DMethod mtdprev: mtdCalls)
		{
			//Avoid self Loops
			if(mtdprev.equals(mtd))
				continue;
			//If the method calls in "mtdCalls" are not present previously add them
			if(!methodSeq.containsVertex(mtdprev))
				methodSeq.addVertex(mtdprev);
			//Draw an edge from previous method calls in "mtdCalls" to "mtd" 
			methodSeq.addEdge(mtdprev, mtd);
		}
	}

	private static List<DMethod> getMethodCalls(DType clazz) {
		List<DMethod> returnList = new ArrayList<>();
		//Check to see if the type is interface
		//If so get all implementing concrete classes
		if(clazz.isInterface())
		{
			List<DType> reachableTypeList = listAllImplementingTypes(clazz);
			for(DType reachableType: reachableTypeList)
			{
				//Should not be interface
				if(!reachableType.isInterface())
				{
					for(DMethod mtdCall:getMtdCalls(reachableType))
					{
						if(!returnList.contains(mtdCall))
							returnList.add(mtdCall);
					}
				}
			}
		}
		else
		{
			for(DMethod mtdCall:getMtdCalls(clazz))
			{
				if(!returnList.contains(mtdCall))
					returnList.add(mtdCall);
			}
		}
		return returnList;
	}
	
	private static List<DMethod> getMtdCalls(DType clazz)
	{
		List<DMethod> returnList = new ArrayList<>();
		
		List<DType> typeList = listAllInheritingTypes(clazz);
		
		for(DType clz:typeList) 
		{
			if((clz.getModifiers()==0)
					||(Modifier.isPrivate(clz.getModifiers()))
					||(Modifier.isAbstract(clz.getModifiers()))
					||(Modifier.isInterface(clz.getModifiers()))) 
			{ 
				//do nothing
				continue; 
			} 
					 
			if(returnMap.containsKey(clz)) 
			{ 
				List<DMethod> mtdList = returnMap.get(clz); 
				for(DMethod assbleObj: mtdList)
				{
					if(!returnList.contains(assbleObj))
						returnList.add(assbleObj); 
				}
			}
		}
		return returnList;
	}

	/**
	 * Determines if the Type of {@code paramType} is a basic dataType or other type that
	 * are too generic such as {@link String}
	 * @param paramType class whose type needs to be classified 
	 * @return {@code true} if type of {@code paramType} is basic else {@code false}
	 */
	private static boolean isBasicParamType(DType paramType) {
		
		switch (paramType.getName()) {
		case "byte":
			return true;
		case "short":
			return true;
		case "int":
			return true;
		case "long":
			return true;
		case "float":
			return true;
		case "double":
			return true;
		case "boolean":
			return true;
		case "char":
			return true;
		case "java.lang.Boolean":
			return true;
		case "java.lang.Byte":
			return true;
		case "java.lang.Character":
			return true;
		case "java.lang.Double":
			return true;
		case "java.lang.Float":
			return true;
		case "java.lang.Integer":
			return true;
		case "java.lang.Long":
			return true;
		case "java.lang.Object":
			return true;
		case "java.lang.Short":
			return true;
		case "java.lang.String":
			return true;
		case "java.lang.Void":
			return true;
		default:
			//System.out.println(paramType.getName());
			break;
		}
		return false;
	}

	/**
	 * Lists all classes/interfaces that implement the interface of {@code type}
	 * @param type the @code The {@link DType} object of interface in question.
	 * @return {@code List} of classes/interfaces implementing interface {@code type}.
	 * The returned list is empty if none found.
	 */
	public static List<DType> listAllImplementingTypes(DType type) {
		List<DType> returnList = new ArrayList<DType>();
		if(interfaceInheritance.containsVertex(type))
		{
			BreadthFirstIterator<DType, DefaultEdge> bfsIterator = new BreadthFirstIterator<DType, DefaultEdge>(
					interfaceInheritance, type);
	
			while (bfsIterator.hasNext()) {
				returnList.add(((DType) bfsIterator.next()));
			}
		}
		return returnList;
	}
	
	
	/**
	 * Lists all interfaces that are implemented by {@code type}
	 * @param type The {@link DType} object of class in question.
	 * @return {@code List} of classes/interfaces implemented by {@code type}.
	 * The returned list is empty if none found.
	 */
	public static List<DType> listAllImplementedTypes(DType type) {
		
		List<DType> returnList = new ArrayList<DType>();
		if(interfaceInheritance.containsVertex(type))
		{
			Set<DefaultEdge> incomming  = interfaceInheritance.incomingEdgesOf(type);
			for(DefaultEdge edge: incomming)
			{
				
					returnList.addAll(listAllImplementedTypes(interfaceInheritance.getEdgeSource(edge)));
								
			}
		}
		return returnList;
	}
	
	/**
	 * Lists all classes that are extended by the class of {@code type}
	 * @param type the @code The {@link DType} object of class in question.
	 * @return {@code List} of classes extended by {@code type}.
	 * The returned list is empty if none found.
	 */
	public static List<DType> listAllInheritedTypes(DType type) {
		List<DType> returnList = new ArrayList<DType>();
		if(classInheritance.containsVertex(type))
		{
			Set<DefaultEdge> incomming  = classInheritance.incomingEdgesOf(type);
			for(DefaultEdge edge: incomming)
			{
				returnList.addAll(listAllInheritedTypes(classInheritance.getEdgeSource(edge)));
			}
		}
		return returnList;
	}
	
	/**
	 * Lists all classes that extend the class of {@code type}
	 * @param type the @code The {@link DType} object of class in question.
	 * @return {@code List} of classes extending {@code type}.
	 * The returned list is empty if none found.
	 */
	public static List<DType> listAllInheritingTypes(DType type) {
		List<DType> returnList = new ArrayList<DType>();
		if(classInheritance.containsVertex(type))
		{
			BreadthFirstIterator<DType, DefaultEdge> bfsIterator = new BreadthFirstIterator<DType, DefaultEdge>(
					classInheritance, type);
	
			while (bfsIterator.hasNext()) {
				returnList.add(((DType) bfsIterator.next()));
			}
		}
		return returnList;
	}

	public static DType buildInheritanceGraph(String name) {
		if (visitedTypeList.contains(name))
			return DTypeFactory.getInstance().getDType(name);
		
		// "name" is the class name to load
		DType clazz = null;
				
		//clazz1 = Class.forName(name);
		clazz = DTypeFactory.getInstance().getDType(name);
		visitedTypeList.add(name);

		// Return null if class is private or no modifier present
		if (clazz.getModifiers() == 0
				|| Modifier.isPrivate(clazz.getModifiers()))
			return null;

		// Interfaces Implemented Directly
		try
		{
			Type[] directinterfaceList = clazz.getJavaClass().getGenericInterfaces();
			for (Type interfaze : directinterfaceList) {
				String interfaceName = interfaze.toString().replace(
						"interface ", "");
				if (interfaze instanceof ParameterizedType)
					interfaceName = ((ParameterizedType) interfaze)
							.getRawType().toString().replace("interface ", "");
				DType intfs = buildInheritanceGraph(interfaceName);
				if (intfs != null) {
					if (!interfaceInheritance.vertexSet().contains(clazz))
						interfaceInheritance.addVertex(clazz);
					if (!interfaceInheritance.vertexSet().contains(intfs))
						interfaceInheritance.addVertex(intfs);
					interfaceInheritance.addEdge(intfs, clazz);
				}
	
			}
		}
		catch(NoSuchFieldException e)
		{
			//e.printStackTrace();
		}

		// Check for superclass
		try
		{
			//System.err.println(clazz.getName());
			DType superclass = DTypeFactory.getInstance().getDType(clazz.getJavaClass().getSuperclass().getCanonicalName());
			if (superclass != null) {
				if (!classInheritance.vertexSet().contains(clazz))
					classInheritance.addVertex(clazz);
				if (!classInheritance.vertexSet().contains(superclass))
					classInheritance.addVertex(superclass);
				classInheritance.addEdge(superclass, clazz);
				buildInheritanceGraph(superclass.getName());
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		
		// Constructor List for building returnsMap
		List<DMethod> consList = clazz.getConstructorList();
		for (DMethod c : consList) {
			// don't handle private or no modifier
			if ((c.getModifiers() == 0)
					|| (Modifier.isPrivate(c.getModifiers()))) {
				continue;
			}
			List<DMethod> methodList = new ArrayList<>();
			if (returnMap.containsKey(clazz))
				methodList = returnMap.get(clazz);
			methodList.add(c);
			returnMap.put(clazz, methodList);
		}

		// Method List for building returnsMap
		//Method[] memberMethodList = clazz.getJavaClass.getMethods();
		List<DMethod> declaredMethodList = clazz.getMethodList();
		for (DMethod mtd : declaredMethodList) {
			// don't handle private or no modifier
			if ((mtd.getModifiers() == 0)
					|| (Modifier.isPrivate(mtd.getModifiers()))) {
				continue;
			}
			List<DMethod> methodList = new ArrayList<DMethod>();
			if (returnMap.containsKey(mtd.getReturnType()))
				methodList = returnMap.get(mtd.getReturnType());
			methodList.add(mtd);
			returnMap.put(mtd.getReturnType(), methodList);
		}
		return clazz;
	}

}
