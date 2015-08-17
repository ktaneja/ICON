package edu.ncsu.csc.ase.dristi.textanalytics;

/**
 * Class to compute Longest Common Sub-sequence
 * 
 * @author Rahul Pandita
 * 
 */
public class LCS {
	
	/**
	 * Method to find {@code LCS (Longest Common Subsequence)} between two words.
	 * if there is no LCS exists method returns an empty string
	 * @param wrd1 the first word
	 * @param wrd2 the second word
	 * @return the LCS of {@literal wrd1} and {@literal wrd2}.
	 * If none exists, an empty string is returned
	 */
	public static String findLCS(String wrd1, String wrd2) {
		StringBuffer retVal = new StringBuffer("");
		int M = wrd1.length();
		int N = wrd2.length();

		// opt[i][j] = length of LCS of wrd1[i..M] and wrd2[j..N]
		int[][] opt = new int[M + 1][N + 1];

		// compute length of LCS and all subproblems via dynamic programming
		for (int i = M - 1; i >= 0; i--) {
			for (int j = N - 1; j >= 0; j--) {
				if (wrd1.charAt(i) == wrd2.charAt(j))
					opt[i][j] = opt[i + 1][j + 1] + 1;
				else
					opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
			}
		}
		// recover LCS itself and put it in return string
		int i = 0, j = 0;
		while (i < M && j < N) {
			if (wrd1.charAt(i) == wrd2.charAt(j)) {
				retVal.append(wrd1.charAt(i));
				i++;
				j++;
			} else if (opt[i + 1][j] >= opt[i][j + 1])
				i++;
			else
				j++;
		}
		return retVal.toString();
	}
}
