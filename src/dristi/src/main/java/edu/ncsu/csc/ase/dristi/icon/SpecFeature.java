package edu.ncsu.csc.ase.dristi.icon;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SpecFeature 
{
	public static String textCorrection(String text)
	{
		text = text.replaceAll(Pattern.quote("&∨g.eclipse.jdt.core.dom.QualifiedName&&"), "XX");
		text = text.replaceAll(Pattern.quote("connect unconnected"), "not connected");
		text = text.replaceAll(Pattern.quote("connect connected"), "connected");
		return text;
	}
	
	public static List<String> getStartingVerbList()
	{
		List<String> verbList = new ArrayList<>();
		verbList.add("marks ");
		verbList.add("tests ");
		verbList.add("flushes ");
		verbList.add("skips ");
		verbList.add("returns ");
		verbList.add("converts ");
		verbList.add("sets ");
		verbList.add("checks ");
		verbList.add("repositions ");
		verbList.add("causes ");
		verbList.add("prints ");
		verbList.add("resets ");
		verbList.add("for example ");
		verbList.add("for example,");
		return verbList;
	}
	
	public static List<String> getSpecKeywords()
	{
		List<String> verbList = new ArrayList<>();
		verbList.add("if ");
		verbList.add("must ");
		verbList.add("will ");
		verbList.add("can ");
		verbList.add("could ");
		verbList.add("may ");
		verbList.add("should ");
		verbList.add("invoke ");
		verbList.add("invoking ");
		verbList.add("has been ");
		return verbList;
	}
	
}
