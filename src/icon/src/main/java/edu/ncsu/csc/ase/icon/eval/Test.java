package edu.ncsu.csc.ase.icon.eval;

import java.util.ArrayList;
import java.util.List;

import net.didion.jwnl.JWNLException;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.WordNetSimilarity;

public class Test 
{
	public static void main(String[] args) throws JWNLException {
		WordNetSimilarity wnSim = WordNetSimilarity.getInstance();
		List<String> phraseLst = getMethodNameList();
		double[][] mat = new double[phraseLst.size()][phraseLst.size()];
		
		for(int i =0; i< phraseLst.size();i++)
		{
			
			for(int j =0; j< phraseLst.size();j++)
			{
				mat[i][j] = wnSim.getScore(phraseLst.get(i),phraseLst.get(j));
			}
			
		}
		
		for(int i =0; i< phraseLst.size();i++)
		{
			System.out.print(phraseLst.get(i)+"\t");
		}
			System.out.println();
		
		for(int i =0; i< phraseLst.size();i++)
		{
			System.out.print(phraseLst.get(i)+"\t");
			
			for(int j =0; j< phraseLst.size();j++)
			{
				System.out.print(mat[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	private static List<String> getMethodNameList()
	{
		List<String> lst = new ArrayList<String>();
		lst.add("add");
		lst.add("add All");
		lst.add("clear");
		lst.add("contains");
		lst.add("contains All");
		lst.add("equals");
		lst.add("get");
		lst.add("hashCode");
		lst.add("index Of");
		lst.add("is Empty");
		lst.add("iterator");
		lst.add("last Index Of");
		lst.add("list Iterator");
		lst.add("remove");
		lst.add("remove All");
		lst.add("replace All");
		lst.add("retain All");
		lst.add("set");
		lst.add("size");
		lst.add("sort");
		lst.add("spliterator");
		lst.add("subList");
		lst.add("to Array");
		return lst;
	}
}
