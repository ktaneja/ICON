package edu.ncsu.csc.ase.dristi.configuration;

import java.io.File;

public class Config 
{
	
	public static final String PROJECT_PATH = "";
	//public static final String PROJECT_PATH = "/Users/rahulpandita/Dropbox/my docs/workspace/APINLP/";
	
	public static final String DATA_FILE = PROJECT_PATH + "data.txt";
	
	public static final String JDK_SRC_PATH = "data\\jdk17src\\java\\io";
	
	public static final String JDK_SRC_PATH1 = "data\\jdk17src";
	
	// BEGIN Logging related configuration variables//
	/**
	 * Variable to control verbosity of parser 
	 * The six logging levels used by Log are (in order):
	 * 		TRACE (the least serious)
	 * 		DEBUG
	 * 		INFO
	 * 		WARN
	 * 		ERROR
	 * 		FATAL (the most serious)
	 */
	public static final String DEFAULT_LOG_LEVEL_KEY = "ERROR";
	// END Logging related configuration variables//
	
	// BEGIN WordNet configuration//
	/**
	 * Path to folder containing WordNet Files
	 */
	public static final String WORD_NET_PATH = PROJECT_PATH + "WordNet-3.0";
	
	/**
	 * Path to WordNet Configuration File
	 */
	public static final String WORD_NET_CONFIG_XML_PATH = "wordnet.xml";
	
	/**
	 * Minimum Threshold for WordNet similarity 
	 */
	public static final double WORD_NET_MIN_THRESHOLD = 0.9; 
	
	// END WordNet configuration//
	
	// BEGIN WEB Crawling configuration//
	
	/**
	 * Path to File, where the Crawled API will be stored on persistent storage.
	 */
	public static final String AMAZONS3_PATH = PROJECT_PATH + "AMAZONS3.txt";
	
	// END WEB Crawling configuration//
	
	/**
	 * Path to File, where the Sentence CACHE data will be stored;
	 */
	public static final String SENTENCE_CACHE_FILE_PATH = "sentnce.cache";
	public static final String POS_CACHE_FILE_PATH = "POS.cache";
	public static final String TYPED_DEPENDENCIES_CACHE_FILE_PATH = "typed.cache";
	
	
	// BEGIN APACHE LUCENE configuration//
	
	/**
	 * Path to Folder containing the REST API APACHE LUCENE Index
	 */
	public static final String REST_API_LUCENE_IDX_PATH = PROJECT_PATH + "idx" + File.separator + "idx_intermediate";
	
	/**
	 * Path to Folder containing the REST API APACHE LUCENE Index
	 */
	public static final String JAVA_API_LUCENE_IDX_PATH = PROJECT_PATH + "data" + File.separator + "perf" + File.separator + "idx" + File.separator + "idx_intermediate";
	
	/**
	 * Path to Folder containing the REST API APACHE LUCENE Index
	 */
	public static final String JAVA_API_LUCENE_SIMPLE_IDX_PATH = PROJECT_PATH + "data" + File.separator + "perf" + File.separator + "idx" + File.separator + "idx_Simple";
	
	
	// END APACHE LUCENE configuration//
	
	
	/* BEGIN LUCENE INDEX FIELD NAMES*/
	public static final String REST_API_OPERATION_NAME = "Name";
	
	public static final String REST_API_OPERATION_DESCRIPTION = "Description";
	
	public static final String REST_API_OPERATION_URL = "URL";
	
	public static final String REST_API_OPERATION_REQUEST_ELEMENTS = "REQELE";
	
	public static final String REST_API_OPERATION_REQUEST_EXAMPLE = "REQEXA";
	
	public static final String REST_API_OPERATION_REQUEST_HEADERS = "REQHEAD";
	
	public static final String REST_API_OPERATION_REQUEST_NAME = "REQNAME";
	
	public static final String REST_API_OPERATION_REQUEST_PARAMETERS = "REQPARAM";
	
	public static final String REST_API_OPERATION_REQUEST_SYNTAX = "REQSYN";
	
	public static final String REST_API_OPERATION_RESPONSE_ELEMENTS = "RESELE";
	
	public static final String REST_API_OPERATION_RESPONSE_EXAMPLE = "RESEXA";
	
	public static final String REST_API_OPERATION_RESPONSE_HEADERS = "RESHEAD";
	
	public static final String REST_API_OPERATION_RESPONSE_NAME = "RESNAME";
	
	public static final String REST_API_OPERATION_RESPONSE_SPECIAL_ERRORS = "RESSPEERR";
	
	public static final String REST_API_OPERATION_RESPONSE_SYNTAX = "RESSYN";

	
	/* END LUCENE INDEX FIELD NAMES */
}
