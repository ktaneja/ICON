package edu.ncsu.csc.ase.dristi.ReqAnalysis;

import java.io.File;
import java.util.Map;

import org.junit.Assert;

import junit.framework.TestCase;
import edu.ncsu.csc.ase.dristi.reqAnalysis.SynonymReduction;
import edu.ncsu.csc.ase.dristi.configuration.Config;

/**
 * Test Class for {@link SynonymReduction}
 * @author Rahul Pandita
 *
 */
public class SynonymReductionTest  extends TestCase 
{
	private static final String path = Config.PROJECT_PATH + "data" + File.separator + "senList.txt";
	
	public static void test1() 
	{
		SynonymReduction sr = new SynonymReduction(path);
		Assert.assertTrue(sr.getSentences() != null);
		Assert.assertEquals(sr.getSentences().size(), 11876);;
	}
	
	public static void test2() 
	{
		SynonymReduction sr = new SynonymReduction("");
		Assert.assertTrue(sr.getSentences() != null);
		Assert.assertEquals(sr.getSentences().size(), 0);;
	}
	
	public static void test3 ()
	{
		SynonymReduction sr = new SynonymReduction(null);
		Map<String, Integer> termMap = sr.termVector("AA AB ABC AA AA AA AB AB AB ABC");
		Assert.assertTrue(termMap.keySet().size()==3);
		Assert.assertTrue(termMap.get("abc")==2);
		Assert.assertTrue(termMap.get("aa")==4);
		Assert.assertTrue(termMap.get("ab")==4);
	}

}
