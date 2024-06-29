package com.rnd.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class filetest {
	public static String featureFilePath = "C:\\Users\\piyushmu\\git\\AutomationRnD\\src\\test\\resources\\output\\src\\test\\java\\com\\oilrig\\features\\steps\\"
			+ "test.java";

	public static void testfile() {

		try {
			File myObj = new File(featureFilePath);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		write1();
	}
	
	
	public static void write1() {
		try {
			FileWriter myWriter = new FileWriter(featureFilePath, true);
			myWriter.write("Line-1\r\n\n");
			myWriter.append("Line-2\r\n\n");
			myWriter.close();
	
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		write2() ;
	}
	
	public static void write2() {
		try {
			FileWriter myWriter = new FileWriter(featureFilePath, true);
			myWriter.append("Line-4\r\n\n");
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

}
