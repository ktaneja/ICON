package edu.ncsu.csc.ase.dristi.textanalytics;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class LCSTest extends TestCase
{
	@Test
	public void testSimple() {
		String wrd1 = "Rahul";
		String wrd2 = "ahurl";
		String result = LCS.findLCS(wrd1, wrd2);
		Assert.assertTrue(result.equals("ahul"));

	}
	
	@Test
	public void testEmpty() {
		String wrd1 = "";
		String wrd2 = "";
		String result = LCS.findLCS(wrd1, wrd2);
		Assert.assertTrue(result.equals(""));

	}
	
	@Test
	public void testSecondEmpty() {
		String wrd1 = "a";
		String wrd2 = "";
		String result = LCS.findLCS(wrd1, wrd2);
		Assert.assertTrue(result.equals(""));

	}
	
	@Test
	public void testFirstEmpty() {
		String wrd1 = "";
		String wrd2 = "a";
		String result = LCS.findLCS(wrd1, wrd2);
		Assert.assertTrue(result.equals(""));

	}
}	