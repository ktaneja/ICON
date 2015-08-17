package edu.ncsu.csc.ase.icon.eval;

import java.util.List;

import edu.ncsu.csc.ase.dristi.util.ConsoleUtil;

public class SentnceSplitterTest {
	
	public static void main(String[] args) {
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		List<ExcelRow> lst = sr.readSentences();
		int i=1;
		for(ExcelRow row: lst)
		{
			if(Cache.getInstance().getSentnceList(row.getComment()).size()>1)
				ConsoleUtil.readConsole(row.getComment());
			System.out.println(i + "/" + lst.size() );
			i++;
		}
		
		/*
		 * in in .available()
		 * out.write(csq.subSequence(start, end).toString())
		 * &∨g.eclipse.jdt.core.dom.QualifiedName&&
		 * toURI toURI ()).equals( f.
		 * ".."
		 * ".tmp"MM
		 * '.'MM
		 * Console con = System.console(); if (con != null) { Scanner sc = new Scanner(con.reader()); .
		 * .).
		 * out.append(csq, start, end)
		 * out.print(csq.subSequence(start, end).toString())
		 * Thread.currentThread().interrupt();
		 */
	}

}
