package edu.ncsu.csc.ase.dristi.javautil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

public class APITransformer extends BodyTransformer{

	
	public HashMap<SootMethod, HashMap<Stmt, List<Object>>> map =new HashMap<SootMethod, HashMap<Stmt,List<Object>>>();
	
	public HashMap<SootMethod, ArrayList<Boolean>> retValueMap = new HashMap<SootMethod, ArrayList<Boolean>>();
	
	public HashMap<SootMethod, ArrayList<Boolean>> exMap = new HashMap<SootMethod, ArrayList<Boolean>>();
	
	@Override
	protected void internalTransform(Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {
	if(!body.getMethod().getName().startsWith("dum"))
		return;
			System.out.println("here!!!!");
		//HashMap<SootMethod, List<SootClass>> exceptionMap = new HashMap<SootMethod, List<SootClass>>();
		
		//HashMap<Stmt, List<Object>> logStmtList = new HashMap<Stmt, List<Object>>();
		
		//HashMap<SootMethod, ArrayList<Boolean>> exMap1 = new HashMap<SootMethod, ArrayList<Boolean>>();
		
		//HashMap<SootMethod, ArrayList<Boolean>> retValueMap1 = new HashMap<SootMethod, ArrayList<Boolean>>();
		
		PatchingChain<Unit> units =  body.getUnits();
		System.out.println("$$$" + body.getMethod().getParameterType(0));
		Type tt = body.getMethod().getParameterType(0);
		SootClass sc = Scene.v().getSootClass(tt.toString());
		System.out.println(sc.getSuperclass().getJavaStyleName());
		StringBuffer buff = new StringBuffer();
		if(sc.isPublic())
		{
			buff.append("public class ");
			buff.append(sc.getShortJavaStyleName());
			buff.append(" extends ");
			buff.append(sc.getSuperclass().getJavaStyleName());
		}
		else
		{
			int i =0;
			System.out.println(10/i);
		}
		
		for(SootField sf: sc.getFields())
		{
			if (sf.isPublic())
				System.out.println("");
		}
		
		System.out.println(buff.toString());
		for(SootMethod sm : sc.getMethods())
		{
		System.out.println(sm);
		}
		for (Unit unit : units) 
		{
			
			Stmt s = (Stmt) unit;
			if(s.containsInvokeExpr())
			{
				InvokeExpr invocation = s.getInvokeExpr();
				
				SootMethod method = invocation.getMethod();
				System.out.println(method);
			}
			
		}
		
		
	}
	
	public void addretValueMap(HashMap<SootMethod, ArrayList<Boolean>> map)
	{
		ArrayList<Boolean> val;
		for(SootMethod method: map.keySet())
		{
			if(!(method.getReturnType() instanceof soot.VoidType))
			{
				val = map.get(method);
				if(this.retValueMap.containsKey(method))
				{
					val = this.retValueMap.get(method);
					val.addAll(map.get(method));
				}
				this.retValueMap.put(method, val);
			}
		}
	}
	
	public void addexMap(HashMap<SootMethod, ArrayList<Boolean>> map)
	{
		ArrayList<Boolean> val;
		for(SootMethod method: map.keySet())
		{
			val = map.get(method);
			if(this.exMap.containsKey(method))
			{
				val = this.exMap.get(method);
				val.addAll(map.get(method));
			}
			this.exMap.put(method, val);
		}
	}
	

}
