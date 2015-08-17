package edu.ncsu.csc.ase.icon.eval;

import org.junit.Assert;
import org.junit.Test;

/**
 * Utility Class for reading data from the Excel Sheets
 * @author Rahul Pandita
 * 
 */
public class SentenceReaderTest {
	
	@Test
	public void Test01() {
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		Assert.assertTrue(sr.readData().size()==3198);
	}
	
	@Test
	public void Test02() {
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		System.out.println(sr.readSentences().size());
		Assert.assertTrue(sr.readSentences().size()==2417);
	}
}
