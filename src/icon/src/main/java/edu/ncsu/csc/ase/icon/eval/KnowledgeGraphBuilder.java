package edu.ncsu.csc.ase.icon.eval;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DMethod;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DType;
import edu.ncsu.csc.ase.dristi.datastructure.ootype.DTypeFactory;
import edu.ncsu.csc.ase.dristi.javautil.JavaDependencyBuilder;
import edu.ncsu.csc.ase.dristi.javautil.Src2JavaDocReader;

/**
 * 
 * @author rahulpandita
 * @date Jan 21, 2014 6:30:21 PM
 */
public class KnowledgeGraphBuilder 
{
	public static void main(String[] args) throws Exception{
		JavaDependencyBuilder.main(args);
		Src2JavaDocReader t = new Src2JavaDocReader();
		List<String> typeNameList = t.getTypeNamesFromDirectory(Config.JDK_SRC_PATH);
		Map<String, DType> subList = new LinkedHashMap<String, DType>();
		for (String typeName : typeNameList)
		{
			DType clazz = DTypeFactory.getInstance().getDType(typeName);
			if ((clazz.getModifiers() == 0)
					|| (Modifier.isPrivate(clazz.getModifiers()))
					|| (Modifier.isAbstract(clazz.getModifiers()))
					|| (Modifier.isInterface(clazz.getModifiers()))) {
				// do nothing
				continue;
			}
			// Build Dependencies on type
			try {
				subList.put(typeName, t.updateTypeComments(typeName));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		}
		
		PrintWriter out = new PrintWriter("temp.txt");
		for(String typeName : subList.keySet())
		{
			DType clazz = subList.get(typeName);
			String name = clazz.getName();
			name = name.replace("java.io.", "");
			out.println("ka = new KnowledgeAtom(\""+name+"\");");
			
			StringBuffer buff = new StringBuffer();
			buff.append("ka.setSynonyms(\"");
			buff.append(name);
			buff.append("\", ");
			
			List<String> implemented = new ArrayList<>();
			for(DType typ: JavaDependencyBuilder.listAllImplementedTypes(clazz))
			{
				if(!implemented.contains(typ.getName()))
					implemented.add(typ.getName());
			}
			
			for(DType typ: JavaDependencyBuilder.listAllInheritedTypes(clazz))
			{
				if(!implemented.contains(typ.getName()))
					implemented.add(typ.getName());
			}
			
			for(String tps: implemented)
			{
				buff.append("\"");
				buff.append(tps.replace("java.io.", ""));
				buff.append("\"");
				buff.append(", ");
				
			}
			out.println(buff.substring(0, buff.length()-2)+");");
			
			
			
			buff = new StringBuffer();
			buff.append("ka.setActions(");
			List<String> mtdNameList = new ArrayList<>();
			for(DMethod mtd: clazz.getMethodList())
			{
				if(!mtdNameList.contains(mtd.getMethodName()))
					mtdNameList.add(mtd.getMethodName());
				
			}
			
			
			for(String mtdName: mtdNameList)
			{
				buff.append("\"");
				buff.append(mtdName);
				buff.append("\"");
				buff.append(", ");
				
			}
			out.println(buff.substring(0, buff.length()-2)+");");
			out.println("Knowledge.getInstance().addKnowledgeAtom(ka);");
			
			out.println("");
			
		}
		out.close();
	}
}
