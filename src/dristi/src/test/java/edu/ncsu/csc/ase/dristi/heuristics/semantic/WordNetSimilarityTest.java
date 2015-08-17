package edu.ncsu.csc.ase.dristi.heuristics.semantic;

import junit.framework.TestCase;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.WordNetSimilarity;

public class WordNetSimilarityTest extends TestCase {

	public void test01() throws Exception 
	{
		WordNetSimilarity wnsim = WordNetSimilarity.getInstance();
		assertTrue(wnsim.isSimilar("has","have"));
	}
}
