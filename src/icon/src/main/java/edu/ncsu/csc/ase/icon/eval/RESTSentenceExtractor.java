package edu.ncsu.csc.ase.icon.eval;

import java.util.List;

import com.accenture.labs.sa.REST.NLP.AmazonS3RESTAPICrawler;
import com.accenture.labs.sa.REST.NLP.dataStructure.Operation;
import com.accenture.labs.sa.REST.NLP.dataStructure.Request;
import com.accenture.labs.sa.REST.NLP.dataStructure.Response;

import edu.ncsu.csc.ase.dristi.util.FileUtilExcel;

/**
 * THis class extracts a list of sentences from the operations of AmazonS3 REST API  
 * and puts them in a xls sheet.
 * 
 * @author Rahul Pandita
 * Created: Mar 5, 2014 4:33:11 PM
 */
public class RESTSentenceExtractor extends SentenceExtractor 
{
	public static void main(String[] args) 
	{
		//getting the list of operations
		List<Operation> operList = AmazonS3RESTAPICrawler.readOperations();
		try 
		{
			writeXlS(operList);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeXlS(List<Operation> operList) throws Exception 
	{
		FileUtilExcel xlsWriter = new FileUtilExcel("senList1");
		for(Operation operation :operList)
		{
			xlsWriter.writeDataToExcelBold(operation.getName(), "");
			count = count +1;
			writeComments2Xls(xlsWriter, operation);
		}
		xlsWriter.commitXlS();
		System.err.println(count);
	}

	private static void writeComments2Xls(FileUtilExcel xlsWriter, Operation operation) throws Exception
	{
		String temp = "";
		temp = operation.getDescription()==null?"":operation.getDescription();
		writeComment(xlsWriter, temp, "SUMMARY");
		
		Request request = operation.getRequest()==null?new Request():operation.getRequest();
		Response response = operation.getResponse()==null?new Response():operation.getResponse();
		temp = request.getParameters()==null?"":request.getParameters();
		writeComment(xlsWriter, temp, "PARAM");
		temp = request.getHeaders()==null?"":request.getHeaders();
		writeComment(xlsWriter, temp, "PARAM");
		temp = request.getElements()==null?"":request.getElements();
		writeComment(xlsWriter, temp, "PARAM");
		
		temp = response.getHeaders()==null?"":response.getHeaders();
		writeComment(xlsWriter, temp, "RETURN");
		temp = response.getElements()==null?"":response.getElements();
		writeComment(xlsWriter, temp, "RETURN");
		
		temp = response.getSpecialErrors()==null?"":response.getSpecialErrors();
		writeComment(xlsWriter, temp, "EXCEPTION");
		
		
		
	}
}
