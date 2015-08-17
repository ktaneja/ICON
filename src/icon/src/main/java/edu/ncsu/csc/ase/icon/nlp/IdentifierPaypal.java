package edu.ncsu.csc.ase.icon.nlp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cc.mallet.types.Instance;
import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.analysis.SemanticAnalyser;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.knowledge.JavaAPIKnowledge;
import edu.ncsu.csc.ase.dristi.knowledge.Knowledge;
import edu.ncsu.csc.ase.dristi.knowledge.KnowledgeAtom;
import edu.ncsu.csc.ase.dristi.knowledge.RESTAPIKnowledge;
import edu.ncsu.csc.ase.icon.eval.ExcelRow;
import edu.ncsu.csc.ase.icon.eval.SentenceReader;
import edu.ncsu.csc.ase.icon.eval.SpecFeature;

public class IdentifierPaypal{
	
	protected NLPParser parser;
	protected SemanticAnalyser analyser;
	
	
	private static List<String> tstList = new ArrayList<String>();
	
	
	public IdentifierPaypal() 
	{
		parser = NLPParser.getInstance();
		analyser = new SemanticAnalyser();
	}
	
	private static void writeOutput(String fileName)
	{
		
		try(PrintWriter out = new PrintWriter(fileName))
		{
			for(String str: tstList)
			{
				out.println(str);
				out.println();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		//JavaAPIKnowledge.addKnowledge();
		//RESTAPIKnowledge.addKnowledge();
		addKnowledgePaypal();
		
		//SentenceReader sr ;
		//sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 3);
		//List<ExcelRow> rowList = new ArrayList<ExcelRow>();
		//rowList.addAll(sr.readSentences());
		
		IdentifierPaypal id = new IdentifierPaypal();
		//tstList = new ArrayList<String>();
		id.process();
		//writeOutput("op.txt");
	}
	
	private static void addKnowledgePaypal() {
		KnowledgeAtom ka = new KnowledgeAtom("paypal");
		ka.setSynonyms("paypal", "service", "order", "agreement", "payment", "authorization", "refund", "sales");
		ka.setActions("create","execute","look","update","list","refund","capture ","void","reauthorize","approve","retrieve","suspend","reactivate","search","cancel","set","bill","authorize","get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	public void process()
	{
		List<String> sentnces =testSentnceList();
		String op;
		StringBuffer buff = new StringBuffer();
		for(String sentnce: sentnces)
		{
			op = process(sentnce);
			buff.append(sentnce).append("\n").append(op).append("\n\n");
			
		}
		System.out.println("-----------------------------------------");
		System.out.println(buff.toString());
	}
	
	private String process(String sentnce)
	{
		StringBuffer buff = new StringBuffer("");
		String text = SpecFeature.textCorrection(sentnce);
		List<String> sentenceList;
		sentenceList = analyser.applyReduction(text);
		List<ITuple> tupleList;
		for (String sentence : sentenceList) {
			tupleList = parser.getTuples(sentence);
			for (ITuple tuple : tupleList) {
				if (tuple != null) {
					List<ITuple> tList=  new ArrayList<>();
					tList.addAll(analyser.getSequencingConstraints("paypal", tuple));
					for (ITuple t : tList) {
						if (t != null) {
							buff.append(t.toString());
							buff.append("\n");
						}
					}
				}
			}
		}
		return buff.toString().trim();
	}
	

	private List<String> testSentnceList()
	{
		List<String> lst = new ArrayList<String>();
		//lst.add("Important: When using the paypal payment method, the payer must be redirected to the approval_url provided within the links of the response.");
		lst.add("When using the paypal payment method, the payer must be redirected to the approval_url provided within the links of the response.");
		lst.add("Use this call to execute (complete) a PayPal payment that has been approved by the payer.");
		//lst.add("Important: This call only works after a buyer has approved the payment using the provided PayPal approval URL.");
		lst.add("This call only works after a buyer has approved the payment using the provided PayPal approval URL.");
		lst.add("Use this call to get details about payments that have not completed, such as payments that are created and approved, or if a payment has failed.");
		lst.add("Please note that it is not possible to use patch after execute has been called.");
		//lst.add("Note: This call returns only the sales that were created via the REST API.");
		lst.add("This call returns only the sales that were created via the REST API.");
		lst.add("Use this call to refund a completed payment.");
		lst.add("To get a list of your refunds, you can first get a list of payments.");
		lst.add("Use this resource to capture and process a previously created authorization.");
		lst.add("Use this call to void a previously authorized payment.");
		//lst.add("Note: A fully captured authorization cannot be voided.");
		lst.add("A fully captured authorization cannot be voided.");
		lst.add("Use this call to reauthorize a PayPal account payment.");
		lst.add("If 30 days have passed from the original authorization, you must create a new authorization instead.");
		lst.add("Use this call to get details about a captured payment.");
		lst.add("Use this call to refund a captured payment.");
		lst.add("You can update the information for an existing billing plan.");
		lst.add("Use this call to execute an agreement after the buyer approves it.");
		lst.add("Pass the payment token in the URI of a POST call, to execute the subscription agreement after buyer approval.");
		lst.add("Use this call to void an existing order.");
		lst.add("Use this call to refund an existing captured order.");
		return lst;
	}
	
	
	
}
