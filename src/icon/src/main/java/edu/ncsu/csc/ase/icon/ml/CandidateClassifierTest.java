package edu.ncsu.csc.ase.icon.ml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cc.mallet.classify.AdaBoost;
import cc.mallet.classify.AdaBoostTrainer;
import cc.mallet.classify.Classification;
import cc.mallet.classify.DecisionTree;
import cc.mallet.classify.DecisionTreeTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesEMTrainer;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.fst.SimpleTagger;
import cc.mallet.fst.SimpleTagger.SimpleTaggerSentence2FeatureVectorSequence;
import cc.mallet.pipe.AugmentableFeatureVectorAddConjunctions;
import cc.mallet.pipe.AugmentableFeatureVectorLogScale;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2AugmentableFeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.SimpleTaggerSentence2StringTokenization;
import cc.mallet.pipe.SimpleTaggerSentence2TokenSequence;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequence2FeatureSequenceWithBigrams;
import cc.mallet.pipe.TokenSequence2FeatureVectorSequence;
import cc.mallet.pipe.TokenSequenceNGrams;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.types.CrossValidationIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import edu.ncsu.csc.ase.icon.eval.Cache;
import edu.ncsu.csc.ase.icon.eval.ExcelRow;
import edu.ncsu.csc.ase.icon.eval.SentenceReader;

public class CandidateClassifierTest {
	
	private NaiveBayes classifier = null;
	
	private NaiveBayesTrainer trainer = null;
	
	public CandidateClassifierTest() {
		trainer = new NaiveBayesTrainer();
	}
	
	
	
	
	
	public static void main(String[] args) {
		//DataInstnace ins = new DataInstnace("SUMMARY", "Important: This call only works after a buyer has approved the payment using the provided PayPal approval URL.", "", "","");
		//System.out.println(ins.getData().toString());
		CandidateClassifierTest  cc = new CandidateClassifierTest();
		cc.eval4();
		Cache.getInstance().write();
	}

	private static List<Instance> getInstanceList1() {
		List<Instance> insList = new ArrayList<Instance>();
		//insList.add(new DataInstnace("true, true, false, false", "Temporal", "Java", "blah must be called b4"));
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		List<ExcelRow> rowList = sr.readSentences();
		int i=1;
		String ss;
		for(ExcelRow row: rowList)
		{
			ss = row.getComment();
			ss = Cache.getInstance().clean(ss);
			insList.add(new Instance(ss,row.getFinalMarked(), "Java", "aa"));
			System.out.println("Processed "+ i+"/"+rowList.size());
			i=i+1;
		}
		System.out.println(insList.size());
		return insList;
	}
	
	private static List<Instance> getInstanceList2() {
		List<Instance> insList = new ArrayList<Instance>();
		//insList.add(new DataInstnace("true, true, false, false", "Temporal", "Java", "blah must be called b4"));
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		List<ExcelRow> rowList = sr.readSentences();
		int i=1;
		String ss;
		for(ExcelRow row: rowList)
		{
			ss = row.getComment();
			ss = Cache.getInstance().clean(ss);
			ss = Cache.getInstance().getFeatureSeq(ss);
			insList.add(new Instance(ss,row.getFinalMarked(), "Java", "aa"));
			System.out.println("Processed "+ i+"/"+rowList.size());
			i=i+1;
		}
		System.out.println(insList.size());
		return insList;
	}
	
	private static List<Instance> getInstanceList() {
		List<Instance> insList = new ArrayList<Instance>();
		//insList.add(new DataInstnace("true, true, false, false", "Temporal", "Java", "blah must be called b4"));
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		List<ExcelRow> rowList = sr.readSentences();
		int i=1;
		for(ExcelRow row: rowList)
		{
			insList.add(new DataInstnace(row, "Java", "aa"));
			System.out.println("Processed "+ i+"/"+rowList.size());
			i=i+1;
		}
		System.out.println(insList.size());
		return insList;
	}


	public void train() {
		Pattern tokenPattern = Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$");
		Pipe instancePipe;
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		pipeList.add(new CharSequence2TokenSequence(tokenPattern));
		pipeList.add(new SimpleTaggerSentence2StringTokenization());
		pipeList.add(new TokenSequence2FeatureSequence());
		pipeList.add(new FeatureSequence2AugmentableFeatureVector());
		
		instancePipe = new SerialPipes(pipeList);
		
		InstanceList instances = new InstanceList (instancePipe);
		instances.addThruPipe(getInstanceList1().iterator());
		trainer.train(instances);
		CrossValidationIterator cv = new CrossValidationIterator(instances, 10);
		
		while(cv.hasNext())
		{
			InstanceList[] fold = cv.nextSplit();
			classifier = trainer.train(fold[0]);
			
			System.out.print(classifier.getAccuracy(fold[0]));
			System.out.println("\t" + classifier.getAccuracy(fold[1]));
		}
	}
	
