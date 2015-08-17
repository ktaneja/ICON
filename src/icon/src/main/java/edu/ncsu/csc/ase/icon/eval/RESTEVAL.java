package edu.ncsu.csc.ase.icon.eval;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.cache.SentenceCache;
import edu.ncsu.csc.ase.dristi.util.FileUtilExcel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;

/**
 * 
 * @author Rahul Pandita
 * Created: Mar 6, 2014 1:31:48 AM
 */
public class RESTEVAL extends ICONEval
{
	
	public RESTEVAL() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public void main1() {
		try {
			FileUtilExcel xlsWriter = new FileUtilExcel("senList2");

			FileInputStream fileInputStream = new FileInputStream("senList1.xls");
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheetAt(0);
			HSSFRow row;
			HSSFCell cellA1, cellB1, cellC1, cellD1, cellE1, cellF1, cellG1, cellH1, cellI1;
			Integer sNo, sen, rMarked, kMarked, finalMarked, iconVal;
			String className, methodName, commentType, comment, constraint;
			
			for (int i = 0; i < 1543; i++) {
				row = worksheet.getRow(i);
				if(row == null)
					continue;
				cellA1 = row.getCell(0);
				if (cellA1 == null)
					sNo = 0;
				else
					sNo = ((Double) cellA1.getNumericCellValue()).intValue();

				cellB1 = row.getCell(1);
				if (cellB1 == null)
					className = "";
				else
					className = cellB1.getStringCellValue();

				cellC1 = row.getCell(2);
				if (cellC1 == null)
					methodName = "";
				else
					methodName = cellC1.getStringCellValue();

				cellD1 = row.getCell(3);
				if (cellD1 == null)
					commentType = "";
				else
					commentType = cellD1.getStringCellValue();

				cellE1 = row.getCell(4);
				if (cellE1 == null)
					comment = "";
				else
					comment = cellE1.getStringCellValue();

				cellF1 = row.getCell(5);
				if (cellF1 == null)
					sen = 0;
				else
					sen = ((Double) cellF1.getNumericCellValue()).intValue();

				cellG1 = row.getCell(6);
				if (cellG1 == null)
					rMarked = 0;
				else
					rMarked = ((Double) cellG1.getNumericCellValue())
							.intValue();

				cellH1 = row.getCell(7);
				if (cellH1 == null)
					kMarked = 0;
				else
					kMarked = ((Double) cellH1.getNumericCellValue())
							.intValue();

				cellI1 = row.getCell(8);
				if (cellI1 == null)
					finalMarked = 0;
				else
					finalMarked = ((Double) cellI1.getNumericCellValue())
							.intValue();

				iconVal = 0;
				constraint = "";
				// val=false;
				if ((commentType.trim().equalsIgnoreCase("SUMMARY"))
						|| (commentType.trim().equalsIgnoreCase("EXCEPTION"))) {
					try {
						constraint = test(className, comment);
					} catch (Exception e) {

					}
					iconVal = constraint.length() > 0 ? 1 : 0;
				}

				xlsWriter.writeDataToExcel(sNo, className, methodName,
						commentType, comment, sen, rMarked, kMarked,
						finalMarked, iconVal, constraint);

			}
			xlsWriter.commitXlS();
			SentenceCache.getInstace().updatePersistance();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean CandidateSelectionHeuristic(NLPParser parser,
			String text) {
		if (text.length() > 140)
			return false;

		for (String startingVerb : SpecFeature.getStartingVerbList()) {
			if (text.trim().toLowerCase().startsWith(startingVerb))
				return false;
		}

		List<Tree> posTrees;
		if (mem4.containsKey(text)) {
			posTrees = mem4.get(text);
		} else {
			posTrees = parser.getPOSTree(text);
			mem4.put(text, posTrees);
		}

		for (Tree posTree : posTrees) {
			List<TaggedWord> taggedWords = posTree.taggedYield();
			for (TaggedWord wrd : taggedWords) {
				if (wrd.beginPosition() == 0) {
					if (wrd.tag().startsWith("VB")) {
						return false;
					}

				}
			}
		}

		List<SemanticGraph> typedDependencyList;
		if (mem3.containsKey(text)) {
			typedDependencyList = mem3.get(text);
		} else {
			typedDependencyList = parser.getStanfordDependencies(text);
			mem3.put(text, typedDependencyList);
		}
		GrammaticalRelation tmod = GrammaticalRelation.valueOf("tmod");
		// GrammaticalRelation mark = GrammaticalRelation.valueOf("mark");
		// GrammaticalRelation root = GrammaticalRelation.valueOf("root");
		// GrammaticalRelation auxpass = GrammaticalRelation.valueOf("auxpass");

		for (SemanticGraph posTree : typedDependencyList) 
		{

			for (SemanticGraphEdge edge : posTree.edgeListSorted()) {
				if (edge.getRelation().equals(tmod)) {
					return true;
				}

			}

			IndexedWord wrd = posTree.getFirstRoot();
			if (wrd.tag().toUpperCase().trim().startsWith("VB")) 
			{
				if (wrd.tag().toUpperCase().equals("VB")
						|| wrd.tag().toUpperCase().equals("VBD")
						|| wrd.tag().toUpperCase().equals("VBN"))
					return true;
			}

		}

		for (String keyword : SpecFeature.getSpecKeywords()) {
			if (text.trim().toLowerCase().contains(keyword)) {
				return true;
			}
		}

		return false;
	}


	public void main2() {
		try 
		{
			System.out.println(test("DELETE Bucket policy", "Replacing objects is disabled until enableResolveObject is called."));
			SentenceCache.getInstace().updatePersistance();
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		RESTEVAL instance = new RESTEVAL();
		instance.main2();
	}

}
