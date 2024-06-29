/*
 * Copyright (C) 2017 Clearstream.TV, Inc. All Rights Reserved.
 * Proprietary and confidential.
 *
 */
package com.rnd.mustache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rnd.factory.FileUtils;
import com.rnd.factory.LoadTestData;

import net.openhft.compiler.CompilerUtils;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;



import com.github.mustachejava.DefaultMustacheFactory;

/**
 * @author Clearstream
 */
@SuppressWarnings(
{ "deprecation" })
public class EmailTemplate
{
	String actionClassName;
	List<String> setters = new ArrayList<String>();
	
	private static final Logger LOG = LogManager.getLogger(EmailTemplate.class);

	/**
	 * Prepares the login feature file template for the test completion based on the
	 * moustache template - PostFeature.mustache/GetFeature.mustache
	 * 
	 * @param pojoClassName
	 * @param apiMethod
	 * @param allKeys
	 */
	public String getFeatureTemplate(String pojoClassName, String apiMethod, Map<String, Boolean> allKeys)
			throws IOException {
		LOG.info("Filling Data into Feature File Template");
		String featureContent = "";
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = null;

		Map<String, String> mappedDetails = new HashMap<String, String>();
		
		String feature = pojoClassName.replace(LoadTestData.apiData.get(pojoClassName).get("type"), "");
		mappedDetails.put("feature", feature);

		if (apiMethod.equalsIgnoreCase("POST")) {
			String columnHeaders = "";
			String fieldValues = "";
			
			for (Map.Entry entry : allKeys.entrySet()) {
				columnHeaders += " | " + entry.getKey().toString();
				fieldValues += " | " + "testData";
			}
			mappedDetails.put("columnHeaders", columnHeaders + " |");
			mappedDetails.put("fieldValues", fieldValues + " |");

			mustache = mf.compile("FeaturePOST.mustache");
		} else if (apiMethod.equalsIgnoreCase("GET")) {
			mustache = mf.compile("FeatureGET.mustache");
		}

		try (StringWriter writer = new StringWriter()) {
			mustache.execute(writer, mappedDetails);
			writer.flush();
			featureContent = writer.toString();
		}

		return featureContent;
	}
	
	/**
	 * Prepares the login step definition file template for the test completion based on the
	 * moustache template - StepDefinitionPOST.mustache/StepDefinitionGET.mustache
	 * 
	 * @param pojoClassName
	 * @param apiMethod
	 */
	public String getStepDefinitionTemplate(String stepDefinitionClassName, String pojoClassName, String apiMethod)
			throws IOException {
		LOG.info("Filling Data into Step Definition File Template");
		String stepDefinitionContent = "";
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = null;

		Map<String, String> mappedDetails = new HashMap<String, String>();
		mappedDetails.put("stepDefinitionClassName", stepDefinitionClassName);
		
		String feature = pojoClassName.replace(LoadTestData.apiData.get(pojoClassName).get("type"), "");
		mappedDetails.put("feature", feature);
		
		actionClassName = pojoClassName + "Actions";
		mappedDetails.put("actionClassName", actionClassName);
		
		mappedDetails.put("apiMethod", apiMethod);

		if (apiMethod.equalsIgnoreCase("POST")) {
			mustache = mf.compile("StepDefinitionPOST.mustache");
		} else if (apiMethod.equalsIgnoreCase("GET")) {
			mustache = mf.compile("StepDefinitionGET.mustache");
		}

		try (StringWriter writer = new StringWriter()) {
			mustache.execute(writer, mappedDetails);
			writer.flush();
			stepDefinitionContent = writer.toString();
		}

		return stepDefinitionContent;
	}
	
	
	/**
	 * Prepares the login step definition file template for the test completion based on the
	 * moustache template - StepDefinitionPOST.mustache/StepDefinitionGET.mustache
	 * 
	 * @param pojoClassName
	 * @param apiMethod
	 * @param allKeys
	 */
	public String getActionTemplate(String actionClassName, String pojoClassName, String apiMethod, Map<String, Boolean> allKeys)
			throws IOException {
		LOG.info("Filling Data into Action File Template");
		String actionContent = "";
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = null;

		Map<String, String> mappedDetails = new HashMap<String, String>();
		mappedDetails.put("actionClassName", actionClassName);
		
		String feature = pojoClassName.replace(LoadTestData.apiData.get(pojoClassName).get("type"), "");
		mappedDetails.put("feature", feature);

		actionClassName = pojoClassName + "Actions";
		mappedDetails.put("actionClassName", actionClassName);

		mappedDetails.put("apiMethod", apiMethod);

		String apiURL = LoadTestData.apiData.get(pojoClassName).get("url");
		mappedDetails.put("apiURL", apiURL);

		if (apiMethod.equalsIgnoreCase("POST")) {
			String setMethods = "";
			//getSetterMethods(pojoClassName);
			setMethods = getSetterMethods(pojoClassName, allKeys);
			/*for (String methodName : setters) {
				setMethods += pojoClassName + "." + methodName + "(\"testData\");";
			}*/
			
			mappedDetails.put("setMethods", setMethods);

			mustache = mf.compile("ActionPOST.mustache");
		} else if (apiMethod.equalsIgnoreCase("GET")) {
			mustache = mf.compile("ActionGET.mustache");
		}

		try (StringWriter writer = new StringWriter()) {
			mustache.execute(writer, mappedDetails);
			writer.flush();
			actionContent = writer.toString();
		}

		return actionContent;
	}
	
