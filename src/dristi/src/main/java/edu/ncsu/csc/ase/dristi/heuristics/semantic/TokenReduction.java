package edu.ncsu.csc.ase.dristi.heuristics.semantic;

import java.util.LinkedHashMap;
import java.util.Map;

public class TokenReduction {
	private static TokenReduction instance;

	private static Map<String, String> patternMap;
	
	/**
	 * Private Constructor for Singleton Pattern
	 */
	private TokenReduction() {
		populatePatternMap();
	}

	/**
	 * Method to access the singleton object
	 * 
	 * @return Singleton TokenReduction object
	 */
	public static synchronized TokenReduction getInstance() {
		if (instance == null)
			instance = new TokenReduction();
		return instance;
	}
	
	/**
	 * Populates the patternMap
	 */
	private void populatePatternMap() {
		patternMap = new LinkedHashMap<String, String>();
		patternMap.put("GET operation", "GEToperation");
		patternMap.put("DELETE operation", "DELETEoperation");
		patternMap.put("a tag set", "a tagset");
		patternMap.put("the tag set", "the tagset");
		patternMap.put("logging status", "loggingstatus");
		
		patternMap.put("request parameter", "requestparameter");
		patternMap.put("Request Parameters", "RequestParameters");
		
		patternMap.put("acl subresource", "aclsubresource");
		patternMap.put("location subresource", "locationsubresource");
		patternMap.put("logging subresource", "loggingsubresource");
		patternMap.put("notification subresource", "notificationsubresource");
		patternMap.put("policy subresource", "policysubresource");
		patternMap.put("requestPayment subresource", "requestPaymentsubresource");
		patternMap.put("versioning subresource", "versioningsubresource");
		patternMap.put("versions subresource", "versionssubresource");
		patternMap.put("tagging subresource", "taggingsubresource");
		patternMap.put("torrent subresource", "torrentsubresource");
		
		
		patternMap.put("versioning state", "versioningstate");
		patternMap.put("request payment configuration", "requestpaymentconfiguration");
		
		//MISC
		patternMap.put("Multi-Object Delete operation", "MULTIOOBJECTDELETE operation");
		patternMap.put("HTTP request", "HTTPrequest");
		patternMap.put("access control lists (ACL)", "ACLs");
		patternMap.put("access control list (ACL)", "ACL");
		patternMap.put("upload ID", "uploadID");
		patternMap.put("multipart upload", "multipartupload");
		patternMap.put("Amazon S3 Developer Guide", "AmazonS3DeveloperGuide");
		patternMap.put("Amazon Simple Storage Service Developer Guide", "AmazonSimpleStorageServiceDeveloperGuide");
		patternMap.put("Amazon S3", "AmazonS3");
		
		patternMap.put("READ access", "READaccess");
		patternMap.put("WRITE access", "WRITEaccess");
		
		patternMap.put("AWS Access Key ID", "AWSAccessKeyID");
		patternMap.put("object versions", "objectversions");
		patternMap.put("Delete Markers", "DeleteMarkers");
		
		patternMap.put("200 OK", "200OK");
		
		//GENERIC ENGLISH
		patternMap.put("e.g.", "for example");
        patternMap.put("vs.", "versus");
		patternMap.put("up to", "upto");
		
		//ERROR RESPONSES
		patternMap.put("301 Moved Permanently" , "301MovedPermanently");
		patternMap.put("307 Moved Temporarily" , "307MovedTemporarily");
		patternMap.put("400 Bad Request" , "400BadRequest");
		patternMap.put("403 Forbidden" , "403Forbidden");
		patternMap.put("404 Not Found" , "404NotFound");
		patternMap.put("405 Method Not Allowed" , "405MethodNotAllowed");
		patternMap.put("409 Conflict" , "409Conflict");
		patternMap.put("411 Length Required" , "411LengthRequired");
		patternMap.put("412 Precondition Failed" , "412PreconditionFailed");
		patternMap.put("416 Requested Range Not Satisfiable" , "416RequestedRangeNotSatisfiable");
		patternMap.put("500 Internal Server Error" , "500InternalServerError");
		patternMap.put("501 Not Implemented" , "501NotImplemented");
		patternMap.put("503 Service Unavailable" , "503ServiceUnavailable");
		patternMap.put("503 Slow Down" , "503SlowDown");
		
		//PERMISSION LIST
		patternMap.put("s3:PutCORSConfiguration", "s3PutCORSConfiguration");
		patternMap.put("s3:GetLifecycleConfiguration", "s3GetLifecycleConfiguration");
		patternMap.put("s3:GetBucketCORS", "s3GetBucketCORS");
		patternMap.put("s3:PutBucketCORS", "s3PutBucketCORS");
		patternMap.put("s3:PutBucketTagging", "s3PutBucketTagging");
		patternMap.put("s3:PutLifecycleConfiguration", "s3PutLifecycleConfiguration");
		patternMap.put("S3:DeleteBucketWebsite", "S3DeleteBucketWebsite");
		patternMap.put("s3:ReducedRedundancyLostObject", "s3ReducedRedundancyLostObject");
		patternMap.put("s3:GetBucketNotification", "s3GetBucketNotification");
		patternMap.put("s3:GetBucketTagging", "s3GetBucketTagging");
		patternMap.put("S3:GetBucketWebsite", "S3GetBucketWebsite");
		patternMap.put("s3:ReducedRedundancyLostObject", "s3ReducedRedundancyLostObject");
		patternMap.put("s3:PutBucketNotification", "s3PutBucketNotification");
		
		// NAMES OF OPERATIONS
		patternMap.put("GET Service", "GETService");
		patternMap.put("DELETE Bucket", "DELETEBucket");
		patternMap.put("DELETE Bucket cors", "DELETEBucketcors");
		patternMap.put("DELETE Bucket lifecycle", "DELETEBucketlifecycle");
		patternMap.put("DELETE Bucket policy", "DELETEBucketpolicy");
		patternMap.put("DELETE Bucket tagging", "DELETEBuckettagging");
		patternMap.put("DELETE Bucket website", "DELETEBucketwebsite");
		patternMap.put("GET Bucket (List Objects)", "GETBucketListObjects");
		patternMap.put("GET Bucket acl", "GETBucketacl");
		patternMap.put("GET Bucket cors", "GETBucketcors");
		patternMap.put("GET Bucket lifecycle", "GETBucketlifecycle");
		patternMap.put("GET Bucket policy", "GETBucketpolicy");
		patternMap.put("GET Bucket location", "GETBucketlocation");
		patternMap.put("GET Bucket logging", "GETBucketlogging");
		patternMap.put("GET Bucket notification", "GETBucketnotification");
		patternMap.put("GET Bucket tagging", "GETBuckettagging");
		patternMap.put("GET Bucket Object versions", "GETBucketObjectversions");
		patternMap.put("GET Bucket requestPayment", "GETBucketrequestPayment");
		patternMap.put("GET Bucket versioning", "GETBucketversioning");
		patternMap.put("GET Bucket website", "GETBucketwebsite");
		patternMap.put("HEAD Bucket", "HEADBucket");
		patternMap.put("List Multipart Uploads", "ListMultipartUploads");
		patternMap.put("PUT Bucket", "PUTBucket");
		patternMap.put("PUT Bucket acl", "PUTBucketacl");
		patternMap.put("PUT Bucket cors", "PUTBucketcors");
		patternMap.put("PUT Bucket lifecycle", "PUTBucketlifecycle");
		patternMap.put("PUT Bucket policy", "PUTBucketpolicy");
		patternMap.put("PUT Bucket logging", "PUTBucketlogging");
		patternMap.put("PUT Bucket notification", "PUTBucketnotification");
		patternMap.put("PUT Bucket tagging", "PUTBuckettagging");
		patternMap.put("PUT Bucket requestPayment", "PUTBucketrequestPayment");
		patternMap.put("PUT Bucket versioning", "PUTBucketversioning");
		patternMap.put("PUT Bucket website", "PUTBucketwebsite");
		patternMap.put("DELETE Object", "DELETEObject");
		patternMap.put("Delete Multiple Objects", "DeleteMultipleObjects");
		patternMap.put("GET Object", "GETObject");
		patternMap.put("GET Object ACL", "GETObjectACL");
		patternMap.put("GET Object torrent", "GETObjecttorrent");
		patternMap.put("HEAD Object", "HEADObject");
		patternMap.put("OPTIONS object", "OPTIONSobject");
		patternMap.put("POST Object", "POSTObject");
		patternMap.put("POST Object restore", "POSTObjectrestore");
		patternMap.put("PUT Object", "PUTObject");
		patternMap.put("PUT Object acl", "PUTObjectacl");
		patternMap.put("PUT Object - Copy", "PUTObject-Copy");
		patternMap.put("Initiate Multipart Upload", "InitiateMultipartUpload");
		patternMap.put("Upload Part", "UploadPart");
		patternMap.put("Upload Part - Copy", "UploadPartCopy");
		patternMap.put("Complete Multipart Upload", "CompleteMultipartUpload");
		patternMap.put("Abort Multipart Upload", "AbortMultipartUpload");
		patternMap.put("List Parts", "ListParts");
		
		
		// Java Tokens
		patternMap.put("input stream", "inputstream");
		patternMap.put("read()", "readMM");
		patternMap.put("available()", "availableMM");
		patternMap.put("reset()", "resetMM");
		patternMap.put("skip()", "skipMM");
		
		patternMap.put("this object", "thisObject");
		
		patternMap.put("has been", "is");
		patternMap.put("if an I/O error occurs", "ifanIOerroroccurs");
		patternMap.put("an I/O error occurs", "anIOerroroccurs");
		patternMap.put("if some other I/O error occurs", "ifanIOerroroccurs");
		patternMap.put("some other I/O error occurs", "anIOerroroccurs");
		
		patternMap.put("I/O error", "IOerror");
	}
	
	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public String applyReduction(String sentence) {
		for (String phrase : patternMap.keySet()) 
		{
			while(sentence.contains(phrase))
			{
				sentence = sentence.replace(phrase, patternMap.get(phrase));
			}
		}
		return sentence;
	}
	
	public String applyDecoration(String sentence) {
		for (String phrase : patternMap.keySet()) 
		{
			while(sentence.contains(phrase))
			{
				sentence = sentence.replace(phrase,"<font color=\"blue\"><b><u>"+ patternMap.get(phrase) + "</u></b></font>");
			}
		}
		return sentence;
	}
}
