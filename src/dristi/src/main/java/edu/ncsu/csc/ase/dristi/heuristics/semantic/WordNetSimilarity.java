package edu.ncsu.csc.ase.dristi.heuristics.semantic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import net.didion.jwnl.JWNLException;

import edu.illinois.cs.cogcomp.wnsim.WNSim;
import edu.ncsu.csc.ase.dristi.configuration.Config;

/**
 * This Class is responsible for calculating the similarity between two
 * words/phrases. The Class utilizes the WORD-NET similarity project by UIUC
 * Cognitive Computing Group :
 * <http://cogcomp.cs.illinois.edu/page/software_view/Illinois%20WNSim>
 * 
 * @author rahul_pandita
 * 
 */
public class WordNetSimilarity {

	/**
	 * variable to hold the singleton instance
	 */
	private static WordNetSimilarity instance;

	/**
	 * HashMap to store additional synonyms
	 */
	private static HashMap<String, ArrayList<String>> synonymList;

	/**
	 * Instance variable holding instance of WNSim Class
	 */
	private static WNSim wnsim;

	/**
	 * Static method to get instance of WordNetSimilarity singleton object
	 * 
	 * @return
	 */
	public static synchronized WordNetSimilarity getInstance() {
		if (instance == null) {
			try {
				instance = new WordNetSimilarity();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	/**
	 * Private Constructor for Singleton Pattern
	 * 
	 * @throws FileNotFoundException
	 * @throws JWNLException
	 */
	private WordNetSimilarity() throws FileNotFoundException, JWNLException {
		wnsim = WNSim.getInstance(Config.WORD_NET_PATH,
				Config.WORD_NET_CONFIG_XML_PATH);
		synonymList = new HashMap<String, ArrayList<String>>();
		ArrayList<String> synonyms = new ArrayList<String>();
		synonyms.add("USER");
		synonyms.add("YOUR");
		synonyms.add("YOU");
		synonymList.put("YOUR", synonyms);
		synonymList.put("USER", synonyms);
		synonymList.put("YOU", synonyms);
		
		ArrayList<String> synonyms1 = new ArrayList<String>();
		synonyms1.add("PUT");
		synonyms1.add("CREATE");
		synonymList.put("PUT", synonyms1);
		synonymList.put("CREATE", synonyms1);
	}

	/**
	 * Method to Compute similarity between words in a phrase and the provided
	 * word. If any of the word in phrase is synonym of word the method returns
	 * true else false.
	 * 
	 * @param phrase
	 *            phrase that needs to matched
	 * @param word
	 *            word with which the phrase is matched
	 * @return true if any of the word in phrase is synonym of word, else false
	 * @throws Exception
	 */
	public boolean isSimilar(String phrase, String word) throws Exception {
		boolean similarity = false;
		String[] wordlist = phrase.split("\\s+");
		for (String wrd : wordlist) {
			if (wrd.equalsIgnoreCase(word))
				return true;
			if ((synonymList.containsKey(wrd.toUpperCase()))
					&& (synonymList.get(wrd.toUpperCase()).contains(word
							.toUpperCase())))
				return true;

			if(word.toLowerCase().equalsIgnoreCase("exist")||word.toLowerCase().equalsIgnoreCase("exists"))
			{
				//are
				if((wrd.toLowerCase().equalsIgnoreCase("be"))||(wrd.toLowerCase().equalsIgnoreCase("is"))||wrd.toLowerCase().equalsIgnoreCase("are"))
					return false;
			}
			else if(wrd.toLowerCase().equalsIgnoreCase("exist")||wrd.toLowerCase().equalsIgnoreCase("exists"))
			{
				if((word.toLowerCase().equalsIgnoreCase("be"))||(word.toLowerCase().equalsIgnoreCase("is"))||word.toLowerCase().equalsIgnoreCase("are"))
					return false;
			}
			double sim = wnsim.getWupSimilarity(wrd, word);
			//System.out.println(wrd + "-" + word + "(" + sim + ")");
			if (sim > Config.WORD_NET_MIN_THRESHOLD) {
				return true;

			}
		}

		return similarity;
	}
	
	
	
	public double getScore(String phrase, String word) throws JWNLException
	{
		double sim=1;
		String[] wordlist = phrase.split("\\s+");
		for (String wrd : wordlist) {
			if (wrd.equalsIgnoreCase(word))
				return sim;
			if ((synonymList.containsKey(wrd.toUpperCase()))
					&& (synonymList.get(wrd.toUpperCase()).contains(word
							.toUpperCase())))
				return sim;

			if(word.toLowerCase().equalsIgnoreCase("exists"))
			{
				if((wrd.toLowerCase().equalsIgnoreCase("be"))||(wrd.toLowerCase().equalsIgnoreCase("is")))
					return 0.0;
			}
			sim = wnsim.getWupSimilarity(wrd, word);
			//System.out.println(wrd + "-" + word + "(" + sim + ")");
			if (sim > Config.WORD_NET_MIN_THRESHOLD) {
				return sim;

			}
		}

		return sim;
	}
}
