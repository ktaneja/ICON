package edu.ncsu.csc.ase.dristi.icon;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class ExcelRow 
{
	private Integer sNo, sen, rMarked, kMarked, finalMarked, iconVal;
	private String className, methodName, commentType, comment, constraint;
	
	public ExcelRow (HSSFRow row)
	{
		parse(row);
		
	}
	
	
	
	private void parse(HSSFRow row) {
		HSSFCell cell = row.getCell(0);
		if (cell == null)
			sNo = 0;
		else
			sNo = ((Double) cell.getNumericCellValue()).intValue();

		cell = row.getCell(1);
		if (cell == null)
			className = "";
		else
			className = cell.getStringCellValue();

		cell = row.getCell(2);
		if (cell == null)
			methodName = "";
		else
			methodName = cell.getStringCellValue();

		cell = row.getCell(3);
		if (cell == null)
			commentType = "";
		else
			commentType = cell.getStringCellValue();

		cell = row.getCell(4);
		if (cell == null)
			comment = "";
		else
			comment = cell.getStringCellValue();

		cell = row.getCell(5);
		if (cell == null)
			sen = 0;
		else
			sen = ((Double) cell.getNumericCellValue()).intValue();

		cell = row.getCell(6);
		if (cell == null)
			rMarked = 0;
		else
			rMarked = ((Double) cell.getNumericCellValue()).intValue();

		cell = row.getCell(7);
		if (cell == null)
			kMarked = 0;
		else
			kMarked = ((Double) cell.getNumericCellValue())
					.intValue();

		cell = row.getCell(8);
		if (cell == null)
			finalMarked = 0;
		else
			finalMarked = ((Double) cell.getNumericCellValue()).intValue();

		iconVal = 0;
		constraint = "";
		
	}



	public Integer getsNo() {
		return sNo;
	}
	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}
	public Integer getSen() {
		return sen;
	}
	public void setSen(Integer sen) {
		this.sen = sen;
	}
	public Integer getrMarked() {
		return rMarked;
	}
	public void setrMarked(Integer rMarked) {
		this.rMarked = rMarked;
	}
	public Integer getkMarked() {
		return kMarked;
	}
	public void setkMarked(Integer kMarked) {
		this.kMarked = kMarked;
	}
	public Integer getFinalMarked() {
		return finalMarked;
	}
	public void setFinalMarked(Integer finalMarked) {
		this.finalMarked = finalMarked;
	}
	public Integer getIconVal() {
		return iconVal;
	}
	public void setIconVal(Integer iconVal) {
		this.iconVal = iconVal;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getCommentType() {
		return commentType;
	}
	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getConstraint() {
		return constraint;
	}
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	
	
}
