package edu.ncsu.csc.ase.dristi.util;

import org.junit.Assert;

import edu.ncsu.csc.ase.dristi.util.StringUtil;
import junit.framework.TestCase;

public class StringUtilTest extends TestCase 
{
	public void test1() {
		String testStr = "abcd";
		String expectedStr ="abcd";
		String actualStr = StringUtil.removeSpaces(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));

	}
	
	public void test2() {
		String testStr = "ab cd";
		String expectedStr = "abcd";
		String actualStr = StringUtil.removeSpaces(testStr);
		
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test3() {
		String testStr = " ab cd";
		String expectedStr = "abcd";
		String actualStr = StringUtil.removeSpaces(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test4() {
		String testStr = "ab cd ";
		String expectedStr = "abcd";
		String actualStr = StringUtil.removeSpaces(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test5() {
		String testStr = "  ab   cd  ";
		String expectedStr = "abcd";
		String actualStr = StringUtil.removeSpaces(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test6() {
		String testStr = "";
		String expectedStr = "";
		String actualStr = StringUtil.removeSpaces(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test7() {
		String testStr = "     ";
		String expectedStr = "";
		String actualStr = StringUtil.removeSpaces(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test8() {
		
		String testStr = null;
		try
		{
			StringUtil.removeSpaces(testStr);
			//Exception should be thrown by this time
			Assert.assertTrue(false);
		}
		catch(NullPointerException e)
		{
			Assert.assertTrue(true);
		}
	}
	
	public void test9(){
		String testStr ="IOException";
		String expectedStr = "IO Exception";
		String actualStr = StringUtil.splitCamelCase(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test10(){
		String testStr ="LineNumberInputStream";
		String expectedStr = "Line Number Input Stream";
		String actualStr = StringUtil.splitCamelCase(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
	
	public void test11(){
		String testStr ="FilePermissionCollection";
		String expectedStr = "File Permission Collection";
		String actualStr = StringUtil.splitCamelCase(testStr);
		Assert.assertTrue(expectedStr.equals(actualStr));
	}
} 
	