package edu.ncsu.csc.ase.dristi.heuristics.semantic;



/**
 * This Class holds the logic to deal with incomplete sentences
 * 
 * @author rahul_pandita
 * 
 */
public class SentenceCompletion {
	
	private static SentenceCompletion instance;

	/**
	 * Private Constructor for Singleton Pattern
	 */
	private SentenceCompletion() {
		populatePatternMap();
	}

	/**
	 * Method to access the singleton object
	 * 
	 * @return Singleton SentenceCompletion object
	 */
	public static synchronized SentenceCompletion getInstance() {
		if (instance == null)
			instance = new SentenceCompletion();
		return instance;
	}

	/**
	 * Populates the patternMap
	 */
	private void populatePatternMap() {
		//TODO Determine if a generic Pattern can be constructed
	}

	public String applyReduction(String sentence) {
		sentence = sentence.trim();
		if(sentence.startsWith("Returns"))
			sentence = sentence.replace("Returns", "This implementation returns");
		else if(sentence.startsWith("Sets"))
			sentence = sentence.replace("Sets", "This implementation sets");
		else if(sentence.startsWith("Creates"))
			sentence = sentence.replace("Creates", "This implementation creates");
		else if(sentence.startsWith("Restores"))
			sentence = sentence.replace("Restores", "This implementation restores");
		else if(sentence.startsWith("Uploads"))
			sentence = sentence.replace("Uploads", "This implementation uploads");
		else if(sentence.startsWith("Deletes"))
			sentence = sentence.replace("Deletes", "This implementation deletes");
		return sentence;
	}
	
	public String applyDecoration(String sentence) {
		sentence = sentence.trim();
		if(sentence.startsWith("Returns"))
			sentence = sentence.replace("Returns", "<font color=\"red\"><b><u>This implementation returns</u></b></font>");
		else if(sentence.startsWith("Sets"))
			sentence = sentence.replace("Sets", "<font color=\"red\"><b><u>This implementation sets</u></b></font>");
		else if(sentence.startsWith("Creates"))
			sentence = sentence.replace("Creates", "<font color=\"red\"><b><u>This implementation creates</u></b></font>");
		else if(sentence.startsWith("Restores"))
			sentence = sentence.replace("Restores", "<font color=\"red\"><b><u>This implementation restores</u></b></font>");
		else if(sentence.startsWith("Uploads"))
			sentence = sentence.replace("Uploads", "<font color=\"red\"><b><u>This implementation uploads</u></b></font>");
		else if(sentence.startsWith("Deletes"))
			sentence = sentence.replace("Deletes", "<font color=\"red\"><b><u>This implementation deletes</u></b></font>");
		return sentence;
	}
}
