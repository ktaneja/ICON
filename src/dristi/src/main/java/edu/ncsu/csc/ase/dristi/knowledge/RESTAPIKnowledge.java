package edu.ncsu.csc.ase.dristi.knowledge;

/**
 * 
 * @author rahul_pandita
 * 
 */
public class RESTAPIKnowledge {
	public static void addKnowledge() 
	{
		addKnowledgeService();
		addKnowledgeBucket();
		addKnowledgeObject();
	}

	private static void addKnowledgeService() {
		KnowledgeAtom ka = new KnowledgeAtom("Service");
		ka.setSynonyms("Service", "Amazon S3 service", "Amazon S3", "S3");
		ka.setActions("get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeBucket() {
		KnowledgeAtom ka = new KnowledgeAtom("Bucket");
		ka.setSynonyms("Bucket", "cors", "lifecycle", "acl", "policy", "tagging", "acl", "location", "version", "logging", "log", "requestPayment", "website", "Object", "notification", "Multipart Uploads", "Multipart");
		ka.setActions("delete", "get", "head", "list", "put", "initiate");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeObject() {
		KnowledgeAtom ka = new KnowledgeAtom("Object");
		ka.setSynonyms("Object", "Multiple Object", "ACL", "torrent", "restore", "part", "multipart");
		ka.setActions("delete", "get", "head", "list", "put", "initiate", "upload", "complete", "abort");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
}
