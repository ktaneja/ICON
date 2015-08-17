package edu.ncsu.csc.ase.dristi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

//TODO shift to newer version
public class FileUtilExcelOld {

	public static final String fileName = "results.xls";
	
	private static HSSFWorkbook myWorkBook;
	private static HSSFSheet mySheet;
	private static FileUtilExcelOld instance;
	
	public static synchronized FileUtilExcelOld getInstance() throws Exception {
		if(instance == null)
		{
			instance = new FileUtilExcelOld();
		}
		return instance;
	}
	
	private FileUtilExcelOld() throws Exception
	{
		 myWorkBook = new HSSFWorkbook(new FileInputStream(new File(fileName)));
		 mySheet = myWorkBook.getSheetAt(0);
	}
	public static void main(String[] args) throws Exception
	{
		/** Name of excel file that we are going to create **/
		
		getInstance().writeDataToExcelFile(fileName);
	}

	/** This method writes data to new excel file **/
	private void writeDataToExcelFile(String fileName) throws Exception
	{
		String[][] excelData = new String[1][2];
		excelData[0][0] = "c1";
		excelData[0][1] = "c2";
		
		HSSFWorkbook myWorkBook = new HSSFWorkbook(new FileInputStream(new File(fileName)));
		HSSFSheet mySheet = myWorkBook.getSheetAt(0);
		HSSFRow myRow = null;
		HSSFCell myCell = null;

		int appendRowNum =  mySheet.getLastRowNum()+ 1; 
		for (int rowNum = 0; rowNum < excelData.length; rowNum++) 
		{
			myRow = mySheet.createRow(rowNum + appendRowNum);

			for (int cellNum = 0; cellNum < 2; cellNum++) 
			{
				myCell = myRow.createCell(cellNum);
				myCell.setCellValue(excelData[rowNum][cellNum]);

			}
		}

		try 
		{
			FileOutputStream out = new FileOutputStream(fileName);
			myWorkBook.write(out);
			out.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
	
	public void writeDataToExcel(String c1, String c2) throws Exception
	{
		String[][] excelData = new String[1][2];
		excelData[0][0] = c1;
		excelData[0][1] = c2;
		HSSFRow myRow = null;
		HSSFCell myCell = null;

		int appendRowNum =  mySheet.getLastRowNum()+ 1; 
		for (int rowNum = 0; rowNum < excelData.length; rowNum++) 
		{
			myRow = mySheet.createRow(rowNum + appendRowNum);

			for (int cellNum = 0; cellNum < 2; cellNum++) 
			{
				myCell = myRow.createCell(cellNum);
				myCell.setCellValue(excelData[rowNum][cellNum]);

			}
		}
	}
	
	public void writeDataToExcelItalic(String c1, String c2) throws Exception
	{
		String[][] excelData = new String[1][2];
		excelData[0][0] = c1;
		excelData[0][1] = c2;
		HSSFRow myRow = null;
		HSSFCell myCell = null;
		
		HSSFFont font = myWorkBook.createFont();
		font.setItalic(true);
		font.setColor(HSSFFont.COLOR_RED);
		HSSFCellStyle style = myWorkBook.createCellStyle();
		style.setFont(font);

		int appendRowNum =  mySheet.getLastRowNum()+ 1; 
		for (int rowNum = 0; rowNum < excelData.length; rowNum++) 
		{
			myRow = mySheet.createRow(rowNum + appendRowNum);

			for (int cellNum = 0; cellNum < 2; cellNum++) 
			{
				myCell = myRow.createCell(cellNum);
				myCell.setCellStyle(style);
				myCell.setCellValue(excelData[rowNum][cellNum]);

			}
		}
		
	}
	
	public void writeDataToExcelBold(String c1, String c2) throws Exception
	{
		String[][] excelData = new String[1][2];
		excelData[0][0] = c1;
		excelData[0][1] = c2;
		HSSFRow myRow = null;
		HSSFCell myCell = null;
		
		HSSFFont font = myWorkBook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = myWorkBook.createCellStyle();
		style.setFont(font);

		int appendRowNum =  mySheet.getLastRowNum()+ 1; 
		for (int rowNum = 0; rowNum < excelData.length; rowNum++) 
		{
			myRow = mySheet.createRow(rowNum + appendRowNum);

			for (int cellNum = 0; cellNum < 2; cellNum++) 
			{
				myCell = myRow.createCell(cellNum);
				myCell.setCellStyle(style);
				myCell.setCellValue(excelData[rowNum][cellNum]);

			}
		}
		
	}
	
	public void writeDataToExcel(String ... cell) throws Exception
	{
		HSSFRow myRow = null;
		HSSFCell myCell = null;
		
		int appendRowNum =  mySheet.getLastRowNum()+ 1; 
		myRow = mySheet.createRow(appendRowNum);
		for (int cellNum = 0; cellNum < cell.length; cellNum++) 
		{
			myCell = myRow.createCell(cellNum);
			myCell.setCellValue(cell[cellNum]);
		}
		
	}
	public void commitXlS() throws Exception
	{
		FileOutputStream out = new FileOutputStream(fileName);
		myWorkBook.write(out);
		out.close();
	}
	
}

