package edu.ncsu.csc.ase.dristi.textanalytics;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.csc.ase.dristi.datastructure.IDFreqPair;
import junit.framework.TestCase;

public class NGramDriverTest extends TestCase
{
	@Test
	public void test1() {
		Reader reader = null;
		StringBuffer buff = new StringBuffer();
		buff.append("please extract n-grams, ok thanks , ok thanks !");
		reader = new StringReader(buff.toString());
		List<IDFreqPair<String>> lst = NGramDriver.getNGrams(reader, 2);
		Assert.assertTrue("List size should be 1", lst.size()==1);
		Assert.assertTrue("The only Element should be ok thanks", lst.get(0).getId().equals("ok thanks"));
	}
	
	@Test
	public void testPuntuationIndependence() {
		Reader reader = null;
		StringBuffer buff = new StringBuffer();
		buff.append("please extract n-grams, ok thanks, ok thanks!");
		reader = new StringReader(buff.toString());
		List<IDFreqPair<String>> lst = NGramDriver.getNGrams(reader, 2);
		Assert.assertTrue("List size should be 1", lst.size()==1);
		Assert.assertTrue("The only Element should be ok thanks", lst.get(0).getId().equals("ok thanks"));
	}
}