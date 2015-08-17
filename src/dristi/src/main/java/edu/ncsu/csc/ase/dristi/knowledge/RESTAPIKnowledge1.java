package edu.ncsu.csc.ase.dristi.knowledge;

/**
 * 
 * @author Rahul Pandita
 * Created: Mar 7, 2014 7:37:05 PM
 */
public class RESTAPIKnowledge1 
{
	
	public static void addKnowledge() 
	{
		addKnowledgeService();
		addKnowledgeBucketcors();
		addKnowledgeBucketLifecycle();
		addKnowledgeBucketPolicy();
		addKnowledgeBucketTagging();
		addKnowledgeBucketWebsite();
		addKnowledgeBucketacl();
		addKnowledgeBucketlogging();
		addKnowledgeBucketlocation();
		addKnowledgeBucketNotification();
		addKnowledgeBucketRequestPayment();
		addKnowledgeBucketVersion();
		addKnowledgeBucket();
		addKnowledgeObject();
		addKnowledgeMultipleObject();
		addKnowledgeObjectacl();
		addKnowledgeObjecttorrent();
		addKnowledgeObjectRestore();
		addKnowledgeMultipartUpload();
		addKnowledgePart();
	}

	
	private static void addKnowledgeService() {
		KnowledgeAtom ka = new KnowledgeAtom("Service");
		ka.setSynonyms("Service", "Amazon S3 service", "Amazon S3", "S3");
		ka.setActions("return");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeBucketcors() {
		KnowledgeAtom ka = new KnowledgeAtom("cors");
		ka.setSynonyms("bucket cors", "cors");
		ka.setActions("delete", "return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}


	private static void addKnowledgeBucketLifecycle() {
		KnowledgeAtom ka = new KnowledgeAtom("lifecycle");
		ka.setSynonyms("bucket lifecycle", "lifecycle");
		ka.setActions("delete", "return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketPolicy() {
		KnowledgeAtom ka = new KnowledgeAtom("policy");
		ka.setSynonyms("bucket policy", "policy");
		ka.setActions("delete", "return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketTagging() {
		KnowledgeAtom ka = new KnowledgeAtom("tagging");
		ka.setSynonyms("bucket tagging", "tagging", "bucket tag", "tag");
		ka.setActions("delete", "return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketWebsite() {
		KnowledgeAtom ka = new KnowledgeAtom("website");
		ka.setSynonyms("bucket website", "website");
		ka.setActions("delete", "return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketacl() {
		KnowledgeAtom ka = new KnowledgeAtom("acl");
		ka.setSynonyms("bucket acl", "acl");
		ka.setActions("return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketlogging() {
		KnowledgeAtom ka = new KnowledgeAtom("logging");
		ka.setSynonyms("bucket logging", "bucket log", "logging", "log");
		ka.setActions("return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketlocation() {
		KnowledgeAtom ka = new KnowledgeAtom("location");
		ka.setSynonyms("bucket location", "location");
		ka.setActions("return");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketNotification() {
		KnowledgeAtom ka = new KnowledgeAtom("notification");
		ka.setSynonyms("bucket notification", "notification");
		ka.setActions("return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucketRequestPayment() {
		KnowledgeAtom ka = new KnowledgeAtom("requestPayment");
		ka.setSynonyms("bucket requestpayment", "requestpayment", "request payment", "payment", "bucket payment");
		ka.setActions("return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeBucketVersion() {
		KnowledgeAtom ka = new KnowledgeAtom("version");
		ka.setSynonyms("bucket version", "version");
		ka.setActions("return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeBucket() {
		KnowledgeAtom ka = new KnowledgeAtom("bucket");
		ka.setSynonyms("bucket");
		ka.setActions("delete", "return", "put", "exist");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeObject() {
		KnowledgeAtom ka = new KnowledgeAtom("object");
		ka.setSynonyms("object");
		ka.setActions("delete", "return", "put", "exist", "options", "post", "copy");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeMultipleObject() {
		KnowledgeAtom ka = new KnowledgeAtom("multipleobject");
		ka.setSynonyms("multipleobject", "multiple object");
		ka.setActions("delete");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeObjectacl() {
		KnowledgeAtom ka = new KnowledgeAtom("ObjectAcl");
		ka.setSynonyms("object acl", "acl");
		ka.setActions("return", "put");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeObjecttorrent() {
		KnowledgeAtom ka = new KnowledgeAtom("ObjectTorrent");
		ka.setSynonyms("object torrent", "torrent");
		ka.setActions("return");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgeObjectRestore() {
		KnowledgeAtom ka = new KnowledgeAtom("ObjectRestore");
		ka.setSynonyms("object restore", "restore");
		ka.setActions("post");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeMultipartUpload() {
		KnowledgeAtom ka = new KnowledgeAtom("MultipartUpload");
		ka.setSynonyms("MultipartUpload", "multipart upload", "multipart");
		ka.setActions("initiate", "list", "complete", "abort");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
	
	private static void addKnowledgePart() {
		KnowledgeAtom ka = new KnowledgeAtom("Part");
		ka.setSynonyms("part");
		ka.setActions("upload", "copy", "list");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
}
