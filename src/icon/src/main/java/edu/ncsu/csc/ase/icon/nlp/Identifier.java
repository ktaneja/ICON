package edu.ncsu.csc.ase.icon.nlp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

public class Identifier{
	
	protected NLPParser parser;
	protected SemanticAnalyser analyser;
	
	
	private static List<String> tstList = new ArrayList<String>();
	
	
	public Identifier() 
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
		
		JavaAPIKnowledge.addKnowledge();
		//RESTAPIKnowledge.addKnowledge();
		//addKnowledgePaypal();
		
		//SentenceReader sr ;
		//sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 3);
		//List<ExcelRow> rowList = new ArrayList<ExcelRow>();
		//rowList.addAll(sr.readSentences());
		
		Identifier id = new Identifier();
		//tstList = new ArrayList<String>();
		id.process();
		//writeOutput("op.txt");
	}
	
	private static void addKnowledgePaypal() {
		KnowledgeAtom ka = new KnowledgeAtom("paypal");
		ka.setSynonyms("paypal", "service");
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
					tList.addAll(analyser.getSequencingConstraints("Service", tuple));
					tList.addAll(analyser.getSequencingConstraints("cors", tuple));
					tList.addAll(analyser.getSequencingConstraints("lifecycle", tuple));
					tList.addAll(analyser.getSequencingConstraints("policy", tuple));
					tList.addAll(analyser.getSequencingConstraints("tagging", tuple));
					tList.addAll(analyser.getSequencingConstraints("website", tuple));
					tList.addAll(analyser.getSequencingConstraints("acl", tuple));
					tList.addAll(analyser.getSequencingConstraints("logging", tuple));
					tList.addAll(analyser.getSequencingConstraints("location", tuple));
					tList.addAll(analyser.getSequencingConstraints("notification", tuple));
					tList.addAll(analyser.getSequencingConstraints("requestPayment", tuple));
					tList.addAll(analyser.getSequencingConstraints("version", tuple));
					tList.addAll(analyser.getSequencingConstraints("bucket", tuple));
					tList.addAll(analyser.getSequencingConstraints("object", tuple));
					tList.addAll(analyser.getSequencingConstraints("multipleobject", tuple));
					tList.addAll(analyser.getSequencingConstraints("ObjectAcl", tuple));
					tList.addAll(analyser.getSequencingConstraints("ObjectTorrent", tuple));
					tList.addAll(analyser.getSequencingConstraints("ObjectRestore", tuple));
					tList.addAll(analyser.getSequencingConstraints("MultipartUpload", tuple));
					tList.addAll(analyser.getSequencingConstraints("Part", tuple));
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
		lst.add("All objects (including all object versions and Delete Markers) in the bucket must be deleted before the bucket itself can be deleted.");
		//lst.add("However, bucket owners can grant other users permission to delete the website configuration by writing a bucket policy granting them the S3:DeleteBucketWebsite permission.");
		//lst.add("However, the bucket owner can use a bucket policy to grant permission to other users to read this configuration with the s3:GetBucketNotification permission.");
		//lst.add("However, bucket owners can allow other users to read the website configuration by writing a bucket policy granting them the S3:GetBucketWebsite permission.");
		//lst.add("However, bucket owners can use a bucket policy to grant permission to other users to set this configuration with s3:PutBucketNotification permission.");
		lst.add("After restoring an object copy, you can update the restoration period by reissuing this request with the new period.");
		lst.add("However, for copying an object greater than 5 GB, you must use the multipart upload API.");
		lst.add("Note After you initiate multipart upload and upload one or more parts, you must either complete or abort multipart upload in order to stop getting charged for storage of the uploaded parts.");
		lst.add("Only after you either complete or abort multipart upload, Amazon S3 frees up the parts storage and stops charging you for the parts storage.");
		//lst.add("Note After you initiate multipart upload and upload one or more parts, you must either complete or abort multipart upload in order to stop getting charged for storage of the uploaded parts.");
		//lst.add("Only after you either complete or abort multipart upload, Amazon S3 frees up the parts storage and stops charging you for the parts storage.");
		lst.add("After successfully uploading all relevant parts of an upload, you call this operation to complete the upload.");
		lst.add("After a multipart upload is aborted, no additional parts can be uploaded using that upload ID.");
		lst.add("This operation must include the upload ID, which you obtain by sending the initiate multipart upload request (see Initiate Multipart Upload).");
		return lst;
	}
	
	
	
}
