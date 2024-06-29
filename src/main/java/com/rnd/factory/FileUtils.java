package com.rnd.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import com.rnd.mustache.EmailTemplate;
import com.rnd.utility.ConstantsPaths;

public class FileUtils {
	public static String featureFilePath;
	public static String stepDefinitionClassFilePath;
	public static String actionClassFilePath;
	public static ArrayList<String> methodNameList = new ArrayList<String>();
	public static String actionClassName;
	public static String pojoClassPath;
	
	
	
	public static void createFeatureFile(String pojoClassName, String apiMethod, Map<String, Boolean> allKeys) {
		String featureFileDirPath = ConstantsPaths.FEATURE_FILE_DIR_PATH;
		featureFilePath = featureFileDirPath + pojoClassName + ".feature";

		try {
			File myObj = new File(featureFilePath);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		

			EmailTemplate emailTemplate = new EmailTemplate();
			String featureContent = emailTemplate.getFeatureTemplate(pojoClassName, apiMethod, allKeys);
			
			FileWriter myWriter = new FileWriter(featureFilePath, false);
			myWriter.write(featureContent);
			myWriter.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static  void createStepDefinitonFile(String pojoClassName, String apiMethod) {
		String classFileDirPath = ConstantsPaths.STEP_DEFINITION_CLASS_DIR_PATH;
		String stepDefinitionClassName = pojoClassName + "Steps";
		stepDefinitionClassFilePath = classFileDirPath + stepDefinitionClassName + ".java";
		
		try {
			File myObj = new File(stepDefinitionClassFilePath);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
			
			EmailTemplate emailTemplate = new EmailTemplate();
			String stepDefinitionContent = emailTemplate.getStepDefinitionTemplate(stepDefinitionClassName, pojoClassName, apiMethod);
			
			FileWriter myWriter = new FileWriter(stepDefinitionClassFilePath, false);
			myWriter.write(stepDefinitionContent);
			myWriter.close();
			
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}	
	}
	

	static public void createActionFile(String pojoClassName, String apiMethod, Map<String, Boolean> allKeys) {
		String classFileDirPath = ConstantsPaths.ACTION_CLASS_DIR_PATH;
		String actionClassName = pojoClassName + "Actions";
		actionClassFilePath = classFileDirPath + actionClassName + ".java";
		
		try {
			File myObj = new File(actionClassFilePath);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
			
			EmailTemplate emailTemplate = new EmailTemplate();
			String ActionContent = emailTemplate.getActionTemplate(actionClassName, pojoClassName, apiMethod, allKeys);
			
			FileWriter myWriter = new FileWriter(actionClassFilePath, false);
			myWriter.write(ActionContent);
			myWriter.close();
			
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}	
	}
	public static String getPOJOClassName(String filePath) {
		String line = "";
		String POJOClassName = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));

			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("public class ")) {
					POJOClassName = line.trim().replace("public class ", "").replace("{", "").trim();
					break;
				}

			}
			br.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return POJOClassName;

	}
	
	public static String getMethodName(String filePath, String key) {
		String line = "";
		String methodName = "";
		int counter = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));

			while (((line = br.readLine()) != null) && (counter <= 3)) {
				if (line.trim().contains(key) || (counter > 0)) {
					counter++;
				}
				if (counter == 3) {
					methodName = "set" + line.trim().subSequence(line.trim().indexOf(" "), line.trim().lastIndexOf(" ")).toString().trim();
					break;
				}
			}

			
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return methodName;

	}

	public static void createResponseBuilderFile(HashSet<String> list_of_POJOs) {
		String responseBuilderFilePath = ConstantsPaths.RESPONSE_BUILDER_FILE_PATH;

		try {
			File myObj = new File(responseBuilderFilePath);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}

			EmailTemplate emailTemplate = new EmailTemplate();
			String responseBuilderContent = emailTemplate.getResponseBuilderTemplate(list_of_POJOs);

			FileWriter myWriter = new FileWriter(responseBuilderFilePath, false);
			myWriter.write(responseBuilderContent);
			myWriter.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
		
	
}
