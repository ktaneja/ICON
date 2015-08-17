package com.accenture.labs.sa.REST.NLP;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.accenture.labs.sa.REST.NLP.dataStructure.Example;
import com.accenture.labs.sa.REST.NLP.dataStructure.Message;
import com.accenture.labs.sa.REST.NLP.dataStructure.Operation;
import com.accenture.labs.sa.REST.NLP.dataStructure.Request;
import com.accenture.labs.sa.REST.NLP.dataStructure.Response;

import edu.ncsu.csc.ase.dristi.configuration.Config;
import edu.ncsu.csc.ase.dristi.util.StringUtil;

/**
 * A Utility Class to Crawl Elements of Amazon S3 REST API from WEB-page
 * @author rahul_pandita
 *
 */
public class AmazonS3RESTAPICrawler {

	/**
	 * ENUM to hold Common Chapter Titles in AMAZON S3 REST API web-pages
	 * @author rahul_pandita
	 *
	 */
	public enum ChapterTitles {

		Description,
		Requests,
		Syntax,
		RequestParameters,
		RequestHeaders,
		RequestElements,
		Responses,
		ResponseHeaders,
		ResponseElements,
		SpecialErrors,
		Examples,
		SampleRequest,
		SampleResponse,
		RelatedResources,
		RelatedActions,
		unknown
		
	}
	
