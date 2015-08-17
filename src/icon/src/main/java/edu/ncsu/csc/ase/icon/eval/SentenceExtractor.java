package edu.ncsu.csc.ase.icon.eval;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.ncsu.csc.ase.dristi.util.FileUtilExcel;

public class SentenceExtractor 
{
	
	protected static int count = 0;
	
	/**
	 * Approximation for finding sentence boundaries
	 * Code Modified from
	 * <a href="http://stackoverflow.com/questions/5553410/regular-expression-match-a-sentence">Stack Overflow</a>
	 * @param subjectString
	 * @return
	 */
	public static List<String> sentnceBoundaryApprox(String subjectString) {
        List<String> sentences = new ArrayList<String>();
		Pattern re = Pattern.compile(
            "# Match a sentence ending in punctuation or EOS.\n" +
            "[^.!?\\s]    # First char is non-punct, non-ws\n" +
            "[^.!?]*      # Greedily consume up to punctuation.\n" +
            "(?:          # Group for unrolling the loop.\n" +
            "  [.!?]      # (special) inner punctuation ok if\n" +
            "  (?!['\"]?\\s|$)  # not followed by ws or EOS.\n" +
            "  [^.!?]*    # Greedily consume up to punctuation.\n" +
            ")*           # Zero or more (special normal*)\n" +
            "[.!?]?       # Optional ending punctuation.\n" +
            "['\"]?       # Optional closing quote.\n" +
            "(?=\\s|$)", 
            Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher reMatcher = re.matcher(subjectString);
        while (reMatcher.find()) {
            sentences.add(reMatcher.group());
        } 
        return sentences;
    }
	
	public static void mainTest(String[] args) {
		String ss = "Abstract class for writing filtered character streams. \n\nThe abstract class java.io.FilterWriter itself \t provides default methods that pass all requests to the contained stream. Subclasses of FilterWriter should override some of these methods and may also provide additional methods and fields.";
		for(String s : sentnceBoundaryApprox(ss))
			System.out.println(s);
	}
	
	public static void writeComment(FileUtilExcel xlsWriter, String temp, String CommentType) throws Exception 
	{
		if(temp!="")
		{
			for(String sen: sentnceBoundaryApprox(temp))
			{
				xlsWriter.writeDataToExcel("",CommentType, sen);
				count  = count + 1;
			}
		}
	}
}
