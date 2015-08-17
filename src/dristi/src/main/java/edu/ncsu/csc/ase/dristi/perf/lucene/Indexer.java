package edu.ncsu.csc.ase.dristi.perf.lucene;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.javautil.Src2JavaDocReader;
import edu.ncsu.csc.ase.dristi.util.ConsoleUtil;


/**
 * 
 * @author rahulpandita
 * Created: Jan 28, 2014 10:50:15 AM
 */
public class Indexer 
{
	
	public IndexWriter getIndexWriter() throws IOException {
		return getIndexWriter(Config.JAVA_API_LUCENE_SIMPLE_IDX_PATH);
	}
	
	public IndexWriter getIndexWriter(String idxPath) throws IOException {
		return getIndexWriter(idxPath, Version.LATEST);
	}
	
	public IndexWriter getIndexWriter(String idxPath, Version version) throws IOException {
		return getIndexWriter(idxPath, Version.LATEST, new SimpleAnalyzer());
	}
	
	public IndexWriter getIndexWriter(String idxPath, Version version, Analyzer analyser) throws IOException {
		IndexWriter indexWriter = null;
		if (indexWriter == null) 
		{
			IndexWriterConfig config = new IndexWriterConfig(version, analyser);
			Directory index = FSDirectory.open(new File(idxPath));
			indexWriter = new IndexWriter(index, config);
		}
		return indexWriter;
	}
	
	/**
	 * This method must be called in the end
	 * @param indexWriter
	 * @throws IOException
	 */
	public void closeIndexWriter(IndexWriter indexWriter) throws IOException {
		if (indexWriter != null) 
		{
			indexWriter.close();
		}
	}

	
	/**
	 * Main Method to be removed from in production environment
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Indexer idxr = new Indexer();
		
		
		try 
		{
			idxr.rebuildIndexes();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public Map<String, Map<CommentType, String>> getMethodComments()
	{
		Map<String, Map<CommentType, String>> returnMap = new HashMap<>();
		
		Src2JavaDocReader docReader = new Src2JavaDocReader();
		try
		{
			List<String> typeNames = docReader.getTypeNamesFromDirectory(Config.JDK_SRC_PATH1);
			for(String type: typeNames)
			{
				//String type = "java.io.BufferedReader";
				LuceneASTVisitor visitor =  new LuceneASTVisitor(type);
				docReader.updateTypeComments(type, visitor);
				Map<String, Map<CommentType, String>> commentMap = visitor.getCommentMap();
				returnMap.putAll(commentMap);
				//prettyPrint(commentMap);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ConsoleUtil.readConsole("");
		}
		
		return returnMap;
	}
	
	public void prettyPrint(Map<String, Map<CommentType, String>> commentMap) {
		for(String typeName: commentMap.keySet())
		{
			System.out.println(typeName);
			for(CommentType typ: commentMap.get(typeName).keySet())
			{
				System.out.print(typ.name());
				System.out.print("\t");
				String s = commentMap.get(typeName).get(typ);
				while(s.contains(" * "))
					s = s.replace(" * ", "");
				System.out.println(s);
			}
		}
		
	}

	public void rebuildIndexes() throws IOException 
	{
		IndexWriter indexWriter = getIndexWriter();
		Map<String, Map<CommentType, String>> commentMap = getMethodComments();
		
		Map<CommentType, String> temp;
		for(String typeName: commentMap.keySet())
		{
			temp = commentMap.get(typeName);
			for(CommentType typ: temp.keySet())
			{
				String s = temp.get(typ);
				while(s.contains(" * "))
					s = s.replace(" * ", "");
				
				Document doc = new Document();
				doc.add(new TextField("MTDNAME", typeName, Field.Store.YES));
				doc.add(new StringField("TYPE", typ.name(), Field.Store.YES));
				
				FieldType type = new FieldType();
				type.setIndexed(true);
				type.setStored(true);
				type.setStoreTermVectors(true);
				Field field = new Field("COMMENT", s, type);
				doc.add(field);
				
				indexWriter.addDocument(doc);
			}
		}
		
	
		closeIndexWriter(indexWriter);
		
	}
	
	
	
}
