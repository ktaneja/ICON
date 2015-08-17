package edu.ncsu.csc.ase.dristi.util;

/**
 * Class containing common String utility routines
 * 
 * @author rahul_pandita
 * 
 */
public class StringUtil {
	/**
	 * Utility function to remove spaces
	 * 
	 * @param str
	 *            the String in which spaces are to be removed
	 * @return String with all spaces stripped
	 */
	public static String removeSpaces(String str) {
		return str.replaceAll("\\s", "");
	}

	/**
	 * Utility function to split Camel Case notation Code Adapted from <a href=
	 * "http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java"
	 * >StackOverflow</a>
	 * 
	 * @param s
	 * @return
	 */
	public static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

}
