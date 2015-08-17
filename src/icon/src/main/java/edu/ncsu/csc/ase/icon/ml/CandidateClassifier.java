package edu.ncsu.csc.ase.icon.ml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.AdaBoostM2Trainer;
import cc.mallet.classify.AdaBoostTrainer;
import cc.mallet.classify.BalancedWinnowTrainer;
import cc.mallet.classify.C45Trainer;
import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.DecisionTreeTrainer;
import cc.mallet.classify.MCMaxEntTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.classify.WinnowTrainer;
import cc.mallet.pipe.AugmentableFeatureVectorLogScale;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2AugmentableFeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceNGrams;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.types.CrossValidationIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import edu.ncsu.csc.ase.dristi.knowledge.JavaAPIKnowledge;
import edu.ncsu.csc.ase.dristi.knowledge.Knowledge;
import edu.ncsu.csc.ase.dristi.knowledge.KnowledgeAtom;
import edu.ncsu.csc.ase.dristi.knowledge.RESTAPIKnowledge;
import edu.ncsu.csc.ase.icon.eval.Cache;
import edu.ncsu.csc.ase.icon.eval.ExcelRow;
import edu.ncsu.csc.ase.icon.eval.SentenceReader;

public class CandidateClassifier{
	
	private boolean feature = false;

	private static final int crossValidateN = 10;

	private Classifier classifier = null;
	
	private ClassifierTrainer<?> trainer;
	
	
	private static List<String> tstList = new ArrayList<String>();
	
	
	public CandidateClassifier(ClassifierTrainer<?> trainer, boolean feature) {
		this.trainer = trainer;
		this.feature = feature;
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
		RESTAPIKnowledge.addKnowledge();
		addKnowledgePaypal();
		
		
		//process(new AdaBoostM2Trainer(new NaiveBayesTrainer()));
		//process(new BalancedWinnowTrainer());
		//process(new DecisionTreeTrainer());
		//process(new MCMaxEntTrainer());
		//process(new WinnowTrainer());
		//process(new MaxEntTrainer());
		//process(new AdaBoostTrainer(new NaiveBayesTrainer()));
		process(new C45Trainer());
		//process(new NaiveBayesTrainer());
		
		
		Cache.getInstance().write();
		
	}
	