	private void eval1() {
		//Pattern tokenPattern = Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$");
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		
		// Pipes: lowercase, tokenize, remove stopwords, map to features
		//pipeList.add(new Input2CharSequence("UTF-8"));
		//pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		//pipeList.add(new TokenSequenceRemoveStopwords(true, true));
		pipeList.add(new SimpleTaggerSentence2TokenSequence());
		//int[] ngrams = {2,3,4,5};
		//pipeList.add(new TokenSequenceNGrams( ngrams));
		pipeList.add(new TokenSequence2FeatureSequence());
		pipeList.add(new Target2Label());
		pipeList.add(new FeatureSequence2AugmentableFeatureVector());
		
		Pipe instancePipe = new SerialPipes(pipeList);
		
		InstanceList instances = new InstanceList (instancePipe);
		
		
		//InstanceList testInstances1 = new InstanceList (instancePipe);
		instances.addThruPipe(getInstanceList1().iterator());
		//testInstances1.addThruPipe(getInstanceList1().subList(1500, 2416).iterator());
		CrossValidationIterator cv = new CrossValidationIterator(instances, 10);
		
		while(cv.hasNext())
		{
			InstanceList[] fold = cv.nextSplit();
			classifier = trainer.train(fold[0]);
			
			System.out.print(classifier.getAccuracy(fold[0]));
			System.out.println("\t" + classifier.getAccuracy(fold[1]));
		}
		
		//instances.addThruPipe(getInstanceList1().iterator());
		//System.out.println("\t" + classifier.getAccuracy(testInstances1));
		
		DataInstnace ins = new DataInstnace("SUMMARY", "This call only works after a buyer has approved the payment using the provided PayPal approval URL.", "", "","");
		Classification c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ins = new DataInstnace("SUMMARY", "All objects (including all object versions and delete markers) in the bucket must be deleted before the bucket itself can be deleted.", "", "","");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ins = new DataInstnace("SUMMARY", "You must initiate a multipart upload (see Initiate Multipart Upload) before you can upload any part.", "", "","");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
	}
	
	private void eval3() {
		//Pattern tokenPattern = Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$");
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		
		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceRemoveStopwords(true, true));
		//pipeList.add(new SimpleTaggerSentence2TokenSequence());
		int[] ngrams = {1,2};
		pipeList.add(new TokenSequenceNGrams( ngrams));
		pipeList.add(new TokenSequence2FeatureSequence());
		pipeList.add(new Target2Label());
		pipeList.add(new FeatureSequence2AugmentableFeatureVector());
		
		Pipe instancePipe = new SerialPipes(pipeList);
		
		InstanceList instances = new InstanceList (instancePipe);
		
		
		//InstanceList testInstances1 = new InstanceList (instancePipe);
		instances.addThruPipe(getInstanceList2().iterator());
		//testInstances1.addThruPipe(getInstanceList1().subList(1500, 2416).iterator());
		CrossValidationIterator cv = new CrossValidationIterator(instances, 10);
		
		while(cv.hasNext())
		{
			InstanceList[] fold = cv.nextSplit();
			classifier = trainer.train(fold[0]);
			
			System.out.print(classifier.getAccuracy(fold[0]));
			System.out.println("\t" + classifier.getAccuracy(fold[1]));
		}
		
		//instances.addThruPipe(getInstanceList1().iterator());
		//System.out.println("\t" + classifier.getAccuracy(testInstances1));
		String ss = Cache.getInstance().clean("This call only works after a buyer has approved the payment using the provided PayPal approval URL.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		Instance ins = new Instance(ss,"", "Java", "aa");
		Classification c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("All objects (including all object versions and delete markers) in the bucket must be deleted before the bucket itself can be deleted.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		
		ss = Cache.getInstance().clean("You must initiate a multipart upload (see Initiate Multipart Upload) before you can upload any part.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("Closes the underlying inputstream");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("If the stream has been closed previously");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("All objects in the bucket must be deleted before the bucket itself can be deleted.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("This call only works after a buyer has previosuly approved the payment.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		
		
	}
	
	private void eval4() {
		//Pattern tokenPattern = Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$");
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
		
		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceRemoveStopwords(true, true));
		//pipeList.add(new SimpleTaggerSentence2TokenSequence());
		int[] ngrams = {2,3};
		//pipeList.add(new TokenSequenceNGrams( ngrams));
		pipeList.add(new TokenSequence2FeatureSequence());
		//pipeList.add(new TokenSequence2FeatureSequenceWithBigrams());
		pipeList.add(new Target2Label());
		pipeList.add(new FeatureSequence2AugmentableFeatureVector());
		pipeList.add(new AugmentableFeatureVectorLogScale());
		
		Pipe instancePipe = new SerialPipes(pipeList);
		
		InstanceList instances = new InstanceList (instancePipe);
		
		
		//InstanceList testInstances1 = new InstanceList (instancePipe);
		instances.addThruPipe(getInstanceList2().iterator());
		//testInstances1.addThruPipe(getInstanceList1().subList(1500, 2416).iterator());
		CrossValidationIterator cv = new CrossValidationIterator(instances, 10);
		//DecisionTree classifier = null;
		//DecisionTreeTrainer trainer = new DecisionTreeTrainer(10); 
		while(cv.hasNext())
		{
			InstanceList[] fold = cv.nextSplit();
			classifier = trainer.train(fold[0]);
			
			System.out.print(classifier.getAccuracy(fold[0]));
			System.out.println("\t" + classifier.getAccuracy(fold[1]));
		}
		
		//instances.addThruPipe(getInstanceList1().iterator());
		//System.out.println("\t" + classifier.getAccuracy(testInstances1));
		String ss = Cache.getInstance().clean("This call only works after a buyer has approved the payment using the provided PayPal approval URL.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		Instance ins = new Instance(ss,"", "Java", "aa");
		Classification c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("All objects (including all object versions and delete markers) in the bucket must be deleted before the bucket itself can be deleted.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		
		ss = Cache.getInstance().clean("You must initiate a multipart upload (see Initiate Multipart Upload) before you can upload any part.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("Closes the underlying inputstream");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("If the stream has been closed previously");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("All objects in the bucket must be deleted before the bucket itself can be deleted.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		ss = Cache.getInstance().clean("This call only works after a buyer has previosuly approved the payment.");
		ss = Cache.getInstance().getFeatureSeq(ss);
		ins = new Instance(ss,"", "Java", "aa");
		c  = classifier.classify(ins.getData().toString());
		System.out.println(c.getLabeling().getBestLabel());
		
		
		
	}
}
