package edu.ncsu.csc.ase.dristi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This is a utility class to write data to xls sheet
 * 
 * @author rpandit
 *
 * @date Jan 17, 2014, 1:30:09 PM
 */
public class FileUtilExcel 
{

	private static final String XLS_Extention= ".xls";
	
	private String fileName;
	
	private HSSFWorkbook myWorkBook;
	
	private HSSFSheet mySheet;
	
	private boolean comittedState;
	
	private HSSFCellStyle red_italic;
	private HSSFCellStyle red_bold;
	
	private HSSFCellStyle italic;
	private HSSFCellStyle bold;
	
	
	public FileUtilExcel(String fileName) throws Exception
	{
		fileName = sanitizeExtention(fileName);
		this.fileName = fileName;
		this.myWorkBook = new HSSFWorkbook(new FileInputStream(new File(this.fileName)));
		this.mySheet = myWorkBook.getSheetAt(2);
		this.comittedState = false;
		
		HSSFFont font = myWorkBook.createFont();
		font.setItalic(true);
		font.setColor(HSSFFont.COLOR_RED);
		this.red_italic = myWorkBook.createCellStyle();
		this.red_italic.setFont(font);
		
		font = myWorkBook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFFont.COLOR_RED);
		this.red_bold = myWorkBook.createCellStyle();
		this.red_bold.setFont(font);
		
		font = myWorkBook.createFont();
		font.setItalic(true);
		this.italic = myWorkBook.createCellStyle();
		this.italic.setFont(font);
		
		font = myWorkBook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		this.bold = myWorkBook.createCellStyle();
		this.bold.setFont(font);
	}
	
	
	
	private void checkState() throws Exception
	{
		if(comittedState)
			throw new IOException("The file is already closed.");
	}

	private String sanitizeExtention(String fileName) {
		if(!fileName.endsWith(FileUtilExcel.XLS_Extention))
			fileName = fileName.concat(FileUtilExcel.XLS_Extention);
		return fileName;
	}
	
	//Test Method to be removed from production environment
	public static void main(String[] args) throws Exception
	{
		/** Name of excel file that we are going to create **/
		
		//FileUtilExcel obj = new FileUtilExcel("test");
		
	}

	public void writeDataToExcel(String c1, String c2) throws Exception
	{
		writeDataToExcel(c1, c2, null);
	}
	
	private void writeDataToExcel(String c1, String c2, HSSFCellStyle style) throws Exception
	{
		checkState();
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
				if(style != null)
					myCell.setCellStyle(style);
				
				myCell.setCellValue(excelData[rowNum][cellNum]);

			}
		}
	}
	
	/**
	 * 
	 * @param c1
	 * @param c2
	 * @throws Exception
	 */
	public void writeDataToExcelRedItalic(String c1, String c2) throws Exception
	{
		
		writeDataToExcel(c1, c2, red_italic);
	}
	
	/**
	 * 
	 * @param c1
	 * @param c2
	 * @throws Exception
	 */
	public void writeDataToExcelItalic(String c1, String c2) throws Exception
	{
		
		writeDataToExcel(c1, c2, italic);
	}
	
	public void writeDataToExcelRedBold(String c1, String c2) throws Exception
	{
		writeDataToExcel(c1, c2, red_bold);
	}
	
	public void writeDataToExcelBold(String c1, String c2) throws Exception
	{
		writeDataToExcel(c1, c2, bold);
	}
	
	
	public void writeDataToExcel(String ... cell) throws Exception
	{
		checkState();
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
	
	public void writeDataToExcel(Object ... cell) throws Exception
	{
		checkState();
		HSSFRow myRow = null;
		HSSFCell myCell = null;
		int appendRowNum =  mySheet.getLastRowNum()+ 1; 
		myRow = mySheet.createRow(appendRowNum);
		for (int cellNum = 0; cellNum < cell.length; cellNum++) 
		{
			myCell = myRow.createCell(cellNum);
			if(cell[cellNum] instanceof Integer)
				myCell.setCellValue(Double.valueOf((Integer)cell[cellNum]));
			else if(cell[cellNum] instanceof Double)
				myCell.setCellValue((Double)cell[cellNum]);
			else if(cell[cellNum] instanceof String)
				myCell.setCellValue((String)cell[cellNum]);
			else if(cell[cellNum] instanceof Boolean)
				myCell.setCellValue((Boolean)cell[cellNum]);
			else if(cell[cellNum] instanceof Date)
				myCell.setCellValue((Date)cell[cellNum]);
			else
				myCell.setCellValue(cell[cellNum].toString());
				
		}
	}
	
	/**
	 * This method must be called finally.
	 * Subsequent invocations of any other method (including this) will result in an Exception
	 * @throws Exception
	 */
	public void commitXlS() throws Exception
	{
		checkState();
		FileOutputStream out = new FileOutputStream(fileName);
		myWorkBook.write(out);
		out.close();
	}
	
}

