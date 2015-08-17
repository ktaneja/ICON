package edu.ncsu.csc.ase.dristi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.ncsu.csc.ase.dristi.test.Main;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Main m = Main.getInstance();

		try {
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			System.out.println("Enter The Sentnce =>");
			String s = br.readLine();
			while (s.length() > 0) {

				System.out.println(m.parse(s));
				System.out.println("Enter The Sentnce =>");

				s = br.readLine();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
