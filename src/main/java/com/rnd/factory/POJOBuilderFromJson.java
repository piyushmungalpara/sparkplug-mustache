package com.rnd.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.jsonschema2pojo.DefaultGenerationConfig;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.GsonAnnotator;
import org.jsonschema2pojo.SchemaGenerator;
import org.jsonschema2pojo.SchemaMapper;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.SourceType;
import org.jsonschema2pojo.rules.RuleFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rnd.utility.Constants;
import com.rnd.utility.ConstantsPaths;
import com.sun.codemodel.JCodeModel;

public class POJOBuilderFromJson {
	public static HashSet<String> list_of_POJOs = new HashSet<>();

	public void buildJSON(String response) {
		try {

			final GenerationConfig config = new DefaultGenerationConfig() {
				@Override
				public SourceType getSourceType() {
					return SourceType.JSON;
				}

				/*
				 * @Override public boolean isIncludeAdditionalProperties() { return true; }
				 */

			};

			File jsonFilePath = new File(ConstantsPaths.JSON_FILE_DIR_PATH);
			File[] jsonFiles = jsonFilePath.listFiles();

			for (File file : jsonFiles) {
				String fileName = file.getName();

				String resourcePath = "/Schema/" + fileName;

				URL source = POJOBuilderFromJson.class.getResource(resourcePath);
				// System.out.println(source);
				final JCodeModel codeModel = new JCodeModel();
				// final RuleFactory ruleFactory = new RuleFactory(config, new
				// Jackson2Annotator(config), new SchemaStore());
				final RuleFactory ruleFactory = new RuleFactory(config, new GsonAnnotator(config), new SchemaStore());
				final SchemaMapper schemaMapper = new SchemaMapper(ruleFactory, new SchemaGenerator());

				String pojoClassName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1,
						resourcePath.lastIndexOf("."));
				schemaMapper.generate(codeModel, pojoClassName,
						"com.webapi.models." + pojoClassName, source);

				String dirPath = ConstantsPaths.POJO_PATH;
				// File file = new File(dirPath);

				Path path = Paths.get(dirPath);
				if (!new File(dirPath).isDirectory()) {
					path = Files.createDirectory(path);
				}

				System.out.println(path);
				codeModel.build(path.toFile());

				ObjectMapper mapper = new ObjectMapper();
				final JsonNode rootNode = mapper.readTree(file);
				Map<String, Boolean> allKeys = new HashMap<String, Boolean>();
				allKeys = getAllKeys(rootNode);

				LoadTestData.loadDataFromJsonFile();
				String apiMethod = LoadTestData.apiData.get(pojoClassName).get("method");
				
				if (LoadTestData.apiData.get(pojoClassName).get("createFeature").equals("true")) {

					FileUtils.createFeatureFile(pojoClassName, apiMethod, allKeys);
					FileUtils.createStepDefinitonFile(pojoClassName, apiMethod);
					FileUtils.createActionFile(pojoClassName, apiMethod, allKeys);
				}
				
				if (LoadTestData.apiData.get(pojoClassName).get("type").equals("response")) {
					list_of_POJOs.add(pojoClassName);
				}
				
			}
			
			FileUtils.createResponseBuilderFile(list_of_POJOs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private static Map<String, Boolean> getAllKeys(final JsonNode node) throws IOException {
		Map<String, Boolean> allNodes = new HashMap<String, Boolean>();
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();

		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			final String key = field.getKey();
			final JsonNode value = field.getValue();
			allNodes.put(key, field.getValue().isContainerNode());
			/*
			 * if (value.isContainerNode()) { getAllKeys(value); // RECURSIVE CALL } else {
			 * System.out.println("Value: " + value); }
			 */
		}
		System.out.println(allNodes);
		return allNodes;
	}
}
