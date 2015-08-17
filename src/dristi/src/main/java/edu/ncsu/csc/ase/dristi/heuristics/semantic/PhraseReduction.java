package edu.ncsu.csc.ase.dristi.heuristics.semantic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ncsu.csc.ase.dristi.util.StringUtil;


/**
 * This Class holds the templates to deal with frequently occurring phrases
 * 
 * @author rahul_pandita
 * 
 */
public class PhraseReduction {
	
	private static PhraseReduction instance;

	public enum Operation {

		REMOVESPACE, REPLACEPARAM,unknown

	}

	private static Map<String, Operation> patternMap;

	/**
	 * Private Constructor for Singleton Pattern
	 */
	private PhraseReduction() {
		populatePatternMap();
	}

	/**
	 * Method to access the singleton object
	 * 
	 * @return Singleton PhraseReduction object
	 */
	public static synchronized PhraseReduction getInstance() {
		if (instance == null)
			instance = new PhraseReduction();
		return instance;
	}

	/**
	 * Populates the patternMap
	 */
	private void populatePatternMap() {
		patternMap = new LinkedHashMap<String, Operation>();
		patternMap
				.put("[A-Z]+[\\s]+[oO]peration",
				Operation.REMOVESPACE);
		patternMap.put("[cC]ommon[\\s][rR]equest[\\s][hH]eaders",
				Operation.REMOVESPACE);
		patternMap.put("[rR]equest[\\s][hH]eaders", Operation.REMOVESPACE);
		patternMap.put("[cC]ommon[\\s][rR]esponse[\\s][hH]eaders",
				Operation.REMOVESPACE);
		patternMap.put("[rR]esponse[\\s][hH]eaders", Operation.REMOVESPACE);
		patternMap.put("[fF]or[\\s][mM]ore[\\s][iI]nformation",
				Operation.REMOVESPACE);
		patternMap.put(
				"[cC][oO][rR][sS][\\s][cC]onfiguration[\\s][iI]nformation",
				Operation.REMOVESPACE);
	}

	public String applyReduction(String sentence) {

		Operation oper = Operation.unknown;
		Pattern pattern;
		Matcher matcher;
		for (String phrase : patternMap.keySet()) {
			pattern = Pattern.compile(phrase);
			matcher = pattern.matcher(sentence);

			if (matcher.find()) {
				oper = patternMap.get(phrase); 
				switch (oper) {
				case REMOVESPACE:
					String newPhrase = StringUtil.removeSpaces(matcher.group());
					sentence = sentence.replaceAll(phrase, newPhrase);
					break;
				default:
					break;
				}
			}
		}
		return sentence;
	}
	
	public String applyDecoration(String sentence) {

		Operation oper = Operation.unknown;
		Pattern pattern;
		Matcher matcher;
		for (String phrase : patternMap.keySet()) {
			pattern = Pattern.compile(phrase);
			matcher = pattern.matcher(sentence);

			if (matcher.find()) {
				oper = patternMap.get(phrase); 
				switch (oper) {
				case REMOVESPACE:
					String newPhrase = StringUtil.removeSpaces(matcher.group());
					sentence = sentence.replaceAll(phrase,"<font color=\"CC00CC\"><b><u>"+ newPhrase+"</u></b></font>");
					break;

				default:
					break;
				}
			}
		}
		return sentence;
	}
}