	public void getSetterMethods_bkp(String pojoClassName) {
		String pojoClassPath = System.getProperty("user.dir")
				+ "/src/test/resources/output/src/main/java/com/webapi/models/" + pojoClassName + "/" + pojoClassName
				+ ".java";

		String line = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader(pojoClassPath));

			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith("public void ")) {
					String methodName = line.trim().replace("public void ", "");
					methodName = methodName.substring(0, methodName.indexOf("("));
					setters.add(methodName);
				}
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public String getSetterMethods(String pojoClassName, Map<String, Boolean> allKeys) {

		String pojoClassPath = System.getProperty("user.dir")
				+ "/src/test/resources/output/src/main/java/com/webapi/models/" + pojoClassName + "/" + pojoClassName
				+ ".java";

		String line = "";
		String methodCalled = "";

		try {
			for (Map.Entry entry : allKeys.entrySet()) {
				if ((boolean) entry.getValue()) {
					String NestedPOJOClassPath = System.getProperty("user.dir")
							+ "/src/test/resources/output/src/main/java/com/webapi/models/" + pojoClassName + "/"
							+ entry.getKey().toString().replace("_", "") + ".java";

					BufferedReader br = new BufferedReader(new FileReader(NestedPOJOClassPath));
					String currentPOJOClassName = FileUtils.getPOJOClassName(NestedPOJOClassPath);
					methodCalled += currentPOJOClassName + " " + currentPOJOClassName + " = " + "new " + currentPOJOClassName + "();";
					
					while ((line = br.readLine()) != null) {
						if (line.trim().startsWith("public void ")) {
							String methodName = line.trim().replace("public void ", "");
							methodName = methodName.substring(0, methodName.indexOf("("));
							setters.add(methodName);
							
							methodCalled += currentPOJOClassName + "." + methodName + "(\"testData\");";

						} else if (line.trim().startsWith("@Override")) {
							break;
						}
					}
					
					br.close();
					
					String methodNameOfParentPOJO = FileUtils.getMethodName(pojoClassPath, entry.getKey().toString().replace("_", ""));
					methodCalled += pojoClassName + "." + methodNameOfParentPOJO + "(" + currentPOJOClassName + ");";


				} else if (!(boolean) entry.getValue()) {
					
					BufferedReader br = new BufferedReader(new FileReader(pojoClassPath));

					while ((line = br.readLine()) != null) {
						if (line.trim().startsWith("public void ")) {
							String methodName = line.trim().replace("public void ", "");
							methodName = methodName.substring(0, methodName.indexOf("("));
							if (methodName.toLowerCase().contains(entry.getKey().toString().replace("_", ""))) {
								setters.add(methodName);
								methodCalled += pojoClassName + "." + methodName + "(\"testData\");";
								break;
							}
						}
					}
					br.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return methodCalled;
	}


	
	public String getFeatureTemplatebkp(String feature, String featureFilePath, LinkedList<String> allKeys) throws IOException
	{
		LOG.info("Filling Data into Login Feature File Template");
		String emailContent = "";
		
		Map<String, String> mappedDetails = new HashMap<String, String>();
		List<Map<String, String>> baseDetails = new LinkedList<Map<String, String>>();
		baseDetails.add(mappedDetails);

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("LoginFeature.mustache");
		Map<String, List<Map<String, String>>> MappedRows = new HashMap<String, List<Map<String, String>>>();

		MappedRows.put("baseDetails", baseDetails);
		MappedRows.put("tableRow", baseDetails);

		try (StringWriter writer = new StringWriter())
		{
			mustache.execute(writer, MappedRows);
			writer.flush();
			emailContent = writer.toString();
		}

		
		return emailContent;
	}

	public String getResponseBuilderTemplate(HashSet<String> list_of_POJOs) throws IOException {
		LOG.info("Filling Data into Response Builder Template");
		String RSContent = "";
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = null;

		Map<String, String> mappedDetails = new HashMap<String, String>();

		String imports = "";
		for (String pojo : list_of_POJOs) {
			imports += "import com.webapi.models." + pojo + "." + pojo + ";\n";
		}
		mappedDetails.put("imports", imports);

		String instanceVariables = "";
		for (String pojo : list_of_POJOs) {
			instanceVariables += "\tprivate " + pojo + " " + pojo.toLowerCase() + ";\n";
		}
		mappedDetails.put("instanceVariables", instanceVariables);

		String methods = "";
		for (String pojo : list_of_POJOs) {
			methods += "\tpublic " + pojo + " get" + pojo + "() {\n" + "\t\treturn " + pojo.toLowerCase() + ";\n"
					+ "\t}\n\n";

			methods += "\tpublic void set" + pojo + "(" + pojo + " " + pojo.toLowerCase() + ") {\n" + "\t\tthis."
					+ pojo.toLowerCase() + " = " + pojo.toLowerCase() + ";\n" + "\t}\n\n";
		}
		mappedDetails.put("gettersAndSetters", methods);

		mustache = mf.compile("ResponseBuilder.mustache");

		try (StringWriter writer = new StringWriter()) {
			mustache.execute(writer, mappedDetails);
			writer.flush();
			RSContent = writer.toString();
		}

		return RSContent;

	}

		
}