	/**
	 * Main method to crawl an Amazon S3 REST API web-page
	 * @param url string representing the URL of the web-page
	 * @return A populated object OPeration corresponding to the the URL
	 */
	public static Operation crawler(String url)
	{
		System.err.println("Crawling -> " + url);
		Operation o = new Operation();
		o.setUrl(url);
		try {
			Document doc = Jsoup.connect(url).get();
			
			// Get the right div
			Element e = doc.getElementById("divRight");
			// Get the main Content div
			e = e.getElementById("divContent");
			for(Element child: e.children())
			{
				if(child.hasClass("section"))
				{
					for(Element c: child.children())
					{
						if(c.hasClass("titlepage"))
							o.setName(c.text().trim());
						else if(c.hasClass("section"))
						{
							
							ChapterTitles ch = getTitle(c);
							System.err.println(ch.toString());
							switch (ch) {
							case Description:
								crawlDescription(o, c);
								break;
							case Requests:
								crawlRequest(o,c);
								break;
							case Responses:
								crawlResponse(o,c);
								break;
							case Examples:
								crawlExample(o,c);
								break;
							case RelatedResources:
								o.setLinkURL(getLinks(c));
								break;
							case RelatedActions:
								o.setLinkURL(getLinks(c));
								break;
							case unknown:
								System.err.println(c);
								break;
							default:
								System.err.println(c.baseUri());
								break;
							}
							//crawlSection(o, c);
						}
					}
					return o;
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return o;
	}
	
	/**
	 * Parser function to parse the Example section 
	 * @param operation THe object where the parsed content will be stored
	 * @param element the HTML element that needs to be parsed
	 */
	private static void crawlExample(Operation operation, Element element) {
		Example example = new Example();
		for(Element child: element.children())
		{
			if(child.hasClass("section"))
			{
				ChapterTitles ch = getTitle(child);
				switch (ch) 
				{
					case SampleRequest:
						example.setRequest(getTextContent(child));
						break;
					case SampleResponse:
						example.setResponse(getTextContent(child));
						break;
					default:
						break;
				}
			}
		}
		example.setUrl(element.baseUri());
		example.setLinkURL(getLinks(element));
		operation.setExample(example);
		
	}

	/**
	 * Parser function to extract Chapter Title from the given HTML element
	 * @param element HTML element from which the title is to be extracted
	 * @return Appropriate ENUM of type ChapterTitles
	 */
	private static ChapterTitles getTitle(Element element) {
		StringBuffer buff = new StringBuffer();
		for(Element child: element.children())
		{
			if(child.hasClass("titlepage"))
			{
				buff.append(child.text());
			}
		}
		ChapterTitles retValue = ChapterTitles.unknown;
		try{
			retValue = ChapterTitles.valueOf(StringUtil.removeSpaces(buff.toString().trim()));
		}
		catch(Exception e)
		{
			System.err.println("Unresolved: -> " + StringUtil.removeSpaces(buff.toString().trim()));
			//e.printStackTrace();
		}
		return retValue;
	}
	
	/**
	 * Get the description out of the HTML element. ALso extracts the HTML Links from the description to be added as references
	 * @param operation the object in which the description is to be stored
	 * @param element HTML element from which the description is extracted
	 */
	private static void crawlDescription(Operation operation, Element element) {
		operation.setDescription(getTextContent(element));
		operation.setLinkURL(getLinks(element));
	}

	/**
	 * Extracts the text content from the provided HTML element
	 * @param element the HTML element from which the text is to be extracted
	 * @return Text Content from the the HTML element
	 */
	private static String getTextContent(Element element)
	{
		StringBuffer buff = new StringBuffer();
		
		for(Element child: element.children())
		{
			if(!child.hasClass("titlepage"))
			{
				buff.append(child.text());
			}
		}
		return buff.toString();
	}
	
	/**
	 * Parser function to parse the Request section 
	 * @param operation THe object where the parsed content will be stored
	 * @param element the HTML element that needs to be parsed
	 */
	private static void crawlRequest(Operation operation, Element element) 
	{
		Request req = new Request();
		for(Element child: element.children())
		{
			if(child.hasClass("section"))
			{
				ChapterTitles ch = getTitle(child);
				switch (ch) 
				{
					case Syntax:
						req.setSyntax(getTextContent(child));
						break;
					case RequestParameters:
						crawlReqParam(req,child);
						break;
					case RequestHeaders:
						crawlHeaders(req, child);
						break;
					case RequestElements:
						crawlElements(req, child);
						break;
					default:
						break;
				}
			}
		}
		req.setUrl(element.baseUri());
		operation.setRequest(req);
	}
	
	/**
	 * Parser function to parse the Response section 
	 * @param operation THe object where the parsed content will be stored
	 * @param element the HTML element that needs to be parsed
	 */
	private static void crawlResponse(Operation operation, Element element) 
	{
		Response res = new Response();
		for(Element child: element.children())
		{
			if(child.hasClass("section"))
			{
				ChapterTitles ch = getTitle(child);
				switch (ch) 
				{
					case ResponseHeaders:
						crawlHeaders(res, child);
						break;
					case ResponseElements:
						crawlElements(res, child);
						break;
					case SpecialErrors:
						crawlResErrors(res,child);
						break;
					default:
						int i =0;
						System.out.println(10/i);
						break;
				}
			}
		}
		res.setUrl(element.baseUri());
		operation.setResponse(res);
	}
	
	/**
	 * Parser function to parse the Errors section 
	 * @param req The Request object where the parsed content will be stored
	 * @param element the HTML element that needs to be parsed
	 */
	private static void crawlResErrors(Response req, Element element) {
		//TODO Crawl table handle differently
		req.setSpecialErrors(getTextContent(element));
		req.setLinkURL(getLinks(element));
	}
	
	/**
	 * Parser function to parse the Headers section 
	 * @param req The Request object where the parsed content will be stored
	 * @param element the HTML element that needs to be parsed
	 */
	private static void crawlHeaders(Message req, Element element) {
		//TODO Crawl table handle differently
		req.setHeaders(getTextContent(element));
		req.setLinkURL(getLinks(element));
	}
	
	/**
	 * Parser function to parse the Message section 
	 * @param message The Message object where the parsed content will be stored
	 * @param element the HTML element that needs to be parsed
	 */
	private static void crawlElements(Message message, Element element) {
		//TODO Crawl table handle differently
		message.setElements(getTextContent(element));
		message.setLinkURL(getLinks(element));
	}
	
	/**
	 * Parser function to parse the Request Parameter section 
	 * @param req The Request object where the parsed content will be stored
	 * @param element the HTML element that needs to be parsed
	 */
	private static void crawlReqParam(Request req, Element element) {
		//TODO Crawl table handle differently
		req.setParameters(getTextContent(element));
		req.setLinkURL(getLinks(element));
		
	}
	
	/**
	 * Extracts all the HTML url's from the provided HTML element
	 * @param element the HTML Element from which the url's are to be extracted
	 * @return A list of String representing the url's embedded in the element
	 */
	private static List<String> getLinks(Element element) {
		Elements links = element.select("a[href]");
		ArrayList<String> linkList = new ArrayList<String>();
		for (Element link : links)
		{
			linkList.add(link.attr("abs:href"));
		}
		return linkList;
	}
	
	/**
	 * main function for testing purpose to be removed from the the final version 
	 * @param args
	 */
	public static void main(String[] args) {
		writeOperations();
		//System.out.println(getOperations().size());
		readOperations();
	}
	
	/**
	 * Utility function to write the Operations crawled from the web documents of REST API.
	 */
	public static void writeOperations() {
		ArrayList<Operation> operList = getOperations();
		ObjectOutputStream oos = null;
		try{
			 
			FileOutputStream fout = new FileOutputStream(Config.AMAZONS3_PATH);
			oos = new ObjectOutputStream(fout);   
			for(Operation oper:operList)
				oos.writeObject(oper);
			
			
			System.out.println("Done");
	 
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }finally
		   {
			   try {
	                if (oos != null) {
	                	oos.close();
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
		   }
		System.err.println("Wrote Operations ->" + operList.size());
	}
	
	/**
	 * Utility function to read the Operations of REST API previously written into persistent storage
	 * @return List of Operations crawled from the web documents of REST API.
	 */
	public static ArrayList<Operation> readOperations() 
	{
		ArrayList<Operation> operList = new ArrayList<Operation>();
		ObjectInputStream ois = null;
		try{
			 
			FileInputStream fin = new FileInputStream(Config.AMAZONS3_PATH);
			ois = new ObjectInputStream(fin);   
			Object obj = null;
            
            while ((obj = ois.readObject()) != null) {
                
                if (obj instanceof Operation) {
                
                    operList.add((Operation)obj);
                }
                
            }
			//	oos.writeObject(oper);
			ois.close();
			System.out.println("Done");
	 
		   }catch (EOFException e){
			   System.out.println("EOF Reached!");
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }finally
		   {
			   try {
	                if (ois != null) {
	                	ois.close();
	                }
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
		   }
		System.err.println("Read Operations ->" + operList.size());
		return operList;
	}
	
	
	/**
	 * Utility function to read the Operations from the web documents of REST API
	 * @return List of Operations crawled from the web documents of REST API.
	 */
	public static ArrayList<Operation> getOperations() {
		ArrayList<Operation> operList = new ArrayList<Operation>();
		
		//Operation on Service
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTServiceGET.html"));
		
		//Operation on Buckets
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETE.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEcors.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETElifecycle.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEpolicy.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEtagging.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketDELETEwebsite.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETacl.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETcors.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlifecycle.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETpolicy.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlocation.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETlogging.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETnotification.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETtagging.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETVersion.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentGET.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETversioningStatus.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGETwebsite.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketHEAD.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListMPUpload.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUT.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTacl.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTcors.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlifecycle.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTpolicy.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTlogging.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTnotification.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTtagging.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTrequestPaymentPUT.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTVersioningStatus.html"));
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketPUTwebsite.html"));
		
		//Operations on Objects
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectDELETE.html"));//DELETE Object
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/multiobjectdeleteapi.html"));//Delete Multiple Objects
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGET.html"));//GET Object
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETacl.html"));//GET Object ACL
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectGETtorrent.html"));//GET Object torrent
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectHEAD.html"));//HEAD Object
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTOPTIONSobject.html"));//OPTIONS object
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOST.html"));//POST Object
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPOSTrestore.html"));//POST Object restore
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUT.html"));//PUT Object
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectPUTacl.html"));//PUT Object acl
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/RESTObjectCOPY.html"));//PUT Object - Copy
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadInitiate.html"));//Initiate Multipart Upload
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPart.html"));//Upload Part
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadUploadPartCopy.html"));//Upload Part - Copy
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadComplete.html"));//Complete Multipart Upload
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadAbort.html"));//Abort Multipart Upload
		operList.add(crawler("http://docs.aws.amazon.com/AmazonS3/latest/API/mpUploadListParts.html"));//List Parts
		
		return operList;
	}
}
