package edu.ncsu.csc.ase.icon.eval;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility Class for reading data from the Excel Sheets
 * @author Rahul Pandita
 * 
 */
public class SentenceReader {
	
	public static final String DEFAULT_FILE_LOC = "data"+ File.separator + "senList.xlsx";
	
	private String FILE_LOC;
	private int sheet;
	
	public SentenceReader() {
		this.FILE_LOC = DEFAULT_FILE_LOC;
		this.sheet = 0;
	}
	
	public SentenceReader(String file)
	{
		this.FILE_LOC = file;
		this.sheet = 0;
	}
	
	public SentenceReader(String file, int sheet)
	{
		this.FILE_LOC = file;
		this.sheet = sheet;
	}
	
	
	public static void main(String[] args) {
		SentenceReader sr = new SentenceReader(SentenceReader.DEFAULT_FILE_LOC, 1);
		System.out.println(sr.readData().size());
	}
	
	public List<ExcelRow> readData()
	{
		List<ExcelRow> returnVal = new ArrayList<>();
		try
		{
			FileInputStream fileInputStream = new FileInputStream(FILE_LOC);
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet worksheet = workbook.getSheetAt(sheet);
			Row row;
			ExcelRow dataRow;
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				row = worksheet.getRow(i);
				dataRow = new ExcelRow(row);
				returnVal.add(dataRow);
			}
			fileInputStream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnVal;
	}
	
	public List<ExcelRow> readSentences()
	{
		List<ExcelRow> returnVal = new ArrayList<>();
		try
		{
			FileInputStream fileInputStream = new FileInputStream(FILE_LOC);
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet worksheet = workbook.getSheetAt(sheet);
			Row row;
			ExcelRow dataRow;
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				row = worksheet.getRow(i);
				dataRow = new ExcelRow(row);
				if (dataRow.getSen()==1)
					returnVal.add(dataRow);
			}
			fileInputStream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnVal;
	}
	
	public List<ExcelRow> readTemporalSentences()
	{
		List<ExcelRow> returnVal = new ArrayList<>();
		try
		{
			FileInputStream fileInputStream = new FileInputStream(FILE_LOC);
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet worksheet = workbook.getSheetAt(sheet);
			Row row;
			ExcelRow dataRow;
			for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
				row = worksheet.getRow(i);
				dataRow = new ExcelRow(row);
				if (dataRow.getSen()==1 && dataRow.getFinalMarked()==1)
					returnVal.add(dataRow);
			}
			fileInputStream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnVal;
	}
}