	public static void process(ClassifierTrainer<?> trainer)
	{
		try{
		String fileName = "data/"+trainer.getClass().getSimpleName();
		
		pp1(trainer, true, fileName+".txt");
		pp1(trainer, false, fileName+"_words.txt");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void pp1(ClassifierTrainer<?> trainer, boolean feature, String fileName)
	{
		CandidateClassifier cc;
		tstList = new ArrayList<String>();
		for(int i=0;i<30;i++)
		{
			cc = new CandidateClassifier(new C45Trainer(), feature);
			cc.train();
			
		}
		writeOutput(fileName);
	}
	
	private static void addKnowledgePaypal() {
		KnowledgeAtom ka = new KnowledgeAtom("paypal");
		ka.setSynonyms("paypal", "service");
		ka.setActions("create","execute","look","update","list","refund","capture ","void","reauthorize","approve","retrieve","suspend","reactivate","search","cancel","set","bill","authorize","get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private List<Instance> getInstanceList() {
		List<Instance> insList = new ArrayList<Instance>();
		SentenceReader sr ;
		sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 0);
		List<ExcelRow> rowList = new ArrayList<ExcelRow>();
		rowList.addAll(sr.readSentences());
		sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		rowList.addAll(sr.readSentences());
		
		sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 3);
		rowList.addAll(sr.readSentences());
		
		Collections.shuffle(rowList);
		int i=1;
		String ss;
		KnowledgeAtom ka;
		List<String> actions;
		for(ExcelRow row: rowList)
		{
			ka = Knowledge.getInstance().fetchAtom(row.getClassName());
			actions= new ArrayList<String>();
			if (ka!=null)
			{
				actions = ka.getActions()!=null?ka.getActions():new ArrayList<String>();
			}
			ss = row.getComment();
			ss = Cache.getInstance().clean(ss);
			if(feature)
				ss = Cache.getInstance().getFeatureSeq(ss, actions);
			ss = row.getCommentType() + " " + ss;
			insList.add(new Instance(ss,row.getFinalMarked(), "Java", "aa"));
			System.out.println("Processed "+ i+"/"+rowList.size());
			i=i+1;
			
		}
		return insList;
	}
	
	private void train() 
	{
		
		InstanceList instances = new InstanceList (getPipe());
		instances.addThruPipe(getInstanceList().iterator());
		//classifier = trainer.train(instances);
		crossValidate(instances);
	}





	private void crossValidate(InstanceList instances) {
		CrossValidationIterator cv = new CrossValidationIterator(instances, crossValidateN);
		StringBuffer buff = new StringBuffer();
		double avg_p1 = 0;
		double avg_r1 = 0;
		double avg_f1 = 0;
		double avg_acc = 0;
		double p1,r1,f1,acc;
		int i=0;
		String label;
		while(cv.hasNext())
		{
			InstanceList[] fold = cv.nextSplit();
			classifier = trainer.train(fold[0]);
			label = classifier.getLabelAlphabet().lookupLabel(1).toString();
			if(label.equals("1"))
			{
				i=1; 
			}
			p1 = classifier.getPrecision(fold[1], classifier.getLabelAlphabet().lookupLabel(i));
			r1 = classifier.getRecall(fold[1], classifier.getLabelAlphabet().lookupLabel(i));
			f1 = classifier.getF1(fold[1], classifier.getLabelAlphabet().lookupLabel(i));
			acc = classifier.getAccuracy(fold[1]);
			
			avg_p1 = avg_p1+(p1/10);
			avg_r1 = avg_r1+(r1/10);
			avg_f1 = avg_f1+(f1/10);
			avg_acc = avg_acc+(acc/10);
			
			buff.append("0\t").append(p1).append("\t").append(r1).append("\t").append(f1).append("\t").append(acc).append("\n");
		}
		buff.append("1\t").append(avg_p1).append("\t").append(avg_r1).append("\t").append(avg_f1).append("\t").append(avg_acc).append("\n");
		tstList.add(buff.toString());
	}





	private Pipe getPipe() {
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		//Pattern tokenPattern = Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$");
		// Pipes: lowercase, tokenize, remove stopwords, map to features
		
		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceRemoveStopwords(true, true));
		//pipeList.add(new SimpleTaggerSentence2TokenSequence());
		//int[] ngrams = {1,2};
		//pipeList.add(new TokenSequenceNGrams( ngrams));
		pipeList.add(new TokenSequence2FeatureSequence());
		//pipeList.add(new TokenSequence2FeatureSequenceWithBigrams());
		pipeList.add(new Target2Label());
		pipeList.add(new FeatureSequence2AugmentableFeatureVector());
		pipeList.add(new AugmentableFeatureVectorLogScale());
		Pipe instancePipe = new SerialPipes(pipeList);
		return instancePipe;
	}





	private void testClassifier() {
		testClassifier("Closes the underlying inputstream", Knowledge.getInstance().fetchAtom("BufferedInputStream").getActions());
		testClassifier("If the stream has been closed previously", Knowledge.getInstance().fetchAtom("BufferedInputStream").getActions());
		
		
		
		testClassifier("All objects (including all object versions and delete markers) in the bucket must be deleted before the bucket itself can be deleted.", Knowledge.getInstance().fetchAtom("Bucket").getActions());
		testClassifier("All objects in the bucket must be deleted before the bucket itself can be deleted.", Knowledge.getInstance().fetchAtom("Bucket").getActions());
		testClassifier("You must initiate a multipart upload (see Initiate Multipart Upload) before you can upload any part.", Knowledge.getInstance().fetchAtom("Bucket").getActions());
		
		List<String> actions = Knowledge.getInstance().fetchAtom("paypal").getActions();
		
		
		testClassifier("This call only works after a buyer has approved the payment using the provided PayPal approval URL.", actions);
		testClassifier("This call only works after a buyer has previosuly approved the payment.", actions);
		testClassifier("You can optionally update transaction information when executing the payment by passing in one or more transactions.", actions);
		testClassifier("Please note that it is not possible to use patch after execute has been called.", actions);
		testClassifier("This call returns only the sales that were created via the REST API.", actions);
		testClassifier("To get a list of your refunds, you can first get a list of payments.", actions);
		testClassifier("A fully captured authorization cannot be voided.", actions);
		testClassifier("If 30 days have passed from the original authorization, you must create a new authorization instead.", actions);
		testClassifier("Pass the payment token in the URI of a POST call, to execute the subscription agreement after buyer approval.", actions);
		testClassifier("When using the paypal payment method, the payer must be redirected to the approval_url provided within the links of the response.", actions);
	}
	
	private void testClassifier(String sentence, List<String> actions) {
		sentence = Cache.getInstance().clean(sentence);
		String feature = Cache.getInstance().getFeatureSeq(sentence, actions);
		Instance ins = new Instance(feature,"", "Java", "aa");
		Classification c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel() + "->"+sentence + "\n\t(" + feature + ")");
	}
	
	
}
