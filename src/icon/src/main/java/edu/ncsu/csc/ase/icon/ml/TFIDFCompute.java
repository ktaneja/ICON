package edu.ncsu.csc.ase.icon.ml;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.regex.Pattern;

import cc.mallet.extract.StringSpan;
import cc.mallet.extract.StringTokenization;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.TokenSequence;
import cc.mallet.util.CharSequenceLexer;

/**
*  Pipe that tokenizes a character sequence.  Expects a CharSequence
*   in the Instance data, and converts the sequence into a token
*   sequence using the given regex or CharSequenceLexer.  
*   (The regex / lexer should specify what counts as a token.)
*/
public class TFIDFCompute extends Pipe implements Serializable
{
	
	CharSequenceLexer lexer;
	
	public TFIDFCompute (CharSequenceLexer lexer)
	{
		this.lexer = lexer;
	}

	public TFIDFCompute (String regex)
	{
		this.lexer = new CharSequenceLexer (regex);
	}

	public TFIDFCompute (Pattern regex)
	{
		this.lexer = new CharSequenceLexer (regex);
	}

	public TFIDFCompute ()
	{
		this (new CharSequenceLexer());
	}

	public Instance pipe (Instance carrier)
	{
		//TFIDF.docWrdFreq.put("", value)
		CharSequence string = (CharSequence) carrier.getData();
		lexer.setCharSequence (string);
		TokenSequence ts = new StringTokenization (string);
		StringSpan ss;
		while (lexer.hasNext()) {
			TFIDF.addList(lexer.next().toString(),"");
			//if(TFIDF.)
			ss = new StringSpan (string, lexer.getStartOffset (), lexer.getEndOffset ());
			ts.add (ss);
		}
		carrier.setData(ts);
		return carrier;
	}

	// Serialization 
	
	private static final long serialVersionUID = 1;
	private static final int CURRENT_SERIAL_VERSION = 0;
	
	private void writeObject (ObjectOutputStream out) throws IOException {
		out.writeInt(CURRENT_SERIAL_VERSION);
		out.writeObject(lexer);
	}
	
	private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
		int version = in.readInt ();
		lexer = (CharSequenceLexer) in.readObject();
	}


	
}
