package edu.ncsu.csc.ase.dristi.util;

import edu.stanford.nlp.trees.Tree;

/**
 * Utility Class for NLP related tasks
 * 
 * @author rahul_pandita
 * 
 */
public class NLPUtil {

	/**
	 * Creates a Sentence from the given POS Tree.
	 * 
	 * @param tree
	 *            the POS representation of a sentence.
	 * @return a string of words as the sentence.
	 */
	public static String getSentnce(Tree tree) {
		StringBuffer buff = new StringBuffer();
		String s;
		for (int i = 0; i < tree.getLeaves().size(); i++) {
			s = tree.getLeaves().get(i).nodeString();
			if (s.equalsIgnoreCase("-LRB-"))
				s = "(";
			else if (s.equalsIgnoreCase("-RRB-"))
				s = ")";
			buff.append(s);
			buff.append(" ");
		}
		s = buff.toString().trim();
		
		s =s.replace(", , ", "");
		
		if(s.endsWith(",."))
			s = s.substring(0, s.length()-2);
		if(s.endsWith(", ."))
			s = s.substring(0, s.length()-3);
		if(s.endsWith(","))
			s = s.substring(0, s.length()-1);
		if(s.startsWith(","))
			s = s.substring(1);
		if (!s.endsWith("."))
			s = s + ".";
		return s;
	}

}
