package edu.ncsu.csc.ase.dristi.javautil;

import soot.Pack;
import soot.PackManager;
import soot.Transform;

public class SootMain {
	
	public static void main(String[] args) {
		//String DIRNAME = "C:\\Phoenix\\OOPSLA12\\dev\\workspace\\Sample\\src";
		String DIRNAME = "D:\\phoenix\\Projects\\AGG\\workspace\\J2METEST\\src";
		
		Pack jtp = PackManager.v().getPack("jtp");
	    APITransformer lt = new APITransformer();
		jtp.add(new Transform("jtp.FindLogPatterns", lt));
		

		args = new String[]{"-allow-phantom-refs","c", "-j2me","-keep-line-number", "-process-dir" ,DIRNAME};
		soot.Main.main(args);
		
		
		
	}
}
