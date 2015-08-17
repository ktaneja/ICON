package edu.ncsu.csc.ase.dristi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class ConsoleUtil 
{
	
	private static final InputStreamReader isr = new InputStreamReader(System.in);
	private static final BufferedReader br = new BufferedReader(isr);
	
	
	public static String readConsole(String message)
	{
		try 
		{
			System.out.println(message);
			return br.readLine();
			
			 
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static int readConsole(String message, int rangeLow, int rangeHigh)
	{
		int retVal = Integer.MIN_VALUE;
		try 
		{
			System.out.println(message);
			String val =  br.readLine();
			try
			{
				retVal = Integer.parseInt(val);
				if(retVal < rangeLow || retVal > rangeHigh)
					throw new NumberFormatException();
			}
			catch(NumberFormatException ex)
			{
				System.out.println("Wrong Choice, Try Again");
				retVal = readConsole(message, rangeLow, rangeHigh);
			}
			 
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	public static void readConsoleWhile(Object object, Method method, String message) throws Exception 
	{
		try 
		{
			
			System.out.println(message);
			Object[] parameters = new Object[1];
	        parameters[0] = br.readLine();
	        while(((String)parameters[0]).length()>0)
	        {
	        	method.invoke(object, parameters);
	        	System.out.println(message);
				parameters[0] = br.readLine();
	        }
	        
			
			 
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
}
