package edu.ncsu.csc.ase.dristi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import edu.ncsu.csc.ase.dristi.logging.MyLoggerFactory;

/**
 * Utility class for File related operations
 * @author rahul_pandita
 *
 */
public class FileUtil {

	private static final Logger logger = MyLoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * Utility function to read the sentences from a file
	 * 
	 * @param file
	 *            : path to the file to be read
	 * @return List of sentences from specified file. In case of exception empty
	 *         list will be returned.
	 */
	public static List<String> readFile(String file) {
		logger.debug("Reading contents from :" + file);
		List<String> retList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				retList.add(line);
			}
		} catch (Exception e) {
			logger.error("Error reading from " + file);
			retList = new ArrayList<String>();
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("Error in closing input stream of " + file);
					retList = new ArrayList<String>();
					e.printStackTrace();
				}
			}
		}
		return retList;
	}
	
	/**
	 * Utility function to write the Objects to persistent storage.
	 * 
	 * @param objList
	 *            List of Objects to be written
	 * @param file
	 *            path to the file
	 */
	public static void writeOperations(List<Object> objList, String file) {
		logger.debug("Writing objects to :" + file);
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fout = new FileOutputStream(file);
			oos = new ObjectOutputStream(fout);
			for (Object obj : objList)
				oos.writeObject(obj);
		} catch (Exception ex) {
			logger.error("Error in writing to " + file);
			ex.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Error in closing output stream of " + file);
			}
		}
	}

	/**
	 * Utility function to read the sentences from a file into a string
	 * 
	 * @param filename
	 *            : path to the file to be read
	 * @return String containing text from specified file. In case of exception empty
	 *         string will be returned.
	 */
	public static String readFiletoString(String filename) {
		StringBuffer sb = new StringBuffer();
		for(String s: readFile(filename))
		{
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
		
	}
	
	/**
	 * Utility function to write small text to a file
	 * @param fileName path to the file to be appended
	 * @param content content to be appended
	 */
	public static void appendStringtoFile(String fileName, String content) {
		try 
		{
			FileWriter writer = new FileWriter(new File(fileName));
			writer.append(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error in writing to the File: " + fileName);
		}
		
	}

}
