package edu.ncsu.csc.ase.icon.eval;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.analysis.SemanticAnalyser;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;

public class TestClass {

	//private static Map<String, List<ITuple>> mem2 = new HashMap<>();
	//private static Map<String, List<String>> mem1 = new HashMap<>();
	//private static Map<String, List<SemanticGraph>> mem3 = new HashMap<>();
	private static Map<String, List<Tree>> mem4 = new HashMap<>();
	private static List<String> mem5 = new ArrayList<>();

	public static void main(String[] args) {
		try {
			NLPParser parser = NLPParser.getInstance();
			SemanticAnalyser analyser = new SemanticAnalyser();
			FileInputStream fileInputStream = new FileInputStream(
					"senList1.xls");
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheetAt(1);
			HSSFRow row;
			ExcelRow excelRow;

			for (int i = 0; i < 3199; i++) {
				row = worksheet.getRow(i);
				excelRow = new ExcelRow(row);
				try {
					excelRow.setConstraint(test(parser, analyser,
							excelRow.getClassName(), excelRow.getCommentType(),
							excelRow.getComment()));
				} catch (Exception e) {

				}
				excelRow.setIconVal(excelRow.getConstraint().length() > 0 ? 1
						: 0);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String test(NLPParser parser, SemanticAnalyser analyser,
			String className, String commentType, String text) {
		StringBuffer buff = new StringBuffer("");
		commentType = commentType.trim().toUpperCase();

		if (!(commentType.equals("SUMMARY") || commentType.equals("EXCEPTION")))
			return "";

		text = SpecFeature.textCorrection(text);
		CandidateSelectionHeuristic(parser, text);

		/*
		 * List<String> sentenceList; if(mem1.containsKey(text)) { sentenceList
		 * = mem1.get(text); } else { sentenceList =
		 * analyser.applyReduction(text); mem1.put(text, sentenceList); }
		 * 
		 * for(String sentence:sentenceList) { //System.out.println(sentence);
		 * List<Tree> posTreeList = parser.getPOSTree(sentence); for(Tree
		 * posTree:posTreeList) posTree.pennPrint(); List<ITuple> tupleList;
		 * if(mem2.containsKey(sentence)) { tupleList = mem2.get(sentence); }
		 * else { tupleList = parser.getTuples(sentence); mem2.put(sentence,
		 * tupleList); } for(ITuple tuple:tupleList) { if(tuple!=null) {
		 * //System.out.println(tuple.toString()); List<ITuple> tList =
		 * analyser.getSequencingConstraints(className, tuple); for(ITuple
		 * t:tList) { if(t!=null) { System.out.println(t.toString());
		 * buff.append(t.toString()); buff.append("\n"); } } } } }
		 */
		return buff.toString();
	}

	public static boolean CandidateSelectionHeuristic(NLPParser parser,
			String text) {
		if (text.length() > 140)
			return false;

		for (String startingVerb : SpecFeature.getStartingVerbList()) {
			if (!text.trim().toLowerCase().startsWith(startingVerb)) {
				List<Tree> posTrees;
				if (mem4.containsKey(text)) {
					return false;
				} else {
					posTrees = parser.getPOSTree(text);
					mem4.put(text, posTrees);
				}

				for (Tree posTree : posTrees) {
					List<TaggedWord> taggedWords = posTree.taggedYield();
					for (TaggedWord wrd : taggedWords) {
						if (wrd.beginPosition() == 0) {
							if (wrd.tag().startsWith("VB")) {
								if (!mem5.contains(wrd.tag() + "\t"
										+ wrd.word())) {
									mem5.add(wrd.tag() + "\t" + wrd.word());
									System.out.println(wrd.tag() + "\t"
											+ wrd.word() + "\t"
											+ posTree.toString());
								}
							}
							// System.out.println(wrd.word());
						}
					}
				}

			}
			return false;
		}

		for (String keyword : SpecFeature.getSpecKeywords()) {
			if (text.trim().toLowerCase().contains(keyword)) {
				return true;
			}
		}

		return false;
	}
}
