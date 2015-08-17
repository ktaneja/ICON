package edu.ncsu.csc.ase.icon.eval;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import edu.ncsu.csc.ase.dristi.cache.SentenceCache;
import edu.ncsu.csc.ase.dristi.util.FileUtilExcel;

/**
 * 
 * @author Rahul Pandita 
 * Created: Jan 23, 2014 6:44:45 PM
 */
public class JavaEVAL extends ICONEval
{
	

	public JavaEVAL() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}





	public void main1() {
		try {
			FileUtilExcel xlsWriter = new FileUtilExcel("senList2");

			FileInputStream fileInputStream = new FileInputStream(
					"senList1.xls");
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			HSSFSheet worksheet = workbook.getSheetAt(2);
			HSSFRow row;
			HSSFCell cellA1, cellB1, cellC1, cellD1, cellE1, cellF1, cellG1, cellH1, cellI1;
			Integer sNo, sen, rMarked, kMarked, finalMarked, iconVal;
			String className, methodName, commentType, comment, constraint;
			
			for (int i = 0; i < 3199; i++) {
				row = worksheet.getRow(i);

				cellA1 = row.getCell(0);
				if (cellA1 == null)
					sNo = 0;
				else
					sNo = ((Double) cellA1.getNumericCellValue()).intValue();

				cellB1 = row.getCell(1);
				if (cellB1 == null)
					className = "";
				else
					className = cellB1.getStringCellValue();

				cellC1 = row.getCell(2);
				if (cellC1 == null)
					methodName = "";
				else
					methodName = cellC1.getStringCellValue();

				cellD1 = row.getCell(3);
				if (cellD1 == null)
					commentType = "";
				else
					commentType = cellD1.getStringCellValue();

				cellE1 = row.getCell(4);
				if (cellE1 == null)
					comment = "";
				else
					comment = cellE1.getStringCellValue();

				cellF1 = row.getCell(5);
				if (cellF1 == null)
					sen = 0;
				else
					sen = ((Double) cellF1.getNumericCellValue()).intValue();

				cellG1 = row.getCell(6);
				if (cellG1 == null)
					rMarked = 0;
				else
					rMarked = ((Double) cellG1.getNumericCellValue())
							.intValue();

				cellH1 = row.getCell(7);
				if (cellH1 == null)
					kMarked = 0;
				else
					kMarked = ((Double) cellH1.getNumericCellValue())
							.intValue();

				cellI1 = row.getCell(8);
				if (cellI1 == null)
					finalMarked = 0;
				else
					finalMarked = ((Double) cellI1.getNumericCellValue())
							.intValue();

				iconVal = 0;
				constraint = "";
				// val=false;
				if ((commentType.trim().equalsIgnoreCase("SUMMARY"))
						|| (commentType.trim().equalsIgnoreCase("EXCEPTION"))) {
					try {
						constraint = test(className, comment);
					} catch (Exception e) {

					}
					iconVal = constraint.length() > 0 ? 1 : 0;
				}

				xlsWriter.writeDataToExcel(sNo, className, methodName,
						commentType, comment, sen, rMarked, kMarked,
						finalMarked, iconVal, constraint);

			}
			xlsWriter.commitXlS();
			SentenceCache.getInstace().updatePersistance();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	
	public void main2() {
		try 
		{
			
			System.out.println(test("RandomAccessFile", "If this file has an associated channel then the channel is closed as well."));
			System.out.println("");
			SentenceCache.getInstace().updatePersistance();
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception {
		JavaEVAL instance = new JavaEVAL();
		instance.main2();
	}

}
